package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;



import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;

import cn.vailing.chunqiu.promethues.bean.AccelerateDevice;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/8/17.
 */

public class AccelerateDeviceManager extends MyBaseManager {
    private FlameManager flameManger;

    public AccelerateDeviceManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
    }
    public void setFlameManager(FlameManager flameManger){
        this.flameManger = flameManger;
    }
    public void createAccelerateDevice(Scene scene, float x, float y,int angle) {
        AccelerateDevice accelerateDevice = new AccelerateDevice(engine, context, null, null, null);
        accelerateDevice.setInScene(scene,
                ScaleHelper.getInstance().getXLocation(x, width),
                ScaleHelper.getInstance().getYLocation(y, height), wScale, hScale, 150, null);
        accelerateDevice.setAngle(angle);
        accelerateDevice.setCollides(flameManger);
    }

    @Override
    protected void init() {
        width = 55;
        height = 55;
    }
}
