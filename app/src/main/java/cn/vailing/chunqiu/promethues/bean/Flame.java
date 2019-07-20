package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;


import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.modifier.IModifier;

import java.io.IOException;

import cn.vailing.chunqiu.promethues.is.MIAnimationListener;
import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.MathUtil;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.SoundHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;


/**
 * Created by dream on 2017/6/6.
 */
@SuppressWarnings("all")
public class Flame extends MyBaseSprite {
    private int speed;
    private MoveModifier nowMoveModifier;
    private float angle;
    private boolean isVisible;
    private Shape cut;
    private Scene scene;
    private MyPhysicsWorld physicsWorld;
    private Body frameBody;
    private IUpdateHandler searchIUpdateHandler;
    private boolean isDark;
    private Vector2 gravity;
    private IUpdateHandler boundIU;
    private Rectangle rectangle;
    private Rectangle bkRectangle;

    protected BitmapTextureAtlas protectorTexture;
    protected TiledTextureRegion protectorRegion;
    protected AnimatedSprite protector;

    protected BitmapTextureAtlas protectorDieTexture;
    protected TiledTextureRegion protectorDieRegion;
    protected AnimatedSprite protectorDie;

    private float centreX;
    private float centreY;
    private boolean isOnCloseProtector;
    public boolean isCreate;
    private Sound mFireSound;
    private Sound mflySound;
    private Sound mboomSound;
    private TiledTextureRegion flameBoomRegion;

    public Flame(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/flame.png", new Vector2(840, 268), new Vector2(7, 4));
    }

    @Override
    public void Myinit() {
        textureRegion = TextureHelper.getInstance().getFlamelRegion();
    }

