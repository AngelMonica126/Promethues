package cn.vailing.chunqiu.promethues.util;

import android.content.Context;

import org.andengine.engine.Engine;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

/**
 * Created by dream on 2017/9/15.
 */

public class TextureHelper {
    private static TextureHelper textureHelper = new TextureHelper();
    private Context context;
    private Engine engine;
    private TiledTextureRegion AccelerateButtonlRegion;
    private TiledTextureRegion AccelerateTopRegion;
    private TiledTextureRegion AccelerateDowneRegion;
    private TiledTextureRegion BallRegion;
    private TiledTextureRegion BalloonRegion;
    private TiledTextureRegion BalloonTowerRegion;
    private TiledTextureRegion BalloonFireRegion;
    private TiledTextureRegion CalabashRegion;
    private TiledTextureRegion CalabashFireReadyRegion;
    private TiledTextureRegion CalabashFireRegion;
    private TiledTextureRegion FireDeviceFireRegion;
    private TiledTextureRegion LightingRegion;
    private TiledTextureRegion MedusaRegion;
    private TiledTextureRegion MedusaBKRegion;
    private TiledTextureRegion MirrorRegion;
    private TextureRegion RotateRegion;
    private TiledTextureRegion FlameStaticRegion;
    private TiledTextureRegion RopeRegion;
    private TiledTextureRegion RopeBuringRegion;
    private TiledTextureRegion TransforRegion;
    private TiledTextureRegion woodRegion;
    private TiledTextureRegion BlinkRegion;
    private TiledTextureRegion flamelRegion;
    private TiledTextureRegion ProtectorrRegion;
    private TiledTextureRegion ProtectorDieRegion;
    private TiledTextureRegion flameBoomRegion;
    private TextureRegion CalabashRotateRegion;

    private TextureHelper() {
    }

    public static TextureHelper getInstance() {
        return textureHelper;
    }

    public void init(Engine engine, Context context) {
        this.engine = engine;
        this.context = context;
    }

