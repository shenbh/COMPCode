package com.newland.comp.activity.my;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.my.MyAttendanceOverTimeAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.my.OverTimeCountData;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp.view.PullRefreshListView;
import com.newland.comp.view.PullRefreshListView.IXListViewListener;
import com.newland.comp2.R;

/**
 * 我的考勤-加班明细
 * 
 * @author H81
 * 
 */
public class MyAttendanceOverTimeActivity extends BaseActivity implements IXListViewListener {
	private PullRefreshListView pullRefreshListView;
	private int page_index = 1;// 当前页码
//	private int page_row = 4;// 每页显示条数

	private List<OverTimeCountData>list;
	private MyAttendanceOverTimeAdapter myAttendanceOverTimeAdapter;

	private Intent intent;
	private String month;//月份
	
	private View no_data_layout;// 无数据布局

	LoadingDialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_attendance_overtime_detail);
		setTitle();
		intent=getIntent();
		month=intent.getStringExtra("month");
		no_data_layout = findViewById(R.id.no_data_layout);
		pullRefreshListView = (PullRefreshListView) findViewById(R.id.overtime_listview);
		pullRefreshListView.setPullLoadEnable(true);
		pullRefreshListView.setXListViewListener(this);
		getDataFromServer(0);

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
			center_tv.setText("加班明细");
		if (left_btn != null)
			left_btn.setVisibility(View.VISIBLE);
		left_btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				MyAttendanceOverTimeActivity.this.finish();
			}
		});
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {
			right_tv.setVisibility(View.GONE);
		}
	}

	
	public void onListViewRefresh() {
		page_index = 1;
		getDataFromServer(page_index);

		onLoad();
	}

	
	public void onListViewLoadMore() {
		page_index++;
		getDataFromServer(page_index);
		onLoad();
	}

	private void onLoad() {
		pullRefreshListView.stopRefresh();
		pullRefreshListView.stopLoadMore();
		String date = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date());
		pullRefreshListView.setRefreshTime(date);
	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer(int index) {
		dialog=new LoadingDialog(this);
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {dialog.show(true);}
		AjaxParams params = new AjaxParams();
		params.put("userid", SharedPreferencesUtils.getConfigStr(getApplication(), CASH_NAME, "userid"));
		params.put("method", "my_kq_jb");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(SharedPreferencesUtils.getConfigStr(getApplication(), CASH_NAME, "userid")));
		params.put("month", month);// 查询季度
		params.put("page_index", index + "");// 当前页码
		params.put("page_row", ActivityUtil.pageRow );// 每页显示几条
		System.out.println(HttpUtils.URL+"?" + params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				Log.v("newland", strMsg + t.getMessage());
				if (MyAttendanceOverTimeActivity.this==null) {
					return;
				}
				if (no_data_layout != null) {// 显示无数据布局
					no_data_layout.setVisibility(View.VISIBLE);
				}
				if (dialog!=null) {
					dialog.dismiss();
				}
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if (MyAttendanceOverTimeActivity.this==null) {
					return;
				}
				if (dialog!=null) {
					dialog.dismiss();
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
						List<OverTimeCountData> mlist = jsonInfo.getDataList(OverTimeCountData.class);
						if (page_index == 1) {// 刷新
							if (mlist != null && mlist.size() > 0) {
								if (no_data_layout != null) {// 无数据布局隐藏
									no_data_layout.setVisibility(View.GONE);
								}
								 list = mlist;
								myAttendanceOverTimeAdapter = new MyAttendanceOverTimeAdapter(MyAttendanceOverTimeActivity.this, list);
								pullRefreshListView.setAdapter(myAttendanceOverTimeAdapter);

								if (mlist.size() < Integer.valueOf(ActivityUtil.pageRow)) {// 数据没必要分页时，隐藏“加载更多”按钮
									pullRefreshListView.setPullLoadEnable(false);
								}else {
									pullRefreshListView.setPullLoadEnable(true);
								}
							} else {
								if (no_data_layout != null) {// 显示无数据布局
									no_data_layout.setVisibility(View.VISIBLE);
								}
								Toast.makeText(getApplicationContext(), "无响应数据", 500).show();
							}
						} else {
							if (mlist != null && mlist.size() > 0) {
								 list.addAll(mlist);
								myAttendanceOverTimeAdapter.notifyDataSetChanged();
							} else {
								Toast.makeText(getApplicationContext(), "最后一页了", 500).show();
							}
						}
					} else {
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				}
			}
		});
	}
}
