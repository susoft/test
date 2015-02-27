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
 * ���ڵ�  or �����Է� ó��ȭ��
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
		case R.id.ntpbm_0100_btn01://���ڵ� �ν� ��ư.
			mPage2.setVisibility(View.INVISIBLE);
			mPage1.setVisibility(View.VISIBLE);
			
			//instantiate ZXing integration class
			IntentIntegrator scanIntegrator = new IntentIntegrator(Ntpbm0100Activity.this);
			//start scanning
			scanIntegrator.initiateScan();
			break;
		case R.id.ntpbm_0100_btn02://�����Է� ��ư.
			mPage1.setVisibility(View.INVISIBLE);
			mPage2.setVisibility(View.VISIBLE);
			break;
		case R.id.ntpbm_0100_btn03://�Է� �Ǵ� �νĵ� ���ڵ带 �̿��� ��ȸ
			EditText editText = (EditText) findViewById(R.id.barcodeVal);
			String barcodeVal = editText.getText().toString();
			
			searchBarCode(barcodeVal);
			break;
		case R.id.ntpbm_0100_btn04://���.
			MainActivity.loginYn = 1;
			
			this.finish();
			break;
		}
	}
	
	//���ڵ��ν� ���α׷����� ������ ����...
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
	 * ���ڵ带 �̿��� ��ȸ...
	 */ 
	public void searchBarCode(String barcodeVal) {
		// Do something in response to button

		if (barcodeVal == null || "".equals(barcodeVal)) {
			Util.DialogSimple("��ȸ���� ���Է�", "�ڵ������� �Է��ϼ���.", this);
		} else {
			Intent intent;
			if (message.equals(MainActivity.ntpbm_0200)) {//�����ȸ
				intent = new Intent(this, Ntpbm0107Activity.class);
			} else {//��ġ��ȸ
				intent = new Intent(this, Ntpbm0101Activity.class);
			}
			
			/*if (!barcodeVal.contains("SN")) {
				barcodeVal = "SN" + barcodeVal;
			}*/
			searchNtpbm0100Info(barcodeVal.toUpperCase(), intent);//������ ��ȸ�ϱ�.
		}
	}
	
	/** 
	 * ���ڵ�(sn_cd)�� �̿��� ��������ȸ
	 */ 
	private void searchNtpbm0100Info(String barcodeVal, Intent intent) {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcodeVal);
		
		//���� url ��θ� xml���� �����´�.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0100).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//�������� ���� ��������� hashmap ���·� ��ȯ�Ѵ�.
		String[] jsonName = {"SEQ", "IT_CD", "IT_NM", "MD_NM", "GD_OT", "SN_S_CD", "EXP_DATE", "CHK_TF"};
		List<HashMap<String, String>> parseredDataList = server.jsonParserArrayList(resultInfo.toString(), jsonName);
		
		if (parseredDataList != null && parseredDataList.size() > 0) {
			intent.putExtra(MainActivity.EXTRA_MESSAGE, message);
			intent.putExtra("barcode", barcodeVal);
			startActivity(intent);
		} else {
			//���̾�α� ����...
			Util.DialogSimple("������ ����", barcodeVal +  "�� �ش�� �����Ͱ� �������� �ʽ��ϴ�.", this);
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
	
}

