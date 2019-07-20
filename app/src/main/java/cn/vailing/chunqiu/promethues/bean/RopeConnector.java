package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.Engine;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

/**
 * Created by dream on 2017/8/4.
 */

public class RopeConnector extends MyBaseSprite {
    private PhysicsWorld physicWorld;
    private Body connector;

    public RopeConnector(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "gfx/Splash/ropeConnector.png", new Vector2(28,28), new Vector2(1,1));
    }
    public Body createBody(PhysicsWorld physicsWorld) {
        this.physicWorld = physicsWorld;
        connector = PhysicsFactory.createBoxBody(physicsWorld, sprite, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(5f, 1f, 1f, false, (short) 0, (short) 0, (short) 0));
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, connector, true, true));
        return connector;
    }

    public Body getBody() {
        return connector;
    }

    @Override
    protected void init() {

    }
}
