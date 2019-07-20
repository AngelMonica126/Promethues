package cn.vailing.chunqiu.promethues.activity.level.zeus;

import android.os.Bundle;

import cn.vailing.chunqiu.promethues.activity.baseActivity.PromethuesBaseActivity;
import cn.vailing.chunqiu.promethues.manager.BallManager;
import cn.vailing.chunqiu.promethues.manager.MedusaManager;
import cn.vailing.chunqiu.promethues.manager.ObstacleManager;
import cn.vailing.chunqiu.promethues.util.PathContentValue;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/9/26.
 */

public class Eight extends PromethuesBaseActivity{
    private ObstacleManager obstacleManager;
    private BallManager ballManager;
    private MedusaManager medusaManager;
    @Override
    protected void resourceInit() {
        TextureHelper.getInstance().initBall();
        TextureHelper.getInstance().initBlink();
        TextureHelper.getInstance().initMedusa();
        obstacleManager = new ObstacleManager(mEngine, this, wScale, hScale, null);
        ballManager = new BallManager(mEngine, this, wScale, hScale, null);
        medusaManager = new MedusaManager(mEngine, this, wScale, hScale, null);
    }


    @Override
    public void sceneInit() {
        ballManager.setFlameManager(mFlameManager,this);
        medusaManager.setFlameManager(mFlameManager);
        mFlameManager.setBound(-50*hScale, -50*wScale, 1100*hScale, 2000*wScale);
        mFlameManager.setNum(1);
        mPrometheus.setInScene(mScene,
                ScaleHelper.getInstance().getXLocation(100,mPrometheus.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(700,mPrometheus.getRegion().getHeight()),wScale,hScale,0, null);
        mHuman.setInScene(mScene, ScaleHelper.getInstance().getXLocation(100,mHuman.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(200,mHuman.getRegion().getHeight()),wScale,hScale,0, null);
        medusaManager.createMedusa(mScene,1200,500,400);

        Bundle bundle = new Bundle();
        bundle.putInt("beginX", 540);
        bundle.putInt("beginY", 400);
        bundle.putInt("endX", 540);
        bundle.putInt("endY", 900);

        ballManager.createBall(mScene, PathContentValue.LINE,bundle,3f*hScale);
        ballManager.createBall(mScene,920,600);
        ballManager.createBall(mScene,1600,600);
        obstacleManager.createObstacle(mScene,80,890,0,3);
        obstacleManager.createObstacle(mScene,100,430,0,3);
        obstacleManager.setCollision(mFlameManager);
    }
}
