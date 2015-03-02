package com.ntpbm.ntpbmapp.app0300;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.ntpbm.ntpbmapp.HttpConnectServer;
import com.ntpbm.ntpbmapp.ListItem0300Adapter;
import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;
import com.ntpbm.ntpbmapp.SpinnerAdapter;

public class Ntpbm0300Activity extends Activity implements OnClickListener {
	
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0300);
		
		srchSpinFirst();//콤보박스 생성....
		
		findViewById(R.id.ntpbm_0300_btn01).setOnClickListener(this);
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ntpbm_0300_btn01://저장 버튼 이벤트
			
			srchItem();
			
			break;
		}
	}
	
	public int positionSecond;//두번째 콤보박스 선택 인덱스..
	//품목코드, 품목명, 규격, 재고량
	private String[] jsonName = {"IT_CD", "IT_NM", "STD", "STK_SU"};
	private List<HashMap<String, String>> parseredDataList;
	
	/** 
	 * Called when the user clicks the srchItem button 
	 * 조회 버튼 이벤트.
	 */ 
	public void srchItem() {
		// Do something in response to button
		String it_nm = ((EditText)findViewById(R.id.nameEdtVal)).getText().toString();
		
		if (parseredDataList_dvs != null && parseredDataList_dvs.size() < 1) {
			return;
		}
		if (positionSecond < 0) {
			positionSecond = 0;
		}
		
		//조회한다.
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("itd_cd=" + parseredDataList_dvs.get(positionSecond).get("ITD_CD"));
		strbuf.append("&it_nm=" + it_nm);
		
		Log.i("json:before", strbuf.toString());
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0300 + getText(R.string.ntpbm_0300).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
		parseredDataList = server.jsonParserArrayList(resultInfo.toString(), jsonName);
		
		setData();
		
	}

	/*
	 * 서버에서 조회한 정보를 화면에 보여준다.
	 */
	private void setData() {
		if (parseredDataList == null) {
			parseredDataList = new ArrayList<HashMap<String, String>>();
		}
		
		/*MainActivity.logView(getApplicationContext(), "setData.....");
		
		HashMap<String, String> temp = new HashMap<String, String>();
		temp.put("IT_CD", "11111");
		temp.put("IT_NM", "testsetsetse");
		temp.put("STK_SU", "11");
		parseredDataList.add(temp);
		
		MainActivity.logView(getApplicationContext(), "setData....." + parseredDataList.size());*/
		
		listView = (ListView) findViewById(R.id.list0300);         
		ListItem0300Adapter adapter = new ListItem0300Adapter(this, parseredDataList, R.layout.list_view0300);         
		listView.setAdapter(adapter);
	}
	
	
	/////////////////////////////////////////////////////
	//콤보박스 관리..
	List<HashMap<String, String>> parseredDataList_cls;
	List<HashMap<String, String>> parseredDataList_dvs;
	
	/** 
	 * Called when this screen enter 
	 * 콤보박스 생성 첫번쨰.
	 */ 
	public void srchSpinFirst() {
		// Do something in response to button
		//Server connecting..
		//조회한다.
		StringBuffer strbuf = new StringBuffer();
		
		Log.i("json:before", strbuf.toString());
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0300 + getText(R.string.ntpbm0300_cls).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:before", resultInfo.toString());
		
		String[] jsonName_cls = {"ITC_CD", "ITC_NM"};
		parseredDataList_cls = server.jsonParserArrayList(resultInfo.toString(), jsonName_cls);
		
		String[] a13s = {"선택"};
		if (parseredDataList_cls != null) {
			HashMap<String, String> parseredData = null;
			
			a13s = new String[parseredDataList_cls.size()];
			
			Log.i("json:size", parseredDataList_cls.size()+"");
			
			for(int i = 0; i < parseredDataList_cls.size(); i++) {
				parseredData = parseredDataList_cls.get(i);
				
				a13s[i] = parseredData.get("ITC_NM");
			}
		}
		
		/** Android-13 , Custom Adapter 참조 , 임의의 String[] 배열 사용*/
		SpinnerAdapter a13Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a13s);
		
		Spinner spin1 = (Spinner)findViewById(R.id.ntpbm_0300_spnr01);
		spin1.setPrompt("품목그룹을 선택하세요.");
		spin1.setAdapter(a13Adapter);
		
		spin1.setOnItemSelectedListener(mOnItemSelectedListener_First);
	}
	
	/** 
	 * Called when the user clicks the first spinner item  
	 * 콤보박스 생성 두번쨰.
	 */ 
	public void srchSpinSecond(int position) {
		// Do something in response to button
		//Server connecting...
		if (position < 0) {
			if (parseredDataList_cls != null && parseredDataList_cls.size() < 1) {
				return;
			}
			position = 0;
		}
		
		//조회한다.
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("itc_cd=" + parseredDataList_cls.get(position).get("ITC_CD"));
		
		Log.i("json:before", strbuf.toString());
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0300 + getText(R.string.ntpbm0300_dvs).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		String[] jsonName_dvs = {"ITD_CD", "ITC_CD", "ITD_NUM", "ITD_NM"};
		parseredDataList_dvs = server.jsonParserArrayList(resultInfo.toString(), jsonName_dvs);
		
		String[] a13s = {"선택"};
		if (parseredDataList_dvs != null) {
			HashMap<String, String> parseredData = null;
			
			a13s = new String[parseredDataList_dvs.size()];
			for(int i = 0; i < parseredDataList_dvs.size(); i++) {
				parseredData = parseredDataList_dvs.get(i);
				
				a13s[i] = parseredData.get("ITD_NM");
			}
		}
		
		/** Android-13 , Custom Adapter 참조 , 임의의 String[] 배열 사용*/
		SpinnerAdapter a13Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a13s);
		
		Spinner spin1 = (Spinner)findViewById(R.id.ntpbm_0300_spnr02);
		spin1.setPrompt("품목구분을 선택하세요.");
		spin1.setAdapter(a13Adapter);
		
		spin1.setOnItemSelectedListener(mOnItemSelectedListener_Second);
	}
	
	//첫번째 콤보박스 선택 이벤트
	public OnItemSelectedListener mOnItemSelectedListener_First = new OnItemSelectedListener() {
		@Override    
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			srchSpinSecond(position);
		}
		@Override    
		public void onNothingSelected(AdapterView<?> parent) {
			//Nothing....
		}
	};

	//두번째 콤보박스 선택 이벤트
	public OnItemSelectedListener mOnItemSelectedListener_Second = new OnItemSelectedListener() {
		@Override    
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			positionSecond = position;
		}
		@Override    
		public void onNothingSelected(AdapterView<?> parent) {
			//Nothing....
		}
	};
	
	
	//////////////////////////////////////////////////////
	

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(false);
		    ab.setTitle(R.string.app_name);
		    ab.setSubtitle("재고조회");
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
	
}

