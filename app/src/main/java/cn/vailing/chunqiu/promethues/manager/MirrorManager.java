package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;


import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;

import cn.vailing.chunqiu.promethues.bean.Mirror;
import cn.vailing.chunqiu.promethues.is.OnGameManger;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/8/17.
 */

public class MirrorManager extends MyBaseManager {
    private FlameManager flameManager;
    private OnGameManger onGameManger;

    public MirrorManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
    }

    public void setFlameManager(FlameManager flameManager){
        this.flameManager = flameManager;
    }
    public void createMirror(Scene scene, float x,float y ,int angle ,boolean isCanBeRotated,
                             float offsetX,float offsetY,int beginAngle,int endAngle){
        Mirror mirror = new Mirror(engine,context,null,null,null);
        mirror.setInScene(scene,
                ScaleHelper.getInstance().getXLocation(x,width),
                ScaleHelper.getInstance().getYLocation(y,height),
                wScale,hScale,150,null);
        mirror.setMirrorAgree(angle);

        if (isCanBeRotated){
            mirror.setCanBeRotated(scene,offsetX,offsetY,wScale,hScale,beginAngle,endAngle);
        }
        mirror.setCollides(flameManager);

    }
    public void createMirror(Scene scene, float x,float y ,int angle){
        Mirror mirror = new Mirror(engine,context,null,null,null);
        mirror.setInScene(scene,
                ScaleHelper.getInstance().getXLocation(x,width),
                ScaleHelper.getInstance().getYLocation(y,height),
                wScale,hScale,150,null);
        mirror.setMirrorAgree(angle);

        mirror.setCollides(flameManager);

    }

    @Override
    protected void init() {
        width = 250;
        height = 250;
    }
}
