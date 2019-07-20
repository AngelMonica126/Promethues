package cn.vailing.chunqiu.promethues.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.vailing.chunqiu.promethues.R;
import cn.vailing.chunqiu.promethues.activity.baseActivity.PromethuesBaseActivity;
import cn.vailing.chunqiu.promethues.activity.level.zeus.Eight;
import cn.vailing.chunqiu.promethues.activity.level.zeus.Five;
import cn.vailing.chunqiu.promethues.activity.level.zeus.Four;
import cn.vailing.chunqiu.promethues.activity.level.zeus.Nine;
import cn.vailing.chunqiu.promethues.activity.level.zeus.One;
import cn.vailing.chunqiu.promethues.activity.level.zeus.Seven;
import cn.vailing.chunqiu.promethues.activity.level.zeus.Six;
import cn.vailing.chunqiu.promethues.activity.level.zeus.Ten;
import cn.vailing.chunqiu.promethues.activity.level.zeus.Three;
import cn.vailing.chunqiu.promethues.activity.level.zeus.Two;
import cn.vailing.chunqiu.promethues.is.OnLoadListener;
import cn.vailing.chunqiu.promethues.util.ConstantValue;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.SpUtil;

/**
 * Created by dream on 2017/9/24.
 */

public class ChooseActivity extends SimpleBaseGameActivity implements OnLoadListener {
    private static int CAMERA_WIDTH = 480;
    private static int CAMERA_HEIGHT = 320;
    private Camera camera;
    private float wScale;
    private float hScale;
    private TextureRegion mainBKRegion;
    private Scene scene;
    private TextureRegion ChooseBKRegion;
    private TiledTextureRegion Region1;
    private TiledTextureRegion Region2;
    private TiledTextureRegion Region3;
    private TiledTextureRegion Region4;
    private TiledTextureRegion Region5;
    private TiledTextureRegion Region6;
    private TiledTextureRegion Region7;
    private TiledTextureRegion Region8;
    private TiledTextureRegion Region9;
    private TiledTextureRegion Region10;
    private TextureRegion PromethuesFontRegion;
    private Sprite chooseBK;
    private Sound mDownSound;
    private Sound mUpSound;
    private Music mMusic;
    private List<Class> activity;
    private TiledTextureRegion BackMainRegion;
    private TiledTextureRegion ChooseStarRegion;
    private String[] infors;
    private int level;
    private List<TiledTextureRegion> textureRegions;
    private String test = "{\n" +
            "\t\"level\":\"10\",\n" +
            "\t\"infor\":\"2,3,1,0,0,0,0,0,2,3\"\n" +
            "}";
    private Scene mainScene;
    private TextureRegion loadBKTextureRegion;
    private TextureRegion fontTextureRegion;
    private Handler mHandler;
    private OnLoadListener onLoadListener;
    private long mBeginTime;
    private Runnable mLoadScene = new Runnable() {
        @Override
        public void run() {
            if (mEngine != null) {
                onLoadResources();
                loadScene();
                long endTime = System.currentTimeMillis();
                if (endTime - mBeginTime < 3000) {
                    try {
                        Thread.sleep(3000 - (endTime - mBeginTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                onLoadListener.onFinishScene();
            } else {
            }
        }
    };
    private TextureRegion ChangeBKRegion;
    private Sprite changeBK;
    private TiledTextureRegion BeforeRegion;
    private TiledTextureRegion NextRegion;
    private AnimatedSprite next;
    private boolean isInNormal;
    private TiledTextureRegion WinRegion;


    @Override
    public synchronized void onResumeGame() {
        if (this.mEngine != null)
            super.onResumeGame();
    }

    @Override
    protected void onCreateResources() {
        BitmapTextureAtlas loadBKTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        loadBKTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(loadBKTexture, this, "device/SplashBK.png", 0, 0);
        loadBKTexture.load();

        BitmapTextureAtlas fontTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        fontTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(fontTexture, this, "device/Font.png", 0, 0);
        fontTexture.load();

        try {
            mDownSound = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "sound/buttonDown.ogg");
            mUpSound = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "sound/buttonUp.ogg");
            this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "sound/background.mp3");
            this.mMusic.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void onLoadResources() {
        BitmapTextureAtlas mainBKTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mainBKRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainBKTexture, this, "device/mainBK.png", 0, 0);
        mainBKTexture.load();

        BitmapTextureAtlas ChooseBKTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        ChooseBKRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(ChooseBKTexture, this, "device/ChooseBK.png", 0, 0);
        ChooseBKTexture.load();

        BitmapTextureAtlas ChangeBKTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        ChangeBKRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(ChangeBKTexture, this, "device/ChangeBK.png", 0, 0);
        ChangeBKTexture.load();


        BitmapTextureAtlas Texture1 = new BitmapTextureAtlas(mEngine.getTextureManager(), 240, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Region1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Texture1, this, "device/1.png", 0, 0, 2, 1);
        Texture1.load();

        BitmapTextureAtlas Texture2 = new BitmapTextureAtlas(mEngine.getTextureManager(), 240, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Region2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Texture2, this, "device/2.png", 0, 0, 2, 1);
        Texture2.load();

        BitmapTextureAtlas Texture3 = new BitmapTextureAtlas(mEngine.getTextureManager(), 240, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Region3 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Texture3, this, "device/3.png", 0, 0, 2, 1);
        Texture3.load();

        BitmapTextureAtlas Texture4 = new BitmapTextureAtlas(mEngine.getTextureManager(), 240, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Region4 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Texture4, this, "device/4.png", 0, 0, 2, 1);
        Texture4.load();

        BitmapTextureAtlas Texture5 = new BitmapTextureAtlas(mEngine.getTextureManager(), 240, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Region5 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Texture5, this, "device/5.png", 0, 0, 2, 1);
        Texture5.load();

        BitmapTextureAtlas Texture6 = new BitmapTextureAtlas(mEngine.getTextureManager(), 240, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Region6 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Texture6, this, "device/6.png", 0, 0, 2, 1);
        Texture6.load();

        BitmapTextureAtlas Texture7 = new BitmapTextureAtlas(mEngine.getTextureManager(), 240, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Region7 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Texture7, this, "device/7.png", 0, 0, 2, 1);
        Texture7.load();

        BitmapTextureAtlas Texture8 = new BitmapTextureAtlas(mEngine.getTextureManager(), 240, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Region8 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Texture8, this, "device/8.png", 0, 0, 2, 1);
        Texture8.load();

        BitmapTextureAtlas Texture9 = new BitmapTextureAtlas(mEngine.getTextureManager(), 600, 280, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Region9 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Texture9, this, "device/9.png", 0, 0, 2, 1);
        Texture9.load();

        BitmapTextureAtlas Texture10 = new BitmapTextureAtlas(mEngine.getTextureManager(), 700, 280, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Region10 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Texture10, this, "device/10.png", 0, 0, 2, 1);
        Texture10.load();

        BitmapTextureAtlas PromethuesFontTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 2300, 2300, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        PromethuesFontRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(PromethuesFontTexture, this, "device/PromethuesFont.png", 0, 0);
        PromethuesFontTexture.load();
        BitmapTextureAtlas BackMainTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 400, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BackMainRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(BackMainTexture, this, "device/BackMain.png", 0, 0, 2, 1);
        BackMainTexture.load();

        BitmapTextureAtlas ChooseStarTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 150, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        ChooseStarRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(ChooseStarTexture, this, "device/ChooseStar.png", 0, 0, 1, 4);
        ChooseStarTexture.load();

        BitmapTextureAtlas WinTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 225, 300, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        WinRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(WinTexture, this, "device/WinStar.png", 0, 0, 1, 4);
        WinTexture.load();

        BitmapTextureAtlas BeforeTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 400, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BeforeRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(BeforeTexture, this, "device/Before.png", 0, 0, 2, 1);
        BeforeTexture.load();

        BitmapTextureAtlas Nextexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 400, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        NextRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Nextexture, this, "device/Next.png", 0, 0, 2, 1);
        Nextexture.load();
        initData();
    }

    private void initData() {
        activity.add(One.class);
        activity.add(Two.class);
        activity.add(Three.class);
        activity.add(Four.class);
        activity.add(Five.class);
        activity.add(Six.class);
        activity.add(Seven.class);
        activity.add(Eight.class);
        activity.add(Nine.class);
        activity.add(Ten.class);

        textureRegions.add(Region1);
        textureRegions.add(Region2);
        textureRegions.add(Region3);
        textureRegions.add(Region4);
        textureRegions.add(Region5);
        textureRegions.add(Region6);
        textureRegions.add(Region7);
        textureRegions.add(Region8);
        textureRegions.add(Region9);
        textureRegions.add(Region10);
    }

    @Override
    protected Scene onCreateScene() {
        if (SpUtil.getBoolean(this, ConstantValue.MUSIC, false)) {
            mMusic.play();
        }
        mainScene = new Scene();
        Sprite load = new Sprite(ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(0, 1080), loadBKTextureRegion, getVertexBufferObjectManager());
        load.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());


        Scene loadScene = new Scene();
        loadScene.setBackground(new SpriteBackground(load));

        float x = ScaleHelper.getInstance().getXLocation(0, 1920);
        float y = ScaleHelper.getInstance().getYLocation(0, 1080);
        float fx = ScaleHelper.getInstance().getXLocation(1920, 1920);
        float ffx = ScaleHelper.getInstance().getXLocation(-1920, 1920);
        Sprite fontb = new Sprite(x, y, fontTextureRegion, getVertexBufferObjectManager());
        fontb.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());

