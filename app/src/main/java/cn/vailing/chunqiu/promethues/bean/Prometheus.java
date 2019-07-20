package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.modifier.IModifier;

import cn.vailing.chunqiu.promethues.is.MIAnimationListener;
import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.manager.FlameManager;
import cn.vailing.chunqiu.promethues.util.OtherUtil;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/8/10.
 */

public class Prometheus extends MyBaseSprite {
    private FlameManager flameManager;
    private boolean isChange;
    private double startX = 0;
    private double startY = 0;
    private int testX;
    private int testY;
    private float fireX;
    private float fireY;
    private int mirrorAgree;
    private Rectangle rectangle;
    private TextureRegion handRegion;
    private TextureRegion footRegion;
    private TextureRegion bodyRegion;
    private Sprite foot;
    private Sprite hand;
    private Sprite body;
    private boolean isReady;
    private Scene scene;
    private Rectangle fireRect;
    private TiledTextureRegion flameRegion;
    private AnimatedSprite flameHand;
    private AnimatedSprite flameReady;
    private boolean isFire;
    private TextureRegion dashedRegion;
    private Sprite dashed;

    public Prometheus(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/Prometheus.png", new Vector2(3900, 600), new Vector2(13, 2));
    }

    public void setFlameManager(FlameManager flameManager) {
        this.flameManager = flameManager;
    }

    @Override
    public void setInScene(final Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {
        float rotateX = x + textureRegion.getWidth() / 2;
        float rotateY = y + textureRegion.getHeight() / 2;
        this.scene = scene;
        rectangle = new Rectangle(rotateX - textureRegion.getWidth() * 0.9f, rotateY - textureRegion.getHeight() * 0.9f,
                textureRegion.getWidth() * 1.8f, textureRegion.getHeight() * 1.8f, engine.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                return setTouch(pSceneTouchEvent);
            }
        };
        rectangle.setScale(xScale, yScale);
        rectangle.setColor(0,0,0,0);
        scene.attachChild(rectangle);
        testX = (int) (rotateX + rectangle.getWidth() / 2);
        testY = (int) (rotateY + rectangle.getHeight() / 2);
        scene.registerTouchArea(rectangle);
        super.setInScene(scene, x, y, xScale, yScale, 0, null);
//        sprite.setScale(-1,1);
        flameReady = new AnimatedSprite(sprite.getWidth() * 0.73f - flameRegion.getWidth() * 0.5f,
                sprite.getHeight() * 0.2f - flameRegion.getHeight() * 0.5f,
                flameRegion, engine.getVertexBufferObjectManager());
        flameReady.animate(50);
        sprite.attachChild(flameReady);

        foot = new Sprite(x, y, footRegion, engine.getVertexBufferObjectManager());
        foot.setScale(wScale, hScale);
        foot.setRotationCenter(foot.getWidth() * 0.445f, foot.getHeight() * 0.795f);
        foot.setVisible(false);
        scene.attachChild(foot);
        body = new Sprite(0, 0, bodyRegion, engine.getVertexBufferObjectManager());
        foot.attachChild(body);
        body.setRotationCenter(body.getWidth() * 0.415f, body.getHeight() * 0.69f);

        hand = new Sprite(0, 0, handRegion, engine.getVertexBufferObjectManager());
        hand.setRotationCenter(body.getWidth() * 0.38f, body.getHeight() * 0.46f);
        body.attachChild(hand);

        flameHand = new AnimatedSprite(hand.getWidth() * 0.13f - flameRegion.getWidth() * 0.5f,
                hand.getHeight() * 0.1f - flameRegion.getHeight() * 0.5f,
                flameRegion, engine.getVertexBufferObjectManager());
        flameHand.animate(50);
        hand.attachChild(flameHand);
        dashed = new Sprite(rectangle.getWidth() * 0.5f, rectangle.getHeight() * 0.25f, dashedRegion, engine.getVertexBufferObjectManager());
        dashed.setRotationCenter(dashed.getWidth() * 0, dashed.getHeight() * 0.5f);
//        * (1 - wScale) / 2

        rectangle.attachChild(dashed);
//        dashed.setScale(wScale,hScale);
        dashed.setVisible(false);
        fireX = rectangle.getWidth() * 0.5f * wScale + rectangle.getX() + rectangle.getWidth() * (1 - wScale) / 2;
        fireY = rectangle.getHeight() * 0.25f * hScale + rectangle.getY() + rectangle.getHeight() * (1 - hScale) / 2;
//        fireX = dashed.getX() + dashed.getWidth() * (1 - wScale) / 2+rectangle.getX();
//        fireY = dashed.getY() + dashed.getHeight() * (1 - hScale) / 2 +rectangle.getY();
    }

