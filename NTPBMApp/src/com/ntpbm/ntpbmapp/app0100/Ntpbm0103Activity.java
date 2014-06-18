package com.ntpbm.ntpbmapp.app0100;

import java.util.ArrayList;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.ntpbm.ntpbmapp.CustomBaseAdapter;
import com.ntpbm.ntpbmapp.HttpConnectServer;
import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;
import com.ntpbm.ntpbmapp.RowItem;

public class Ntpbm0103Activity extends Activity implements OnItemClickListener, OnItemLongClickListener {
	
public static final String[] titles = new String[] { "�귣��", "���͸�", "�����е�", "����", "�޴���", "������" };
	
public String barcode;

	public static final Integer[] images = { R.drawable.img_ntpbm_0100_01,             
		R.drawable.img_ntpbm_0100_02, R.drawable.img_ntpbm_0100_03, R.drawable.img_ntpbm_0100_04,
		R.drawable.img_ntpbm_0100_05, R.drawable.img_ntpbm_0100_06,};
	public static final String[] descriptions = new String[] {"", "", "", "", "", "" };
	public static final String[] chkboxInfo = new String[] {"", "", "", "", "", "" };
	
	ListView listView;
	List<RowItem> rowItems;
	CustomBaseAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0103);
		
		Intent intent = getIntent();
		barcode = intent.getStringExtra("barcode");
		
		//server connecting... search barcode...
		searchNtpbm0103Info();
		
		rowItems = new ArrayList<RowItem>();         
		for (int i = 0; i < titles.length; i++) {             
			RowItem item = new RowItem(images[i], titles[i], descriptions[i], chkboxInfo[i]);
			rowItems.add(item);
		}
		listView = (ListView) findViewById(R.id.list1);
		
		adapter = new CustomBaseAdapter(this, rowItems, R.layout.list_view02);         
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	@Override    
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MainActivity.logView(getApplicationContext(), position+"");
		
		adapter.setChecked(position);
        // Data ����� ȣ�� Adapter�� Data ���� ����� �˷��༭ Update ��.
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
		if (arg2 == 5) {
    		//�������ϋ��� ȣ��ȴ�.
    		Intent intent = new Intent(this, Ntpbm0108Activity.class);
			startActivity(intent);
    	}
		return false;
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
	
	/** Called when the user clicks the saveAccessoryInfo button */ 
	public void saveAccessoryInfo(View view) {
		// Do something in response to button
		// save in server...
	}
	
	/** Called when the user clicks the cancelAccessoryInfo button */ 
	public void cancelAccessoryInfo(View view) {
		// Do something in response to button
		this.finish();
	}
	
	/** server connecting... search barcode... */ 
	public void searchNtpbm0103Info() {
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
		
	}

}
