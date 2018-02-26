package com.newland.comp.activity.more;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.more.MoreStaffAspAdapter;
import com.newland.comp.bean.more.PrombleData;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp.view.PullRefreshListView.IXListViewListener;
import com.newland.comp2.R;

/**
 * 更多-员工心声界面（我的提问列表）
 * 
 * @author H81
 *
 *         2015年5月6日 上午8:47:05
 * @version 1.0.0
 */
public class MoreStaffAspActivity extends BaseActivity implements IXListViewListener {
	// 标题
	private ImageButton left_btn;// 返回
	private ImageButton right_btn;
	private TextView right_tv;

	private TextView mTv_myquestion;// 我的提问
	private com.newland.comp.view.PullRefreshListView mListview;// 列表

	
	private MoreStaffAspAdapter adapter;
	private List<PrombleData> list;
	private int page_index = 1;// 当前页码
	LoadingDialog dialog;
	private View nodata_layout;//无数据
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_staffasp);
		bindViews();
		setTitle();
		getData();
	}

	private void bindViews() {
		nodata_layout = findViewById(R.id.no_data_layout);
		mTv_myquestion = (TextView) findViewById(R.id.tv_myquestion);
		mListview = (com.newland.comp.view.PullRefreshListView) findViewById(R.id.listview);
		mTv_myquestion.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "点击了我的提问", Toast.LENGTH_SHORT).show();
			}
		});
		mListview.setPullLoadEnable(true);
		mListview.setXListViewListener(this);
		mListview.setOnItemClickListener(new OnItemClickListener() {//点击列表显示详细界面

			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent=new Intent(MoreStaffAspActivity.this,MoreStaffAspDetailActivity.class );
				intent.putExtra("pid", list.get(position-1).pro_id);
				startActivity(intent);
			}
		});
	}

	/**
	 * 设置标题栏
	 */
	private void setTitle() {
		left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (left_btn != null) {
			left_btn.setVisibility(View.VISIBLE);
		}
		if (center_tv != null)
			center_tv.setText("员工心声");
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {// 反馈问题
			right_tv.setVisibility(View.VISIBLE);
			right_tv.setTextColor(getResources().getColor(R.color.app_green));
			right_tv.setText("反馈问题");
			right_tv.setOnClickListener(new OnClickListener() {

				
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), MoreFeedBackActivity.class);
					startActivity(intent);
				}
			});
		}
	}

	
	public void onListViewRefresh() {
		page_index = 1;
		getData();
		onLoad();
	}

	
	public void onListViewLoadMore() {
		page_index++;
		getData();
		onLoad();
	}

	private void onLoad() {
		mListview.stopRefresh();
		mListview.stopLoadMore();
		String date = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date());
		mListview.setRefreshTime(date);
	}

	/**从服务器获取数据
	 * 
	 * userid;//工号
	 * 
	 * signid: md5(userid+'comp')
	 * 
	 * method：my_promble
	 * 
	 * page_index: 当前页码
	 * 
	 * page_row:每页显示几条数据
	 */
	@SuppressWarnings("unchecked")
	private void getData() {
		dialog = new LoadingDialog(MoreStaffAspActivity.this);
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {dialog.show(true);}
		System.out.println("getdata dialog exist");
		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("userid", userid);
		ajaxParams.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		ajaxParams.put("page_index", page_index + "");// 当前页码
		ajaxParams.put("page_row", ActivityUtil.pageRow);// 每页显示几条数据
		ajaxParams.put("method", "my_promble");
		System.out.println(HttpUtils.URL+"?" + ajaxParams.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, ajaxParams, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				strMsg="连接超时";Toast.makeText(getApplicationContext(), strMsg, 1000).show();
				if (MoreStaffAspActivity.this==null) {
					return;
				}
				if (mListview != null) {
					mListview.setVisibility(View.GONE);
				}
				if (nodata_layout != null) {// 显示无数据布局
					nodata_layout.setVisibility(View.VISIBLE);
				}
				dialog.dismiss();
			}

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);

			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				if (MoreStaffAspActivity.this==null) {
					return;
				}
				System.out.println(t.toString());
				dialog.dismiss();

				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(),JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
			
		
				if (jsonInfo != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
						
						List<PrombleData> mlist = jsonInfo.getResultDataKeyToClass("data", PrombleData.class);
						if (page_index == 1) {// 刷新
							if (mlist != null && mlist.size() > 0) {
								if (mListview != null) {
									mListview.setVisibility(View.VISIBLE);
								}
								if (nodata_layout != null) {// 无数据布局隐藏
									nodata_layout.setVisibility(View.GONE);
								}
								list = mlist;
								adapter = new MoreStaffAspAdapter(MoreStaffAspActivity.this, list);
								mListview.setAdapter(adapter);
								if (list.size() < Integer.valueOf(ActivityUtil.pageRow)) {// 数据没必要分页时，隐藏“加载更多”按钮
									mListview.setPullLoadEnable(false);
								} else {
									mListview.setPullLoadEnable(true);
								}
							} else {
								if (mListview != null) {
									mListview.setVisibility(View.GONE);
								}
								if (nodata_layout != null) {// 显示无数据布局
									nodata_layout.setVisibility(View.VISIBLE);
								}
								Toast.makeText(getApplicationContext(), "无响应数据", 500).show();
							}
						} else {
							if (mlist != null && mlist.size() > 0) {
								list.addAll(mlist);
								adapter.notifyDataSetChanged();
							} else {
								Toast.makeText(getApplicationContext(), "最后一页了", 500).show();
							}
						}
					} else {
						Toast.makeText(getApplicationContext(), jsonInfo.getResultDesc(), 1000).show();// 显示失败提示
					}
				}
			}
		});
	}

	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.head_left_btn) {// 返回
			finish();
		}
	}
}
