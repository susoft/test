package com.ntpbm.ntpbmapp.app0100;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
		
		tl = (TableLayout)findViewById(R.id.ntpbm_0106_table);
		
		//server connecting... search barcode...
		searchNtpbm0106Info();
		
		findViewById(R.id.ntpbm_0106_imgbtn01).setOnClickListener(this);
		findViewById(R.id.ntpbm_0106_btn01).setOnClickListener(this);
		findViewById(R.id.ntpbm_0106_btn02).setOnClickListener(this);
		
		// Show the Up button in the action bar.
		setupActionBar();
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
	
	@Override
	public void onClick(View v) {
//		MainActivity.logView(getApplicationContext(), v.getId()+"");
		
		if (v instanceof TableRow) {
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
			boolean flag = false;
			TableRow trow;
			TextView tr1;
			EditText edtVal = (EditText)findViewById(R.id.ntpbm_0106_edtVal);
			
			switch (v.getId()) {
			case R.id.ntpbm_0106_btn01:
				for (int i = 0; i < tl.getChildCount(); i++) {
					trow = (TableRow)tl.getChildAt(i);
					
					tr1 = (TextView)trow.findViewById(R.id.ntpbm_0106_tv);
					if (tr1.isSelected()) {
						tr1.setText(edtVal.getText());
						flag = true;
						break;
					}
				}
				
				if (!flag) {
					LayoutInflater inflater = getLayoutInflater();
					TableRow tr = (TableRow)inflater.inflate(R.layout.ntpbm_0106_tablerow, tl, false);
					
					TextView tv1 = (TextView)tr.findViewById(R.id.ntpbm_0106_tv);
					tv1.setText(edtVal.getText());
					
					trList.add(tr);
					
					tl.addView(tr);
					
					trList.get(trList.size()-1).setOnClickListener(this);
					
				}
				
				break;
			case R.id.ntpbm_0106_btn02:
				for (int i = 0; i < tl.getChildCount(); i++) {
					trow = (TableRow)tl.getChildAt(i);
					
					tr1 = (TextView)trow.findViewById(R.id.ntpbm_0106_tv);
					if (tr1.isSelected()) {
						tr1.setSelected(false);
						tr1.setTextColor(Color.BLACK);
					}
				}
				
				edtVal.setText("");
				break;
			case R.id.ntpbm_0106_imgbtn01:
				for (int i = 0; i < tl.getChildCount(); i++) {
					trow = (TableRow)tl.getChildAt(i);
					
					tr1 = (TextView)trow.findViewById(R.id.ntpbm_0106_tv);
					if (tr1.isSelected()) {
						tr1.setSelected(false);
						tr1.setTextColor(Color.BLACK);
					}
				}
				
				edtVal.setText("");
				break;
			}
		}
	}
	
	/** server connecting... search barcode... */ 
	public void searchNtpbm0106Info() {
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
		
		//init tablelayout info...
		if (tl.getChildCount() > 0) {
			tl.removeAllViews();
		}
		
		trList = new ArrayList<TableRow>();
		
		TextView tv1 = new TextView(this);
		
		LayoutInflater inflater = getLayoutInflater();
		
		//TableRow create...
		for (int i = 0; i < 3; i++) {
			
			TableRow tr = (TableRow)inflater.inflate(R.layout.ntpbm_0106_tablerow, tl, false);
			
			tv1 = (TextView)tr.findViewById(R.id.ntpbm_0106_tv);
			tv1.setText("�޸�....." + i);
			
			trList.add(tr);
			
			tl.addView(tr);
			
			trList.get(i).setOnClickListener(this);
		}
	}
	
}
