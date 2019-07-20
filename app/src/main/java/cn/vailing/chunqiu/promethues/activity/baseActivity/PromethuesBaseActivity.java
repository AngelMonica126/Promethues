package cn.vailing.chunqiu.promethues.activity.baseActivity;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.andengine.util.modifier.ease.EaseStrongOut;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

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
import cn.vailing.chunqiu.promethues.bean.Human;
import cn.vailing.chunqiu.promethues.bean.Prometheus;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.is.OnGameManger;
import cn.vailing.chunqiu.promethues.is.OnLoadListener;
import cn.vailing.chunqiu.promethues.manager.BoundManager;
import cn.vailing.chunqiu.promethues.manager.CameraManager;
import cn.vailing.chunqiu.promethues.manager.FlameManager;
import cn.vailing.chunqiu.promethues.manager.ScoreManager;
import cn.vailing.chunqiu.promethues.override.MapleStoryCamera;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ConstantValue;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.SoundHelper;
import cn.vailing.chunqiu.promethues.util.SpUtil;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/8/13.
 */

public abstract class PromethuesBaseActivity extends SimpleBaseGameActivity implements OnGameManger, MenuScene.IOnMenuItemClickListener, OnLoadListener {
    protected static int CAMERA_WIDTH = 480;
    protected static int CAMERA_HEIGHT = 320;
    protected MapleStoryCamera camera;
    protected Handler mHandler;
    protected Prometheus mPrometheus;
    protected FlameManager mFlameManager;
    protected Scene mScene;
    protected Human mHuman;
    protected BoundManager boundManager;
    protected MyPhysicsWorld mPhysicsWorld;
    protected float wScale;
    protected float hScale;
    private ITextureRegion backTextureRegion;

    private ITextureRegion restartTextureRegion;

    private ITextureRegion resumeTextureRegion;


    private ITextureRegion backBKTextureRegion;

    private ITextureRegion restartBKTextureRegion;

    private ITextureRegion resumeBKTextureRegion;

    private ITextureRegion menuBKTextureRegion;

    private ITextureRegion fontTextureRegion;

    private ITextureRegion loadBKTextureRegion;
    private TextureRegion aRegion;
    private TextureRegion cRegion;
    private TiledTextureRegion stopRegion;
    private TiledTextureRegion reRegion;
    private TiledTextureRegion firebkRegion;
    private TiledTextureRegion flameNumRegion;
    private AnimatedSprite flameNum;
    private TextureRegion zeusRegion;
    private Sound mDownSound;
    private Sound mUpSound;
    private ScoreManager scoreManager;
    private Scene mainScene;
    private final int BACK = 100;
    private final int RESTART = 101;
    private final int RESUME = 102;
    private MenuScene menuScene;
    private OnLoadListener onLoadListener;
    private Sprite load;
    private long mBeginTime;
    private Rectangle menuBK;
    private int flameNumber;
    private boolean isFollow;
    private TextureRegion winBKTextureRegion;
    private List<Class> activity;

