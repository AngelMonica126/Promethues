package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;


import org.andengine.audio.sound.Sound;
import org.andengine.engine.Engine;

import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ConstantValue;
import cn.vailing.chunqiu.promethues.util.SpUtil;

/**
 * Created by dream on 2017/8/9.
 */

public abstract class MyBaseManager {
    protected float hScale;
    protected float wScale;
    protected MyPhysicsWorld physicsWorld;
    protected Engine engine;
    protected Context context;
    protected float width;
    protected float height;

    public MyBaseManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        this.hScale = hScale;
        this.context = context;
        this.engine = engine;
        this.physicsWorld = physicsWorld;
        this.wScale = wScale;
        init();
    }

    protected abstract void init();

}
