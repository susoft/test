package com.ntpbm.ntpbmapp.app0100;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ntpbm.ntpbmapp.CustomBaseAdapter;
import com.ntpbm.ntpbmapp.HttpConnectServer;
import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;
import com.ntpbm.ntpbmapp.RowItem;

/*
 * 설치장치의 설치확인
 */
public class Ntpbm0107Activity extends Activity {
	
	private String message;
	private String barcode;
	
	//private View mImgBtn01, mImgBtn02, mImgBtn03, mImgBtn04, mImgBtn05, mImgBtn06;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0107);
		
		Intent intent = getIntent();
		message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		barcode = intent.getStringExtra("barcode");
		
		//바코드(sn_cd)를 이용한 데이터조회  server connecting... search barcode...
		searchNtpbm0107Info_0107();//설치장소 및 납품업체(구매처)
		
		if (message.equals(MainActivity.ntpbm_0200)) {
			//장비조회에서 들어온 경우
			findViewById(R.id.ntpbm_0107_linearLayout07).setVisibility(View.GONE);
			findViewById(R.id.ntpbm_0107_btn01).setVisibility(View.GONE);
			findViewById(R.id.ntpbm_0107_btn02).setVisibility(View.GONE);
		} else if (message.equals(MainActivity.ntpbm_0107)) {
			//설치확인 - 고객확인 을 보이게 한다.
			findViewById(R.id.ntpbm_0107_linearLayout07).setVisibility(View.GONE);
			findViewById(R.id.ntpbm_0107_btn02).setVisibility(View.GONE);
			
			//고객사인정보를 보여준다.(있다면...)
			File myDir1 = new File(Environment.getExternalStorageDirectory().getPath() + "/ntpbm/Draw/" + barcode);
			
			String resultStr_fname = barcode + "_custom_" + ".png";
			String resultStr_myDir = myDir1.toString();
			
			findViewById(R.id.ntpbm_0107_linearLayout07).setVisibility(View.VISIBLE);
			
			BitmapFactory.Options bo = new BitmapFactory.Options();  
			bo.inSampleSize = 8;
			Bitmap bmp = BitmapFactory.decodeFile(resultStr_myDir + File.separator + resultStr_fname);
			
			((ImageView)findViewById(R.id.imageView1)).setImageBitmap(bmp);
			
		} else if (message.equals(MainActivity.ntpbm_01071)) {
			//전송 - 사인, 전송 을 보이게 한다.
			findViewById(R.id.ntpbm_0107_linearLayout07).setVisibility(View.GONE);
			findViewById(R.id.ntpbm_0107_btn01).setVisibility(View.GONE);
		}
		
		/*mImgBtn01 = findViewById(R.id.ntpbm_0107_linearLayout01);
		ImageButton imgBtn01 = (ImageButton)findViewById(R.id.ntpbm_0107_img01);
		imgBtn01.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mImgBtn01.getVisibility() == View.VISIBLE) {
					mImgBtn01.setVisibility(View.GONE);
				} else {
					mImgBtn01.setVisibility(View.VISIBLE);
				}
			}
		});*/
		
		/*mImgBtn02 = findViewById(R.id.ntpbm_0107_linearLayout02);
		ImageButton imgBtn02 = (ImageButton)findViewById(R.id.ntpbm_0107_img02);
		imgBtn02.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mImgBtn02.getVisibility() == View.VISIBLE)
					mImgBtn02.setVisibility(View.GONE);
				else 
					mImgBtn02.setVisibility(View.VISIBLE);
			}
		});*/
		
		/*mImgBtn03 = findViewById(R.id.ntpbm_0107_linearLayout03);
		ImageButton imgBtn03 = (ImageButton)findViewById(R.id.ntpbm_0107_img03);
		imgBtn03.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mImgBtn03.getVisibility() == View.VISIBLE)
					mImgBtn03.setVisibility(View.GONE);
				else 
					mImgBtn03.setVisibility(View.VISIBLE);
			}
		});*/
		
		/*mImgBtn04 = findViewById(R.id.ntpbm_0107_linearLayout04);
		ImageButton imgBtn04 = (ImageButton)findViewById(R.id.ntpbm_0107_img04);
		imgBtn04.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mImgBtn04.getVisibility() == View.VISIBLE)
					mImgBtn04.setVisibility(View.GONE);
				else 
					mImgBtn04.setVisibility(View.VISIBLE);
			}
		});*/
		
		//제품정보 - 악세사리목록...
		/*mImgBtn05 = findViewById(R.id.ntpbm_0107_linearLayout05);
		ImageButton imgBtn05 = (ImageButton)findViewById(R.id.ntpbm_0107_img05);
		imgBtn05.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mImgBtn05.getVisibility() == View.VISIBLE)
					mImgBtn05.setVisibility(View.GONE);
				else 
					mImgBtn05.setVisibility(View.VISIBLE);
			}
		});*/
		
		/*mImgBtn06 = findViewById(R.id.ntpbm_0107_linearLayout06);
		ImageButton imgBtn06 = (ImageButton)findViewById(R.id.ntpbm_0107_img06);
		imgBtn06.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (mImgBtn06.getVisibility() == View.VISIBLE)
					mImgBtn06.setVisibility(View.GONE);
				else 
					mImgBtn06.setVisibility(View.VISIBLE);
			}
		});*/
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	//설치장소, 설치주소, 설치상세주소, 담당자, 전화번호, 휴대전화, 이메일, 우편번호, 주소 + 상세주소
	private String[] jsonName_0107 = {"C_NM", "ET_NM", "CHA_PLACE"};
	private List<HashMap<String, String>> parseredDataList_0107;
		
