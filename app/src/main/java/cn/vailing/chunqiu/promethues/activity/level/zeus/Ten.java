package cn.vailing.chunqiu.promethues.activity.level.zeus;

import android.os.Bundle;

import cn.vailing.chunqiu.promethues.activity.baseActivity.PromethuesBaseActivity;
import cn.vailing.chunqiu.promethues.bean.AccelerateDevice;
import cn.vailing.chunqiu.promethues.manager.AccelerateDeviceManager;
import cn.vailing.chunqiu.promethues.manager.BallManager;
import cn.vailing.chunqiu.promethues.manager.CalabashManager;
import cn.vailing.chunqiu.promethues.manager.LightingManager;
import cn.vailing.chunqiu.promethues.manager.MedusaManager;
import cn.vailing.chunqiu.promethues.manager.MirrorManager;
import cn.vailing.chunqiu.promethues.manager.ObstacleManager;
import cn.vailing.chunqiu.promethues.manager.TransferManager;
import cn.vailing.chunqiu.promethues.util.PathContentValue;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/9/28.
 */

public class Ten extends PromethuesBaseActivity {
    private ObstacleManager obstacleManager;
    private BallManager ballManager;
    private MirrorManager mirrorManager;
    private MedusaManager medusaManager;
    private TransferManager transferManager;
    private CalabashManager calabashManager;
    private AccelerateDeviceManager accelerateDeviceManager;
    private LightingManager lightingManager;
    @Override
    protected void resourceInit() {
        TextureHelper.getInstance().initBall();
        TextureHelper.getInstance().initMirror();
        TextureHelper.getInstance().initBlink();
        TextureHelper.getInstance().initMedusa();
        TextureHelper.getInstance().initTransfer();
        TextureHelper.getInstance().initCalabash();
        TextureHelper.getInstance().initAccelerateDevice();
        TextureHelper.getInstance().initLighting();
        obstacleManager = new ObstacleManager(mEngine, this, wScale, hScale, null);
        ballManager = new BallManager(mEngine, this, wScale, hScale, null);
        mirrorManager = new MirrorManager(mEngine, this, wScale, hScale, null);
        medusaManager = new MedusaManager(mEngine, this, wScale, hScale, null);
        calabashManager = new CalabashManager(mEngine, this, wScale, hScale, null);
        transferManager = new TransferManager(mEngine, this, wScale, hScale, null);
        lightingManager = new LightingManager(mEngine, this, wScale, hScale, null);
        accelerateDeviceManager = new AccelerateDeviceManager(mEngine, this, wScale, hScale, null);
    }


    @Override
    public void sceneInit() {
        camera.setBounds(0, 0, 2600 * wScale, 1100 * hScale);
        camera.setBoundsEnabled(true);

        ballManager.setFlameManager(mFlameManager, this);
        mirrorManager.setFlameManager(mFlameManager);
        medusaManager.setFlameManager(mFlameManager);
        calabashManager.setFlameManager(mFlameManager);
        transferManager.setFlameManger(mFlameManager);
        accelerateDeviceManager.setFlameManager(mFlameManager);
        mFlameManager.setBound(-50 * hScale, -50 * wScale, 1100 * hScale, 3000 * wScale);
        mFlameManager.setNum(1);
        mPrometheus.setInScene(mScene,
                ScaleHelper.getInstance().getXLocation(100, mPrometheus.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(800, mPrometheus.getRegion().getHeight()), wScale, hScale, 0, null);
//        camera.setCenter(960, 540 / 2);
        mHuman.setInScene(mScene, ScaleHelper.getInstance().getXLocation(100, mHuman.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(200, mHuman.getRegion().getHeight()), wScale, hScale, 0, null);

        medusaManager.createMedusa(mScene, 1200, 800, 300);
        transferManager.createTransfer(mScene, 1920, 700, 0, 1920, 220, 0, false);
        calabashManager.createCalabash(mScene, 1500, 230, true);
        mirrorManager.createMirror(mScene, 1000, -80, 0);
        accelerateDeviceManager.createAccelerateDevice(mScene, 850, 250, -180);
        lightingManager.createLighting(mScene,700,660,2f*hScale,true);
//        lightingManager.createLighting(mScene,1600,650,90);
        lightingManager.createLighting(mScene,1620,950,90);
        lightingManager.createLighting(mScene,500,950,0);
        lightingManager.createLighting(mScene,700,950,0);
        lightingManager.createLighting(mScene,900,950,0);
        lightingManager.createLighting(mScene,1100,950,0);
        lightingManager.createLighting(mScene,1300,950,0);
        lightingManager.createLighting(mScene,1500,950,0);

        lightingManager.createLighting(mScene,1150,300,3f*hScale,true);

        Bundle bundle = new Bundle();
        bundle.putInt("x", 700);
        bundle.putInt("y", 660);
        bundle.putInt("beginAngle", -90);
        bundle.putInt("radius", 150);
        ballManager.createBall(mScene, PathContentValue.CIRCULAR,bundle,1f*hScale);
        ballManager.createBall(mScene,1800,350);
        ballManager.createBall(mScene,1700,750);
        obstacleManager.createObstacle(mScene, 80, 990, 0, 3);
        obstacleManager.createObstacle(mScene, 1650, 910, 0, 20);
        obstacleManager.createObstacle(mScene, 0, 430, 0, 40);
        obstacleManager.setCollision(mFlameManager);
        lightingManager.setCollision(mFlameManager);
        setFollowFlame(4, 0, 0, 2600 * wScale, 1100 * hScale);
    }
}
