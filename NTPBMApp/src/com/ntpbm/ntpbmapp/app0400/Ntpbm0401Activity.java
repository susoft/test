package com.ntpbm.ntpbmapp.app0400;

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
import android.view.View.OnLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;
import com.ntpbm.ntpbmapp.SpinnerAdapter;

public class Ntpbm0401Activity extends Activity implements OnItemSelectedListener, OnClickListener, OnLongClickListener {
	
	TableLayout tl;
	TableRow tr[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0401);
		
		srchSpinFirst();
		
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
		    ab.setSubtitle("품목등록");
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
	
	/** Called when this screen enter */ 
	public void srchSpinFirst() {
		// Do something in response to button
		
		//Server connecting...
		
		/** Android-13 , Custom Adapter 참조 , 임의의 String[] 배열 사용*/
		String[] a13s = {"data01","data02","data03"};
		SpinnerAdapter a13Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a13s);
		
		Spinner spin1 = (Spinner)findViewById(R.id.ntpbm_0401_spnr00);
		spin1.setPrompt("대분류를 선택하세요.");
		spin1.setAdapter(a13Adapter);
		
		spin1.setOnItemSelectedListener(this);
	}
	
	/** Called when the user clicks the first spinner item  */ 
	public void srchSpinSecond() {
		// Do something in response to button
		
		//Server connecting...
		
		/** Android-13 , Custom Adapter 참조 , 임의의 String[] 배열 사용*/
		String[] a13s = {"data01","data02","data03"};
		SpinnerAdapter a13Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a13s);
		
		Spinner spin1 = (Spinner)findViewById(R.id.ntpbm_0401_spnr01);
		spin1.setPrompt("중분류를 선택하세요.");
		spin1.setAdapter(a13Adapter);
		
		spin1.setOnItemSelectedListener(this);
	}
	
	/** Called when the user clicks the first spinner item  */ 
	public void srchSpinThird() {
		// Do something in response to button
		
		//Server connecting...
		
		/** Android-13 , Custom Adapter 참조 , 임의의 String[] 배열 사용*/
		String[] a13s = {"data01","data02","data03"};
		SpinnerAdapter a13Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a13s);
		
		Spinner spin1 = (Spinner)findViewById(R.id.ntpbm_0401_spnr02);
		spin1.setPrompt("소분류를 선택하세요.");
		spin1.setAdapter(a13Adapter);
		
//		spin1.setOnItemSelectedListener(this);
	}
	
	@Override    
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//		MainActivity.logView(getApplicationContext(), parent.getId() +":"+ R.id.ntpbm_0401_spnr00);
		
		//Server connecting...
		switch (parent.getId()) {
		case R.id.ntpbm_0401_spnr00:
			srchSpinSecond();
			break;

		case R.id.ntpbm_0401_spnr01:
			srchSpinThird();
			break;
		}
		
	}
	
	@Override    
	public void onNothingSelected(AdapterView<?> parent) {
		//Nothing....
	}
	
	/** Called when the user clicks the regiItem button */ 
	public void srchItem(View view) {
		// Do something in response to button
		
		//init tablelayout info...
		if (tl.getChildCount() > 0) {
			tl.removeAllViews();
		}
		
		tr = new TableRow[3];
		TextView tv1 = new TextView(this);
		TextView tv2 = new TextView(this);
		TextView tv3 = new TextView(this);
		
		LayoutInflater inflater = getLayoutInflater();
		
		//TableRow create...
		for (int i = 0; i < 3; i++) {
			
			tr[i] = (TableRow)inflater.inflate(R.layout.ntpbm_0401_tablerow, tl, false);
			
			tv1 = (TextView)tr[i].findViewById(R.id.ntpbm_0401_tv1);
			tv1.setText("품명 1...." + i);
			
			tv2 = (TextView)tr[i].findViewById(R.id.ntpbm_0401_tv2);
			tv2.setText("모델명 1...." + i);
			
			tv3 = (TextView)tr[i].findViewById(R.id.ntpbm_0401_tv3);
			tv3.setText("10" + i);
			
			tr[i].setOnClickListener(this);
			tr[i].setOnLongClickListener(this);
			
			tl.addView(tr[i]);
		}
	}
	
	@Override
	public void onClick(View v) {
//		MainActivity.logView(getApplicationContext(), v.getId()+"");
		
		if (v instanceof TableRow) {
			TableRow trow;
			TextView tr1;
			TextView tr2;
			TextView tr3;
			for (int i = 0; i < tl.getChildCount(); i++) {
				trow = (TableRow)tl.getChildAt(i);
				
				tr1 = (TextView)trow.findViewById(R.id.ntpbm_0401_tv1);
				if (tr1.isSelected()) {
					tr1.setSelected(false);
					tr1.setTextColor(Color.BLACK);
					
					tr2 = (TextView)trow.findViewById(R.id.ntpbm_0401_tv2);
					tr2.setTextColor(Color.BLACK);
					
					tr3 = (TextView)trow.findViewById(R.id.ntpbm_0401_tv3);
					tr3.setTextColor(Color.BLACK);
					
					break;
				}
			}
			
			trow = (TableRow)v;
			
			tr1 = (TextView)trow.findViewById(R.id.ntpbm_0401_tv1);
			tr1.setSelected(true);
			tr1.setTextColor(Color.RED);
			
			tr2 = (TextView)trow.findViewById(R.id.ntpbm_0401_tv2);
			tr2.setTextColor(Color.RED);
			
			tr3 = (TextView)trow.findViewById(R.id.ntpbm_0401_tv3);
			tr3.setTextColor(Color.RED);
		}
	}
	
	@Override
	public boolean onLongClick(View v) {
		boolean flag = true;
		String textV = "";
		if (v instanceof TableRow) {
			TableRow trow = (TableRow)v;;
			TextView tr1 = (TextView)trow.findViewById(R.id.ntpbm_0401_tv1);
			
			textV = tr1.getText()+"";
		}
		
//		MainActivity.logView(getApplicationContext(), textV);
		
		//sending file name..
        Intent intent = getIntent();
        intent.putExtra("ntpbm0109_regi_result", textV);
        
        setResult(RESULT_OK, intent);
        
        this.finish();
		
		return flag;
	}
}