        Sprite fonta = new Sprite(ffx, y, fontTextureRegion, getVertexBufferObjectManager());
        fonta.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());

        fontb.registerEntityModifier(new LoopEntityModifier(new MoveXModifier(5f, x, fx)));

        fonta.registerEntityModifier(new LoopEntityModifier(new MoveXModifier(5f, ffx, x)));
        loadScene.attachChild(fontb);
        loadScene.attachChild(fonta);

        mainScene.setChildScene(loadScene);

        mHandler.postDelayed(mLoadScene, 0);

        return mainScene;

    }

    public void createChangeStage() {
        changeBK = new Sprite(ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(1080, 1080), ChangeBKRegion, getVertexBufferObjectManager());
        changeBK.setScale(wScale, hScale);
        scene.attachChild(changeBK);
        if(level>=9){
            AnimatedSprite a9 = new AnimatedSprite(changeBK.getWidth() * 0.25f, changeBK.getHeight() * 0.4f, textureRegions.get(8), getVertexBufferObjectManager()) {
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    return onClick(this, pSceneTouchEvent, 8);
                }
            };
            changeBK.attachChild(a9);
            scene.registerTouchArea(a9);
            AnimatedSprite star = new AnimatedSprite(a9.getWidth() * 0.08f, a9.getHeight() * 1.0f, WinRegion, getVertexBufferObjectManager());
            a9.attachChild(star);
            star.stopAnimation(Integer.parseInt(infors[8]));
        }

        if(level>=10){
            AnimatedSprite a10 = new AnimatedSprite(changeBK.getWidth() * 0.6f, changeBK.getHeight() * 0.4f, textureRegions.get(9), getVertexBufferObjectManager()) {
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    return onClick(this, pSceneTouchEvent, 9);
                }
            };
            changeBK.attachChild(a10);
            scene.registerTouchArea(a10);
            AnimatedSprite star = new AnimatedSprite(a10.getWidth() * 0.08f, a10.getHeight() * 1.0f, WinRegion, getVertexBufferObjectManager());
            a10.attachChild(star);
            star.stopAnimation(Integer.parseInt(infors[9]));
        }


    }

    private void loadScene() {
        isInNormal = true;
        scene = new Scene();
        Sprite mainBKSprite = new Sprite(ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(0, 1080), mainBKRegion, getVertexBufferObjectManager());
        mainBKSprite.setScale(wScale, hScale);
        scene.setBackground(new SpriteBackground(mainBKSprite));


        final Sprite promethuesFont = new Sprite(ScaleHelper.getInstance().getXLocation(-190, 2300),
                ScaleHelper.getInstance().getYLocation(-610, 2300), PromethuesFontRegion, getVertexBufferObjectManager());
        promethuesFont.setScale(wScale, hScale);
        scene.attachChild(promethuesFont);
        promethuesFont.registerEntityModifier(new LoopEntityModifier(
                new SequenceEntityModifier(
                        new MoveXModifier(1f, ScaleHelper.getInstance().getXLocation(-190, 2300), ScaleHelper.getInstance().getXLocation(-380, 2300)),
                        new MoveYModifier(2f, ScaleHelper.getInstance().getYLocation(-610, 2300), ScaleHelper.getInstance().getYLocation(-1220, 2300)),
                        new MoveXModifier(2f, ScaleHelper.getInstance().getXLocation(-380, 2300), ScaleHelper.getInstance().getXLocation(0, 2300)),
                        new MoveYModifier(4f, ScaleHelper.getInstance().getYLocation(-1220, 2300), ScaleHelper.getInstance().getYLocation(0, 2300)),
                        new MoveModifier(2f, ScaleHelper.getInstance().getXLocation(0, 2300), ScaleHelper.getInstance().getXLocation(-190, 2300),
                                ScaleHelper.getInstance().getYLocation(0, 2300), ScaleHelper.getInstance().getYLocation(-610, 2300)),
                        new MoveModifier(2f, ScaleHelper.getInstance().getXLocation(-190, 2300), ScaleHelper.getInstance().getXLocation(-380, 2300),
                                ScaleHelper.getInstance().getYLocation(-610, 2300), ScaleHelper.getInstance().getYLocation(0, 2300)),
                        new MoveYModifier(4f, ScaleHelper.getInstance().getYLocation(0, 2300), ScaleHelper.getInstance().getYLocation(-1220, 2300)),

                        new MoveModifier(2f, ScaleHelper.getInstance().getXLocation(-380, 2300), ScaleHelper.getInstance().getXLocation(-190, 2300),
                                ScaleHelper.getInstance().getYLocation(-1220, 2300), ScaleHelper.getInstance().getYLocation(-610, 2300)),
                        new MoveModifier(2f, ScaleHelper.getInstance().getXLocation(-190, 2300), ScaleHelper.getInstance().getXLocation(0, 2300),
                                ScaleHelper.getInstance().getYLocation(-610, 2300), ScaleHelper.getInstance().getYLocation(-1220, 2300)),
                        new MoveYModifier(4f, ScaleHelper.getInstance().getYLocation(-1220, 2300), ScaleHelper.getInstance().getYLocation(0, 2300)),
                        new MoveModifier(2f, ScaleHelper.getInstance().getXLocation(0, 2300), ScaleHelper.getInstance().getXLocation(-190, 2300),
                                ScaleHelper.getInstance().getYLocation(0, 2300), ScaleHelper.getInstance().getYLocation(-610, 2300))

                )
        ));
        createChangeStage();
        next = new AnimatedSprite(ScaleHelper.getInstance().getXLocation(1700, 200),
                ScaleHelper.getInstance().getCenterYLoction(540, 200), BackMainRegion, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        playSound(mDownSound);
                        break;
                    case TouchEvent.ACTION_UP:
                        changePage();
                        playSound(mUpSound);
                        break;
                }
                return true;
            }
        };
        next.setScale(wScale, hScale);
        next.setRotation(-90);
        scene.attachChild(next);

        scene.registerTouchArea(next);


        chooseBK = new Sprite(ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(0, 1080), ChooseBKRegion, getVertexBufferObjectManager());
        chooseBK.setScale(wScale, hScale);
        scene.attachChild(chooseBK);
