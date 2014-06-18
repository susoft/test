package com.ntpbm.ntpbmapp.app0100;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;
import com.ntpbm.ntpbmapp.SpinnerAdapter;

public class Ntpbm0108Activity extends Activity implements OnClickListener {
	
	TableLayout tl;
	TableRow tr[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0108);
		
		//server connecting... search barcode...
		searchNtpbm0108Info();
		
		/** Android-13 , Custom Adapter 참조 , 임의의 String[] 배열 사용*/
		String[] a01 = {"품명검색", "제조번호검색"};
		SpinnerAdapter a01Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a01);
		
		Spinner spin1 = (Spinner)findViewById(R.id.ntpbm_0108_spnr01);
		spin1.setAdapter(a01Adapter);
		
//		spin.setOnItemSelectedListener(this);
		
		findViewById(R.id.ntpbm_0108_btn01).setOnClickListener(this);
		findViewById(R.id.ntpbm_0108_btn02).setOnClickListener(this);
		findViewById(R.id.ntpbm_0108_btn03).setOnClickListener(this);
		findViewById(R.id.ntpbm_0108_btn04).setOnClickListener(this);
		
		tl = (TableLayout)findViewById(R.id.maintable);
		
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
		    ab.setSubtitle("보관함");
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
	
	@Override
	public void onClick(View v) {
//		MainActivity.logView(getApplicationContext(), v.getId()+"");
		
		switch (v.getId()) {
		case R.id.ntpbm_0108_btn01:
			srchItem();
			break;
		case R.id.ntpbm_0108_btn02:
			add();
			break;
		case R.id.ntpbm_0108_btn03:
			modify();
			break;
		case R.id.ntpbm_0108_btn04:
			del();
			break;
		}
		
		if (v instanceof TableRow) {
			TableRow trow;
			TextView tr1;
			TextView tr2;
			for (int i = 0; i < tl.getChildCount(); i++) {
				trow = (TableRow)tl.getChildAt(i);
				
				tr1 = (TextView)trow.findViewById(R.id.tv_table1);
				if (tr1.isSelected()) {
					tr1.setSelected(false);
					tr1.setTextColor(Color.BLACK);
					
					tr2 = (TextView)trow.findViewById(R.id.tv_table2);
					tr2.setTextColor(Color.BLACK);
					
					break;
				}
			}
			
			trow = (TableRow)v;
			
			tr1 = (TextView)trow.findViewById(R.id.tv_table1);
			tr1.setSelected(true);
			tr1.setTextColor(Color.RED);
			
			tr2 = (TextView)trow.findViewById(R.id.tv_table2);
			tr2.setTextColor(Color.RED);
		}
	}
	
	/** Called when the user clicks the srchItem button */ 
	public void srchItem() {
		// Do something in response to button
		
		//init tablelayout info...
		if (tl.getChildCount() > 0) {
			tl.removeAllViews();
		}
		
		tr = new TableRow[3];
		TextView tv1 = new TextView(this);
		TextView tv2 = new TextView(this);
		
		LayoutInflater inflater = getLayoutInflater();
		
		//TableRow create...
		for (int i = 0; i < 3; i++) {
			
			tr[i] = (TableRow)inflater.inflate(R.layout.ntpbm_0108_tablerow, tl, false);
			
			tv1 = (TextView)tr[i].findViewById(R.id.tv_table1);
			tv1.setText("품명 1...." + i);
			
			tv2 = (TextView)tr[i].findViewById(R.id.tv_table2);
			tv2.setText("모델명 1...." + i);
			
			tr[i].setOnClickListener(this);
			
			tl.addView(tr[i]);
		}
	}
	
	/** Called when the user clicks the add button */ 
	public void add() {
		// Do something in response to button
	}
	
	/** Called when the user clicks the modify button */ 
	public void modify() {
		// Do something in response to button
	}
	
	/** Called when the user clicks the modify button */ 
	public void del() {
		// Do something in response to button
		
		TableRow trow;
		TextView tr1;
		for (int i = 0; i < tl.getChildCount(); i++) {
			trow = (TableRow)tl.getChildAt(i);
			
			tr1 = (TextView)trow.findViewById(R.id.tv_table1);
			if (tr1.isSelected()) {
				
				//server connecting.... del...
				
				tl.removeView(trow);
				
				break;
			}
		}
	}
	
	/** server connecting... search barcode... */ 
	public void searchNtpbm0108Info() {
		//1. server connecting
		
		//2. request...
		
		//3. response...
		
		
	}
}
