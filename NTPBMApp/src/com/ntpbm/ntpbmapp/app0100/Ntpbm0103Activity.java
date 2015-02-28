package com.ntpbm.ntpbmapp.app0100;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

/*
 * 설치장치의 악세사리정보 조회
 */
public class Ntpbm0103Activity extends Activity implements OnItemClickListener, OnItemLongClickListener {
	
	private static String[] titles = new String[] { "브랜드", "배터리", "전극패드", "수건", "메뉴얼", "보관함" };
	
	private static final Integer[] images = { R.drawable.img_ntpbm_0100_01,             
		R.drawable.img_ntpbm_0100_02, R.drawable.img_ntpbm_0100_03, R.drawable.img_ntpbm_0100_04,
		R.drawable.img_ntpbm_0100_05, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06
		, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06};
	
	private static String[] descriptions = new String[] {"", "", "", "", "", "" };
	private static String[] chkboxInfo = new String[] {"", "", "", "", "", "" };
	
	private String barcode;
	
	private ListView listView;
	private List<RowItem> rowItems;
	private CustomBaseAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0103);
		
		Intent intent = getIntent();
		barcode = intent.getStringExtra("barcode");
		
		//바코드(sn_cd)를 이용한 데이터조회  server connecting... search barcode...
		searchNtpbm0103Info();
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 * 목록에서 선택시 이벤트 발생 - 체크박스 제어
	 */
	@Override    
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//MainActivity.logView(getApplicationContext(), position+"");
		adapter.setChecked(position);
        // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
		adapter.notifyDataSetChanged();
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, android.view.View, int, long)
	 * 목록에서 길게선택시 이벤트 발생 - 보관함 화면으로 이동하는 경우만 해당.
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg2 == parseredDataList.size()-1) {
    		//보관함일떄만 호출된다.
    		Intent intent = new Intent(this, Ntpbm0108Activity.class);
			startActivity(intent);
    	}
		return false;
	}
	
	/** 
	 * Called when the user clicks the saveAccessoryInfo button
	 * 악세사리 변경된 정보 저장
	 */ 
	public void saveAccessoryInfo(View view) {
		
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//변경정보 생성하기.
		boolean[] isChecked = adapter.getIsCheckedConfrim();
		for (int i = 0; i < isChecked.length; i++) {
			//원본정보와 현재정보를 확인 하여 변경일 경우 업데이트 대상건으로 처리한다.
			strbuf.append("&seq=" + parseredDataList.get(i).get("SEQ"));
			strbuf.append("&chk_tf=" + (isChecked[i] ? "1" : "0"));
		}
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0103_update).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		DialogSimple("처리결과", "정상처리되었습니다.", this);
	}
	
	/** 
	 * Called when the user clicks the cancelAccessoryInfo button
	 * 악세사리변경 취소 처리(이전화면으로 이동한다.)
	 */ 
	public void cancelAccessoryInfo(View view) {
		// Do something in response to button
		this.finish();
	}
	
	//                            순번, 품목코드, 브랜드명, 모델명, 식별자, 소모품S/N번호, 유효일자, 확인여부
	private String[] jsonName = {"SEQ", "IT_CD", "IT_NM", "MD_NM", "GD_OT", "SN_S_CD", "EXP_DATE", "CHK_TF"};
	private List<HashMap<String, String>> parseredDataList;
	
	/** 
	 * 바코드(sn_cd)를 이용한 데이터조회  (악세사리만 조회한다.)
	 * server connecting... search barcode... 
	 */ 
	public void searchNtpbm0103Info() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0103).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
		parseredDataList = server.jsonParserArrayList(resultInfo.toString(), jsonName);
		
		//서버에서 조회한 정보를 화면에 보여준다.
		setData();
		
		//목록을 refresh 한다.
		setDisplay();
	}

	/*
	 * 서버에서 조회한 정보를 화면에 보여준다.
	 */
	private void setData() {
		if (parseredDataList != null) {
			titles = new String[parseredDataList.size()];//악세사리 리스트
			descriptions = new String[parseredDataList.size()];//temp
			chkboxInfo = new String[parseredDataList.size()];//체크여부
			
			HashMap<String, String> parseredData = null;
			for(int i = 0; i < parseredDataList.size(); i++) {
				parseredData = parseredDataList.get(i);
				
				titles[i] = parseredData.get(jsonName[2]);//악세사리명.
				chkboxInfo[i] = parseredData.get(jsonName[7]);//체크됨.
				descriptions[i] = "";
				
				parseredData.put("CHK_YN", chkboxInfo[i]);
			}
		}
	}
	
	/*
	 * 목록을 refresh 한다.
	 */
	private void setDisplay() {
		rowItems = new ArrayList<RowItem>();         
		for (int i = 0; i < titles.length; i++) {             
			RowItem item = new RowItem(images[0], titles[i], descriptions[i], chkboxInfo[i]);
			rowItems.add(item);
		}
		listView = (ListView) findViewById(R.id.list1);
		
		adapter = new CustomBaseAdapter(this, rowItems, R.layout.list_view02);         
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}
	

	/*
	 * 팝업창 (결과처리)
	 */
	public void DialogSimple(String title, String message, Context context) {
	    AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
	    alt_bld.setPositiveButton("확인", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	dialog.dismiss();     //닫기
	        	close();
	        }
	    });
	    
	    AlertDialog alert = alt_bld.create();
	    alert.setTitle(title);
	    alert.setMessage(message);
	    alert.setCanceledOnTouchOutside(true);
	    alert.show();
	}
	
	public void close() {
		this.finish();
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
}
