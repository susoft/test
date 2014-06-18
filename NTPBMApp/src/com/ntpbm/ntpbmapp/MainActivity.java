package com.ntpbm.ntpbmapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ntpbm.ntpbmapp.app0100.Ntpbm0100Activity;
import com.ntpbm.ntpbmapp.app0300.Ntpbm0300Activity;
import com.ntpbm.ntpbmapp.app0400.Ntpbm0400Activity;

public class MainActivity extends Activity implements OnItemClickListener {
	
	public final static String EXTRA_MESSAGE = "com.ntpbm.ntpbmapp.GUBNUN";
	public final static String ntpbm_0100 = "0100";//��ġȮ��
	public final static String ntpbm_0200 = "0200";//�����ȸ
	public final static String ntpbm_0300 = "0300";//�����ȸ
	public final static String ntpbm_0400 = "0400";//������
	
	public final static String ntpbm_0107 = "0107";//��ġȮ�� > ��ġȮ��
	public final static String ntpbm_01071 = "01071";//��ġȮ�� > ����

	public static MainActivity mainActivity;
	
	public static String domainUrl = "";
	public static String ntpbmPath = "";
	public static String ntpbmPath0100 = "";
	public static String ntpbmPath0300 = "";
	public static String ntpbmPath0400 = "";
	
	public static int loginYn = -1;
	
	public static final String[] titles = new String[] { "��ġȮ��", "�����ȸ", "�����ȸ", "������" };       
	public static final String[] descriptions = new String[] {"", "", "", "" };
	public static final Integer[] images = { R.drawable.ntpbm_0100, R.drawable.ntpbm_0200, R.drawable.ntpbm_0300, R.drawable.ntpbm_0400 };
	
	ListView listView;
	List<RowItem> rowItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0000);
		
		domainUrl = getText(R.string.domainUrl1).toString();
		ntpbmPath = getText(R.string.ntpbmPath).toString();
		ntpbmPath0100 = getText(R.string.ntpbmPath0100).toString();
		ntpbmPath0300 = getText(R.string.ntpbmPath0300).toString();
		ntpbmPath0400 = getText(R.string.ntpbmPath0400).toString();
		
		rowItems = new ArrayList<RowItem>();         
		for (int i = 0; i < titles.length; i++) {             
			RowItem item = new RowItem(images[i], titles[i], descriptions[i], "");             
			rowItems.add(item);         
		}
		
		listView = (ListView) findViewById(R.id.list);         
		CustomBaseAdapter adapter = new CustomBaseAdapter(this, rowItems, R.layout.list_view01);         
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	@Override    
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		Intent intent;
		String ntpbm_gubun;
		
		//������ ȭ������ �̵��Ѵ�.
		if (position == 0) {
			//��ġȭ��
			ntpbm_gubun = ntpbm_0100;
		} else if (position == 1) {
			//�����ȸ
			ntpbm_gubun = ntpbm_0200;
		} else if (position == 2) {
			//�����ȸ
			ntpbm_gubun = ntpbm_0300;
		} else if (position == 3) {
			//������
			ntpbm_gubun = ntpbm_0400;
		} else {
			ntpbm_gubun = ntpbm_0100;
		}
		
		//�α��� Ȯ�� ó��
		if (loginYn < 0) {
			//�α��� ȭ������ �̵��Ѵ�.
			intent = new Intent(this, Ntpbm0001Activity.class);
		} else {
			//������ ȭ������ �̵��Ѵ�.
			if (position == 0) {
				//��ġȭ��
				intent = new Intent(this, Ntpbm0100Activity.class);
			} else if (position == 1) {
				//�����ȸ
				intent = new Intent(this, Ntpbm0100Activity.class);
			} else if (position == 2) {
				//�����ȸ
				intent = new Intent(this, Ntpbm0300Activity.class);
			} else if (position == 3) {
				//������
				intent = new Intent(this, Ntpbm0400Activity.class);
			} else {
				intent = new Intent(this, Ntpbm0400Activity.class);
			}
		}
		intent.putExtra(EXTRA_MESSAGE, ntpbm_gubun);
		startActivity(intent);
	}
	
	// Option menu�� ������ ä��.
	// Option menu ȣ�� �� �ѹ��� �����.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
	    return true;
	}
	
	// Option menu�� ȭ�鿡 ǥ�õǱ� �� �׻� ȣ���
	// �ѹ� �ʱ�ȭ�� Option Menu�� ������ �������� �ٲٰų� �Ҷ� ���.
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean result = super.onPrepareOptionsMenu(menu);
		
		int login;
		
		if (loginYn < 0) {
			//�α��θ� �����ش�.
			login = R.menu.main;
		} else if (loginYn > 0) {
			//�α׾ƿ�, �������� �� �����ش�.
			login = R.menu.main01;
		} else {
			login = R.menu.main;
		}
		
		menu.clear();//�ʱ�ȭ �Ѵ�.
		
		getMenuInflater().inflate(login, menu);
		
		return result;
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
			loginYn = -1;
			break;
		case R.id.action_personinfo:
			Intent intent2 = new Intent(this, Ntpbm0002Activity.class);
			startActivity(intent2);
			break;
		}
		
		return true;
	}
	
	/** log view */ 
	public static void logView(Context context, String strValue) {
		Toast toast = Toast.makeText(context, strValue, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
	
	public static HashMap<String, String> personInfo;
	
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(false);
		}
	}
	
}
