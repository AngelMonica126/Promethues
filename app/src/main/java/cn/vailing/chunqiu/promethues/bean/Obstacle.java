package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;

/**
 * Created by dream on 2017/7/10.
 */

public class Obstacle extends MyBaseSprite {
    private Body body;

    public Obstacle(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/Obstacle.png", new Vector2(128, 42), new Vector2(1, 1));
    }

    @Override
    public void setInScene(Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {
        super.setInScene(scene, x, y, xScale, yScale, 0, null);
    }

    public void setPhysic(PhysicsWorld physicsWorld) {
        body = PhysicsFactory.createBoxBody(physicsWorld, sprite, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(1f, 1f, 1f));
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
    }

    public void setScale(float x, float y) {
        sprite.setScale(x, y);
    }

    public void setRotation(float angle) {
        sprite.setRotation(angle);
    }


    @Override
    protected void init() {

    }
}
