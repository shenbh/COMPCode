package com.newland.comp.activity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.newland.comp.activity.hr.HrActivity;
import com.newland.comp.activity.indicator.IndicatorActivity;
import com.newland.comp.activity.more.MoreActivity;
import com.newland.comp.activity.my.MySelfActivity;
import com.newland.comp.activity.process.ProcessActivity;

import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.UserInfo;
import com.newland.comp.common.AppContext;
import com.newland.comp.utils.AnimCommon;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 主页TabHost 主页
 * 
 * @author H81
 *
 */
public class MenuTabActivity extends TabActivity {

	Intent homeIntent;// 首页
	Intent hrIntent;// 人资
	Intent quoIntent;// 指标
	Intent processIntent;// 流程
	Intent moreIntent;// 流程
	public static TabHost mTabHost;

	public static String MY = "我的";
	public static String HUMAN_RESOURCES = "人资";
	public static String QUOTA = "指标";
	public static String PROCESS = "流程";
	public static String MORE = "更多";

	ImageView image1, image2, image3, image4, image5;
	TextView textView1, textView2, textView3, textView4, textView5;
	final int COLOR_Green = Color.parseColor("#8dc21f");
	final int COLOR_Gray = Color.parseColor("#6e6e6e");
	private UserInfo userInfo;

	private TextView mUnred_num;//流程未读数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tabhost_menu);
		AppContext.getInstance().addActivity(this);
		initView();
		preperIntent();
		setupIntent();
		AppContext.getInstance().tempMenuActivity = this;
		checkPop();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		if(AnimCommon.in!=0 && AnimCommon.out!=0){
			super.overridePendingTransition(AnimCommon.in, AnimCommon.out);
			AnimCommon.clear();
			}
		super.onPause();

	}

	private void initView() {
		image1 = (ImageView) findViewById(R.id.imageView1);
		image2 = (ImageView) findViewById(R.id.imageView2);
		image3 = (ImageView) findViewById(R.id.imageView3);
		image4 = (ImageView) findViewById(R.id.imageView4);
		image5 = (ImageView) findViewById(R.id.imageView5);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		textView4 = (TextView) findViewById(R.id.textView4);
		textView5 = (TextView) findViewById(R.id.textView5);
		userInfo = (UserInfo) getIntent().getSerializableExtra("userinfo");
		mUnred_num=(TextView) findViewById(R.id.unred_num);
	}

	private void preperIntent() {
		homeIntent = new Intent(this, MySelfActivity.class);
		homeIntent.putExtra("userinfo", userInfo);
		hrIntent = new Intent(this, HrActivity.class);
		hrIntent.putExtra("userinfo", userInfo);
		quoIntent = new Intent(this, IndicatorActivity.class);
		quoIntent.putExtra("userinfo", userInfo);
		processIntent = new Intent(this, ProcessActivity.class);
		processIntent.putExtra("userinfo", userInfo);
		moreIntent = new Intent(this, MoreActivity.class);
		moreIntent.putExtra("userinfo", userInfo);
	}

	private void setupIntent() {
		mTabHost = getTabHost();
		mTabHost.addTab(buildTabSpec(MY, MY, R.drawable.traf, homeIntent));
		mTabHost.addTab(buildTabSpec(HUMAN_RESOURCES, HUMAN_RESOURCES, R.drawable.traf, hrIntent));
		mTabHost.addTab(buildTabSpec(QUOTA, QUOTA, R.drawable.traf, quoIntent));
		mTabHost.addTab(buildTabSpec(PROCESS, PROCESS, R.drawable.traf, processIntent));
		mTabHost.addTab(buildTabSpec(MORE, MORE, R.drawable.traf, moreIntent));

	}

	private TabHost.TabSpec buildTabSpec(String tag, String Label, int resIcon, final Intent content) {
		return mTabHost.newTabSpec(tag).setIndicator(Label, getResources().getDrawable(resIcon)).setContent(content);
	}

	public static void setCurrentTabByTag(String tab) {
		mTabHost.setCurrentTabByTag(tab);
	}

	/**
	 * 导航条按钮监听 btn_tab(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param view
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void btn_tab(View view) {

		textView1.setTextColor(COLOR_Gray);
		textView2.setTextColor(COLOR_Gray);
		textView3.setTextColor(COLOR_Gray);
		textView4.setTextColor(COLOR_Gray);
		textView5.setTextColor(COLOR_Gray);
		int checkedId = view.getId();

		switch (checkedId) {
		case R.id.home_layout:
			image1.setImageResource(R.drawable.my_pre);
			image2.setImageResource(R.drawable.hr_nor);
			image3.setImageResource(R.drawable.indicator_nor);
			image4.setImageResource(R.drawable.process_nor);
			image5.setImageResource(R.drawable.more_nor);
			textView1.setTextColor(COLOR_Green);

			mTabHost.setCurrentTabByTag(MY);
			break;

		case R.id.hr_layout:
			image2.setImageResource(R.drawable.hr_pre);
			image1.setImageResource(R.drawable.my_nor);
			image3.setImageResource(R.drawable.indicator_nor);
			image4.setImageResource(R.drawable.process_nor);
			image5.setImageResource(R.drawable.more_nor);
			textView2.setTextColor(COLOR_Green);

			mTabHost.setCurrentTabByTag(HUMAN_RESOURCES);
			break;

		case R.id.indi_layout:
			
			if(!userInfo.getMenuid().contains("18600001")) //如果没有这个菜单权限提示不能进入
			{
				Toast.makeText(getApplicationContext(), "您没有该权限！", 1500).show();
				return;
			}
			image3.setImageResource(R.drawable.indicator_pre);
			image1.setImageResource(R.drawable.my_nor);
			image2.setImageResource(R.drawable.hr_nor);
			image4.setImageResource(R.drawable.process_nor);
			image5.setImageResource(R.drawable.more_nor);
			textView3.setTextColor(COLOR_Green);

			mTabHost.setCurrentTabByTag(QUOTA);
			break;

		case R.id.process_layout:
			image4.setImageResource(R.drawable.process_pre);
			image1.setImageResource(R.drawable.my_nor);
			image2.setImageResource(R.drawable.hr_nor);
			image3.setImageResource(R.drawable.indicator_nor);
			image5.setImageResource(R.drawable.more_nor);
			textView4.setTextColor(COLOR_Green);

			mTabHost.setCurrentTabByTag(PROCESS);
			break;

		case R.id.more_layout:
			image5.setImageResource(R.drawable.more_pre);
			image1.setImageResource(R.drawable.my_nor);
			image2.setImageResource(R.drawable.hr_nor);
			image3.setImageResource(R.drawable.indicator_nor);
			image4.setImageResource(R.drawable.process_nor);
			textView5.setTextColor(COLOR_Green);

			mTabHost.setCurrentTabByTag(MORE);
			break;

		default:
			break;
		}
	}

	/**
	 * 跳转标签页
	 * 
	 * @param tag
	 */
	public static void turnTag(String tag) {
		mTabHost.setCurrentTabByTag(tag);

	}
	/**
	 * 检查服务是否运行
	 */
	private void checkPop()
	{
		//检查有没冒泡提示
		String numstr = SharedPreferencesUtils.getConfigStr(getApplicationContext(), SharedPreferencesUtils.Name, "pro_unred_num");
		int num = Integer.parseInt(StringUtil.noNull(numstr,"0"));
		if (num > 0) {
			mUnred_num.setVisibility(View.VISIBLE);
			mUnred_num.setText("");
		}else 
		if (num==0) {
			mUnred_num.setVisibility(View.GONE);
		}
	}
	
}
