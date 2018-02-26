package com.newland.comp.activity.my;

import java.util.ArrayList;
import java.util.List;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.my.MyPerformanceDetail1Adapter;
import com.newland.comp.adapter.my.MyPerformanceDetailAdapter;
import com.newland.comp.adapter.my.MyViewPageAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.UserInfo;
import com.newland.comp.bean.my.EffictData;
import com.newland.comp.bean.my.EffictData2;
import com.newland.comp.common.AppContext;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.PullRefreshListView;
import com.newland.comp.view.PullRefreshListView.IXListViewListener;
import com.newland.comp2.R;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 绩效明细
 * 
 * @author H81
 * 
 */
public class MyPerformanceDetailActivity extends BaseActivity implements OnClickListener, IXListViewListener {
	/*
	 * viewpager
	 */
	ViewPager viewPager;// viewPaper对象
	List<View> views;
	View view_product;
	View view_manager;
	private int offset;// 动画图片偏移量
	private int bmpW;// 图片宽度
	private int currIndex;// 当前页卡编号

	private TextView mPerformance_detail_product;// 个人生产
	private TextView mPerformance_detail_manager;// 班组管理
	private ImageView product_line;
	private ImageView manager_line;
	private ImageView mPerformance_detail_productline;
	// private ImageView mPerformance_detail_managerline;
	private PullRefreshListView pullRefreshListView;
	private PullRefreshListView pullRefreshListView1;

	List<EffictData> list;// EffictData
	List<EffictData2> list2;// EffictData2

	private MyPerformanceDetailAdapter myPerformanceDetailAdapter;// 个人生产适配器
	private MyPerformanceDetail1Adapter myPerformanceDetail1Adapter;// 班组管理适配器

	private int product_page_index = 1; // 当前页
	private int manager_page_index = 1; // 当前页
	private int page_rows = 5;// 每页显示几条
	private String nowdate;// 当前日期
	private UserInfo userInfo;
	
	private boolean isFirstTime=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.my_performance_detail);
		setTitle();
		bindViews();
		bindViewPager();
		getData();
		
		getDataFromServer(product_page_index);
