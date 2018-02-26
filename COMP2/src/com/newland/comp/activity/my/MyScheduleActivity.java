package com.newland.comp.activity.my;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.my.MyScheduleAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.my.ClassSearchData;
import com.newland.comp.bean.my.ClassSearchInfo;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp.view.PullRefreshListView;
import com.newland.comp.view.PullRefreshListView.IXListViewListener;
import com.newland.comp2.R;

/**
 * 我的-班表查询
 * 
 * @author H81
 * 
 *         2015-4-20 上午11:03:50
 * @version 1.0.0
 */
public class MyScheduleActivity extends BaseActivity implements IXListViewListener{
	// 标题
	private ImageButton left_btn;// 返回
	private ImageButton right_btn;
	private TextView right_tv;

//	private Spinner mKs_spinner;// 科室
//	private Spinner mZq_spinner;// 话务区
	private TextView date_start;// 上班时间起始时间段
	private TextView date_end;// 上班时间结束时间段
	private EditText mInput;// 员工编号
	private Button search_btn;//查询按钮
	private PullRefreshListView pullRefreshListView;
	private View nodata_layout;//无数据
	private EditText username;//用户名子
	
	private MyScheduleAdapter adapter;
	
	private List<ClassSearchData> nopid_list;// 无pid集合
	private List<ClassSearchData> ypid_list;// 有pid集合
	private List<ClassSearchData> ypid_currentVisible_list = new ArrayList<ClassSearchData>();// 有pid集合 并且当前显示专区的集合
	private List<ClassSearchInfo> list;//返回的“班表查询 数据”集合
	
	private String select_ksid="";// 选中的科室id
	private String select_zqid="";//选中的专区id
	private String select_usernum="";//选中的员工编号
	private String select_date="";
	
	
	private int page_index=1;//当前页码
//	private int page_row=4;//每页显示几项数据
	LoadingDialog dialog;
	
