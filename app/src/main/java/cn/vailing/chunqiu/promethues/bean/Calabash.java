package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;


import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import java.io.IOException;

import cn.vailing.chunqiu.promethues.is.MIAnimationListener;
import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.manager.FlameManager;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;


/**
 * Created by dream on 2017/6/24.
 */

public class Calabash extends MyBaseSprite {
    private boolean isVisible = false;
    private LoopEntityModifier loopRotateModifier;
    private int speed;
    private IUpdateHandler iupdateHandler;
    private boolean isCollided;
    private float centerY;
    private float centerX;
    private Rectangle rectangle;
    private FlameManager flameManager;
    protected BitmapTextureAtlas fireReadyTexture;
    protected TiledTextureRegion fireReadyTextureRegion;
    protected AnimatedSprite fireReady;
    private Rectangle Bkrectangle;
    private BitmapTextureAtlas fireTexture;
    private TiledTextureRegion fireTextureRegion;
    private AnimatedSprite fire;
    private Rectangle collideRect;
    private IUpdateHandler noCollideIU;
    private IUpdateHandler collideIU;
    private Sound mdeviceDownSound;
    private Sound mdeviceUpSound;
    private Sound mCalabashUpSound;
    private float myX;
    private float myY;
    private TextureRegion rotateTextureRegion;
    private Sprite rotate;
    private int testX;
    private int testY;
    private float startX;
    private float startY;
    private float mirrorAgree;
    private boolean isRotate;
    private Scene scene;

    public Calabash(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/Calabash.png", new Vector2(150, 150), new Vector2(1, 1));
    }

    @Override
    public void Myinit() {
        textureRegion = TextureHelper.getInstance().getCalabashRegion();
    }

