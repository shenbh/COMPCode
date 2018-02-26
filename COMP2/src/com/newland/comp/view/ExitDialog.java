package com.newland.comp.view;


import com.newland.comp2.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 带有2个按钮的对话框
 * 
 * TwoButtonDialog
 * 
 * Tony Lee
 * http://weibo.com/totome
 * 2013-7-15 下午5:17:41
 * 
 * @version 1.0.0
 *
 */
public class ExitDialog extends Dialog
{
	Activity activity;

	public ExitDialog(Context context, int theme)
	{
		super(context, theme);
		
	}

	public void setMessage(String msg)
	{
		TextView textView = (TextView) findViewById(R.id.tv);
		textView.setText(msg);
	}
	
	public void setLinster()
	{
		Button b1 = (Button) findViewById(R.id.one);
		Button b2 = (Button) findViewById(R.id.two);
		b1.setOnClickListener(new View.OnClickListener()
		{
			
			
			public void onClick(View v)
			{
				activity.finish();
				dismiss();
			}
		});
		b2.setOnClickListener(new View.OnClickListener()
		{

			
			public void onClick(View v)
			{
				dismiss();
				
			}
			
		});
	}
	
	/**
	 * 进度�?
	 * @param context		Activity
	 * @param msg			提示信息
	 * @param cancelable	是否点击关闭		true 关闭	false 不关�?
	 * one  two  两个按钮的监听
	 * @return
	 */
	public static ExitDialog show(Activity activity, String msg, boolean cancelable){
		
		ExitDialog dialog = new ExitDialog(activity, R.style.theme_dialog_alert);
		try {
			dialog.activity = activity;
	    	dialog.setContentView(R.layout.exit_window_layout);
	    	dialog.setCancelable(cancelable);
	    	dialog.setMessage(msg);
	    	dialog.setLinster();
	    	dialog.show();
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return dialog;
	}

}
