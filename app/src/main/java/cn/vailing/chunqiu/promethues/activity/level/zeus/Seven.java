package cn.vailing.chunqiu.promethues.activity.level.zeus;

import android.os.Bundle;
import android.util.Log;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.primitive.Rectangle;

import cn.vailing.chunqiu.promethues.activity.baseActivity.PromethuesBaseActivity;
import cn.vailing.chunqiu.promethues.bean.Calabash;
import cn.vailing.chunqiu.promethues.manager.AccelerateDeviceManager;
import cn.vailing.chunqiu.promethues.manager.BallManager;
import cn.vailing.chunqiu.promethues.manager.CalabashManager;
import cn.vailing.chunqiu.promethues.manager.CameraManager;
import cn.vailing.chunqiu.promethues.manager.LightingManager;
import cn.vailing.chunqiu.promethues.manager.MirrorManager;
import cn.vailing.chunqiu.promethues.manager.ObstacleManager;
import cn.vailing.chunqiu.promethues.manager.TransferManager;
import cn.vailing.chunqiu.promethues.util.PathContentValue;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/9/25.
 */

public class Seven extends PromethuesBaseActivity {
    private ObstacleManager obstacleManager;
    private BallManager ballManager;
    private CalabashManager calabashManager;
    private MirrorManager mirrorManager;
    private TransferManager transferManager;
    private AccelerateDeviceManager accelerateDeviceManager;
    private LightingManager lightingManager;


    @Override
    protected void resourceInit() {
        TextureHelper.getInstance().initBall();
        TextureHelper.getInstance().initBlink();
        TextureHelper.getInstance().initCalabash();
        TextureHelper.getInstance().initMirror();
        TextureHelper.getInstance().initTransfer();
        TextureHelper.getInstance().initAccelerateDevice();
        TextureHelper.getInstance().initLighting();
        obstacleManager = new ObstacleManager(mEngine, this, wScale, hScale, null);
        ballManager = new BallManager(mEngine, this, wScale, hScale, null);
        calabashManager = new CalabashManager(mEngine, this, wScale, hScale, null);
        mirrorManager = new MirrorManager(mEngine, this, wScale, hScale, null);

        transferManager = new TransferManager(mEngine, this, wScale, hScale, null);
        accelerateDeviceManager = new AccelerateDeviceManager(mEngine, this, wScale, hScale, null);
        lightingManager = new LightingManager(mEngine, this, wScale, hScale, null);

    }


    @Override
    public void sceneInit() {
        ballManager.setFlameManager(mFlameManager, this);
        calabashManager.setFlameManager(mFlameManager);
        mirrorManager.setFlameManager(mFlameManager);
        transferManager.setFlameManger(mFlameManager);
        accelerateDeviceManager.setFlameManager(mFlameManager);
        camera.setBounds(0, 0, 3500 * wScale, 1080);
        camera.setBoundsEnabled(true);
        transferManager.setFollow(camera, true);
        mFlameManager.setBound(-50 * hScale, -50 * wScale, 1100 * hScale, 4000 * wScale);
        mFlameManager.setNum(1);
        mPrometheus.setInScene(mScene,
                ScaleHelper.getInstance().getXLocation(100, mPrometheus.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(800, mPrometheus.getRegion().getHeight()), wScale, hScale, 0, null);

        calabashManager.createCalabash(mScene, 1500, 780, true);
        calabashManager.createCalabash(mScene, 1500, 100, 60f*hScale,0);
        calabashManager.createCalabash(mScene, 600, 100, true);
        mirrorManager.createMirror(mScene, 400, 470, -125);

        transferManager.createTransfer(mScene, 900, 500, 0, 1920, 530, 0, true);

        accelerateDeviceManager.createAccelerateDevice(mScene, 2500, 700, -90);
        mirrorManager.createMirror(mScene, 2750, 200, -340);
        mHuman.setInScene(mScene, ScaleHelper.getInstance().getXLocation(2850, mHuman.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(800, mHuman.getRegion().getHeight()), wScale, hScale, 0, null);

        lightingManager.createLighting(mScene, 1300, 550, 2*hScale, false);
        lightingManager.createLighting(mScene, 800, 300, 0);
        lightingManager.createLighting(mScene, 1050, 300, 0);
        lightingManager.createLighting(mScene, 1300, 300, 0);

        Bundle bundle = new Bundle();
        bundle.putInt("beginX", 3200);
        bundle.putInt("beginY", 200);
        bundle.putInt("endX", 3200);
        bundle.putInt("endY", 800);
        lightingManager.createLighting(mScene, PathContentValue.LINE, 90, bundle, 4*wScale);
        lightingManager.createLighting(mScene, 2300, 300, 4*wScale, false);

        ballManager.createBall(mScene, 1020, 180);
        ballManager.createBall(mScene, 780, 860);
        ballManager.createBall(mScene, 2900, 600);
        obstacleManager.createObstacle(mScene, 0, 710, 0, 20);
        obstacleManager.createObstacle(mScene, 1920, 0, 90, 20);
        obstacleManager.createObstacle(mScene, 2470, 813, 0, 3);
        obstacleManager.createObstacle(mScene, 1990, 739, 0, 3);

        obstacleManager.createObstacle(mScene, 80, 990, 0, 3);
        obstacleManager.createObstacle(mScene, 2850, 1030, 0, 3);
        obstacleManager.setCollision(mFlameManager);
        lightingManager.setCollision(mFlameManager);
        setFollowFlame(4,0, 0, 3500 * wScale, 1080);
    }
}
