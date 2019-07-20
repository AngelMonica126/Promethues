package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;


import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.IModifier;

import java.io.IOException;

import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.manager.FlameManager;
import cn.vailing.chunqiu.promethues.override.MapleStoryCamera;
import cn.vailing.chunqiu.promethues.util.PathHelper;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/6/26.
 */

public class Transfer extends MyBaseSprite {
    protected AnimatedSprite otherSprite;
    private FlameManager flameManager;
    private IUpdateHandler iupdateHandlerS;
    private Rectangle firstRectangle;
    private Rectangle secondRectangle;
    private IUpdateHandler iupdateHandlerF;
    private boolean isFire;
    private Rectangle secondBottom;
    private Rectangle firstBottom;
    private Sound mTransferSound;
    private boolean isPositive;
    private boolean isFollow;
    private MapleStoryCamera camera;
    private Scene scene;

    public Transfer(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/Transfor.png", new Vector2(2200, 475), new Vector2(11, 2));
    }

    @Override
    public void Myinit() {
        textureRegion = TextureHelper.getInstance().getTransforRegion();
    }

    @Override
    protected void init() {
        try {
            mTransferSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/transfer.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOtherSprite(Scene scene, float x, float y, float widthScale, float heightScale, int frame) {
        otherSprite = new AnimatedSprite(x, y, this.textureRegion, engine.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
                    fireFlame(2);
                return true;
            }
        };

        otherSprite.animate(60, false);
        otherSprite.setScale(widthScale, heightScale);
        scene.attachChild(otherSprite);
        secondRectangle = new Rectangle(otherSprite.getWidth() / 2, 0, 1, otherSprite.getHeight() * 0.8f, engine.getVertexBufferObjectManager());
        secondRectangle.setColor(0, 0, 0, 0);

        secondBottom = new Rectangle(otherSprite.getWidth() * 0.3f, otherSprite.getHeight() * 0.9f, otherSprite.getWidth() * 0.5f, 1, engine.getVertexBufferObjectManager());
        secondBottom.setColor(0, 0, 0, 0);

        otherSprite.attachChild(secondRectangle);
        otherSprite.attachChild(secondBottom);
        scene.registerTouchArea(otherSprite);
    }

    public void setFirstSprite(Scene scene, float xLocation, float yLocation, float widthScale, float heightScale, int i) {
        setInScene(scene, xLocation, yLocation, widthScale, heightScale, 0, null);
        sprite.animate(60, false);
        firstRectangle = new Rectangle(sprite.getWidth() / 2, 0, 1, sprite.getHeight() * 0.8f, engine.getVertexBufferObjectManager());
        firstRectangle.setColor(0, 0, 0, 0);

        firstBottom = new Rectangle(sprite.getWidth() * 0.3f, sprite.getHeight() * 0.9f, sprite.getWidth() * 0.5f, 1, engine.getVertexBufferObjectManager());
        firstBottom.setColor(0, 0, 0, 0);
        sprite.attachChild(firstRectangle);
        sprite.attachChild(firstBottom);
        this.scene = scene;
    }

    public void setFirstRotate(float angle) {
        sprite.setRotation(angle);
    }

    public void setSecondRotate(float angle) {
        otherSprite.setRotation(angle);
    }

    public void setCollides(final FlameManager flameManager) {
        Transfer.this.flameManager = flameManager;
        iupdateHandlerF = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    if (firstRectangle.collidesWith(flameManager.getFlameSprite())) {
                        if (!isFire) {
                            isOpenIU(false);
                            playSound(mTransferSound);
                            flameManager.setVisibleByScale(false, sprite.getWidth() * wScale, new OnFinishListener() {
                                @Override
                                public void finish() {
                                    fireFlame(2);

                                }
                            });
                        }
                    } else if (firstBottom.collidesWith(flameManager.getFlameSprite())) {
                        isOpenIU(false);
                        flameManager.boom(new OnFinishListener() {
                            @Override
                            public void finish() {
                                flameManager.finish();
                                isOpenIU(true);
                            }
                        });
                    }
                }

            }

