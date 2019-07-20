package cn.vailing.chunqiu.promethues.activity.level.zeus;

import android.os.Bundle;

import cn.vailing.chunqiu.promethues.activity.baseActivity.PromethuesBaseActivity;
import cn.vailing.chunqiu.promethues.manager.AccelerateDeviceManager;
import cn.vailing.chunqiu.promethues.manager.BallManager;
import cn.vailing.chunqiu.promethues.manager.LightingManager;
import cn.vailing.chunqiu.promethues.manager.ObstacleManager;
import cn.vailing.chunqiu.promethues.util.PathContentValue;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/9/25.
 */

public class Six extends PromethuesBaseActivity {
    private ObstacleManager obstacleManager;
    private BallManager ballManager;
    private LightingManager lightingManager;
    private AccelerateDeviceManager accelerateDeviceManager;

    @Override
    protected void resourceInit() {
        TextureHelper.getInstance().initBall();
        TextureHelper.getInstance().initBlink();
        TextureHelper.getInstance().initLighting();
        TextureHelper.getInstance().initAccelerateDevice();
        obstacleManager = new ObstacleManager(mEngine, this, wScale, hScale, null);
        ballManager = new BallManager(mEngine, this, wScale, hScale, null);
        lightingManager = new LightingManager(mEngine, this, wScale, hScale, null);
        accelerateDeviceManager = new AccelerateDeviceManager(mEngine, this, wScale, hScale, null);
    }


    @Override
    public void sceneInit() {
        ballManager.setFlameManager(mFlameManager, this);
        accelerateDeviceManager.setFlameManager(mFlameManager);

        mFlameManager.setBound(-50 * hScale, -50 * wScale, 1100 * hScale, 2000 * wScale);
        mFlameManager.setSpeed(10);
        mFlameManager.setNum(1);
        mPrometheus.setInScene(mScene,
                ScaleHelper.getInstance().getXLocation(100, mPrometheus.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(740, mPrometheus.getRegion().getHeight()), wScale, hScale, 0, null);
        mHuman.setInScene(mScene, ScaleHelper.getInstance().getXLocation(1400, mHuman.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(170, mHuman.getRegion().getHeight()), wScale, hScale, 0, null);
        accelerateDeviceManager.createAccelerateDevice(mScene, 600, 510, -25);

        Bundle bundle = new Bundle();
        bundle.putInt("beginX", 1000);
        bundle.putInt("beginY", 180);
        bundle.putInt("endX", 1230);
        bundle.putInt("endY", 700);
        lightingManager.createLighting(mScene, PathContentValue.LINE, 60, bundle, 2f * hScale);

        Bundle bundle1 = new Bundle();
        bundle1.putInt("beginX", 1230);
        bundle1.putInt("beginY", 700);
        bundle1.putInt("endX", 1000);
        bundle1.putInt("endY", 180);
        lightingManager.createLighting(mScene, PathContentValue.LINE, 60, bundle1, 2f * hScale);
        ballManager.createBall(mScene,462,654);
        ballManager.createBall(mScene,860,500);
        ballManager.createBall(mScene,1250,350);

        obstacleManager.createObstacle(mScene, 1380, 400, 0, 3);
        obstacleManager.createObstacle(mScene, 80, 930, 0, 3);
        obstacleManager.setCollision(mFlameManager);
        lightingManager.setCollision(mFlameManager);
    }
}
