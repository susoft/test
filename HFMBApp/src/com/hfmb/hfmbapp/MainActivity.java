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
import android.os.AsyncTask;
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
import com.hfmb.hfmbapp.util.HttpConnectServer;

public class MainActivity extends Activity {
	public static boolean flag = false;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		setInit();
		
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
		
		if (CommonUtil.phoneNum.equals("01063344115")) {
			Log.i("BRAND", "BRAND: " + Build.BRAND);
			
			//테스트 필요시
			openDialogDebug();
  		} else {
			startAsyncTask();
  		}
	}
	
	//테스트...
	public EditText phonenumEditText;
  	public void openDialogDebug() {
  		
  		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); //inflater 생성
  		View viewInDialog = inflater.inflate(R.layout.daillog_login, null); //inflater로 View 객체에 레이아웃 넣기
  		phonenumEditText = (EditText)viewInDialog.findViewById(R.id.pwmes0001_value01);
  		phonenumEditText.setText(CommonUtil.phoneNum);
  		
  		new AlertDialog.Builder(MainActivity.this)
	        .setTitle("자신의 전화번호 확인(로그인 위함)")
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
		
  		flag = false;
  		if (phonenum == null || phonenum.equals("")) {
  			
  		} else {
  			
  			String telNum = phonenum;
  			
  			Log.i("Input Data", "telNum: " + telNum);
  			
  			if (telNum.startsWith("+82")) {
  				CommonUtil.phoneNum = "0" + telNum.substring(telNum.indexOf("1"));
  			} else {
  				CommonUtil.phoneNum = "0" + telNum.substring(telNum.indexOf("1"));
//  				if (telNum.length() < 11) CommonUtil.phoneNum = "0" + telNum;
//  				else if (telNum.length() >= 11) CommonUtil.phoneNum = telNum;
//  				else CommonUtil.phoneNum = telNum;
  			}
  			
  			Log.i("Input Data", "telNum: " + CommonUtil.phoneNum);
  			
  			if (CommonUtil.phoneNum.equals("01063344115")) {
  				Log.i("BRAND", "telNum: " + telNum);
  				Log.i("BRAND", "BRAND: " + Build.BRAND);
  	  		}
  		}
		
  		startAsyncTask();
  	}
	
	@SuppressWarnings("static-access")
	public void setInit() {
		
		TelephonyManager telManager = (TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE); 
		String telNum = telManager.getLine1Number();
		
		if (telNum.startsWith("+82")) {
			CommonUtil.phoneNum = "0" + telNum.substring(telNum.indexOf("1"));
		} else {
			CommonUtil.phoneNum = "0" + telNum.substring(telNum.indexOf("1"));
			//if (telNum.length() >= 11) CommonUtil.phoneNum = telNum;
			//else CommonUtil.phoneNum = telNum;
		}
		
		if (CommonUtil.phoneNum.equals("01063344115")) {
			Log.i("BRAND", "telNum: " + telNum);
			Log.i("BRAND", "BRAND: " + Build.BRAND);
  		}
	}
	
	//이미지 클릭시.
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			
			Intent intent;
			switch (view.getId()) {
			case R.id.icon1://연합회소개
				intent = new Intent(getApplicationContext(), HfmbActivity001.class);
				break;
			case R.id.icon2://연합회현황
				intent = new Intent(getApplicationContext(), HfmbActivity002.class);
				break;
			case R.id.icon4://교류회현황
				intent = new Intent(getApplicationContext(), HfmbActivity003.class);
				break;
			case R.id.icon5://회원사검색
				intent = new Intent(getApplicationContext(), HfmbActivity004.class);
				break;
			case R.id.icon6://교류회등록
				intent = new Intent(getApplicationContext(), HfmbActivity005.class);
				break;
			case R.id.icon7://회원사등록
				intent = new Intent(getApplicationContext(), HfmbActivity006.class);
				break;
			default://연합회소개
				intent = new Intent(getApplicationContext(), HfmbActivity001.class);
				break;
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
	
	//조회한다.
  	public HashMap<String, String> searchData() {
  		HashMap<String, String> rowItem = null;
  		
      	//조회조건에 따라서 서버와 통신한다.
      	StringBuffer strbuf = new StringBuffer();
      	StringBuffer urlbuf = new StringBuffer();
      	
  		String hfmbSrchNm = CommonUtil.phoneNum;
  		
      	if(hfmbSrchNm == null) hfmbSrchNm = "";
      	
      	strbuf.append("srch_gubun=0");
  		strbuf.append("&srch_nm=" + hfmbSrchNm);
  		
      	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_001.jsp");
      	
  		//server connecting... login check...
  		HttpConnectServer server = new HttpConnectServer();
  		StringBuffer resultInfo = server.sendByHttp(strbuf, urlbuf.toString());
  		
  		if (resultInfo.toString().startsWith("error")) {
  			return rowItem;//에러발생.
  		}
  		
  		//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
  		String[] jsonName = {"id", "meeting_cd", "ceo_nm", "company_cd", "company_nm"
  				, "category_business_cd", "category_business_nm", "addr", "phone1", "phone2"
  				, "phone3", "email", "meeting_nm", "depth_div_cd"
  				, "hfmb_organ_div_cd", "hfmb_duty_div_cd", "auth_div_cd", "del_yn", "gita1"
  				, "gita2", "gita3", "input_dt", "input_tm", "update_dt"
  				, "update_tm"};	
  		
  		List<HashMap<String, String>> rowItems = server.jsonParserList(resultInfo.toString(), jsonName);
  		
  		if (rowItems != null) {
  			if (rowItems.size() > 0) {
  				rowItem = rowItems.get(0);
  				CommonUtil.searchYn = true;
  				
				//교류회등록가능 사용자. 1=admin, 2=power, 3=general
				CommonUtil.insertYn = Integer.parseInt(rowItem.get("auth_div_cd"));
				CommonUtil.meetingCd = rowItem.get("meeting_cd");
				CommonUtil.ceoNm = rowItem.get("ceo_nm");
  			} else {
  				CommonUtil.searchYn = false;
  				CommonUtil.insertYn = 0;
  				CommonUtil.meetingCd = "000000";
  				CommonUtil.ceoNm = "Guest";
  			}
  		} else {
  			CommonUtil.searchYn = false;
  			CommonUtil.insertYn = 0;
  			CommonUtil.meetingCd = "000000";
  			CommonUtil.ceoNm = "Guest";
  		}
  		return rowItem;
  	}
  	
  	public void openDialogAlert(String title) {
		//확인 다이얼로그
		new AlertDialog.Builder(MainActivity.this)
        .setTitle(title)
		.setPositiveButton( "확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				flag = true;
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
  				flag = false;
  				startAsyncTask();
  			}
  		})
  		.setPositiveButton( "닫기", new DialogInterface.OnClickListener() {
  			@Override
  			public void onClick(DialogInterface dialog, int which) {
  				// TODO Auto-generated method stub
  				flag = false;
  				//finishApp();
  			}
  		})
          .show();
  	}
  	
  	private ProgressDialog dialog;
  	public void startAsyncTask() {
  		if (!flag) {
//		if (AsyncTask.Status.PENDING == mTask.getStatus()) {
//			dialog = ProgressDialog.show(this, "", "첫번쨰... 잠시만 기다려 주세요 ...", true);
//			mTask.execute();
//		}
//		if (AsyncTask.Status.FINISHED == mTask.getStatus()) {
//			dialog = ProgressDialog.show(this, "", "두번쨰... 잠시만 기다려 주세요 ...", true);
//			mTask.execute();
//		}

			threadMaxTime = 0; 
	  		dialog = ProgressDialog.show(this, "", CommonUtil.phoneNum + " 잠시만 기다려 주세요 ...", true);
			threadgo = new Thread(mRunnable);
			threadgo.start();
  		}
	}
  	
  	public void finishApp() {
  		this.finish();
  	}
	
	//권한정보 가져오기.
	private AsyncTask<Void, Void, HashMap<String, String>> mTask = new AsyncTask<Void, Void, HashMap<String, String>>() {
		@Override
		protected HashMap<String, String> doInBackground(Void... p) {
			HashMap<String, String> rowItem = null;
            try {
            	rowItem = searchData();
            } catch (Exception e) {
                Log.e("net","오류",e);
            }
            return rowItem;
        }
        
        protected void onPostExecute(HashMap<String, String> rowItem) {
        	dialog.dismiss();
        	if (rowItem == null) {
        		openDialogFail(CommonUtil.phoneNum + "로그인 실패! 온라인 여부 또는 가입여부를 확인하세요.");
        	} else {
        		openDialogAlert(CommonUtil.ceoNm + "님 로그인 하셨습니다.");
        		//openDialogFail("로그인 실패! 온라인 여부 또는 가입여부를 확인하세요.");
        	}
        }
	};
  	
  	HashMap<String, String> rowItemData = null;
	Thread threadgo;
	Runnable mRunnable = new Runnable() {           
        public void run() { 
            while (true) {   
                try {
                	//데이터 조회.
                	rowItemData = searchData();
                	
                	Thread.sleep(1000);
                	
                	if (rowItemData != null) {
                    	Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                        break;
                	}
                	
                	if (threadMaxTime > 6) {
                		Message msg = handler.obtainMessage();
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
    
    private int threadMaxTime = 0;
	
	//thread 동작후 1초마다 그림을 가져와 gridview에 추가하는 함수
	Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
    		dialog.dismiss();
    		
    		try {
            	threadgo.join();
            } catch(InterruptedException e) {
            	Log.i("thread", "Thread  stopping...... enterrupted Exception...");
            }
    		
    		if (rowItemData == null) {
        		openDialogFail(CommonUtil.phoneNum + " 로그인 실패! 온라인 여부 또는 가입여부를 확인하세요.");
        	} else {
        		openDialogAlert(CommonUtil.ceoNm + "님 로그인 하셨습니다.");
        		//openDialogFail("로그인 실패! 온라인 여부 또는 가입여부를 확인하세요.");
        	}
        }
    };
}

