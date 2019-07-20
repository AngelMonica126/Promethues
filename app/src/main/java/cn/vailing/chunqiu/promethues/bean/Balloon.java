package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;


import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import java.io.IOException;

import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.manager.FlameManager;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/8/2.
 */

public class Balloon extends MyBaseSprite {
    protected TiledTextureRegion torchTextureRegion;
    protected AnimatedSprite torch;

    protected TiledTextureRegion fireTextureRegion;
    protected AnimatedSprite fire;
    private Rectangle rectangle;
    private IUpdateHandler collideIU;
    private Body body;
    private float speedY;
    private float speedX;
    private Rectangle test;
    private PrismaticJoint joint;
    private Sound mflySound;
    private Sound mMoveSound;

    public Balloon(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/Balloon.png", new Vector2(1200, 140), new Vector2(5, 2));
    }

    @Override
    public void Myinit() {
        textureRegion = TextureHelper.getInstance().getBalloonRegion();
    }

    @Override
    public void setInScene( Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {
        super.setInScene(scene, x, y, xScale, yScale, 0, mySpriteOnclickListener);
        test = new Rectangle(x, 0, 1, ScaleHelper.getInstance().getAppHeight(), engine.getVertexBufferObjectManager());
        test.setColor(0, 0, 0, 0);
        scene.attachChild(test);
    }

    public void setTorch(Scene scene, float x, float y, long frame) {
        torch = new AnimatedSprite(x, y, torchTextureRegion, engine.getVertexBufferObjectManager());
        torch.setScale(wScale, hScale);
        scene.attachChild(torch);
        rectangle = new Rectangle(0, -torch.getWidth()*0.3f, torch.getWidth(), torch.getWidth()*0.5f, engine.getVertexBufferObjectManager());
        rectangle.setColor(0,0, 0, 0);
        torch.attachChild(rectangle);
    }

    /*
        改进：
            1：testsBody设置进游戏里
     */
    public void setPhysic(PhysicsWorld physicsWorld) {
        body = PhysicsFactory.createBoxBody(physicsWorld, sprite, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1f, 0, 1f));
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
        Body tests = PhysicsFactory.createBoxBody(physicsWorld, test, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(1f, 0.2f, 1f, false, (short) 0, (short) 0, (short) 0));
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(test, tests, true, true));
        PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
        prismaticJointDef.initialize(body, tests, body.getWorldCenter(), new Vector2(0, 1));
        prismaticJointDef.enableLimit = true;
        prismaticJointDef.enableMotor = true;
        prismaticJointDef.maxMotorForce =body.getMass()*physicsWorld.getGravity().y+40*hScale;
        prismaticJointDef.motorSpeed=40*hScale;
        joint = (PrismaticJoint) physicsWorld.createJoint(prismaticJointDef);


    }

    public void setCollide(final FlameManager flameManager) {
        collideIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    if (rectangle.collidesWith(flameManager.getFlameSprite())) {
                        torch.unregisterUpdateHandler(collideIU);
                        createFire();
                        flameManager.setVisibleByScale(false, sprite.getWidth() * wScale, new OnFinishListener() {
                            @Override
                            public void finish() {
                                flameManager.finish();
                            }
                        });
                        fireBall();

                    }
                }
            }

            @Override
            public void reset() {

            }
        };
        torch.registerUpdateHandler(collideIU);
    }

    private void createFire() {
        mflySound.setLooping(true);
        playSound(mflySound);
        playSound(mMoveSound);
        float x = -torchTextureRegion.getWidth() * 0.15f;
        float y = -torchTextureRegion.getHeight() * 0.5f;
        fire = new AnimatedSprite(x, y, fireTextureRegion, engine.getVertexBufferObjectManager());
        fire.animate(80);
        torch.attachChild(fire);
//        fire.setZIndex(torch.getZIndex()-1);
    }

    public void fireBall() {

        sprite.animate(60, false);
        joint.enableLimit(false);
// /        joint.setMaxMotorForce(200 * hScale);
//        joint.setLimits(0, ScaleHelper.getInstance().getAppHeight() / 32f);
//        joint.setMotorSpeed(20f * hScale);
    }

    @Override
    protected void init() {
        torchTextureRegion = TextureHelper.getInstance().getBalloonTowerRegion();
        fireTextureRegion = TextureHelper.getInstance().getBalloonFireRegion();
        try {
            mflySound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/flameFly.ogg");
            mMoveSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/gravityMove.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
