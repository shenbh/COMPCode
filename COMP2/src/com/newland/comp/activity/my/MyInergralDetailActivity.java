package com.newland.comp.activity.my;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.my.IntegralAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.my.IntegralData;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.PullRefreshListView;
import com.newland.comp.view.PullRefreshListView.IXListViewListener;
import com.newland.comp2.R;

/**
 * 积分明细
 * 
 * @author H81
 *
 */
public class MyInergralDetailActivity extends BaseActivity implements IXListViewListener {
	PullRefreshListView pullRefreshListView;
	private int page = 1; // 当前页
	private int rows = 20;// 每页显示几条
	List<IntegralData> list;
	IntegralAdapter adapter;
	TextView head_center_title;
	ImageButton head_left_btn;
	private View no_data_layout;// 无数据布局
	private String quarter; // 积分查询季度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_integral_detail);
		initView();
	}

	private void initView() {
		head_center_title = (TextView) findViewById(R.id.head_center_title);
		head_center_title.setText("积分明细");
		head_left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		no_data_layout = findViewById(R.id.no_data_layout);
		head_left_btn.setVisibility(View.VISIBLE);
		head_left_btn.setOnClickListener(new OnClickListener() {

			
			public void onClick(View arg0) {
				MyInergralDetailActivity.this.finish();

			}
		});
		pullRefreshListView = (PullRefreshListView) findViewById(R.id.listview);
		pullRefreshListView.setPullLoadEnable(true);
		pullRefreshListView.setXListViewListener(this);

		Bundle bundleObject = getIntent().getExtras();
		quarter = bundleObject.getString("quarter");
		list = (List<IntegralData>) bundleObject.getSerializable("list");
		if (list.size() == 0) {// 无数据布局隐藏
			no_data_layout.setVisibility(View.VISIBLE);
		}
		adapter = new IntegralAdapter(MyInergralDetailActivity.this, list);
		pullRefreshListView.setAdapter(adapter);
		if (list.size() < Integer.valueOf(ActivityUtil.pageRow)) {// 数据没必要分页时，隐藏“加载更多”按钮
			pullRefreshListView.setPullLoadEnable(false);
		} else {
			pullRefreshListView.setPullLoadEnable(true);
		}
	}

	private void getData() {
		String userid = SharedPreferencesUtils.getConfigStr(this, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("method", "my_integral");
		params.put("page_index", page + "");// 当前页
		params.put("page_row", ActivityUtil.pageRow + "");// 每页显示几条
		params.put("quarter", quarter);// 当前日期
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		System.out.println(params.toString());
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				Log.v("newland", strMsg + t.getMessage());
				if (MyInergralDetailActivity.this==null) {
					return;
				}
				if (no_data_layout != null) {// 显示无数据布局
					no_data_layout.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if (MyInergralDetailActivity.this==null) {
					return;
				}
				Log.v("newland", t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(),JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
			
			
				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						JsonInfo jsonInfo =   jsonInfov2.getResultDataToClass(JsonInfo.class);
						List<IntegralData> mlist = jsonInfo.getDataList(IntegralData.class);
						if (page == 1) {// 刷新

							if (mlist != null && mlist.size() > 0) {
								if (no_data_layout != null) {// 无数据布局隐藏
									no_data_layout.setVisibility(View.GONE);
								}
								list = mlist;
								adapter = new IntegralAdapter(MyInergralDetailActivity.this, mlist);
								pullRefreshListView.setAdapter(adapter);
								onLoad();
								if (mlist.size() < Integer.valueOf(ActivityUtil.pageRow)) {// 数据没必要分页时，隐藏“加载更多”按钮
									pullRefreshListView.setPullLoadEnable(false);
								} else {
									pullRefreshListView.setPullLoadEnable(true);
								}
								
							} else {
								if (no_data_layout != null) {// 显示无数据布局
									no_data_layout.setVisibility(View.VISIBLE);
								}
								Toast.makeText(getApplicationContext(), "无响应数据", 500).show();
								pullRefreshListView.setVisibility(View.GONE);
								onLoad();
							}
						} else {
							if (mlist != null && mlist.size() > 0) {
								list.addAll(mlist);
								adapter.notifyDataSetChanged();
								onLoad();
							} else {
								Toast.makeText(getApplicationContext(), "最后一页了", 500).show();
								onLoad();
							}
						}
					} else {
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();// 显示失败提示
						pullRefreshListView.setVisibility(View.GONE);
					}
				}
			}

		});
	}

	
	public void onListViewRefresh() {
		page = 1;
		getData();

	}

	
	public void onListViewLoadMore() {
		page++;
		getData();

	}

	private void onLoad() {
		pullRefreshListView.stopRefresh();
		pullRefreshListView.stopLoadMore();
		String date = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date());
		pullRefreshListView.setRefreshTime(date);
	}
	
}
