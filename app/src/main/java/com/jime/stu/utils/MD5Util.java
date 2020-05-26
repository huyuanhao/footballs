package com.jime.stu.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密
 */
public class MD5Util {
    
    /**
     * 加密公共方法    
     * @Title: toMD5    
     * @param str 
     * @return String 返回值
     */
    public static String toMD5(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        return bytesToHexString(getDigest(str.getBytes()));
    }
    
    /**
     * 转16进制字符 
     * @param data 
     * @return String 返回值
     */
    public static String bytesToHexString(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int t = data[i];
            sb.append(Integer.toString(t >> 4 & 0xF, 16)).append(Integer.toString(t & 0xF, 16));
        }
        String ret = sb.toString();
        return ret.toLowerCase();
    }
    
    /**
     * 
     * md5加密 
     * @param data 
     * @return byte[] 返回值
     */
    public static byte[] getDigest(byte[] data) {
        byte[] dgt = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
        md.reset();
        md.update(data);
        dgt = md.digest();
        return dgt;
    }
    
    /**
     * MD5加密字符串32位小写
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5=new BigInteger(1, md.digest()).toString(16);
            //BigInteger会把0省略掉，需补全至32位
            return fillMD5(md5);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密错误:"+e.getMessage(),e);
        }
    }

    private static String fillMD5(String md5){
        return md5.length()==32?md5:fillMD5("0"+md5);
    }
    
    /**
     * MD5普通加密
     * @param key
     * @return
     */
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * TODO(这里用一句话描述这个方法的作用)    
     * @Title: main    
     * @param args void 返回值
     */
    public static void main(String[] args) {
//    	String ss="e10adc3949ba59abbe56e057f20f883e";
    	
    	
    	/*System.out.println(getMD5("jyyw123456!"));//产品
    	System.out.println(getMD5("jyqd123456@"));//渠道
    	System.out.println(getMD5("jy654321,"));//admin
    	System.out.println(getMD5("jy123456qq"));//报表
    	System.out.println(getMD5("chenyc654321"));//陈映慈
    	System.out.println(getMD5("liucaizhen1114"));//刘彩珍
    	System.out.println(getMD5("gaosong4321"));//高松
    	System.out.println(getMD5("zhouqiaoyi1234"));//周巧怡
*/    	System.out.println(getMD5("guoyajie1212,"));//郭雅洁
    	System.out.println(getMD5("hongkaixin1212."));//洪凯欣
    	System.out.println(getMD5("tianrongfei1212;"));//田荣飞
    }
    
}
