package com.jime.stu.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * @author PC
 * @date 2020/06/01 18:15
 */
public class ClipeBoardUtil {
    /**
     * 获取剪切板里内容
     * @param context
     * @return
     */
    public static String getClipeBoardContent(Context context){
        ClipboardManager clipboardManager= (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData primaryClip = clipboardManager.getPrimaryClip();
        String content=null;
        if (primaryClip!=null&&primaryClip.getItemCount()>0){
            ClipData.Item itemAt = primaryClip.getItemAt(0);
            if (itemAt!=null){
                content=itemAt.getText().toString();
            }
        }
        return content;
    }
    /**
     * 放置内容发到剪切板
     */
    public static void setClipeBoardContent(Context context, String content){
        ClipboardManager clipboardManager= (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData primaryClip = ClipData.newPlainText("Label",content);//纯文本内容
        clipboardManager.setPrimaryClip(primaryClip);
    }
}