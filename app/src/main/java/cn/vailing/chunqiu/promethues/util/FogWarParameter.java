package cn.vailing.chunqiu.promethues.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dream on 2017/7/15.
 */

public class FogWarParameter {
    private static List<Integer> idList;
    private static List<Integer> indexList;
    private static FogWarParameter fogWarParameter = new FogWarParameter();
    private static int width;
    private static int height;
    private static int offsetX;
    private static int offsetY;
    private static int column;
    private static int row;

    private FogWarParameter() {
        init();
    }

    public static FogWarParameter getInstance() {
        return fogWarParameter;
    }

    private void init() {
        column = 34;
        row = 34;
        width = 4352;
        height = 4352;
        offsetX = -256;
        offsetY = -1536;
        idList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            idList.add(i + 1);
        }
        indexList = new ArrayList<>();
        indexList.add(0);
        indexList.add(4);
        indexList.add(8);
        indexList.add(12);
        indexList.add(1);
        indexList.add(5);
        indexList.add(9);
        indexList.add(13);
        indexList.add(2);
        indexList.add(6);
        indexList.add(10);
        indexList.add(14);
        indexList.add(3);
        indexList.add(7);
        indexList.add(11);
        indexList.add(15);
    }

    public static int getOffsetY() {
        return offsetY;
    }

    public static int getOffsetX() {
        return offsetX;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

    public static int getGrid(){
        return 128;
    }
    public static int searchIndex(int index) {
        return idList.get(indexList.indexOf(index));
    }
}
