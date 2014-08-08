package com.hfmb.hfmbapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hfmb.hfmbapp.util.DataUtil;

@SuppressLint("SetJavaScriptEnabled")
public class HfmbActivityCompanyIntro extends Activity implements OnTouchListener {

	private WebView oneWebView;
	private WebView twoWebView;
	private WebView threeWebView;
	
	private int position;
	private String companyCd;
	private String companyNm;
	private StringBuffer strbuf;
	
	private ImageView leftImgView, rightImgView;
	
	private TextView textview1;
	
	ViewFlipper flipper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_companyintro_1);
		
		Intent intent = getIntent();
		
		position = intent.getIntExtra("position", 1);
		companyCd = intent.getStringExtra("companyCd");
		companyNm = intent.getStringExtra("companyNm");
		
		setParameter();
		
		setLayout();
		
		setFrameOne();
		setFrameTwo();
		setFrameThree();
		
		leftImgView = (ImageView) findViewById(R.id.imageview_01);
		rightImgView = (ImageView) findViewById(R.id.imageview_02);
		
		leftImgView.setOnClickListener(mOnClickListener);
		rightImgView.setOnClickListener(mOnClickListener);
		
		flipper=(ViewFlipper)findViewById(R.id.viewFlipper1);
		flipper.setOnTouchListener(this);
		
		oneWebView.setOnTouchListener(this);
		twoWebView.setOnTouchListener(this);
		threeWebView.setOnTouchListener(this);
		
		textview1 = (TextView) findViewById(R.id.textview1);
		textview1.setText("기업정보");

        // Show the Up button in the action bar. 
     	setupActionBar();
	}
	
	//서치 버튼 선택시 이벤트.
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override    
		public void onClick(View view) {
			switch(view.getId()) {
			case R.id.imageview_01 : 
				goAnimation(1);//left
				break;
			case R.id.imageview_02 :
				goAnimation(2);//right
				break;
			}
	    }
	};
	
	//parameter setting.
	private void setParameter() {
		strbuf = new StringBuffer();
		strbuf.append("companyCd=" + companyCd);
		strbuf.append("&");
		strbuf.append("companyNm=" + companyNm);
		
		Log.e("Tag", strbuf.toString());
	}
	
	//첫번째 프레임 - ceo 소개, 연혁
	private void setFrameOne() {
		// 웹뷰에서 자바스크립트실행가능
		oneWebView.getSettings().setJavaScriptEnabled(true); 
        // hfmp 연결..
		oneWebView.loadUrl("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_CompanyIntro.jsp?" + strbuf.toString());
        
        // WebViewClient 지정
		oneWebView.setWebViewClient(new WebViewClientClass());
	}
	
	//두번째 프레임 - 사업영역, 제품소개
	private void setFrameTwo() {
		// 웹뷰에서 자바스크립트실행가능
		twoWebView.getSettings().setJavaScriptEnabled(true); 
        // hfmp 연결..
		twoWebView.loadUrl("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_CompanyIntro.jsp?" + strbuf.toString());
        //http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_CompanyIntro.jsp
        
        // WebViewClient 지정
		twoWebView.setWebViewClient(new WebViewClientClass());
	}
	
	//세번째 프레임 - 사업장
	private void setFrameThree() {
		// 웹뷰에서 자바스크립트실행가능
		threeWebView.getSettings().setJavaScriptEnabled(true); 
        // hfmp 연결..
		threeWebView.loadUrl("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_CompanyIntro.jsp?" + strbuf.toString());
        //http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_CompanyIntro.jsp
        
        // WebViewClient 지정
		threeWebView.setWebViewClient(new WebViewClientClass());
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && oneWebView.canGoBack()) { 
        	oneWebView.goBack(); 
            return true; 
        } 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && twoWebView.canGoBack()) { 
        	twoWebView.goBack(); 
            return true; 
        } 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && threeWebView.canGoBack()) { 
        	threeWebView.goBack(); 
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
     * Layout One
     */
    private void setLayout(){
    	oneWebView = (WebView) findViewById(R.id.webViewOne);
    	twoWebView = (WebView) findViewById(R.id.webViewTwo);
    	threeWebView = (WebView) findViewById(R.id.webViewThree);
    }
    
    float xAtDown;
    float xAtUp;
    int count = 0;
    public boolean onTouch(View v, MotionEvent event) {
    	if(v !=oneWebView &&v !=twoWebView && v !=threeWebView && v!=flipper) return false;
    	
    	if(event.getAction() == MotionEvent.ACTION_DOWN){
    		xAtDown=event.getX();  
    	}
    	else if(event.getAction() == MotionEvent.ACTION_UP){
    		xAtUp = event.getX();
    		if(xAtDown > xAtUp){
    			goAnimation(1);//left
    		}
    		else if(xAtDown < xAtUp){
    			goAnimation(2);//right
    		}
    	}
    	return true;
    }
    
    public void goAnimation(int gubun) {
    	switch(gubun) {
    	case 1 :
    		flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.animator.left_in));
    		flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.animator.left_out));
    		count++;
    		if(count < 3)
    			flipper.showNext();
    		else{
    			//Toast.makeText(this, "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();
    			count--;
    		}
    		break;
    	case 2 :
    		flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.animator.right_in));
    		flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.animator.right_out));
    		count--;
    		if(count >- 1)
    			flipper.showPrevious();
    		else{
    			//Toast.makeText(this, "첫번째 페이지 입니다.", Toast.LENGTH_SHORT).show();
    			count++;
    		}
    		break;
    	}
    	Log.e("Tag", "count=" + count);
    	
    	String str = "기업정보";
    	switch(count) {
    	case 0 :
    		str = "기업정보";
    		break;
    	case 1 :
    		str = "사업영역";
    		break;
    	case 2 :
    		str = "사업장";
    		break;
    	}
    	textview1.setText(str);
    }
    
    /**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(false);
			ab.setTitle(companyNm + " 회사소개");
			ab.setSubtitle(DataUtil.ceoNm + DataUtil.temp_01);
		}
	}
	
}
