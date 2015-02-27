package com.ntpbm.ntpbmapp.app0100;

import java.util.ArrayList;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ntpbm.ntpbmapp.CustomBaseAdapter;
import com.ntpbm.ntpbmapp.HttpConnectServer;
import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;
import com.ntpbm.ntpbmapp.RowItem;

/*
 * ��ġ��ġ�� �������� ��ȸ
 */
public class Ntpbm0101Activity extends Activity implements OnItemClickListener {
	
	//private String message;
	private String barcode;
	
	private static final String[] titles = new String[] { "�Ǽ��縮", "��ġ���", "�������", "�޸�", "��ġȮ��", "����" };
	
	private static final Integer[] images = { 
		R.drawable.ntpbm_0100_01, R.drawable.ntpbm_0100_02, 
		R.drawable.ntpbm_0100_03, R.drawable.ntpbm_0100_04,
		R.drawable.ntpbm_0100_05, R.drawable.ntpbm_0100_06};
	
	private static String[] descriptions = new String[] {"0/6", "", "", "", "", "" };
	
	private ListView listView;
	private List<RowItem> rowItems;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0101);
		
		Intent intent = getIntent();
		//message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		barcode = intent.getStringExtra("barcode");
		
		//���ڵ�(sn_cd)�� �̿��� ��������ȸ  server connecting... search barcode...
		searchNtpbm0101Info();//������ ��ȸ�ϱ�.
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 * ȭ���̵�(��� ���ý� �̺�Ʈ �߻�)
	 */
	@Override    
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent;
		
		if (position == 1) {//��ġ���
			intent = new Intent(this, Ntpbm0104Activity.class);
		} else if (position == 2) {//�������
			intent = new Intent(this, Ntpbm0105Activity.class);
		} else if (position == 3) {//�޸�	
			intent = new Intent(this, Ntpbm0106Activity.class);
		} else if (position == 4) {//��ġȮ��
			intent = new Intent(this, Ntpbm0107Activity.class);
			intent.putExtra(MainActivity.EXTRA_MESSAGE, MainActivity.ntpbm_0107);
		} else if (position == 5) {//����
			intent = new Intent(this, Ntpbm0107Activity.class);
			intent.putExtra(MainActivity.EXTRA_MESSAGE, MainActivity.ntpbm_01071);
		} else {//�Ǽ��縮
			intent = new Intent(this, Ntpbm0103Activity.class);
		}
		intent.putExtra("barcode", barcode);
		startActivity(intent);
	}
	
	/** 
	 * Called when the user clicks the srchBrandInfo button
	 * ��ǰ �� ȭ������ �̵��Ѵ�.(��� ��ǰ �̹��� ���ý� �̺�Ʈ �߻�)
	 */ 
	public void srchBrandInfo(View view) {
		// Do something in response to button
		Intent intent = new Intent(this, Ntpbm0102Activity.class);
		intent.putExtra("barcode", barcode);
		startActivity(intent);
	}
	
	//                            ����, ǰ���ڵ�, �귣���, �𵨸�, �ĺ���, �Ҹ�ǰS/N��ȣ, ��ȿ����, Ȯ�ο���
	private String[] jsonName = {"SEQ", "IT_CD", "IT_NM", "MD_NM", "GD_OT", "SN_S_CD", "EXP_DATE", "CHK_TF"};
	private List<HashMap<String, String>> parseredDataList;
	
	/** 
	 * ���ڵ�(sn_cd)�� �̿��� ��������ȸ
	 */ 
	private void searchNtpbm0101Info() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//���� url ��θ� xml���� �����´�.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0101).toString();
		
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
			
			int checkCount = 0;
			int listCount = parseredDataList.size();
			for(int i = 0; i < listCount; i++) {
				parseredData = parseredDataList.get(i);
				
				//�𵨸� �⺻���� ��ȸ�ϱ�.
				if ( i == 0) {
					((TextView)findViewById(R.id.serverTxtNm02)).setText(parseredData.get(jsonName[2]));//�귣��
					((TextView)findViewById(R.id.serverTxtNm04)).setText(parseredData.get(jsonName[3]));//�𵨸�
					((TextView)findViewById(R.id.serverTxtNm06)).setText(parseredData.get(jsonName[4]));//�ĺ���
					((TextView)findViewById(R.id.serverTxtNm08)).setText(parseredData.get(jsonName[5]));//�ø����ȣ
				}
				//üũȮ�� �Ǽ� �˾ƾ���.
				if (parseredData.get(jsonName[7]).equals("1")) checkCount++;
			}
			
			descriptions[0] = checkCount + "/" + (listCount);
		}
		
		setDisplay();//����Ʈ�並 �����Ѵ�.
	}

	/*
	 * ����Ʈ�並 �����Ѵ�.
	 */
	private void setDisplay() {
		rowItems = new ArrayList<RowItem>();
		for (int i = 0; i < titles.length; i++) {             
			RowItem item = new RowItem(images[i], titles[i], descriptions[i], "");             
			rowItems.add(item);         
		}
		listView = (ListView) findViewById(R.id.list1);         
		CustomBaseAdapter adapter = new CustomBaseAdapter(this, rowItems, R.layout.list_view01);         
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
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
