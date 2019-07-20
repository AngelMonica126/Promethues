package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;


import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;

import cn.vailing.chunqiu.promethues.bean.Medusa;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/8/18.
 */

public class MedusaManager extends MyBaseManager {
    private FlameManager flameManager;

    public MedusaManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
    }
    public void setFlameManager(FlameManager flameManager){
        this.flameManager = flameManager;
    }
    public void createMedusa(Scene scene ,float x,float y,int radius){
        Medusa medusa = new Medusa(engine,context,null,null,null);
        medusa.setBKArea(scene,
                ScaleHelper.getInstance().getXLocation(x,width),
                ScaleHelper.getInstance().getYLocation(y,height),
                wScale, hScale,150,radius,radius);
        medusa.setCollision(flameManager);
    }
    @Override
    protected void init() {
        width = 55;
        height = 55;
    }
}
