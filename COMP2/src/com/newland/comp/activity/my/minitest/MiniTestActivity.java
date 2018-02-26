package com.newland.comp.activity.my.minitest;

import java.util.ArrayList;
import java.util.List;
import com.newland.comp.adapter.BaseFragmentPagerAdapter;
import com.newland.comp.fragment.BaseFragment;
import com.newland.comp.fragment.MiniNotTestFragment;
import com.newland.comp.fragment.MiniTestedFragment;
import com.newland.comp2.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 我的微测试--未考试/已考试
 * 
 * @author Administrator
 * 
 */
public class MiniTestActivity extends FragmentActivity {

	// 未考试
	private LinearLayout mHor_lin;
	private LinearLayout mLl_nottest;
	private TextView mTv_nottest;
	private ImageView mIv_nottest;
	// 已考试
	private LinearLayout mLl_tested;
	private TextView mTv_tested;
	private ImageView mIv_tested;

	private BaseFragment currentFragment;// 当前选中的Fragment
	private ViewPager mViewPager;
	private List<BaseFragment> listFragments;

	MiniNotTestFragment miniNotTestFragment;
	MiniNotTestFragment miniTestedFragment;
	private int mScreenWidth = 0;// 屏幕宽度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.minitest);
		bindViews();
		setTitle();
		initData();
		initFragment();
	}

	private void bindViews() {

		mHor_lin = (LinearLayout) findViewById(R.id.hor_lin);
		mLl_nottest = (LinearLayout) findViewById(R.id.ll_nottest);
		mTv_nottest = (TextView) findViewById(R.id.tv_nottest);
		mIv_nottest = (ImageView) findViewById(R.id.iv_nottest);
		mLl_tested = (LinearLayout) findViewById(R.id.ll_tested);
		mTv_tested = (TextView) findViewById(R.id.tv_tested);
		mIv_tested = (ImageView) findViewById(R.id.iv_tested);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
	}

	/**
	 * 设置标题栏
	 */
	private void setTitle() {
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		TextView right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("微测试");
		if (left_btn != null) {// 返回
			left_btn.setVisibility(View.VISIBLE);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {// 日期
			right_tv.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化viewpager颜色
	 */
	private void initData() {
		mIv_nottest.setVisibility(View.VISIBLE);
		mIv_tested.setVisibility(View.INVISIBLE);
		mTv_nottest.setTextColor(this.getResources().getColor(R.color.app_green));
		mTv_tested.setTextColor(this.getResources().getColor(R.color.gray));
	}

	/**
	 * 清除viewpager颜色
	 */
	private void clearPress() {
		mIv_nottest.setVisibility(View.INVISIBLE);
		mIv_tested.setVisibility(View.INVISIBLE);
		mTv_nottest.setTextColor(this.getResources().getColor(R.color.gray));
		mTv_tested.setTextColor(this.getResources().getColor(R.color.gray));
	}

	/**
	 * 初始化Fragment
	 */
	private void initFragment() {
		listFragments = new ArrayList<BaseFragment>();
		miniNotTestFragment = MiniNotTestFragment.newInstance(this,"N");//hasCheck: 未考：N,已考：Y
		miniTestedFragment = MiniNotTestFragment.newInstance(this,"Y");

		listFragments.add(miniNotTestFragment);
		listFragments.add(miniTestedFragment);

		BaseFragmentPagerAdapter mAdapetr = new BaseFragmentPagerAdapter(getSupportFragmentManager(), listFragments);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);
		mViewPager.setOffscreenPageLimit(3);
		currentFragment = miniNotTestFragment;
	}

	/**
	 * ViewPager切换监听方法
	 * */
	public OnPageChangeListener pageListener = new OnPageChangeListener() {

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int position) {
			clearPress();
			mViewPager.setCurrentItem(position);
			currentFragment = listFragments.get(position);
			switch (position) {
			case 0:
				mIv_nottest.setVisibility(View.VISIBLE);
				mTv_nottest.setTextColor(MiniTestActivity.this.getResources().getColor(R.color.app_green));
				break;
			case 1:
				mIv_tested.setVisibility(View.VISIBLE);
				mTv_tested.setTextColor(MiniTestActivity.this.getResources().getColor(R.color.app_green));
				break;
			default:
				break;
			}
			selectTab(position);
		}
	};

	private void selectTab(int tab_postion) {
		for (int i = 0; i < mHor_lin.getChildCount(); i++) {
			View checkView = mHor_lin.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
		}
	}

	public void onClick(View view) {
		clearPress();
		if (view.getId() == R.id.ll_nottest) {
			mIv_nottest.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(0);
		}
		if (view.getId() == R.id.ll_tested)
		{
			mIv_tested.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(1);
		}
		if (view.getId() == R.id.head_left_btn) {// 标题栏左侧“返回”按钮
			this.finish();
		}
	}
}