    @Override
    public void setInScene(Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {

        wScale = xScale;
        hScale = yScale;
        Log.e("vailing",wScale+"  "+hScale);
        sprite = new AnimatedSprite(0, 0, this.textureRegion, engine.getVertexBufferObjectManager());
        sprite.animate(60);

        bkRectangle = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight(), engine.getVertexBufferObjectManager());
        bkRectangle.setScale(xScale, yScale);
        bkRectangle.setColor(0, 0, 0, 0);
        scene.attachChild(bkRectangle);

        bkRectangle.attachChild(sprite);

        centreX = bkRectangle.getWidth() / 2;
        centreY = bkRectangle.getHeight() / 2;

        this.scene = scene;

        rectangle = new Rectangle(bkRectangle.getWidth() * 0.25f, bkRectangle.getHeight() * 0.35f, bkRectangle.getWidth() * 0.5f, bkRectangle.getHeight() * 0.3f, engine.getVertexBufferObjectManager());
        rectangle.setColor(0, 0, 0, 0);
        bkRectangle.attachChild(rectangle);

        cut = new Rectangle(rectangle.getWidth(), rectangle.getHeight() / 2, 1, 1, engine.getVertexBufferObjectManager());
        cut.setColor(0, 0, 0, 0);
        rectangle.attachChild(cut);
        if (physicsWorld != null) {
            frameBody = PhysicsFactory.createCircleBody(physicsWorld, bkRectangle, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1f, 0.5f, 0.5f, false, (short) 0, (short) 0, (short) 0));
            physicsWorld.registerPhysicsConnector(new PhysicsConnector(bkRectangle, frameBody, true, true));
            physicsWorld.addToRemoveGravity(frameBody);
            isCreate = true;
        }
        bkRectangle.setVisible(false);
    }

    public Rectangle getBkRectangle() {
        return bkRectangle;
    }

    public Shape getCut() {
        return cut;
    }

    public void moveNS(float angle) {
        this.angle = angle;
        frameBody.setLinearVelocity(0, 0);
        frameBody.setTransform(frameBody.getPosition(), (float) Math.toRadians(angle));
        frameBody.setLinearVelocity(new Vector2(speed * MathUtil.Rcos(-angle) * wScale, -speed * MathUtil.Rsin(-angle) * hScale));
        if (bkRectangle.getUpdateHandlerCount() == 0) {
            bkRectangle.registerUpdateHandler(boundIU);
        }

        if (isDark) {
            if (sprite.getUpdateHandlerCount() == 0) {
                sprite.registerUpdateHandler(searchIUpdateHandler);
            }

        }

    }

    public void start(final float angle) {
        bkRectangle.registerEntityModifier(new ParallelEntityModifier(
                new ScaleModifier(0.2f, 0, wScale, 0, hScale, new IEntityModifier.IEntityModifierListener() {
                    @Override
                    public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {
                        bkRectangle.setVisible(true);
                        move(angle);
                    }

                    @Override
                    public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {

                    }
                })));

    }

    public void move(float angle) {
        this.angle = angle;
        stop();
        frameBody.setTransform(frameBody.getPosition(), (float) Math.toRadians(angle));
        frameBody.setLinearVelocity(new Vector2(speed * MathUtil.Rcos(-angle) * wScale, -speed * MathUtil.Rsin(-angle) * hScale));
        if (bkRectangle.getUpdateHandlerCount() == 0) {
            bkRectangle.registerUpdateHandler(boundIU);
        }
        playSound(mFireSound);
        mflySound.setLooping(true);
        playSound(mflySound);

        if (isDark) {
            if (sprite.getUpdateHandlerCount() == 0) {
                sprite.registerUpdateHandler(searchIUpdateHandler);
            }

        }

    }

    public void stop() {
        mflySound.stop();
        frameBody.setLinearVelocity(0, 0);
    }

    public void beginMove(float angle) {
        this.angle = angle;
    }

    @Override
    protected void init() {
        speed = 15;
        protectorRegion = TextureHelper.getInstance().getProtectorrRegion();
        protectorDieRegion = TextureHelper.getInstance().getProtectorDieRegion();
        flameBoomRegion = TextureHelper.getInstance().getFlameBoomRegion();
        mFireSound = SoundHelper.getInstance().getmFireSound();
        mflySound = SoundHelper.getInstance().getMflySound();
        mboomSound = SoundHelper.getInstance().getMboomSound();

    }

    public void start(float x, float y, final float angle, float longs, final OnFinishListener finishListener) {
        frameBody.setTransform(new Vector2(x / 32, y / 32), (float) Math.toRadians(angle));
        bkRectangle.registerEntityModifier(new ParallelEntityModifier(
                new ScaleModifier(0.2f, 0, wScale, 0, hScale, new IEntityModifier.IEntityModifierListener() {
                    @Override
                    public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {
                        move(angle);
                    }

                    @Override
                    public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                        if (finishListener != null)
                            finishListener.finish();
                    }
                })
        ));
    }

    public void setDark(final Dark dark) {
        isDark = true;
        searchIUpdateHandler = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                dark.setVisible((int) (bkRectangle.getX() + bkRectangle.getWidth() * wScale / 2), (int) bkRectangle.getY());
            }

            @Override
            public void reset() {

            }
        };
        dark.setVisible((int) (bkRectangle.getX() + bkRectangle.getWidth() * wScale / 2), (int) bkRectangle.getY());
    }

    public void setBound(final float top, final float left, final float bottom, final float right, final OnFinishListener onFinishListener) {
        boundIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {

                if (getCenterY() < top || getCenterX() < left || getCenterX() > right || getCenterY() > bottom) {
                    stop();
                    bkRectangle.unregisterUpdateHandler(boundIU);
                    if (onFinishListener != null) {
                        onFinishListener.finish();
                    }
                }
            }

            @Override
            public void reset() {

            }
        };
    }

    private int getCenterX() {
        return (int) (bkRectangle.getX() + bkRectangle.getWidth() / 2);
    }

    public int getCenterY() {
        return (int) (bkRectangle.getY() + bkRectangle.getHeight() / 2);
    }

    public void setVisible(long delay, boolean visible) {
        isVisible = visible;
        bkRectangle.setVisible(isVisible);
    }

    public void setVisibleByScale(boolean b, float longs, final OnFinishListener onFinishListener) {
//        mFireSound.stop();
        mflySound.stop();
        mboomSound.stop();
        bkRectangle.registerEntityModifier(new ParallelEntityModifier(
                new ScaleModifier(0.2f, ScaleHelper.getInstance().getWidthScale(), 0, ScaleHelper.getInstance().getHeightScale(), 0, new IEntityModifier.IEntityModifierListener() {
                    @Override
                    public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {
                    }

                    @Override
                    public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                        stop();
                        setVisible(0, false);
                        if (onFinishListener != null)
                            onFinishListener.finish();
                    }
                })
        ));
    }

    public float getAngle() {
        return bkRectangle.getRotation() - 90;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }


    public void setPosition(float x, float y, float z) {
        frameBody.setTransform(x / 32, y / 32, (float) Math.toRadians(z));
    }

    public void addForce(float angle) {
        Vector2 be = new Vector2(speed * MathUtil.Rcos(angle) * wScale * 0.5f, -speed * MathUtil.Rsin(angle) * hScale * 0.5f).add(frameBody.getLinearVelocity());
        frameBody.setLinearVelocity(be);
        frameBody.setTransform(frameBody.getPosition(), (float) Math.atan2(be.y, be.x));
    }

    public int getZIndex() {
        return sprite.getZIndex();
    }

    public void detach() {
        engine.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                Engine.EngineLock engineLock = engine.getEngineLock();
                engineLock.lock();
                final PhysicsConnector facePhysicsConnector = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
                physicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
                physicsWorld.destroyBody(frameBody);
                scene.detachChild(bkRectangle);
                engineLock.unlock();
            }
        });
    }

    public void detachSpirte(final Sprite sprite) {
        sprite.setVisible(false);
        engine.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                Engine.EngineLock engineLock = engine.getEngineLock();
                engineLock.lock();
                bkRectangle.detachChild(sprite);
                engineLock.unlock();
            }
        });
    }

    public AnimatedSprite getProtectorDie() {
        return protectorDie;
    }

    public void setPhysic(MyPhysicsWorld physicsWorld) {
        this.physicsWorld = physicsWorld;
    }

    public void openProtection() {
        sprite.registerEntityModifier(new ScaleModifier(0.5f, 1f, 0.5f));
        protector = new AnimatedSprite(centreX - protectorRegion.getWidth() / 2, centreY - protectorRegion.getHeight() / 2,
                protectorRegion, engine.getVertexBufferObjectManager());
        protector.animate(80);
        protector.setRotation(90);
        bkRectangle.attachChild(protector);
    }

    public void closeProtection(final OnFinishListener onFinishListener) {
        isOnCloseProtector = true;
        detachSpirte(protector);
        protectorDie = new AnimatedSprite(centreX - protectorDieRegion.getWidth() / 2, centreY - protectorDieRegion.getHeight() / 2,
                protectorDieRegion, engine.getVertexBufferObjectManager());
        protectorDie.setRotation(90);
        bkRectangle.attachChild(protectorDie);
        protectorDie.animate(60, false);
        sprite.registerEntityModifier(new ScaleModifier(0.5f, 0.5f, 1f, new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {

            }

            @Override
            public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                detachSpirte(protectorDie);
                if (onFinishListener != null)
                    onFinishListener.finish();
                isOnCloseProtector = false;
            }
        }));
    }

    public void flameBoom(final OnFinishListener onFinishListener) {
        mFireSound.stop();
        mflySound.stop();
        playSound(mboomSound);
        final AnimatedSprite boom = new AnimatedSprite(centreX - flameBoomRegion.getWidth() / 2, centreY - flameBoomRegion.getHeight() / 2, flameBoomRegion, engine.getVertexBufferObjectManager());
        sprite.setVisible(false);
        bkRectangle.attachChild(boom);

        boom.animate(80, false, new MIAnimationListener() {
            @Override
            public void onAnimationFinished(AnimatedSprite animatedSprite) {
                if (onFinishListener != null) {
                    onFinishListener.finish();
                    detachSpirte(boom);
                }

            }
        });
    }

    public void reset() {
        speed = 15;
        sprite = null;
        bkRectangle = null;
        rectangle = null;
        cut = null;
    }

    public Rectangle getColliodSprite() {
        return rectangle;
    }

    public boolean isOnCloseProtector() {
        return isOnCloseProtector;
    }

    public void animal() {
        sprite.animate(60);
    }


    public void stopYS() {
        frameBody.setLinearVelocity(0, 0);
    }
}
