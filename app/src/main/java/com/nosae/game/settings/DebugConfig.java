package com.nosae.game.settings;

/**
 * Created by eason on 2015/10/15.
 */
import android.util.Log;

public class DebugConfig {
    // Debug switch
    public static final boolean isDebugOn = true;
    public static final boolean isFpsDebugOn = false;
    public static String TAG = "PoPo";
//    public static final boolean DEBUG_OTHER = isDebugOn && true;

    public static void setTag(String tag) {
        TAG = tag;
    }

    public static boolean d(String msg) {
        if (isDebugOn) {
            Log.d(TAG, msg);
        }
        return false;
    }

    public static void d(String TAG, String msg) {
        if (isDebugOn) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String TAG, String msg, Throwable tr) {
        if (isDebugOn) {
            Log.d(TAG, msg, tr);
        }
    }

    public static boolean e(String msg) {
        if (isDebugOn) {
            Log.e(TAG, msg);
        }
        return false;
    }

    public static void e(String TAG, String msg) {
        if (isDebugOn) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String TAG, String msg, Throwable tr) {
        if (isDebugOn) {
            Log.e(TAG, msg, tr);
        }
    }
}
