package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.modifier.IModifier;

import cn.vailing.chunqiu.promethues.is.MIAnimationListener;
import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.manager.FlameManager;
import cn.vailing.chunqiu.promethues.manager.WoodManager;
import cn.vailing.chunqiu.promethues.util.MathUtil;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/7/22.
 */

public class FireDevice extends MyBaseSprite {
    private Rectangle rectangle;
    private Rectangle collideRectangle;
    private IUpdateHandler collideWoodIU;
    private Rectangle noCollised;
    protected BitmapTextureAtlas fireTexture;
    protected TiledTextureRegion fireTextureRegion;
    protected AnimatedSprite fire;

    public FireDevice(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/FireDeviceUp.png", new Vector2(1250, 360), new Vector2(5, 2));
    }

    @Override
    public void setInScene(Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {
        super.setInScene(scene, x, y, xScale, yScale, 0, null);
//        rectangle = new Rectangle(sprite.getWidth() / 2, 0, sprite.getWidth(), sprite.getHeight(), engine.getVertexBufferObjectManager());
//        rectangle.setColor(1f, 0, 0);
//        rectangle.setVisible(false);
        fire = new AnimatedSprite(sprite.getWidth() * 0.6f, -sprite.getHeight() * 0.55f, fireTextureRegion, engine.getVertexBufferObjectManager());
        fire.setRotation(120);
        fire.setZIndex(-1);
        fire.setVisible(false);
        sprite.attachChild(fire);

        collideRectangle = new Rectangle(0, sprite.getHeight() * 0.3f, 1, sprite.getHeight() * 0.6f, engine.getVertexBufferObjectManager());
        collideRectangle.setColor(0, 0, 0, 0);

        noCollised = new Rectangle(sprite.getHeight() * 0.1f, sprite.getHeight() * 0.3f, sprite.getWidth() * 0.8f, sprite.getHeight() * 0.6f, engine.getVertexBufferObjectManager());
        noCollised.setColor(0, 0, 0, 0);

        sprite.attachChild(collideRectangle);
        sprite.attachChild(noCollised);
    }

    public void setCollide(final FlameManager flameManager) {
        collideRectangle.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    if (collideRectangle.collidesWith(flameManager.getFlameSprite()) && !flameManager.isChange()) {
                        flameManager.setVisibleByScale(false, sprite.getWidth() / 5 * wScale, null);
                        setTempVisible(flameManager);
                    }
                }
            }

            @Override
            public void reset() {

            }
        });
        noCollised.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    if (noCollised.collidesWith(flameManager.getFlameSprite()) && !flameManager.isChange()) {
                        flameManager.boom(new OnFinishListener() {
                            @Override
                            public void finish() {
                                flameManager.finish();
                            }
                        });
                    }
                }

            }

            @Override
            public void reset() {

            }
        });
    }


    public void setCollideWood(final Scene scene, final WoodManager woodManager) {
        collideWoodIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                for (int i = 0; i < woodManager.getWoods().size(); i++) {
                    if (fire.collidesWith(woodManager.getWoods().get(i).getSprite())) {
                        float angle = sprite.getRotation();
                        Vector2 force = new Vector2(1000 * wScale * MathUtil.Rcos(angle), -1000 * hScale * MathUtil.Rsin(angle));
                        Vector2 center = new Vector2((sprite.getWidth() + sprite.getX()) * wScale * 2 / 32f, (sprite.getHeight() + sprite.getY()) * hScale / 32f);
                        woodManager.applyForce(i, force, center);
                        woodManager.detach(scene, i);
                    }
                }
            }

            @Override
            public void reset() {

            }
        };
    }

    public void setRotate(float angle) {
        sprite.setRotation(angle);
    }

    private void setTempVisible(final FlameManager flameManager) {
        sprite.animate(60, false, new MIAnimationListener() {

            @Override
            public void onAnimationFinished(AnimatedSprite animatedSprite) {
                fire(flameManager);
                texture.clearTextureAtlasSources();
                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "device/FireDeviceStatic.png", 0, 0, 5, 2);
                sprite.animate(80);
            }
        });

    }

    private void fire(final FlameManager flameManager) {
        fire.animate(30);
        fire.setScaleCenter(fireTextureRegion.getWidth() * (1 + wScale) / 2, fireTextureRegion.getHeight() * (1 + hScale) / 2);
        fire.registerEntityModifier(new SequenceEntityModifier(
                new ScaleModifier(1f, 0, 1f, new IEntityModifier.IEntityModifierListener() {
                    @Override
                    public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {
                        fire.registerUpdateHandler(collideWoodIU);
                        fire.setVisible(true);
                    }

                    @Override
                    public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {

                    }
                }),
                new ScaleModifier(2f, 1f, 1f, new IEntityModifier.IEntityModifierListener() {
                    @Override
                    public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {

                    }

                    @Override
                    public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                        fire.setVisible(false);
                        fire.stopAnimation();
                        fire.unregisterUpdateHandler(collideWoodIU);
                        sprite.stopAnimation(0);
                        texture.clearTextureAtlasSources();
                        BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "device/FireDeviceDown.png", 0, 0, 5, 2);
                        sprite.animate(60, false, new MIAnimationListener() {
                            @Override
                            public void onAnimationFinished(AnimatedSprite animatedSprite) {
                                flameManager.finish();
                                sprite.stopAnimation(0);
                                texture.clearTextureAtlasSources();
                                BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "device/FireDeviceUp.png", 0, 0, 5, 2);
                            }
                        });
                    }
                })
                )
        );


    }

    @Override
    protected void init() {
        fireTextureRegion = TextureHelper.getInstance().getFireDeviceFireRegion();
    }
}
