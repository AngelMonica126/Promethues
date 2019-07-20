package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import java.util.ArrayList;
import java.util.List;

import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.MathUtil;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/7/18.
 */
@SuppressWarnings("all")
public class ObstacleManager extends MyBaseManager {
    private float width;
    private float height;
    private List<Rectangle> rectangles;
    protected BitmapTextureAtlas texture;
    protected TiledTextureRegion textureRegion;

    public ObstacleManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
    }

    protected void init() {
        width = 66;
        height = 32;
        rectangles = new ArrayList<>();

        texture = new BitmapTextureAtlas(engine.getTextureManager(), 300, 50, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        textureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "device/Obstacle.png", 0, 0, 1, 1);
        texture.load();
    }

    public void createObstacle(Scene scene, float beginX, float beginY, float angle, int num) {
        float offsetX = (int) (width * MathUtil.Rcos(angle));
        float offsetY = (int) (width * MathUtil.Rsin(angle));
        float tempX = beginX;
        float tempY = beginY;
        for (int i = 0; i < num; i++) {
            Obstacle(scene, tempX, tempY, angle);
            tempX += offsetX;
            tempY += offsetY;
        }
        float w = width * num;
        final Rectangle rectangle = new Rectangle(
                ScaleHelper.getInstance().getXLocation(beginX, w),
                ScaleHelper.getInstance().getYLocation(beginY, height),
                w, height, engine.getVertexBufferObjectManager()
        );
        rectangle.setScale(wScale, hScale);
        float centerX = w * (1 - wScale) / 2 + width * wScale / 2;
        float centerY = rectangle.getRotationCenterY();
        rectangle.setRotationCenter(centerX, centerY);
        rectangle.setRotation(angle);
        rectangle.setColor(0,0, 0, 0);
        scene.attachChild(rectangle);
        rectangles.add(rectangle);
        if (physicsWorld != null) {
            Body body = PhysicsFactory.createBoxBody(physicsWorld, rectangle, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(1f, 0.1f, 0.5f));
            physicsWorld.registerPhysicsConnector(new PhysicsConnector(rectangle, body, false, false));
        }
    }

    public void Obstacle(Scene scene, float tempX, float tempY, float angle) {
        Sprite obstacle = new Sprite(ScaleHelper.getInstance().getXLocation(tempX, textureRegion.getWidth()),
                ScaleHelper.getInstance().getYLocation(tempY, textureRegion.getHeight()),
                textureRegion,
                engine.getVertexBufferObjectManager());
        obstacle.setScale(wScale, hScale);
        obstacle.setRotation(angle);
        scene.attachChild(obstacle);
    }

    public void setCollision(final FlameManager flameManager) {
        for (final Rectangle rectangle : rectangles) {
            rectangle.registerUpdateHandler(new IUpdateHandler() {
                @Override
                public void onUpdate(float v) {
                    if (flameManager.isCreate()) {
                        if (rectangle.collidesWith(flameManager.getFlameSprite())) {
                            if (flameManager.isProtect()&&!flameManager.isOnClosePortector()) {
                                float angle = -flameManager.getAngle() + 2 * rectangle.getRotation();
                                flameManager.move(angle-90);
                                flameManager.closeProtection();
                            } else if(!flameManager.isProtect()){
                                rectangle.unregisterUpdateHandler(this);
                                final IUpdateHandler temp = this;
                                flameManager.boom(new OnFinishListener() {
                                    @Override
                                    public void finish() {
                                      flameManager.finish();
                                        rectangle.registerUpdateHandler(temp);
                                    }
                                });

                            }
                        }
                    }
                }

                @Override
                public void reset() {

                }
            });
        }
    }
}
