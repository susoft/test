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
 * ��ġ��ġ�� ������ ��ȸ�ϱ�..
 */
public class Ntpbm0102Activity extends Activity {
	
	private String barcode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0102);
		
		Intent intent = getIntent();
		barcode = intent.getStringExtra("barcode");
		
		//���ڵ�(sn_cd)�� �̿��� ��������ȸ  server connecting... search barcode...
		searchNtpbm0102Info();
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	//                            ����, ǰ���ڵ�, �귣���, �𵨸�, �ĺ���, �Ҹ�ǰS/N��ȣ, ��ȿ����, Ȯ�ο���
	private String[] jsonName = {"SEQ", "IT_CD", "IT_NM", "MD_NM", "GD_OT", "SN_S_CD", "EXP_DATE", "CHK_TF"};
	private List<HashMap<String, String>> parseredDataList;
	
	/** 
	 * ���ڵ�(sn_cd)�� �̿��� ��������ȸ  server connecting... search barcode... 
	 */ 
	private void searchNtpbm0102Info() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//���� url ��θ� xml���� �����´�.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0102).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//�������� ���� ��������� hashmap ���·� ��ȯ�Ѵ�.
		parseredDataList = server.jsonParserArrayList(resultInfo.toString(), jsonName);
		
		setData();//�������� ��ȸ�� ������ ȭ�鿡 �����ش�.
	}

	/*
	 * �������� ��ȸ�� ������ ȭ�鿡 �����ش�.
	 */
	private void setData() {
		if (parseredDataList != null) {
			HashMap<String, String> parseredData = null;
			for(int i = 0; i < parseredDataList.size(); i++) {
				parseredData = parseredDataList.get(i);

				//�𵨸� �⺻���� ��ȸ�ϱ�.
				if ( i == 0) {
					((TextView)findViewById(R.id.serverTxtNm02)).setText(parseredData.get(jsonName[2]));//�귣��
					((TextView)findViewById(R.id.serverTxtNm04)).setText(parseredData.get(jsonName[3]));//�𵨸�
					((TextView)findViewById(R.id.serverTxtNm06)).setText(parseredData.get(jsonName[4]));//�ĺ���
					((TextView)findViewById(R.id.serverTxtNm08)).setText(parseredData.get(jsonName[5]));///�ø����ȣ
				}
			}
		}
		
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm02)).setText("");//������ȣ
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm04)).setText("");//��������
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm06)).setText("");//����ȸ��
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm08)).setText("");//������
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm10)).setText("");//����ó��
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm12)).setText("");//����ó�ּ�
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm14)).setText("");//����ó�����
		((TextView)findViewById(R.id.ntpbm_0102_txt_nm16)).setText("");//����ó����ó
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
