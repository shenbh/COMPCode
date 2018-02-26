package com.newland.comp.activity.my;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.indicator.IndicatorDetailActivity;
import com.newland.comp.adapter.my.PersonListAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.my.PersonList;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp.view.PullRefreshListView.IXListViewListener;
import com.newland.comp2.R;

/**
 * 人员选择列表
 * @author H81
 *
 */
public class MyPersonListActivity extends BaseActivity implements IXListViewListener{

	 private EditText mEd_person;
	 private com.newland.comp.view.PullRefreshListView pullRefreshListView;
	 private LoadingDialog loading;
	 private String type;
	 private int page = 1; // 当前页
	 private List<PersonList> list;
	 private PersonListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personlist);
		bindViews();
		refrush();
	}
	
	 private void bindViews() {

	        mEd_person = (EditText) findViewById(R.id.ed_person);
	        pullRefreshListView = (com.newland.comp.view.PullRefreshListView) findViewById(R.id.list_data);
	        type =getIntent().getStringExtra("type");
	        TextView title = (TextView) findViewById(R.id.head_center_title);
			title.setText("人员列表选择");
			ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
			left_btn.setVisibility(View.VISIBLE);
			left_btn.setOnClickListener(new OnClickListener() {

				
				public void onClick(View arg0) {
					MyPersonListActivity.this.finish();
				}
			});
	 }
	 
	 
	 /**
	  * 搜索人民
	  * @param view
	  */
	 @SuppressWarnings("unchecked")
	public void btn_search(View view)
	 {
		 refrush();
	 }

	@SuppressWarnings("unchecked")
	private void refrush() {
		loading = new LoadingDialog(this);
		 loading.show(true);
			String userid = SharedPreferencesUtils.getConfigStr(this, BaseActivity.CASH_NAME, "userid");
			FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
			AjaxParams params = new AjaxParams();
			params.put("userid", userid);
			params.put("method", "next_userid");
			params.put("search_name", mEd_person.getText().toString());
			params.put("type", type);
			params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
			params.put("page_index", page + "");
			params.put("page_row", ActivityUtil.pageRow + "");
			System.out.println(HttpUtils.URL+"?" + params.toString());
			fh.post(HttpUtils.URL, params, new AjaxCallBack() {
				@Override
				public void onLoading(long count, long current) {
				}

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
					strMsg="连接超时";Toast.makeText(getApplicationContext(), strMsg, 1000).show();
					if (MyPersonListActivity.this==null) {
						return;
					}
					if (loading != null) {
						loading.dismiss();
					}
				}

				@Override
				public void onSuccess(Object t) {
					if (MyPersonListActivity.this==null) {
						return;
					}
					System.out.println(t.toString());
					if (loading != null) {
						loading.dismiss();
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
							List<PersonList> mlist = jsonInfo.getDataList(PersonList.class);
							if (page == 1) {// 刷新

								if (mlist != null && mlist.size() > 0) {
									list = mlist;
									adapter = new PersonListAdapter(getApplicationContext(), mlist );
									pullRefreshListView.setAdapter(adapter);
									addListViewLinster();
									onLoad();
									pullRefreshListView.setPullLoadEnable(false); //加载更多隐藏起来，这个页面就是全部显示人员
									
								} else {
									if (list != null) {
										list.clear();
										adapter.notifyDataSetChanged();
									}

									Toast.makeText(getApplicationContext(), "无响应数据", 500).show();
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
							Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();// 显示登录失败提示
						}
					}
				}
			});
	}
	 
	
	/**
	 * listview的监听
	 */
	private void addListViewLinster()
	{
		pullRefreshListView.setPullLoadEnable(true);
		pullRefreshListView.setXListViewListener(this);
		pullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position > 0) {
					System.out.println("点击" + position);
					PersonList bean = list.get(position-1);
					Intent intent = new Intent(getApplicationContext(), IndicatorDetailActivity.class);
					intent.putExtra("userid", bean.userid);
					intent.putExtra("username", bean.username);
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			}
		});
	}
	
	
	
	
	
	 /**
		 * 复写listview刷新和加载方法
		 */
		private void onLoad() {
			pullRefreshListView.stopRefresh();
			pullRefreshListView.stopLoadMore();
			String date = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date());
			pullRefreshListView.setRefreshTime(date);
		}

		
		public void onListViewRefresh() {
			page = 1;
			refrush();
			onLoad();
		}

		
		public void onListViewLoadMore() {
			page++;
			refrush();
			onLoad();
		}
}
