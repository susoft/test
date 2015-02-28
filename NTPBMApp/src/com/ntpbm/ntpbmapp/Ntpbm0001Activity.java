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
			
			//ȭ�鿡�� ������ �Ѱ��� �����͸� �����´�.
			EditText loginid = (EditText)findViewById(R.id.idVal);
			EditText loginpwd = (EditText)findViewById(R.id.pwdVal);
			
			if (loginid.getText() == null || loginid.getText().toString().trim().equals("") || loginid.getText().toString().trim() == "") {
				Util.DialogSimple("���� ���Է�", "�α��� ���̵� �Է��ϼ���", this);
				return;
			}
			
			if (loginpwd.getText() == null || loginpwd.getText().toString().trim().equals("") || loginpwd.getText().toString().trim() == "") {
				Util.DialogSimple("���� ���Է�", "�α��� ����� �Է��ϼ���", this);
				return;
			}
			
			DialogProgress();
			
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("loginid=" + loginid.getText());
			strbuf.append("&loginpwd=" + loginpwd.getText());
			
			Log.i("json: before", strbuf.toString());
			
			//���� url ��θ� xml���� �����´�.
			String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath + getText(R.string.ntpbm_0001).toString();
			
			//server connecting... login check...
			HttpConnectServer server = new HttpConnectServer();
			StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
			
			Log.i("json:", resultInfo.toString());
			
			//�������� ���� ��������� hashmap ���·� ��ȯ�Ѵ�.
			String[] jsonName = {"EPLY_CD", "EPLY_NM", "iPSW_NUM", "HP_NUM", "E_MAIL", "SIGN_KOR_FILE_NM"};
			HashMap<String, String> parseredData = server.jsonParserList(resultInfo.toString(), jsonName);
			
			String strLoginYn = parseredData.get(jsonName[2]);
			
			Log.i("json:strLoginYn", strLoginYn);
			
			dialog.dismiss();
			
			//login ���� �Ǵܿ� ���� ���μ����� ó���Ѵ�.
			if (strLoginYn.equals("1")) {
				//�������� ������ �ٿ�ε��Ѵ�.
				saveImage(parseredData.get("SIGN_KOR_FILE_NM").toString());
				
				MainActivity.personInfo = parseredData;
				MainActivity.loginYn = 1;
				
				Intent intent;
				String EXTRA_MESSAGE = MainActivity.EXTRA_MESSAGE;
				
				//������ ȭ������ �̵��Ѵ�.
				if (message.equals(MainActivity.ntpbm_0100)) {
					//��ġȭ��
					intent = new Intent(this, Ntpbm0100Activity.class);
				} else if (message.equals(MainActivity.ntpbm_0200)) {
					//�����ȸ
					intent = new Intent(this, Ntpbm0100Activity.class);
				} else if (message.equals(MainActivity.ntpbm_0300)) {
					//�����ȸ
					intent = new Intent(this, Ntpbm0300Activity.class);
				} else if (message.equals(MainActivity.ntpbm_0400)) {
					//������
					intent = new Intent(this, Ntpbm0400Activity.class);
				} else {
					intent = new Intent(this, Ntpbm0100Activity.class);
				}
				
				intent.putExtra(EXTRA_MESSAGE, message);
				startActivity(intent);
				
				finish();
			} else {
				//���̾�α� ����...
				Util.DialogSimple("�α��� ����", "�α��� ���̵�/����� ��Ȯ���� �ʽ��ϴ�. �ٽ� Ȯ���Ͻʽÿ�.", this);
			}
			break;
		case R.id.cancelBtn:
			MainActivity.loginYn = -1;
			
			this.finish();
			break;
		}
	}
	
	/*
	 * �������� ������ �ٿ�ε� �Ͽ� ���ÿ� �����Ѵ�.
	 */
	private void saveImage(String filename) {
		String imageUrl = MainActivity.domainUrl + MainActivity.ntpbmPath + File.separator + Util.sign_path;
		String path = Util.path + Util.sign_path + File.separator;
		Util.saveBitmapToFileCache(Util.download(filename, imageUrl), filename, path, 100);
	}
	
	ProgressDialog dialog;
	private void DialogProgress(){
        dialog = ProgressDialog.show(this, "", "��ø� ��ٷ� �ּ��� ...", true);
    }
}
