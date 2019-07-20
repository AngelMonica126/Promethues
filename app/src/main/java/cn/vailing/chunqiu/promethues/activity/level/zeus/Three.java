package cn.vailing.chunqiu.promethues.activity.level.zeus;

import cn.vailing.chunqiu.promethues.activity.baseActivity.PromethuesBaseActivity;
import cn.vailing.chunqiu.promethues.bean.Transfer;
import cn.vailing.chunqiu.promethues.manager.BallManager;
import cn.vailing.chunqiu.promethues.manager.MirrorManager;
import cn.vailing.chunqiu.promethues.manager.ObstacleManager;
import cn.vailing.chunqiu.promethues.manager.TransferManager;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/9/23.
 */

public class Three extends PromethuesBaseActivity {
    private ObstacleManager obstacleManager;
    private BallManager ballManager;
    private TransferManager transferManager;

    @Override
    protected void resourceInit() {
        TextureHelper.getInstance().initBall();
        TextureHelper.getInstance().initBlink();
        TextureHelper.getInstance().initMirror();
        TextureHelper.getInstance().initTransfer();
        obstacleManager = new ObstacleManager(mEngine, this, wScale, hScale, null);
        ballManager = new BallManager(mEngine, this, wScale, hScale, null);
        transferManager = new TransferManager(mEngine, this, wScale, hScale, null);

    }


    @Override
    public void sceneInit() {
        ballManager.setFlameManager(mFlameManager,this);
        transferManager.setFlameManger(mFlameManager);
        mFlameManager.setBound(-50*hScale, -50*wScale, 1100*hScale, 2000*wScale);
        mFlameManager.setNum(1);
        mPrometheus.setInScene(mScene,
                ScaleHelper.getInstance().getXLocation(100,mPrometheus.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(700,mPrometheus.getRegion().getHeight()),wScale,hScale,0, null);
        mHuman.setInScene(mScene, ScaleHelper.getInstance().getXLocation(1400,mHuman.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(170,mHuman.getRegion().getHeight()),wScale,hScale,0, null);
        transferManager.createTransfer(mScene,200,190,0,1300,690,0,true);

        ballManager.createBall(mScene,534,756);
        ballManager.createBall(mScene,1028,756);
        ballManager.createBall(mScene,872,304);

        obstacleManager.createObstacle(mScene,0,400,0,30);
        obstacleManager.createObstacle(mScene,80,890,0,3);
        obstacleManager.createObstacle(mScene,1380,900,0,3);
        obstacleManager.setCollision(mFlameManager);
    }
}
