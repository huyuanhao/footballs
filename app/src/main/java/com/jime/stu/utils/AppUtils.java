package com.jime.stu.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * @author Administrator
 * @date 2020/06/17 21:13
 */
public class AppUtils {
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context){
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                imei = tm.getDeviceId();
            }else {
                Method method = tm.getClass().getMethod("getImei");
                imei = (String) method.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }
}