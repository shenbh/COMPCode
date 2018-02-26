package com.newland.comp.test;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.LoginActivity;
import com.newland.comp.bean.UserInfo;
import com.newland.comp.bean.indicator.IndicatorOperationItem;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp2.R;

public class TestActivity extends BaseActivity{
	
	private WebView mWebView;
	 private Handler mHandler = new Handler();  
	 DemoJavaScriptInterface js;
	 private EditText mEdit;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity);
//		testWebView();
		testLogin();
	}
	
	@SuppressWarnings("unchecked")
	private void testLogin()
	{
		AjaxParams params = new AjaxParams();
		params.put("userid", "24018175");
		params.put("pwd", "admin");  
		params.put("imei", "242343");  
		params.put("method","login");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid("24018175"));
		System.out.println(params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL , params,
				new AjaxCallBack()
				{
					@Override
					public void onLoading(long count, long current)
					{
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg)
					{
						System.out.println(strMsg);
					}
				

					@Override
					public void onSuccess(Object t)
					{
						System.out.println(t.toString());

					}
				});
	}
	
	
	
	private void testTimePick()
	{
		 mEdit = (EditText) findViewById(R.id.edit);
		
		mEdit.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				new DatePickDialog(TestActivity.this,DatePickDialog.YEAR_MONTH).datePicKDialog(mEdit);
				
			}
		});
		
	}
	
	
	
	/**
	 * 测试Json
	 */
	private void testJson()
	{
//		UserInfo userInfo = new UserInfo("1","21323","afewfgew","0");
//		UserInfo userInfo2 = new UserInfo("2","111","aewfe","0");
		List<UserInfo> list = new ArrayList<UserInfo>();
//		List<UserInfo> list2 = new ArrayList<UserInfo>();
//		list.add(userInfo);
//		list2.add(userInfo2);
		JsonInfo jsonInfo = new JsonInfo("0","成功",list);
//		jsonInfo.setData2(list2);
		IndicatorOperationItem indicatorOperationItem = new IndicatorOperationItem();
		indicatorOperationItem.setIndicator_id("1");
		
		jsonInfo.setData_exp(indicatorOperationItem);
		String str = JSON.toJSONString(jsonInfo);
		System.out.println(str);
		
		JsonInfo jsonInfo2 =  JSON.parseObject(str, JsonInfo.class);
//		String str2 = JSON.toJSONString(jsonInfo2.getData());
//		List<UserInfo> list2 = JSON.parseArray(str2, UserInfo.class);
		List<UserInfo> list3  = jsonInfo2.getDataList(UserInfo.class);
		for (UserInfo userInfo3 : list3) {
			System.out.println(userInfo3);
		}
		IndicatorOperationItem indicatorOperationItem2 = jsonInfo2.getData_exp(IndicatorOperationItem.class);
		System.out.println(indicatorOperationItem2);
	}
	@JavascriptInterface
	private void testWebView()
	{
		mWebView = (WebView) findViewById(R.id.webview);  
		js = new DemoJavaScriptInterface();
		WebSettings webSettings = mWebView.getSettings();       
        webSettings.setJavaScriptEnabled(true);     
        mWebView.setWebChromeClient(new WebChromeClient());
//	    mWebView.addJavascriptInterface(js, "Android");
	    mWebView.loadUrl("file:///android_asset/line-labels.htm"); 
		  
//			UserInfo userInfo = new UserInfo("1","21323","afewfgew","0");
//			UserInfo userInfo2 = new UserInfo("2","111","aewfe","0");
			List<UserInfo> list = new ArrayList<UserInfo>();
//			list.add(userInfo);
//			list.add(userInfo2);

//			final String data = JSON.toJSONString(userInfo);
			mWebView.setWebViewClient(new WebViewClient(){
			    public void onPageFinished(WebView view, String url){   
//			    	mWebView.loadUrl("javascript:test()");
//			    	mWebView.loadUrl("javascript:test('"+data+"')");
			    }           
			});
	}
	@JavascriptInterface
	public void run_js(View view)
	{
		 
		  mHandler.post(new Runnable() {
            public void run() {
            	String data = "fergtrhthytjy";
            	mWebView.loadUrl("javascript:test('"+data+"')");
            }
        });
	}
	
	  final class DemoJavaScriptInterface {

	        DemoJavaScriptInterface() {
	        }

	        /**
	         * This is not called on the UI thread. Post a runnable to invoke
	         * loadUrl on the UI thread.
	         */
	        @JavascriptInterface
	        public void clickOnAndroid() {
	        	System.out.println("接收js参数"); 
	            mHandler.post(new Runnable() {
	                public void run() {
	                	Toast.makeText(getApplicationContext(), "js", Toast.LENGTH_SHORT).show();
	                }
	            });

	        }
	
	  } 
	  /**
	     * Provides a hook for calling "alert" from javascript. Useful for
	     * debugging your javascript.
	     */
	    final class MyWebChromeClient extends WebChromeClient {
	        @Override
	        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
	            result.confirm();
	            return true;
	        }
	        
	   
	    }
	    
	    
	    private void testMd5()
	    {
	    	System.out.println(MD5Utils.getInstance().getUserIdSignid("24235"));
	    }
	    

}