//		getDataFromServer1(manager_page_index);
	}

	private void bindViews() {

		mPerformance_detail_product = (TextView) findViewById(R.id.performance_detail_product);// 个人生产
		mPerformance_detail_manager = (TextView) findViewById(R.id.performance_detail_manager);// 班组管理
		product_line = (ImageView) findViewById(R.id.performance_detail_product_line);
		manager_line = (ImageView) findViewById(R.id.performance_detail_manager_line);
		mPerformance_detail_product.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
		mPerformance_detail_manager.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
		product_line.setVisibility(View.VISIBLE);
		manager_line.setVisibility(View.INVISIBLE);
		mPerformance_detail_product.setOnClickListener(new MyOnClick(0));
		mPerformance_detail_manager.setOnClickListener(new MyOnClick(1));

	}

	/**
	 * 初始化viewpager
	 */
	private void bindViewPager() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		bmpW = screenW / 2;
		offset = bmpW;

		viewPager = (ViewPager) findViewById(R.id.myperfor_viewPager);
		views = new ArrayList<View>();
		LayoutInflater layoutInflater = getLayoutInflater();
		view_product = layoutInflater.inflate(R.layout.my_performance_detail_product, null);// 个人生产
		view_manager = layoutInflater.inflate(R.layout.my_performance_detail_manager, null);// 班组管理

		pullRefreshListView = (PullRefreshListView) view_product.findViewById(R.id.performance_detail_listview);// 内容
		pullRefreshListView.setPullLoadEnable(true);
		pullRefreshListView.setXListViewListener(this);

		pullRefreshListView1 = (PullRefreshListView) view_manager.findViewById(R.id.performance_detail_listview);// 内容
		pullRefreshListView1.setPullLoadEnable(true);
		pullRefreshListView1.setXListViewListener(this);

		views.add(view_product);
		views.add(view_manager);
		viewPager.setAdapter(new MyViewPageAdapter(views));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setOffscreenPageLimit(2);
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset;// 页卡1-->页卡2 偏移量

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 页卡切换监听
		 */

		public void onPageSelected(int arg0) {
			Animation animation = new TranslateAnimation(one * currIndex, one * arg0, 0, 0);
			currIndex = arg0;
			setTextColor(currIndex);
		}
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
			viewPager.setCurrentItem(index);
		}

	}

	/**
	 * 设置个人上产和班组管理的背景色
	 * 
	 * @param id
	 */
	private void setTextColor(int index) {
		switch (index) {
		case 0:// 个人生产
			mPerformance_detail_product.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
			mPerformance_detail_manager.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			product_line.setVisibility(View.VISIBLE);
			manager_line.setVisibility(View.INVISIBLE);
			pullRefreshListView.setAdapter(myPerformanceDetailAdapter);
			break;
		case 1:// 班组管理
			mPerformance_detail_product.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
			mPerformance_detail_manager.setTextColor(getApplicationContext().getResources().getColor(R.color.app_green));
			product_line.setVisibility(View.INVISIBLE);
			manager_line.setVisibility(View.VISIBLE);
			pullRefreshListView1.setAdapter(myPerformanceDetail1Adapter);
			break;
		default:
			break;
		}
	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer(int index) {
		initDialog(this);
		AjaxParams params = new AjaxParams();
		params.put("userid", userInfo.getUserid());
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userInfo.getUserid()));
		params.put("method", "my_effect");
		params.put("page_index", index + "");// 当前页
		params.put("page_row", page_rows + "");// 每页显示几条
		params.put("month", nowdate);// 当前日期
		System.out.println(params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = new BaseActivity().replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				Log.v("newland", strMsg + t.getMessage());
				if (MyPerformanceDetailActivity.this == null) {
					return;
				}
				if (dialog != null) {
					dialog.dismiss();
				}
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (MyPerformanceDetailActivity.this == null) {
					return;
				}
				System.out.println("my_effect---" + t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式",  Toast.LENGTH_SHORT).show();
				}

				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						List<EffictData> mlist = jsonInfo.getDataList(EffictData.class);
						setData(mlist);
						if(isFirstTime){
							getDataFromServer1(manager_page_index);
							isFirstTime=false;
						}
					} else {
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(),  Toast.LENGTH_SHORT).show();// 显示登录失败提示
					}
				}
			}
		});
	}
	private void getDataFromServer1(int index) {
		initDialog(this);
		AjaxParams params = new AjaxParams();
		params.put("userid", userInfo.getUserid());
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userInfo.getUserid()));
		params.put("method", "my_effect");
		params.put("page_index", index + "");// 当前页
		params.put("page_row", page_rows + "");// 每页显示几条
		params.put("month", nowdate);// 当前日期
		System.out.println(params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = new BaseActivity().replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				Log.v("newland", strMsg + t.getMessage());
				if (MyPerformanceDetailActivity.this == null) {
					return;
				}
				if (dialog != null) {
					dialog.dismiss();
				}
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (MyPerformanceDetailActivity.this == null) {
					return;
				}
				System.out.println("my_effect---" + t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式",  Toast.LENGTH_SHORT).show();
				}

				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						List<EffictData2> mlist2 = jsonInfo.getDataList2(EffictData2.class);
						setData1( mlist2);
					} else {
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(),  Toast.LENGTH_SHORT).show();// 显示登录失败提示
					}
				}
			}
		});
	}

	/**
	 * 接收传递过来的值
	 */
	@SuppressWarnings("unchecked")
	private void getData() {
		Bundle bundle = this.getIntent().getExtras();
		nowdate = bundle.getString("nowdate");
		userInfo = (UserInfo) bundle.getSerializable("userinfo");
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
			center_tv.setText("绩效明细");
		if (left_btn != null)
			left_btn.setVisibility(View.VISIBLE);
		left_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				MyPerformanceDetailActivity.this.finish();
			}
		});
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {
			right_tv.setVisibility(View.GONE);
		}
	}

	public void onClick(View v) {
	}

	private void onLoad() {
		pullRefreshListView.stopRefresh();
		pullRefreshListView.stopLoadMore();
		pullRefreshListView1.stopRefresh();
		pullRefreshListView1.stopLoadMore();
		// String date = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new
		// Date());
		pullRefreshListView.setRefreshTime(nowdate);
		pullRefreshListView1.setRefreshTime(nowdate);
	}

	public void onListViewRefresh() {
		if (viewPager.getCurrentItem() == 0) {
			product_page_index = 1;
			getDataFromServer(product_page_index);
		}
		if (viewPager.getCurrentItem() == 1) {
			manager_page_index = 1;
			getDataFromServer1(manager_page_index);
		}

		onLoad();
	}

	public void onListViewLoadMore() {
		if (viewPager.getCurrentItem() == 0) {
			product_page_index++;
			getDataFromServer(product_page_index);
		}
		if (viewPager.getCurrentItem() == 1) {
			manager_page_index++;
			getDataFromServer1(manager_page_index);
		}
		onLoad();
	}

	private void setData(List<EffictData> mlist) {
		if (product_page_index == 1) {// 刷新
			if (mlist != null && mlist.size() > 0) {
				if (mlist.size() < page_rows) {// 数据没必要分页时，隐藏“加载更多”按钮
					pullRefreshListView.setPullLoadEnable(false);
				} else {
					pullRefreshListView.setPullLoadEnable(true);
				}
				list = mlist;
				myPerformanceDetailAdapter = new MyPerformanceDetailAdapter(MyPerformanceDetailActivity.this, list);
				pullRefreshListView.setAdapter(myPerformanceDetailAdapter);
			} else {
				Toast.makeText(getApplicationContext(), "无响应数据",  Toast.LENGTH_SHORT).show();
				pullRefreshListView.setVisibility(View.GONE);
			}
		} else {
			if (mlist != null && mlist.size() > 0) {

				list.addAll(mlist);
				myPerformanceDetailAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(getApplicationContext(), "最后一页了",  Toast.LENGTH_SHORT).show();
			}
		}
	}
	private void setData1( List<EffictData2> mlist2) {
		if (product_page_index == 1) {// 刷新
			if (mlist2 != null && mlist2.size() > 0) {
				if (mlist2.size() < page_rows) {// 数据没必要分页时，隐藏“加载更多”按钮
					pullRefreshListView.setPullLoadEnable(false);
				} else {
					pullRefreshListView.setPullLoadEnable(true);
				}
				list2 = mlist2;
				myPerformanceDetail1Adapter = new MyPerformanceDetail1Adapter(MyPerformanceDetailActivity.this, list2);
				pullRefreshListView.setAdapter(myPerformanceDetailAdapter);
			} else {
				Toast.makeText(getApplicationContext(), "无响应数据",  Toast.LENGTH_SHORT).show();
				pullRefreshListView.setVisibility(View.GONE);
			}
		} else {
			if (mlist2 != null && mlist2.size() > 0) {

				list2.addAll(mlist2);
				myPerformanceDetail1Adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(getApplicationContext(), "最后一页了",  Toast.LENGTH_SHORT).show();
			}
		}
	}

}