    protected Runnable mLoadScene = new Runnable() {
        @Override
        public void run() {
            if (mEngine != null) {
                loadResuorce();
                resourceInit();
                loadScene();
                sceneInit();
                createMenu();
                playMusic();
                long endTime = System.currentTimeMillis();
                if (endTime - mBeginTime < 4000) {
                    try {
                        Thread.sleep(4000 - (endTime - mBeginTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                onLoadListener.onFinishScene();
            } else {
            }
        }
    };
    private TiledTextureRegion BackMainRegion;

    private Runnable mComeTask = new Runnable() {
        @Override
        public void run() {
            if (level < activity.size()) {
                Intent intent = new Intent(getApplication(), activity.get(level));
                intent.putExtra("level", level + 1);
                mMusic.stop();
                startActivity(intent);
                finish();
            } else {
                finish();

            }

        }
    };
    private Sound click;
    private TiledTextureRegion ChooseLightingRegion;
    private TextureRegion Lighting3Region;
    private TextureRegion Lighting1Region;
    private TextureRegion Lighting2Region;
    private Sprite lighting1;
    private Sprite lighting2;
    private Sprite lighting3;
    private Sound victory;

    private void playMusic() {
        if (SpUtil.getBoolean(this, ConstantValue.MUSIC, false)) {
            mMusic.play();
        }
    }

    private CameraManager cameraManager;

    protected Runnable mRestartTask = new Runnable() {
        @Override
        public void run() {
            camera.reset();
            SoundHelper.getInstance().stop();
            mScene = new Scene();
            mScene.registerUpdateHandler(mPhysicsWorld);
            mainScene.clearChildScene();
            createNPC();
            loadScene();
            sceneInit();
            createMenu();
            if (!mMusic.isPlaying()) {
                playMusic();
            }
            mainScene.setChildScene(mScene, false, true, true);

        }
    };
    protected Runnable mBackTask = new Runnable() {
        @Override
        public void run() {
            finish();
            mMusic.stop();
        }
    };
    private MenuScene winScene;
    private TiledTextureRegion WinbackRegion;
    private Font mFont;
    private TiledTextureRegion WinStarRegion;
    private int level;
    private Music mMusic;


    public synchronized void onResumeGame() {
        if (this.mEngine != null)
            super.onResumeGame();
    }


    @Override
    protected final void onCreateResources() {
        TextureHelper.getInstance().init(mEngine, this);
        SoundHelper.getInstance().init(mEngine, this);
        TextureHelper.getInstance().initFlame();
        SoundHelper.getInstance().initFlame();
        mPhysicsWorld = new MyPhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        mPrometheus = new Prometheus(mEngine, this, null, null, null);
        mHuman = new Human(mEngine, this, null, null, null, this);
        mFlameManager = new FlameManager(mEngine, this, wScale,hScale,
                mPhysicsWorld, this);
        boundManager = new BoundManager(mEngine, this, wScale,hScale, mPhysicsWorld);

        mPrometheus.setFlameManager(mFlameManager);
        mHuman.setFlameManager(mFlameManager);
        boundManager.setFlameManager(mFlameManager);
        createNPC();
        BitmapTextureAtlas loadBKTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        loadBKTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(loadBKTexture, this, "device/SplashBK.png", 0, 0);
        loadBKTexture.load();

        BitmapTextureAtlas fontTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        fontTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(fontTexture, this, "device/Font.png", 0, 0);
        fontTexture.load();
        BitmapTextureAtlas zeusTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        zeusRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(zeusTexture, this, "device/Zeus.png", 0, 0);
        zeusTexture.load();
    }

    private void createNPC() {
        mPrometheus = new Prometheus(mEngine, this, null, null, null);
        mHuman = new Human(mEngine, this, null, null, null, this);
        mFlameManager = new FlameManager(mEngine, this, wScale, hScale, mPhysicsWorld, this);
        boundManager = new BoundManager(mEngine, this, wScale, hScale, mPhysicsWorld);

        mPrometheus.setFlameManager(mFlameManager);
        mHuman.setFlameManager(mFlameManager);
        boundManager.setFlameManager(mFlameManager);
        scoreManager = new ScoreManager(mEngine, this, wScale, hScale, mPhysicsWorld);
    }

    private void loadResuorce() {
        BitmapTextureAtlas backTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1024, 420, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        backTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backTexture, this, "device/back.png", 0, 0);
        backTexture.load();

        BitmapTextureAtlas restartTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 506, 420, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        restartTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(restartTexture, this, "device/re.png", 0, 0);
        restartTexture.load();

        BitmapTextureAtlas resumeTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 610, 480, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        resumeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(resumeTexture, this, "device/resume.png", 0, 0);
        resumeTexture.load();


        BitmapTextureAtlas backBKTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        backBKTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backBKTexture, this, "device/back_bk.png", 0, 0);
        backBKTexture.load();

        BitmapTextureAtlas restartBKTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        restartBKTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(restartBKTexture, this, "device/restart_bk.png", 0, 0);
        restartBKTexture.load();

        BitmapTextureAtlas resumeBKTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        resumeBKTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(resumeBKTexture, this, "device/resume_bk.png", 0, 0);
        resumeBKTexture.load();

        BitmapTextureAtlas menuBKTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        menuBKTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuBKTexture, this, "device/menu_bk.png", 0, 0);
        menuBKTexture.load();

        BitmapTextureAtlas WinBKTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1107, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        winBKTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(WinBKTexture, this, "device/Win.png", 0, 0);
        WinBKTexture.load();


        BitmapTextureAtlas aTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 400, 400, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        aRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(aTexture, this, "device/srBK.png", 0, 0);
        aTexture.load();

        BitmapTextureAtlas cTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 400, 400, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        cRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(cTexture, this, "device/fBK.png", 0, 0);
        cTexture.load();

        BitmapTextureAtlas stopTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 400, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        stopRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(stopTexture, this, "device/Stop.png", 0, 0, 2, 1);
        stopTexture.load();

        BitmapTextureAtlas reTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 200, 100, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        reRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(reTexture, this, "device/Restart.png", 0, 0, 2, 1);
        reTexture.load();

        BitmapTextureAtlas WinbackTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 200, 100, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        WinbackRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(WinbackTexture, this, "device/WinBack.png", 0, 0, 2, 1);
        WinbackTexture.load();

        BitmapTextureAtlas BackMainTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 400, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BackMainRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(BackMainTexture, this, "device/BackMain.png", 0, 0, 2, 1);
        BackMainTexture.load();

        BitmapTextureAtlas firebkTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 960, 1000, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        firebkRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(firebkTexture, this, "device/FireBk.png", 0, 0, 6, 5);
        firebkTexture.load();

        BitmapTextureAtlas flameNumTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 534, 50, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        flameNumRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(flameNumTexture, this, "device/FlameNum.png", 0, 0, 6, 1);
        flameNumTexture.load();

        BitmapTextureAtlas WinStarTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 300, 400, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        WinStarRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(WinStarTexture, this, "device/WinStar.png", 0, 0, 1, 4);
        WinStarTexture.load();


        BitmapTextureAtlas Lighting1 = new BitmapTextureAtlas(mEngine.getTextureManager(), 666, 1025, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Lighting1Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(Lighting1, this, "device/Lighting1.png", 0, 0);
        Lighting1.load();

        BitmapTextureAtlas Lighting2 = new BitmapTextureAtlas(mEngine.getTextureManager(), 641, 641, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Lighting2Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(Lighting2, this, "device/Lighting2.png", 0, 0);
        Lighting2.load();

        BitmapTextureAtlas Lighting3 = new BitmapTextureAtlas(mEngine.getTextureManager(), 622, 684, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Lighting3Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(Lighting3, this, "device/Lighting3.png", 0, 0);
        Lighting3.load();

        this.mFont = FontFactory.createFromAsset(mEngine.getFontManager(), mEngine.getTextureManager(), 512, 512,
                TextureOptions.BILINEAR, this.getAssets(), "font/font.ttf", 120, true, android.graphics.Color.BLACK);
        this.mFont.load();
        loadSound();
    }

    private void loadSound() {
        try {
            this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "sound/background.mp3");
            this.mMusic.setLooping(true);
            mDownSound = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "sound/buttonDown.ogg");
            mUpSound = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "sound/buttonUp.ogg");
            click = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "sound/click.ogg");
            victory = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "sound/victory.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void resourceInit();

    @Override
    protected final Scene onCreateScene() {
        mainScene = new Scene();

        Sprite zeus = new Sprite(ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(0, 1080), zeusRegion, getVertexBufferObjectManager());
        zeus.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());
        mainScene.setBackground(new SpriteBackground(zeus));


        mScene = new Scene();
        load = new Sprite(ScaleHelper.getInstance().getXLocation(0, 1920),
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
        initData();
        mScene.registerUpdateHandler(mPhysicsWorld);
        return mainScene;
    }

    private void initData() {
        mBeginTime = System.currentTimeMillis();
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
    }

    private void loadScene() {
        Sprite zeus = new Sprite(ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(0, 1080), zeusRegion, getVertexBufferObjectManager());
        zeus.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());
        mScene.setBackground(new SpriteBackground(zeus));

        boundManager.createBound(mScene, ScaleHelper.getInstance().getAppWidth(), ScaleHelper.getInstance().getAppHeight());


        mEngine.registerUpdateHandler(new FPSLogger());
        mScene.setTouchAreaBindingOnActionMoveEnabled(true);
        mScene.setTouchAreaBindingOnActionDownEnabled(true);
    }

    private void createMenu() {
        menuBK = new Rectangle(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, getVertexBufferObjectManager());
        menuBK.setColor(0, 0, 0, 0);
        scoreManager.createScore(menuBK, this);

        Sprite a = new Sprite(ScaleHelper.getInstance().getXLocation(1520, 400),
                ScaleHelper.getInstance().getYLocation(0, 400), aRegion, mEngine.getVertexBufferObjectManager());
        a.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());
        menuBK.attachChild(a);

        AnimatedSprite stop = new AnimatedSprite(a.getWidth() * 0.75f, a.getHeight() * 0.2f, stopRegion, mEngine.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        playSound(mDownSound);
//                        mDownSound.play();
                        this.stopAnimation(1);
                        break;
                    case TouchEvent.ACTION_UP:
//                        mUpSound.play();
                        playSound(mUpSound);
                        if (mScene.hasChildScene()) {
                            mScene.clearChildScene();
                        } else {
                            createMenuScene();
                            mScene.setChildScene(menuScene, false, true, true);
                        }
                        this.stopAnimation(0);
                        break;
                }
                return true;
            }
        };
        a.attachChild(stop);

        AnimatedSprite restart = new AnimatedSprite(a.getWidth() * 0.43f, -a.getHeight() * 0.00f, reRegion, mEngine.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        playSound(mDownSound);
                        this.stopAnimation(1);
                        break;
                    case TouchEvent.ACTION_UP:
                        mHandler.postDelayed(mRestartTask, 0);
                        playSound(mUpSound);
                        this.stopAnimation(0);
                        break;
                }
                return true;
            }
        };
        a.attachChild(restart);

        Sprite c = new Sprite(ScaleHelper.getInstance().getXLocation(1620, 300),
                ScaleHelper.getInstance().getYLocation(780, 300), cRegion, mEngine.getVertexBufferObjectManager());
        c.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());
        menuBK.attachChild(c);
        AnimatedSprite fire = new AnimatedSprite(c.getWidth() * 0.45f, c.getHeight() * 0.35f, firebkRegion, mEngine.getVertexBufferObjectManager());
        c.attachChild(fire);
        fire.animate(60);

        flameNum = new AnimatedSprite(fire.getWidth() * 0.25f, fire.getHeight() * 0.6f, flameNumRegion, mEngine.getVertexBufferObjectManager());
        fire.attachChild(flameNum);
        mScene.registerTouchArea(stop);
        mScene.registerTouchArea(restart);
        flameNum.stopAnimation(flameNumber);
        mScene.attachChild(menuBK);
        if (isFollow) {
            cameraManager.setFlameManager(mFlameManager);
            cameraManager.setMoveCamera(mScene, camera);

            menuBK.registerUpdateHandler(new IUpdateHandler() {
                @Override
                public void onUpdate(float v) {
                    menuBK.setX(camera.getCenterX() - menuBK.getWidth() / 2);
                    menuBK.setY(camera.getCenterY() - menuBK.getHeight() / 2);
                }

                @Override
                public void reset() {

                }
            });
        }
    }


    @Override
    public EngineOptions onCreateEngineOptions() {

        activity = new ArrayList<>();
        Intent intent = getIntent();
        level = intent.getIntExtra("level", 1);
        onLoadListener = this;
        mHandler = new Handler();
        ScaleHelper.getInstance().init(this);
        wScale = ScaleHelper.getInstance().getWidthScale();
        hScale = ScaleHelper.getInstance().getHeightScale();
        CAMERA_WIDTH = ScaleHelper.getInstance().getAppWidth();
        CAMERA_HEIGHT = ScaleHelper.getInstance().getAppHeight();
        camera = new MapleStoryCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new FillResolutionPolicy(), camera);
        engineOptions.getAudioOptions().setNeedsMusic(true);
