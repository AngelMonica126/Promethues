package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;

import cn.vailing.chunqiu.promethues.bean.Balloon;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/8/18.
 */

public class BalloonManager extends MyBaseManager {
    private FlameManager flameManager;
    private float torchWidth;
    private float torchHeigh;

    public BalloonManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
    }
    public void setFlameManager(FlameManager flameManager){
        this.flameManager = flameManager;
    }
    public void createBalloon(Scene scene,float x,float y,float torchX,float torchY){
        Balloon balloon = new Balloon(engine,context,null,null,null);
        balloon.setInScene(scene,
                ScaleHelper.getInstance().getXLocation(x,width),
                ScaleHelper.getInstance().getYLocation(y,height),
                wScale,hScale,150,null);
        balloon.setTorch(scene,
                ScaleHelper.getInstance().getXLocation(torchX,torchWidth),
                ScaleHelper.getInstance().getYLocation(torchY,torchHeigh),150);
        balloon.setPhysic(physicsWorld);
        balloon.setCollide(flameManager);
    }
    @Override
    protected void init() {
        width = 400;
        height = 113;
        torchWidth = 70;
        torchHeigh = 182;
    }
}
