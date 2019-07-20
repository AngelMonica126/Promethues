package cn.vailing.chunqiu.promethues.activity.level.zeus;

import cn.vailing.chunqiu.promethues.activity.baseActivity.PromethuesBaseActivity;
import cn.vailing.chunqiu.promethues.manager.BallManager;
import cn.vailing.chunqiu.promethues.manager.CalabashManager;
import cn.vailing.chunqiu.promethues.manager.MirrorManager;
import cn.vailing.chunqiu.promethues.manager.ObstacleManager;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/9/24.
 */

public class Five extends PromethuesBaseActivity {
    private ObstacleManager obstacleManager;
    private BallManager ballManager;
    private MirrorManager mirrorManager;
    private CalabashManager calabashManager;
    @Override
    protected void resourceInit() {
        TextureHelper.getInstance().initBall();
        TextureHelper.getInstance().initBlink();
        TextureHelper.getInstance().initCalabash();
        TextureHelper.getInstance().initMirror();
        obstacleManager = new ObstacleManager(mEngine, this, wScale, hScale, null);
        ballManager = new BallManager(mEngine, this, wScale, hScale, null);
        mirrorManager = new MirrorManager(mEngine, this, wScale, hScale, null);
        calabashManager = new CalabashManager(mEngine, this, wScale, hScale, null);

    }


    @Override
    public void sceneInit() {
        ballManager.setFlameManager(mFlameManager,this);
        mirrorManager.setFlameManager(mFlameManager);
        calabashManager.setFlameManager(mFlameManager);

        mFlameManager.setBound(-50*hScale, -50*wScale, 1100*hScale, 2000*wScale);
        mFlameManager.setNum(1);
        mPrometheus.setInScene(mScene,
                ScaleHelper.getInstance().getXLocation(100,mPrometheus.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(700,mPrometheus.getRegion().getHeight()),wScale,hScale,0, null);
        mHuman.setInScene(mScene, ScaleHelper.getInstance().getXLocation(1300,mHuman.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(70,mHuman.getRegion().getHeight()),wScale,hScale,0, null);
        calabashManager.createCalabash(mScene,1400,700,60f*hScale,0);
        mirrorManager.createMirror(mScene,500,300,0,true,0,0,-120,120);
        ballManager.createBall(mScene,600,780);
        ballManager.createBall(mScene,1100,780);
        ballManager.createBall(mScene,980,590);


        obstacleManager.createObstacle(mScene,0,600,0,12);

        obstacleManager.createObstacle(mScene,80,890,0,3);

        obstacleManager.createObstacle(mScene,1300,300,0,3);
        obstacleManager.setCollision(mFlameManager);
    }
}
