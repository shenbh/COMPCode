package com.newland.comp.activity.hr;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newland.comp.adapter.BaseFragmentPagerAdapter;
import com.newland.comp.common.AppContext;
import com.newland.comp.fragment.BaseFragment;
import com.newland.comp.fragment.CheckInFragment;
import com.newland.comp.fragment.ExchangeRecoderFragment;
import com.newland.comp.fragment.ExchangeZoneFragment;
import com.newland.comp.fragment.LotteryFragment;
import com.newland.comp.fragment.MarkingWorkloadFragment;
import com.newland.comp.fragment.MyPayStubFragment;
import com.newland.comp.fragment.PayrollNightFragment;
import com.newland.comp.fragment.PayrollOthersFragment;
import com.newland.comp.fragment.QosFragment;
import com.newland.comp.fragment.SecondKillFragment;
import com.newland.comp.fragment.WorkLoadFragment;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp2.R;

/**
 * 积分商城
 * @author H81
 *
 */
public class HrIntegralShopActivity extends FragmentActivity{

	private LinearLayout mHor_lin;
	private LinearLayout mDhzq;
	private TextView mDhzq_tv;
	private ImageView mDhzq_iv;
	private LinearLayout mMs;
	private TextView mMs_tv;
	private ImageView mMs_iv;
	private LinearLayout mCj;
	private TextView mCj_tv;
	private ImageView mCj_iv;
	private LinearLayout mJl;
	private TextView mJl_tv;
	private ImageView mJl_iv;
	private android.support.v4.view.ViewPager mViewPager;
	private List<BaseFragment> listFragments;
	private BaseFragment currentFragment;// 当前选中的fragment
	private ExchangeZoneFragment exchangeZoneFragment; // 兑换专区
	private ExchangeRecoderFragment exchangeRecoderFragment; // 兑换记录
	private SecondKillFragment secondKillFragment; // 积分秒杀
	private LotteryFragment lotteryFragment; // 积分抽奖

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.integral_shop);
		AppContext.getInstance().addActivity(this);
		bindViews();
		initFragment();
	}
	
    // End Of Content View Elements

    private void bindViews() {

        mHor_lin = (LinearLayout) findViewById(R.id.hor_lin);
        mDhzq = (LinearLayout) findViewById(R.id.dhzq);
        mDhzq_tv = (TextView) findViewById(R.id.dhzq_tv);
        mDhzq_iv = (ImageView) findViewById(R.id.dhzq_iv);
        mMs = (LinearLayout) findViewById(R.id.ms);
        mMs_tv = (TextView) findViewById(R.id.ms_tv);
        mMs_iv = (ImageView) findViewById(R.id.ms_iv);
        mCj = (LinearLayout) findViewById(R.id.cj);
        mCj_tv = (TextView) findViewById(R.id.cj_tv);
        mCj_iv = (ImageView) findViewById(R.id.cj_iv);
        mJl = (LinearLayout) findViewById(R.id.jl);
        mJl_tv = (TextView) findViewById(R.id.jl_tv);
        mJl_iv = (ImageView) findViewById(R.id.jl_iv);
        mViewPager = (android.support.v4.view.ViewPager) findViewById(R.id.mViewPager);
        
        TextView title = (TextView) findViewById(R.id.head_center_title);
		title.setText("积分商城");
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		left_btn.setVisibility(View.VISIBLE);
		left_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				HrIntegralShopActivity.this.finish();
			}
		});
    }
    
    /**
	 * 初始化Fragment
	 * */
	private void initFragment() {
		listFragments = new ArrayList<BaseFragment>();
		exchangeZoneFragment = ExchangeZoneFragment.newInstance(HrIntegralShopActivity.this);
		exchangeRecoderFragment = ExchangeRecoderFragment.newInstance(HrIntegralShopActivity.this);
		secondKillFragment = SecondKillFragment.newInstance(HrIntegralShopActivity.this);
		lotteryFragment = LotteryFragment.newInstance(HrIntegralShopActivity.this);

		listFragments.add(exchangeZoneFragment);
		listFragments.add(secondKillFragment);
		listFragments.add(lotteryFragment);
		listFragments.add(exchangeRecoderFragment);

		BaseFragmentPagerAdapter mAdapetr = new BaseFragmentPagerAdapter(getSupportFragmentManager(), listFragments);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);

		mViewPager.setOffscreenPageLimit(1);
		currentFragment = exchangeZoneFragment;
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
				mDhzq_iv.setVisibility(View.VISIBLE);
				mDhzq_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
				break;
			case 1:
				mMs_iv.setVisibility(View.VISIBLE);
				mMs_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
				break;
			case 2:
				mCj_iv.setVisibility(View.VISIBLE);
				mCj_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
				break;
			case 3:
				mJl_iv.setVisibility(View.VISIBLE);
				mJl_tv.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
				break;

			default:
				break;
			}
			currentFragment.refresh();
		}
	};


	private void clearPress() {

		mDhzq_iv.setVisibility(View.INVISIBLE);
		mMs_iv.setVisibility(View.INVISIBLE);
		mCj_iv.setVisibility(View.INVISIBLE);
		mJl_iv.setVisibility(View.INVISIBLE);

		mDhzq_tv.setTextColor(this.getResources().getColor(R.color.gray));
		mMs_tv.setTextColor(this.getResources().getColor(R.color.gray));
		mCj_tv.setTextColor(this.getResources().getColor(R.color.gray));
		mJl_tv.setTextColor(this.getResources().getColor(R.color.gray));

	}
	
	
	public void onClick(View view) {
		clearPress();
		if (view.getId() == R.id.dhzq) // 兑换专区
		{
			mDhzq_iv.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(0);
		} else if (view.getId() == R.id.ms) // 积分秒杀
		{
			mMs_iv.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(1);
		} else if (view.getId() == R.id.cj) // 积分抽奖
		{
			mCj_iv.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(2);
		}

		else if (view.getId() == R.id.jl) // 兑换记录
		{
			mJl_iv.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(3);

		}
	}
}
