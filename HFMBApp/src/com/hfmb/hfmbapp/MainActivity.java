package com.hfmb.hfmbapp;

import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.hfmb.hfmbapp.util.CommonUtil;
import com.hfmb.hfmbapp.util.DataUtil;
import com.hfmb.hfmbapp.util.HttpConnectServer;

public class MainActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		setInit();//자신의 전화번호 가져오기.
		
		//버튼 액션.
		ImageView icon1 = (ImageView)findViewById(R.id.icon1);//연합회소개
		ImageView icon2 = (ImageView)findViewById(R.id.icon2);//연합회현황
		ImageView icon4 = (ImageView)findViewById(R.id.icon4);//교류회현황
		ImageView icon5 = (ImageView)findViewById(R.id.icon5);//회원사검색
		ImageView icon6 = (ImageView)findViewById(R.id.icon6);//교류회등록
		ImageView icon7 = (ImageView)findViewById(R.id.icon7);//회원사등록
		
		icon1.setOnClickListener(mOnClickListener);//연합회소개
		icon2.setOnClickListener(mOnClickListener);//연합회현황
		icon4.setOnClickListener(mOnClickListener);//교류회현황
		icon5.setOnClickListener(mOnClickListener);//회원사검색
		icon6.setOnClickListener(mOnClickListener);//교류회등록
		icon7.setOnClickListener(mOnClickListener);//회원사등록
		
		icon1.setOnTouchListener(CommonUtil.imgbtnTouchListener);//연합회소개
		icon2.setOnTouchListener(CommonUtil.imgbtnTouchListener);//연합회현황
		icon4.setOnTouchListener(CommonUtil.imgbtnTouchListener);//교류회현황
		icon5.setOnTouchListener(CommonUtil.imgbtnTouchListener);//회원사검색
		icon6.setOnTouchListener(CommonUtil.imgbtnTouchListener);//교류회등록
		icon7.setOnTouchListener(CommonUtil.imgbtnTouchListener);//회원사등록
		
		if (DataUtil.phoneNum.equals("01063344115")) {
			//Debugging ...
			openDialogDebug();
  		} else {
			startAsyncTask();
  		}
	}
	
	//자기자신의 전화번호 가져오기...
	@SuppressWarnings("static-access")
	public void setInit() {
		TelephonyManager telManager = (TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE); 
		String telNum = telManager.getLine1Number();
		
		DataUtil.phoneNum = "0" + telNum.substring(telNum.indexOf("1"));
	}
	
	//전화번호를 이용한 인증처리 thread... start...
	private HashMap<String, String> rowItemData = null;
	
	private ProgressDialog dialog;//조회처리 프로그래스바..
	private int threadMaxTime = 0;
  	private Thread threadgo;
  	public void startAsyncTask() {
  		if (!DataUtil.flag) {
			threadMaxTime = 0; 
	  		dialog = ProgressDialog.show(this, "", DataUtil.phoneNum + " 잠시만 기다려 주세요 ...", true);
			threadgo = new Thread(mRunnable);
			threadgo.start();
  		}
	}
  	
  	// threadgo 의 runnable 
	Runnable mRunnable = new Runnable() {           
        public void run() { 
            while (true) {   
                try {
                	//데이터 조회.
                	rowItemData = searchData();
                	
                	Thread.sleep(1000);//1초 간으로 진행.
                	
                	//데이터 조회 완료 여부 체크
                	if (rowItemData != null) {
                    	Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                        break;
                	}
                	
                	//쓰레드 4회 이상일떄 stop 처리.
                	if (4 <= threadMaxTime) {
                		Message msg = handler.obtainMessage();
                		//rowItemData = null;
                        handler.sendMessage(msg);
                        break;
                	}
                    
                	threadMaxTime++;
                	
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    };
    
    //조회한다.
  	public HashMap<String, String> searchData() {
  		HashMap<String, String> rowItem = null;
  		
      	//조회조건에 따라서 서버와 통신한다.
      	StringBuffer strbuf = new StringBuffer();
      	StringBuffer urlbuf = new StringBuffer();
      	
  		String hfmbSrchNm = DataUtil.phoneNum;
  		
      	if(hfmbSrchNm == null) hfmbSrchNm = "";
      	
      	strbuf.append("srch_gubun=0");
  		strbuf.append("&srch_nm=" + hfmbSrchNm);
  		
      	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_001.jsp");
      	
  		//server connecting... login check...
  		HttpConnectServer server = new HttpConnectServer();
  		StringBuffer resultInfo = server.sendByHttp(strbuf, urlbuf.toString());
  		
  		CommonUtil.showMessage("Debugging", "resultInfo = " + resultInfo);
  		
  		if (resultInfo.toString().startsWith("error")) {
  			return rowItem;//에러발생.
  		}
  		
  		List<HashMap<String, String>> rowItems = server.jsonParserList(resultInfo.toString(), DataUtil.jsonName);
  		
  		if (rowItems != null) {
  			if (rowItems.size() > 0) {
  				rowItem = rowItems.get(0);
  				
				//교류회등록가능 사용자. 1=admin, 2=power, 3=general
  				DataUtil.searchYn = true;
  				DataUtil.insertYn = Integer.parseInt(rowItem.get("auth_div_cd"));
  				DataUtil.meetingCd = rowItem.get("meeting_cd");
  				DataUtil.ceoNm = rowItem.get("ceo_nm");
  			} else {
  				DataUtil.searchYn = false;
  				DataUtil.insertYn = 0;
  				DataUtil.meetingCd = "000000";
  				DataUtil.ceoNm = "Guest";
  			}
  		} else {
  			DataUtil.searchYn = false;
  			DataUtil.insertYn = 0;
  			DataUtil.meetingCd = "000000";
  			DataUtil.ceoNm = "Guest";
  		}
  		return rowItem;
  	}
    
	//thread handler.. 결과 처리.
	Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
    		dialog.dismiss();//조회처리 프로그래스바.. 닫기
    		
    		//thread stop ... 
    		try {
            	threadgo.join();
            } catch(InterruptedException e) {
            	Log.i("thread", "Thread  stopping...... enterrupted Exception...");
            }
    		
    		if (rowItemData == null) {
        		openDialogFail(DataUtil.phoneNum + " 로그인 실패! 온라인 여부 또는 가입여부를 확인하세요.");
        	} else {
        		openDialogAlert(DataUtil.ceoNm + "님 로그인 하셨습니다.");
        	}
        }
    };
    
    //로그인 성공시.
    public void openDialogAlert(String title) {
		//확인 다이얼로그
		new AlertDialog.Builder(MainActivity.this)
        .setTitle(title)
		.setPositiveButton( "확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				DataUtil.flag = true;
			}
		})
        .show();
	}
  	
  	//로그인 실패시
  	public void openDialogFail(String title) {
  		//확인 다이얼로그
  		new AlertDialog.Builder(MainActivity.this)
          .setTitle(title)
  		.setNegativeButton( "재시도", new DialogInterface.OnClickListener() {
  			@Override
  			public void onClick(DialogInterface dialog, int which) {
  				// TODO Auto-generated method stub
  				DataUtil.flag = false;
  				startAsyncTask();
  			}
  		})
  		.setPositiveButton( "닫기", new DialogInterface.OnClickListener() {
  			@Override
  			public void onClick(DialogInterface dialog, int which) {
  				// TODO Auto-generated method stub
  				DataUtil.flag = false;
  				//finishApp();
  			}
  		})
          .show();
  	}
  	
  	public void finishApp() {
  		this.finish();
  	}
	
	//이미지 클릭시.
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			
			Intent intent;
			int id = view.getId();
			if (id == R.id.icon1) {
				intent = new Intent(getApplicationContext(), HfmbActivity001.class);
			} else if (id == R.id.icon2) {
				intent = new Intent(getApplicationContext(), HfmbActivity002.class);
			} else if (id == R.id.icon4) {
				intent = new Intent(getApplicationContext(), HfmbActivity003.class);
			} else if (id == R.id.icon5) {
				intent = new Intent(getApplicationContext(), HfmbActivity004.class);
			} else if (id == R.id.icon6) {
				intent = new Intent(getApplicationContext(), HfmbActivity005.class);
			} else if (id == R.id.icon7) {
				intent = new Intent(getApplicationContext(), HfmbActivity006.class);
			} else {
				intent = new Intent(getApplicationContext(), HfmbActivity001.class);
			}
			startActivity(intent);
		}
	};
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(false);
			ab.hide();
		}
	}
    
    /* Start.................
     * Debugging .... Test 하기 위함. (개발자를 위함)
     */
    public EditText phonenumEditText;
	public void openDialogDebug() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); //inflater 생성
		View viewInDialog = inflater.inflate(R.layout.daillog_login, null); //inflater로 View 객체에 레이아웃 넣기
		
		DataUtil.phoneNum = "01036252271";
		
		phonenumEditText = (EditText)viewInDialog.findViewById(R.id.pwmes0001_value01);
		phonenumEditText.setText(DataUtil.phoneNum);
		
		new AlertDialog.Builder(MainActivity.this)
	        .setTitle("Debugging")
	  		.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
	  			@Override
	  			public void onClick(DialogInterface dialog, int which) {
	  				// TODO Auto-generated method stub
	  				checkLogin();
	  			}
	  		})
	  		.setView(viewInDialog)
	  		.show();
	}
    	
	//로그인 정보를 체크한다.//테스트...
	public void checkLogin() {
		String phonenum = phonenumEditText.getText().toString();
	
		DataUtil.flag = false;
		if (phonenum == null || phonenum.equals("")) {
			
		} else {
			Log.i("Input Data", "phonenum: " + phonenum);
			
			DataUtil.phoneNum = "0" + phonenum.substring(phonenum.indexOf("1"));
			
			Log.i("Input Data", "telNum: " + DataUtil.phoneNum);
			
			if (DataUtil.phoneNum.equals("01063344115")) {
				Log.i("BRAND", "telNum: " + phonenum);
				Log.i("BRAND", "BRAND: " + Build.BRAND);
	  		}
			
			startAsyncTask();
		}
	}
	/* End.............
     * Debugging .... Test 하기 위함. (개발자를 위함)
     */
}

