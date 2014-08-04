package com.hfmb.hfmbapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class HfmbActivityCompanyIntro extends Activity {

	private WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_companyintro);
		
		setLayout();
		
		Intent intent = getIntent();
		
		//int position = intent.getIntExtra("position", 1);
		String companyCd = intent.getStringExtra("companyCd");
		
		// 웹뷰에서 자바스크립트실행가능
        mWebView.getSettings().setJavaScriptEnabled(true); 
        // hfmp 연결..
        mWebView.loadUrl("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_CompanyIntro.jsp?companyCd=" + companyCd);
        //http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_CompanyIntro.jsp
        
        // WebViewClient 지정
        mWebView.setWebViewClient(new WebViewClientClass());
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) { 
            mWebView.goBack(); 
            return true; 
        } 
        return super.onKeyDown(keyCode, event);
    }
     
    private class WebViewClientClass extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            return true; 
        } 
    }
     
    /*
     * Layout
     */
    private void setLayout(){
        mWebView = (WebView) findViewById(R.id.webView);
    }
	
}
