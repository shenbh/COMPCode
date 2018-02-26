package com.newland.comp.activity.my;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.my.MyMessageAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.my.SysMsg;
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
 * 我的消息界面
 * 
 * @author H81
 *
 *         2015年4月22日 下午4:44:54
 * @version 1.0.0
 */
public class MyMessageActivity extends BaseActivity implements IXListViewListener {
	private PullRefreshListView pullRefreshListView;
	private int page_index = 1;// 当前页码
	// private int page_row = 4;// 每页显示条数
	private List<SysMsg> list;
	private MyMessageAdapter myMessageAdapter;
	private View mView;

	// HashMap<String, Boolean> hashMap;// 用来保存点击状态
	LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_message);
		settitle();
		initListView();
		dialog = new LoadingDialog(this);
		reflush();
	}

	/**
	 * 设置标题栏
	 */
	private void settitle() {
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		TextView right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("我的消息");
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
	 * 初始化ListView
	 */
	private void initListView() {
		// hashMap = new HashMap<String, Boolean>();
		pullRefreshListView = (PullRefreshListView) findViewById(R.id.listview);
		pullRefreshListView.setPullLoadEnable(true);
		pullRefreshListView.setXListViewListener(this);
		pullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Intent intent = new Intent(MyMessageActivity.this, MyMessageDetailActivity.class);
//				intent.putExtra("msgid", list.get(position - 1).msgid);
//				intent.putExtra("content", list.get(position - 1).content);
//				intent.putExtra("sender", list.get(position - 1).sender);
//				intent.putExtra("time", list.get(position - 1).msg_time);
//				intent.putExtra("state", list.get(position - 1).statue);// 0未读，
//																		// 1已读
//				startActivity(intent);
				list.get(position - 1).statue = "1";
				myMessageAdapter.notifyDataSetChanged();

			}
		});
	}

	@Override
	public void reflush() {
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {dialog.show(true);}
		String userid = SharedPreferencesUtils.getConfigStr(MyMessageActivity.this, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "sys_msg");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("page_index", page_index + "");// 当前页码
		params.put("page_row", ActivityUtil.pageRow);// 每页显示几条
		System.out.println(HttpUtils.URL+"?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				Log.v("newland", strMsg + t.getMessage());
				// load_msg.setVisibility(View.GONE);
				if (MyMessageActivity.this==null) {
					return;
				}
				dialog.dismiss();
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				Log.v("newland", t.toString());
				// load_msg.setVisibility(View.GONE);
				if (MyMessageActivity.this==null) {
					return;
				}
				dialog.dismiss();
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						List<SysMsg> mlist = jsonInfo.getDataList(SysMsg.class);
						if (page_index == 1) {// 刷新
							if (mlist != null && mlist.size() > 0) {
								// 循环遍历data里的每个项，用带a标签字符串的内容替换带a标签的字符串
								for (int i = 0; i < mlist.size(); i++) {
									String str = mlist.get(i).content;
									mlist.get(i).content=replaceATag( str);
								}
								list = mlist;
								myMessageAdapter = new MyMessageAdapter(MyMessageActivity.this, list);
								pullRefreshListView.setAdapter(myMessageAdapter);

								if (mlist.size() < Integer.valueOf(ActivityUtil.pageRow)) {// 数据没必要分页时，隐藏“加载更多”按钮
									pullRefreshListView.setPullLoadEnable(false);
								} else {
									pullRefreshListView.setPullLoadEnable(true);
								}
							} else {
								Toast.makeText(MyMessageActivity.this, "无响应数据", 500).show();
							}
						} else {
							if (mlist != null && mlist.size() > 0) {
								// 循环遍历data里的每个项，用带a标签字符串的内容替换带a标签的字符串
								for (int i = 0; i < mlist.size(); i++) {
									String str = mlist.get(i).content;
									mlist.get(i).content=replaceATag( str);
								}
								list.addAll(mlist);
								myMessageAdapter.notifyDataSetChanged();
							} else {
								Toast.makeText(MyMessageActivity.this, "最后一页了", 500).show();
							}
						}
					} else {
						Toast.makeText(MyMessageActivity.this, jsonInfov2.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				}
			}
		});
	}
	
	/**替换a标签
	 * @param str
	 * @return
	 */
	public String replaceATag(String str){
		String newStr=str;
		int index = 0, index1 = 0, index2 = 0, index3 = 0, startIndex = 0;
		index = str.indexOf("<a", startIndex);
		while (index != -1) {
			index1 = str.indexOf(">", index);
			index2 = str.indexOf("<", index1);
			index3 = str.indexOf(">", index2);
			if (index2 != 0) {
				String s = str.substring(index, index3 + 1);
				String s1 = str.substring(index1 + 1, index2);
				newStr = str.replace(str.substring(index, index3 + 1), str.substring(index1 + 1, index2));// 用标签内容替换整个标签
				str=newStr;
			}
			startIndex = index3;
			index = str.indexOf("<a", startIndex);
		}
		return newStr;
	}

	public String collect(String content, String regex) {
		String str = "";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		if (m.find()) {
			String value = m.group(1);
			str = value;
		}
		return str;
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
		pullRefreshListView.stopRefresh();
		pullRefreshListView.stopLoadMore();
		String date = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date());
		pullRefreshListView.setRefreshTime(date);
	}

	public void onClick(View view) {
		if (view.getId() == R.id.head_left_btn) {
			this.finish();
			overridePendingTransition(R.anim.finished_move_x_in, R.anim.finfished_move_x_out);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.finished_move_x_in, R.anim.finfished_move_x_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
