package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;


import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;

import java.util.ArrayList;
import java.util.List;

import cn.vailing.chunqiu.promethues.bean.Rope;
import cn.vailing.chunqiu.promethues.bean.RopeConnector;
import cn.vailing.chunqiu.promethues.is.IRopeBurningListener;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.MathUtil;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/7/24.
 */

public class RopeManager extends MyBaseManager {
    private int h;
    private int w;
    private List<Rope> ropes;
    private float offset = 3f;
    private Scene scene;
    private int index;
    private Runnable cupRopeRunnable = new Runnable() {
        @Override
        public void run() {
            cutRope(index, 0);
        }
    };

    public RopeManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
    }


    public void createRope(Scene scene, Body begin, float offsetBX, float offsetBY, boolean beginC, Body end, float offsetEX, float offsetEY, boolean endC) {
        this.scene = scene;
        float X = begin.getWorldCenter().x * PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT + offsetBX;
        float Y = begin.getWorldCenter().y * PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT + offsetBY;
        Vector2 vector2 = end.getWorldCenter().mul(PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT).cpy().add(offsetEX, offsetEY);
        float angle = MathUtil.judgeAngle(X, Y, vector2.x, vector2.y);
        float offsetX = ((h - offset * wScale) * MathUtil.Rcos(angle)) * wScale;
        float offsetY = ((h - offset * hScale) * MathUtil.Rsin(angle)) * hScale;
        Body temp = begin;
        float tempX = X;
        float tempY = Y;
        float distance = MathUtil.pythagorean(X, Y, vector2.x, vector2.y);
        int num = (int) (distance / ((h - offset * hScale) * hScale));
        for (int i = 0; i <= num; i++) {
            Rope rope = new Rope(engine, context, null, null, null);
            rope.setInScene(scene,
                    ScaleHelper.getInstance().getRealCenterX(X, w),
                    ScaleHelper.getInstance().getRealCenterY(Y, h), wScale, hScale, 0, null);
            rope.setRotation(angle);
            Body body = rope.createBody(physicsWorld);
            if (i == 0) {
                RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                revoluteJointDef.initialize(temp, body, new Vector2(body.getWorldCenter().x,
                        body.getWorldCenter().y));
                physicsWorld.createJoint(revoluteJointDef);
            } else {
                RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                revoluteJointDef.initialize(temp, body, new Vector2(body.getWorldCenter().x - (offsetX) / 64f,
                        body.getWorldCenter().y - offsetY / 64f));
                physicsWorld.createJoint(revoluteJointDef);
            }

            ropes.add(rope);
            temp = body;
            Y += offsetY;
            X += offsetX;
        }

        RevoluteJointDef endJoint = new RevoluteJointDef();
        endJoint.initialize(end, temp, temp.getWorldCenter());
        physicsWorld.createJoint(endJoint);
        if (beginC) {
            RopeConnector ropeConnector = new RopeConnector(engine, context, null, null, null);
            ropeConnector.setInScene(scene,
                    ScaleHelper.getInstance().getRealCenterX(tempX, ropeConnector.getRegion().getWidth()),
                    ScaleHelper.getInstance().getRealCenterY(tempY, ropeConnector.getRegion().getHeight()),
                    wScale, hScale, 0, null);
            Body ropeConnectorBody = ropeConnector.createBody(physicsWorld);

            WeldJointDef weld2 = new WeldJointDef();
            weld2.initialize(ropeConnectorBody, begin, ropeConnectorBody.getWorldCenter());
            physicsWorld.createJoint(weld2);
        }
        if (endC) {
            RopeConnector ropeConnector = new RopeConnector(engine, context, null, null, null);
            ropeConnector.setInScene(scene,
                    ScaleHelper.getInstance().getRealCenterX(temp.getWorldCenter().x * 32f, ropeConnector.getRegion().getWidth()),
                    ScaleHelper.getInstance().getRealCenterY(temp.getWorldCenter().y * 32f, ropeConnector.getRegion().getHeight()),
                    wScale, hScale, 0, null);
            Body ropeConnectorBody = ropeConnector.createBody(physicsWorld);
            WeldJointDef weld2 = new WeldJointDef();
            weld2.initialize(ropeConnectorBody, end, ropeConnectorBody.getWorldCenter());
            physicsWorld.createJoint(weld2);
        }
    }

    public void setCollision(final FlameManager flameManager) {
        IUpdateHandler collisionIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    for (index = 0; index < ropes.size(); index++) {
                        if (ropes.get(index).getSprite().collidesWith(flameManager.getCutSprite())) {
                            flameManager.setVisibleByScale(true, 0, new OnFinishListener() {
                                @Override
                                public void finish() {
                                    flameManager.finish();
                                }
                            });
                            engine.runSafely(RopeManager.this.cupRopeRunnable);
                            scene.unregisterUpdateHandler(this);
                        }
                    }
                }

            }

            @Override
            public void reset() {

            }
        };
        scene.registerUpdateHandler(collisionIU);
    }

    /*
       -1,0,1
        0 both
       -1 down
        1 up
     */
    private boolean markOne;
    private boolean markTwo;

    public void cutRope(final int index, final int type) {
        if (!ropes.get(index).isChange()&&ropes!=null) {
            ropes.get(index).cut(new IRopeBurningListener() {
                @Override
                public void start() {
                    if (index - 1 >= 0) {
                        cutRope(index - 1, -1);
                    }
                    if (index + 1 <= ropes.size() - 1) {
                        cutRope(index + 1, 1);
                    }
                }

                @Override
                public void finish() {
                    engine.runOnUpdateThread(new Runnable() {
                        @Override
                        public void run() {
                        Engine.EngineLock engineLock = engine.getEngineLock();
                        engineLock.lock();
                        scene.detachChild(ropes.get(index).getSprite());
                        engineLock.unlock();
                            if (markOne && markTwo) {
                                ropes.clear();
                                ropes = null;
                            }
                        }
                    });
                    if (index - 1 >= 0) {
                        if (type != 1)
                            cutRope(index - 1, -1);
                    } else {
                        markOne = true;
                    }
                    if (index + 1 <= ropes.size() - 1) {
                        if (type != -1)
                            cutRope(index + 1, 1);
                    } else {
                        markTwo = true;
                    }
                }
            });
        }

    }

    @Override
    protected void init() {
        ropes = new ArrayList<>();
        h = 20;
        w = 10;
    }

}