	private TextView mDate_start_pulldown; 
	private TextView mDate_end_pulldown;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_schedule);
		bindViews();
		setTitle();
		reflush();
	}

	private void bindViews() {
		date_start = (TextView) findViewById(R.id.date_start);
		date_end = (TextView) findViewById(R.id.date_end);
		mInput = (EditText) findViewById(R.id.input);
		search_btn=(Button) findViewById(R.id.search_btn);
		username = (EditText) findViewById(R.id.username);
		pullRefreshListView=(PullRefreshListView) findViewById(R.id.schedule_listview);
		pullRefreshListView.setPullLoadEnable(true);
		pullRefreshListView.setXListViewListener(this);
		nodata_layout=findViewById(R.id.no_data_layout);
		
		mDate_start_pulldown = (TextView) findViewById(R.id.date_start_pulldown); 
		mDate_end_pulldown = (TextView) findViewById(R.id.date_end_pulldown);
		
		if (pullRefreshListView!=null) {
			pullRefreshListView.setVisibility(View.GONE);
		}
		if (nodata_layout!=null) {//显示无数据布局
			nodata_layout.setVisibility(View.VISIBLE);
		}
		
		nopid_list = new ArrayList<ClassSearchData>();
		ypid_list = new ArrayList<ClassSearchData>();
		//TODO ---
		date_start.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					new DatePickDialog(MyScheduleActivity.this, DatePickDialog.YEAR_MONTH_DAY).datePicKDialog(date_start);
				}
			});
		mDate_start_pulldown.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				new DatePickDialog(MyScheduleActivity.this, DatePickDialog.YEAR_MONTH_DAY).datePicKDialog(date_start);
			}
		});
		date_end.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					new DatePickDialog(MyScheduleActivity.this, DatePickDialog.YEAR_MONTH_DAY).datePicKDialog(date_end);
				}
			});	
		mDate_end_pulldown.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				new DatePickDialog(MyScheduleActivity.this, DatePickDialog.YEAR_MONTH_DAY).datePicKDialog(date_end);
			}
		});
		username.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					username.setText("");		
					mInput.setText("");
				}
			}
		});
		mInput.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					username.setText("");		
					mInput.setText("");
				}
			}
		});
		
		//设置初始时间
		date_start.setText(StringUtil.getNowTime(StringUtil.DAY_TIME));
		date_end.setText(StringUtil.getNowTime(StringUtil.DAY_TIME));
		mInput.setText(SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid"));
		username.setText(SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "username"));
	}
	/**
	 * 设置标题栏
	 */
	private void setTitle() {
		left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (left_btn!=null) {
			left_btn.setVisibility(View.VISIBLE);
		}
		if (center_tv != null)
			center_tv.setText("班表查询");
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {
			right_tv.setVisibility(View.GONE);
		}
	}

	/**
	 * 添加监听
	 */
	private void addListener() {
		
		search_btn.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				page_index=1;
				select_usernum=mInput.getText().toString();
//				select_date=mDate.getText().toString();
				System.out.println("select_ksid  "+select_ksid);
				System.out.println("select_zqid  "+select_zqid);
				System.out.println("select_usernum  "+select_usernum);
				System.out.println("select_date  "+select_date);
				getData();
			}
		});

	}

	/**
	 * 获取查询条件接口
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void reflush() {
		dialog=new LoadingDialog(this);
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {dialog.show(true);}
		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "class_search");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));

		System.out.println(HttpUtils.URL+"?"+ params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				strMsg="连接超时";Toast.makeText(getApplicationContext(), strMsg, 1000).show();
				if (MyScheduleActivity.this==null) {
					return;
				}
			}

			@Override
			public void onSuccess(Object t) {
				if (this==null) {
					return;
				}
				dialog.dismiss();
				System.out.println(t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(),JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				
				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						JsonInfo jsonInfo =   jsonInfov2.getResultDataToClass(JsonInfo.class);
						List<ClassSearchData> classSearchData = jsonInfo.getDataList(ClassSearchData.class);
						if(classSearchData.size()>0)
						{
							ypid_list.clear();
							nopid_list.clear();
							
							ClassSearchData claData = new ClassSearchData();
							claData.id="";
							claData.id_name="全部";
							nopid_list.add(claData);  //默认状态
							for (int i = 0, len = classSearchData.size(); i < len; i++) {
								if (StringUtil.isNotEmpty(classSearchData.get(i).getPid())) {
									ypid_list.add(classSearchData.get(i));// 有pid
								} else {
									nopid_list.add(classSearchData.get(i));// 无pid
								}
							}
							addListener();
							getData();
						}
						
					} else {
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();// 显示失败提示
					}
				}
			}
		});

	}

	/**
	 * 获取员工班列表
	 */
	@SuppressWarnings("unchecked")
	private void getData() {
		dialog=new LoadingDialog(this);
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {
			dialog.show(true);
		}
		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("userid", userid);
		ajaxParams.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		ajaxParams.put("page_index", page_index+"");
		ajaxParams.put("page_row", ActivityUtil.pageRow);
		ajaxParams.put("ks_id", select_ksid);
		ajaxParams.put("zq_id", select_zqid);
		if (StringUtil.isEmpty(select_usernum)) {
			ajaxParams.put("usernum", "");
		}else {
			ajaxParams.put("usernum", select_usernum);
		}
		ajaxParams.put("username", URLEncoder.encode(username.getText().toString()));//
		ajaxParams.put("date_start", date_start.getText().toString());//
		ajaxParams.put("date_end",date_end.getText().toString() );//
		ajaxParams.put("method", "class_yg_search");
		System.out.println(HttpUtils.URL+"?" + ajaxParams.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, ajaxParams, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				strMsg="连接超时";Toast.makeText(getApplicationContext(), strMsg, 1000).show();
				if (MyScheduleActivity.this==null) {
					return;
				}
				if (pullRefreshListView!=null) {
					pullRefreshListView.setVisibility(View.GONE);
				}
				if (nodata_layout!=null) {//显示无数据布局
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
				if (MyScheduleActivity.this==null) {
					return;
				}
				dialog.dismiss();
				System.out.println(t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(),JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
			
				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						JsonInfo jsonInfo =   jsonInfov2.getResultDataToClass(JsonInfo.class);
						List<ClassSearchInfo> mlist = jsonInfo.getDataList(ClassSearchInfo.class);
						
						if (page_index == 1) {// 刷新
							if (mlist != null && mlist.size() > 0) {
								if (pullRefreshListView!=null) {
									pullRefreshListView.setVisibility(View.VISIBLE);
								}
								if (nodata_layout!=null) {//无数据布局隐藏
									nodata_layout.setVisibility(View.GONE);
								}
								
								list = mlist;
								adapter=new MyScheduleAdapter(MyScheduleActivity.this, list);
								pullRefreshListView.setAdapter(adapter);
								
								if (mlist.size() < Integer.valueOf(ActivityUtil.pageRow)) {// 数据没必要分页时，隐藏“加载更多”按钮
									pullRefreshListView.setPullLoadEnable(false);
								} else {
									pullRefreshListView.setPullLoadEnable(true);
								}
							} else {
								if (pullRefreshListView!=null) {
									pullRefreshListView.setVisibility(View.GONE);
								}
								if (nodata_layout!=null) {//显示无数据布局
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
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();// 显示失败提示
					}
				}
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
		pullRefreshListView.stopRefresh();
		pullRefreshListView.stopLoadMore();
		String date = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date());
		pullRefreshListView.setRefreshTime(date);
	}
	public void onClick(View view) {
		if (view.getId() == R.id.head_left_btn) {// 返回
			finish();
		}
	}
}
