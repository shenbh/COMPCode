package com.newland.comp.activity.process;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.process.PrYandFAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.process.ProcessDataComplete;
import com.newland.comp.bean.process.ProcessStateData;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp.view.PullRefreshListView.IXListViewListener;
import com.newland.comp2.R;

/**
 * 流程-已办与发起流程查询
 * 
 * @author H81
 *
 *         2015年4月29日 上午9:46:40
 * @version 1.0.0
 */
public class PrYandFActivity extends BaseActivity implements IXListViewListener {
	private EditText mInput;// 请输入工单标题
	private TextView mStartdate;// 起始时间
	private TextView mEnddate;// 结束时间
	private Button mSearch_btn;// 查询
	private com.newland.comp.view.PullRefreshListView mListview;

	private View nodata_layout;// 无数据

	private PrYandFAdapter adapter;

	private List<ProcessDataComplete> list;

	private int page_index = 1;// 当前页码

	private String intentData;//已办流程、发起流程类别
	LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pryiban);
		intentData = getIntent().getStringExtra("type");
		setTitle();
		bindViews();
		getData();

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
			if (intentData.equals("finished")) {
				center_tv.setText("已办流程");
			}else if (intentData.equals("launch")) {
				center_tv.setText("我发起的流程");
			}
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

	private void bindViews() {

		mInput = (EditText) findViewById(R.id.input);
		mStartdate = (TextView) findViewById(R.id.startdate);
		mEnddate = (TextView) findViewById(R.id.enddate);
		mSearch_btn = (Button) findViewById(R.id.search_btn);
		mListview = (com.newland.comp.view.PullRefreshListView) findViewById(R.id.listview);
		mListview.setPullLoadEnable(true);
		mListview.setXListViewListener(this);
		
		nodata_layout = findViewById(R.id.no_data_layout);

		if (mListview != null) {
			mListview.setVisibility(View.GONE);
		}
		if (nodata_layout != null) {// 显示无数据布局
			nodata_layout.setVisibility(View.VISIBLE);
		}
	
		addListener();
	}

	/**
	 * 添加监听
	 */
	private void addListener() {
		if (mListview != null) {//pulllistview点击事件
			mListview.setOnItemClickListener(new OnItemClickListener() {

				
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					System.out.println("position" + (position - 1));
					String pro_type = list.get(position - 1).proType;
					String flow_type=list.get(position-1).flowType;//流程类型 0：程序定制 1：客户自定义
					//TODO 跳转到web或者本地
					if (flow_type.equals("0")) {
						if (pro_type.equals(PROCESS_TYPE_VACATE) || pro_type.equals(PROCESS_TYPE_LEAVE)) {
							if(list!=null){
								Intent intent = new Intent(getApplicationContext(), PrYandFDetailActivity.class);
								Bundle bundle=new Bundle();
								bundle.putSerializable("ProcessDataComplete", list.get(position-1));
								intent.putExtras(bundle);
								intent.putExtra("type", intentData);
								PrYandFActivity.this.startActivity(intent);
							}else {
								Toast.makeText(PrYandFActivity.this, "无数据", Toast.LENGTH_SHORT).show();
							}
						} 
						else {
							Toast.makeText(getApplicationContext(), "该流程暂未实现手机端办理，请通过PC端办理。", Toast.LENGTH_SHORT).show();
						}
					}else if (flow_type.equals("1")) {
						if(list!=null){
							Intent intent = new Intent(getApplicationContext(), PrDetailWebActivity.class);
							Bundle bundle=new Bundle();
							bundle.putSerializable("ProcessDataComplete", list.get(position-1));
							intent.putExtras(bundle);
							//TODO type:finished launch
							intent.putExtra("type", intentData);
							PrYandFActivity.this.startActivity(intent);
						}else {
							Toast.makeText(PrYandFActivity.this, "无数据", Toast.LENGTH_SHORT).show();
						}
					}
					else {
						Toast.makeText(getApplicationContext(), "flowType类型错误！", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		if (mStartdate != null) {//开始时间
			mStartdate.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					new DatePickDialog(PrYandFActivity.this, DatePickDialog.YEAR_MONTH_DAY).datePicKDialog(mStartdate);
				}
			});
		}
		if (mEnddate != null) {//结束时间
			mEnddate.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					new DatePickDialog(PrYandFActivity.this, DatePickDialog.YEAR_MONTH_DAY).datePicKDialog(mEnddate);
				}
			});
		}
		mSearch_btn.setOnClickListener(new OnClickListener() {//查询按钮

			
			public void onClick(View arg0) {
				page_index = 1;

				getData();
			}
		});
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

	public void onClick(View view) {
		if (view.getId() == R.id.head_left_btn) {// 返回
			finish();
		}
	}

	/**网络请求
	 */
	@SuppressWarnings("unchecked")
	private void getData() {
		dialog = new LoadingDialog(PrYandFActivity.this);
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {dialog.show(true);}
		System.out.println("getdata dialog exist");
		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("userid", userid);
		ajaxParams.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		ajaxParams.put("page_index", page_index + "");// 当前页码
		ajaxParams.put("page_row", ActivityUtil.pageRow);// 每页显示几条数据
		ajaxParams.put("work_title", mInput.getText().toString());// “” 默认空串查询全部
		ajaxParams.put("start_time", mStartdate.getText().toString());// ””默认空串查询全部
		ajaxParams.put("end_time", mEnddate.getText().toString());// ””默认空串查询全部
		ajaxParams.put("method", "process_state");
		ajaxParams.put("type", intentData);
		System.out.println(HttpUtils.URL+"?" + ajaxParams.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, ajaxParams, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				if (PrYandFActivity.this==null) {
					return;
				}
				strMsg="连接超时";Toast.makeText(getApplicationContext(), strMsg, 1000).show();
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
				// TODO Auto-generated method stub
				super.onLoading(count, current);

			}

			@Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				System.out.println("process_state---"+t.toString());
				if (PrYandFActivity.this==null) {
					return;
				}
				dialog.dismiss();
				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(),JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				
				if (jsonInfo != null) {
					if (JsonInfoV3.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
						try{
//						List<ProcessDataComplete> mlist = jsonInfo.getResultDataKeyToClass("data", ProcessDataComplete.class);
						
						List<ProcessDataComplete> mlist = jsonInfo.getResultDataKeyToClass(t.toString(), "resultData", ProcessDataComplete.class);

						if (page_index == 1) {// 刷新
							if (mlist != null && mlist.size() > 0) {
								if (mListview != null) {
									mListview.setVisibility(View.VISIBLE);
								}
								if (nodata_layout != null) {// 无数据布局隐藏
									nodata_layout.setVisibility(View.GONE);
								}

								list = mlist;
								adapter = new PrYandFAdapter(PrYandFActivity.this, list);
								mListview.setAdapter(adapter);

								if (mlist.size() < Integer.valueOf(ActivityUtil.pageRow)) {// 数据没必要分页时，隐藏“加载更多”按钮
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
						}catch (Exception e) {
							// TODO: handle exception
						}
					} else {
//						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();// 显示失败提示
						Toast.makeText(getApplicationContext(), jsonInfo.getResultDesc(), 1000).show();// 显示失败提示
					}
				}
				
			}
		});
	}
	public void clickEnddate(View v){
		mEnddate.performClick();
	}

	public void clickStartDate(View v){
		mStartdate.performClick();
	}
}
