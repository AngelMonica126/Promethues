package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;


import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;

import cn.vailing.chunqiu.promethues.bean.WoodConnector;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/8/5.
 */

public class WoodConnectorManager extends MyBaseManager{
    private float width;
    private float height;

    public WoodConnectorManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
    }


    @Override
    protected void init() {
        height = 299;
        width = 29;
    }

    public WoodConnector connect(Scene scene, final Body one, final Body two, int type) {
        WoodConnector w = null;
        switch (type) {
            case 1:
                w = createStraightConnector(scene, one, two);
                break;
            case 2:
                w = createVerticalConnector(scene, one, two);
                break;
            case 3:
                w = createConvexConnector(scene, one, two);
                break;
            case 4:
                w = createCrossConnector(scene, one, two);
                break;
        }

        return w;
    }

    private WoodConnector createConvexConnector(Scene scene, Body one, Body two) {
        Vector2 intersection = getIntersection(one, two);
        final int angle = (int) (Math.toDegrees(one.getAngle()) - 90);
        final WoodConnector woodConnector = new WoodConnector(engine, context, "device/woodConnectorThree.png", null, null);
        woodConnector.setInScene(scene,
                ScaleHelper.getInstance().getRealCenterX(intersection.x, woodConnector.getRegion().getWidth()),
                ScaleHelper.getInstance().getRealCenterY(intersection.y, woodConnector.getRegion().getHeight()),
                wScale, hScale, 0, null);
        woodConnector.setRotation(angle);

        Body connector = woodConnector.createBody(physicsWorld);

//        WeldJointDef weld1 = new WeldJointDef();
//        weld1.initialize(one, connector, connector.getWorldCenter());
//        physicsWorld.createJoint(weld1);

        WeldJointDef weld2 = new WeldJointDef();
        weld2.initialize(one, connector, connector.getWorldCenter());
        physicsWorld.createJoint(weld2);

        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.initialize(one, two, connector.getWorldCenter());
        physicsWorld.createJoint(weldJointDef);
        return woodConnector;
    }

    private Vector2 getIntersection(Body one, Body two) {
        Vector2 first = one.getWorldCenter().mul(PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT);
        Vector2 second = two.getWorldCenter().mul(PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT);
        final int angle = (int) (Math.toDegrees(one.getAngle()) - 90);
        float X = 0, Y = 0;
        if (angle % (90) == 0 || angle == 0) {
            if ((angle - 90) % 180 == 0) {
                X = first.x;
                Y = second.y;
            } else if ((angle - 90) % 90 == 0) {
                X = second.x;
                Y = first.y;
            }
        } else {
            float k1 = (float) (1 / Math.tan(Math.toRadians(angle)));
            float k2 = -1 / k1;
            X = ((second.y - first.y) - (k2 * second.x - k1 * first.x)) / (k1 - k2);
            Y = k1 * X + first.y - k1 * first.x;
        }
        Vector2 intersection = new Vector2(X, Y);
        return intersection;
    }

    private WoodConnector createVerticalConnector(Scene scene, Body one, Body two) {
        Vector2 intersection = getIntersection(one, two);
        final int angle = (int) (Math.toDegrees(one.getAngle()) - 90);
        final WoodConnector woodConnector = new WoodConnector(engine, context, "device/woodConnectorTwo.png", null, null);
        woodConnector.setInScene(scene,
                ScaleHelper.getInstance().getRealCenterX(intersection.x, woodConnector.getRegion().getWidth()),
                ScaleHelper.getInstance().getRealCenterY(intersection.y, woodConnector.getRegion().getHeight()),
                wScale, hScale, 0, null);

        woodConnector.setRotation(angle + 90);
        Body connector = woodConnector.createBody(physicsWorld);

        WeldJointDef weld = new WeldJointDef();
        weld.initialize(one, connector, connector.getWorldCenter());
        physicsWorld.createJoint(weld);

        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.initialize(one, two, connector.getWorldCenter());
        physicsWorld.createJoint(weldJointDef);
        return woodConnector;
    }

    private WoodConnector createStraightConnector(Scene scene, Body one, Body two) {
        Vector2 first = one.getWorldCenter().mul(PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT);
        Vector2 second = two.getWorldCenter().mul(PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT);
        final float angle = (float) (Math.toDegrees(one.getAngle()) - 90);
        float X = (first.x + second.x) / 2;
        float Y = (first.y + second.y) / 2;
        final WoodConnector woodConnector = new WoodConnector(engine, context, "device/woodConnectorOne.png", null, null);
        woodConnector.setInScene(scene,
                ScaleHelper.getInstance().getRealCenterX(X, woodConnector.getRegion().getWidth()),
                ScaleHelper.getInstance().getRealCenterY(Y, woodConnector.getRegion().getHeight()),
                wScale, hScale, 0, null);
        woodConnector.setRotation(angle);
        Body connector = woodConnector.createBody(physicsWorld);

        WeldJointDef weld = new WeldJointDef();
        weld.initialize(one, connector, connector.getWorldCenter());
        physicsWorld.createJoint(weld);

        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.initialize(one, two, connector.getWorldCenter());
        physicsWorld.createJoint(weldJointDef);
        return woodConnector;
    }

    private WoodConnector createCrossConnector(Scene scene, Body one, Body two) {
        Vector2 intersection = getIntersection(one, two);
        final int angle = (int) (Math.toDegrees(one.getAngle()) - 90);
        final WoodConnector woodConnector = new WoodConnector(engine, context, "device/woodConnectorFour.png", null, null);
        woodConnector.setInScene(scene,
                ScaleHelper.getInstance().getRealCenterX(intersection.x, woodConnector.getRegion().getWidth()),
                ScaleHelper.getInstance().getRealCenterY(intersection.y, woodConnector.getRegion().getHeight()),
                wScale, hScale, 0, null);
        woodConnector.setRotation(angle + 90);
        Body connector = woodConnector.createBody(physicsWorld);

        WeldJointDef weld = new WeldJointDef();
        weld.initialize(one, connector, connector.getWorldCenter());
        physicsWorld.createJoint(weld);

        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.initialize(one, two, connector.getWorldCenter());
        physicsWorld.createJoint(weldJointDef);
        return woodConnector;
    }
}
