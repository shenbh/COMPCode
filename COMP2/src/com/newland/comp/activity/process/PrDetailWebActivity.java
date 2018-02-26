package com.newland.comp.activity.process;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.process.ProcessDataComplete;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp2.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 流程详情web展示
 * 
 * @author Administrator
 *
 */
public class PrDetailWebActivity extends PrToDoActivity {

	private WebView mPrdetail_webview;
	String pro_type;
	Intent intent ;
	ProcessDataComplete processDataComplete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prdetail);
		getIntentData();
		bindViews();
		loadWebview();
		setTitle();
	}

	private void setTitle(){
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("流程详情");
		if (left_btn != null) {// 返回
			left_btn.setVisibility(View.VISIBLE);
			left_btn.setOnClickListener(new OnClickListener() {

				
				public void onClick(View arg0) {
					PrDetailWebActivity.this.finish();
				}
			});
		}
	}
	private void getIntentData() {
//		pro_type = intent.getStringExtra("pro_type");
//		type = intent.getStringExtra("type");
		intent = getIntent();
		processDataComplete=(ProcessDataComplete) intent.getSerializableExtra("ProcessDataComplete");
	}

	private void bindViews() {

		mPrdetail_webview = (WebView) findViewById(R.id.prdetail_webview);
	}

	
													
	/**
	 * http://10.46.217.132:8990/csoa/jsp/wkflow/wk_index.jsp?task_id=3206&flow_id=997&node_id=617&biz_id=100000003&flow_code=%E7%BE%A4%E5%8F%91%E4%BB%BB%E5%8A%A1%E5%88%86%E9%85%8D%E6%B5%81%E7%A8%8B&def_id=224&url=%5Cnlsf%5Cjsp%5CformRunning.jsp
	 */
	private void loadWebview() {
		StringBuffer urlStr=new StringBuffer();
		
		try {
			
			urlStr.append(HttpUtils.URL_PROCESS);
			
			urlStr.append("task_id=");
			urlStr.append(URLEncoder.encode(processDataComplete.taskId,"UTF-8"));
			urlStr.append("&");
			
			urlStr.append("flow_id=");
			urlStr.append(URLEncoder.encode(processDataComplete.flowId,"UTF-8"));
			urlStr.append("&");
			
			urlStr.append("node_id=");
			urlStr.append(URLEncoder.encode(processDataComplete.nodeId,"UTF-8"));
			urlStr.append("&");
			
			urlStr.append("biz_id=");
			urlStr.append(URLEncoder.encode(processDataComplete.bizId,"UTF-8"));
			urlStr.append("&");
			
			urlStr.append("flow_code=");
			urlStr.append(URLEncoder.encode(processDataComplete.flowCode,"UTF-8"));
			urlStr.append("&");
			
			urlStr.append("def_id=");
			urlStr.append(URLEncoder.encode(processDataComplete.defId,"UTF-8"));
			urlStr.append("&");
			
			urlStr.append("url=");
			urlStr.append(URLEncoder.encode(processDataComplete.formPage,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		initDialog(this);
		
		WebSettings webSettings = mPrdetail_webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mPrdetail_webview.loadUrl(urlStr.toString());
		
		mPrdetail_webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				dialog.dismiss();
			}
		});
	}
	
}
