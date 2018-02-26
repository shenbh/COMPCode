package com.newland.comp.activity.indicator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.indicator.IndicatorWarningAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.indicator.WarningDetail;
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
 * 话务预警 详细信息
 * 
 * @author Administrator
 *
 */
public class IndicatorWarningActivity extends BaseActivity implements IXListViewListener {

	// private ListView list_data;
	private PullRefreshListView list_data;
	private List<WarningDetail> list;
	private IndicatorWarningAdapter adapter;
	private View load_msg;

	private int page_index=1;//当前页码
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_data_tiitle);
		initView();
		reflush();
	}

	private void initView() {
		load_msg = findViewById(R.id.load_msg);
		// list_data = (ListView) findViewById(R.id.list_data);
		list_data = (PullRefreshListView) findViewById(R.id.list_data);
		list_data.setPullLoadEnable(true);
		list_data.setXListViewListener(this);
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("预警信息");
		left_btn.setVisibility(View.VISIBLE);
		left_btn.setOnClickListener(new OnClickListener() {

			
			public void onClick(View arg0) {
				IndicatorWarningActivity.this.finish();

			}
		});

	}

	/**
	 * 获取信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void reflush() {
		super.reflush();
		if (load_msg != null) {
			load_msg.setVisibility(View.VISIBLE);
		}

		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "warning_detail");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("quarter_type", StringUtil.noNull(getIntent().getStringExtra("quarter_type")));
		params.put("time", StringUtil.noNull(getIntent().getStringExtra("time")));
		params.put("page_index", page_index+"");
		params.put("page_row", ActivityUtil.pageRow);
		if (StringUtil.noNull(getIntent().getStringExtra("type")).equals("hw_warn")) // 话务预警
		{
			params.put("type", "hw_warn");
		} else if (StringUtil.noNull(getIntent().getStringExtra("type")).equals("fault_warn")) {
			params.put("type", "fault_warn");
		} else {
			params.put("type", "complaint");
		}
		System.out.println(HttpUtils.URL+"?" + params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				strMsg="连接超时";Toast.makeText(getApplicationContext(), strMsg, 1000).show();
				if (IndicatorWarningActivity.this==null) {
					return;
				}
				if (load_msg != null) {
					load_msg.setVisibility(View.GONE);
				}
			}

			@Override
			public void onSuccess(Object t) {
				if (IndicatorWarningActivity.this==null) {
					return;
				}
				System.out.println(t.toString());
				if (load_msg != null) {
					load_msg.setVisibility(View.GONE);
				}
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(),JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
		
				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						JsonInfo jsonInfo =   jsonInfov2.getResultDataToClass(JsonInfo.class);
						List<WarningDetail>mlist = jsonInfo.getDataList(WarningDetail.class);
						if (page_index == 1) {// 刷新
							if (mlist != null && mlist.size() > 0) {
								list=mlist;
								adapter = new IndicatorWarningAdapter(getApplicationContext(), list);
								list_data.setAdapter(adapter);
								if (list.size() < Integer.valueOf(ActivityUtil.pageRow)) {// 数据没必要分页时，隐藏“加载更多”按钮
									list_data.setPullLoadEnable(false);
								} else {
									list_data.setPullLoadEnable(true);
								}
							} else {
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
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();// 显示失败提示
					}

				}
			}
		});
	}

	
	public void onListViewRefresh() {
		page_index = 1;
		reflush();
		onLoad();
	}

	
	public void onListViewLoadMore() {
		page_index++;
		reflush();
		onLoad();
	}

	private void onLoad() {
		list_data.stopRefresh();
		list_data.stopLoadMore();
		String date = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date());
		list_data.setRefreshTime(date);
	}
}
