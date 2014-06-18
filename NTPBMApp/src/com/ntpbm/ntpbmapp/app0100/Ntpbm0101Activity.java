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
import android.widget.ListView;
import android.widget.TextView;

import com.ntpbm.ntpbmapp.CustomBaseAdapter;
import com.ntpbm.ntpbmapp.HttpConnectServer;
import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;
import com.ntpbm.ntpbmapp.RowItem;

public class Ntpbm0101Activity extends Activity implements OnItemClickListener {
	
	public String message;
	public String barcode;
	
	public static final String[] titles = new String[] { "악세사리", "설치장소", "현장사진", "메모", "설치확인", "전송" };
	
	public static final Integer[] images = { 
		R.drawable.ntpbm_0100_01, R.drawable.ntpbm_0100_02, 
		R.drawable.ntpbm_0100_03, R.drawable.ntpbm_0100_04,
		R.drawable.ntpbm_0100_05, R.drawable.ntpbm_0100_06};
	
	public static final String[] descriptions = new String[] {"0/6", "", "", "", "", "" };
	
	ListView listView;
	List<RowItem> rowItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0101);
		
		Intent intent = getIntent();
		message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		barcode = intent.getStringExtra("barcode");
		
		//server connecting... search barcode...
		searchNtpbm0101Info();
		
		rowItems = new ArrayList<RowItem>();
		for (int i = 0; i < titles.length; i++) {             
			RowItem item = new RowItem(images[i], titles[i], descriptions[i], "");             
			rowItems.add(item);         
		}
		listView = (ListView) findViewById(R.id.list1);         
		CustomBaseAdapter adapter = new CustomBaseAdapter(this, rowItems, R.layout.list_view01);         
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	@Override    
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent;
		if (position == 0) {
			//악세사리
			intent = new Intent(this, Ntpbm0103Activity.class);
		} else if (position == 1) {
			//설치장소
			intent = new Intent(this, Ntpbm0104Activity.class);
		} else if (position == 2) {
			//현장사진
			intent = new Intent(this, Ntpbm0105Activity.class);
		} else if (position == 3) {
			//메모
			intent = new Intent(this, Ntpbm0106Activity.class);
		} else if (position == 4) {
			//설치확인
			intent = new Intent(this, Ntpbm0107Activity.class);
			intent.putExtra(MainActivity.EXTRA_MESSAGE, MainActivity.ntpbm_0107);
		} else if (position == 5) {
			//전송
			intent = new Intent(this, Ntpbm0107Activity.class);
			intent.putExtra(MainActivity.EXTRA_MESSAGE, MainActivity.ntpbm_01071);
		} else {
			intent = new Intent(this, Ntpbm0103Activity.class);
		}
		intent.putExtra("barcode", barcode);
		startActivity(intent);
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
	
	/** Called when the user clicks the srchBrandInfo button */ 
	public void srchBrandInfo(View view) {
		// Do something in response to button
		Intent intent = new Intent(this, Ntpbm0102Activity.class);
		startActivity(intent);
	}
	
	/** server connecting... search barcode... */ 
	public void searchNtpbm0101Info() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("barcode=" + barcode);
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100
				+ getText(R.string.ntpbm_0101).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
//		String[] jsonName = {"serverip", "loginid", "loginpwd", "loginyn"};
//		HashMap<String, String> parseredData = server.jsonParserList(resultInfo.toString(), jsonName);
		
		((TextView)findViewById(R.id.serverTxtNm02)).setText("진산인포시스");
		((TextView)findViewById(R.id.serverTxtNm04)).setText("진산인포시스모델");
		((TextView)findViewById(R.id.serverTxtNm06)).setText("진산인포식별자");
		((TextView)findViewById(R.id.serverTxtNm08)).setText("123456789");
	}
	
}
