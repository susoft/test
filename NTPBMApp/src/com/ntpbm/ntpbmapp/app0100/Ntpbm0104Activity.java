package com.ntpbm.ntpbmapp.app0100;

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

import com.ntpbm.ntpbmapp.HttpConnectServer;
import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;

public class Ntpbm0104Activity extends Activity {
	
	public String barcode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0104);
		
		Intent intent = getIntent();
		barcode = intent.getStringExtra("barcode");
		
		//server connecting... search barcode...
		searchNtpbm0104Info();
		
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
		    ab.setSubtitle("��ġȮ��");
		}
	}
	
	// Option menu�� ������ ä��.
	// Option menu ȣ�� �� �ѹ��� �����.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.main01, menu);
		
	    return super.onPrepareOptionsMenu(menu);
	}
	
	// Option menu item�� ���õǾ����� �߻��Ѵ� event�� handler
	// �߻��� event�� �� �޼ҵ峪 super Ŭ������ ���� �޼ҵ忡�� ó�� ���� ������ false���� 
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
	
	/** Called when the user clicks the saveInstall button */ 
	public void saveInstall(View view) {
		// Do something in response to button
		// save in server...
	}
	
	/** Called when the user clicks the cancelInstall button */ 
	public void cancelInstall(View view) {
		// Do something in response to button
		this.finish();
	}
	
	/** server connecting... search barcode... */ 
	public void searchNtpbm0104Info() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("barcode=" + barcode);
		
		//���� url ��θ� xml���� �����´�.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100
				+ getText(R.string.ntpbm_0102).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//�������� ���� ��������� hashmap ���·� ��ȯ�Ѵ�.
//		String[] jsonName = {"serverip", "loginid", "loginpwd", "loginyn"};
//		HashMap<String, String> parseredData = server.jsonParserList(resultInfo.toString(), jsonName);
		
		
//		((TextView)findViewById(R.id.serverTxtNm02)).setText("���������ý�");
//		((TextView)findViewById(R.id.serverTxtNm04)).setText("���������ý���");
//		((TextView)findViewById(R.id.serverTxtNm06)).setText("���������ĺ���");
//		((TextView)findViewById(R.id.serverTxtNm08)).setText("123456789");
	}
	
}

