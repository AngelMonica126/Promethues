package cn.vailing.chunqiu.promethues.activity.main;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
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
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.modifier.IModifier;

import java.io.IOException;

import cn.vailing.chunqiu.promethues.is.OnLoadListener;
import cn.vailing.chunqiu.promethues.util.ConstantValue;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.SpUtil;

/**
 * Created by dream on 2017/9/27.
 */

public class MainActivity extends SimpleBaseGameActivity implements OnLoadListener {
    private static int CAMERA_WIDTH = 480;
    private static int CAMERA_HEIGHT = 320;
    private Camera camera;
    private TextureRegion mainBKRegion;
    private float wScale;
    private float hScale;
    private TiledTextureRegion mainFlame;
    private AnimatedSprite flame;
    private TextureRegion MainRectRegion;
    private TextureRegion PromethuesTextureRegion;
    private TiledTextureRegion BeginRegion;
    private TiledTextureRegion SettingRegion;
    private TiledTextureRegion ExitRegion;
    private Scene scene;
    private TextureRegion PromethuesFontRegion;
    private Sound mDownSound;
    private Sound mUpSound;
    private Music mMusic;
    private Rectangle mainRectangle;
    private boolean isInSetting;
    private Rectangle settingRect;
    private TextureRegion SettingMenuRegion;
    private TiledTextureRegion BackMainRegion;
    private TextureRegion ChooseRegion;
    private Rectangle musicButton;
    private Rectangle soundButton;
    private OnLoadListener onLoadListener;
    private long mBeginTime;
    protected Runnable mLoadScene = new Runnable() {
        @Override
        public void run() {
            if (mEngine != null) {
                loadRecourse();
                loadScene();
                long endTime = System.currentTimeMillis();
                if (endTime - mBeginTime < 2000) {
                    try {
                        Thread.sleep(2000 - (endTime - mBeginTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                onLoadListener.onFinishScene();
            } else {
            }
        }
    };
    private Scene mainScene;
    private Handler mHander;
    private TextureRegion dayuRegion;

    @Override
    public synchronized void onResumeGame() {
        if (this.mEngine != null)
            super.onResumeGame();

    }

    @Override
    protected void onCreateResources() {
        BitmapTextureAtlas dayuTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 550, 550, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        dayuRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(dayuTexture, this, "device/dayu.png", 0, 0);
        dayuTexture.load();
    }

    private void loadRecourse() {
        BitmapTextureAtlas ChooseTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 200, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        ChooseRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(ChooseTexture, this, "device/Choose.png", 0, 0);
        ChooseTexture.load();


        BitmapTextureAtlas SettingMenuTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1035, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        SettingMenuRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(SettingMenuTexture, this, "device/SettingMenu.png", 0, 0);
        SettingMenuTexture.load();

        BitmapTextureAtlas PromethuesFontTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 2300, 2300, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        PromethuesFontRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(PromethuesFontTexture, this, "device/PromethuesFont.png", 0, 0);
        PromethuesFontTexture.load();

        BitmapTextureAtlas mainBKTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mainBKRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainBKTexture, this, "device/mainBK.png", 0, 0);
        mainBKTexture.load();

        BitmapTextureAtlas mainFlameTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 2500, 3600, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mainFlame = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainFlameTexture, this, "device/MainFlame.png", 0, 0, 5, 4);
        mainFlameTexture.load();

        BitmapTextureAtlas MainRectTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        MainRectRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(MainRectTexture, this, "device/MainRect.png", 0, 0);
        MainRectTexture.load();

        BitmapTextureAtlas PromethuesTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 1920, 1080, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        PromethuesTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(PromethuesTexture, this, "device/Promethues.png", 0, 0);
        PromethuesTexture.load();

        BitmapTextureAtlas BeginTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 600, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BeginRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(BeginTexture, this, "device/Begin.png", 0, 0, 2, 1);
        BeginTexture.load();

        BitmapTextureAtlas SettingTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 600, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        SettingRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(SettingTexture, this, "device/Setting.png", 0, 0, 2, 1);
        SettingTexture.load();

        BitmapTextureAtlas ExitTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 680, 230, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        ExitRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(ExitTexture, this, "device/Exit.png", 0, 0, 2, 1);
        ExitTexture.load();

        BitmapTextureAtlas BackMainTexture = new BitmapTextureAtlas(mEngine.getTextureManager(), 400, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BackMainRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(BackMainTexture, this, "device/BackMain.png", 0, 0, 2, 1);
        BackMainTexture.load();
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
        mainScene = new Scene();
        mainScene.setBackground(new Background(1, 1, 1));
        final Sprite fish = new Sprite(ScaleHelper.getInstance().getCenterXLoction(960, 550),
                ScaleHelper.getInstance().getCenterYLoction(540, 550), dayuRegion, getVertexBufferObjectManager());
        fish.setScale(wScale, hScale);
        mainScene.attachChild(fish);
//       mainScene.registerEntityModifier(new AlphaModifier(2.1f, 1, 0, new IEntityModifier.IEntityModifierListener() {
//           @Override
//           public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {
//
//           }
//
//           @Override
//           public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
//                mainScene.setAlpha(1F);
//           }
//       }));
//        fish.registerEntityModifier(new AlphaModifier(1f, 1f, 0, new IEntityModifier.IEntityModifierListener() {
//            @Override
//            public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {
//
//            }
//
//            @Override
//            public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
//                Log.e("vailing",iEntity.getAlpha()+"  ");
//            }
//        }));
        mHander.postDelayed(this.mLoadScene, 0);

        return mainScene;
    }

    private void loadScene() {
        scene = new Scene();
        Sprite mainBKSprite = new Sprite(ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(0, 1080), mainBKRegion, getVertexBufferObjectManager());
        mainBKSprite.setScale(wScale, hScale);
        scene.setBackground(new SpriteBackground(mainBKSprite));

        Sprite promethuesFont = new Sprite(ScaleHelper.getInstance().getXLocation(-190, 2300),
                ScaleHelper.getInstance().getYLocation(-610, 2300), PromethuesFontRegion, getVertexBufferObjectManager());
        promethuesFont.setScale(wScale, hScale);
        scene.attachChild(promethuesFont);


        flame = new AnimatedSprite(ScaleHelper.getInstance().getCenterXLoction(960, mainFlame.getWidth()),
                ScaleHelper.getInstance().getCenterYLoction(540, mainFlame.getHeight()), mainFlame, getVertexBufferObjectManager());
        flame.setScale(wScale, hScale);
        flame.animate(80);
        scene.attachChild(flame);

        mainRectangle = new Rectangle(0, 0, 1, 1, getVertexBufferObjectManager());
        mainRectangle.setColor(0, 0, 0, 0);
        scene.attachChild(mainRectangle);

        settingRect = new Rectangle(CAMERA_WIDTH, 0, 1, 1, getVertexBufferObjectManager());
        settingRect.setColor(0, 0, 0, 0);
        scene.attachChild(settingRect);

        Sprite promethues = new Sprite(ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(0, 705), PromethuesTextureRegion, getVertexBufferObjectManager());
        mainRectangle.attachChild(promethues);
        promethues.setScale(wScale, hScale);

        Sprite mainRect = new Sprite(ScaleHelper.getInstance().getXLocation(0, 1920),
                ScaleHelper.getInstance().getYLocation(584, 584), MainRectRegion, getVertexBufferObjectManager());
        mainRectangle.attachChild(mainRect);
        mainRect.setScale(wScale, hScale);

        createMainMenu(mainRectangle);
        if (SpUtil.getBoolean(this, ConstantValue.MUSIC, false)) {
            mMusic.play();
        }
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
    }

    private void createSettingMenu() {
        Sprite settingMenu = new Sprite(ScaleHelper.getInstance().getCenterXLoction(960, 1035),
                ScaleHelper.getInstance().getCenterYLoction(540, 1080), SettingMenuRegion, getVertexBufferObjectManager());
        settingMenu.setScale(wScale, hScale);
        settingRect.attachChild(settingMenu);

        AnimatedSprite backMain = new AnimatedSprite(ScaleHelper.getInstance().getXLocation(68, 200),
                ScaleHelper.getInstance().getYLocation(68, 200), BackMainRegion, getVertexBufferObjectManager()) {
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
                        back();
                        break;
                }
                return true;
            }
        };
        backMain.setScale(wScale, hScale);
        settingRect.attachChild(backMain);

        soundButton = new Rectangle(ScaleHelper.getInstance().getXLocation(694, 490),
                ScaleHelper.getInstance().getYLocation(394, 160), 490, 160, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        playSound(mDownSound);
                        break;
                    case TouchEvent.ACTION_UP:
                        setChoose(this, 1);
                        playSound(mUpSound);
                        break;
                }
                return true;
            }
        };
        soundButton.setScale(wScale, hScale);
        soundButton.setColor(0, 0, 0, 0);
        settingRect.attachChild(soundButton);