	/** 
	 * 바코드(sn_cd)를 이용한 데이터조회  server connecting... search barcode... 
	 * 설치장소
	 */ 
	public void searchNtpbm0107Info_0107() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0107).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
		parseredDataList_0107 = server.jsonParserArrayList(resultInfo.toString(), jsonName_0107);

		//서버에서 조회한 정보를 화면에 보여준다.
		setData_0107();

		searchNtpbm0107Info_0103();//제품정보(악세사리)
	}

	/*
	 * 서버에서 조회한 정보를 화면에 보여준다.(설치장소)
	 */
	private void setData_0107() {
		if (parseredDataList_0107 != null) {
			HashMap<String, String> parseredData = null;
			for(int i = 0; i < parseredDataList_0107.size(); i++) {
				parseredData = parseredDataList_0107.get(i);
				
				//서버에서 파일을 다운로드 하여 로컬에 저장한다.
				((TextView)findViewById(R.id.ntpbm_0107_txtNm_0100)).setText("제조사정보 : " + parseredData.get(jsonName_0107[0]));
				((TextView)findViewById(R.id.ntpbm_0107_txtNm_0300)).setText("구매처정보 : " + parseredData.get(jsonName_0107[1]));
				((TextView)findViewById(R.id.ntpbm_0107_txtNm_0400)).setText("설치장소 : " + parseredData.get(jsonName_0107[2]));
			}
		}
	}

	//순번, 품목코드, 브랜드명, 모델명, 식별자, 소모품S/N번호, 유효일자, 확인여부
	private String[] jsonName_0103 = {"SEQ", "IT_CD", "IT_NM", "MD_NM", "GD_OT", "SN_S_CD", "EXP_DATE", "CHK_TF"};
	private List<HashMap<String, String>> parseredDataList_0103;
		
	/** 
	 * 바코드(sn_cd)를 이용한 데이터조회  server connecting... search barcode... 
	 * 악세사리
	 */ 
	public void searchNtpbm0107Info_0103() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0103).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
		parseredDataList_0103 = server.jsonParserArrayList(resultInfo.toString(), jsonName_0103);

		//서버에서 조회한 정보를 화면에 보여준다.
		setData_0103();
		
		setDisplay();
	}

	/*
	 * 서버에서 조회한 정보를 화면에 보여준다.(악세사리)
	 */
	private void setData_0103() {
		if (parseredDataList_0103 != null) {
			titles = new String[parseredDataList_0103.size()];//악세사리 리스트
			descriptions = new String[parseredDataList_0103.size()];//temp
			chkboxInfo = new String[parseredDataList_0103.size()];//체크여부
			
			HashMap<String, String> parseredData = null;
			for(int i = 0; i < parseredDataList_0103.size(); i++) {
				parseredData = parseredDataList_0103.get(i);
				
				titles[i] = parseredData.get(jsonName_0103[2]);//악세사리명.
				chkboxInfo[i] = parseredData.get(jsonName_0103[7]);//체크됨.
				descriptions[i] = "";
			}
		}
	}
	
	private static String[] titles = new String[] { "브랜드", "배터리", "전극패드", "수건", "메뉴얼", "보관함" };
	
	private static final Integer[] images = { R.drawable.img_ntpbm_0100_01,             
		R.drawable.img_ntpbm_0100_02, R.drawable.img_ntpbm_0100_03, R.drawable.img_ntpbm_0100_04,
		R.drawable.img_ntpbm_0100_05, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06
		, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06};
	
	
	private static String[] descriptions = new String[] {"", "", "", "", "", "" };
	private static String[] chkboxInfo = new String[] {"", "", "", "", "", "" };
	
	private ListView listView;
	private List<RowItem> rowItems;
	private CustomBaseAdapter adapter;
	
	/*
	 * 목록을 refresh 한다.
	 */
	private void setDisplay() {
		rowItems = new ArrayList<RowItem>();         
		for (int i = 0; i < titles.length; i++) {             
			RowItem item = new RowItem(images[0], titles[i], descriptions[i], chkboxInfo[i]);
			rowItems.add(item);
		}
		listView = (ListView) findViewById(R.id.list2);
		
		adapter = new CustomBaseAdapter(this, rowItems, R.layout.list_view02);         
		listView.setAdapter(adapter);
	}
	
	/** 
	 * Called when the user clicks the confirmCustom button 
	 * 고객확인 화면으로 이동한다.(서명하는 화면)
	 */ 
	public void confirmCustom(View view) {
		// Do something in response to button
		Intent intent = new Intent(this, Ntpbm0109Activity.class);
		intent.putExtra("barcode", barcode);
		startActivityForResult(intent, 1);
	}
	
	/** 
	 * Called when the user clicks the sendEmail button 
	 * 이메일 전송한다.
	 */ 
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
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 * 고객서명확인 후 리턴되는 정보에 따라서 처리한다.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		//MainActivity.logView(getApplicationContext(), requestCode + ":" + resultCode);
		
		if (resultCode == -1) {
			String resultStr_fname = data.getStringExtra("ntpbm0109_regi_fname");
			String resultStr_myDir = data.getStringExtra("ntpbm0109_regi_myDir");
			
			findViewById(R.id.ntpbm_0107_linearLayout07).setVisibility(View.VISIBLE);
			
			BitmapFactory.Options bo = new BitmapFactory.Options();  
			bo.inSampleSize = 8;
			Bitmap bmp = BitmapFactory.decodeFile(resultStr_myDir + File.separator + resultStr_fname);
			
			((ImageView)findViewById(R.id.imageView1)).setImageBitmap(bmp);
		}
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////

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
