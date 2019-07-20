package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;


import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import java.io.IOException;

import cn.vailing.chunqiu.promethues.is.MIAnimationListener;
import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.manager.FlameManager;
import cn.vailing.chunqiu.promethues.util.MathUtil;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/7/7.
 */

public class Plane extends MyBaseSprite implements IAccelerationListener {
    private Rectangle collision;
    private float angle;
    private Scene scene;
    private boolean isVisible = true;
    private IUpdateHandler locationIU;
    private IUpdateHandler rotateIU;
    private boolean isExistFlame;
    private FlameManager flameManager;
    private IUpdateHandler collisionIU;
    private PhysicsWorld physicsWorld;
    protected TiledTextureRegion flameTextureRegion;
    protected AnimatedSprite flame;
    private int gAngle;
    private Sound mMoveSound;

    public Plane(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/PlaneDeformation.png", new Vector2(900, 300), new Vector2(5, 2));
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
        deformate(false);
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
                    }
                });
    }

    public void setPhysic() {
        rotateIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                angle = -(int) Math.toDegrees(Math.atan2(physicsWorld.getGravity().y, physicsWorld.getGravity().x));
                if (angle + 90 >= -20 && angle + 90 <= 20) {
//                    Log.e("vailing",(angle+90)+"  ");
                } else if (angle + 90 < -20) {
                    setRotation(--gAngle);
                } else if (angle + 90 > 20) {

                    setRotation(++gAngle);
                }
            }

            @Override
            public void reset() {

            }
        };
        locationIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                float x = sprite.getX() + MathUtil.Rcos(gAngle);
                float y = sprite.getY() + MathUtil.Rsin(gAngle);
                setLocation(x, y);
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

    public void setRotate(boolean isRotate) {
        if (isRotate) {
            sprite.registerUpdateHandler(rotateIU);
        } else {
            sprite.unregisterUpdateHandler(rotateIU);
        }
    }

    public void setCollision(final FlameManager flameManager) {
        Plane.this.flameManager = flameManager;
        collisionIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    if (collision.collidesWith(flameManager.getFlameSprite())) {
                        float testAngle = MathUtil.getMinAngle(flameManager.getAngle()) - MathUtil.getMinAngle(sprite.getRotation());
                        if (-testAngle > 0 && -testAngle < 180) {
                            deformate(true);
                            flameManager.setVisibleByScale(false, sprite.getHeight(), null);
                            isExistFlame = true;
                            collision.clearUpdateHandlers();
                            setMove(true);

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

    private void deformate(boolean isDeformate) {
        if (isDeformate) {
            flame = new AnimatedSprite(sprite.getWidth() / 2 - flameTextureRegion.getWidth() / 2, -flameTextureRegion.getHeight() / 3, flameTextureRegion, engine.getVertexBufferObjectManager());
            sprite.attachChild(flame);
            flame.animate(70);
            sprite.animate(80, false);
        } else {
            sprite.detachChild(flame);
            flame.dispose();
            sprite.stopAnimation();
            texture.clearTextureAtlasSources();
            BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "device/PlaneOver.png", 0, 0, (int) tiledSize.x, (int) tiledSize.y);
            sprite.animate(80, false, new MIAnimationListener() {

                @Override
                public void onAnimationFinished(AnimatedSprite animatedSprite) {
                    texture.clearTextureAtlasSources();
                    BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "device/PlaneDeformation.png", 0, 0, (int) tiledSize.x, (int) tiledSize.y);
                    sprite.stopAnimation(1);
                }
            });
        }

    }


    public void setLocation(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void setRotation(int angle) {
        sprite.setRotation(angle);
    }

    public void startMove() {

    }

    @Override
    protected void init() {
        flameTextureRegion = TextureHelper.getInstance().getFlameStaticRegion();
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