        musicButton = new Rectangle(ScaleHelper.getInstance().getXLocation(694, 490),
                ScaleHelper.getInstance().getYLocation(554, 160), 490, 160, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        playSound(mDownSound);
                        break;
                    case TouchEvent.ACTION_UP:
                        setChoose(this, 2);
                        playSound(mUpSound);
                        break;
                }
                return true;
            }
        };
        musicButton.setColor(0, 0, 0, 0);
        musicButton.setScale(wScale, hScale);
        settingRect.attachChild(musicButton);
        loadSoundSetting();
        scene.registerTouchArea(soundButton);
        scene.registerTouchArea(musicButton);
        scene.registerTouchArea(backMain);

    }

    private void back() {
        settingRect.registerEntityModifier(new MoveXModifier(1f, 0, CAMERA_WIDTH));
        mainRectangle.registerEntityModifier(new MoveXModifier(1f, ScaleHelper.getInstance().getXLocation(-1920, 0), 0));
    }

    private void loadSoundSetting() {
//        setChoose(soundButton,1);
//        setChoose(soundButton,1);
//        setChoose(musicButton,2);
        if (SpUtil.getBoolean(this, ConstantValue.SOUND, false)) {
            Sprite choose = new Sprite(soundButton.getWidth() * 0.6f, 0, ChooseRegion, getVertexBufferObjectManager());
            soundButton.attachChild(choose);
        }
        if (SpUtil.getBoolean(this, ConstantValue.MUSIC, false)) {
            Sprite choose = new Sprite(musicButton.getWidth() * 0.6f, 0, ChooseRegion, getVertexBufferObjectManager());
            musicButton.attachChild(choose);
        }
//        soundButton.detachChildren();
    }

    private void setChoose(Rectangle rectangle, int type) {
        //1:sound 2:music
        if (type == 1) {
            boolean isChoose = SpUtil.getBoolean(this, ConstantValue.SOUND, false);
            isChoose = !isChoose;
            if (isChoose) {
                Sprite choose = new Sprite(rectangle.getWidth() * 0.6f, 0, ChooseRegion, getVertexBufferObjectManager());
                soundButton.attachChild(choose);
            } else {

                if (soundButton.getChildCount() != 0) {
                    soundButton.detachChildren();
                }
            }
            SpUtil.setBoolean(this, ConstantValue.SOUND, isChoose);

        } else {
            boolean isChoose = SpUtil.getBoolean(this, ConstantValue.MUSIC, false);
            isChoose = !isChoose;
            if (isChoose) {
                Sprite choose = new Sprite(rectangle.getWidth() * 0.6f, 0, ChooseRegion, getVertexBufferObjectManager());
                musicButton.attachChild(choose);
                if (!mMusic.isPlaying()) {
                    mMusic.play();
                }
            } else {

                if (musicButton.getChildCount() != 0) {
                    musicButton.detachChildren();
                }
                if (mMusic.isPlaying()) {
                    mMusic.pause();
                }
            }
            SpUtil.setBoolean(this, ConstantValue.MUSIC, isChoose);
        }
    }

    private void createMainMenu(Rectangle rectangle) {
        final AnimatedSprite begin = new AnimatedSprite(ScaleHelper.getInstance().getCenterXLoction(370, 300),
                ScaleHelper.getInstance().getCenterYLoction(790, 200), BeginRegion, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        playSound(mDownSound);
                        this.stopAnimation(0);
                        break;
                    case TouchEvent.ACTION_UP:
                        this.stopAnimation(1);
                        begin();
                        playSound(mUpSound);
                        break;
                }
                return true;
            }
        };
        begin.setScale(wScale, hScale);
        begin.stopAnimation(1);
        rectangle.attachChild(begin);
        scene.registerTouchArea(begin);

        AnimatedSprite setting = new AnimatedSprite(ScaleHelper.getInstance().getCenterXLoction(950, 300),
                ScaleHelper.getInstance().getCenterYLoction(790, 200), SettingRegion, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        playSound(mDownSound);
                        this.stopAnimation(1);
                        break;
                    case TouchEvent.ACTION_UP:
                        this.stopAnimation(0);
                        setting();
                        playSound(mUpSound);
                        break;
                }
                return true;
            }
        };
        setting.stopAnimation(0);
        setting.setScale(wScale, hScale);
        rectangle.attachChild(setting);
        scene.registerTouchArea(setting);

        AnimatedSprite exit = new AnimatedSprite(ScaleHelper.getInstance().getCenterXLoction(1532, 340),
                ScaleHelper.getInstance().getCenterYLoction(780, 230), ExitRegion, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        playSound(mDownSound);
                        this.stopAnimation(1);
                        break;
                    case TouchEvent.ACTION_UP:
                        this.stopAnimation(0);
                        exit();
                        playSound(mUpSound);
                        break;
                }
                return true;
            }
        };
        exit.stopAnimation(0);
        exit.setScale(wScale, hScale);
        rectangle.attachChild(exit);
        scene.registerTouchArea(exit);
        createSettingMenu();
        loadSoundSetting();

    }

    private void playSound(Sound sound) {
        if (SpUtil.getBoolean(this, ConstantValue.SOUND, false)) {
            sound.play();
        }
    }

    private void exit() {
        finish();
    }

    private void setting() {
        settingRect.registerEntityModifier(new MoveXModifier(1f, CAMERA_WIDTH, 0));
        mainRectangle.registerEntityModifier(new MoveXModifier(1f, 0, ScaleHelper.getInstance().getXLocation(-1920, 0)));
    }

    private void begin() {
        Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
        startActivity(intent);
        mMusic.stop();

    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        setInfor();
        mBeginTime = System.currentTimeMillis();
        mHander = new Handler();
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
// 声明程序要使用音效
        engineOptions.getAudioOptions().setNeedsSound(true);
        return engineOptions;
    }

    private void setInfor() {
        String temp = SpUtil.getString(this, ConstantValue.LEVEL_INFO, null);
        if (TextUtils.isEmpty(temp)) {
            String infor = "{\n" +
                    "\t\"level\":\"1\",\n" +
                    "\t\"infor\":\"0,0,0,0,0,0,0,0,0,0\"\n" +
                    "}";
            ;
            SpUtil.setString(this, ConstantValue.LEVEL_INFO, infor);
            SpUtil.setBoolean(this, ConstantValue.MUSIC, true);
            SpUtil.setBoolean(this, ConstantValue.SOUND, true);
        }
    }

    @Override
    public void onFinishResources() {

    }

    @Override
    public void onFinishScene() {
        mainScene.setChildScene(scene, true, true, true);
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
