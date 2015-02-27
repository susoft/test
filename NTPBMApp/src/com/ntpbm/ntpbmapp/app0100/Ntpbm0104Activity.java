package com.ntpbm.ntpbmapp.app0100;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ntpbm.ntpbmapp.HttpConnectServer;
import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;
import com.ntpbm.ntpbmapp.Util;

/*
 * 설치장치의 설치장소
 */
public class Ntpbm0104Activity extends Activity {
	
	private String barcode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0104);
		
		Intent intent = getIntent();
		barcode = intent.getStringExtra("barcode");
		
		//바코드(sn_cd)를 이용한 데이터조회  server connecting... search barcode...
		searchNtpbm0104Info();
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	
	/** 
	 * Called when the user clicks the saveInstall button 
	 * 수정된 정보를 저장한다.
	 */ 
	public void saveInstall(View view) {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//변경정보 생성하기.
		strbuf.append("&cha_place=" + ((EditText)findViewById(R.id.ntpbm_0104_edit01)).getText());//설치장소
		strbuf.append("&cha_addr=" + ((EditText)findViewById(R.id.ntpbm_0104_edit02)).getText());//설치주소
		strbuf.append("&cha_addr_dti=" + ((EditText)findViewById(R.id.ntpbm_0104_edit03)).getText());//설치상세주소
		strbuf.append("&cha_nm=" + ((EditText)findViewById(R.id.ntpbm_0104_edit04)).getText());//담당자
		strbuf.append("&cha_tel=" + ((EditText)findViewById(R.id.ntpbm_0104_edit05)).getText());//전화번호
		strbuf.append("&cha_hp=" + ((EditText)findViewById(R.id.ntpbm_0104_edit06)).getText());//휴대전화
		strbuf.append("&cha_e_mail=" + ((EditText)findViewById(R.id.ntpbm_0104_edit07)).getText());//이메일
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0104_update).toString();
		
		Log.i("json:strbuf", strbuf.toString());
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttpPair(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		Util.DialogSimple("처리결과", "정상처리되었습니다.", this);
	}
	
	/** 
	 * Called when the user clicks the cancelInstall button 
	 * 수정 취소
	 */ 
	public void cancelInstall(View view) {
		// Do something in response to button
		this.finish();
	}

	//설치장소, 설치주소, 설치상세주소, 담당자, 전화번호, 휴대전화, 이메일, 우편번호, 주소 + 상세주소
	private String[] jsonName = {"CHA_PLACE", "CHA_ADDR", "CHA_ADDR_DTI", "CHA_NM", "CHA_TEL", "CHA_HP", "CHA_E_MAIL", "CHA_ZIP_NUM", "CHA_ADDRESS"};
	private List<HashMap<String, String>> parseredDataList;
	
	/** 
	 * 바코드(sn_cd)를 이용한 데이터조회  server connecting... search barcode... 
	 */ 
	private void searchNtpbm0104Info() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0104).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
		parseredDataList = server.jsonParserArrayList(resultInfo.toString(), jsonName);

		//서버에서 조회한 정보를 화면에 보여준다.
		setData();
	}

	/*
	 * 서버에서 조회한 정보를 화면에 보여준다.
	 */
	private void setData() {
		if (parseredDataList != null) {
			HashMap<String, String> parseredData = null;
			for(int i = 0; i < parseredDataList.size(); i++) {
				parseredData = parseredDataList.get(i);
				
				((EditText)findViewById(R.id.ntpbm_0104_edit01)).setText(parseredData.get(jsonName[0]));
				((EditText)findViewById(R.id.ntpbm_0104_edit02)).setText(parseredData.get(jsonName[1]));//주소
				((EditText)findViewById(R.id.ntpbm_0104_edit03)).setText(parseredData.get(jsonName[2]));//상세주소
				((EditText)findViewById(R.id.ntpbm_0104_edit04)).setText(parseredData.get(jsonName[3]));
				((EditText)findViewById(R.id.ntpbm_0104_edit05)).setText(parseredData.get(jsonName[4]));
				((EditText)findViewById(R.id.ntpbm_0104_edit06)).setText(parseredData.get(jsonName[5]));
				((EditText)findViewById(R.id.ntpbm_0104_edit07)).setText(parseredData.get(jsonName[6]));
				//((EditText)findViewById(R.id.ntpbm_0104_edit08)).setText(parseredData.get(jsonName[7]));//납일일자, 현재일자
			}
		}
		
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		((EditText)findViewById(R.id.ntpbm_0104_edit08)).setText(timeStamp);//납일일자, 현재일자
	}
	
	//날짜 세팅하기..(date piker...)
	public int year, month, day, hour, minute;
	
	public void datepikerD(View view) {
		new DatePickerDialog(this, dateSetListener, year, month, day).show();
	}
	
	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
             // TODO Auto-generated method stub
        	setDate(year, monthOfYear, dayOfMonth);
        }
	};
	
	public void setDate(int years, int monthOfYear, int dayOfMonth) {
		this.year = years;
    	this.month = monthOfYear;
    	this.day = dayOfMonth;
    	
    	String msg = String.format("%d - %d - %d", year, monthOfYear+1, dayOfMonth);
    	((EditText)findViewById(R.id.ntpbm_0104_edit08)).setText(msg);
    	
    	Log.i("", msg);
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

