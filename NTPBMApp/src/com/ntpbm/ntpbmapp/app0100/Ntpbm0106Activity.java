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
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ntpbm.ntpbmapp.HttpConnectServer;
import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;

/*
 * 설치장치의 메모
 */
public class Ntpbm0106Activity extends Activity implements OnClickListener {
	
	public String barcode;
	TableLayout tl;
	ArrayList<TableRow> trList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0106);
		
		Intent intent = getIntent();
		barcode = intent.getStringExtra("barcode");
		
		//tl = (TableLayout)findViewById(R.id.ntpbm_0106_table);
		
		//바코드(sn_cd)를 이용한 데이터조회  server connecting... search barcode...
		searchNtpbm0106Info();
		
		//findViewById(R.id.ntpbm_0106_imgbtn01).setOnClickListener(this);
		findViewById(R.id.ntpbm_0106_btn01).setOnClickListener(this);
		findViewById(R.id.ntpbm_0106_btn02).setOnClickListener(this);
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v instanceof TableRow) {
			//테이블에서 한 row 선택시.. 
			TableRow trow;
			TextView tr1;
			for (int i = 0; i < tl.getChildCount(); i++) {
				trow = (TableRow)tl.getChildAt(i);
				
				tr1 = (TextView)trow.findViewById(R.id.ntpbm_0106_tv);
				if (tr1.isSelected()) {
					tr1.setSelected(false);
					tr1.setTextColor(Color.BLACK);
				}
			}
			
			trow = (TableRow)v;
			
			tr1 = (TextView)trow.findViewById(R.id.ntpbm_0106_tv);
			tr1.setSelected(true);
			tr1.setTextColor(Color.RED);
			
			EditText edtVal = (EditText)findViewById(R.id.ntpbm_0106_edtVal);
			edtVal.setText(tr1.getText());
		} else {
			//boolean flag = false;
			//TableRow trow;
			//TextView tr1;
			//EditText edtVal = (EditText)findViewById(R.id.ntpbm_0106_edtVal);
			
			switch (v.getId()) {
			case R.id.ntpbm_0106_btn01://저장 버튼 이벤트
				
				//수정건 처리...
				/*for (int i = 0; i < tl.getChildCount(); i++) {
					trow = (TableRow)tl.getChildAt(i);
					
					tr1 = (TextView)trow.findViewById(R.id.ntpbm_0106_tv);
					if (tr1.isSelected()) {
						tr1.setText(edtVal.getText());
						flag = true;
						break;
					}
				}
				
				//신규건 처리...
				if (!flag) {
					LayoutInflater inflater = getLayoutInflater();
					TableRow tr = (TableRow)inflater.inflate(R.layout.ntpbm_0106_tablerow, tl, false);
					
					TextView tv1 = (TextView)tr.findViewById(R.id.ntpbm_0106_tv);
					tv1.setText(edtVal.getText());
					
					trList.add(tr);
					
					tl.addView(tr);
					
					trList.get(trList.size()-1).setOnClickListener(this);
				}*/
				
				saveMemo();
				
				break;
			case R.id.ntpbm_0106_btn02://취소버튼 이벤트
				/*for (int i = 0; i < tl.getChildCount(); i++) {
					trow = (TableRow)tl.getChildAt(i);
					
					tr1 = (TextView)trow.findViewById(R.id.ntpbm_0106_tv);
					if (tr1.isSelected()) {
						tr1.setSelected(false);
						tr1.setTextColor(Color.BLACK);
					}
				}
				
				edtVal.setText("");*/
				this.finish();
				break;
			/*case R.id.ntpbm_0106_imgbtn01://신규추가 버튼 (연필 버튼 이벤트)
				for (int i = 0; i < tl.getChildCount(); i++) {
					trow = (TableRow)tl.getChildAt(i);
					
					tr1 = (TextView)trow.findViewById(R.id.ntpbm_0106_tv);
					if (tr1.isSelected()) {
						tr1.setSelected(false);
						tr1.setTextColor(Color.BLACK);
					}
				}
				
				edtVal.setText("");
				break;*/
			}
		}
	}
	
	/*
	 * 입력한 메모를 저장한다.
	 */
	private void saveMemo() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		strbuf.append("&isp_note=" + ((EditText)findViewById(R.id.ntpbm_0106_edtVal)).getText());
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0106_update).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttpPair(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		DialogSimple("처리결과", "정상처리되었습니다.", this);
	}
	
	//순번, 품목코드, 브랜드명
	private String[] jsonName = {"ISP_NOTE"};
	private List<HashMap<String, String>> parseredDataList;
	
	/** 
	 * server connecting... search barcode... 
	 * 바코드(sn_cd)를 이용한 데이터조회  
	 */ 
	public void searchNtpbm0106Info() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0106).toString();
		
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
		if (parseredDataList != null) {
			HashMap<String, String> parseredData = null;
			for(int i = 0; i < parseredDataList.size(); i++) {
				parseredData = parseredDataList.get(i);
				((EditText)findViewById(R.id.ntpbm_0106_edtVal)).setText(parseredData.get(jsonName[0]));
			}
		}
		
		//init tablelayout info...
		/*if (tl.getChildCount() > 0) {
			tl.removeAllViews();
		}
		
		trList = new ArrayList<TableRow>();
		TextView tv1 = new TextView(this);
		TextView tv2 = new TextView(this);
		LayoutInflater inflater = getLayoutInflater();
		
		if (parseredDataList != null) {
			HashMap<String, String> parseredData = null;
			for(int i = 0; i < parseredDataList.size(); i++) {
				parseredData = parseredDataList.get(i);
				
				TableRow tr = (TableRow)inflater.inflate(R.layout.ntpbm_0106_tablerow, tl, false);
				trList.add(tr);
				
				tv1 = (TextView)tr.findViewById(R.id.ntpbm_0106_tv);
				tv1.setText(parseredData.get(jsonName[0]));
				
				tv2 = (TextView)tr.findViewById(R.id.ntpbm_0106_tv_seq);
				tv2.setText(parseredData.get(jsonName[1]));
				
				tl.addView(tr);
				trList.get(i).setOnClickListener(this);
			}
		}*/
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

	
	/////////////////////////////////////////////////////////////
	
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