            @Override
            public void reset() {

            }
        };
        iupdateHandlerS = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    if (secondRectangle.collidesWith(flameManager.getFlameSprite())) {
                        if (!isFire) {
                            isOpenIU(false);
                            playSound(mTransferSound);
                            flameManager.setVisibleByScale(false, sprite.getWidth() * wScale, new OnFinishListener() {
                                @Override
                                public void finish() {
                                    fireFlame(1);
                                }
                            });
                        }
                    } else if (secondBottom.collidesWith(flameManager.getFlameSprite())) {
                        isOpenIU(false);
                        flameManager.boom(new OnFinishListener() {
                            @Override
                            public void finish() {
                                flameManager.finish();
                                isOpenIU(true);
                            }
                        });
                    }
                }

            }

            @Override
            public void reset() {

            }
        };
        isOpenIU(true);
    }

    private void fireFlame(int i) {
        float centerX = 0;
        float centerY = 0;
        float angle = -sprite.getRotation();
        switch (i) {
            case 1:
                centerX = sprite.getX() + sprite.getWidth() / 2;
                centerY = sprite.getY() + sprite.getHeight() / 2;
                angle = sprite.getRotation();
                if (isFollow) {
                    Rectangle rectangle = new Rectangle(otherSprite.getX() + otherSprite.getWidth() / 2,
                            otherSprite.getY() + otherSprite.getHeight() / 2, 10, 10, engine.getVertexBufferObjectManager());
                    scene.attachChild(rectangle);
                    rectangle.setColor(0,0,0,0);
                    rectangle.registerEntityModifier(new MoveModifier(1.5f,rectangle.getX(),sprite.getX() + sprite.getWidth() / 2,rectangle.getY(),
                            sprite.getY() + sprite.getHeight() / 2));
                    camera.setChaseEntity(rectangle);
                }
                break;
            case 2:
                centerX = otherSprite.getX() + otherSprite.getWidth() / 2;
                centerY = otherSprite.getY() + otherSprite.getHeight() / 2;
                angle = otherSprite.getRotation();
                if (isFollow) {
                    Rectangle rectangle = new Rectangle(sprite.getX() + sprite.getWidth() / 2,
                            sprite.getY() + sprite.getHeight() / 2, 10, 10, engine.getVertexBufferObjectManager());
                    scene.attachChild(rectangle);
                    rectangle.setColor(0,0,0,0);
                    rectangle.registerEntityModifier(new MoveModifier(1.5f,rectangle.getX(),otherSprite.getX() + otherSprite.getWidth() / 2,rectangle.getY(),
                            otherSprite.getY() + otherSprite.getHeight() / 2));
                    camera.setChaseEntity(rectangle);
                }
                break;
        }
        angle = (isPositive ? angle : (angle + 180));
        final float finalCenterX = centerX;
        final float finalCenterY = centerY;
        final float finalAngle = angle;
        sprite.registerEntityModifier(new AlphaModifier(1.5f, 1f, 1f, new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {
                if(isFollow)
                camera.resume();
            }

            @Override
            public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                flameManager.setVisible(0, true);
                flameManager.start(
                        finalCenterX,
                        finalCenterY,
                        finalAngle,
                        sprite.getWidth() * ScaleHelper.getInstance().getWidthScale(),
                        new OnFinishListener() {
                            @Override
                            public void finish() {
                                if (isFollow)
                                    camera.setChaseEntity(flameManager.getBkSprite());
                                isOpenIU(true);
                            }
                        });
            }
        }));
    }

    private void isOpenIU(boolean isOpen) {
        if (isOpen) {
            sprite.registerUpdateHandler(iupdateHandlerF);
            otherSprite.registerUpdateHandler(iupdateHandlerS);
        } else {
            sprite.unregisterUpdateHandler(iupdateHandlerF);
            otherSprite.unregisterUpdateHandler(iupdateHandlerS);
        }
    }

    public void setPositive(boolean isPositive) {
        this.isPositive = isPositive;
    }

    public void setFollow(MapleStoryCamera camera) {
        this.isFollow = true;
        this.camera = camera;
    }
}