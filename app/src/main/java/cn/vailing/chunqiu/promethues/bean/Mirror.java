package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import java.io.IOException;

import cn.vailing.chunqiu.promethues.is.MIAnimationListener;
import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.manager.FlameManager;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;


/**
 * Created by dream on 2017/6/19.
 */

public class Mirror extends MyBaseSprite {
    private BitmapTextureAtlas rotateTexture;
    private ITextureRegion rotateTextureRegion;
    private Sprite rotate;
    double startX = 0;
    double startY = 0;
    private int testX;
    private int testY;
    private double rotateAgree = 0;
    private Rectangle rectangle;
    private float myX;
    private float myY;
    private float mirrorAgree = 0;
    private Rectangle childRectangle;
    protected BitmapTextureAtlas fireTexture;
    protected TiledTextureRegion fireTextureRegion;
    protected AnimatedSprite fire;
    private Scene scene;
    private Rectangle collision;
    private IUpdateHandler collisionIU;
    private IUpdateHandler protectIU;
    private Sound mBlinkSound;
    private Rectangle noCollision;
    private IUpdateHandler noCollisIU;

    public Mirror(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/Mirror.png", new Vector2(320, 320), new Vector2(1, 1));
    }

    @Override
    public void Myinit() {
        textureRegion = TextureHelper.getInstance().getMirrorRegion();
    }

    @Override
    public void setInScene(Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {
        super.setInScene(scene, x, y, xScale, yScale, 0, mySpriteOnclickListener);
        this.scene = scene;
        this.myX = x;
        this.myY = y;
        childRectangle = new Rectangle(sprite.getWidth() * 0.1f, sprite.getHeight() * 1.8f / 3, sprite.getWidth() * 0.8f, 1, engine.getVertexBufferObjectManager());
        childRectangle.setColor(0,0, 0, 0);
        collision = new Rectangle(sprite.getWidth() * 0.1f, sprite.getHeight() * 0.4f, sprite.getWidth() * 0.8f, 1, engine.getVertexBufferObjectManager());
        collision.setColor(0,0,0,0);

        noCollision = new Rectangle(sprite.getWidth() * 0.1f, sprite.getHeight() * 0.6f, sprite.getWidth() * 0.8f, sprite.getHeight() * 0.4f, engine.getVertexBufferObjectManager());
        noCollision.setColor(0,0, 0, 0);

        sprite.attachChild(noCollision);
        sprite.attachChild(childRectangle);
        sprite.attachChild(collision);
    }

    public void setCanBeRotated(Scene scene, float offsetX, float offsetY, float xScale, float yScale, final int beginAngle, final int endAngle) {
        float rotateX = myX + sprite.getWidth() / 2 - rotateTextureRegion.getWidth() / 2 + offsetX * xScale;
        float rotateY = myY + sprite.getHeight() / 2 - rotateTextureRegion.getHeight() + offsetY * yScale;
        rectangle = new Rectangle(rotateX, rotateY, rotateTextureRegion.getWidth() * xScale * 2, rotateTextureRegion.getHeight() * yScale * 2, engine.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                return setRotate(pSceneTouchEvent, beginAngle, endAngle);
            }
        };
        rectangle.setColor(0, 0, 0, 0);
        scene.attachChild(rectangle);
        rotate = new Sprite(rotateX, rotateY, rotateTextureRegion, engine.getVertexBufferObjectManager());
        rotate.setScale(xScale, yScale);
        scene.attachChild(rotate);
        testX = (int) (rotateX + rectangle.getWidth() / 2);
        testY = (int) (rotateY + rectangle.getHeight() / 2);
        scene.registerTouchArea(rectangle);
    }