    public void initFlame(){
        BitmapTextureAtlas flameButton = new BitmapTextureAtlas(engine.getTextureManager(), 840, 268, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        flamelRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(flameButton, context, "device/flame.png", 0, 0, 7, 4);
        flameButton.load();

        BitmapTextureAtlas  Protector = new BitmapTextureAtlas(engine.getTextureManager(), 500, 400, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
         ProtectorrRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Protector, context, "device/Protector.png",
                0, 0, 5, 4);
        Protector.load();

        BitmapTextureAtlas  ProtectorDie = new BitmapTextureAtlas(engine.getTextureManager(), 1125, 450, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        ProtectorDieRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(ProtectorDie, context, "device/ProtectorDie.png",
                0, 0, 5, 2);
        ProtectorDie.load();

        BitmapTextureAtlas  flameBoom = new BitmapTextureAtlas(engine.getTextureManager(), 1680, 536, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        flameBoomRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(flameBoom, context, "device/flameBoom.png", 0, 0, 7, 4);
        flameBoom.load();
    }
    public void initAccelerateDevice() {
        BitmapTextureAtlas AccelerateButton = new BitmapTextureAtlas(engine.getTextureManager(), 200, 100, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        AccelerateButtonlRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(AccelerateButton, context, "device/AccelerateButton.png", 0, 0, 2, 1);
        AccelerateButton.load();


        BitmapTextureAtlas AccelerateTop = new BitmapTextureAtlas(engine.getTextureManager(), 3466, 400, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        AccelerateTopRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(AccelerateTop, context, "device/AccelerateTop.png", 0, 0, 13, 2);
        AccelerateTop.load();

        BitmapTextureAtlas AccelerateDown = new BitmapTextureAtlas(engine.getTextureManager(), 3466, 400, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        AccelerateDowneRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(AccelerateDown, context, "device/AccelerateDown.png", 0, 0, 13, 2);
        AccelerateDown.load();
    }

    public void initBall() {
        BitmapTextureAtlas Ball = new BitmapTextureAtlas(engine.getTextureManager(), 100, 100, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BallRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Ball, context, "device/Ball.png", 0, 0, 1, 1);
        Ball.load();
    }

    public void initBalloon() {
        BitmapTextureAtlas Balloon = new BitmapTextureAtlas(engine.getTextureManager(), 2000, 226, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BalloonRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Balloon, context, "device/Balloon.png", 0, 0, 5, 2);
        Balloon.load();

        BitmapTextureAtlas BalloonTower = new BitmapTextureAtlas(engine.getTextureManager(), 70, 182, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BalloonTowerRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(BalloonTower, context, "device/BalloonTower.png", 0, 0, 1, 1);
        BalloonTower.load();

        BitmapTextureAtlas BalloonFire = new BitmapTextureAtlas(engine.getTextureManager(), 1900, 100, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BalloonFireRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(BalloonFire, context, "device/BalloonFire.png", 0, 0, 19, 1);
        BalloonFire.load();
    }

    public void initCalabash() {
        BitmapTextureAtlas Calabash = new BitmapTextureAtlas(engine.getTextureManager(), 150, 150, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        CalabashRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Calabash, context, "device/Calabash.png", 0, 0, 1, 1);
        Calabash.load();

        BitmapTextureAtlas CalabashFireReady = new BitmapTextureAtlas(engine.getTextureManager(), 1000, 400, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        CalabashFireReadyRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(CalabashFireReady, context, "device/CalabashFireReady.png", 0, 0, 5, 2);
        CalabashFireReady.load();

        BitmapTextureAtlas CalabashFire = new BitmapTextureAtlas(engine.getTextureManager(), 1000, 800, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        CalabashFireRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(CalabashFire, context, "device/CalabashFire.png", 0, 0, 5, 4);
        CalabashFire.load();

        BitmapTextureAtlas CalabashRotate = new BitmapTextureAtlas(engine.getTextureManager(), 300, 300, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        CalabashRotateRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(CalabashRotate, context, "device/BlashRotate.png", 0, 0);
        CalabashRotate.load();

    }

    public void initFireDevice() {
        BitmapTextureAtlas FireDeviceFire = new BitmapTextureAtlas(engine.getTextureManager(), 1500, 1300, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        FireDeviceFireRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(FireDeviceFire, context, "device/FireDeviceFire.png", 0, 0, 6, 5);
        FireDeviceFire.load();
    }

    public void initLighting() {
        BitmapTextureAtlas Lighting = new BitmapTextureAtlas(engine.getTextureManager(), 900, 900, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        LightingRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Lighting, context, "device/Lighting.png", 0, 0, 3, 10);
        Lighting.load();
    }

    public void initMedusa() {
        BitmapTextureAtlas Medusa = new BitmapTextureAtlas(engine.getTextureManager(), 1200, 120, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        MedusaRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Medusa, context, "device/Medusa.png", 0, 0, 10, 1);
        Medusa.load();

        BitmapTextureAtlas MedusaBK = new BitmapTextureAtlas(engine.getTextureManager(), 2100, 1050, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        MedusaBKRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(MedusaBK, context, "device/MedusaBK.png", 0, 0, 6, 3);
        MedusaBK.load();
    }

    public void initMirror() {
        BitmapTextureAtlas Mirror = new BitmapTextureAtlas(engine.getTextureManager(), 320, 320, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        MirrorRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Mirror, context, "device/Mirror.png", 0, 0, 1, 1);
        Mirror.load();

        BitmapTextureAtlas Rotate = new BitmapTextureAtlas(engine.getTextureManager(), 150, 150, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        RotateRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(Rotate, context, "device/Rotate.png", 0, 0);
        Rotate.load();
    }

    public void initPlane() {
        BitmapTextureAtlas FlameStatic = new BitmapTextureAtlas(engine.getTextureManager(), 1350, 50, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        FlameStaticRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(FlameStatic, context, "device/FlameStatic.png", 0, 0, 27, 1);
        FlameStatic.load();
    }

    public void initRope() {
        BitmapTextureAtlas Rope = new BitmapTextureAtlas(engine.getTextureManager(), 10, 20, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        RopeRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Rope, context, "device/Rope.png", 0, 0, 1, 1);
        Rope.load();

        BitmapTextureAtlas RopeBuring = new BitmapTextureAtlas(engine.getTextureManager(), 110, 20, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        RopeBuringRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(RopeBuring, context, "device/RopeBuring.png", 0, 0, 11, 1);
        RopeBuring.load();
    }

    public void initTransfer() {
        BitmapTextureAtlas Transfor = new BitmapTextureAtlas(engine.getTextureManager(), 2200, 475, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        TransforRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Transfor, context, "device/Transfor.png", 0, 0, 11, 2);
        Transfor.load();
    }

    public void initWood() {
        BitmapTextureAtlas wood = new BitmapTextureAtlas(engine.getTextureManager(), 399, 200, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        woodRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(wood, context, "device/Wood.png", 0, 0, 21, 1);
        wood.load();
    }

    public void initBlink() {
        BitmapTextureAtlas Blink = new BitmapTextureAtlas(engine.getTextureManager(), 1600, 1280, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BlinkRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(Blink, context, "device/MirrorFire.png", 0, 0, 5, 4);
        Blink.load();
    }

    public TextureRegion getCalabashRotateRegion() {
        return CalabashRotateRegion;
    }

    public TiledTextureRegion getFlameBoomRegion() {
        return flameBoomRegion;
    }

    public TiledTextureRegion getProtectorDieRegion() {
        return ProtectorDieRegion;
    }

    public TiledTextureRegion getFlamelRegion() {
        return flamelRegion;
    }

    public TiledTextureRegion getProtectorrRegion() {
        return ProtectorrRegion;
    }

    public TiledTextureRegion getBlinkRegion() {
        return BlinkRegion;
    }

    public TiledTextureRegion getWoodRegion() {
        return woodRegion;
    }

    public TiledTextureRegion getTransforRegion() {
        return TransforRegion;
    }

    public TiledTextureRegion getRopeBuringRegion() {
        return RopeBuringRegion;
    }

    public TiledTextureRegion getRopeRegion() {
        return RopeRegion;
    }

    public TiledTextureRegion getFlameStaticRegion() {
        return FlameStaticRegion;
    }

    public TextureRegion getRotateRegion() {
        return RotateRegion;
    }

    public TiledTextureRegion getMirrorRegion() {
        return MirrorRegion;
    }

    public TiledTextureRegion getMedusaBKRegion() {
        return MedusaBKRegion;
    }

    public TiledTextureRegion getMedusaRegion() {
        return MedusaRegion;
    }

    public TiledTextureRegion getLightingRegion() {
        return LightingRegion;
    }

    public TiledTextureRegion getFireDeviceFireRegion() {
        return FireDeviceFireRegion;
    }

    public TiledTextureRegion getCalabashFireRegion() {
        return CalabashFireRegion;
    }

    public TiledTextureRegion getCalabashFireReadyRegion() {
        return CalabashFireReadyRegion;
    }

    public TiledTextureRegion getCalabashRegion() {
        return CalabashRegion;
    }

    public TiledTextureRegion getBalloonFireRegion() {
        return BalloonFireRegion;
    }

    public TiledTextureRegion getBalloonTowerRegion() {
        return BalloonTowerRegion;
    }

    public TiledTextureRegion getBalloonRegion() {
        return BalloonRegion;
    }

    public TiledTextureRegion getBallRegion() {
        return BallRegion;
    }

    public TiledTextureRegion getAccelerateButtonlRegion() {
        return AccelerateButtonlRegion;
    }

    public TiledTextureRegion getAccelerateTopRegion() {
        return AccelerateTopRegion;
    }

    public TiledTextureRegion getAccelerateDowneRegion() {
        return AccelerateDowneRegion;
    }

}
