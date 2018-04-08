package com.basha.karim.baking.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by karim on 4/7/2018.
 */

public class ToastUtils {

    private static Toast mToast;

    public ToastUtils() {

    }

    public static void createToast(Context context, String message, int length) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context,message,length);
        mToast.show();
    }

}
