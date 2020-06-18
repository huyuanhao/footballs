package com.jime.stu.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.blankj.utilcode.util.LogUtils;

import java.lang.reflect.Method;
import java.util.Objects;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * @author Administrator
 * @date 2020/06/17 21:13
 */
public class AppUtils {
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                imei = tm.getDeviceId();
            } else {
                Method method = tm.getClass().getMethod("getImei");
                imei = (String) method.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (imei == null) {
            imei = "";
        }
        return imei;
    }

    public static String getChannel(Context context) {
        String channel = "";
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(
                    context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String value = Objects.requireNonNull(bundle.get("MTA_CHANNEL")).toString();
            channel = value;
            LogUtils.e("MTA_CHANNEL", value);
        } catch (Exception e) {// 忽略找不到包信息的异常
        }
        return channel;
    }
}
