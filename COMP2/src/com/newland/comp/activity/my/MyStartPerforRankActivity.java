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
 * 绩效等级 我的星级
 * @author H81
 *
 */
public class MyStartPerforRankActivity extends BaseActivity{
	
	private MyStartExp bean;
    private WebView webView;
    private LinearLayout mJf_detail;
    private TextView mJx_rank;
    private TextView mJx_rank_sum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_performance_rank);
		bindViews();
		initData();
		initWebView();
	}
	
	

    private void bindViews() {

    	webView = (WebView) findViewById(R.id.webview);
        mJf_detail = (LinearLayout) findViewById(R.id.jf_detail);
        mJx_rank = (TextView) findViewById(R.id.jx_rank);
        mJx_rank_sum = (TextView) findViewById(R.id.jx_rank_sum);
    }
    
    
    private void initData()
    {
    	bean = (MyStartExp) getIntent().getSerializableExtra("bean");
    	mJx_rank.setText(StringUtil.noNull(bean.jx_level,"0"));
    	mJx_rank_sum.setText(StringUtil.noNull(bean.jx_level_val,"0"));
    	TextView head_center_title = (TextView) findViewById(R.id.head_center_title);
		head_center_title.setText("绩效等级");
		ImageButton head_left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		head_left_btn.setVisibility(View.VISIBLE);
		head_left_btn.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				MyStartPerforRankActivity.this.finish();
				
			}
		});
    }
    
	private void initWebView() {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportMultipleWindows(true);
		webView.setWebChromeClient(new WebChromeClient());
		webView.loadUrl("file:///android_asset/mystart_perfor_rank.htm");
		  final String data = JSON.toJSONString(bean);
	    webView.setWebViewClient(new WebViewClient() {
				public void onPageFinished(WebView view, String url) {
					// mWebView.loadUrl("javascript:test()");
			
						webView.loadUrl("javascript:init_data('" + data + "')");
		
				
				}
			});
	}
    
	
}
