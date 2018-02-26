package com.newland.comp.activity;

import com.newland.comp.service.LoopSysMsgService;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.view.LoadingDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class TabBaseActivity extends BaseActivity {

	private long mExitTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		checkPushService();
	}
	/**
	 * 检查服务是否运行
	 */
	private void checkPushService()
	{
		if(!SharedPreferencesUtils.getConfigBoolean(this, SharedPreferencesUtils.Name, "isClosedPush"))//用户没有关闭推送的话
		{
			if(!isServiceRunning(this, "com.newland.comp.service.LoopSysMsgService"))
			{
				Intent intent = new Intent(this,LoopSysMsgService.class);
				startService(intent);
			}
			
		}
		
	
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "在按一次退出", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
