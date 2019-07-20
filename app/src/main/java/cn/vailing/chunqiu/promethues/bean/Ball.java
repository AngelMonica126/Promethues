package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;
import android.os.Bundle;

import com.badlogic.gdx.math.Vector2;


import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import java.io.IOException;

import cn.vailing.chunqiu.promethues.is.MIAnimationListener;
import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.is.OnGameManger;
import cn.vailing.chunqiu.promethues.manager.FlameManager;
import cn.vailing.chunqiu.promethues.util.PathContentValue;
import cn.vailing.chunqiu.promethues.util.PathHelper;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/9/18.
 */

public class Ball extends MyBaseSprite {
    private Scene scene;
    private IUpdateHandler locationIU;

    protected BitmapTextureAtlas fireTexture;
    protected TiledTextureRegion fireTextureRegion;
    protected AnimatedSprite fire;
    private Sound mBlinkSound;

    public Ball(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/Ball.png", new Vector2(150, 150), new Vector2(1, 1));
    }

    @Override
    public void Myinit() {
        textureRegion = TextureHelper.getInstance().getBallRegion();
    }

    @Override
    public void setInScene( Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {
        super.setInScene(scene, x, y, xScale, yScale, 0, null);
        this.scene = scene;
    }

    public void setPath(Scene scene, int way, Bundle bundle, float speed) {
        this.scene = scene;
        switch (way) {
            case PathContentValue.LINE:
                setLine(bundle, speed);
                break;
            case PathContentValue.CIRCULAR:
                setCircular(bundle, speed);
                break;
        }
    }

    private void setCircular(Bundle bundle, float speed) {
        float x = bundle.getInt("x");
        float y = bundle.getInt("y");
        float beginAngle = bundle.getInt("beginAngle");
        float radius = bundle.getInt("radius");
        float beginX = (float) (x + Math.cos(beginAngle) * radius);
        float beginY = (float) (y + Math.sinh(beginAngle) * radius);
        setInScene(scene,
                ScaleHelper.getInstance().getCenterXLoction(beginX, textureRegion.getWidth()),
                ScaleHelper.getInstance().getCenterYLoction(beginY, textureRegion.getHeight()),
                wScale, hScale, 60, null);

        final PathHelper pathHelper = new PathHelper(x, y, radius, 0, speed, true);
        locationIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                setLocation(ScaleHelper.getInstance().getCenterXLoction(pathHelper.circular().x, textureRegion.getWidth()),
                        ScaleHelper.getInstance().getCenterYLoction(pathHelper.circular().y, textureRegion.getHeight()));
            }

            @Override
            public void reset() {

            }
        };
        sprite.registerUpdateHandler(locationIU);
    }


    private void setLine(Bundle bundle, float speed) {
        int beginX = bundle.getInt("beginX");
        int beginY = bundle.getInt("beginY");
        int endX = bundle.getInt("endX");
        int endY = bundle.getInt("endY");
        setInScene(scene,
                ScaleHelper.getInstance().getCenterXLoction(beginX, textureRegion.getWidth()),
                ScaleHelper.getInstance().getCenterYLoction(beginY, textureRegion.getHeight()),
                wScale, hScale, 60, null);
        final PathHelper pathHelper = new PathHelper(beginX, beginY, endX, endY, speed);
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
        sprite.registerUpdateHandler(locationIU);
    }

    private void setLocation(float x, float y) {
        sprite.setPosition(x, y);
    }

    @Override
    protected void init() {
        fireTextureRegion = TextureHelper.getInstance().getBlinkRegion();
        try {
            mBlinkSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/blink.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCollide(final FlameManager flameManager, final OnGameManger onRecordListener) {
        sprite.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if(flameManager.isCreate()){
                    if(sprite.collidesWith(flameManager.getFlameSprite())){
                        playSound(mBlinkSound);
                        if(onRecordListener!=null){
                            onRecordListener.addStart();
                        }
                        float x = sprite.getX() + sprite.getWidth()/2;
                        float y = sprite.getY() + sprite.getHeight()/2;
                        fire = new AnimatedSprite(ScaleHelper.getInstance().getRealCenterX(x, fireTextureRegion.getWidth()),
                                ScaleHelper.getInstance().getRealCenterY(y, fireTextureRegion.getHeight()),
                                fireTextureRegion, engine.getVertexBufferObjectManager());
                        fire.setScale(wScale*1.5f, hScale *1.5f);
                        scene.attachChild(fire);
                        sprite.setVisible(false);
                        fire.animate(40, false, new MIAnimationListener() {
                            @Override
                            public void onAnimationFinished(AnimatedSprite animatedSprite) {
                                fire.setVisible(false);
                                engine.runOnUpdateThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Engine.EngineLock engineLock = engine.getEngineLock();
                                        engineLock.lock();
                                        scene.detachChild(fire);
                                        scene.detachChild(sprite);
                                        engineLock.unlock();
                                    }
                                });
                            }
                        });
                        sprite.unregisterUpdateHandler(this);
                    }
                }
            }

            @Override
            public void reset() {

            }
        });
    }
}
