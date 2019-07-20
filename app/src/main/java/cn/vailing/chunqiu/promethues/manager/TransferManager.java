package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;



import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;

import cn.vailing.chunqiu.promethues.bean.Transfer;
import cn.vailing.chunqiu.promethues.override.MapleStoryCamera;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/8/17.
 */

public class TransferManager extends MyBaseManager {
    private FlameManager flameManger;
    private boolean isFollow;
    private MapleStoryCamera camera;

    public TransferManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
    }
    public void setFlameManger(FlameManager flameManger){
        this.flameManger = flameManger;
    }
    public void createTransfer(Scene scene,float x1,float y1,float angle1,float x2,float y2,float angle2,boolean isPositive){
        Transfer transfer = new Transfer(engine,context,null,null,null);
        transfer.setFirstSprite(scene,
                ScaleHelper.getInstance().getXLocation(x1,width),
                ScaleHelper.getInstance().getYLocation(y1,height),
                wScale,hScale,150);
        transfer.setFirstRotate(angle1);
        transfer.setPositive(isPositive);
        transfer.setOtherSprite(scene,
                ScaleHelper.getInstance().getXLocation(x2,width),
                ScaleHelper.getInstance().getYLocation(y2,height),
                wScale,hScale,150);
        transfer.setSecondRotate(angle2);
        transfer.setCollides(flameManger);
        if(isFollow)
        transfer.setFollow(camera);

    }
    public void setFollow(MapleStoryCamera camera,boolean isFollow){
        this.isFollow  = isFollow;
        this.camera =camera;
    }
    @Override
    protected void init() {
        height = 253;
        width = 70;
    }
}
