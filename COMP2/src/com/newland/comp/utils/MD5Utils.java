package com.newland.comp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	 // 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private static MD5Utils instance=null;
    public static MD5Utils getInstance(){
        if(instance==null){
            synchronized(MD5Utils.class){
                if(instance==null){
                    instance=new MD5Utils();
                }
            }
        }
        return instance;
    }
    private MD5Utils(){}

    // 返回形式为数字跟字符串
    private  String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 返回形式只为数字
    private  String byteToNum(byte bByte) {
        int iRet = bByte;
        System.out.println("iRet1=" + iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }

    // 转换字节数组为16进制字串
    private  String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public  String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
    
    
    /**
     * 获取  MD5(userid+'comp') 格式加密的值
     * @return
     */
    public String getUserIdSignid(String userid)
    {
		return GetMD5Code(userid+"comp");
    }
    
    /**
     * 获取  MD5(pwd+'comp') 格式加密的值
     * @return
     */
    public String getUserPwdSignid(String pwd)
    {
		return GetMD5Code(pwd+"comp");
    }
    
    

    public static void main(String[] args) {
    	MD5Utils getMD5 = new MD5Utils();
        System.out.println(getMD5.getUserIdSignid("29790"));
    }

}
