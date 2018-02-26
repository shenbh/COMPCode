package com.newland.comp.activity.my;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.my.MyStartExp;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 培训学习  我的星级
 * @author H81
 *
 */
public class MyStartTrainActivity extends BaseActivity{
	
    private TextView mScore;
    private WebView webView;
    MyStartExp bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_start_train);
		bindViews();
		initData();
		initWebView();
	}
	
	

    private void bindViews() {

           mScore = (TextView) findViewById(R.id.score);
           webView = (WebView) findViewById(R.id.webview);
    }
    
    
    private void initData()
    {
    	bean = (MyStartExp) getIntent().getSerializableExtra("bean");
    	mScore.setText(StringUtil.noNull(bean.px_val,"0"));
    	TextView head_center_title = (TextView) findViewById(R.id.head_center_title);
		head_center_title.setText("培训学习");
		ImageButton head_left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		head_left_btn.setVisibility(View.VISIBLE);
		head_left_btn.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				MyStartTrainActivity.this.finish();
				
			}
		});
    }
    
	private void initWebView() {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportMultipleWindows(true);
		webView.setWebChromeClient(new WebChromeClient());
		webView.loadUrl("file:///android_asset/mystart_train.htm");
		  final String data = JSON.toJSONString(bean);
	    webView.setWebViewClient(new WebViewClient() {
				public void onPageFinished(WebView view, String url) {
					// mWebView.loadUrl("javascript:test()");
			
						webView.loadUrl("javascript:init_data('" + data + "')");
		
				
				}
			});
	}
	
}
