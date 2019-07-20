package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;
import android.os.Bundle;
import com.badlogic.gdx.math.Vector2;


import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import java.io.IOException;

import cn.vailing.chunqiu.promethues.is.MIAnimationListener;
import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.manager.FlameManager;
import cn.vailing.chunqiu.promethues.util.MathUtil;
import cn.vailing.chunqiu.promethues.util.PathContentValue;
import cn.vailing.chunqiu.promethues.util.PathHelper;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;


/**
 * Created by dream on 2017/7/3.
 */

public class GravityDevice extends MyBaseSprite implements IAccelerationListener {
    private Rectangle collision;
    private float angle;
    private BitmapTextureAtlas pointTexture;
    private ITextureRegion pointTextRegion;
    private Scene scene;
    private boolean isVisible = true;
    private IUpdateHandler locationIU;
    private IUpdateHandler rotateIU;
    private boolean isExistFlame;
    private IUpdateHandler collisionIU;
    private IUpdateHandler searchIUpdateHandler;
    private boolean isDark;
    private FlameManager flameManager;
    private PhysicsWorld physicsWorld;
    private boolean isMove;
    private int gAngle;
    private Sound mMoveSound;

    public GravityDevice(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/GravityDeformation.png", new Vector2(890, 150), new Vector2(10, 1));
    }

    @Override
    public void setInScene( Scene scene, float x, float y, float xScale, float yScale, long frame, final MySpriteOnclickListener mySpriteOnclickListener) {
        super.setInScene(scene, x, y, xScale, yScale, 0, new MySpriteOnclickListener() {
            @Override
            public boolean onTouchEvent(AnimatedSprite animatedSprite, TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                return setOnTouched(pSceneTouchEvent);
            }
        });
        collision = new Rectangle(0, sprite.getHeight(), sprite.getWidth(), 3, engine.getVertexBufferObjectManager());
        collision.setColor(0, 0, 0, 0);
        sprite.attachChild(collision);
        physicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
        scene.registerUpdateHandler(physicsWorld);
        engine.enableAccelerationSensor(context, this);
    }

