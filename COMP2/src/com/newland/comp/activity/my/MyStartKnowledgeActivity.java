package com.newland.comp.activity.my;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.my.MyStartExp;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 知识传承  我的星级
 * @author H81
 *
 */
public class MyStartKnowledgeActivity extends BaseActivity{
	
	private MyStartExp bean;
    private TextView mSugget;
    private TextView mAction;
    private TextView mCourse;
    private TextView mMy_case;
    private TextView mTraining;
    private TextView mZscc_score;
    private TextView mIs_bear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_start_know);
		bindViews();
		initData();
	}
	
	
	 private void bindViews() {

	        mSugget = (TextView) findViewById(R.id.sugget);
	        mAction = (TextView) findViewById(R.id.action);
	        mCourse = (TextView) findViewById(R.id.course);
	        mMy_case = (TextView) findViewById(R.id.my_case);
	        mTraining = (TextView) findViewById(R.id.training);
	        mZscc_score = (TextView) findViewById(R.id.zscc_score);
	        mIs_bear = (TextView) findViewById(R.id.is_bear);
	    }
    
    
    private void initData()
    {
    	bean = (MyStartExp) getIntent().getSerializableExtra("bean");
    	mSugget.setText(StringUtil.noNull(bean.sugget,"0") + "个");
    	mAction.setText(StringUtil.noNull(bean.action,"0") + "个");
    	mCourse.setText(StringUtil.noNull(bean.course,"0") + "个");
    	mMy_case.setText(StringUtil.noNull(bean.my_case,"0") + "个");
    	mTraining.setText(StringUtil.noNull(bean.training,"0") + "小时");
    	mZscc_score.setText(StringUtil.noNull(bean.zscc_score,"0"));
    	mIs_bear.setText(StringUtil.noNull(bean.is_bear,"0"));
    	
    	TextView head_center_title = (TextView) findViewById(R.id.head_center_title);
		head_center_title.setText("知识传承");
		ImageButton head_left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		head_left_btn.setVisibility(View.VISIBLE);
		head_left_btn.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				MyStartKnowledgeActivity.this.finish();
				
			}
		});
    }
	
}
