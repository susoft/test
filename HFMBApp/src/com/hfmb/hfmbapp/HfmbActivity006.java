package com.hfmb.hfmbapp;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hfmb.hfmbapp.util.CommonUtil;
import com.hfmb.hfmbapp.util.DataUtil;
import com.hfmb.hfmbapp.util.HttpConnectServer;
import com.hfmb.hfmbapp.util.SpinnerAdapter;

public class HfmbActivity006 extends FragmentActivity {

	private ProgressDialog dialog;
	private boolean flag = false;
	DialogListAdapter listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hfmb_006);

		// Show the Up button in the action bar.
		setupActionBar();
		
		if (!flag) {
			dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요 ...", true);
			
			//로그인 정보가 사무국직원일떄만 교류회 전체 리스트를 조회한다.
			if (DataUtil.insertYn == 1) {
				new Thread(new Runnable() {           
		            public void run() { 
		                while (true) {   
		                    try {
		                    	//데이터 조회.
		                    	getOrganData();
		                    	
		                    	Thread.sleep(1000);
		                    	if (meetingRowItems != null) {
			                    	Message msg = handler13.obtainMessage();
			                    	handler13.sendMessage(msg);
			                        break;
		                    	}
		                        
		                    } catch (InterruptedException ie) {
		                        ie.printStackTrace();
		                    }
		                }
		            }
		        }).start();
			} else {
				a13s = new String[2];
				a13cds = new String[2];
				
				a13s[0] = "선택";
				a13cds[0] = "";
				
				a13s[1] = DataUtil.meetingNm;
				a13cds[1] = DataUtil.meetingCd;
				
				setSpinner13();
				
				//a13Adapter
		    	Spinner spin13 = (Spinner)findViewById(R.id.hfmb_006_Spinner_01);
		    	int  position13 = a13Adapter.getPosition(DataUtil.meetingCd);
		    	spin13.setSelection(position13);
			}
			
			new Thread(new Runnable() {           
	            public void run() { 
	                while (true) {   
	                    try {
	                    	//데이터 조회.
	                    	getCodeData();//코드데이터 가져오기.
	                    	
	                    	Thread.sleep(1000);
	                    	if (codeItems != null) {
		                    	Message msg = handlerCode.obtainMessage();
		                    	handlerCode.sendMessage(msg);
		                        break;
	                    	}
	                        
	                    } catch (InterruptedException ie) {
	                        ie.printStackTrace();
	                    }
	                }
	            }
	        }).start();
		}
		
		flag = true;
		
		//사진추가...
		ImageView photo = (ImageView)findViewById(R.id.hfmb_006_photo);
		photo.setOnClickListener(mOnClickListener);
		photo.setOnTouchListener(CommonUtil.imgbtnTouchListener);
		
		EditText hfmb_006_edit_012 = (EditText) findViewById(R.id.hfmb_006_edit_012);//입회일자(gita1)
		hfmb_006_edit_012.setOnClickListener(mOnClickListener);
		
		//저장, 취소 버튼.
		Button saveBtn = (Button)findViewById(R.id.hfmb_006_btn01);
		Button cancelBtn = (Button)findViewById(R.id.hfmb_006_btn02);
		
		saveBtn.setOnClickListener(mOnClickListener);
		cancelBtn.setOnClickListener(mOnClickListener);
		
		ImageView imagePhone = (ImageView) findViewById(R.id.hfmb_006_phone);
		imagePhone.setOnClickListener(mOnClickListener);
		imagePhone.setOnTouchListener(CommonUtil.imgbtnTouchListener);
	}
	
	/*
	 * 교류회 spinner 세팅 하기.
	 */
	private String[] a13s; 
	private String[] a13cds;
	private SpinnerAdapter a13Adapter;
	public void setSpinner13() {
		/** Android-13 , Custom Adapter 참조 , 임의의 String[] 배열 사용*/
		a13Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a13s);
		a13Adapter.setItemCds(a13cds);
		Spinner spin1 = (Spinner)findViewById(R.id.hfmb_006_Spinner_01);
		spin1.setPrompt("교류회를 선택하세요.");
		spin1.setAdapter(a13Adapter);
		spin1.setOnItemSelectedListener(mOnItemSelectedListener);
	}
	
	/*
	 * 교류회직책 spinner 세팅 하기.
	 */
	private String[] a14s; 
	private String[] a14cds;
	private SpinnerAdapter a14Adapter;
	public void setSpinner14() {
		
		getCodeData("J002");
		
		/** Android-13 , Custom Adapter 참조 , 임의의 String[] 배열 사용*/
		a14Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a14s);
		a14Adapter.setItemCds(a14cds);
		Spinner spin1 = (Spinner)findViewById(R.id.hfmb_006_Spinner_02);
		spin1.setPrompt("교류회직책을 선택하세요.");
		spin1.setAdapter(a14Adapter);
		spin1.setOnItemSelectedListener(mOnItemSelectedListener);
	}
	
	/*
	 * 연합회직책 spinner 세팅 하기.
	 */
	private String[] a15s; 
	private String[] a15cds;
	private SpinnerAdapter a15Adapter;
	public void setSpinner15() {
		
		getCodeData("J001");
		
		/** Android-13 , Custom Adapter 참조 , 임의의 String[] 배열 사용*/
		a15Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a15s);
		a15Adapter.setItemCds(a15cds);
		Spinner spin1 = (Spinner)findViewById(R.id.hfmb_006_Spinner_03);
		spin1.setPrompt("연합회직책을 선택하세요.");
		spin1.setAdapter(a15Adapter);
		spin1.setOnItemSelectedListener(mOnItemSelectedListener);
	}
	
	public OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
		@Override    
		public void onItemSelected(AdapterView<?> parent, View view, int position, long ids) {
			int id = parent.getId();
			if (id == R.id.hfmb_006_Spinner_01) {
				a13Adapter.setSelectedItemCd(a13cds[position]);
				a13Adapter.setSelectedItem(a13s[position]);
			} else if (id == R.id.hfmb_006_Spinner_02) {
				a14Adapter.setSelectedItemCd(a14cds[position]);
				a14Adapter.setSelectedItem(a14s[position]);
			} else if (id == R.id.hfmb_006_Spinner_03) {
				a15Adapter.setSelectedItemCd(a15cds[position]);
				a15Adapter.setSelectedItem(a15s[position]);
			}
			
		}
		@Override    
		public void onNothingSelected(AdapterView<?> parent) {
			//Nothing....
		}
	};
	
	View selectedView;//선택된 button.
	private OnClickListener mOnClickListener = new OnClickListener() {
		//listview의 item 선택시.
		@Override    
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.hfmb_006_photo :
				//갤러리를 띄운다.
		        Intent intent = new Intent(
		                Intent.ACTION_GET_CONTENT,      // 또는 ACTION_PICK
		                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		        intent.setType("image/*");              // 모든 이미지
		        intent.putExtra("crop", "true");        // Crop기능 활성화
		        intent.putExtra(MediaStore.EXTRA_OUTPUT, CommonUtil.getTempUri());     // 임시파일 생성
		        intent.putExtra("outputFormat",         // 포맷방식
		                Bitmap.CompressFormat.JPEG.toString());

		        startActivityForResult(intent, 1);
				break;
			case R.id.hfmb_006_btn01 :
				goThread();
				break;
			case R.id.hfmb_006_btn02 :
				finish();
				break;
			case R.id.hfmb_006_phone :
				Intent intent1 = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
				intent1.setType(Phone.CONTENT_TYPE);
				startActivityForResult(intent1, 0);
				break;
			case R.id.hfmb_006_edit_012 :
				datePicker();
				break;
			}
			
	    }
	};
	
	public void datePicker() {
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyyMMdd", Locale.KOREA );
		String timeStamp = formatter.format(new Date());
		int yy = Integer.parseInt(timeStamp.substring(0, 4));
		int mm = Integer.parseInt(timeStamp.substring(4, 6))-1;
		int dd = Integer.parseInt(timeStamp.substring(6));
		
		DatePickerDialog dialog = new DatePickerDialog(this, listener, yy, mm, dd);
		dialog.show();
	}
	private OnDateSetListener listener = new OnDateSetListener() {		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			monthOfYear++;
			
			String month = "";
			if (monthOfYear < 10) month = "0" + monthOfYear;
			else month = "" + monthOfYear;
			
			String day = "";
			if (dayOfMonth < 10) day = "0" + dayOfMonth;
			else day = "" + dayOfMonth;
			
			TextView textView = (TextView) findViewById(R.id.hfmb_006_edit_012);//입회일자(gita1)
			textView.setText(year + "." + month + "." + day);
			//Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
		}
	};
	
	private List<HashMap<String, String>> meetingRowItems;
	//교류회 데이터 가져오기.
	public void getOrganData() {
		//meetingRowItems = new ArrayList<HashMap<String, String>>();
		
		//조회조건에 따라서 서버와 통신한다.
    	StringBuffer strbuf = new StringBuffer();
    	StringBuffer urlbuf = new StringBuffer();
    	
		//Log.i("parameter ====== " , strbuf.toString()); 
		
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_0032.jsp");
    	
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlbuf.toString());
		
		//Log.i("json:", resultInfo.toString());
		
		//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
		String[] jsonName = {"meeting_cd", "meeting_nm", "ceo_nm1", "ceo_nm2", "company_count"};
		
		meetingRowItems = server.jsonParserList(resultInfo.toString(), jsonName);
		
		int count = meetingRowItems.size();
		
		a13s = new String[count+1];
		a13cds = new String[count+1];
		a13s[0] = "선택";
		a13cds[0] = "";
		for(int i = 0; i < count; i++) {
			a13s[i+1] = meetingRowItems.get(i).get("meeting_nm");
			a13cds[i+1] = meetingRowItems.get(i).get("meeting_cd");
		}
	}
	//thread 동작후 1초마다 그림을 가져와 gridview에 추가하는 함수
	Handler handler13 = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

    		setSpinner13();
    		
            //dialog.dismiss();
        }
    };
	
	private List<HashMap<String, String>> codeItems;
	//Code 데이터 가져오기.
	public void getCodeData() {
		//codeItems = new ArrayList<HashMap<String, String>>();
		
		//조회조건에 따라서 서버와 통신한다.
    	StringBuffer strbuf = new StringBuffer();
    	StringBuffer urlbuf = new StringBuffer();
    	
		//Log.i("parameter ====== " , strbuf.toString()); 
		
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_Code.jsp");
    	
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlbuf.toString());
		
		//Log.i("json:", resultInfo.toString());
		
		//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
		String[] jsonName = {"code_Key", "code", "code_Nm", "code_Long_Nm", "depth", "order_sort"};
		
		codeItems = server.jsonParserList(resultInfo.toString(), jsonName);
		
	}
	
	//thread 동작후 1초마다 그림을 가져와 gridview에 추가하는 함수
	Handler handlerCode = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setSpinner14();
    		setSpinner15();
    		
    		dialog.dismiss();
        }
    };
	
	//교류회 데이터 가져오기.
	public void getCodeData(String code) {
		List<String> list = new ArrayList<String>();
		List<String> listCd = new ArrayList<String>();
		
		int count = codeItems.size();
		for(int i = 0; i < count; i++) {
			if (code.equals(codeItems.get(i).get("code_Key"))) {
				list.add(codeItems.get(i).get("code_Nm"));
				listCd.add(codeItems.get(i).get("code"));
			}
		}
		
		if (code.equals("J002")) {
			a14s = new String[list.size()];
			a14s = list.toArray(a14s);
			
			a14cds = new String[listCd.size()];
			a14cds = listCd.toArray(a14cds);
		} else if (code.equals("J001")) {
			a15s = new String[list.size()];
			a15s = list.toArray(a15s);
			
			a15cds = new String[listCd.size()];
			a15cds = listCd.toArray(a15cds);
		}
	}
	
	FileInputStream mFileInputStream;
	String selfileName;
	//사진등록하기.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		CommonUtil.showMessage("Tag", "[" + requestCode + "]-[" + resultCode+"]");
		switch (requestCode) {
		case 1://이미지선택시.
			switch (resultCode) {
			case -1 ://데이터 가져올떄.
				selfileName = Environment.getExternalStorageDirectory() + "/temp.jpg";

				Bitmap bitmap = CommonUtil.SafeDecodeBitmapFile(selfileName);

				ImageView photo = (ImageView)findViewById(R.id.hfmb_006_photo);
				photo.setImageBitmap(bitmap);

				String path = getApplicationContext().getCacheDir().getPath();

				//압축한 파일을 저장한다.
				CommonUtil.SaveBitmapToFileCache(bitmap, "test.jpg", path);

				selfileName = path + File.separator + "test.jpg";

				break;
			}
			break;
		case 0://연락처 선택시.
			switch (resultCode) {
			case -1 ://데이터 가져올떄.
				EditText hfmb_006_edit_04 = (EditText) findViewById(R.id.hfmb_006_edit_04);//회사전화번호
		    	EditText hfmb_006_edit_05 = (EditText) findViewById(R.id.hfmb_006_edit_05);//모바일
		    	EditText hfmb_006_edit_06 = (EditText) findViewById(R.id.hfmb_006_edit_06);//팩스
		    	EditText hfmb_006_edit_07 = (EditText) findViewById(R.id.hfmb_006_edit_07);//이메일
		    	
				//전화정보 가져오기.
				Cursor cursor = getContentResolver().query(data.getData(), 
						 new String[]{Phone.CONTACT_ID, Phone.DISPLAY_NAME}
						, null, null, null);
				cursor.moveToFirst();
				
				String id = cursor.getString(0);
				String name = cursor.getString(1);
				
				cursor.close();
				
				//전화번호 가져오기.
				String number1 = "";
				String number2 = "";
				String number3 = "";
				
				Cursor phoneCursor = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID+"='"+id+"'", null, null);

				Log.d("TAG", "phone count = "+phoneCursor.getCount());
				
				while(phoneCursor.moveToNext()){
					String number = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER));
					String numberType = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.TYPE));
					switch(Integer.parseInt(numberType)){
					case Phone.TYPE_HOME : //number1 = number; break;
					case Phone.TYPE_MOBILE : number2 = number; break;
					case Phone.TYPE_WORK : number1 = number; break;
					case Phone.TYPE_FAX_WORK : number3 = number; break;
					case Phone.TYPE_FAX_HOME :
					case Phone.TYPE_PAGER :
					case Phone.TYPE_OTHER :
					case Phone.TYPE_CALLBACK :
					case Phone.TYPE_CAR :
					case Phone.TYPE_COMPANY_MAIN :
					case Phone.TYPE_ISDN :
					case Phone.TYPE_MAIN :
					case Phone.TYPE_OTHER_FAX :
					case Phone.TYPE_RADIO :
					case Phone.TYPE_TELEX :
					case Phone.TYPE_TTY_TDD :
					case Phone.TYPE_WORK_MOBILE :
					case Phone.TYPE_WORK_PAGER :
					case Phone.TYPE_ASSISTANT :
					case Phone.TYPE_MMS :
					default:
					break;
					}
				}
				
				phoneCursor.close();
				
				//이메일가져오기.
				String email = "";
				
				Cursor emailCursor = getContentResolver().query(Email.CONTENT_URI,
						new String[]{Email.DATA, Email.TYPE}, Email.CONTACT_ID+"='"+id+"'"
						,null, null);

				Log.d("TAG", "email count = "+emailCursor.getCount());
				
				while(emailCursor.moveToNext()) {
					email = emailCursor.getString(emailCursor.getColumnIndex(Email.DATA));
					String emailType = emailCursor.getString(emailCursor.getColumnIndex(Email.TYPE));
					switch(Integer.parseInt(emailType)){
					case 1: //hfmb_006_edit_07.setText("Home: "+email); break;
					case 2: //hfmb_006_edit_07.setText("Work: "+email); break;
					case 3: //hfmb_006_edit_07.setText("Other: "+email); break;
					case 4: //hfmb_006_edit_07.setText("Mobile: "+email); break;
					case 5: //hfmb_006_edit_07.setText("Custom: "+email); break;
					}
				}
				
				emailCursor.close();
				
				hfmb_006_edit_04.setText(CommonUtil.fomattingPhone(number1));
		    	hfmb_006_edit_05.setText(CommonUtil.fomattingPhone(number2));
		    	hfmb_006_edit_06.setText(CommonUtil.fomattingPhone(number3));
		    	hfmb_006_edit_07.setText(email);
		    	
				CommonUtil.showMessage("test", "[" + id + "]-[" + name + "]");
				CommonUtil.showMessage("test", "[" + number1 + "]-[" + number2 + "]-[" + number3 + "]-[" + email + "]");
				break;
			}
			break;
		default:
			break;
		}
	}
	
	public void goThread() {
		resultInfo = null;
		message = null;
		dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요 ...", true);
		threadgo = new Thread(mRunnable);
		threadgo.start();
	}
	
	Thread threadgo;
	Runnable mRunnable = new Runnable() {           
        public void run() { 
            while (true) {   
                try {
                	saveInfo();
                	Thread.sleep(1000);
                	if (message != null) {
                    	Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                        break;
                	}
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    };
	
	//thread 동작후 1초마다 그림을 가져와 gridview에 추가하는 함수
	Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //CommonUtil.showMessage(getApplicationContext(), message);
            
            dialog.dismiss();
            
            try {
            	threadgo.join();
            } catch(InterruptedException e) {
            	Log.i("thread", "Thread  stopping...... enterrupted Exception...");
            }
            
            if (resultInfo != null) {
            	if (selfileName != null) {
		            File file = new File(selfileName);
		            if (file.isFile()) {
		            	if(file.delete()) {
		            		CommonUtil.showMessage("File", "삭제되었습니다.");
		            	} else {
		            		CommonUtil.showMessage("File", "삭제실패되었습니다.");
		            	}
		            } else {
		            	CommonUtil.showMessage("File", "파일이 아닙니다.");
		            }
            	}
            }
            
            openDialogAlert(message);
        }
    };
    
    public String message;
    public StringBuffer resultInfo;
    private boolean resultFlag = false;
    
	//저장한다.
	public void saveInfo() {
    	StringBuffer urlbuf = new StringBuffer();
    	HashMap<String, String> params = new HashMap<String, String>();
    	
    	EditText hfmb_006_edit_01 = (EditText) findViewById(R.id.hfmb_006_edit_01);//회원사명
    	EditText hfmb_006_edit_02 = (EditText) findViewById(R.id.hfmb_006_edit_02);//업종
    	EditText hfmb_006_edit_03 = (EditText) findViewById(R.id.hfmb_006_edit_03);//주소
    	EditText hfmb_006_edit_04 = (EditText) findViewById(R.id.hfmb_006_edit_04);//전화번호
    	EditText hfmb_006_edit_05 = (EditText) findViewById(R.id.hfmb_006_edit_05);//모바일
    	EditText hfmb_006_edit_06 = (EditText) findViewById(R.id.hfmb_006_edit_06);//팩스
    	EditText hfmb_006_edit_07 = (EditText) findViewById(R.id.hfmb_006_edit_07);//이메일
    	
    	EditText hfmb_006_edit_011 = (EditText) findViewById(R.id.hfmb_006_edit_011);//대표명
    	EditText hfmb_006_edit_012 = (EditText) findViewById(R.id.hfmb_006_edit_012);//입회일자(gita1)
    	
		String company_nm = hfmb_006_edit_01.getText().toString();//회원사명
		
		//값 체크...
    	if(company_nm == null || company_nm.equals("")) {
    		message = "회원사명을 입력하세요.";
    		return;
    	}
    	if(hfmb_006_edit_011.getText().toString() == null || hfmb_006_edit_011.getText().toString().equals("")) {
    		message = "대표명을 입력하세요.";
    		return;
    	}
    	if(hfmb_006_edit_05.getText().toString() == null || hfmb_006_edit_05.getText().toString().equals("")) {
    		message = "핸드폰번호를 입력하세요.";
    		return;
    	}
    	
    	//데이터 세팅.
    	company_nm.replaceAll(" ", "");
    	
    	params.put("company_nm", company_nm);
    	
    	params.put("id", "");
    	params.put("meeting_cd", hfmb_006_edit_01.getText().toString());
    	params.put("ceo_nm", hfmb_006_edit_011.getText().toString());
    	params.put("company_cd", "");
    	params.put("category_business_cd", "");
    	params.put("category_business_nm", hfmb_006_edit_02.getText().toString());
    	params.put("addr", hfmb_006_edit_03.getText().toString());
    	params.put("phone1", hfmb_006_edit_04.getText().toString());
    	params.put("phone2", hfmb_006_edit_05.getText().toString());
    	
    	params.put("phone3", hfmb_006_edit_06.getText().toString());
    	params.put("meeting_cd", a13Adapter.getSelectedItemCd());//hfmb_006_Spinner_01
    	//params.put("photo", photo);
    	params.put("email", hfmb_006_edit_07.getText().toString());
    	params.put("meeting_nm", a13Adapter.getSelectedItem());//hfmb_006_Spinner_01
    	params.put("depth_div_cd", "2");
    	params.put("hfmb_organ_div_cd", a15Adapter.getSelectedItemCd());//hfmb_006_Spinner_03
    	params.put("hfmb_duty_div_cd", a14Adapter.getSelectedItemCd());//hfmb_006_Spinner_02
    	params.put("auth_div_cd", hfmb_006_edit_01.getText().toString());
    	
    	params.put("del_yn", "N");//N
    	params.put("gita1", hfmb_006_edit_012.getText().toString());
    	params.put("gita2", "");
    	params.put("gita3", "");
    	params.put("input_dt", "");
    	params.put("input_tm", "");
    	params.put("update_dt", "");
    	params.put("update_tm", "");
    	
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_0062.jsp");
    	
    	HttpConnectServer server = new HttpConnectServer();
    	resultInfo = server.HttpFileUpload(urlbuf.toString(), params, selfileName);
    	//message = "정상처리되었습니다.";
    	
    	Log.i("json:", resultInfo.toString());
		
		HashMap<String, String> results = server.jsonParserList(resultInfo.toString(), DataUtil.jsonNameResult, "Result");
		
		if (results != null) {
			if (results.get("error").equals("0")) {
				message = "정상 처리 되었습니다.";
				resultFlag = true;
			} else {
				message = "비정상 처리 되었습니다.";
				resultFlag = false;
			}
		} else {
			message = "비정상처리되었습니다.";
			resultFlag = false;
		}
		
    }
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(false);
			ab.setTitle("회원사등록");
			ab.setSubtitle(DataUtil.ceoNm + DataUtil.temp_01);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	// This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                	Log.e("back", "test......11111");
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                	Log.e("back", "test......22222");
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
	    
	    if (item.getItemId() == R.id.subMenu_1) {
	    	Intent intent = new Intent( this, HfmbActivity001.class);
			startActivity(intent);
			this.finish();
	    } else if (item.getItemId() == R.id.subMenu_2) {
	    	Intent intent = new Intent( this, HfmbActivity002.class);
			startActivity(intent);
    		this.finish();
	    } else if (item.getItemId() == R.id.subMenu_3) {
	    	Intent intent = new Intent( this, HfmbActivity003.class);
			startActivity(intent);
    		this.finish();
	    } else if (item.getItemId() == R.id.subMenu_4) {
	    	Intent intent = new Intent( this, HfmbActivity005.class);
			startActivity(intent);
    		this.finish();
	    }
	    
	    return super.onMenuItemSelected(featureId, item);
	}
	
    //회원사 생성 성공시.
    public void openDialogAlert(String title) {
		//확인 다이얼로그
		new AlertDialog.Builder(HfmbActivity006.this)
        .setTitle(title)
		.setPositiveButton( "확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (resultFlag) finished();
			}
		})
        .show();
	}
    
    public void finished() {
    	this.finish();
    }
  	
}
