package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;


import org.andengine.engine.Engine;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import cn.vailing.chunqiu.promethues.is.OnGameManger;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/9/23.
 */

public class ScoreManager extends MyBaseManager {
    private TextureRegion scoreRegion;
    private TextureRegion timeRegion;
    private TiledTextureRegion starRegion;
    private AnimatedSprite star1;
    private AnimatedSprite star2;
    private AnimatedSprite star3;
    private OnGameManger onGameManger;
    private int score;
    private Font mFont;
    private long mTime;
    private Timer timer;

    public ScoreManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
    }

    public void createScore(Rectangle scene, OnGameManger onGameManger) {
        this.onGameManger = onGameManger;
        Sprite score = new Sprite(ScaleHelper.getInstance().getXLocation(0, scoreRegion.getWidth()),
                ScaleHelper.getInstance().getYLocation(0, scoreRegion.getHeight()), scoreRegion, engine.getVertexBufferObjectManager());
        score.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());
        scene.attachChild(score);
        star1 = new AnimatedSprite(score.getWidth() * 0.9f, score.getHeight() * 0.6f, starRegion, engine.getVertexBufferObjectManager());
        star2 = new AnimatedSprite(star1.getWidth() + score.getWidth() * 0.9f, score.getHeight() * 0.6f, starRegion, engine.getVertexBufferObjectManager());
        star3 = new AnimatedSprite(star1.getWidth() * 2f + score.getWidth() * 0.9f, score.getHeight() * 0.6f, starRegion, engine.getVertexBufferObjectManager());
        star1.stopAnimation(1);
        star2.stopAnimation(1);
        star3.stopAnimation(1);
        score.attachChild(star1);
        score.attachChild(star2);
        score.attachChild(star3);
        final Sprite time = new Sprite(ScaleHelper.getInstance().getXLocation(timeRegion.getWidth() * 0.1f, timeRegion.getWidth()),
                ScaleHelper.getInstance().getYLocation(0, timeRegion.getHeight()), timeRegion, engine.getVertexBufferObjectManager());
        time.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());
        scene.attachChild(time);
        this.mFont = FontFactory.createFromAsset(engine.getFontManager(), engine.getTextureManager(), 512, 512, TextureOptions.BILINEAR, context.getAssets(), "font/font.ttf", 80, true, android.graphics.Color.BLACK);
        this.mFont.load();
        final Text text = new Text(score.getWidth() * 0.9f, score.getHeight() * 0.25f, mFont, "00:00", engine.getVertexBufferObjectManager());
        score.attachChild(text);
        timer = new Timer();
        final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mTime++;
                text.setText(sdf.format(mTime * 1000));

            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    public void addStar() {
        score++;
        switch (score) {
            case 1:
                star1.stopAnimation(0);
                break;
            case 2:
                star2.stopAnimation(0);
                break;
            case 3:
                star3.stopAnimation(0);
                break;
        }
    }

    public long getTime() {
        return mTime;
    }

    public int getScore() {
        return score;
    }

    @Override
    protected void init() {
        BitmapTextureAtlas scoreTexture = new BitmapTextureAtlas(engine.getTextureManager(), 156, 252, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        scoreRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(scoreTexture, context, "device/Score.png", 0, 0);
        scoreTexture.load();

        BitmapTextureAtlas timeTexture = new BitmapTextureAtlas(engine.getTextureManager(), 371, 237, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        timeRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(timeTexture, context, "device/Time.png", 0, 0);
        timeTexture.load();
        BitmapTextureAtlas starTexture = new BitmapTextureAtlas(engine.getTextureManager(), 100, 50, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        starRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(starTexture, context, "device/Start.png", 0, 0, 2, 1);
        starTexture.load();
    }

    public void stop() {
        timer.cancel();
    }
}
