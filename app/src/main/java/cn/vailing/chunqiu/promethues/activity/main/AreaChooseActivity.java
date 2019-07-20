package cn.vailing.chunqiu.promethues.activity.main;

import android.os.Handler;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.io.IOException;

import cn.vailing.chunqiu.promethues.is.OnLoadListener;
import cn.vailing.chunqiu.promethues.util.ConstantValue;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.SpUtil;

/**
 * Created by dream on 2017/9/28.
 */

public class AreaChooseActivity extends SimpleBaseGameActivity implements OnLoadListener {
    private static int CAMERA_WIDTH = 480;
    private static int CAMERA_HEIGHT = 320;
    private Camera camera;
    private float wScale;
    private float hScale;
    private TextureRegion loadBKTextureRegion;
    private TextureRegion fontTextureRegion;
    private Sound mDownSound;
    private Sound mUpSound;
    private Music mMusic;
    private Scene mainScene;
    private Handler mHandler;
    private long mBeginTime;
    private OnLoadListener onLoadListener;
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
    private TextureRegion zeusBKTextureRegion;
    private TextureRegion zeusFontTextureRegion;
    private TextureRegion zeusMarkTextureRegion;
    private Scene scene;

    private void loadScene() {
        scene = new Scene();
        Sprite zeusBK = new Sprite(ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(0, 368), zeusBKTextureRegion, getVertexBufferObjectManager());
        zeusBK.setScale(wScale,hScale);
        Sprite zeusFont = new Sprite(0,0,zeusFontTextureRegion,getVertexBufferObjectManager());
        Sprite zeusMark = new Sprite(0,0,zeusMarkTextureRegion,getVertexBufferObjectManager());
        zeusBK.attachChild(zeusFont);
        zeusBK.attachChild(zeusMark);

        scene.attachChild(zeusBK);

    }

    private void onLoadResources() {
        BitmapTextureAtlas zeusBKTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 368, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        zeusBKTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(zeusBKTexture, this, "device/zeusBK.png", 0, 0);
        zeusBKTexture.load();

        BitmapTextureAtlas zeusFontTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 735, 288, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        zeusFontTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(zeusFontTexture, this, "device/zeusFont.png", 0, 0);
        zeusFontTexture.load();

        BitmapTextureAtlas zeusMarkTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 371, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        zeusMarkTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(zeusMarkTexture, this, "device/zeusMark.png", 0, 0);
        zeusMarkTexture.load();

//        BitmapTextureAtlas zeusBKTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 368, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//        zeusBKTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(zeusBKTexture, this, "device/zeusBK.png", 0, 0);
//        zeusBKTexture.load();
//
//        BitmapTextureAtlas zeusFontTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 735, 288, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//        zeusFontTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(zeusFontTexture, this, "device/zeusFont.png", 0, 0);
//        zeusFontTexture.load();
//
//        BitmapTextureAtlas zeusMarkTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 371, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//        zeusMarkTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(zeusMarkTexture, this, "device/zeusMark.png", 0, 0);
//        zeusMarkTexture.load();

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

    @Override
    public EngineOptions onCreateEngineOptions() {
        mBeginTime = System.currentTimeMillis();
        mHandler = new Handler();
        onLoadListener = this;
        ScaleHelper.getInstance().init(this);
        wScale = ScaleHelper.getInstance().getWidthScale();
        hScale = ScaleHelper.getInstance().getHeightScale();
        CAMERA_WIDTH = ScaleHelper.getInstance().getAppWidth();
        CAMERA_HEIGHT = ScaleHelper.getInstance().getAppHeight();
        camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new FillResolutionPolicy(), camera);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        engineOptions.getAudioOptions().setNeedsSound(true);
        return engineOptions;
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
}
