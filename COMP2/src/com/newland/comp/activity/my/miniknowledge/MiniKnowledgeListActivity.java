package com.newland.comp.activity.my.miniknowledge;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp2.R;

/**
 * 微知识列表界面--------需要整到首页的Fragment里
 * 
 * @author Administrator
 * 
 */
public class MiniKnowledgeListActivity extends BaseActivity {

	private TextView mSearchresult;// 查询到几条结果
	private ListView mListview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.miniknewledgelist);
		bindViews();
		setTitle("微知识", "");
	}

	private void bindViews() {
		mSearchresult = (TextView) findViewById(R.id.searchresult);
		mListview = (ListView) findViewById(R.id.listview);
	}

	public void onClick(View arg0) {
		if (arg0.getId() == R.id.head_left_btn) {// 标题栏左侧“返回”按钮
			this.finish();
		}
	}

	/*
	 * userid;//工号 signid: md5(userid+'comp') typeId:知识目录ID title:知识标题
	 * keyWords:关键字 summary:知识简介 content:知识内容 queryType: new:最新,hot:最热
	 * method：getTrInfoQuery
	 */
	@Override
	public void reflush() {
		String userid = SharedPreferencesUtils.getConfigStr(this, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
//		params.put("typeId", file);
//		params.put("title", file);
//		params.put("keyWords", file);
//		params.put("summary", file);
//		params.put("content", file);
//		params.put("queryType", file);
		params.put("method", "getTrInfoQuery");
		System.out.println(params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, new AjaxCallBack<Object>() {

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
			}
		});

	}
}
