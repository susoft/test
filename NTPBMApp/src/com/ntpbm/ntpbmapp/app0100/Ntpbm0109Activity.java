package com.ntpbm.ntpbmapp.app0100;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ntpbm.ntpbmapp.HttpConnectServer;
import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;
import com.ntpbm.ntpbmapp.Util;

/*
 * 설치장치의 고객 사인 확인
 */
public class Ntpbm0109Activity extends Activity {
	
	private String barcode;
	
	//납품업자
	private DrawView1 drawView1;
	ArrayList<Vertex1> arVertex1;
	LinearLayout linear1;
	
	//고객
	private DrawView2 drawView2;
	ArrayList<Vertex2> arVertex2;
	LinearLayout linear2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0109);
		
		Intent intent = getIntent();
		barcode = intent.getStringExtra("barcode");
		
		Log.i("barcode", barcode);
		
		//바코드(sn_cd)를 이용한 데이터조회  server connecting... search barcode...
		searchNtpbm0104Info();
		
		//납품자명 세팅. 납품자서명 세팅. 로그인사번...
		//{"EPLY_CD", "EPLY_NM", "iPSW_NUM", "HP_NUM", "E_MAIL", "SIGN_KOR_FILE_NM"};
		HashMap<String, String> person = MainActivity.personInfo;
		((EditText)findViewById(R.id.ntpbm_0109_edit01)).setText(person.get("EPLY_NM"));
		
		drawView1 = new DrawView1(this);
		linear1 = (LinearLayout)findViewById(R.id.ntpbm_0109_linearLayout03);
		linear1.addView(drawView1);
		arVertex1 = new ArrayList<Vertex1>();
		
		//담당자명 세팅.
		
		drawView2 = new DrawView2(this);
		linear2 = (LinearLayout)findViewById(R.id.ntpbm_0109_linearLayout00);
		linear2.addView(drawView2);
		arVertex2 = new ArrayList<Vertex2>();
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	//설치장소, 설치주소, 설치상세주소, 담당자, 전화번호, 휴대전화, 이메일, 우편번호, 주소 + 상세주소
	private String[] jsonName = {"CHA_PLACE", "CHA_ADDR", "CHA_ADDR_DTI", "CHA_NM", "CHA_TEL", "CHA_HP", "CHA_E_MAIL", "CHA_ZIP_NUM", "CHA_ADDRESS"};
	private List<HashMap<String, String>> parseredDataList;
	
	/** 
	 * 바코드(sn_cd)를 이용한 데이터조회  server connecting... search barcode... 
	 */ 
	private void searchNtpbm0104Info() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//서버 url 경로를 xml에서 가져온다.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0104).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
		parseredDataList = server.jsonParserArrayList(resultInfo.toString(), jsonName);

		//서버에서 조회한 정보를 화면에 보여준다.
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
				((TextView)findViewById(R.id.ntpbm_0109_txtNm_0200)).setText("담당자명 : " + parseredData.get(jsonName[3]));//담당자명
			}
		}
	}
	
	
	/** 
	 * Called when the user clicks the regi button 
	 * 고객서명 등록..
	 */ 
	public void regi(View view) {
		// Do something in response to button
		
		String fname = barcode + "_custom_" + ".jpg";
		
		File myDir = new File(Util.path + "Draw");
		if (!myDir.isDirectory()) myDir.mkdirs();
        
        File myDir1 = new File(Util.path + "Draw/" + barcode);
        if (!myDir1.isDirectory()) myDir1.mkdirs();
        
        File file = new File (myDir1, fname);
        if (file.exists ()) file.delete ();
        
        String fnamepath = file.getAbsolutePath ();
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream (fnamepath);
            
            linear2.buildDrawingCache();
            Bitmap bitmap = linear2.getDrawingCache();
            bitmap.compress (CompressFormat.JPEG, 100, fos);
            
            //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
            
            //서버에 서명 전송하기.
            updateSign(fname, myDir1.toString());

            //sending file name..
            Intent intent = getIntent();
            intent.putExtra("ntpbm0109_regi_fname", fname);
            intent.putExtra("ntpbm0109_regi_myDir", myDir1.toString());
            
            setResult(RESULT_OK, intent);
            
            this.finish();
            
        } catch (Throwable ex) {
        	Toast.makeText (getApplicationContext(), "Error: " + ex.getMessage (), Toast.LENGTH_LONG).show ();
        	ex.printStackTrace ();
        }
	}
	
	/** 
	 * Called when the user clicks the reset button 
	 * 고객서명 초기화..
	 */ 
	public void reset(View view) {
		// Do something in response to button
		drawView2 = new DrawView2(this);
		
		linear2 = (LinearLayout)findViewById(R.id.ntpbm_0109_linearLayout00);
		
		linear2.addView(drawView2);
		
		arVertex2 = new ArrayList<Vertex2>();
		
		linear2.invalidate();
		linear2.postInvalidate();
		//linear2.notifyAll();
	}
	
	/** 
	 * Called when the user clicks the cancel button 
	 * 취소(화면 나가기)
	 */ 
	public void cancel(View view) {
		// Do something in response to button
		this.finish();
	}
	
	private void updateSign(String filename, String filepath) {
		StringBuffer urlbuf = new StringBuffer();
    	HashMap<String, String> params = new HashMap<String, String>();
    	
    	String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    	
    	params.put("sn_cd", barcode);
    	params.put("isp_date", timeStamp);//설치일자
    	params.put("dlv_nm", ((TextView)findViewById(R.id.ntpbm_0109_edit01)).getText().toString());//납품자명

    	Log.i("json: before", params.get("isp_date"));
    	Log.i("json: before", params.get("dlv_nm"));
    	
    	String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0109_sign).toString();
		urlbuf.append(urlStr);
    	
    	HttpConnectServer server = new HttpConnectServer();
    	StringBuffer resultInfo = server.HttpFileUpload(urlbuf.toString(), params, filepath + File.separator + filename);
    	
    	Log.i("json:result", resultInfo.toString().trim());
    	
    	MainActivity.logView(getApplicationContext(), "정상처리되었습니다.");
    	//DialogSimple("처리결과", "정상처리되었습니다.");
	}
	
	///////////납품자 서명
	public class Vertex1 {
		float x;
		float y;
		boolean Draw;
		int color;
		   
		Vertex1(float ax, float ay, boolean ad, int color) {
			x = ax;
			y = ay;
			Draw = ad;
			this.color=color;
		}
	}
	
	protected class DrawView1 extends View {
		Paint mPaint;
	    boolean clear;
	    int co;
	    public DrawView1(Context context) {
	        super(context);
	        
	        // Paint 객체 미리 초기화
			mPaint = new Paint();
			mPaint.setStrokeWidth(3); //두께 설정
			mPaint.setAntiAlias(true); //부드러운 표현
	  
			clear = false;
			co=Color.BLACK;
	    }
	    public void onDraw(Canvas canvas) {
	    	canvas.drawColor(0xffe0e0e0);//배경 하얀색으로 덮어(도화지 배경을 하얗게 칠함)
	  
	    	// 정점을 순회하면서 선분으로 잇는다.
	    	for (int i=0;i<arVertex1.size();i++) {
	    		if (arVertex1.get(i).Draw) {
	    			mPaint.setColor(arVertex1.get(i).color);
	    			//
	    			canvas.drawLine(arVertex1.get(i-1).x, arVertex1.get(i-1).y, 
	    					arVertex1.get(i).x, arVertex1.get(i).y, mPaint);
	    		}
	    	}
	    }
	    
	    // 터치 이동시마다 정점들을 추가한다.
	    public boolean onTouchEvent(MotionEvent event) {
	    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    		arVertex1.add(new Vertex1(event.getX(), event.getY(), false, co));
	    		return true;
	    	}
	    	if (event.getAction() == MotionEvent.ACTION_MOVE) {
	    		arVertex1.add(new Vertex1(event.getX(), event.getY(), true, co));
	    		invalidate();//화면에 그림을 그림 -> onDraw()실행함.
	    		return true;
	    	}	
	    	return false;
	    }
	}

	//고객 서명
	public class Vertex2 {
		float x;
		float y;
		boolean Draw;
		int color;
		   
		Vertex2(float ax, float ay, boolean ad, int color) {
			x = ax;
			y = ay;
			Draw = ad;
			this.color=color;
		}
	}
	
	protected class DrawView2 extends View {
		Paint mPaint;
	    boolean clear;
	    int co;
	    public DrawView2(Context context) {
	        super(context);
	        
	        // Paint 객체 미리 초기화
			mPaint = new Paint();
			mPaint.setStrokeWidth(3); //두께 설정
			mPaint.setAntiAlias(true); //부드러운 표현
	  
			clear = false;
			co=Color.BLACK;
	    }
	    public void onDraw(Canvas canvas) {
	    	canvas.drawColor(0xffe0e0e0);//배경 하얀색으로 덮어(도화지 배경을 하얗게 칠함)
	  
	    	// 정점을 순회하면서 선분으로 잇는다.
	    	for (int i=0;i<arVertex2.size();i++) {
	    		if (arVertex2.get(i).Draw) {
	    			mPaint.setColor(arVertex2.get(i).color);
	    			//
	    			canvas.drawLine(arVertex2.get(i-1).x, arVertex2.get(i-1).y, 
	    					arVertex2.get(i).x, arVertex2.get(i).y, mPaint);
	    		}
	    	}
	    }
	    
	    // 터치 이동시마다 정점들을 추가한다.
	    public boolean onTouchEvent(MotionEvent event) {
	    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    		arVertex2.add(new Vertex2(event.getX(), event.getY(), false, co));
	    		return true;
	    	}
	    	if (event.getAction() == MotionEvent.ACTION_MOVE) {
	    		arVertex2.add(new Vertex2(event.getX(), event.getY(), true, co));
	    		invalidate();//화면에 그림을 그림 -> onDraw()실행함.
	    		return true;
	    	}	
	    	return false;
	    }
	}
	
	
	
	////////////////////////////////////////////////////////

	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(false);
		    ab.setTitle(R.string.app_name);
		    ab.setSubtitle("고객서명");
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
