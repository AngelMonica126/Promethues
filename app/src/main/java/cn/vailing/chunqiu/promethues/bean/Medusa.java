package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import java.io.IOException;

import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.manager.FlameManager;
import cn.vailing.chunqiu.promethues.util.MathUtil;
import cn.vailing.chunqiu.promethues.util.PathHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/7/6.
 */
@SuppressWarnings("all")
public class Medusa extends MyBaseSprite {
    private BitmapTextureAtlas BKTexture;
    private TiledTextureRegion BKTextureRegion;
    private AnimatedSprite BKSprite;
    private boolean isVisible;
    private IUpdateHandler collisionIU;
    private float centerX;
    private float centerY;
    private float w;
    private float h;
    private Scene scene;
    private IUpdateHandler moveIU;
    private FlameManager flameManager;
    private IUpdateHandler boomIU;
    private Sound mdeviceDownSound;
    private Sound mdeviceUpSound;

    public Medusa(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/Medusa.png", new Vector2(1200, 120), new Vector2(10, 1));
    }

    @Override
    public void Myinit() {
        textureRegion = TextureHelper.getInstance().getMedusaRegion();
    }

    @Override
    public void setInScene(Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {
        super.setInScene(scene, x, y, xScale, yScale, 0, null);
        Rectangle rectangle = new Rectangle(sprite.getWidth() * -0.2f, sprite.getHeight() * -0.2f, sprite.getWidth() * 1.4f, sprite.getHeight() * 1.4f, engine.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        playSound(mdeviceDownSound);
                        break;
                    case TouchEvent.ACTION_UP:
                        playSound(mdeviceUpSound);
                        Medusa.this.setVisible();
                        if (isVisible) {
                            fireFlame();
                        }
                        break;
                }
                return true;
            }
        };
        rectangle.setColor(0, 0, 0, 0);
        sprite.attachChild(rectangle);
        scene.registerTouchArea(rectangle);
    }

    private void fireFlame() {
        BKSprite.clearUpdateHandlers();
        float angle = flameManager.getAngle();
        flameManager.move(angle + 90);
    }

    public void setBKArea(Scene scene, float x, float y, float xScale, float yScale, long frame, int wRadius, int hRadius) {
        this.scene = scene;
        setScale(xScale, yScale);
        centerX = x + getRegion().getWidth() / 2;
        centerY = y + getRegion().getHeight() / 2;
        w = wRadius * wScale * 2;
        h = hRadius * hScale * 2;
        BKSprite = new AnimatedSprite(centerX - w / 2, centerY - h / 2, BKTextureRegion, engine.getVertexBufferObjectManager());
        BKSprite.animate(40);
        BKSprite.setSize(w, h);
        scene.attachChild(BKSprite);
        setVisible();
        setInScene(scene, x, y, xScale, yScale, frame, null);
    }

    public void setCollision(final FlameManager flameManager) {
        this.flameManager = flameManager;
        collisionIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    float flameX = flameManager.getBkSprite().getX() + flameManager.getWidth() / 2;
                    float flameY = flameManager.getBkSprite().getY() + flameManager.getHeight() / 2;
                    if (Math.pow(flameX - centerX, 2) + Math.pow(flameY - centerY, 2) <= Math.pow(w * 0.9f / 2, 2)) {
                        BKSprite.unregisterUpdateHandler(collisionIU);
                        moveFlame(flameManager, flameX, flameY);
                    }
                }
            }

            @Override
            public void reset() {

            }
        };
        boomIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate())
                    if (sprite.collidesWith(flameManager.getFlameSprite())) {
                        sprite.unregisterUpdateHandler(this);
                        flameManager.boom(new OnFinishListener() {
                            @Override
                            public void finish() {
                                flameManager.finish();
                                sprite.registerUpdateHandler(boomIU);
                            }
                        });

                    }
            }

            @Override
            public void reset() {

            }
        };
        sprite.registerUpdateHandler(boomIU);
    }

    private void moveFlame(final FlameManager flameManager, float flameX, float flameY) {
        flameManager.stopYS();
        float angle = (float) Math.toDegrees(Math.atan2(centerY - flameY, centerX - flameX));
        float Kangle = -angle + flameManager.getAngle() + 90;
        boolean isPositive = false;
        float kAngle = -(float) Math.toDegrees(Math.atan2(flameX - centerX, flameY - centerY));
        if (Kangle < 0) {
            isPositive = true;
        }
        float radius = MathUtil.pythagorean(centerX, centerY, flameX, flameY);
        final PathHelper pathHelper = new PathHelper(centerX, centerY, radius, (int) (180 + angle), 1f * wScale, isPositive);
        moveIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (!flameManager.isDestory()) {
                    flameManager.setPosition(pathHelper.circular().x,
                            pathHelper.circular().y, pathHelper.circular().z - 90);
                } else {
                    isVisible=false;
                    setVisible();
                    BKSprite.unregisterUpdateHandler(moveIU);
                }

            }

            @Override
            public void reset() {

            }
        };
        BKSprite.registerUpdateHandler(moveIU);
    }

    public void setVisible() {
        BKSprite.setVisible(isVisible);
        if (isVisible) {
            if (sprite != null)
                sprite.animate(60, false);
            if (collisionIU != null)
                BKSprite.registerUpdateHandler(collisionIU);
        } else {
            if (sprite != null)
                sprite.stopAnimation(0);
            if (collisionIU != null)
                BKSprite.unregisterUpdateHandler(collisionIU);
        }
        isVisible = !isVisible;
    }

    @Override
    protected void init() {
        BKTextureRegion = TextureHelper.getInstance().getMedusaBKRegion();
        try {
            mdeviceDownSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/deviceDown.ogg");
            mdeviceUpSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "sound/deviceUp.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
