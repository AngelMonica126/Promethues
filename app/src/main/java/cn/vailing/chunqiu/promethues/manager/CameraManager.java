package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.IModifier;

import cn.vailing.chunqiu.promethues.override.MapleStoryCamera;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;

/**
 * Created by dream on 2017/9/26.
 */

public class CameraManager extends MyBaseManager {
    private FlameManager flameManager;
    private float bx;
    private float ex;
    private float by;
    private float ey;

    public CameraManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, null);
    }

    public void setFlameManager(FlameManager flameManager) {
        this.flameManager = flameManager;
    }

    public void setMoveCamera(Scene scene, final MapleStoryCamera camera) {

        final Rectangle rectangle = new Rectangle(bx, by, 1, 1, engine.getVertexBufferObjectManager());
        rectangle.setColor(0, 0, 0, 0);
        rectangle.registerEntityModifier(new SequenceEntityModifier(
                new MoveModifier(4f, ex, bx, ey, by, new IEntityModifier.IEntityModifierListener() {
                    @Override
                    public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {

                    }

                    @Override
                    public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                        if (!flameManager.getFollow()) {
                            camera.reset();
                            flameManager.setFollow(camera);
                        }
                    }
                }))
        );
        camera.setChaseEntity(rectangle);
        scene.attachChild(rectangle);
        scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {
                if (touchEvent.isActionUp()) {
                    if (!flameManager.getFollow()) {
                        camera.reset();
                        flameManager.setFollow(camera);
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void init() {

    }

    public void setBound(float bx, float by, float ex, float ey) {
        this.bx=bx;
        this.by=by;
        this.ex=ex;
        this.ey=ey;
    }
}
