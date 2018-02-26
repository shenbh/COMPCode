/**
 * 系统项目名称
 * com.lee.utils
 * SharedPreferencesUtils.java
 * 
 * 2013-5-30-上午10:49:03
 * 
 */
package com.newland.comp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * SharedPreferencesUtils  
 * 
 * Tony Lee
 * 2013-5-30 上午10:49:03
 * 
 * @version 1.0.0
 * 
 */
public class SharedPreferencesUtils
{
	public static  String Name="COMP";
	/**
	 * setConfig设置布尔变量
	 * (这里描述这个方法适用条件 – 可选)
	 * @param context
	 * @param cachName
	 * @param key
	 * @param bl 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	  public  static void setConfigBoolean(Context context,String cachName, String key, Boolean bl)
	    {
	    	SharedPreferences share = context.getSharedPreferences(cachName, Activity.MODE_PRIVATE);
	    	SharedPreferences.Editor  edit = share.edit();
			edit.putBoolean(key, bl); 
			edit.commit();
	    }
	  
	  /**
	   * 
	   * getConfig 获取本地布尔变量 
	   * 没有这个变量 默认false
	   * @param context
	   * @param cachName
	   * @param key
	   * @return 
	   *boolean
	   * @exception 
	   * @since  1.0.0
	   */
	  public  static boolean getConfigBoolean(Context context, String cachName, String key)
	    {
	    	SharedPreferences share = context.getSharedPreferences(cachName, Activity.MODE_PRIVATE);
	    	return  share.getBoolean(key, false); 

	    }
	  
	  
	  public static void setConfigStr(Context context,String cachName, String key, String str)
	    {
	    	SharedPreferences share = context.getSharedPreferences(cachName, Activity.MODE_PRIVATE);
	    	SharedPreferences.Editor  edit = share.edit();
			edit.putString(key, str);
			edit.commit();
	    }
	  
	  
	  public static String getConfigStr(Context context, String cachName, String key)
	    {
	    	SharedPreferences share = context.getSharedPreferences(cachName, Activity.MODE_PRIVATE);
	    	return  share.getString(key, ""); 

	    }
	  
	  
}