//        chooseBK.setVisible(false);
        AnimatedSprite back = new AnimatedSprite(ScaleHelper.getInstance().getXLocation(70, 200),
                ScaleHelper.getInstance().getYLocation(20, 200)
                , BackMainRegion, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        this.stopAnimation(1);
                        playSound(mDownSound);
                        break;
                    case TouchEvent.ACTION_UP:
                        this.stopAnimation(0);
                        playSound(mUpSound);
                        finish();
                        break;
                }
                return true;
            }
        };
        back.setScale(wScale, hScale);
        scene.registerTouchArea(back);
        scene.attachChild(back);
        createNum();
    }

    private void changePage() {
        if (isInNormal) {

            if (changeBK.getEntityModifierCount() != 0) {
                changeBK.clearEntityModifiers();
            }
            if (chooseBK.getEntityModifierCount() != 0) {
                chooseBK.clearEntityModifiers();
            }
            if (next.getEntityModifierCount() != 0) {
                next.clearEntityModifiers();
            }
            changeBK.registerEntityModifier(new MoveYModifier(1f, changeBK.getY(),
                    ScaleHelper.getInstance().getYLocation(0, 1080), EaseBounceOut.getInstance()));
            chooseBK.registerEntityModifier(new MoveYModifier(1f, chooseBK.getY(),
                    ScaleHelper.getInstance().getYLocation(-1080, 1080), EaseBounceOut.getInstance()));
            next.registerEntityModifier(new RotationModifier(1f, next.getRotation(), -270));
        } else {
            if (changeBK.getEntityModifierCount() != 0) {
                changeBK.clearEntityModifiers();
            }
            if (chooseBK.getEntityModifierCount() != 0) {
                chooseBK.clearEntityModifiers();
            }
            if (next.getEntityModifierCount() != 0) {
                next.clearEntityModifiers();
            }
            changeBK.registerEntityModifier(new MoveYModifier(1f, changeBK.getY(),
                    ScaleHelper.getInstance().getYLocation(1080, 1080), EaseBounceOut.getInstance()));
            chooseBK.registerEntityModifier(new MoveYModifier(1f, chooseBK.getY(),
                    ScaleHelper.getInstance().getYLocation(0, 1080), EaseBounceOut.getInstance()));
            next.registerEntityModifier(new RotationModifier(1f, next.getRotation(), -90));
        }
        isInNormal = !isInNormal;

    }

    private void createNum() {
        int x = 220;
        int y = 290;
        if (level > 8) {
            level = 8;
        }
        for (int i = 0; i < level; i++) {
            if (i == 4) {
                y = 640;
                x = 1540;
            }
            final int finalI = i;
            AnimatedSprite a = new AnimatedSprite(x, y, textureRegions.get(finalI), getVertexBufferObjectManager()) {
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    return onClick(this, pSceneTouchEvent, finalI);
                }
            };
            chooseBK.attachChild(a);
            AnimatedSprite star = new AnimatedSprite(-a.getWidth() * 0.2f, a.getHeight() * 1.2f, ChooseStarRegion, getVertexBufferObjectManager());
            a.attachChild(star);
            star.stopAnimation(Integer.parseInt(infors[i]));
            if (i < 4) {
                x += 440;
            } else if (x > 4) {
                x -= 440;
            }
            scene.registerTouchArea(a);
        }

    }

    private boolean onClick(AnimatedSprite sprite, TouchEvent pSceneTouchEvent, int i) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                sprite.stopAnimation(1);
                playSound(mDownSound);
                break;
            case TouchEvent.ACTION_UP:
                sprite.stopAnimation(0);
                Intent intent = new Intent(ChooseActivity.this, activity.get(i));
                intent.putExtra("level", i + 1);
                startActivity(intent);
                finish();
                mMusic.stop();
                playSound(mUpSound);
                break;
        }
        return true;
    }

    private void playSound(Sound sound) {
        if (SpUtil.getBoolean(this, ConstantValue.SOUND, false)) {
            sound.play();
        }
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        onLoadListener = this;
        mBeginTime = System.currentTimeMillis();
//        SpUtil.setString(this,ConstantValue.LEVEL_INFO,test);
        mHandler = new Handler();
        AnalyseJson();
        activity = new ArrayList();
        textureRegions = new ArrayList<>();
        ScaleHelper.getInstance().init(this);
        wScale = ScaleHelper.getInstance().getWidthScale();
        hScale = ScaleHelper.getInstance().getHeightScale();
        CAMERA_WIDTH = ScaleHelper.getInstance().getAppWidth();
        CAMERA_HEIGHT = ScaleHelper.getInstance().getAppHeight();
        camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new FillResolutionPolicy(), camera);
        engineOptions.getAudioOptions().setNeedsMusic(true);
// 声明程序要使用音效
        engineOptions.getAudioOptions().setNeedsSound(true);
        return engineOptions;
    }

    private void AnalyseJson() {
        String infor = SpUtil.getString(this, ConstantValue.LEVEL_INFO, null);
        if (infor != null) {
            try {
                JSONObject jsonObject = new JSONObject(infor);
                level = jsonObject.getInt("level");
                String in = jsonObject.getString("infor");
                infors = in.split(",");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFinishResources() {

    }

    @Override
    public void onFinishScene() {
        mEngine.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                Engine.EngineLock engineLock = mEngine.getEngineLock();
                engineLock.lock();
                mainScene.clearChildScene();
                mainScene.setChildScene(scene, true, true, true);
                engineLock.unlock();
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mMusic!=null&&mMusic.isPlaying()){
            mMusic.pause();
        }
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
        if(SpUtil.getBoolean(this,ConstantValue.MUSIC,false)&&mMusic!=null){
            if(!mMusic.isPlaying()){
                mMusic.resume();
            }
        }
    }
}
