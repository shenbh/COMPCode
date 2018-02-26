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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.my.PersonListAdapter;
import com.newland.comp.bean.my.PersonList;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp.view.PullRefreshListView.IXListViewListener;
import com.newland.comp2.R;

/**
 * 人员选择列表 无搜索列表
 * 
 * @author H81
 *
 */
public class MyPersonListNoSearchActivity extends BaseActivity {

	private ListView listview;
	private LoadingDialog loading;
	private String type;
	private int page = 1; // 当前页
	private List<PersonList> list;
	private PersonListAdapter adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personlist_nosearch);
		bindViews();
		addListViewLinster();
	}

	private void bindViews() {

		listview = (ListView) findViewById(R.id.list_data);
		list = (List<PersonList>) getIntent().getSerializableExtra("list");
		TextView title = (TextView) findViewById(R.id.head_center_title);
		title.setText("人员列表选择");
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		left_btn.setVisibility(View.VISIBLE);
		left_btn.setOnClickListener(new OnClickListener() {

			
			public void onClick(View arg0) {
				MyPersonListNoSearchActivity.this.finish();
			}
		});
		adapter = new PersonListAdapter(this, list);
		listview.setAdapter(adapter);
	}

	/**
	 * listview的监听
	 */
	private void addListViewLinster() {
		listview.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				PersonList bean = list.get(position);
				Intent intent = new Intent();
				intent.putExtra("userid", bean.userid);
				intent.putExtra("username", bean.username);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}

		});
	}

	@Override
	public void onBackPressed() {

		setResult(Activity.RESULT_CANCELED);
		finish();
	}

}
