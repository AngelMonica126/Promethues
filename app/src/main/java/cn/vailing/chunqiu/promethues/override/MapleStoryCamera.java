package cn.vailing.chunqiu.promethues.override;

import android.util.Log;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.Constants;
import org.andengine.util.adt.cache.LRUCache;

/**
 * Created by dream on 2017/9/25.
 */

public class MapleStoryCamera extends BoundCamera {

    private Rectangle mChaseEntity;
    private float mBoundsXMax = 16 * 80;
    private float mBoundsYMax = 16 * 50;
    private float startX;
    private float startY;
    private float cameraX;
    private float cameraY;
    private boolean isStop;
    private int type;

    public MapleStoryCamera(float pX, float pY, float pWidth, float pHeight) {
        super(pX, pY, pWidth, pHeight);
    }

    public MapleStoryCamera(float pX, float pY, float pWidth, float pHeight, float boundsXMax, float boundsYMax) {
        super(pX, pY, pWidth, pHeight);
        this.mBoundsXMax = boundsXMax;
        this.mBoundsYMax = boundsYMax;
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        if (mChaseEntity != null && !isStop) {
            final float[] centerCoordinates = mChaseEntity.getSceneCenterCoordinates();
            float x = centerCoordinates[Constants.VERTEX_INDEX_X];
            float y = centerCoordinates[Constants.VERTEX_INDEX_Y];
            cameraX = x;
            cameraY = y;
            switch (type){
                case 1:
                    cameraX = this.getWidth()/2;
                    if(y > this.getHeight()/2){
                        cameraY = this.getHeight()/2;
                    }

                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    cameraY = this.getHeight()/2;
                    if(x < this.getWidth()/2){
                        cameraX = this.getWidth()/2;
                    }
                    break;

            }
            super.setCenter(cameraX, cameraY);
        } else if(mChaseEntity!=null){
            super.setCenter(cameraX, cameraY);
        }
    }

    public void setChaseEntity(Rectangle pChaseEntity) {
        super.setChaseEntity(pChaseEntity);
        this.mChaseEntity = pChaseEntity;
//        cameraX = this.getWidth() / 2;
//        cameraY = this.getHeight() / 2;

    }

    @Override
    public void reset() {
        cameraX = this.getWidth() / 2;
        cameraY = this.getHeight() / 2;
        super.setCenter(cameraX, cameraY);
        this.mChaseEntity = null;
        super.reset();
    }

    public void stop() {
        isStop = true;
    }

    public void resume() {
        isStop = false;
    }

    public void setType(int type) {
        this.type = type;
    }
}