//// 声明程序要使用音效
        engineOptions.getAudioOptions().setNeedsSound(true);
        return engineOptions;
    }

    private void playSound(Sound sound) {
        if (SpUtil.getBoolean(this, ConstantValue.SOUND, false)) {
            sound.play();
        }
    }

    private void createWinScene() {
        playSound(victory);
        mMusic.pause();
        SoundHelper.getInstance().stop();
        int num = scoreManager.getScore();
        long time = scoreManager.getTime();
        wirteScore(num, time);
        scoreManager.stop();

        final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

        winScene = new MenuScene(camera);
        Sprite winBk = new Sprite(ScaleHelper.getInstance().getCenterXLoction(960, 1107),
                ScaleHelper.getInstance().getCenterYLoction(-540, 1080), winBKTextureRegion, getVertexBufferObjectManager());
        winBk.setScale(wScale, hScale);
        winBk.registerEntityModifier(new MoveYModifier(1f, ScaleHelper.getInstance().getCenterYLoction(-540, 1080),
                ScaleHelper.getInstance().getCenterYLoction(540, 1080),EaseBounceOut.getInstance()));
        AnimatedSprite restart = new AnimatedSprite(winBk.getWidth() * 0.32f, winBk.getHeight() * 0.855f, reRegion,
                getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        this.stopAnimation(1);
                        playSound(mDownSound);
                        break;
                    case TouchEvent.ACTION_UP:
                        this.stopAnimation(0);
                        mHandler.postDelayed(PromethuesBaseActivity.this.mRestartTask, 0);
                        playSound(mUpSound);
                        break;
                }
                return true;
            }
        };
        winBk.attachChild(restart);
        AnimatedSprite comeBack = new AnimatedSprite(winBk.getWidth() * 0.575f, winBk.getHeight() * 0.855f,
                WinbackRegion, getVertexBufferObjectManager()) {
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
                        mHandler.postDelayed(mComeTask, 0);
                        break;
                }
                return true;
            }
        };
        winBk.attachChild(comeBack);
        AnimatedSprite backMain = new AnimatedSprite(-winBk.getWidth() * 0.3f, 68
                , BackMainRegion, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        playSound(mDownSound);
                        this.stopAnimation(1);
                        break;
                    case TouchEvent.ACTION_UP:
                        playSound(mUpSound);
                        this.stopAnimation(0);
                        mHandler.postDelayed(mBackTask, 0);
                        break;
                }
                return true;
            }
        };
        winBk.attachChild(backMain);
        final Text text = new Text(winBk.getWidth() * 0.2f, winBk.getHeight() * 0.57f, mFont, "00:00", getVertexBufferObjectManager());
        winBk.attachChild(text);


        text.setText(sdf.format(time * 1000));

        AnimatedSprite star = new AnimatedSprite(winBk.getWidth() * 0.55f, winBk.getHeight() * 0.6f, WinStarRegion, getVertexBufferObjectManager());
        winBk.attachChild(star);
        star.stopAnimation(num);

        winScene.attachChild(winBk);
        winScene.buildAnimations();
        winScene.setBackgroundEnabled(false);
        winScene.setOnMenuItemClickListener(this);
        winScene.registerTouchArea(restart);
        winScene.registerTouchArea(comeBack);
        winScene.registerTouchArea(backMain);
    }

    private void wirteScore(int num, long time) {
        String infor = SpUtil.getString(this, ConstantValue.LEVEL_INFO, null);
        if (infor != null) {
            try {
                JSONObject jsonObject = new JSONObject(infor);
                int level = jsonObject.getInt("level");
                String in = jsonObject.getString("infor");
                String[] infors = in.split(",");
                if (level <= infors.length + 1) {
                    if (this.level >= level && this.level < 10) {
                        level = this.level + 1;
                    }
                    int bnum = Integer.parseInt(infors[this.level - 1]);
                    if (bnum <= num) {
                        infors[this.level - 1] = num + "";
                    }
                    StringBuilder score = new StringBuilder();
                    for (int i = 0; i < infors.length; i++) {
                        score.append(infors[i]);
                        if (i != infors.length - 1) {
                            score.append(",");
                        }
                    }
                    JSONObject w = new JSONObject();
                    w.put("level", level);
                    w.put("infor", score.toString());
                    SpUtil.setString(this, ConstantValue.LEVEL_INFO, w.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void createMenuScene() {
        if (mMusic.isPlaying()) {
            mMusic.pause();
        }

        menuScene = new MenuScene(camera);
        final SpriteMenuItem resumeItem = new SpriteMenuItem(RESUME, resumeTextureRegion, getVertexBufferObjectManager());

        resumeItem.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());
        resumeItem.registerEntityModifier(new ParallelEntityModifier(
                new MoveModifier(
                        0.8f,
                        ScaleHelper.getInstance().getXLocation(0, 610),
                        ScaleHelper.getInstance().getXLocation(0, 610),
                        ScaleHelper.getInstance().getYLocation(200, 480),
                        ScaleHelper.getInstance().getYLocation(200, 480)),
                new AlphaModifier(0.8f, 0f, 1f, EaseStrongOut.getInstance())
        ));
        resumeItem.setVisible(false);
        resumeItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);


        final SpriteMenuItem backItem = new SpriteMenuItem(BACK, backTextureRegion, getVertexBufferObjectManager());
        backItem.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());
        backItem.registerEntityModifier(new ParallelEntityModifier(
//                new AlphaModifier(0.8f, 0f, 1f, EaseStrongOut.getInstance()),
                new MoveModifier(
                        0.8f,
                        ScaleHelper.getInstance().getXLocation(500, 1024),
                        ScaleHelper.getInstance().getXLocation(500, 1024),
                        ScaleHelper.getInstance().getYLocation(640, 420),
                        ScaleHelper.getInstance().getYLocation(640, 420)),
                new AlphaModifier(0.8f, 0f, 1f, EaseStrongOut.getInstance())
        ));
        backItem.setVisible(false);
        backItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);


        final SpriteMenuItem restartItem = new SpriteMenuItem(RESTART, restartTextureRegion, getVertexBufferObjectManager());
        restartItem.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());
        restartItem.registerEntityModifier(new ParallelEntityModifier(
//                new AlphaModifier(0.8f, 0f, 1f, EaseStrongOut.getInstance()),
                new MoveModifier(
                        0.8f,
                        ScaleHelper.getInstance().getXLocation(1200, 506),
                        ScaleHelper.getInstance().getXLocation(1200, 506),
                        ScaleHelper.getInstance().getYLocation(100, 420),
                        ScaleHelper.getInstance().getYLocation(100, 420)),
                new AlphaModifier(0.8f, 0f, 1f, EaseStrongOut.getInstance())
        ));
        restartItem.setVisible(false);
        restartItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        final Sprite resumeBKItem = new Sprite(ScaleHelper.getInstance().getXLocation(-1920, 1920),
                ScaleHelper.getInstance().getYLocation(0, 1080), resumeBKTextureRegion, getVertexBufferObjectManager());

        resumeBKItem.registerEntityModifier(new MoveModifier(
                1.2f,
                ScaleHelper.getInstance().getXLocation(-1920, 1920),
                ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(0, 1080),
                ScaleHelper.getInstance().getYLocation(0, 1080),
                EaseBounceOut.getInstance()));
        resumeBKItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        resumeBKItem.setScale(wScale, hScale);


        lighting1 = new Sprite(0, 0, Lighting1Region, getVertexBufferObjectManager());
        resumeBKItem.attachChild(lighting1);


        final Sprite backBKItem = new Sprite(ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(1080, 1080),
                backBKTextureRegion, getVertexBufferObjectManager());
        backBKItem.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());
        backBKItem.registerEntityModifier(new MoveModifier(
                1.2f,
                ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(1080, 1080),
                ScaleHelper.getInstance().getYLocation(0, 1080),
                EaseBounceOut.getInstance()));
        backBKItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        lighting2 = new Sprite(backBKItem.getWidth() * 0.3f, backBKItem.getHeight() * 0.4f, Lighting2Region, getVertexBufferObjectManager());
        backBKItem.attachChild(lighting2);


        final Sprite restartBKItem = new Sprite(ScaleHelper.getInstance().getXLocation(1920, 1920),
                ScaleHelper.getInstance().getYLocation(0, 1080),
                restartBKTextureRegion, getVertexBufferObjectManager());
        restartBKItem.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());
        restartBKItem.registerEntityModifier(new MoveModifier(
                1.2f,
                ScaleHelper.getInstance().getXLocation(1920, 1920),
                ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(0, 1080),
                ScaleHelper.getInstance().getYLocation(0, 1080), new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {

            }

            @Override
            public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                resumeItem.setVisible(true);
                backItem.setVisible(true);
                restartItem.setVisible(true);
            }
        },
                EaseBounceOut.getInstance()));
        restartBKItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        lighting3 = new Sprite(backBKItem.getWidth() * 0.65f, 0, Lighting3Region, getVertexBufferObjectManager());
        restartBKItem.attachChild(lighting3);
        lighting1.setVisible(false);
        lighting2.setVisible(false);
        lighting3.setVisible(false);
        menuScene.attachChild(backBKItem);
        menuScene.attachChild(resumeBKItem);
        menuScene.attachChild(restartBKItem);
        menuScene.addMenuItem(backItem);
        menuScene.addMenuItem(resumeItem);
        menuScene.addMenuItem(restartItem);
        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);
        menuScene.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
        if ((pKeyCode == KeyEvent.KEYCODE_MENU || pKeyCode == KeyEvent.KEYCODE_BACK) && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (this.mScene.hasChildScene()) {
                mScene.clearChildScene();
            } else {
                createMenuScene();
                this.mScene.setChildScene(this.menuScene, false, true, true);

            }
            return true;
        } else {
            return super.onKeyDown(pKeyCode, pEvent);
        }
    }

    @Override
    public boolean onMenuItemClicked(MenuScene menuScene, final IMenuItem iMenuItem, float v, float v1) {

        playSound(click);
        menuScene.registerEntityModifier(new AlphaModifier(0.5f, 1, 1, new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {
                switch (iMenuItem.getID()) {
                    case BACK:
                        lighting2.setVisible(true);
                        break;
                    case RESTART:
                        lighting3.setVisible(true);
                        break;
                    case RESUME:
                        lighting1.setVisible(true);
                        break;
                }

            }

            @Override
            public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                switch (iMenuItem.getID()) {
                    case BACK:
                        mHandler.postDelayed(mBackTask, 0);
                        break;
                    case RESTART:
                        mHandler.postDelayed(mRestartTask, 0);
                        break;
                    case RESUME:
                        playMusic();
                        mScene.clearChildScene();
                        break;
                }
            }
        }));

        return true;
    }

    //type 上下左右 1234
    protected void setFollowFlame(int type, float bx, float by, float ex, float ey) {
        camera.setType(type);
        isFollow = true;
        cameraManager = new CameraManager(mEngine, this, wScale, hScale, null);
        cameraManager.setBound(bx, by, ex, ey);
    }

    @Override
    public void gameOver() {
        mHandler.postDelayed(mRestartTask, 0);
    }

    @Override
    public void gameVictory() {
        createWinScene();
        this.mScene.setChildScene(this.winScene, false, true, true);

    }

    public abstract void sceneInit();

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
                mainScene.setChildScene(mScene, true, true, true);
                engineLock.unlock();
            }
        });
    }

    @Override
    public void setFlameNum(int index) {
        if (flameNum != null) {
            flameNum.stopAnimation(index - 1);
        } else {
            flameNumber = index - 1;
        }

    }

    @Override
    public void addStart() {
        scoreManager.addStar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoundHelper.getInstance().stop();
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
