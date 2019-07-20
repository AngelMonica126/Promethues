package cn.vailing.chunqiu.promethues.util;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by dream on 2017/6/19.
 */

public class MathUtil {
    public static float judgeAngle(float initialX, float initialY, float destinationX, float destinationY) {
        return (float) Math.toDegrees(Math.atan2(destinationY - initialY,destinationX - initialX));
//        if (initialY == destinationY) {
//            if (initialX != destinationX) {
//                angle = -Math.PI / 2;
//            } else {
//                angle = 0;
//            }
//        }
//        if (initialX != destinationX && initialY != destinationY) {
//            angle = (float) Math.atan((destinationY - initialY) / (destinationX - initialX));
//            angle = Math.atan(angle);
//        }
//        float direction = (initialX * destinationY - initialY * destinationX);
//        if (direction < 0) {
//            angle = -angle;
//        } else {
//            angle = Math.PI - angle;
//        }
    }
    public static  float pythagorean(float width,float height){
        return (float) Math.sqrt(width*width+height*height);
    }
    public static double getTimeForSpeed(float x, float y, float destinationX, float destinationY, int speed) {
        float routeX = destinationX - x;
        float routeY = destinationY - y;
        float time = (float) (Math.sqrt(routeX * routeX + routeY * routeY) / speed);
        return time;
    }

    public static Vector2 getDestination(float angle, float x, float y) {
        float wight = ScaleHelper.getInstance().getAppWidth();
        Vector2 vector2 = new Vector2();
        vector2.x = wight;
        vector2.y = (float) Math.abs(vector2.x * Math.tan(Math.toRadians(angle)));
        switch (judgeArea(angle)) {
            case 1:
                vector2.x += x;
                vector2.y = y - vector2.y;
                break;
            case 2:
                vector2.x = x - vector2.x;
                vector2.y = y - vector2.y;
                break;
            case 3:
                vector2.x = x - vector2.x;
                vector2.y += y;
                break;
            case 4:
                vector2.x += x;
                vector2.y += y;
                break;
        }
        return vector2;
    }

    public static int judgeArea(float angle) {
        if (angle < 0) {
            while (angle < 0) {
                angle += 360;
            }
        }
        int area = 1;
        angle = angle % 360;
        if (angle >= 0 && angle < 90) {
            area = 1;
        } else if (angle >= 90 && angle < 180) {
            area = 2;
        } else if (angle >= 180 && angle < 270) {
            area = 3;
        } else if (angle >= 270 && angle < 360) {
            area = 4;
        }
        return area;
    }

    public static float getMinAngle(float rotation) {
        rotation = rotation % 360;
        return rotation;
    }

    public static float cos(float angle) {
        if (angle < 0) {
            while (angle < 0) {
                angle += 360;
            }
        }
        angle = angle % 360;
        if (angle == 90 || angle == 270) {
            return 0;
        } else {
            return (float) Math.cos(Math.toRadians(angle));
        }

    }

    public static float sin(float angle) {
        if (angle < 0) {
            while (angle < 0) {
                angle += 360;
            }
        }
        angle = angle % 360;
        if (angle == 0 || angle == 360)
            return 0;
        else {
            return (float) Math.sin(Math.toRadians(angle));
        }
    }

    public static float Rcos(float angle) {
        if (Math.abs(angle % 360) == 90 || Math.abs(angle % 360) == 270) {
            return 0;
        } else {
            return (float) Math.cos(Math.toRadians(angle));
        }

    }

    public static float Rsin(float angle) {
        if (Math.abs(angle % 360) == 0 || Math.abs(angle % 360) == 360)
            return 0;
        else {
            return (float) Math.sin(Math.toRadians(angle));
        }
    }

    public static int loctionToTan(float x, float y, float x1, float y1) {
        float tan = 0;
        if (x == x1) {
            tan = 0;
        }
        if (y == y1 && x != x1) {
            tan = 90;
        }
        if (x != x1 && y != y1) {
            tan = (float) -Math.toDegrees(Math.atan(y1 - y) / (x1 - x));
        }
        return (int) tan;
    }

    public static float pythagorean(float beginX, float beginY, float endX, float endY) {
        return (float) Math.sqrt(Math.pow(endX - beginX, 2) + Math.pow(endY - beginY, 2));
    }

    public static float judgeAnglePN(float beginX, float beginY, float endX, float endY) {
        float demo = beginX * endY - beginY * endX;
        if (demo == 0) {
            return 0;
        }
        if (demo > 0) {
            return 1;
        } else
            return -1;
    }

    public static boolean Approximate(double A, double B, int offset) {
        if (Math.abs(A - B) < offset) {
            return true;
        }
        return false;
    }

    public static int judgeLoactionOU(int columnIndex, int rowIndex) {
        int c = columnIndex % 2;
        int r = rowIndex % 2;
        int re = 1;
        if (c == r) {
            if (c == 1) {
                re = 1;
            } else {
                re = 3;
            }
        } else {
            if (c == 1) {
                re = 2;
            } else {
                re = 4;
            }
        }
        Log.e("vailing", re + "  ");
        return re;
    }

    public static int addAllRect(int[][] location, int i, int columnIndex, int rowIndex) {
        int count = 0;
        switch (i) {
            case 1:
//                （i,j)
                count = (location[columnIndex][rowIndex] +
                        location[columnIndex + 1][rowIndex] +
                        location[columnIndex][rowIndex + 1] +
                        location[columnIndex + 1][rowIndex + 1]);
                break;
            case 2:
//                (i+1,j)
                count = (location[columnIndex + 2][rowIndex] +
                        location[columnIndex + 3][rowIndex] +
                        location[columnIndex + 2][rowIndex + 1] +
                        location[columnIndex + 3][rowIndex + 1]);
                break;
            case 3:
//                (i,j+1)
                count = (location[columnIndex][rowIndex + 2] +
                        location[columnIndex + 1][rowIndex + 2] +
                        location[columnIndex][rowIndex + 3] +
                        location[columnIndex + 1][rowIndex + 3]);
                break;
            case 4:
//                (i+1,j+1)
                count = (location[columnIndex + 2][rowIndex + 2] +
                        location[columnIndex + 3][rowIndex + 2] +
                        location[columnIndex + 2][rowIndex + 3] +
                        location[columnIndex + 3][rowIndex + 3]);
                break;
        }
        return count;
    }
}
