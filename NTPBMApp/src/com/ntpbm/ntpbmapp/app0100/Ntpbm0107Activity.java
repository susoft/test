package com.ntpbm.ntpbmapp.app0100;

import java.io.File;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;

public class Ntpbm0107Activity extends Activity {
	
	public String message;
	
	View mImgBtn01, mImgBtn02, mImgBtn03, mImgBtn04, mImgBtn05, mImgBtn06;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0107);
		
		Intent intent = getIntent();
		message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		MainActivity.logView(getApplicationContext(), message);
		
		if (message.equals(MainActivity.ntpbm_0200)) {
			//장비조회에서 들어온 경우
			findViewById(R.id.ntpbm_0107_btn01).setVisibility(View.GONE);
			findViewById(R.id.ntpbm_0107_btn02).setVisibility(View.GONE);
		} else if (message.equals(MainActivity.ntpbm_0107)) {
			//설치확인 - 고객확인 을 보이게 한다.
			findViewById(R.id.ntpbm_0107_linearLayout07).setVisibility(View.GONE);
			findViewById(R.id.ntpbm_0107_btn02).setVisibility(View.GONE);
		} else if (message.equals(MainActivity.ntpbm_01071)) {
			//전송 - 사인, 전송 을 보이게 한다.
			findViewById(R.id.ntpbm_0107_btn01).setVisibility(View.GONE);
		}
		
		mImgBtn01 = findViewById(R.id.ntpbm_0107_linearLayout01);
		ImageButton imgBtn01 = (ImageButton)findViewById(R.id.ntpbm_0107_img01);
		imgBtn01.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mImgBtn01.getVisibility() == View.VISIBLE) {
					mImgBtn01.setVisibility(View.GONE);
				} else {
					mImgBtn01.setVisibility(View.VISIBLE);
				}
			}
		});
		
//		mImgBtn02 = findViewById(R.id.ntpbm_0107_linearLayout02);
//		ImageButton imgBtn02 = (ImageButton)findViewById(R.id.ntpbm_0107_img02);
//		imgBtn02.setOnClickListener(new Button.OnClickListener() {
//			public void onClick(View v) {
//				if (mImgBtn02.getVisibility() == View.VISIBLE)
//					mImgBtn02.setVisibility(View.GONE);
//				else 
//					mImgBtn02.setVisibility(View.VISIBLE);
//			}
//		});
		
		mImgBtn03 = findViewById(R.id.ntpbm_0107_linearLayout03);
		ImageButton imgBtn03 = (ImageButton)findViewById(R.id.ntpbm_0107_img03);
		imgBtn03.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mImgBtn03.getVisibility() == View.VISIBLE)
					mImgBtn03.setVisibility(View.GONE);
				else 
					mImgBtn03.setVisibility(View.VISIBLE);
			}
		});
		
		mImgBtn04 = findViewById(R.id.ntpbm_0107_linearLayout04);
		ImageButton imgBtn04 = (ImageButton)findViewById(R.id.ntpbm_0107_img04);
		imgBtn04.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mImgBtn04.getVisibility() == View.VISIBLE)
					mImgBtn04.setVisibility(View.GONE);
				else 
					mImgBtn04.setVisibility(View.VISIBLE);
			}
		});
		
		mImgBtn05 = findViewById(R.id.ntpbm_0107_linearLayout05);
		ImageButton imgBtn05 = (ImageButton)findViewById(R.id.ntpbm_0107_img05);
		imgBtn05.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mImgBtn05.getVisibility() == View.VISIBLE)
					mImgBtn05.setVisibility(View.GONE);
				else 
					mImgBtn05.setVisibility(View.VISIBLE);
			}
		});
		
		mImgBtn06 = findViewById(R.id.ntpbm_0107_linearLayout06);
		ImageButton imgBtn06 = (ImageButton)findViewById(R.id.ntpbm_0107_img06);
		imgBtn06.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mImgBtn06.getVisibility() == View.VISIBLE)
					mImgBtn06.setVisibility(View.GONE);
				else 
					mImgBtn06.setVisibility(View.VISIBLE);
			}
		});
		
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
	
	/** Called when the user clicks the confirmCustom button */ 
	public void confirmCustom(View view) {
		// Do something in response to button
		Intent intent = new Intent(this, Ntpbm0109Activity.class);
		//startActivity(intent);

		startActivityForResult(intent, 1);
	}
	
	/** Called when the user clicks the sendEmail button */ 
	public void sendEmail(View view) {
		// Do something in response to button
		
		//SERVER CONNECTING...
		
		//SEND MAIL....
		
		Intent email = new Intent(Intent.ACTION_SEND);
		
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{"ouyoungdm@lycos.co.kr"});
	    email.putExtra(Intent.EXTRA_SUBJECT, "testing mail..");
	    email.putExtra(Intent.EXTRA_TEXT, "sending.... mail... ");
	    
	    String fileName = "test.html";
		String sendFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ntpbm" + File.separator + "mail" + File.separator + fileName;
		File f = new File(sendFilePath);
		if (f.exists()) {
//			MainActivity.logView(getApplicationContext(), "첨부파일이 생성되지 않앗습니다.");
			final Uri fileUri = Uri.fromFile(f);
			email.putExtra(Intent.EXTRA_STREAM, fileUri);
		}
	    
	    //need this to prompts email client only
	    email.setType("message/rfc822");
	    
	    startActivity(Intent.createChooser(email, "Choose an Email client :"));
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
//		MainActivity.logView(getApplicationContext(), requestCode + ":" + resultCode);
		
		if (resultCode == -1) {
			String resultStr_fname = data.getStringExtra("ntpbm0109_regi_fname");
			String resultStr_myDir = data.getStringExtra("ntpbm0109_regi_myDir");
			
			findViewById(R.id.ntpbm_0107_linearLayout07).setVisibility(View.VISIBLE);
			
			BitmapFactory.Options bo = new BitmapFactory.Options();  
			bo.inSampleSize = 8;  
			Bitmap bmp = BitmapFactory.decodeFile(resultStr_myDir + File.separator + resultStr_fname, bo);
			
			((ImageView)findViewById(R.id.imageView1)).setImageBitmap(bmp);
		}
	}
	
}
