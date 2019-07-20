package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;


import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;

import cn.vailing.chunqiu.promethues.bean.Calabash;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/8/17.
 */

public class CalabashManager extends MyBaseManager {
    private FlameManager flameManager;

    public CalabashManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
    }

    public void setFlameManager(FlameManager flameManager) {
        this.flameManager = flameManager;
    }

    public void createCalabash(Scene scene, float x, float y, float speed, float angle) {
        Calabash calabash = new Calabash(engine, context, null, null, null);
        calabash.setInScene(scene,
                ScaleHelper.getInstance().getXLocation(x, width),
                ScaleHelper.getInstance().getYLocation(y, height),
                wScale, hScale, 150, null);
        if (speed != -1) {
            calabash.setRotate((int) speed);
        } else {
            calabash.setRotation(angle);
        }
        calabash.setCollides(flameManager);
    }

    public void createCalabash(Scene scene, float x, float y, boolean isCanBeRotate) {
        Calabash calabash = new Calabash(engine, context, null, null, null);
        calabash.setInScene(scene,
                ScaleHelper.getInstance().getXLocation(x, width),
                ScaleHelper.getInstance().getYLocation(y, height),
                wScale, hScale, 150, null);
//        calabash.setRotation(angle);
        if (isCanBeRotate)
            calabash.setCanBeRotated(scene);
        calabash.setCollides(flameManager);
    }

    @Override
    protected void init() {
        width = 241;
        height = 125;
    }
}