    private boolean setRotate(TouchEvent pSceneTouchEvent, float beginAngle, float endAngle) {
        switch (pSceneTouchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = pSceneTouchEvent.getX();
                startY = pSceneTouchEvent.getY();
                startX -= testX;
                startY -= testY;
                break;
            case MotionEvent.ACTION_MOVE:
                double newX = pSceneTouchEvent.getX();
                double newY = pSceneTouchEvent.getY();
                newX -= testX;
                newY -= testY;
                double A = (startX * newX + startY * newY) / (Math.sqrt(startX * startX + startY * startY) * Math.sqrt(newX * newX + newY * newY));
                if (A <= 1) {
                    double agree = Math.abs(Math.toDegrees(Math.acos(A)));
                    double B = startX * newY - startY * newX;
                    if (B < 0) {
                        agree = -agree;
                    }
                    agree /= 1.5;
                    rotateAgree += agree;
                    rotateAgree %= 360;
                    rotate.setRotation((float) rotateAgree);
                    if (agree > 0) {
                        if (mirrorAgree >= endAngle) {
                            mirrorAgree = endAngle;
                        } else {
                            mirrorAgree++;
                        }
                    } else {

                        if (mirrorAgree <= beginAngle) {
                            mirrorAgree = beginAngle;
                        } else {
                            mirrorAgree--;
                        }
                    }
                    if (mirrorAgree > beginAngle && mirrorAgree < endAngle) {
                        sprite.setRotation(mirrorAgree);
                    }
                    startX = newX;
                    startY = newY;
                }
                break;
        }
        return true;
    }

    public void setCollides(final FlameManager flameManager) {
        collisionIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()){
                    if (childRectangle.collidesWith(flameManager.getFlameSprite())) {
                        playSound(mBlinkSound);
                        float x = flameManager.getBkSprite().getX() + flameManager.getBkSprite().getWidth() * (1 + wScale) / 2;
                        float y = flameManager.getBkSprite().getY() + flameManager.getBkSprite().getHeight() / 2;
                        fire = new AnimatedSprite(ScaleHelper.getInstance().getRealCenterX(x, fireTextureRegion.getWidth()),
                                ScaleHelper.getInstance().getRealCenterY(y, fireTextureRegion.getHeight()),
                                fireTextureRegion, engine.getVertexBufferObjectManager());
                        fire.setScale(wScale * 1.5f, hScale * 1.5f);
                        scene.attachChild(fire);
                        sprite.unregisterUpdateHandler(collisionIU);
//                        noCollision.registerUpdateHandler(noCollisIU);
                        fire.animate(40, false, new MIAnimationListener() {
                            @Override
                            public void onAnimationFinished(AnimatedSprite animatedSprite) {
                                fire.setVisible(false);
                                engine.runOnUpdateThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Engine.EngineLock engineLock = engine.getEngineLock();
                                        engineLock.lock();
                                        scene.detachChild(fire);
                                        engineLock.unlock();
                                        sprite.registerUpdateHandler(collisionIU);
                                    }
                                });
                            }
                        });
                        float angle = -flameManager.getAngle() + 2 * mirrorAgree;
                        flameManager.moveNS(angle - 90);
                    }
                }

            }

            @Override
            public void reset() {

            }
        };

        noCollisIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if(flameManager.isChange()){
                    if(!noCollision.collidesWith(flameManager.getFlameSprite())){
                        sprite.registerUpdateHandler(collisionIU);
                    }
                }
            }

            @Override
            public void reset() {

            }
        };
        protectIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()){
                    if (collision.collidesWith(flameManager.getFlameSprite())) {
                        sprite.unregisterUpdateHandler(collisionIU);
                        collision.unregisterUpdateHandler(this);
                        flameManager.boom(new OnFinishListener() {
                            @Override
                            public void finish() {
                                flameManager.finish();
                                collision.registerUpdateHandler(protectIU);
                            }
                        });
                    }
//                    else if (!collision.collidesWith(flameManager.getFlameSprite())) {
//                        if (sprite.getUpdateHandlerCount() == 0) {
//                            sprite.registerUpdateHandler(collisionIU);
//                        }
//                    }
                }

            }

            @Override
            public void reset() {

            }
        };
        collision.registerUpdateHandler(protectIU);
        sprite.registerUpdateHandler(collisionIU);
    }

    @Override
    protected void init() {
        try {
            mBlinkSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/blink.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        rotateTextureRegion = TextureHelper.getInstance().getRotateRegion();

        fireTextureRegion =TextureHelper.getInstance().getBlinkRegion();
    }

    public float getMirrorAgree() {
        return mirrorAgree;
    }

    public void setMirrorAgree(float mirrorAgree) {
        this.mirrorAgree = mirrorAgree;
        sprite.setRotation(mirrorAgree);
    }
}
