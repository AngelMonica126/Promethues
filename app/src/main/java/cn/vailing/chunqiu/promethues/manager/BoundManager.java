package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;


import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;

import java.util.ArrayList;
import java.util.List;

import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;

/**
 * Created by dream on 2017/8/21.
 */

public class BoundManager extends MyBaseManager {
    private FlameManager flameManager;
    private List<Rectangle>rectangles;
    private int[] angles = new int[]{90,0,90,0};
    public BoundManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
        rectangles = new ArrayList<>();
    }

    public void setFlameManager(FlameManager flameManager) {
        this.flameManager = flameManager;
    }

    public void createBound(Scene scene,float w,float h){
        Rectangle left = new Rectangle(0,0,1,h,engine.getVertexBufferObjectManager());
        Rectangle top = new Rectangle(0,0,w,1,engine.getVertexBufferObjectManager());
        Rectangle right = new Rectangle(w,0,1,h,engine.getVertexBufferObjectManager());
        Rectangle bottom = new Rectangle(0,h,w,1,engine.getVertexBufferObjectManager());
        left.setColor(0,0,0,0);
        top.setColor(0,0,0,0);
        right.setColor(0,0,0,0);
        bottom.setColor(0,0,0,0);
        scene.attachChild(left);
        scene.attachChild(top);
        scene.attachChild(right);
        scene.attachChild(bottom);
        rectangles.add(left);
        rectangles.add(top);
        rectangles.add(right);
        rectangles.add(bottom);
        setCollides();
    }

    private void setCollides() {
        for(int i =0;i<rectangles.size();i++){
            final int finalI = i;
            rectangles.get(i).registerUpdateHandler(new IUpdateHandler() {
                @Override
                public void onUpdate(float v) {
                    if (flameManager.isCreate()) {
                        if (rectangles.get(finalI).collidesWith(flameManager.getFlameSprite())) {
                            if (flameManager.isProtect()) {
                                float angle = -flameManager.getAngle() + 2 * angles[finalI];
                                flameManager.move( -angle);
                                flameManager.closeProtection();
                            }
                        }
                    }
                }

                @Override
                public void reset() {

                }
            });
        }
    }

    @Override
    protected void init() {

    }
}
