package com.newland.comp.activity.my;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.my.MyPagerAdapter;
import com.newland.comp.adapter.my.MyViewPageAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.UserInfo;
import com.newland.comp.bean.my.MyInfo;
import com.newland.comp.bean.my.MyInfoMationKeyValue;
import com.newland.comp.bean.process.ProcessKeyValue;
import com.newland.comp.common.AppContext;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 我的资料界面
 * 
 * @author H81
 * 
 */
public class MyInformationActivity extends Activity {
	ViewPager viewPager;// viewPaper对象
	LocalActivityManager manager = null;

	int currIndex = 0;// 当前页卡编号
	int offset;// 动画图片偏移量
	int bmpW;// 图片宽度

	View view_baseInfo;// 基本信息
	View view_jobInfo;// 职位信息
	View view_commuInfo;// 通讯信息
	View view_eduInfo;// 学历信息
	View view_practiceInfo;// 实习信息
	View view_worknumberInfo;// 工号信息
	View view_areaInfo;// 区域信息
	View view_productInfo;// 生产信息
	private TextView myinfo_tv_baseinfo;// 基本信息
	private TextView myinfo_tv_worknumberinfo;// 工号信息
	private TextView myinfo_tv_jobinfo;// 职位信息
	private TextView myinfo_tv_commuinfo;// 通讯信息
	private TextView myinfo_tv_eduinfo;// 学历信息
	private TextView myinfo_tv_practiceinfo;// 实习信息
	private TextView myinfo_tv_areainfo;// 区域信息
	private TextView myinfo_tv_productinfo;// 产品信息
	private ImageView myinfo_tv_baseinfo_line;// 基本信息
	private ImageView myinfo_tv_worknumberinfo_line;// 工号信息
	private ImageView myinfo_tv_jobinfo_line;// 职位信息
	private ImageView myinfo_tv_commuinfo_line;// 通讯信息
	private ImageView myinfo_tv_eduinfo_line;// 学历信息
	private ImageView myinfo_tv_practiceinfo_line;// 实习信息
	private ImageView myinfo_tv_areainfo_line;// 区域信息
	private ImageView myinfo_tv_productinfo_line;// 产品信息
	List<View> views;

	private UserInfo userInfo;
	private MyInfo myInfo = null;
	// 基本信息
	private TextView mMyinfo_userid;// 员工编号
	private TextView mMyinfo_name;// 姓名
	private TextView mMyinfo_sex;// 性别
	private TextView mMyinfo_nation;// 民族
	private TextView mMyinfo_birth;// 出生日期
	private TextView mMyinfo_npersonnel;// 人员性质
	private TextView mMyinfo_offices;// 科室
	private TextView mMyifo_jointime;// 参加本单位时间
	private TextView mMyifo_political;// 政治面貌
	private TextView mMyinfo_type;// 员工类型
	private TextView mMyinfo_belong;// 归属
	private TextView mMyinfo_native_place;// 籍贯
	private TextView mMyifo_health;// 健康状况
	private TextView mMyinfo_marital_status;// 婚姻状况
	// 职位信息
	private TextView mMyinfo_jobname;// 职位名称
	private TextView mMyinfo_standardrank;// 标准职称
	private TextView mMyinfo_currentrank;// 现职称
	private TextView mMyinfo_qualificationflag;// 量化标识
	private TextView mMyinfo_jobtime;// 新工定岗时间
	// 通讯信息
	private TextView mMyinfo_mobilephone;// 移动电话
	private TextView mMyinfo_email;// 邮箱
	// 学历信息
	private TextView mMyinfo_school;// 毕业学校
	private TextView mMyinfo_profession;// 所学专业
	private TextView mMyinfo_edu;// 学历
	private TextView mMyinfo_edutype;// 学习形式
	// 实习信息
	private TextView mMyinfo_internship_start;// 实习开始时间
	private TextView mMyinfo_internship_end;// 实习结束时间
	private TextView mMyinfo_newWorkCount;// 新工期数
	private TextView mMyinfo_specialty;// 特长
	// 工号信息
	private LinearLayout mLayout_daiban;//
	// 区域信息
	private TextView mMyinfo_quyu; // 区域
	private TextView mMyinfo_areatime;// 入域时间
	private TextView mMyinfo_banzu;// 班组
	private TextView mMyinfo_workaddr;// 办公地点
	// 其他信息
	private TextView mMyinfo_mainskillline; // 主技能线
	private TextView mMyinfo_isfulltime;// 是否全职
	private TextView mMyinfo_alinemark;// 一线标识
	private TextView mMyinfo_workduty;// 工作制

