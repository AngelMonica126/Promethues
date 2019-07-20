package cn.vailing.chunqiu.promethues.util;

/**
 * Created by dream on 2017/7/6.
 */

public class ThreeArray {
    public float x;
    public float y;
    public float z;

    public ThreeArray() {
    }

    public ThreeArray(float x, float z, float y) {
        this.x = x;
        this.z = z;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
