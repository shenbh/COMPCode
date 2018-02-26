package com.newland.comp.activity.hr;

import com.newland.comp.activity.TabBaseActivity;
import com.newland.comp2.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 底部菜单 人资
 * 
 * @author H81
 *
 */
public class HrActivity extends TabBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hr);
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
			center_tv.setText("人资");
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
	/**请假申请
	 * @param view
	 */
	public void vacate_click(View view)
	{
		Intent intent=new Intent(getApplicationContext(),HrVacateApplyActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
	}
	/**离职申请
	 * @param view
	 */
	public void leave_click(View view)
	{
		Intent intent=new Intent(getApplicationContext(),HrLeaveOfficeActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
	}
	/**考勤报备
	 * @param view
	 */
	public void filing_click(View view)
	{
		Intent intent=new Intent(getApplicationContext(),HrFilingActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
	}
	/**培训报名
	 * @param view
	 */
	public void train_click(View view)
	{
	}
	/**积分商城
	 * @param view
	 */
	public void shop_click(View view)
	{
		Intent intent=new Intent(getApplicationContext(),HrIntegralShopActivity.class);
		startActivity(intent);
	}
}
