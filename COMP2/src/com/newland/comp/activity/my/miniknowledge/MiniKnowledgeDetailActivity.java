package com.newland.comp.activity.my.miniknowledge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.my.FeedbackActivity;
import com.newland.comp.activity.my.MyPerformanceActivity;
import com.newland.comp.activity.my.MyPerformanceDetailActivity;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp2.R;

/**
 * 微知识详情界面
 * 
 * @author Administrator
 * 
 */
public class MiniKnowledgeDetailActivity extends BaseActivity {

	private TextView mKnowledgetitle;
	private TextView mKnowledgetitle_content;
	private TextView mPublishtime;
	private TextView mPublishtime_content;
	private TextView mPublishperson;
	private TextView mPublishperson_content;
	private TextView mKey;
	private TextView mKey_content;
	private TextView mKnowledgeintro;
	private TextView mKnowledgeintro_content;
	private TextView mKnowledgedetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.miniknowledgedetail);
		bindViews();
		setTitle();
	}

	private void bindViews() {

		mKnowledgetitle = (TextView) findViewById(R.id.knowledgetitle);
		mKnowledgetitle_content = (TextView) findViewById(R.id.knowledgetitle_content);
		mPublishtime = (TextView) findViewById(R.id.publishtime);
		mPublishtime_content = (TextView) findViewById(R.id.publishtime_content);
		mPublishperson = (TextView) findViewById(R.id.publishperson);
		mPublishperson_content = (TextView) findViewById(R.id.publishperson_content);
		mKey = (TextView) findViewById(R.id.key);
		mKey_content = (TextView) findViewById(R.id.key_content);
		mKnowledgeintro = (TextView) findViewById(R.id.knowledgeintro);
		mKnowledgeintro_content = (TextView) findViewById(R.id.knowledgeintro_content);
		mKnowledgedetail = (TextView) findViewById(R.id.knowledgedetail);
	}
	/**
	 * 设置标题栏
	 */
	private void setTitle() {
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		TextView right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		bindViews();
		if (center_tv != null)
			center_tv.setText("微知识");
		if (left_btn != null) {// 返回
			left_btn.setVisibility(View.VISIBLE);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {// 日期
			right_tv.setVisibility(View.VISIBLE);
			// 判断textview里面是否有时间内容
			right_tv.setText("反馈问题");
		}
	}
	
	public void onClick(View arg0) {
		if(arg0.getId() ==  R.id.head_right_tv){//反馈意见
			Intent intent= new Intent(this,FeedbackActivity.class);
			//TODO 反馈意见
//			intent.putExtra("", "");
			startActivity(intent);
		}
		if (arg0.getId() == R.id.head_left_btn) {// 标题栏左侧“返回”按钮
			this.finish();
		}
	}
}
