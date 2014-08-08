package com.hfmb.hfmbapp;

import java.util.ArrayList;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

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
		
		if (DataUtil.phoneNum.equals("01063344115") || DataUtil.phoneNum.equals("01026470771")) {
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
    
    //로그인정보 체크
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
  		
  		rowItems = server.jsonParserList(resultInfo.toString(), DataUtil.jsonName);
  		
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
    		
    		if (rowItems != null) {
      			if (rowItems.size() > 0) {
      				if (rowItems.size() > 1) {
      					openDial();
      				} else {
        				//교류회등록가능 사용자. 1=admin, 2=power, 3=general
          				selectedLogin(rowItems.get(0));
      				}
      			} else {
      				DataUtil.searchYn = false;
      				DataUtil.insertYn = 0;
      				DataUtil.meetingCd = "000000";
      				DataUtil.meetingNm = "Guest";
      				DataUtil.ceoNm = "Guest";
      				
      				messageLogin();
      			}
      		} else {
      			DataUtil.searchYn = false;
      			DataUtil.insertYn = 0;
      			DataUtil.meetingCd = "000000";
      			DataUtil.meetingNm = "Guest";
      			DataUtil.ceoNm = "Guest";
      			
      			messageLogin();
      		}

      		Log.d("Tag", DataUtil.insertYn + "-" + DataUtil.searchYn);
        }
    };
    
    public void selectedLogin(HashMap<String, String> rowItem) {
    	//교류회등록가능 사용자. 1=admin, 2=power, 3=general
		DataUtil.searchYn = true;
		DataUtil.insertYn = Integer.parseInt(rowItem.get("auth_div_cd"));
		DataUtil.meetingCd = rowItem.get("meeting_cd");
		DataUtil.meetingNm = rowItem.get("meeting_nm");
		DataUtil.ceoNm = rowItem.get("ceo_nm");
		
		rowItemData = rowItem;
		
		messageLogin();
    }
    
    public void messageLogin() {
    	if (rowItemData == null) {
    		openDialogFail(DataUtil.phoneNum + " 로그인 실패! 온라인 여부 또는 가입여부를 확인하세요.");
    	} else {
    		openDialogAlert(DataUtil.ceoNm + "님 로그인 하셨습니다.");
    	}
    }
    
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
  				
        		DataUtil.searchYn = false;
      			DataUtil.insertYn = 0;
      			DataUtil.meetingCd = "000000";
      			DataUtil.meetingNm = "Guest";
      			DataUtil.ceoNm = "Guest";
  			}
  		})
          .show();
  	}
  	
	//이미지 클릭시.
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			boolean inFlag = true;
			String message = "";
			Intent intent = new Intent(getApplicationContext(), HfmbActivity001.class);//연합회소개
			
			switch (view.getId()) {
			case R.id.icon1://연합회소개
				intent = new Intent(getApplicationContext(), HfmbActivity001.class);
				break;
			case R.id.icon2://연합회현황
				intent = new Intent(getApplicationContext(), HfmbActivity002.class);
				break;
			case R.id.icon4://교류회현황
				if (!DataUtil.searchYn) inFlag = false;
				message = "교류회현황은 회원사만 조회 가능합니다.";
				intent = new Intent(getApplicationContext(), HfmbActivity003.class);
				break;
			case R.id.icon5://회원사검색
				if (!DataUtil.searchYn) inFlag = false;
				message = "회원사검색은 회원사만 조회 가능합니다.";
				intent = new Intent(getApplicationContext(), HfmbActivity004.class);
				break;
			case R.id.icon6://교류회등록
				//권한체크.. -- 사무국직원만 가능.
				if (DataUtil.insertYn != 1) inFlag = false;
				message = "교류회 등록은 사무국직원만 가능합니다.";
				intent = new Intent(getApplicationContext(), HfmbActivity005.class);
				break;
			case R.id.icon7://회원사등록
				//권한체크.. -- 사무국직원, 교류회 회장, 총무 만 가능.
				if (DataUtil.insertYn != 1 && DataUtil.insertYn != 2) inFlag = false;
				message = "회원사 등록은 사무국직원과 교류회 회장,총무만 가능합니다.";
				intent = new Intent(getApplicationContext(), HfmbActivity006.class);
				break;
			}
			
			if (inFlag) {
				startActivity(intent);
			} else {
				openDialogAlert(message);//진행불가.
			}
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
	
	//이미지정보 수정화면을 팝업한다.
	List<HashMap<String, String>> rowItems;
	DialogListAdapter listAdapter;
	AlertDialog subMenu;
	View convertView;
	public void openDial() {
        LayoutInflater mInflater = (LayoutInflater)this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);  
		convertView = mInflater.inflate(R.layout.dialog_01, null);
		
        if (subMenu != null) {
        	subMenu.dismiss();
        }
        
        ListView list = (ListView)convertView.findViewById(R.id.list);
        listAdapter = new DialogListAdapter(this, rowItems, R.layout.diallog_listview);
        list.setAdapter(listAdapter);
        
        list.setOnItemClickListener(mOnItemClickListener);
        
        LinearLayout dial_linearlayout = (LinearLayout)convertView.findViewById(R.id.dial_linearlayout);
        dial_linearlayout.setVisibility(View.GONE);
        
        subMenu = new AlertDialog.Builder(this)
        .setTitle("복수 회원사로 다음에서 선택하세요.")
        .setNeutralButton("선택", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which){
				int selPosition = listAdapter.getSelectedPosition();
				selectedLogin(rowItems.get(selPosition));
			}
		})
//        .setNegativeButton( "취소", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//			}
//		})
		.setView(convertView)
        .create();
        
        subMenu.show();
	}
	
	//list view 에서 item을 선택하였을때.
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override    
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//goLogin(position);
			selectedLogin(rowItems.get(position));
	    }
	};
	
//	public void goLogin(int position) {
//		selectedLogin(rowItems.get(position));
//	}
    
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

