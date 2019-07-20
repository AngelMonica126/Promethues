package cn.vailing.chunqiu.promethues.activity.level.zeus;

import cn.vailing.chunqiu.promethues.activity.baseActivity.PromethuesBaseActivity;
import cn.vailing.chunqiu.promethues.manager.BallManager;
import cn.vailing.chunqiu.promethues.manager.ObstacleManager;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/9/28.
 */

public class DeveiceTest extends PromethuesBaseActivity {
    private ObstacleManager obstacleManager;
    private BallManager ballManager;

    @Override
    protected void resourceInit() {
        TextureHelper.getInstance().initBall();
        TextureHelper.getInstance().initBlink();
        obstacleManager = new ObstacleManager(mEngine, this, wScale, hScale, null);
        ballManager = new BallManager(mEngine, this, wScale, hScale, null);
    }


    @Override
    public void sceneInit() {
        ballManager.setFlameManager(mFlameManager, this);
        mFlameManager.setBound(-50 * hScale, -50 * wScale, 1100 * hScale, 2000 * wScale);
        mFlameManager.setNum(1);
        mPrometheus.setInScene(mScene,
                ScaleHelper.getInstance().getXLocation(100, mPrometheus.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(700, mPrometheus.getRegion().getHeight()), wScale, hScale, 0, null);
        mHuman.setInScene(mScene, ScaleHelper.getInstance().getXLocation(1400, mHuman.getRegion().getWidth()),
                ScaleHelper.getInstance().getYLocation(660, mHuman.getRegion().getHeight()), wScale, hScale, 0, null);
        ballManager.createBall(mScene, 500, 700);
        ballManager.createBall(mScene, 800, 700);
        ballManager.createBall(mScene, 1100, 700);
        obstacleManager.createObstacle(mScene, 80, 890, 0, 3);
        obstacleManager.createObstacle(mScene, 1400, 890, 0, 3);
        obstacleManager.setCollision(mFlameManager);
    }
}
