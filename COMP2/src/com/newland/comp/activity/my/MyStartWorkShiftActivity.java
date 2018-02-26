package com.newland.comp.activity.my;

import java.util.ArrayList;
import java.util.List;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.my.MyStartTurnWorkAdapter;
import com.newland.comp.bean.my.MyStartData;
import com.newland.comp2.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 我的星级  轮岗信息
 * @author H81
 *
 */
public class MyStartWorkShiftActivity extends BaseActivity{

	private ArrayList<MyStartData> turningList;//轮岗信息
	private ListView list_data;
	private View load_msg;
	private MyStartTurnWorkAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_data_tiitle_nopulllistview);
		initView();
	}
	
	private void initView()
	{
		load_msg = findViewById(R.id.load_msg);
		load_msg.setVisibility(View.GONE);
		list_data= (ListView) findViewById(R.id.list_data);
		 TextView  head_center_title = (TextView) findViewById(R.id.head_center_title);
		 head_center_title.setText("星级轮岗");
		 ImageButton  head_left_btn = (ImageButton) findViewById(R.id.head_left_btn);
			head_left_btn.setVisibility(View.VISIBLE);
			head_left_btn.setOnClickListener(new OnClickListener() {
				
				
				public void onClick(View arg0) {
					MyStartWorkShiftActivity.this.finish();
					
				}
			});
			
		turningList = (ArrayList<MyStartData>) getIntent().getSerializableExtra("list");	
		adapter = new MyStartTurnWorkAdapter(this, turningList);
		list_data.setAdapter(adapter);
	}
	
}
