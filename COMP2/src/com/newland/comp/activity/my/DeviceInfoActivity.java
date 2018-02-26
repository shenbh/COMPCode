package com.newland.comp.activity.my;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 本机信息
 * 
 * @author H81
 *
 */
public class DeviceInfoActivity extends BaseActivity {
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.systemsetting_deviceinfo);
		initView();
		intent = getIntent();
		String versionname = intent.getStringExtra("versionname");
		String versioncode = intent.getStringExtra("versioncode");
		String clientversioncode = intent.getStringExtra("clientversioncode");
		setmobileinfo( versionname, versioncode, clientversioncode);
	}

	private void initView() {
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		TextView title = (TextView) findViewById(R.id.head_center_title);
		left_btn.setVisibility(View.VISIBLE);
		right_btn.setVisibility(View.GONE);
		left_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				DeviceInfoActivity.this.finish();
			}
		});
		title.setText("关于本机信息");
	}

	/**
	 * 设置本机信息与对应控件上
	 * 
	 * @param struserid2
	 * @param sysVersionName2
	 * @param sysVersionCode2
	 * @param clientVersionCode2
	 */
	public void setmobileinfo( String sysVersionName2, String sysVersionCode2, String clientVersionCode2) {
		TextView versionname = (TextView) findViewById(R.id.sys_versionname_content);
		TextView versioncode = (TextView) findViewById(R.id.sys_versioncode_content);
		TextView clientversioncode = (TextView) findViewById(R.id.sys_clientversiontcode_content);
		versionname.setText(sysVersionName2);
		versioncode.setText(sysVersionCode2);
		clientversioncode.setText(clientVersionCode2);
	}
}
