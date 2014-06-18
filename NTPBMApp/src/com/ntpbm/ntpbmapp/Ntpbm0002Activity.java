package com.ntpbm.ntpbmapp;

import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class Ntpbm0002Activity extends Activity {
	
	public String loginId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0002);
		
		Intent intent = getIntent();
		loginId = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		//server connecting... search person infomation
		searchNtpbm0002Info();
		
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
		    ab.setSubtitle("개인정보");
		}
	}
	
	// Option menu에 아이템 채움.
	// Option menu 호출 시 한번만 실행됨.
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
	
	/** server connecting... search barcode... */ 
	public void searchNtpbm0002Info() {
		
		HashMap<String, String> personInfo = MainActivity.personInfo;
		
		((TextView)findViewById(R.id.nameEdtVal)).setText(personInfo.get("EPLY_NM"));
		((TextView)findViewById(R.id.idEdtVal)).setText(personInfo.get("EPLY_CD"));
		((TextView)findViewById(R.id.emailEdtVal)).setText(personInfo.get("E_MAIL"));
		((TextView)findViewById(R.id.hpnumEdtVal)).setText(personInfo.get("HP_NUM"));
		((TextView)findViewById(R.id.phonenumEdtVal)).setText(personInfo.get("TEL_NUM"));
		((TextView)findViewById(R.id.hpnumEdtVal)).setText(personInfo.get("TEL_NUM"));
		((TextView)findViewById(R.id.faxnumEdtVal)).setText(personInfo.get("FAX_NUM"));
		((TextView)findViewById(R.id.addrEdtVal)).setText(personInfo.get("ADDR"));
		((TextView)findViewById(R.id.homepageEdtVal)).setText(personInfo.get("HOMEPAGE"));
		
		String SIGN_FILENM = personInfo.get("SIGN_FILENM");
		
		//서명 이미지 처리하기...
		String imagePath = MainActivity.domainUrl + MainActivity.ntpbmPath + SIGN_FILENM;
		
		HttpConnectServer server = new HttpConnectServer();
		Bitmap bm = server.LoadImage(imagePath);
		
		((ImageView)findViewById(R.id.signatureImgVal)).setImageBitmap( bm ) ;
	}
}
