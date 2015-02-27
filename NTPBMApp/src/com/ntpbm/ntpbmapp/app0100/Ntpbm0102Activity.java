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
import android.widget.TextView;

import com.ntpbm.ntpbmapp.HttpConnectServer;
import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;

/*
 * 설치장치의 상세정보 조회하기..
 */
public class Ntpbm0102Activity extends Activity {
	
	private String barcode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0102);
		
		Intent intent = getIntent();
		barcode = intent.getStringExtra("barcode");
		
		//바코드(sn_cd)를 이용한 데이터조회  server connecting... search barcode...
		searchNtpbm0102Info();
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	//                            순번, 품목코드, 브랜드명, 모델명, 식별자, 소모품S/N번호, 유효일자, 확인여부
	private String[] jsonName = {"SEQ", "IT_CD", "IT_NM", "MD_NM", "GD_OT", "SN_S_CD", "EXP_DATE", "CHK_TF"};
	private List<HashMap<String, String>> parseredDataList;
	
	/** 
	 * 바코드(sn_cd)를 이용한 데이터조회  server connecting... search barcode... 
	 */ 
	private void searchNtpbm0102Info() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0102).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
		parseredDataList = server.jsonParserArrayList(resultInfo.toString(), jsonName);
		
		setData();//서버에서 조회한 정보를 화면에 보여준다.
	}

	/*
	 * 서버에서 조회한 정보를 화면에 보여준다.
	 */
	private void setData() {
		if (parseredDataList != null) {
			HashMap<String, String> parseredData = null;
			for(int i = 0; i < parseredDataList.size(); i++) {
				parseredData = parseredDataList.get(i);

				//모델명 기본정보 조회하기.
				if ( i == 0) {
					((TextView)findViewById(R.id.serverTxtNm02)).setText(parseredData.get(jsonName[2]));//브랜드
					((TextView)findViewById(R.id.serverTxtNm04)).setText(parseredData.get(jsonName[3]));//모델명
					((TextView)findViewById(R.id.serverTxtNm06)).setText(parseredData.get(jsonName[4]));//식별자
					((TextView)findViewById(R.id.serverTxtNm08)).setText(parseredData.get(jsonName[5]));///시리얼번호
				}
			}
		}
		
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm02)).setText("");//제조번호
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm04)).setText("");//제조일자
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm06)).setText("");//제조회사
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm08)).setText("");//제조국
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm10)).setText("");//구입처명
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm12)).setText("");//구입처주소
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm14)).setText("");//구입처담당자
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm16)).setText("");//구입처연락처
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	
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
