package cn.vailing.chunqiu.promethues.manager;

import android.content.Context;
import android.util.Log;


import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;

import cn.vailing.chunqiu.promethues.bean.Dark;
import cn.vailing.chunqiu.promethues.bean.Flame;
import cn.vailing.chunqiu.promethues.is.OnFinishListener;
import cn.vailing.chunqiu.promethues.is.OnGameManger;
import cn.vailing.chunqiu.promethues.override.MapleStoryCamera;
import cn.vailing.chunqiu.promethues.override.MyPhysicsWorld;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/8/9.
 */
@SuppressWarnings("all")
public class FlameManager extends MyBaseManager implements OnFinishListener {
    private OnGameManger onGameManger;
    private int num;
    private Flame flame;
    private float top;
    private float left;
    private float bottom;
    private float right;
    private Dark dark;
    private Scene scene;
    private boolean isCreate;
    private boolean isFollow;
    private MapleStoryCamera camera;
    private boolean isProtect;
    private boolean isChange;
    private int speed;
    private boolean isDestory;

    public FlameManager(Engine engine, Context context, float wScale, float hScale, MyPhysicsWorld myPhysicsWorld, OnGameManger onGameManger) {
        super(engine, context, wScale, hScale, myPhysicsWorld);
        this.onGameManger = onGameManger;
        this.speed = -1;
    }

    public void setBound(final float top, final float left, final float bottom, final float right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public void setDark(Dark dark) {
        this.dark = dark;
    }

    public void setChange(boolean change) {
        isChange = change;
    }

    public void createFlame(Scene scene, float x, float y, float angle) {
        this.scene = scene;
        if (num <= 0) {
            return;
        }

        isDestory = false;
        flame = new Flame(engine, context, null, null, null);
        isCreate = true;
        isChange = false;
        num--;
        onGameManger.setFlameNum(num + 1);
        flame.setPhysic(physicsWorld);
        flame.setInScene(scene,
                ScaleHelper.getInstance().getRealCenterX(x, width),
                ScaleHelper.getInstance().getRealCenterY(y, height),
                wScale,
                hScale,
                0,
                null);
        flame.setBound(top, left, bottom, right, this);
        if (speed != -1)
            flame.setSpeed(this.speed);


        flame.start(angle);
        if (isFollow)
            camera.setChaseEntity(flame.getBkRectangle());
        if (dark != null) {
            flame.setDark(dark);
        }
        if (isFollow) {
            camera.setChaseEntity(flame.getBkRectangle());
        }
//        }
    }

    public boolean canBeCreate() {
        boolean b = false;
        if (!isCreate && num > 0) {
            b = true;
        }
        return b;
    }

    public void setNum(int num) {
        this.num = num;
        onGameManger.setFlameNum(num + 1);
    }

    @Override
    protected void init() {
        width = 120;
        height = 67;
    }

    public Rectangle getFlameSprite() {
        if (flame != null) {
            return flame.getColliodSprite();
        }
        return null;
    }

    public Rectangle getBkSprite() {
        if (flame != null) {
            return flame.getBkRectangle();
        }
        return null;
    }

    public Shape getCutSprite() {
        if (flame != null) {
            return flame.getCut();
        }
        return null;
    }

    public void setFollow(MapleStoryCamera camera) {
        isFollow = true;
        this.camera = camera;
    }

    @Override
    public void finish() {
        if (flame != null) {
            flame.stop();
            flame.detach();
            flame = null;
        }
        if (isFollow) {
            camera.reset();
        }
        isCreate = false;
        if (num <= 0 && onGameManger != null) {
            onGameManger.gameOver();
        }
    }

    public boolean isCreate() {
        return isCreate;
    }

    public void addForce(float angle) {
        if (flame != null) {
            flame.addForce(angle);
        }
    }

    public int getZIndex() {
        if (flame != null) {
            return flame.getZIndex();
        }
        return -1;
    }

    public void setVisibleByScale(boolean b, float longs, OnFinishListener onFinishListener) {
        if (flame != null) {
            flame.setVisibleByScale(b, longs, onFinishListener);
            isChange = true;
            if (isFollow) {
                camera.stop();
            }
        }
    }

    public void setVisible(int longs, boolean visible) {
        if (flame != null) {
            flame.setVisible(longs, visible);

        }
    }

    public void start(float centerX, float centerY, float angle, float speed, OnFinishListener onFinishListener) {
        if (flame != null) {
            flame.start(centerX, centerY, angle, speed, onFinishListener);
            isChange = false;
            if (isFollow) {
                camera.resume();
            }
        }
    }

    public int getAngle() {
        int angle = 0;
        if (flame != null) {
            angle = (int) flame.getAngle();
        }
        return angle;
    }

    public void move(float angle) {
        if (flame != null) {
            flame.move(angle);
            isChange = false;

        }
    }

    public int getWidth() {
        return (int) width;
    }

    public int getHeight() {
        return (int) height;
    }

    public void stop() {
        if (flame != null) {
            flame.stop();
            isChange = true;

        }
    }

    public void setPosition(float x, float y, float z) {
        if (flame != null) {
            flame.setPosition(x, y, z);
        }
    }

    public void openProtection() {
        if (flame != null) {
            flame.openProtection();
            isProtect = true;
        }
    }

    public void closeProtection() {
        if (flame != null) {
            flame.closeProtection(new OnFinishListener() {
                @Override
                public void finish() {
                    isProtect = false;
                }
            });

        }
    }

    public boolean isOnClosePortector() {
        if (flame != null) {
            return flame.isOnCloseProtector();
        }
        return false;
    }

    public boolean isProtect() {
        return isProtect;
    }

    public void boom(OnFinishListener onFinishListener) {
        if (flame != null) {
            isChange = true;
            flame.stop();
            isDestory = true;
            flame.flameBoom(onFinishListener);
//            flame.detach();
//            finish();
            if (isFollow) {
                camera.reset();
            }
        }
    }

    public boolean isChange() {
        return isChange;
    }

    public void moveNS(float angle) {
        if (flame != null) {
            flame.moveNS(angle);
            isChange = false;

        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean getFollow() {
        return isFollow;
    }

    public boolean isDestory() {
        return isDestory;
    }

    public void stopYS() {
        if(flame!=null){
            flame.stopYS();
        }
    }
}
