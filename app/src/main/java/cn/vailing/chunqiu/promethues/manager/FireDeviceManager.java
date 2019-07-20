package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;


import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;

import cn.vailing.chunqiu.promethues.bean.FireDevice;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/8/18.
 */

public class FireDeviceManager extends MyBaseManager {
    private FlameManager flameManager;
    private WoodManager woodManager;

    public FireDeviceManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
    }
    public void setFlameManager(FlameManager flameManager){
        this.flameManager = flameManager;
    }
    public void setWoodManager (WoodManager woodManager){
        this.woodManager = woodManager;
    }
    public void createFireDevice(Scene scene,float x,float y,float angle ){
        FireDevice fireDevice = new FireDevice(engine,context,null,null,null);
        fireDevice.setInScene(scene,
                ScaleHelper.getInstance().getXLocation(x,width),
                ScaleHelper.getInstance().getYLocation(y,height),
                wScale,hScale,150,null);
        fireDevice.setRotate(angle);
        fireDevice.setCollide(flameManager);
        fireDevice.setCollideWood(scene,woodManager);
    }

    @Override
    protected void init() {
        width = 241;
        height = 125;
    }
}
