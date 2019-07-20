package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.modifier.IModifier;

import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;

/**
 * Created by dream on 2017/8/5.
 */

public class WoodConnector extends MyBaseSprite {
    private PhysicsWorld physicWorld;
    private Body connector;

    public WoodConnector(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, path, new Vector2(35, 35), new Vector2(1, 1));
    }

    @Override
    public void setInScene(Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {
        super.setInScene(scene, x, y, xScale, yScale, frame, mySpriteOnclickListener);
    }

    public Body createBody(PhysicsWorld physicsWorld) {
        this.physicWorld = physicsWorld;
        connector = PhysicsFactory.createBoxBody(physicsWorld, sprite, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1f, 1f, 1f, false, (short) 0, (short) 0, (short) 0));
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, connector, true, true));
        return connector;
    }

    public void setRotation(float angle) {
        sprite.setRotation(angle);
    }

    public Body getBody() {
        return connector;
    }
    public void detach(final Scene scene) {
        sprite.registerEntityModifier(new AlphaModifier(1f, 1f, 0, new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {

            }

            @Override
            public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                engine.runOnUpdateThread(new Runnable() {
                    @Override
                    public void run() {
                        Engine.EngineLock engineLock = engine.getEngineLock();
                        engineLock.lock();
                        if (physicWorld != null) {
                            final PhysicsConnector facePhysicsConnector = physicWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
                            physicWorld.unregisterPhysicsConnector(facePhysicsConnector);
                            physicWorld.destroyBody(facePhysicsConnector.getBody());
                        }
                        scene.detachChild(sprite);
                        engineLock.unlock();
                    }
                });
            }
        }));

    }
    @Override
    protected void init() {

    }
}