    private boolean setTouch(TouchEvent pSceneTouchEvent) {
        if (!flameManager.canBeCreate()) {
            return true;
        }
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                if (!isChange)
                    changeToOnFire();
                startX = pSceneTouchEvent.getX();
                startY = pSceneTouchEvent.getY();
                startX -= testX;
                startY -= testY;
                break;
            case TouchEvent.ACTION_MOVE:
                if (isReady) {
                    double newX = pSceneTouchEvent.getX();
                    double newY = pSceneTouchEvent.getY();
                    newX -= testX;
                    newY -= testY;
                    double A = (startX * newX + startY * newY) / (Math.sqrt(startX * startX + startY * startY) * Math.sqrt(newX * newX + newY * newY));
                    if (A <= 1) {
                        double agree = Math.abs(Math.toDegrees(Math.acos(A)));
                        double B = startX * newY - startY * newX;
                        if (B < 0) {
                            agree = -agree;
                        }
                        if (agree > 0) {
                            if (mirrorAgree >= 50) {
                                mirrorAgree = 50;
                            } else {
                                mirrorAgree += 1f;
                            }
                        } else {
                            if (mirrorAgree <= -50) {
                                mirrorAgree = -50;
                            } else {
                                mirrorAgree -= 1f;
                            }
                        }
                        if (mirrorAgree > -50 && mirrorAgree < 50) {
                            setRotation(mirrorAgree);
                        }
                        startX = (float) newX;
                        startY = (float) newY;
                    }
                }
                break;
            case TouchEvent.ACTION_UP:
//                if(!isFire){
                flameReady.setVisible(false);
                changeToFire();
//                }


                break;
        }
        return true;
    }

    private void setRotation(int mirrorAgree) {
//        isFire =ture;
        //-20--35
        //-20--5
        if (mirrorAgree >= -20 && mirrorAgree < 35) {
            body.setRotation(mirrorAgree);
            dashed.setRotation(mirrorAgree);
        } else if ((mirrorAgree >= -40 && mirrorAgree < -20)) {
            body.setRotation(-20);
            dashed.setRotation(mirrorAgree);

            hand.setRotation(mirrorAgree + 20);
        } else if ((mirrorAgree >= 35 && mirrorAgree < 40)) {
            body.setRotation(35);
            dashed.setRotation(mirrorAgree);

            hand.setRotation(mirrorAgree - 35);
        } else if ((mirrorAgree >= -50 && mirrorAgree < -40)) {
            body.setRotation(-20);
            hand.setRotation(-20);
            dashed.setRotation(mirrorAgree);
            foot.setRotation(mirrorAgree + 40);
            sprite.setRotation(mirrorAgree + 40);
        } else if ((mirrorAgree >= 40 && mirrorAgree < 50)) {
            body.setRotation(35);
            dashed.setRotation(mirrorAgree);
            hand.setRotation(5);
            foot.setRotation(mirrorAgree - 40);
            sprite.setRotation(mirrorAgree - 40);
        }

    }

    public void defeated(final OnFinishListener onFinishListener) {
//        texture.clearTextureAtlasSources();
//        BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "gfx/Splash/PrometheusDie.png", 0, 0, 9, 1);
//        sprite.animate(OtherUtil.getAnimateFrame(26, 9, 150), false, new MIAnimationListener() {
//
//            @Override
//            public void onAnimationFinished(AnimatedSprite animatedSprite) {
//                if (onFinishListener != null) {
//                    onFinishListener.finish();
//                }
//            }
//        });
    }

    private void changeToFire() {
        final float angle = hand.getRotation() + body.getRotation() + foot.getRotation();
        sprite.setVisible(true);
        foot.setVisible(false);
        sprite.animate(OtherUtil.getAnimateFrameAfter(26, 6, 13, 100), false, new MIAnimationListener() {
            @Override
            public void onAnimationFinished(AnimatedSprite animatedSprite) {
                flameManager.createFlame(scene, fireX, fireY, angle);
                foot.registerEntityModifier(new AlphaModifier(0.4f, 1, 1, new IEntityModifier.IEntityModifierListener() {
                    @Override
                    public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {

                    }

                    @Override
                    public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                        sprite.animate(OtherUtil.getAnimateFrameAfter(26, 13, 26, 100), false, new MIAnimationListener() {
                            @Override
                            public void onAnimationFinished(AnimatedSprite animatedSprite) {
                                sprite.setRotation(0);
                                hand.setRotation(0);
                                body.setRotation(0);
                                foot.setRotation(0);
                                mirrorAgree = 0;
                                isChange = false;
                                flameReady.setVisible(true);
                                dashed.setRotation(0);
                                dashed.setVisible(false);
                            }
                        });
                    }
                }));

            }
        });
    }

    private void changeToOnFire() {
        flameReady.setVisible(false);
        sprite.animate(OtherUtil.getAnimateFrame(26, 6, 100), false, new MIAnimationListener() {
            @Override
            public void onAnimationFinished(AnimatedSprite animatedSprite) {
                foot.setVisible(true);
                sprite.setVisible(false);
                isReady = true;
                dashed.setVisible(true);
            }
        });
    }

    public float getCenterX() {
        return sprite.getX() + sprite.getWidth() / 2;
    }

    public float getCenterY() {
        return sprite.getY() + sprite.getHeight() / 2;
    }

    public float getAngle() {
        return sprite.getRotation();
    }

    @Override
    protected void init() {
        BitmapTextureAtlas hand = new BitmapTextureAtlas(engine.getTextureManager(), 200, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        handRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hand, context, "device/Hand.png", 0, 0);
        hand.load();

        BitmapTextureAtlas foot = new BitmapTextureAtlas(engine.getTextureManager(), 200, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        footRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(foot, context, "device/Foot.png", 0, 0);
        foot.load();

        BitmapTextureAtlas body = new BitmapTextureAtlas(engine.getTextureManager(), 200, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        bodyRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(body, context, "device/Body.png", 0, 0);
        body.load();

        BitmapTextureAtlas flame = new BitmapTextureAtlas(engine.getTextureManager(), 1350, 50, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        flameRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(flame, context, "device/FlameStatic.png", 0, 0, 27, 1);
        flame.load();

        BitmapTextureAtlas dashed = new BitmapTextureAtlas(engine.getTextureManager(), 320, 10, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        dashedRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(dashed, context, "device/Dashed.png", 0, 0);
        dashed.load();
    }
}
