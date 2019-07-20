package cn.vailing.chunqiu.promethues.activity.level.zeus;

import android.os.Bundle;

import cn.vailing.chunqiu.promethues.activity.baseActivity.PromethuesBaseActivity;
import cn.vailing.chunqiu.promethues.manager.BallManager;
import cn.vailing.chunqiu.promethues.manager.LightingManager;
import cn.vailing.chunqiu.promethues.manager.MedusaManager;
import cn.vailing.chunqiu.promethues.manager.ObstacleManager;
import cn.vailing.chunqiu.promethues.util.PathContentValue;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/9/26.
 */

public class Nine extends PromethuesBaseActivity {
    private ObstacleManager obstacleManager;
    private BallManager ballManager;
    private MedusaManager medusaManager;
    private LightingManager lightingManager;

    @Override
    protected void resourceInit() {
        TextureHelper.getInstance().initBall();
        TextureHelper.getInstance().initBlink();
        TextureHelper.getInstance().initMedusa();
        TextureHelper.getInstance().initLighting();
        obstacleManager = new ObstacleManager(mEngine, this, wScale, hScale, null);
        ballManager = new BallManager(mEngine, this, wScale, hScale, null);
        medusaManager = new MedusaManager(mEngine, this, wScale, hScale, null);
        lightingManager = new LightingManager(mEngine, this, wScale, hScale, null);

    }

    @Override
    public void sceneInit() {
        ballManager.setFlameManager(mFlameManager, this);
        medusaManager.setFlameManager(mFlameManager);
        camera.setBounds(0, -1300 * hScale, 2000 * wScale, 1300 * hScale);
        camera.setBoundsEnabled(true);
        mFlameManager.setBound(-1500 * hScale, -50 * wScale, 1100 * hScale, 2000 * wScale);
        mFlameManager.setNum(1);
        mPrometheus.setInScene(mScene,
                ScaleHelper.getInstance().getXLocation(100, mPrometheus.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(750, mPrometheus.getRegion().getHeight()), wScale, hScale, 0, null);
        mHuman.setInScene(mScene, ScaleHelper.getInstance().getXLocation(1400, mHuman.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(-1000, mHuman.getRegion().getHeight()), wScale, hScale, 0, null);
        medusaManager.createMedusa(mScene, 1300, 600, 300);
        medusaManager.createMedusa(mScene, 400, 100, 300);
        medusaManager.createMedusa(mScene, 1300, -400, 300);
        medusaManager.createMedusa(mScene, 400, -900, 300);
        lightingManager.createLighting(mScene, 600, 700, 1, false);
        obstacleManager.createObstacle(mScene, 0, 550, 0, 10);
//        camera.setCenter(camera.getCenterX(), -100);
        Bundle bundle = new Bundle();
        bundle.putInt("beginX", 800);
        bundle.putInt("beginY", 650);
        bundle.putInt("endX", 1200);
        bundle.putInt("endY", 650);
        lightingManager.createLighting(mScene, PathContentValue.LINE, 0, bundle, 2f);
        lightingManager.createLighting(mScene, 130, -300, 0);
        lightingManager.createLighting(mScene, 330, -300, 0);
        lightingManager.createLighting(mScene, 530, -300, 0);
        lightingManager.createLighting(mScene, 730, -300, 0);
        lightingManager.createLighting(mScene, 930, -300, 0);
        lightingManager.createLighting(mScene, 1130, -300, 0);

        setFollowFlame(1,0,  1300*hScale, 2000 * wScale,  -1300*hScale);
        lightingManager.createLighting(mScene, 800, 200, 0);
        lightingManager.createLighting(mScene, 1000, 200, 0);
        lightingManager.createLighting(mScene, 1200, 200, 0);
        lightingManager.createLighting(mScene, 1400, 200, 0);
        lightingManager.createLighting(mScene, 1600, 200, 0);
        lightingManager.createLighting(mScene, 1800, 200, 0);

        ballManager.createBall(mScene, 1390, 830);
        ballManager.createBall(mScene, 490, 330);
        ballManager.createBall(mScene,1390,-170);
        obstacleManager.createObstacle(mScene, 80, 940, 0, 3);
        obstacleManager.createObstacle(mScene, 900, -770, 0, 15);
        obstacleManager.setCollision(mFlameManager);
        lightingManager.setCollision(mFlameManager);
    }
}
