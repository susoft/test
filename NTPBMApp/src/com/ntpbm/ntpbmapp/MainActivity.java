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
	public final static String ntpbm_0100 = "0100";//설치확인
	public final static String ntpbm_0200 = "0200";//장비조회
	public final static String ntpbm_0300 = "0300";//재고조회
	public final static String ntpbm_0400 = "0400";//견적서
	
	public final static String ntpbm_0107 = "0107";//설치확인 > 설치확인
	public final static String ntpbm_01071 = "01071";//설치확인 > 전송

	public static MainActivity mainActivity;
	
	public static String domainUrl = "";
	public static String ntpbmPath = "";
	public static String ntpbmPath0100 = "";
	public static String ntpbmPath0300 = "";
	public static String ntpbmPath0400 = "";
	
	public static int loginYn = -1;
	
	public static final String[] titles = new String[] { "설치확인", "장비조회", "재고조회", "견적서" };       
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
		
		//각각의 화면으로 이동한다.
		if (position == 0) {
			//설치화면
			ntpbm_gubun = ntpbm_0100;
		} else if (position == 1) {
			//장비조회
			ntpbm_gubun = ntpbm_0200;
		} else if (position == 2) {
			//재고조회
			ntpbm_gubun = ntpbm_0300;
		} else if (position == 3) {
			//견적서
			ntpbm_gubun = ntpbm_0400;
		} else {
			ntpbm_gubun = ntpbm_0100;
		}
		
		//로그인 확인 처리
		if (loginYn < 0) {
			//로그인 화면으로 이동한다.
			intent = new Intent(this, Ntpbm0001Activity.class);
		} else {
			//각각의 화면으로 이동한다.
			if (position == 0) {
				//설치화면
				intent = new Intent(this, Ntpbm0100Activity.class);
			} else if (position == 1) {
				//장비조회
				intent = new Intent(this, Ntpbm0100Activity.class);
			} else if (position == 2) {
				//재고조회
				intent = new Intent(this, Ntpbm0300Activity.class);
			} else if (position == 3) {
				//견적서
				intent = new Intent(this, Ntpbm0400Activity.class);
			} else {
				intent = new Intent(this, Ntpbm0400Activity.class);
			}
		}
		intent.putExtra(EXTRA_MESSAGE, ntpbm_gubun);
		startActivity(intent);
	}
	
	// Option menu에 아이템 채움.
	// Option menu 호출 시 한번말 실행됨.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
	    return true;
	}
	
	// Option menu가 화면에 표시되기 전 항상 호출됨
	// 한번 초기화된 Option Menu의 내용을 동적으로 바꾸거나 할때 사용.
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean result = super.onPrepareOptionsMenu(menu);
		
		int login;
		
		if (loginYn < 0) {
			//로그인만 보여준다.
			login = R.menu.main;
		} else if (loginYn > 0) {
			//로그아웃, 개인정보 를 보여준다.
			login = R.menu.main01;
		} else {
			login = R.menu.main;
		}
		
		menu.clear();//초기화 한다.
		
		getMenuInflater().inflate(login, menu);
		
		return result;
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
