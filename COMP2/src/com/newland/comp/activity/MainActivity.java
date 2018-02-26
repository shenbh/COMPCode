package com.newland.comp.activity;

import com.newland.comp.activity.my.SystemSettingActivity;
import com.newland.comp.test.TestActivity;
import com.newland.comp.view.ExitDialog;
import com.newland.comp2.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startActivity(new Intent(this, SystemSettingActivity.class));
	}

	ExitDialog dialog;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			ExitDialog dialog = ExitDialog.show(this, "您确定要退出？", true);
			return true;
		}
		return true;
	}
}
