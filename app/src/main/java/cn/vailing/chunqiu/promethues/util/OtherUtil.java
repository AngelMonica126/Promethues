package cn.vailing.chunqiu.promethues.util;

/**
 * Created by dream on 2017/8/10.
 */

public class OtherUtil {
    public static long[] getAnimateFrame(int all,int now,int frame){
        long[] longs = new long[all];
        int i = 0;
        for(;i<now;i++){
            longs[i] = frame;
        }
        for (;i<all;i++){
            longs[i] = 0;
        }
        return longs;
    }

    public static long[] getAnimateFrameAfter(int all,int begin ,int now,int frame){
        long[] longs = new long[all];
        int i = 0;
        for(;i<begin;i++){
            longs[i] = 0;
        }
        for(;i<now;i++){
            longs[i] = frame;
        }
        for (;i<all;i++){
            longs[i] = 0;
        }
        return longs;
    }
}
