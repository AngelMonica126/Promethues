package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.modifier.IModifier;

import java.util.ArrayList;
import java.util.List;

import cn.vailing.chunqiu.promethues.is.MIAnimationListener;
import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.manager.FlameManager;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/7/10.
 */

public class Wood extends MyBaseSprite {
    private Body body;
    private PhysicsWorld physicsWorld;
    private List<WoodConnector> woodConnectors;

    public Wood(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/Wood.png", new Vector2(19, 200), new Vector2(1, 1));
    }

    @Override
    public void Myinit() {
        textureRegion = TextureHelper.getInstance().getWoodRegion();
    }

    @Override
    public void setInScene( Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {
        super.setInScene(scene, x, y, xScale, yScale, 0, mySpriteOnclickListener);
    }

    public Body setPhysic(PhysicsWorld physicsWorld) {
        this.physicsWorld = physicsWorld;
        body = PhysicsFactory.createBoxBody(physicsWorld, sprite, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1f, 0.2f, 1f));
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
        return body;
    }

    public Body getBody() {
        return body;
    }

    public void setScale(float x, float y) {
        sprite.setScale(x, y);
    }

    public void setRotation(float angle) {
        sprite.setRotation(angle);
    }

    public void applyForce(Vector2 force, Vector2 center) {
        body.applyForce(force, new Vector2(body.getWorldCenter().x, body.getWorldCenter().y));
    }
    public void setCollide(final FlameManager flameManager){
        sprite.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    if (sprite.collidesWith(flameManager.getFlameSprite())) {
                        if (flameManager.isProtect()&&!flameManager.isOnClosePortector()) {
                            float angle = -flameManager.getAngle() + 2 * sprite.getRotation();
                            flameManager.move(angle-90);
                            flameManager.closeProtection();
                        } else if(!flameManager.isProtect()){
                            sprite.unregisterUpdateHandler(this);
                            final IUpdateHandler temp = this;
                            flameManager.boom(new OnFinishListener() {
                                @Override
                                public void finish() {
                                    flameManager.finish();
                                    sprite.registerUpdateHandler(temp);
                                }
                            });

                        }
                    }
                }
            }

            @Override
            public void reset() {

            }
        });
    }
    public void detach(final Scene scene) {
        sprite.animate(50, false, new MIAnimationListener() {
            @Override
            public void onAnimationFinished(AnimatedSprite animatedSprite) {

            }
        });
        sprite.registerEntityModifier(new AlphaModifier(1f, 1f, 0, new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {
                for (WoodConnector woodConnector : woodConnectors) {
                  woodConnector.detach(scene);
                }
            }

            @Override
            public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                engine.runOnUpdateThread(new Runnable() {
                    @Override
                    public void run() {
                        Engine.EngineLock engineLock = engine.getEngineLock();
                        engineLock.lock();
                        if (physicsWorld != null) {
                            final PhysicsConnector facePhysicsConnector = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
                            physicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
                            physicsWorld.destroyBody(facePhysicsConnector.getBody());
                        }
                        scene.detachChild(sprite);
                        engineLock.unlock();
                    }
                });
            }
        }));
//        sprite.animate(400, true, new AnimatedSprite.IAnimationListener() {
//            @Override
//            public void onAnimationStarted(AnimatedSprite animatedSprite, int i) {
//
//            }
//
//            @Override
//            public void onAnimationFrameChanged(AnimatedSprite animatedSprite, int i, int i1) {
//
//            }
//
//            @Override
//            public void onAnimationLoopFinished(AnimatedSprite animatedSprite, int i, int i1) {
//
//            }
//
//            @Override
//            public void onAnimationFinished(AnimatedSprite animatedSprite) {
//
//            }
//        });
    }

    public void addConnector(WoodConnector woodConnector) {
        woodConnectors.add(woodConnector);
    }

    @Override
    protected void init() {
        woodConnectors = new ArrayList<>();
    }
}
