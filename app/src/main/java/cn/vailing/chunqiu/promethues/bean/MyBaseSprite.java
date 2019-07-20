package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.util.ConstantValue;
import cn.vailing.chunqiu.promethues.util.SpUtil;

/**
 * Created by dream on 2017/6/19.
 */

public abstract class MyBaseSprite {
    protected BitmapTextureAtlas texture;
    protected TiledTextureRegion textureRegion;
    protected AnimatedSprite sprite;
    protected Engine engine;
    protected Context context;
    protected String path;
    protected Vector2 textureSize;
    protected Vector2 tiledSize;
    protected float wScale;
    protected float hScale;


    public MyBaseSprite(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        this.engine = engine;
        this.textureSize = textureSize;
        this.context = context;
        this.path = path;
        this.tiledSize = tiledSize;
        Myinit();
        init();
    }

    public void setInScene( Scene scene, float x, float y, float xScale, float yScale, long frame, final MySpriteOnclickListener mySpriteOnclickListener) {
        sprite = new AnimatedSprite(x, y, this.textureRegion, engine.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (mySpriteOnclickListener != null) {
                    return mySpriteOnclickListener.onTouchEvent(this, pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                } else {
                    return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                }
            }
        };
        if (frame != 0)
            sprite.animate(frame);
        sprite.setScale(xScale, yScale);
        scene.attachChild(sprite);
        if (mySpriteOnclickListener != null) {
            scene.registerTouchArea(sprite);
        }
        this.wScale =xScale;
        this.hScale = yScale;
    }

    public void setScale(float wScale, float hScale) {
        this.wScale = wScale;
        this.hScale = hScale;
    }

    public AnimatedSprite getSprite() {
        return sprite;
    }

    public TiledTextureRegion getRegion() {
        return textureRegion;
    }

    protected abstract void init();

    public void Myinit() {
        texture = new BitmapTextureAtlas(engine.getTextureManager(), (int) textureSize.x, (int) textureSize.y, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        textureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, path, 0, 0, (int) tiledSize.x, (int) tiledSize.y);
        texture.load();
    }

    protected void playSound(Sound sound) {
        if (SpUtil.getBoolean(context, ConstantValue.SOUND, false)) {
            sound.play();
        }
    }
}
