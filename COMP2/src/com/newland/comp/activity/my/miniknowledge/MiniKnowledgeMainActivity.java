package com.newland.comp.activity.my.miniknowledge;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp2.R;

/**
 * 微知识首页
 * 
 * @author Administrator
 * 
 */
public class MiniKnowledgeMainActivity extends BaseActivity {

	private Spinner mKeyandtitle;
	private Spinner mTitleItem;
	private EditText mEdt_key;
	private ImageView mSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.miniknowledgemain);
		bindViews();
		setTitle();
	}

	private void bindViews() {

		mKeyandtitle = (Spinner) findViewById(R.id.keyandtitle);
		mTitleItem = (Spinner) findViewById(R.id.title_item);
		mEdt_key = (EditText) findViewById(R.id.edt_key);
		mSearch = (ImageView) findViewById(R.id.search);
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
			center_tv.setText("微知识");
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

	public void onClick(View view) {
		if (view.getId() == R.id.head_left_btn) {// 标题栏左侧“返回”按钮
			this.finish();
		}
	}
}
