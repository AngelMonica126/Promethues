package cn.vailing.chunqiu.promethues.util;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by dream on 2017/7/4.
 */
public class PathHelper {
    private boolean isPositive;
    private float offset;
    private float angle;
    private float radius;
    private float offsetY;
    private float offsetX;
    private float beginX, beginY, endX, endY;
    private Vector2 position;
    private ThreeArray threeArray;


    public PathHelper() {
    }

    public PathHelper(float beginX, float beginY, float endX, float endY, float offset) {
        this.beginX = beginX;
        this.endY = endY;
        this.endX = endX;
        this.beginY = beginY;
        position = new Vector2();
        position.x = beginX;
        position.y = beginY;
        if(beginX>endX){
            float t = this.beginX;
            this. beginX = this.endX;
            this.endX = t;
        }if(beginY>endY){
            float t = this.beginY;
            this. beginY =this. endY;
            this. endY =t;
        }
        offsetX = 0;
        offsetY = 0;
        if (beginX == endX) {
            if (beginY != endY) {
                offsetY = offset;
            }
        } else {
            offsetX = offset;
            offsetY = ((endY - beginY) / (endX - beginX)) * offset;
        }
    }

    public PathHelper(float beginX, float beginY, float radius, int angle, float offset, boolean isPositive) {
        this.beginX = beginX;
        this.beginY = beginY;
        this.radius = radius;
        this.angle = (float) angle;
        threeArray = new ThreeArray();
        threeArray.x = beginX + MathUtil.Rcos(angle) * radius;
        threeArray.y = beginY + MathUtil.Rsin(angle) * radius;
        threeArray.z = angle;
        this.offset = offset;
        this.isPositive = isPositive;//顺逆时针
    }


    public ThreeArray circular() {
        if (isPositive) {
            angle += offset;
        } else {
            angle -= offset;
        }
        threeArray.x = beginX + MathUtil.Rcos(angle) * radius;
        threeArray.y = beginY + MathUtil.Rsin(angle) * radius;
        if (isPositive) {
            threeArray.z = 180 + angle;
        } else {
            threeArray.z = angle;
        }
        angle = angle % 360;

        return threeArray;
    }

    public Vector2 linePath() {
        position.x += offsetX;
        position.y += offsetY;
        if (position.x > endX || position.x < beginX) {
            offsetX = -offsetX;
            offsetY = -offsetY;
        } else if (position.y > endY || position.y < beginY) {
            offsetY = -offsetY;
            offsetX = -offsetX;
        }

        return position;
    }
}
