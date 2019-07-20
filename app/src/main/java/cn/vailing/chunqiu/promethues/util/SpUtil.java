package cn.vailing.chunqiu.promethues.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by dream on 2017/1/25.
 */
public class SpUtil {
    private static SharedPreferences sp;
    public static void setBoolean(Context context, String key, boolean value) {
        if(sp==null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean deValue) {
        if(sp==null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
    }
        return sp.getBoolean(key,deValue);
    }

    public static void setString(Context context, String key, String value) {
        if(sp==null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String deValue) {
        if(sp==null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getString(key,deValue);
    }

    public static void remove(Context context, String key) {
        if(sp==null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
}
