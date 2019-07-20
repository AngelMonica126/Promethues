package cn.vailing.chunqiu.promethues.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by dream on 2017/6/17.
 */

public class ScaleHelper {
    private final int defaultWidth = 1920;
    private final int defaultHeight = 1080;
    private static ScaleHelper scaleHelper = new ScaleHelper();
    private int CAMERA_HEIGHT;
    private int CAMERA_WIDTH;
    private float scaleWidth = 1;
    private float scaleHeight = 1;

    private ScaleHelper() {
    }

    public static ScaleHelper getInstance() {
        return scaleHelper;
    }

    public void init(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        CAMERA_HEIGHT = dm.heightPixels;
        CAMERA_WIDTH = dm.widthPixels;
        scaleWidth = (float) CAMERA_WIDTH / defaultWidth;
        scaleHeight = (float) CAMERA_HEIGHT / defaultHeight;
    }

    public int getAppWidth() {
        return CAMERA_WIDTH;
    }

    public int getAppHeight() {
        return CAMERA_HEIGHT;
    }

    public float getXLocation(float x, float w) {
        return x * scaleWidth - (w - w * scaleWidth) / 2;
    }

    public float getRealX(float x, float w) {
        return x / scaleWidth + w * (1 - scaleWidth) / (2 * scaleWidth);
    }

    public float getRealY(float y, float h) {
        return y / scaleHeight + h * (1 - scaleHeight) / (2 * scaleHeight);
    }

    public float getYLocation(float y, float h) {
        return y * scaleHeight - (h - h * scaleHeight) / 2;
    }

    public float getHeightScale() {
        return scaleHeight;
    }

    public float getWidthScale() {
        return scaleWidth;
    }

    public float getCenterXLoction(float x, float width) {
        return x * scaleWidth - width / 2;
    }

    public float getRealCenterXLocation(float x, float width) {
        return  x - width * (1 - scaleWidth) / 2;
    }

    public float getRealCenterYLocation(float y, float height) {
        return y - height * (1 - scaleHeight) / 2;
    }

    public float getRealCenterX(float x, float width) {
        return x - width / 2;
    }

    public float getRealCenterY(float y, float height) {
        return y - height / 2;
    }

    public float getCenterYLoction(float y, float height) {
        return y * scaleHeight - height / 2;
    }

    public float getNoRealCenterX(float x, float width) {
        return x*scaleWidth - width / 2;
    }
    public float getNoRealCenterY(float y, float height) {
        return y*scaleHeight - height / 2;
    }
}
