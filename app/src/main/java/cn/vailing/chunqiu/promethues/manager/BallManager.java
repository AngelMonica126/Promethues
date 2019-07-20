package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;
import android.os.Bundle;


import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;

import cn.vailing.chunqiu.promethues.activity.level.zeus.One;
import cn.vailing.chunqiu.promethues.bean.Ball;
import cn.vailing.chunqiu.promethues.is.OnGameManger;
import cn.vailing.chunqiu.promethues.is.OnRecordListener;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/9/18.
 */

public class BallManager extends MyBaseManager {
    private FlameManager flameManager;
    private OnGameManger onRecordListener;

    public BallManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
    }

    public void setFlameManager(final FlameManager flameManager, OnGameManger onRecordListener){
        this.flameManager = flameManager;
        this.onRecordListener = onRecordListener;
    }
    public void createBall(Scene scene, int way, Bundle bundle, float moveSpeed) {
        Ball ball = new Ball(engine, context, null, null, null);
        ball.setScale(wScale, hScale);
        ball.setPath(scene, way, bundle, moveSpeed);
        ball.setCollide(flameManager,onRecordListener);
    }
    public void createBall(Scene scene, float x,float y) {
        Ball ball = new Ball(engine, context, null, null, null);
    ball.setInScene(scene, ScaleHelper.getInstance().getCenterXLoction(x,ball.getRegion().getWidth()),
            ScaleHelper.getInstance().getCenterYLoction(y,ball.getRegion().getHeight()),wScale,hScale,0,null);
    ball.setCollide(flameManager,onRecordListener);
}
    @Override
    protected void init() {

    }
}
