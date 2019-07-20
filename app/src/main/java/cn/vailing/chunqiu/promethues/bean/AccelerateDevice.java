package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;


import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.modifier.IModifier;

import java.io.IOException;

import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.manager.FlameManager;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/7/10.
 */

public class AccelerateDevice extends MyBaseSprite {
    protected BitmapTextureAtlas topTexture;
    protected TiledTextureRegion topTextureRegion;
    protected BitmapTextureAtlas downTexture;
    protected TiledTextureRegion downTextureRegion;
    private Rectangle button;
    private Rectangle collideRect;
    private Scene scene;
    private IUpdateHandler collideIU;
    private Sound mdeviceDownSound;
    private Sound mdeviceUpSound;
    private Sound mAccelerateSound;

    public AccelerateDevice(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/AccelerateButton.png", new Vector2(200, 100), new Vector2(2, 1));
    }

    @Override
    public void Myinit() {
        textureRegion = TextureHelper.getInstance().getAccelerateButtonlRegion();

    }

    @Override
    public void setInScene(Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {
        super.setInScene(scene, x, y, xScale, yScale, 0, null);
        this.scene = scene;
        button = new Rectangle(-sprite.getWidth() * 0.25f, -sprite.getHeight() * 0.25f, sprite.getWidth() * 1.5f, sprite.getHeight() * 1.5f, engine.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                return setOnTouch(pSceneTouchEvent);
            }
        };
        button.setColor(0, 0, 0, 0);
        sprite.attachChild(button);

        collideRect = new Rectangle(sprite.getWidth() * 0.8f, -sprite.getHeight() * 0.2f,
                sprite.getWidth() * 2f, sprite.getHeight() * 1.4f, engine.getVertexBufferObjectManager());
        collideRect.setColor(0, 0, 0, 0);
        sprite.attachChild(collideRect);
        scene.registerTouchArea(button);
    }

    private boolean setOnTouch(TouchEvent pSceneTouchEvent) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                playSound(mdeviceDownSound);
                sprite.stopAnimation(1);
                break;
            case TouchEvent.ACTION_UP:
                playSound(mdeviceUpSound);
                sprite.stopAnimation(0);
                setTempVisible();
                break;
        }
        return true;
    }

    public void setAngle(float angle) {
        sprite.setRotation(angle);
    }


    public void setCollides(final FlameManager flameManager) {
        collideIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    if (collideRect.collidesWith(flameManager.getFlameSprite())) {
                        float angle = sprite.getRotation();
                        flameManager.addForce(-angle);
                        if (collideRect.getUpdateHandlerCount() != 0) {
                            collideRect.clearUpdateHandlers();
                        }
                    }
                }
            }

            @Override
            public void reset() {

            }
        };
    }

    public void setTempVisible() {
        playSound(mAccelerateSound);
        final AnimatedSprite top = new AnimatedSprite(sprite.getWidth() * 0.8f, -sprite.getHeight() * 0.5f, topTextureRegion, engine.getVertexBufferObjectManager());
        sprite.attachChild(top);
        final AnimatedSprite down = new AnimatedSprite(0, 0, downTextureRegion, engine.getVertexBufferObjectManager());
        top.attachChild(down);
        top.setScaleCenter(0, top.getHeight() / 2);
        top.registerEntityModifier(new ParallelEntityModifier(
                        new ScaleModifier(0.6f, 0, 1f, new IEntityModifier.IEntityModifierListener() {
                            @Override
                            public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {
                                top.animate(30);
                                down.animate(30);
                                if (collideRect.getUpdateHandlerCount() == 0)
                                    collideRect.registerUpdateHandler(collideIU);
                                top.setVisible(true);
                            }

                            @Override
                            public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                                if (collideRect.getUpdateHandlerCount() != 0) {
                                    collideRect.clearUpdateHandlers();
                                }
                                top.setVisible(false);
                                engine.runOnUpdateThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Engine.EngineLock engineLock = engine.getEngineLock();
                                        engineLock.lock();
                                        scene.detachChild(top);
                                        engineLock.unlock();
                                    }
                                });
                            }
                        }),
                        new MoveXModifier(0.6f, collideRect.getX(), collideRect.getX() + collideRect.getWidth() * 0.1f)
                )
        );
    }

    @Override
    protected void init() {
        topTextureRegion = TextureHelper.getInstance().getAccelerateTopRegion();
        downTextureRegion = TextureHelper.getInstance().getAccelerateDowneRegion();
        try {
            mdeviceDownSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/deviceDown.ogg");
            mdeviceUpSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/deviceUp.ogg");
            mAccelerateSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/accelerate.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
