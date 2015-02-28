package com.ntpbm.ntpbmapp;

import java.io.File;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ntpbm.ntpbmapp.app0100.Ntpbm0100Activity;
import com.ntpbm.ntpbmapp.app0300.Ntpbm0300Activity;
import com.ntpbm.ntpbmapp.app0400.Ntpbm0400Activity;

public class Ntpbm0001Activity extends Activity implements OnClickListener {
	
	
	private String message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0001);
		
		Intent intent = getIntent();
		message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		((EditText)findViewById(R.id.idVal)).setPrivateImeOptions("defaultInputmode=english;");
		((EditText)findViewById(R.id.idVal)).setPrivateImeOptions("defaultInputmode=english;");
		
		findViewById(R.id.loginBtn).setOnClickListener(this);
		findViewById(R.id.cancelBtn).setOnClickListener(this);
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(false);
		    ab.setTitle(R.string.app_name);
		    ab.setSubtitle("Login");
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			
			//화면에서 서버에 넘겨줄 데이터를 가져온다.
			EditText loginid = (EditText)findViewById(R.id.idVal);
			EditText loginpwd = (EditText)findViewById(R.id.pwdVal);
			
			if (loginid.getText() == null || loginid.getText().toString().trim().equals("") || loginid.getText().toString().trim() == "") {
				Util.DialogSimple("정보 미입력", "로그인 아이디를 입력하세요", this);
				return;
			}
			
			if (loginpwd.getText() == null || loginpwd.getText().toString().trim().equals("") || loginpwd.getText().toString().trim() == "") {
				Util.DialogSimple("정보 미입력", "로그인 비번을 입력하세요", this);
				return;
			}
			
			DialogProgress();
			
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("loginid=" + loginid.getText());
			strbuf.append("&loginpwd=" + loginpwd.getText());
			
			Log.i("json: before", strbuf.toString());
			
			//서버 url 경로를 xml에서 가져온다.
			String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath + getText(R.string.ntpbm_0001).toString();
			
			//server connecting... login check...
			HttpConnectServer server = new HttpConnectServer();
			StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
			
			Log.i("json:", resultInfo.toString());
			
			//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
			String[] jsonName = {"EPLY_CD", "EPLY_NM", "iPSW_NUM", "HP_NUM", "E_MAIL", "SIGN_KOR_FILE_NM"};
			HashMap<String, String> parseredData = server.jsonParserList(resultInfo.toString(), jsonName);
			
			String strLoginYn = parseredData.get(jsonName[2]);
			
			Log.i("json:strLoginYn", strLoginYn);
			
			dialog.dismiss();
			
			//login 여부 판단에 따라 프로세스를 처리한다.
			if (strLoginYn.equals("1")) {
				//서버에서 파일을 다운로드한다.
				saveImage(parseredData.get("SIGN_KOR_FILE_NM").toString());
				
				MainActivity.personInfo = parseredData;
				MainActivity.loginYn = 1;
				
				Intent intent;
				String EXTRA_MESSAGE = MainActivity.EXTRA_MESSAGE;
				
				//각각의 화면으로 이동한다.
				if (message.equals(MainActivity.ntpbm_0100)) {
					//설치화면
					intent = new Intent(this, Ntpbm0100Activity.class);
				} else if (message.equals(MainActivity.ntpbm_0200)) {
					//장비조회
					intent = new Intent(this, Ntpbm0100Activity.class);
				} else if (message.equals(MainActivity.ntpbm_0300)) {
					//재고조회
					intent = new Intent(this, Ntpbm0300Activity.class);
				} else if (message.equals(MainActivity.ntpbm_0400)) {
					//견적서
					intent = new Intent(this, Ntpbm0400Activity.class);
				} else {
					intent = new Intent(this, Ntpbm0100Activity.class);
				}
				
				intent.putExtra(EXTRA_MESSAGE, message);
				startActivity(intent);
				
				finish();
			} else {
				//다이얼로그 띄우기...
				Util.DialogSimple("로그인 실패", "로그인 아이디/비번이 정확하지 않습니다. 다시 확인하십시요.", this);
			}
			break;
		case R.id.cancelBtn:
			MainActivity.loginYn = -1;
			
			this.finish();
			break;
		}
	}
	
	/*
	 * 서버에서 파일을 다운로드 하여 로컬에 저장한다.
	 */
	private void saveImage(String filename) {
		String imageUrl = MainActivity.domainUrl + MainActivity.ntpbmPath + File.separator + Util.sign_path;
		String path = Util.path + Util.sign_path + File.separator;
		Util.saveBitmapToFileCache(Util.download(filename, imageUrl), filename, path, 100);
	}
	
	ProgressDialog dialog;
	private void DialogProgress(){
        dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요 ...", true);
    }
}
