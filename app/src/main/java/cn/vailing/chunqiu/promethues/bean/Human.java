package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.modifier.IModifier;

import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.is.OnGameManger;
import cn.vailing.chunqiu.promethues.manager.FlameManager;

/**
 * Created by dream on 2017/8/12.
 */

public class Human extends MyBaseSprite {
    private OnGameManger onGameManger;
    private FlameManager flameManager;
    private Rectangle rectangle;
    private int testX;
    private int testY;
    private boolean isChangeToFeel;
    private IUpdateHandler feelIU;
    private TextureRegion humanBTextRegion;
    private TextureRegion humanATextRegion;
    private Sprite handA;
    private Sprite handB;
    private TextureRegion HuoBaTextRegion;
    private Sprite huoBa;
    private TiledTextureRegion faceTextRegion;
    private AnimatedSprite face;
    private float centreX;
    private float centreY;
    private TiledTextureRegion FlameStaticRegion;
    private AnimatedSprite flame;

    public Human(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize, OnGameManger onGameManger) {
        super(engine, context, "device/Human.png", new Vector2(200, 259), new Vector2(1, 1));
        this.onGameManger = onGameManger;
    }

    @Override
    public void setInScene( Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {
        float rotateX = x + textureRegion.getWidth() / 2 - textureRegion.getWidth() / 2;
        float rotateY = y + textureRegion.getHeight() / 2 - textureRegion.getHeight() / 2;
        rectangle = new Rectangle(rotateX, rotateY, textureRegion.getWidth() * xScale * 2, textureRegion.getHeight() * yScale * 2, engine.getVertexBufferObjectManager());
        rectangle.setColor(0, 0, 0, 0);
        scene.attachChild(rectangle);
        testX = (int) (rotateX + rectangle.getWidth() / 2);
        testY = (int) (rotateY + rectangle.getHeight() / 2);

        super.setInScene(scene, x, y, xScale, yScale, 0, mySpriteOnclickListener);
        setCollision();
        centreX  = sprite.getX()+sprite.getWidth()/2;
        centreY  = sprite.getY()+sprite.getHeight()/2;
//        sprite.setSkew(0);
        face  = new AnimatedSprite(0,0,faceTextRegion,engine.getVertexBufferObjectManager());
        sprite.attachChild(face);
        face.stopAnimation(1);
        handA = new Sprite(0, 0, humanATextRegion, engine.getVertexBufferObjectManager());
        sprite.attachChild(handA);
        handA.setRotationCenter(handA.getWidth()*0.395f,handA.getHeight()*0.405f);
        handA.setRotation(20);

        handB = new Sprite(0, 0, humanBTextRegion, engine.getVertexBufferObjectManager());
        handA.attachChild(handB);

        handB.setRotationCenter(handB.getWidth()*0.34f,handB.getHeight()*0.498f);
        handB.setRotation(-25);
        huoBa = new Sprite(0,0,HuoBaTextRegion,engine.getVertexBufferObjectManager());
        handB.attachChild(huoBa);
        huoBa.setRotationCenter(huoBa.getWidth()*0.3f,huoBa.getHeight()*0.448f);
        flame = new AnimatedSprite(huoBa.getWidth()*0.175f,huoBa.getHeight()*0.12f,FlameStaticRegion,engine.getVertexBufferObjectManager());
        huoBa.attachChild(flame);
        flame.setVisible(false);
//        sprite.setSkew(100);

    }

    private void setCollision() {
        feelIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    if (rectangle.collidesWith(flameManager.getFlameSprite())) {
                        if (!isChangeToFeel) {
                            feelingFlame();
                        }
                    } else {
                        if (isChangeToFeel) {
                            changeBack();
                        }
                    }
                }
            }

            @Override
            public void reset() {

            }
        };
        rectangle.registerUpdateHandler(feelIU);
        sprite.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                if (flameManager.isCreate()) {
                    if (sprite.collidesWith(flameManager.getFlameSprite())) {
                        flameManager.setVisibleByScale(true,0,null);
                        rectangle.clearUpdateHandlers();
                        gameVictory();
                    }
                }
            }

            @Override
            public void reset() {

            }
        });
    }

    private void gameVictory() {
//        texture.clearTextureAtlasSources();
//        float angle = (float) Math.toDegrees(Math.atan2(flameManager.getBkSprite().getY()-centreY,
//                flameManager.getBkSprite().getX()-centreX ));
//        Log.e("vailing",angle+"  ");
        flame.animate(50);
        flame.setVisible(true);
        sprite.registerEntityModifier(new AlphaModifier(0.8f, 1f, 1f, new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {

            }

            @Override
            public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                if (onGameManger != null) {
                    onGameManger.gameVictory();
                }
            }
        }));
//        BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "gfx/Splash/HumanCatchFlame.png", 0, 0, 9, 1);
//        sprite.animate(OtherUtil.getAnimateFrame(13, 9, 150), false, new MIAnimationListener() {
//            @Override
//            public void onAnimationFinished(AnimatedSprite animatedSprite) {
//                if (onGameManger != null) {
//                    onGameManger.gameVictory();
//                }
//            }
//        });
    }

    private void changeBack() {
        isChangeToFeel = false;
//        texture.clearTextureAtlasSources();
//        BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "gfx/Splash/HumanNormal.png", 0, 0, 13, 1);
//        sprite.animate(150);
    }

    private void feelingFlame() {
        isChangeToFeel = true;

//        texture.clearTextureAtlasSources();
//        BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "gfx/Splash/HumanFeelingFlame.png", 0, 0, 9, 1);
//        sprite.animate(OtherUtil.getAnimateFrame(13, 9, 150));

    }

    public void setFlameManager(FlameManager flameManager) {
        this.flameManager = flameManager;
    }

    @Override
    protected void init() {
        BitmapTextureAtlas humanBTexture = new BitmapTextureAtlas(engine.getTextureManager(), 200, 259, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        humanBTextRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(humanBTexture, context, "device/HumanHandB.png", 0, 0);
        humanBTexture.load();

        BitmapTextureAtlas humanATexture = new BitmapTextureAtlas(engine.getTextureManager(), 200, 259, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        humanATextRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(humanATexture, context, "device/HumanHandA.png", 0, 0);
        humanATexture.load();

        BitmapTextureAtlas HuoBaTexture = new BitmapTextureAtlas(engine.getTextureManager(), 200, 259, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        HuoBaTextRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(HuoBaTexture, context, "device/Huoba.png", 0, 0);
        HuoBaTexture.load();

        BitmapTextureAtlas faceTexture = new BitmapTextureAtlas(engine.getTextureManager(), 600, 259, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        faceTextRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(faceTexture, context, "device/HumanFace.png", 0, 0,3,1);
        faceTexture.load();
        BitmapTextureAtlas FlameStatic = new BitmapTextureAtlas(engine.getTextureManager(), 1350, 50, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        FlameStaticRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(FlameStatic, context, "device/FlameStatic.png", 0, 0, 27, 1);
        FlameStatic.load();
    }
}
