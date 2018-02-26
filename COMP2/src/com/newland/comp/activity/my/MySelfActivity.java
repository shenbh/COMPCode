package com.newland.comp.activity.my;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.TabBaseActivity;
import com.newland.comp.activity.my.miniknowledge.MiniKnowledgeMainActivity;
import com.newland.comp.activity.my.minitest.MiniTestActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.UserInfo;
import com.newland.comp.utils.AnimCommon;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 我的界面
 * 
 * @author H81
 * 
 */
@SuppressLint("HandlerLeak")
public class MySelfActivity extends TabBaseActivity {

	private TextView title;
	private UserInfo userInfo;
	private TextView unred_num;
	// 首页顶部的图片
	private int imageIds[];
	private ArrayList<ImageView> images;
	private ArrayList<View> dots;
	private ViewPager mViewPager;
	private ViewPagerAdapter adapter;
	private int oldPosition = 0;// 记录上一次点的位置
	private int currentItem; // 当前页面
	private ScheduledExecutorService scheduledExecutorService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_service);
		initPagerView();
		initView();
		refrushSysMsgNum();
	}

	private void initPagerView() {
		// 图片ID
		imageIds = new int[] { R.drawable.item01, R.drawable.item02, R.drawable.item03, R.drawable.item04, R.drawable.item05 };

		// 显示的图片
		images = new ArrayList<ImageView>();
		for (int i = 0; i < imageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imageIds[i]);

			images.add(imageView);
		}

		// 显示的点
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.dot_0));
		dots.add(findViewById(R.id.dot_1));
		dots.add(findViewById(R.id.dot_2));
		dots.add(findViewById(R.id.dot_3));
		dots.add(findViewById(R.id.dot_4));
		mViewPager = (ViewPager) findViewById(R.id.vp);
		adapter = new ViewPagerAdapter();
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int position) {
				dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
				dots.get(position).setBackgroundResource(R.drawable.dot_focused);
				oldPosition = position;
				currentItem = position;
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initView() {
		findViewById(R.id.head_right_btn).setVisibility(View.VISIBLE);
		userInfo = (UserInfo) getIntent().getSerializableExtra("userinfo");
		unred_num = (TextView) findViewById(R.id.unred_num);
		title = (TextView) findViewById(R.id.head_center_title);
		title.setText("首页");
	}

	public void right_btn(View view) {
		Intent intent = new Intent(this, SystemSettingActivity.class);
		intent.putExtra("userinfo", userInfo);
		startActivity(intent);
	}

	/**
	 * 我的资料
	 * 
	 * @param view
	 */
	public void my_info(View view) {
		Intent intent = new Intent(getApplicationContext(), MyInformationActivity.class);
		intent.putExtra("userinfo", userInfo);
		startActivity(intent);
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
	}

	/**
	 * 我的薪酬
	 * 
	 * @param view
	 */
	public void my_payroll(View view) {
		Intent intent = new Intent(this, MyPayrollActivity.class);
		intent.putExtra("userinfo", userInfo);
		startActivity(intent);
	}

	/**
	 * 我的绩效
	 * 
	 * @param view
	 */
	public void my_performance(View view) {
		Intent intent = new Intent(MySelfActivity.this, MyPerformanceActivity.class);
		intent.putExtra("userinfo", userInfo);
		startActivity(intent);
		overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
	}

	/**
	 * 我的积分
	 * 
	 * @param view
	 */
	public void btn_my_inergral(View view) {
		Intent intent = new Intent(getApplicationContext(), MyInergralActivity.class);
		intent.putExtra("userinfo", userInfo);
		startActivity(intent);
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
	}

	/**
	 * 我的考勤
	 */
	public void btn_my_attendance(View view) {
		Intent intent = new Intent(MySelfActivity.this, MyAttendanceActivity.class);
		intent.putExtra("userinfo", userInfo);
		startActivity(intent);
		overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
	}

	/**
	 * 班表查询
	 * 
	 * @param view
	 */
	public void my_schedule(View view) {
		Intent intent = new Intent(getApplicationContext(), MyScheduleActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
	}

	/**
	 * 我的星级
	 * 
	 * @param view
	 */
	public void my_start(View view) {

		Intent intent = new Intent(getApplicationContext(), MyStartActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
	}

	/**
	 * 我的消息
	 * 
	 * @param view
	 */
	public void my_message(View view) {
		// 在缓存中保存未读消息数
		SharedPreferencesUtils.setConfigStr(getApplicationContext(), SharedPreferencesUtils.Name, "unred_num", "0");
		unred_num.setVisibility(View.GONE);
		Intent intent = new Intent(getApplicationContext(), MyMessageActivity.class);
		AnimCommon.set(R.anim.move_x_in, R.anim.move_x_out);
		startActivity(intent);
		refrushSysMsgNum();
	}

	/**
	 * 我的指标
	 * 
	 * @param view
	 */
	public void my_indicator(View view) {

	}

	/**
	 * 微知识
	 * 
	 * @param view
	 */
	public void miniknowledge(View view) {
		Intent intent = new Intent(getApplicationContext(), MiniKnowledgeMainActivity.class);
		startActivity(intent);
	}

	/**
	 * 微测试
	 * 
	 * @param view
	 */
	public void minitest(View view) {
		Intent intent = new Intent(getApplicationContext(), MiniTestActivity.class);
		startActivity(intent);
	}

	/**
	 * 检查服务是否运行
	 */
	private void checkPop() {
		// 检查有没冒泡提示
		String numstr = SharedPreferencesUtils.getConfigStr(getApplicationContext(), SharedPreferencesUtils.Name, "unred_num");
		int num = Integer.parseInt(StringUtil.noNull(numstr, "0"));
		if (num > 0) {
			unred_num.setVisibility(View.VISIBLE);
			// unred_num.setText(numstr);
			unred_num.setText("");
		}
		// if (num > 99) {
		// unred_num.setVisibility(View.VISIBLE);
		// unred_num.setText("...");
		// }
		if (num <= 0) {
			unred_num.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取未读消息数
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void refrushSysMsgNum() {
		String userid = SharedPreferencesUtils.getConfigStr(this, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "sys_msg_unred");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = new BaseActivity().replaceErroStr(strMsg);
				System.out.println("服务请求网络失败" + strMsg);
			}

			@Override
			public void onSuccess(Object t) {
				System.out.println(t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", Toast.LENGTH_SHORT).show();
				}
				if (jsonInfov2 != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) // 正常返回数据
					{
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						JSONObject jsonObject = jsonInfo.getData_expToJsonObject();
						// 在缓存中保存未读消息数
						SharedPreferencesUtils.setConfigStr(getApplicationContext(), SharedPreferencesUtils.Name, "unred_num", StringUtil.noNull(jsonObject.getString("unred_num"), "0"));
						checkPop();
					} else {// 显示登陆失败信息
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), Toast.LENGTH_SHORT).show();
					}
				}

			}
		});
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		refrushSysMsgNum();
	}

	// 顶部图片
	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return images.size();
		}

		// 是否是同一张图片
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			view.removeView(images.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			view.addView(images.get(position));
			return images.get(position);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 每隔2秒钟切换一张图片
		scheduledExecutorService.scheduleAtFixedRate(new ViewPagerTask(), 4, 4, TimeUnit.SECONDS);
	}

	// 切换图片
	private class ViewPagerTask implements Runnable {
		public void run() {
			currentItem = (currentItem + 1) % imageIds.length;
			// 更新界面
			handler.obtainMessage().sendToTarget();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 设置当前页面
			mViewPager.setCurrentItem(currentItem);
		}
	};

	@Override
	protected void onStop() {
		super.onStop();
		if (!scheduledExecutorService.isShutdown()) {
			scheduledExecutorService.shutdown();
		}
	}
}
