package cn.vailing.chunqiu.promethues.activity.level.zeus;

import cn.vailing.chunqiu.promethues.activity.baseActivity.PromethuesBaseActivity;
import cn.vailing.chunqiu.promethues.manager.BallManager;
import cn.vailing.chunqiu.promethues.manager.MirrorManager;
import cn.vailing.chunqiu.promethues.manager.ObstacleManager;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/9/23.
 */

public class Two extends PromethuesBaseActivity {
    private ObstacleManager obstacleManager;
    private BallManager ballManager;
    private MirrorManager mirrorManager;

    @Override
    protected void resourceInit() {
        TextureHelper.getInstance().initBall();
        TextureHelper.getInstance().initBlink();
        TextureHelper.getInstance().initMirror();
        obstacleManager = new ObstacleManager(mEngine, this, wScale, hScale, null);
        ballManager = new BallManager(mEngine, this, wScale, hScale, null);
        mirrorManager = new MirrorManager(mEngine, this, wScale, hScale, null);

    }


    @Override
    public void sceneInit() {
        ballManager.setFlameManager(mFlameManager,this);
        mirrorManager.setFlameManager(mFlameManager);
        mFlameManager.setBound(-50*hScale, -50*wScale, 1100*hScale, 2000*wScale);
        mFlameManager.setNum(1);
        mPrometheus.setInScene(mScene,
                ScaleHelper.getInstance().getXLocation(100,mPrometheus.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(700,mPrometheus.getRegion().getHeight()),wScale,hScale,0, null);
        mHuman.setInScene(mScene, ScaleHelper.getInstance().getXLocation(1400,mHuman.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(660,mHuman.getRegion().getHeight()),wScale,hScale,0, null);

        mirrorManager.createMirror(mScene,730,250,0);


        ballManager.createBall(mScene,502,600);
        ballManager.createBall(mScene,968,484);
        ballManager.createBall(mScene,1320,670);

        obstacleManager.createObstacle(mScene,820,550,90,6);
        obstacleManager.createObstacle(mScene,80,890,0,3);
        obstacleManager.createObstacle(mScene,1400,890,0,3);
        obstacleManager.setCollision(mFlameManager);
    }
}