	private View load_msg;

	List<MyInfoMationKeyValue> listKeyValue;// 工号信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.my_information);
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		userInfo = (UserInfo) getIntent().getSerializableExtra("userinfo");
		initTitle();
		initTab();
		initViewPaper();
		getMyInfo(userInfo);
	}

	@Override
	protected void onPause() {
		super.onPause();
		manager.dispatchPause(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		manager.dispatchResume();
	}

	/**
	 * 初始化ViewPaper
	 */
	private void initViewPaper() {
		viewPager = (ViewPager) findViewById(R.id.myinfo_viewPager);
		views = new ArrayList<View>();

		LayoutInflater layoutInflater = getLayoutInflater();
		view_baseInfo = layoutInflater.inflate(R.layout.my_information_baseinfo, null);// 基本信息
		view_jobInfo = layoutInflater.inflate(R.layout.my_information_job, null);// 职位信息
		view_commuInfo = layoutInflater.inflate(R.layout.my_information_communication, null);// 通讯信息
		view_eduInfo = layoutInflater.inflate(R.layout.my_informaiton_education, null);// 学历信息
		view_practiceInfo = layoutInflater.inflate(R.layout.my_infomation_practice, null);// 实习信息
		view_worknumberInfo = layoutInflater.inflate(R.layout.my_information_worknumber, null);// 工号信息
		view_areaInfo = layoutInflater.inflate(R.layout.my_infomation_area, null);// 区域信息
		view_productInfo = layoutInflater.inflate(R.layout.my_infomation_product, null);// 产品信息

		views.add(view_baseInfo);
		views.add(view_jobInfo);
		views.add(view_commuInfo);
		views.add(view_eduInfo);
		views.add(view_practiceInfo);
		views.add(view_worknumberInfo);
		views.add(view_areaInfo);
		views.add(view_productInfo);

		viewPager.setAdapter(new MyViewPageAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setOffscreenPageLimit(0);
		load_msg = findViewById(R.id.load_msg);
		initbaseView();
		initJobView();
		initCommuView();
		initEduView();
		initPracticeView();
		initWorknumView();
		initAreaView();
		initOtherInfo();
	}

	/**
	 * 初始化基本信息控件
	 */
	private void initbaseView() {
		mMyinfo_userid = (TextView) view_baseInfo.findViewById(R.id.myinfo_userid);
		mMyinfo_name = (TextView) view_baseInfo.findViewById(R.id.myinfo_name);
		mMyinfo_sex = (TextView) view_baseInfo.findViewById(R.id.myinfo_sex);
		mMyinfo_nation = (TextView) view_baseInfo.findViewById(R.id.myinfo_nation);
		mMyinfo_birth = (TextView) view_baseInfo.findViewById(R.id.myinfo_birth);
		mMyinfo_npersonnel = (TextView) view_baseInfo.findViewById(R.id.myinfo_npersonnel);
		mMyinfo_offices = (TextView) view_baseInfo.findViewById(R.id.myinfo_offices);
		mMyifo_jointime = (TextView) view_baseInfo.findViewById(R.id.myifo_jointime);
		mMyifo_political = (TextView) view_baseInfo.findViewById(R.id.myifo_political);
		mMyinfo_type = (TextView) view_baseInfo.findViewById(R.id.myinfo_type);
		mMyinfo_belong = (TextView) view_baseInfo.findViewById(R.id.myinfo_belong);
		mMyinfo_native_place = (TextView) view_baseInfo.findViewById(R.id.myinfo_native_place);
		mMyifo_health = (TextView) view_baseInfo.findViewById(R.id.myinfo_health);
		mMyinfo_marital_status = (TextView) view_baseInfo.findViewById(R.id.myinfo_marital_status);
		mMyinfo_specialty = (TextView) view_baseInfo.findViewById(R.id.myinfo_specialty);
		mMyinfo_specialty.setMovementMethod(new ScrollingMovementMethod());
	}

	/**
	 * 初始化职位信息控件
	 */
	private void initJobView() {
		mMyinfo_jobname = (TextView) view_jobInfo.findViewById(R.id.myinfo_jobname);
		mMyinfo_standardrank = (TextView) view_jobInfo.findViewById(R.id.myinfo_standardrank);
		mMyinfo_currentrank = (TextView) view_jobInfo.findViewById(R.id.myinfo_currentrank);
		mMyinfo_qualificationflag = (TextView) view_jobInfo.findViewById(R.id.myinfo_qualificationflag);
		mMyinfo_jobtime = (TextView) view_jobInfo.findViewById(R.id.myinfo_jobtime);
	}

	/**
	 * 初始化通讯信息控件
	 */
	private void initCommuView() {
		mMyinfo_mobilephone = (TextView) view_commuInfo.findViewById(R.id.myinfo_mobilephone);
		mMyinfo_email = (TextView) view_commuInfo.findViewById(R.id.myinfo_email);
	}

	/**
	 * 初始化学历信息控件
	 */
	private void initEduView() {
		mMyinfo_school = (TextView) view_eduInfo.findViewById(R.id.myinfo_school);
		mMyinfo_profession = (TextView) view_eduInfo.findViewById(R.id.myinfo_profession);
		mMyinfo_edu = (TextView) view_eduInfo.findViewById(R.id.myinfo_edu);
		mMyinfo_edutype = (TextView) view_eduInfo.findViewById(R.id.myinfo_edutype);
	}

	/**
	 * 初始化实习信息控件
	 */
	private void initPracticeView() {
		mMyinfo_internship_start = (TextView) view_practiceInfo.findViewById(R.id.myinfo_internship_start);
		mMyinfo_internship_end = (TextView) view_practiceInfo.findViewById(R.id.myinfo_internship_end);
		mMyinfo_newWorkCount = (TextView) view_practiceInfo.findViewById(R.id.myinfo_newworkcount);
	}

	/**
	 * 初始化工号信息控件
	 */
	private void initWorknumView() {
		mLayout_daiban = (LinearLayout) view_worknumberInfo.findViewById(R.id.layout_daiban);
	}

	/**
	 * 初始化区域信息控件
	 */
	private void initAreaView() {
		mMyinfo_quyu = (TextView) view_areaInfo.findViewById(R.id.myinfo_area_quyu);
		mMyinfo_areatime = (TextView) view_areaInfo.findViewById(R.id.myinfo_areatime);
		mMyinfo_banzu = (TextView) view_areaInfo.findViewById(R.id.myinfo_banzu);
		mMyinfo_workaddr = (TextView) view_areaInfo.findViewById(R.id.myinfo_workaddr);
	}

	/**
	 * 初始化其他信息控件
	 */
	private void initOtherInfo() {
		mMyinfo_mainskillline = (TextView) view_productInfo.findViewById(R.id.myinfo_mainskillline);
		mMyinfo_isfulltime = (TextView) view_productInfo.findViewById(R.id.myinfo_isfulltime);
		mMyinfo_alinemark = (TextView) view_productInfo.findViewById(R.id.myinfo_alinemark);
		mMyinfo_workduty = (TextView) view_productInfo.findViewById(R.id.myinfo_workduty);
	}

	/**
	 * 通过activity获取视图
	 * 
	 * @param id
	 * @param intent
	 * @return
	 */
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1-->页卡2 偏移量
		int two = offset * 2;// 页卡2-->页卡3 偏移量

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 页卡切换监听
		 */

		public void onPageSelected(int position) {
			Animation animation = new TranslateAnimation(one * currIndex, one * position, 0, 0);
			currIndex = position;
			setTextColor(currIndex);
		}
	}

	/**
	 * 初始化 四个tab标签
	 */
	private void initTab() {
		myinfo_tv_baseinfo = (TextView) findViewById(R.id.myinfo_tv_baseinfo);// 基本信息
		myinfo_tv_worknumberinfo = (TextView) findViewById(R.id.myinfo_tv_worknumberinfo);// 工号信息
		myinfo_tv_jobinfo = (TextView) findViewById(R.id.myinfo_tv_jobinfo);// 职位信息
		myinfo_tv_commuinfo = (TextView) findViewById(R.id.myinfo_tv_commuinfo);// 通讯信息
		myinfo_tv_eduinfo = (TextView) findViewById(R.id.myinfo_tv_eduinfo);// 学历信息
		myinfo_tv_practiceinfo = (TextView) findViewById(R.id.myinfo_tv_practiceinfo);
		myinfo_tv_areainfo = (TextView) findViewById(R.id.myinfo_tv_areainfo);
		myinfo_tv_productinfo = (TextView) findViewById(R.id.myinfo_tv_productinfo);
		myinfo_tv_baseinfo_line = (ImageView) findViewById(R.id.myinfo_tv_baseinfo_line);
		myinfo_tv_worknumberinfo_line = (ImageView) findViewById(R.id.myinfo_tv_worknumberinfo_line);
		myinfo_tv_jobinfo_line = (ImageView) findViewById(R.id.myinfo_tv_jobinfo_line);
		myinfo_tv_commuinfo_line = (ImageView) findViewById(R.id.myinfo_tv_commuinfo_line);
		myinfo_tv_eduinfo_line = (ImageView) findViewById(R.id.myinfo_tv_eduinfo_line);
		myinfo_tv_practiceinfo_line = (ImageView) findViewById(R.id.myinfo_tv_practiceinfo_line);
		myinfo_tv_areainfo_line = (ImageView) findViewById(R.id.myinfo_tv_areainfo_line);
		myinfo_tv_productinfo_line = (ImageView) findViewById(R.id.myinfo_tv_productinfo_line);
		clearPress();
		myinfo_tv_baseinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
		myinfo_tv_baseinfo_line.setVisibility(View.VISIBLE);
		myinfo_tv_baseinfo.setOnClickListener(new MyOnClick(0));
		myinfo_tv_jobinfo.setOnClickListener(new MyOnClick(1));
		myinfo_tv_commuinfo.setOnClickListener(new MyOnClick(2));
		myinfo_tv_eduinfo.setOnClickListener(new MyOnClick(3));
		myinfo_tv_practiceinfo.setOnClickListener(new MyOnClick(4));
		myinfo_tv_worknumberinfo.setOnClickListener(new MyOnClick(5));
		myinfo_tv_areainfo.setOnClickListener(new MyOnClick(6));
		myinfo_tv_productinfo.setOnClickListener(new MyOnClick(7));
	}

	private void clearPress() {
		myinfo_tv_baseinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
		myinfo_tv_jobinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
		myinfo_tv_commuinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
		myinfo_tv_eduinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
		myinfo_tv_practiceinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
		myinfo_tv_worknumberinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
		myinfo_tv_areainfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
		myinfo_tv_productinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
		myinfo_tv_baseinfo_line.setVisibility(View.INVISIBLE);
		myinfo_tv_jobinfo_line.setVisibility(View.INVISIBLE);
		myinfo_tv_commuinfo_line.setVisibility(View.INVISIBLE);
		myinfo_tv_eduinfo_line.setVisibility(View.INVISIBLE);
		myinfo_tv_practiceinfo_line.setVisibility(View.INVISIBLE);
		myinfo_tv_worknumberinfo_line.setVisibility(View.INVISIBLE);
		myinfo_tv_areainfo_line.setVisibility(View.INVISIBLE);
		myinfo_tv_productinfo_line.setVisibility(View.INVISIBLE);
	}

	/**
	 * 设置背景色
	 */
	private void setTextColor(int index) {
		switch (index) {
		case 0:// 基本信息
			myinfo_tv_baseinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
			myinfo_tv_jobinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_commuinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_eduinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_practiceinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_worknumberinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_areainfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_productinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_baseinfo_line.setVisibility(View.VISIBLE);
			myinfo_tv_jobinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_commuinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_eduinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_practiceinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_worknumberinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_areainfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_productinfo_line.setVisibility(View.INVISIBLE);
			break;
		case 1:// 职位信息
			myinfo_tv_baseinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_jobinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
			myinfo_tv_commuinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_eduinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_practiceinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_worknumberinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_areainfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_productinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_baseinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_jobinfo_line.setVisibility(View.VISIBLE);
			myinfo_tv_commuinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_eduinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_practiceinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_worknumberinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_areainfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_productinfo_line.setVisibility(View.INVISIBLE);
			break;
		case 2:// 通讯信息
			myinfo_tv_baseinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_jobinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_commuinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
			myinfo_tv_eduinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_practiceinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_worknumberinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_areainfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_productinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_baseinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_jobinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_commuinfo_line.setVisibility(View.VISIBLE);
			myinfo_tv_eduinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_practiceinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_worknumberinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_areainfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_productinfo_line.setVisibility(View.INVISIBLE);
			break;
		case 3:// 学历信息
			myinfo_tv_baseinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_jobinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_commuinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_eduinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
			myinfo_tv_practiceinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_worknumberinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_areainfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_productinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_baseinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_jobinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_commuinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_eduinfo_line.setVisibility(View.VISIBLE);
			myinfo_tv_practiceinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_worknumberinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_areainfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_productinfo_line.setVisibility(View.INVISIBLE);
			break;
		case 4:// 实习信息
			myinfo_tv_baseinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_jobinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_commuinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_eduinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_practiceinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
			myinfo_tv_worknumberinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_areainfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_productinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_baseinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_jobinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_commuinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_eduinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_practiceinfo_line.setVisibility(View.VISIBLE);
			myinfo_tv_worknumberinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_areainfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_productinfo_line.setVisibility(View.INVISIBLE);
			break;
		case 5:// 工号信息
			myinfo_tv_baseinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_jobinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_commuinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_eduinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_practiceinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_worknumberinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
			myinfo_tv_areainfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_productinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_baseinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_jobinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_commuinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_eduinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_practiceinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_worknumberinfo_line.setVisibility(View.VISIBLE);
			myinfo_tv_areainfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_productinfo_line.setVisibility(View.INVISIBLE);
			break;
		case 6:// 区域信息
			myinfo_tv_baseinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_jobinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_commuinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_eduinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_practiceinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_worknumberinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_areainfo.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
			myinfo_tv_productinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_baseinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_jobinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_commuinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_eduinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_practiceinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_worknumberinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_areainfo_line.setVisibility(View.VISIBLE);
			myinfo_tv_productinfo_line.setVisibility(View.INVISIBLE);
			break;
		case 7:// 其他信息
			myinfo_tv_baseinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_jobinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_commuinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_eduinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_practiceinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_worknumberinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_areainfo.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			myinfo_tv_productinfo.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
			myinfo_tv_baseinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_jobinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_commuinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_eduinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_practiceinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_worknumberinfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_areainfo_line.setVisibility(View.INVISIBLE);
			myinfo_tv_productinfo_line.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化标题栏
	 */
	private void initTitle() {
		TextView centertitle = (TextView) findViewById(R.id.head_center_title);
		centertitle.setText("我的资料");
		ImageButton title_left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton title_right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		title_left_btn.setVisibility(View.VISIBLE);
		title_left_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				MyInformationActivity.this.finish();
			}
		});
		title_right_btn.setVisibility(View.GONE);
	}

	/**
	 * 表头点击事件
	 * 
	 * @author GM
	 * 
	 */
	class MyOnClick implements OnClickListener {
		int index = 0;

		MyOnClick(int i) {

			this.index = i;
		}

		public void onClick(View v) {
			clearPress();
			setTextColor(index);
			viewPager.setCurrentItem(index);
		}
	}

	/**
	 * 从网络获取我的资料
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getMyInfo(UserInfo mUserInfo) {
		AjaxParams params = new AjaxParams();
		params.put("userid", mUserInfo.getUserid());
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(mUserInfo.getUserid()));
		params.put("method", "userid_info");
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = new BaseActivity().replaceErroStr(strMsg);
				strMsg = "连接超时";
				Toast.makeText(getApplicationContext(), strMsg, 1000).show();
				if (MyInformationActivity.this == null) {
					return;
				}
				load_msg.setVisibility(View.GONE);
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if (MyInformationActivity.this == null) {
					return;
				}
				System.out.println(t.toString());
				load_msg.setVisibility(View.GONE);
				JsonInfoV3 jsonInfov3 = null;
				try {
					jsonInfov3 = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				if (jsonInfov3 != null) {
					// 服务器传回来的用户信息
					JsonInfo jsonInfo = jsonInfov3.getResultDataToClass(JsonInfo.class);
					if (JsonInfoV3.SUCCESS_CODE.equals(jsonInfov3.getResultCode())) {// 正常返回数据
						// 服务器传回来的基本信息
						myInfo = jsonInfo.getData_exp(MyInfo.class);
						listKeyValue = jsonInfov3.getDataExpToClass("data_exp", "noList", MyInfoMationKeyValue.class);
						if (myInfo != null) {
							setBaseData();
							setJobData();
							setCommuData();
							setEduData();
							setPracticeDate();
							setWorkNumberData();
							setAreaDate();
							setOtherInfo();
						}
					} else {
						if (StringUtil.isNotEmpty(jsonInfov3.getResultDesc())) {
							Toast.makeText(getApplicationContext(), jsonInfov3.getResultDesc(), 1000).show();
						}
					}
				}
			}
		});

	}

	/**
	 * 设置基本信息数据
	 */
	private void setBaseData() {
		mMyinfo_userid.setText(StringUtil.noNull(myInfo.getUser_no()));
		mMyinfo_name.setText(StringUtil.noNull(myInfo.getName()));
		mMyinfo_sex.setText(StringUtil.noNull(myInfo.getMale()));
		mMyinfo_nation.setText(StringUtil.noNull(myInfo.getPhyle()));
		mMyinfo_birth.setText(StringUtil.noNull(myInfo.getBirth()));
		mMyinfo_npersonnel.setText(StringUtil.noNull(myInfo.getPerson_type()));
		mMyinfo_offices.setText(StringUtil.noNull(myInfo.getDepartment()));
		mMyifo_jointime.setText(StringUtil.noNull(myInfo.getJoin_time()));
		mMyifo_political.setText(StringUtil.noNull(myInfo.getPolitical()));
		mMyinfo_type.setText(StringUtil.noNull(myInfo.getPerson_type2()));
		mMyinfo_belong.setText(StringUtil.noNull(myInfo.getBelong_to()));
		mMyifo_health.setText(StringUtil.noNull(myInfo.getHealth()));
		mMyinfo_native_place.setText(StringUtil.noNull(myInfo.getNative_place()));
		mMyinfo_marital_status.setText(StringUtil.noNull(myInfo.getMarriage()));

		mMyinfo_specialty.setText(StringUtil.noNull(myInfo.getSpecialty()));
	}

	/**
	 * 设置职位信息数据
	 */
	private void setJobData() {
		mMyinfo_jobname.setText(StringUtil.noNull(myInfo.getJob_name()));
		mMyinfo_standardrank.setText(StringUtil.noNull(myInfo.getJob_grade()));
		mMyinfo_currentrank.setText(StringUtil.noNull(myInfo.getNow_job_grade()));
		mMyinfo_qualificationflag.setText(StringUtil.noNull(myInfo.getQ_identification()));
		mMyinfo_jobtime.setText(StringUtil.noNull(myInfo.getPositions_time()));
	}

	/**
	 * 设置通讯信息数据
	 */
	private void setCommuData() {
		mMyinfo_mobilephone.setText(StringUtil.noNull(myInfo.getPhone()));
		mMyinfo_email.setText(StringUtil.noNull(myInfo.getEmail()));
	}

	/**
	 * 设置学历信息数据
	 */
	private void setEduData() {
		mMyinfo_school.setText(StringUtil.noNull(myInfo.getSchool()));
		mMyinfo_profession.setText(StringUtil.noNull(myInfo.getProfession()));
		mMyinfo_edu.setText(StringUtil.noNull(myInfo.getEdu()));
		mMyinfo_edutype.setText(StringUtil.noNull(myInfo.getEdu_type()));
	}

	/**
	 * 设置实习信息数据
	 */
	private void setPracticeDate() {
		mMyinfo_internship_start.setText(StringUtil.noNull(myInfo.getInternship_start()));
		mMyinfo_internship_end.setText(StringUtil.noNull(myInfo.getInternship_end()));
		mMyinfo_newWorkCount.setText(StringUtil.noNull(myInfo.getTime_limit()));
	}

	/**
	 * 设置工号信息
	 */
	private void setWorkNumberData() {
		mLayout_daiban.removeAllViews();
		LinearLayout.LayoutParams mainlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
		lp1.rightMargin = 10;
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1, 1.0f);
		lp2.leftMargin = 10;
		lp2.topMargin = 10;
		lp2.bottomMargin = 10;
		lp2.rightMargin = 10;
		for (int i = 0; i < listKeyValue.size(); i++) {
			LinearLayout mainLayout = new LinearLayout(this);
			mainLayout.setOrientation(LinearLayout.HORIZONTAL);
			TextView tx = new TextView(this);
			tx.setText(listKeyValue.get(i).key + ":");
			tx.setTextColor(Color.rgb(0, 0, 0));
			tx.setGravity(Gravity.LEFT);
			tx.setPadding(10, 0, 10, 0);
			mainLayout.addView(tx, lp1);
			TextView tx2 = new TextView(this);
			tx2.setText(listKeyValue.get(i).value);
			tx2.setTextColor(Color.rgb(141, 194, 31));
			tx2.setGravity(Gravity.LEFT);
			tx2.setPadding(10, 0, 10, 0);
			mainLayout.addView(tx2, lp1);
			mLayout_daiban.addView(mainLayout, mainlp);
			// 下划线
			ImageView iv = new ImageView(this);
			iv.setBackgroundColor(getResources().getColor(R.color.app_gray_drak));
			mLayout_daiban.addView(iv, lp2);
		}
	}

	/**
	 * 设置区域信息数据
	 */
	private void setAreaDate() {
		mMyinfo_quyu.setText(StringUtil.noNull(myInfo.getDistrict()));
		mMyinfo_banzu.setText(StringUtil.noNull(myInfo.getGroup()));
		mMyinfo_workaddr.setText(StringUtil.noNull(myInfo.getOffice_dis()));
		mMyinfo_areatime.setText(StringUtil.noNull(myInfo.getIn_area_time()));
	}

	/**
	 * 设置其他信息数据
	 */
	private void setOtherInfo() {
		mMyinfo_mainskillline.setText(StringUtil.noNull(myInfo.getMain_skill()));
		mMyinfo_isfulltime.setText(StringUtil.noNull(myInfo.getFull_time_job()));
		mMyinfo_alinemark.setText(StringUtil.noNull(myInfo.getIdentification()));
		mMyinfo_workduty.setText(StringUtil.noNull(myInfo.getStandard_working()));
	}
}
