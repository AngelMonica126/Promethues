package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.vailing.chunqiu.promethues.bean.Lighting;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.is.OnGameManger;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ConstantValue;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.SoundHelper;
import cn.vailing.chunqiu.promethues.util.SpUtil;

/**
 * Created by dream on 2017/9/5.
 */

public class LightingManager extends MyBaseManager {
    private List<Rectangle> rectangles;

    public LightingManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
        rectangles = new ArrayList<>();

    }

    public void createLighting(Scene scene, int way, Bundle bundle, float moveSpeed,
                               float rotateCenterX, float rotateCenterY, int rotateSpeed, boolean isPositive) {
        Lighting lighting = new Lighting(engine, context, null, null, null);
        lighting.setScale(wScale, hScale);
        lighting.setPath(scene, way, bundle, moveSpeed);
        lighting.setRotate(rotateCenterX, rotateCenterY, rotateSpeed, isPositive);
        rectangles.add(lighting.getCollideRectangle());
    }

    public void createLighting(Scene scene, float x, float y, float rotateSpeed, boolean isPositive) {
        Lighting lighting = new Lighting(engine, context, null, null, null);
        lighting.setInScene(scene, ScaleHelper.getInstance().getCenterXLoction(x, width),
                ScaleHelper.getInstance().getCenterYLoction(y, height),
                wScale, hScale, 0, null);
        lighting.setRotate(rotateSpeed, isPositive);
        rectangles.add(lighting.getCollideRectangle());
    }

    public void createLighting(Scene scene, float x, float y, int angle) {
        Lighting lighting = new Lighting(engine, context, null, null, null);
        lighting.setInScene(scene, ScaleHelper.getInstance().getCenterXLoction(x, width),
                ScaleHelper.getInstance().getCenterYLoction(y, height),
                wScale, hScale, 0, null);
        lighting.setRotation(angle);
        rectangles.add(lighting.getCollideRectangle());
    }


    public void createLighting(Scene scene, int way, int angle, Bundle bundle, float moveSpeed) {
        Lighting lighting = new Lighting(engine, context, null, null, null);
        lighting.setScale(wScale, hScale);
        lighting.setPath(scene, way, bundle, moveSpeed);
        rectangles.add(lighting.getCollideRectangle());
        lighting.setRotation(angle);
    }

    public void setCollision(final FlameManager flameManager) {
        for (final Rectangle rectangle : rectangles) {
            rectangle.registerUpdateHandler(new IUpdateHandler() {
                @Override
                public void onUpdate(float v) {
                    if (flameManager.isCreate()) {
                        if (rectangle.collidesWith(flameManager.getFlameSprite())) {
                            clear();
                            flameManager.stop();
                            flameManager.boom(new OnFinishListener() {
                                @Override
                                public void finish() {
                                    flameManager.finish();
                                    setCollision(flameManager);
                                }
                            });

//                            }
                        }
                    }
                }

                @Override
                public void reset() {

                }
            });
        }
    }

    private void clear() {
        for (Rectangle rectangle : rectangles) {
            if (rectangle.getUpdateHandlerCount() != 0)
                rectangle.clearUpdateHandlers();
        }
    }

    @Override
    protected void init() {
        width = 300;
        height = 90;
    }
}
