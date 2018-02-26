package com.newland.comp.activity.more;

import com.newland.comp.activity.TabBaseActivity;
import com.newland.comp2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 更多界面
 * 
 * @author H81
 *
 */
public class MoreActivity extends TabBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		setTitle();
	}
	/**
	 * 设置标题栏
	 */
	private void setTitle(){
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		TextView right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("更多");
		if (left_btn != null) {// 返回
			left_btn.setVisibility(View.GONE);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {// 日期
			right_tv.setVisibility(View.GONE);
		}
	}
	
	/**员工心声
	 * @param view
	 */
	public void staffaspirations_click(View view)
	{
		Intent intent=new Intent(getApplicationContext(),MoreStaffAspActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
	}
}