    @Override
    public void setInScene(Scene scene, float x, float y, float xScale, float yScale, long frame, final MySpriteOnclickListener mySpriteOnclickListener) {
        this.wScale = xScale;
        this.hScale = yScale;
        this.scene = scene;
        sprite = new AnimatedSprite(0, 0, this.textureRegion, engine.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (flameManager.isCreate()) {
                    return setTouched(pSceneTouchEvent);
                } else {
                    return false;
                }
            }
        };
        this.myX = x;
        this.myY = y;
        Bkrectangle = new Rectangle(x, y, textureRegion.getWidth(), textureRegion.getHeight(), engine.getVertexBufferObjectManager());
        Bkrectangle.setColor(0, 0, 0, 0);
        Bkrectangle.setScale(xScale, yScale);
        scene.attachChild(Bkrectangle);
        float rotateX = sprite.getRotationCenterX() - rotateTextureRegion.getWidth() * 0.5f;
        float rotateY = sprite.getRotationCenterY() - rotateTextureRegion.getHeight() * 0.5f;
        rotate = new Sprite(rotateX, rotateY, rotateTextureRegion, engine.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                return setRotate(pSceneTouchEvent);
            }
        };
        Bkrectangle.attachChild(rotate);
        rotate.setZIndex(0);
        testX = (int) (myX + rotate.getWidth() * wScale / 2);
        testY = (int) (myY + rotate.getHeight() * hScale / 2);
        rotate.setVisible(false);
        Bkrectangle.attachChild(sprite);
        scene.registerTouchArea(sprite);

        centerX = x + Bkrectangle.getWidth() / 2;
        centerY = y + Bkrectangle.getHeight() / 2;
        rectangle = new Rectangle(sprite.getWidth() * 0.1f, 0, sprite.getWidth() * 0.8f, 1, engine.getVertexBufferObjectManager());
        rectangle.setColor(0, 0, 0, 0);
        sprite.attachChild(rectangle);

        collideRect = new Rectangle(sprite.getWidth() * 0.1f, sprite.getHeight() * 0.1f, sprite.getWidth() * 0.8f, sprite.getHeight() * 0.8f, engine.getVertexBufferObjectManager());
        sprite.attachChild(collideRect);
        collideRect.setColor(0, 0, 0, 0);
        fireReady = new AnimatedSprite(sprite.getWidth() * -0.185f, sprite.getHeight() * -0.525f, fireReadyTextureRegion, engine.getVertexBufferObjectManager());
        Bkrectangle.attachChild(fireReady);

        fire = new AnimatedSprite(sprite.getWidth() * -0.185f, sprite.getHeight() * -0.525f, fireTextureRegion, engine.getVertexBufferObjectManager());
        Bkrectangle.attachChild(fire);
        fire.setVisible(false);
    }

    private boolean setTouched(TouchEvent pSceneTouchEvent) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                playSound(mdeviceDownSound);
                break;
            case TouchEvent.ACTION_UP:
                playSound(mdeviceUpSound);
                if (isCollided) {
                    isCollided = false;
                    fireFlame();
                }
                break;
        }
        return true;
    }

    private void fireFlame() {
        setRotate(0);
        fireReady.setVisible(true);
        fireReady.stopAnimation(0);
        fire.stopAnimation();
        fire.setVisible(false);
        flameManager.setVisible(0, true);
        flameManager.start(
                centerX,
                centerY,
                sprite.getRotation() - 90,
                sprite.getWidth() * wScale,
                new OnFinishListener() {
                    @Override
                    public void finish() {
                        if (isRotate) {
                            setCanBeRotated(scene);
                            sprite.unregisterEntityModifier(loopRotateModifier);
                        }
                        mirrorAgree = sprite.getRotation();
                        Bkrectangle.registerUpdateHandler(noCollideIU);
                    }
                });

    }

    public void setRotation(float angle) {
        sprite.setRotation(angle);
    }

    public void setRotate(int speed) {
        if (speed == 0) {
            sprite.clearEntityModifiers();
            return;
        }
        this.speed = speed;
        int time = 360 / this.speed;
        loopRotateModifier = new LoopEntityModifier(
                new RotationModifier(time, sprite.getRotation(), 360 + sprite.getRotation())
        );
        sprite.registerEntityModifier(loopRotateModifier);

    }

    public void setCanBeRotated(Scene scene) {
        isRotate = true;
        rotate.setRotation(sprite.getRotation());
        rotate.setVisible(true);
        rotate.registerEntityModifier(new ScaleModifier(1f, 0, 1f));
        scene.registerTouchArea(rotate);
    }


    private boolean setRotate(TouchEvent pSceneTouchEvent) {
        switch (pSceneTouchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = pSceneTouchEvent.getX();
                startY = pSceneTouchEvent.getY();
                startX -= testX;
                startY -= testY;
                break;
            case MotionEvent.ACTION_MOVE:
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
                    agree /= 1.5;
                    if (agree > 0) {
                        mirrorAgree += 3f;
                    } else {
                        mirrorAgree -= 3f;
                    }
                    sprite.setRotation(mirrorAgree);
                    rotate.setRotation(mirrorAgree);
                    startX = (float) newX;
                    startY = (float) newY;
                }
                break;
        }
        return true;
    }

    public void setCollides(final FlameManager flameManager) {
        Calabash.this.flameManager = flameManager;
        iupdateHandler = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    if (rectangle.collidesWith(flameManager.getFlameSprite()) && !flameManager.isChange()) {
                        playSound(mCalabashUpSound);
                        flameManager.setVisibleByScale(false, sprite.getWidth() * wScale, null);
                        isCollided = true;
                        sprite.unregisterUpdateHandler(iupdateHandler);
                        collideRect.unregisterUpdateHandler(collideIU);
                        fireReady.animate(70, false, new MIAnimationListener() {
                            @Override
                            public void onAnimationFinished(AnimatedSprite animatedSprite) {
                                fire.setVisible(true);
                                fire.animate(60);
                                fireReady.setVisible(false);
                            }
                        });
                        if (isRotate) {
                            setRotate((int) (60f * wScale));
                            scene.unregisterTouchArea(rotate);
                            rotate.registerEntityModifier(new ScaleModifier(2f, 1, 0));
                        }
                    }
                }

            }

            @Override
            public void reset() {

            }
        };
        sprite.registerUpdateHandler(iupdateHandler);
        noCollideIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    if (!Bkrectangle.collidesWith(flameManager.getFlameSprite())) {
                        sprite.registerUpdateHandler(iupdateHandler);
                        Bkrectangle.unregisterUpdateHandler(noCollideIU);
                        collideRect.registerUpdateHandler(collideIU);
                        if (!isRotate)
                            setRotate(speed);
                    }
                }
            }

            @Override
            public void reset() {

            }
        };
        collideIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (collideRect.collidesWith(flameManager.getFlameSprite()) && !flameManager.isChange()) {
                    flameManager.boom(new OnFinishListener() {
                        @Override
                        public void finish() {
                            flameManager.finish();
                        }
                    });
                }
            }

            @Override
            public void reset() {

            }
        };
        collideRect.registerUpdateHandler(collideIU);
    }

    @Override
    protected void init() {
        speed = (int) (60f* ScaleHelper.getInstance().getHeightScale());
        fireReadyTextureRegion = TextureHelper.getInstance().getCalabashFireReadyRegion();
        fireTextureRegion = TextureHelper.getInstance().getCalabashFireRegion();
        rotateTextureRegion = TextureHelper.getInstance().getCalabashRotateRegion();
        try {
            mdeviceDownSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/deviceDown.ogg");
            mdeviceUpSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/deviceUp.ogg");
            mCalabashUpSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/calabash.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
