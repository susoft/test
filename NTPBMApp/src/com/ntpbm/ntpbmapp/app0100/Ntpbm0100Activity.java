package com.ntpbm.ntpbmapp.app0100;

import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ntpbm.ntpbmapp.HttpConnectServer;
import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;
import com.ntpbm.ntpbmapp.Util;

/*
 * 바코드  or 직접입력 처리화면
 */
public class Ntpbm0100Activity extends Activity implements OnClickListener {
	
	private String message;
	private String scanContent;
	private String scanFormat;
	
	private View mPage1, mPage2;
	private TextView formatTxt, contentTxt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0100);
		
		Intent intent = getIntent();
		message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		mPage1 = findViewById(R.id.ntpbm_0100_page1);
		mPage2 = findViewById(R.id.ntpbm_0100_page2);
		
		formatTxt = (TextView)findViewById(R.id.scan_format);
		contentTxt = (TextView)findViewById(R.id.scan_content);
		
		findViewById(R.id.ntpbm_0100_btn01).setOnClickListener(this);
		findViewById(R.id.ntpbm_0100_btn02).setOnClickListener(this);
		findViewById(R.id.ntpbm_0100_btn03).setOnClickListener(this);
		findViewById(R.id.ntpbm_0100_btn04).setOnClickListener(this);
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ntpbm_0100_btn01://바코드 인식 버튼.
			mPage2.setVisibility(View.INVISIBLE);
			mPage1.setVisibility(View.VISIBLE);
			
			//instantiate ZXing integration class
			IntentIntegrator scanIntegrator = new IntentIntegrator(Ntpbm0100Activity.this);
			//start scanning
			scanIntegrator.initiateScan();
			break;
		case R.id.ntpbm_0100_btn02://직접입력 버튼.
			mPage1.setVisibility(View.INVISIBLE);
			mPage2.setVisibility(View.VISIBLE);
			break;
		case R.id.ntpbm_0100_btn03://입력 또는 인식된 바코드를 이용한 조회
			EditText editText = (EditText) findViewById(R.id.barcodeVal);
			String barcodeVal = editText.getText().toString();
			
			searchBarCode(barcodeVal);
			break;
		case R.id.ntpbm_0100_btn04://취소.
			MainActivity.loginYn = 1;
			
			this.finish();
			break;
		}
	}
	
	//바코드인식 프로그램에서 데이터 받음...
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//retrieve result of scanning - instantiate ZXing object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		//check we have a valid result
		if (scanningResult != null) {
			//get content from Intent Result
			scanContent = scanningResult.getContents();
			//get format name of data scanned
			scanFormat = scanningResult.getFormatName();
			//output to UI
			formatTxt.setText("FORMAT: "+scanFormat);
			contentTxt.setText("CONTENT: "+scanContent);
			
			searchBarCode(scanFormat);
		}
		else{
			//invalid scan data or scan canceled
			Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	/** 
	 * Called when the user clicks the searchBarCode button 
	 * 바코드를 이용한 조회...
	 */ 
	public void searchBarCode(String barcodeVal) {
		// Do something in response to button

		if (barcodeVal == null || "".equals(barcodeVal)) {
			Util.DialogSimple("조회정보 미입력", "코드정보를 입력하세요.", this);
		} else {
			Intent intent;
			if (message.equals(MainActivity.ntpbm_0200)) {//장비조회
				intent = new Intent(this, Ntpbm0107Activity.class);
			} else {//설치조회
				intent = new Intent(this, Ntpbm0101Activity.class);
			}
			
			/*if (!barcodeVal.contains("SN")) {
				barcodeVal = "SN" + barcodeVal;
			}*/
			searchNtpbm0100Info(barcodeVal.toUpperCase(), intent);//데이터 조회하기.
		}
	}
	
	/** 
	 * 바코드(sn_cd)를 이용한 데이터조회
	 */ 
	private void searchNtpbm0100Info(String barcodeVal, Intent intent) {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcodeVal);
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0100).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
		String[] jsonName = {"SEQ", "IT_CD", "IT_NM", "MD_NM", "GD_OT", "SN_S_CD", "EXP_DATE", "CHK_TF"};
		List<HashMap<String, String>> parseredDataList = server.jsonParserArrayList(resultInfo.toString(), jsonName);
		
		if (parseredDataList != null && parseredDataList.size() > 0) {
			intent.putExtra(MainActivity.EXTRA_MESSAGE, message);
			intent.putExtra("barcode", barcodeVal);
			startActivity(intent);
		} else {
			//다이얼로그 띄우기...
			Util.DialogSimple("데이터 없음", barcodeVal +  "에 해당된 데이터가 존재하지 않습니다.", this);
		}
	}
	
	////////////////////////////////////////////////////////////////////

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(false);
		    ab.setTitle(R.string.app_name);
		    ab.setSubtitle("설치확인");
		}
	}
	
	// Option menu에 아이템 채움.
	// Option menu 호출 시 한번말 실행됨.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.main01, menu);
		
	    return super.onPrepareOptionsMenu(menu);
	}
	
	// Option menu item이 선택되었을때 발생한는 event의 handler
	// 발생한 event가 이 메소드나 super 클래스의 원래 메소드에서 처리 되지 않으면 false리턴 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		int itemId = item.getItemId();
		
		switch (itemId) {
		case R.id.action_login:
			Intent intent = new Intent(this, Ntpbm0001Activity.class);
			startActivity(intent);
			break;
		case R.id.action_logout:
			MainActivity.loginYn = -1;
			this.finish();
			break;
		case R.id.action_personinfo:
			Intent intent2 = new Intent(this, Ntpbm0002Activity.class);
			startActivity(intent2);
			break;
		}
		
		return true;
	}
	
}

