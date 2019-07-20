package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;
import android.os.Bundle;

import com.badlogic.gdx.math.Vector2;


import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;

import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.util.PathContentValue;
import cn.vailing.chunqiu.promethues.util.PathHelper;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/9/5.
 */

public class Lighting extends MyBaseSprite {
    private Rectangle collideRectangle;
    private Scene scene;
    private IUpdateHandler locationIU;
    private IUpdateHandler rotateIU;


    public Lighting(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/Lighting.png", new Vector2(900, 900), new Vector2(3, 10));

    }

    @Override
    public void Myinit() {
        textureRegion = TextureHelper.getInstance().getLightingRegion();
    }

    @Override
    public void setInScene(Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {
        super.setInScene(scene, x, y, xScale, yScale, 60, null);
        collideRectangle = new Rectangle(sprite.getWidth()*0.1f, sprite.getHeight() / 2, sprite.getWidth()*0.8f, 1, engine.getVertexBufferObjectManager());
        collideRectangle.setColor(0, 0, 0, 0);
        sprite.attachChild(collideRectangle);
    }

    public void setPath(Scene scene, int way, Bundle bundle, float speed) {
        this.scene = scene;
        switch (way) {
            case PathContentValue.LINE:
                setLine(bundle, speed);
                break;
            case PathContentValue.CIRCULAR:
                setCircular(bundle, speed);
                break;
        }
    }

    private void setCircular(Bundle bundle, float speed) {
        float x = bundle.getInt("x");
        float y = bundle.getInt("y");
        float beginAngle = bundle.getInt("beginAngle");
        float radius = bundle.getInt("radius");
        final boolean isRotate = bundle.getBoolean("isRotate");
        float beginX = (float) (x + Math.cos(beginAngle) * radius);
        float beginY = (float) (y + Math.sinh(beginAngle) * radius);
        setInScene(scene,
                ScaleHelper.getInstance().getCenterXLoction(beginX, textureRegion.getWidth()),
                ScaleHelper.getInstance().getCenterYLoction(beginY, textureRegion.getHeight()),
                wScale, hScale, 60, null);

        final PathHelper pathHelper = new PathHelper(x, y, radius, 0, speed, true);
        locationIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                setLocation(ScaleHelper.getInstance().getCenterXLoction(pathHelper.circular().x, textureRegion.getWidth()),
                        ScaleHelper.getInstance().getCenterYLoction(pathHelper.circular().y, textureRegion.getHeight()));
                if (isRotate) {
                    setRotation((int) pathHelper.circular().z + 90);
                }
            }

            @Override
            public void reset() {

            }
        };
        sprite.registerUpdateHandler(locationIU);
    }

    public Rectangle getCollideRectangle() {
        return collideRectangle;
    }

    public void setRotate(float speed, boolean isPositive) {
        final float mSpeed = isPositive ? speed : (-speed);
        rotateIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                setRotation((sprite.getRotation() + mSpeed));
            }

            @Override
            public void reset() {

            }
        };
        sprite.registerUpdateHandler(rotateIU);
    }

    public void setRotate(float rotateCenterX, float rotateCenterY, int speed, boolean isPositive) {
        rotateCenterX *= wScale;
        rotateCenterX -= sprite.getX();
        rotateCenterY *= hScale;
        rotateCenterY -= sprite.getY();

        sprite.setRotationCenter(rotateCenterX, rotateCenterY);
        final int mSpeed = isPositive ? speed : (-speed);
        rotateIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                setRotation((sprite.getRotation() + mSpeed));
            }

            @Override
            public void reset() {

            }
        };
        sprite.registerUpdateHandler(rotateIU);
    }

    private void setLine(Bundle bundle, float speed) {
        int beginX = bundle.getInt("beginX");
        int beginY = bundle.getInt("beginY");
        int endX = bundle.getInt("endX");
        int endY = bundle.getInt("endY");
        setInScene(scene,
                ScaleHelper.getInstance().getCenterXLoction(beginX, textureRegion.getWidth()),
                ScaleHelper.getInstance().getCenterYLoction(beginY, textureRegion.getHeight()),
                wScale, hScale, 60, null);
        final PathHelper pathHelper = new PathHelper(beginX, beginY, endX, endY, speed);
        locationIU = new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                setLocation(ScaleHelper.getInstance().getCenterXLoction(pathHelper.linePath().x, textureRegion.getWidth()),
                        ScaleHelper.getInstance().getCenterYLoction(pathHelper.linePath().y, textureRegion.getHeight()));
            }

            @Override
            public void reset() {

            }
        };
        sprite.registerUpdateHandler(locationIU);
    }

    private void setLocation(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void setRotation(float angle) {
        sprite.setRotation(angle);
    }

    @Override
    protected void init() {

    }
}
