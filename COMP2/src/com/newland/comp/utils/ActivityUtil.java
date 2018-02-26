package com.newland.comp.utils;

import java.util.List;

import com.newland.comp.bean.UserInfo;

import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * 系统常用函数与操作
 * 
 * @author Administrator
 * 
 */
public class ActivityUtil {

	public static String pageRow="10"; //每页显示10行
	/**
	 * 弹出一个对话框（一个按钮）
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param btnName1
	 *            按钮名
	 * @param msg
	 *            弹出信息
	 * @param l1
	 *            监听事件1
	 */
	public static void AlertMessage(Context context, String title, String msg, String btnName1, DialogInterface.OnClickListener l1) {
		if (context != null) {
			Builder builder = new Builder(context);
			if (builder != null) {
				builder.setTitle(title);
				builder.setMessage(msg);
				builder.setCancelable(false);
				builder.setPositiveButton(btnName1, l1);
				builder.create().show();
			}

		}

	}

	/**
	 * 弹出一个对话框（两个按钮）
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param btnName1
	 *            按钮名1
	 * @param btnName2
	 *            按钮名2
	 * @param msg
	 *            弹出信息
	 * @param l1
	 *            监听事件1
	 * @param l2
	 *            监听事件2
	 */
	public static void AlertMessage(Context context, String title, String msg, String btnName1, String btnName2, DialogInterface.OnClickListener l1, DialogInterface.OnClickListener l2) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(btnName1, l1);
		builder.setNegativeButton(btnName2, l2);
		builder.create().show();
	}

	/**
	 * 弹出一个对话框（三个按钮）
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param btnName1
	 *            按钮名1
	 * @param btnName2
	 *            按钮名2
	 * @param btnName3
	 *            按钮名3
	 * @param msg
	 *            弹出信息
	 * @param l1
	 *            监听事件1
	 * @param l2
	 *            监听事件2
	 * @param l3
	 *            监听事件3
	 */
	public static void AlertMessage(Context context, String title, String msg, String btnName1, String btnName2, String btnName3, DialogInterface.OnClickListener l1,
			DialogInterface.OnClickListener l2, DialogInterface.OnClickListener l3) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(btnName1, l1);
		builder.setNeutralButton(btnName2, l2);
		builder.setNegativeButton(btnName3, l3);
		builder.create().show();
	}

	// DO NOTHING ONCLICKLISTENER
	public static DialogInterface.OnClickListener DONOTHINGLISTENER = new DialogInterface.OnClickListener() {

		
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * 滚动到底部
	 * 
	 * @param scroll
	 * @param inner
	 */
	public static void scrollToButtom(final View scroll, final View inner) {

		Handler handler = new Handler();
		handler.post(new Runnable() {

			
			public void run() {
				// TODO Auto-generated method stub
				if (scroll == null || inner == null) {
					return;
				}
				int offset = inner.getMeasuredHeight() - scroll.getHeight();
				if (offset < 0) {
					offset = 0;
				}
				scroll.scrollTo(0, offset);
			}
		});
	}

	/**
	 * 断开蓝牙
	 */
	public static void exitBT() {
		BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mAdapter != null) {
			if (mAdapter.isEnabled()) {
				Log.v("ACTIVITYUTIL", "close BT");
				mAdapter.disable();
			} else {
				Log.v("ACTIVITYUTIL", "蓝牙未开启");
			}
		} else {
			Log.v("ACTIVITYUTIL", "说明没有找到蓝牙硬件或者驱动存在问题");
		}
	}

	/**
	 * 显示下拉框
	 * 
	 * @param context
	 * @param spinner
	 * @param adp
	 * @param adpres
	 */
	public static void showDropDown(Context context, Spinner spinner, String[] adpres) {
		ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, adpres);
		adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adp);
		spinner.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示下拉框 自定义组件
	 * 
	 * @param context
	 * @param spinner
	 * @param adp
	 * @param adpres
	 */
	public static void showDropDown(Context context, Spinner spinner, String[] adpres, int simpleSpinnerItem) {
		ArrayAdapter<String> adp = new ArrayAdapter<String>(context, simpleSpinnerItem, adpres);
		adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adp);
		spinner.setVisibility(View.VISIBLE);

	}
	
	/**
	 * 显示下拉框 自定义组件
	 * 
	 * @param context
	 * @param spinner
	 * @param adp
	 * @param adpres
	 */
	public static void showDropDown(Context context, Spinner spinner, List<String> list, int simpleSpinnerItem) {
		ArrayAdapter<String> adp = new ArrayAdapter<String>(context, simpleSpinnerItem, list);
		adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adp);
		spinner.setVisibility(View.VISIBLE);

	}


	/**
	 * 设置下拉框默认值
	 * 
	 * @param spinner
	 * @param defaultValue
	 * @param Values
	 */
	public static void setDefaultSelection(Spinner spinner, String defaultValue, String[] Values) {
		int position = -1;
		for (int i = 0; i < Values.length; i++) {
			if (Values[i].equals(defaultValue)) {
				position = i;
			}
		}
		if (position != -1) {
			spinner.setSelection(position);
		}
	}

	/**
	 * 隐藏输入框
	 * 
	 * @param context
	 * @param view
	 */
	public static void hiddenInput(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 显示长TOAST通知
	 * 
	 * @param context
	 * @param msg
	 */
	public static void ShowToastLong(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 显示短TOAST通知
	 * 
	 * @param context
	 * @param msg
	 */
	public static void ShowToastShort(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void callTEL(Context context, String telNUM) {
		Intent i = new Intent("android.intent.action.CALL", Uri.parse("tel:" + telNUM));
		context.startActivity(i);

	}

	/**
	 * 
	 * @param strs
	 * @return
	 */
	public static String[] getArray(String[][] strs) {
		String[] str = new String[strs.length];
		for (int i = 0; i < str.length; i++) {
			str[i] = strs[i][1];
		}
		return str;
	}

	public static String getArrayValue(String[][] strs, String key) {
		String str = "";
		for (int i = 0; i < strs.length; i++) {
			if (strs[i][0].equalsIgnoreCase(key)) {
				str = strs[i][1];
				break;
			}
		}
		return str;
	}

	/**
	 * 获取视图高
	 * 
	 * @param view
	 * @return
	 */
	public static int getViewHeight(View view) {
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(width, height);
		int height2 = view.getMeasuredHeight();
		return height2;

	}

	public static int getVieWidth(View view) {
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(width, height);
		int width2 = view.getMeasuredWidth();
		return width2;

	}

}
