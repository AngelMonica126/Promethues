package cn.vailing.chunqiu.promethues.activity.level.zeus;

import android.os.Bundle;

import cn.vailing.chunqiu.promethues.activity.baseActivity.PromethuesBaseActivity;
import cn.vailing.chunqiu.promethues.bean.Mirror;
import cn.vailing.chunqiu.promethues.manager.BallManager;
import cn.vailing.chunqiu.promethues.manager.MirrorManager;
import cn.vailing.chunqiu.promethues.manager.ObstacleManager;
import cn.vailing.chunqiu.promethues.manager.TransferManager;
import cn.vailing.chunqiu.promethues.util.PathContentValue;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/9/23.
 */

public class Four extends PromethuesBaseActivity {
    private ObstacleManager obstacleManager;
    private BallManager ballManager;
    private TransferManager transferManager;
    private MirrorManager mirrorManager;

    @Override
    protected void resourceInit() {
        TextureHelper.getInstance().initBall();
        TextureHelper.getInstance().initBlink();
        TextureHelper.getInstance().initTransfer();
        TextureHelper.getInstance().initMirror();
        obstacleManager = new ObstacleManager(mEngine, this, wScale, hScale, null);
        ballManager = new BallManager(mEngine, this, wScale, hScale, null);
        transferManager = new TransferManager(mEngine, this, wScale, hScale, null);
        mirrorManager = new MirrorManager(mEngine, this, wScale, hScale, null);
    }


    @Override
    public void sceneInit() {

        ballManager.setFlameManager(mFlameManager,this);
        mirrorManager.setFlameManager(mFlameManager);
        transferManager.setFlameManger(mFlameManager);
        mFlameManager.setBound(-50*hScale, -50*wScale, 1100*hScale, 2000*wScale);
        mFlameManager.setNum(1);
        mPrometheus.setInScene(mScene,
                ScaleHelper.getInstance().getXLocation(100,mPrometheus.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(700,mPrometheus.getRegion().getHeight()),wScale,hScale,0, null);
        mHuman.setInScene(mScene, ScaleHelper.getInstance().getXLocation(1600,mHuman.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(660,mHuman.getRegion().getHeight()),wScale,hScale,0, null);
        mirrorManager.createMirror(mScene,795,410,90);
        mirrorManager.createMirror(mScene,1600,300,0,true,0,0,-90,90);
//        mirrorManager.createMirror(mScene,1000,700,-180,true,0,300,-270,-90);
        transferManager.createTransfer(mScene,359,290,0,1000,290,0,true);
        Bundle bundle = new Bundle();
        bundle.putInt("beginX", 500);
        bundle.putInt("beginY", 600);
        bundle.putInt("endX", 500);
        bundle.putInt("endY", 900);

        ballManager.createBall(mScene, PathContentValue.LINE,bundle,1.6f*hScale);
        ballManager.createBall(mScene,1440,416);
        ballManager.createBall(mScene,1650,590);

        obstacleManager.createObstacle(mScene, 940,0,90,20);
        obstacleManager.createObstacle(mScene, 0,500,0,9);
        obstacleManager.createObstacle(mScene, 990,500,0,5);
        obstacleManager.createObstacle(mScene,80,890,0,3);
        obstacleManager.createObstacle(mScene,1600,890,0,3);
        obstacleManager.setCollision(mFlameManager);
    }
}
