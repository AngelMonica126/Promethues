package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import java.util.ArrayList;
import java.util.List;

import cn.vailing.chunqiu.promethues.bean.Wood;
import cn.vailing.chunqiu.promethues.bean.WoodConnector;
import cn.vailing.chunqiu.promethues.bean.WoodHelper;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.MathUtil;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/8/5.
 */

public class WoodManager extends MyBaseManager{
    private int width;
    private float height;
    private BitmapTextureAtlas texture;
    private TiledTextureRegion textureRegion;
    private WoodConnectorManager woodConnectorManager;
    private List<Wood> woods;
    private FlameManager flameManager;

    public WoodManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld physicsWorld) {
        super(engine, context, wScale, hScale, physicsWorld);
    }
    public void setFlameManager(FlameManager flameManager){
        this.flameManager = flameManager;
    }
    protected void init() {
        height = 200;
        width = 19;
        woods = new ArrayList<>();
        woodConnectorManager = new WoodConnectorManager(engine, context, wScale, hScale, physicsWorld);

        texture = new BitmapTextureAtlas(engine.getTextureManager(), 29, 299, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        textureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "gfx/Splash/wood.png", 0, 0, 1, 1);
        texture.load();
    }

    public Wood createStraightWood(Scene scene, float x, float y, float angle, int left, int right) {
        float offsetX = (int) (height * MathUtil.Rcos(angle));
        float offsetY = (int) (height * MathUtil.Rsin(angle));
        float tempX = x;
        float tempY = y;
        Wood first = Wood(scene, tempX, tempY, angle);
        Body temp = first.getBody();
        for (int i = 0; i < right; i++) {
            tempX += offsetX;
            tempY += offsetY;
            Wood wood = Wood(scene, tempX, tempY, angle);
            WoodConnector w = woodConnectorManager.connect(scene, temp, wood.getBody(), 1);
            if (i == 0) {
                first.addConnector(w);
            } else {
                wood.addConnector(w);

            }
            woods.add(wood);
            temp = wood.getBody();
        }
        tempX = x;
        tempY = y;
        temp = first.getBody();
        for (int i = 0; i < left; i++) {
            tempX -= offsetX;
            tempY -= offsetY;
            Wood wood = Wood(scene, tempX, tempY, angle);
            WoodConnector w = woodConnectorManager.connect(scene, temp, wood.getBody(), 1);
            if (i == 0) {
                first.addConnector(w);
            } else {
                wood.addConnector(w);
            }
            woods.add(wood);
            temp = wood.getBody();

        }
        return first;
    }

    public void createVerticalWood(Scene scene, float x, float y, float angle, int up, int right) {
        Wood first = createStraightWood(scene, x, y, angle, 0, right);
        float offsetRight = -height / 2;
        float offsetUp = -height / 2;
        float length = MathUtil.pythagorean(offsetRight, offsetUp);
        float arf = (float) Math.abs(Math.toDegrees(Math.atan2(offsetUp, offsetRight)));
        arf = 90 - angle - arf;
        Wood second = createStraightWood(scene, x - length * MathUtil.Rcos(arf), y + length * MathUtil.Rsin(arf), angle + 90, up, 0);
        WoodConnector w = woodConnectorManager.connect(scene, first.getBody(), second.getBody(), 2);
        first.addConnector(w);
        woods.add(first);
        woods.add(second);
    }

    public void createConvexWood(Scene scene, float x, float y, float angle, float offsetRight, int up, int left, int right) {
        Wood first = createStraightWood(scene, x, y, angle, left, right);
        float offsetUp = -height / 2;
        offsetRight = -offsetRight;
        float length = MathUtil.pythagorean(offsetRight, offsetUp);
        float arf = (float) Math.toDegrees(Math.atan2(offsetRight, offsetUp));
        arf = 90 - angle - arf;
        Wood second = createStraightWood(scene, x - length * MathUtil.Rcos(arf), y + length * MathUtil.Rsin(arf), angle + 90, up, 0);
        WoodConnector w =  woodConnectorManager.connect(scene, first.getBody(), second.getBody(), 3);
        first.addConnector(w);
        woods.add(first);
        woods.add(second);
    }

    public void createCrossWood(Scene scene, float x, float y, float angle, float offsetRight, float offsetUp, int up, int down, int left, int right) {
        Wood first = createStraightWood(scene, x, y, angle, left, right);
        float length = MathUtil.pythagorean(offsetRight, offsetUp);
        offsetRight = -offsetRight;
        offsetUp = -offsetUp;
        float arf = (float) Math.toDegrees(Math.atan2(offsetRight, offsetUp));
        arf = 90 - angle - arf;
        Wood second = createStraightWood(scene, x - length * MathUtil.Rcos(arf), y + length * MathUtil.Rsin(arf), angle + 90, up, down);
        WoodConnector w = woodConnectorManager.connect(scene, first.getBody(), second.getBody(), 4);

        first.addConnector(w);
        woods.add(first);
        woods.add(second);
    }

    private Wood Wood(Scene scene, float tempX, float tempY, float angle) {
        Wood wood = new Wood(engine, context, null, null, null);
        wood.setInScene(scene, ScaleHelper.getInstance().getNoRealCenterX(tempX, wood.getRegion().getWidth()),
                ScaleHelper.getInstance().getNoRealCenterY(tempY, wood.getRegion().getHeight()), wScale, hScale, 0, null);
        wood.setRotation(angle + 90);
        wood.setPhysic(physicsWorld);
        wood.setCollide(flameManager);
        return wood;
    }

    public List<Wood> getWoods() {
        return woods;
    }

    public void applyForce(int index, Vector2 force, Vector2 center) {
        woods.get(index).applyForce(force, center);
    }

    public void detach(final Scene scene, final int index) {
        Wood wood = woods.get(index);
        woods.remove(index);
        wood.detach(scene);
    }
}