    private boolean setOnTouched(TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN && flameManager.isCreate()) {
            if (isExistFlame) {
                setMove(false);
                fireFlame();
            }
        }
        return true;
    }

    private void fireFlame() {
        setVisible();
        isExistFlame = false;
        flameManager.setVisible(0, true);
        flameManager.start(
                sprite.getX() + textureRegion.getWidth() / 2,
                sprite.getY() + textureRegion.getHeight() / 2,
                sprite.getRotation() - 90,
                sprite.getWidth(),
                new OnFinishListener() {
                    @Override
                    public void finish() {
                        collision.registerUpdateHandler(collisionIU);
                        setMove(isMove);
                        if (searchIUpdateHandler != null) {
                            sprite.unregisterUpdateHandler(searchIUpdateHandler);
                        }
                    }
                });
    }

    public void setPhysic() {
        rotateIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                angle = -(int) Math.toDegrees(Math.atan2(physicsWorld.getGravity().y, physicsWorld.getGravity().x));
//                if(angle+90)
//                Log.e("vailing",(angle+90)+"  ");
                if (angle + 90 >= -20 && angle+90 <= 20) {
//                    Log.e("vailing",(angle+90)+"  ");
                } else if (angle + 90 < -20) {
                    setRotation(--gAngle);
                } else if (angle + 90 > 20) {

                    setRotation(++gAngle);
                }
//                setRotation((int) (angle + 90));
            }

            @Override
            public void reset() {

            }
        };

    }

    public void setMove(boolean isMove) {
        if (isMove) {
            sprite.registerUpdateHandler(locationIU);
            mMoveSound.setLoaded(true);
            mMoveSound.play();
        } else {
            sprite.unregisterUpdateHandler(locationIU);
            mMoveSound.stop();
        }
    }

    public void setIfMove(boolean isMove) {
        this.isMove = isMove;
        setMove(isMove);
    }

    public void setRotate(boolean isRotate) {
        if (isRotate) {
            sprite.registerUpdateHandler(rotateIU);
        } else {
            sprite.unregisterUpdateHandler(rotateIU);
        }
    }

    public void setCollision(final FlameManager flameManager) {
        GravityDevice.this.flameManager = flameManager;
        collisionIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (collision.collidesWith(flameManager.getFlameSprite())) {
                    float testAngle = MathUtil.getMinAngle(flameManager.getAngle()) - MathUtil.getMinAngle(sprite.getRotation());
                    if (-testAngle > 0 && -testAngle < 180) {
                        setMove(!isMove);
                        flameManager.setVisibleByScale(false, sprite.getHeight(), null);
                        isExistFlame = true;
                        collision.clearUpdateHandlers();
                        sprite.animate(60, false);
                        if (isDark) {
                            sprite.registerUpdateHandler(searchIUpdateHandler);
                        }
                    }
                }
            }

            @Override
            public void reset() {

            }
        };
        collision.registerUpdateHandler(collisionIU);
    }

    public void setDark(final Dark dark) {
        isDark = true;
        searchIUpdateHandler = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                dark.setVisible(getCenterX(), getCenterY());
            }

            @Override
            public void reset() {

            }
        };
    }

    private int getCenterX() {
        return (int) (sprite.getX() + sprite.getWidth() * wScale / 2);
    }

    public int getCenterY() {
        return (int) (sprite.getY());
    }

    public void setVisible() {
        sprite.stopAnimation();
        texture.clearTextureAtlasSources();
        BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "device/GravityOver.png", 0, 0, (int) tiledSize.x, (int) tiledSize.y);
        sprite.animate(60, false, new MIAnimationListener() {
            @Override
            public void onAnimationFinished(AnimatedSprite animatedSprite) {
                texture.clearTextureAtlasSources();
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "device/GravityDeformation.png", 0, 0, (int) tiledSize.x, (int) tiledSize.y);
                sprite.stopAnimation(0);
            }
        });
    }

    public void setLocation(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void setRotation(int angle) {
        sprite.setRotation(angle);
    }

    public void setPath( Scene scene, int way, Bundle bundle, MySpriteOnclickListener mySpriteOnclickListener) {
        this.scene = scene;
        switch (way) {
            case PathContentValue.LINE:
                setLine(bundle, mySpriteOnclickListener);
                break;
        }
    }

    private void setLine(Bundle bundle, MySpriteOnclickListener mySpriteOnclickListener) {
        int beginX = bundle.getInt("beginX");
        int beginY = bundle.getInt("beginY");
        int endX = bundle.getInt("endX");
        int endY = bundle.getInt("endY");
        int length = (int) MathUtil.pythagorean(beginX, beginY, endX, endY);
        int N = 40;
        int n = length / N;
        float offsetX = (endX - (float) beginX) / (float) n;
        float offsetY = (endY - (float) beginY) / (float) n;
        float wSO = wScale / n;
        float hSO = hScale / n;
        final Sprite[] sprites = new Sprite[n];
        for (int i = 0; i <= n / 2; i++) {
            sprites[i] = new Sprite(ScaleHelper.getInstance().getCenterXLoction(beginX + i * offsetX, (int) pointTextRegion.getWidth()),
                    ScaleHelper.getInstance().getCenterYLoction(beginY + i * offsetY, (int) pointTextRegion.getHeight()), pointTextRegion, engine.getVertexBufferObjectManager());
            scene.attachChild(sprites[i]);
            sprites[i].setScale(wScale / 2 + wSO * i, hScale / 2 + hSO * i);

            sprites[n - i - 1] = new Sprite(ScaleHelper.getInstance().getCenterXLoction(endX - i * offsetX, (int) pointTextRegion.getWidth()),
                    ScaleHelper.getInstance().getCenterYLoction(endY - i * offsetY, (int) pointTextRegion.getHeight()), pointTextRegion, engine.getVertexBufferObjectManager());
            scene.attachChild(sprites[n - i - 1]);
            sprites[n - i - 1].setScale(wScale / 2 + wSO * i, hScale / 2 + hSO * i);

        }
        setInScene(scene,
                ScaleHelper.getInstance().getCenterXLoction(beginX, textureRegion.getWidth()),
                ScaleHelper.getInstance().getCenterYLoction(beginY, textureRegion.getHeight()),
                wScale, hScale, 100, mySpriteOnclickListener);
        final PathHelper pathHelper = new PathHelper(beginX, beginY, endX, endY, 0.8f);
        locationIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                setLocation(ScaleHelper.getInstance().getCenterXLoction(pathHelper.linePath().x, textureRegion.getWidth()),
                        ScaleHelper.getInstance().getCenterYLoction(pathHelper.linePath().y, textureRegion.getHeight()));
            }

            @Override
            public void reset() {

            }
        };
    }

    @Override
    protected void init() {
        pointTexture = new BitmapTextureAtlas(engine.getTextureManager(), 16, 16, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        pointTextRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(pointTexture, context, "gfx/path.png", 0, 0);
        pointTexture.load();
        setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());
        try {
            mMoveSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/gravityMove.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccelerationAccuracyChanged(AccelerationData accelerationData) {

    }

    @Override
    public void onAccelerationChanged(AccelerationData accelerationData) {
        final Vector2 gravity = Vector2Pool.obtain(accelerationData.getX(), accelerationData.getY());
        this.physicsWorld.setGravity(gravity);
        Vector2Pool.recycle(gravity);
    }
}
