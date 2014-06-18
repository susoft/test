package com.ntpbm.ntpbmapp;

import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ntpbm.ntpbmapp.app0100.Ntpbm0100Activity;
import com.ntpbm.ntpbmapp.app0300.Ntpbm0300Activity;
import com.ntpbm.ntpbmapp.app0400.Ntpbm0400Activity;

public class Ntpbm0001Activity extends Activity implements OnClickListener {
	
	public String message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0001);
		
		Intent intent = getIntent();
		message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		findViewById(R.id.loginBtn).setOnClickListener(this);
		findViewById(R.id.cancelBtn).setOnClickListener(this);
		
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
		    ab.setSubtitle("Login");
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			DialogProgress();
			
			//화면에서 서버에 넘겨줄 데이터를 가져온다.
			EditText serverip = (EditText)findViewById(R.id.serverVal);
			EditText loginid = (EditText)findViewById(R.id.idVal);
			EditText loginpwd = (EditText)findViewById(R.id.pwdVal);
			
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("serverip=" + serverip.getText());
			strbuf.append("&loginid=" + loginid.getText());
			strbuf.append("&loginpwd=" + loginpwd.getText());
			
			//서버 url 경로를 xml에서 가져온다.
			String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath
					+ getText(R.string.ntpbm_0001).toString();
			
			//server connecting... login check...
			HttpConnectServer server = new HttpConnectServer();
			StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
			
			Log.i("json:", resultInfo.toString());
			
			//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
			String[] jsonName = {"EPLY_CD", "EPLY_NM", "iPSW_NUM", "HP_NUM"
					, "E_MAIL", "SIGN_KOR", "TEL_NUM", "FAX_NUM"
					, "ADDR", "HOMEPAGE", "SIGN_FILENM"};
			HashMap<String, String> parseredData = server.jsonParserList(resultInfo.toString(), jsonName);
			
			String strLoginYn = parseredData.get(jsonName[2]);
			
			//login 여부 판단에 따라 프로세스를 처리한다.
			if (strLoginYn.equals("1")) {
				MainActivity.personInfo = parseredData;
				MainActivity.loginYn = 1;
				this.finish();
				
				Intent intent;
				String EXTRA_MESSAGE = MainActivity.EXTRA_MESSAGE;
				
				//각각의 화면으로 이동한다.
				if (message.equals(MainActivity.ntpbm_0100)) {
					//설치화면
					intent = new Intent(this, Ntpbm0100Activity.class);
				} else if (message.equals(MainActivity.ntpbm_0200)) {
					//장비조회
					intent = new Intent(this, Ntpbm0100Activity.class);
				} else if (message.equals(MainActivity.ntpbm_0300)) {
					//재고조회
					intent = new Intent(this, Ntpbm0300Activity.class);
				} else if (message.equals(MainActivity.ntpbm_0400)) {
					//견적서
					intent = new Intent(this, Ntpbm0400Activity.class);
				} else {
					intent = new Intent(this, Ntpbm0100Activity.class);
				}
				
				intent.putExtra(EXTRA_MESSAGE, message);
				startActivity(intent);
			} else {
				dialog.dismiss();
				//다이얼로그 띄우기...
				DialogSimple();
			}
			break;
		case R.id.cancelBtn:
			MainActivity.loginYn = -1;
			
			this.finish();
			break;
		}
	}
	
	private void DialogSimple() {
	    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	    alt_bld.setPositiveButton("확인", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	dialog.dismiss();     //닫기
	        }
	    });
	    
	    AlertDialog alert = alt_bld.create();
	    alert.setTitle("로그인 실패");
	    alert.setMessage("로그인 아이디/비번이 정확하지 않습니다. 다시 확인하십시요.");
	    alert.setCanceledOnTouchOutside(true);
//	    alert.setIcon(R.drawable.icon);
	    alert.show();
	}
	
	ProgressDialog dialog;
	private void DialogProgress(){
        dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요 ...", true);
        
        // 창을 내린다.
        //dialog.dismiss();
    }
	
//	private void DialogSimple1() {
//	    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
//	    alt_bld.setMessage("로그인 아이디/비번이 정확하지 않습니다. 다시 확인하십시요.").setCancelable(false).setPositiveButton("Yes",
//	        new DialogInterface.OnClickListener() {
//		        public void onClick(DialogInterface dialog, int id) {
//		            // Action for 'Yes' Button
//		        }
//	        }).setNegativeButton("No",
//	        new DialogInterface.OnClickListener() {
//		        public void onClick(DialogInterface dialog, int id) {
//		            // Action for 'NO' Button
//		            dialog.cancel();
//		        }
//	        });
//	    AlertDialog alert = alt_bld.create();
//	    alert.setTitle("로그인 실패");
//	    alert.setCanceledOnTouchOutside(true);
////	    alert.setIcon(R.drawable.icon);
//	    alert.show();
//	}
	
//	private void DialogSelectOption() {
//        final String items[] = { "item1", "item2", "item3" };
//        AlertDialog.Builder ab = new AlertDialog.Builder(this);
//        ab.setTitle("Title");
//        ab.setSingleChoiceItems(items, 0,
//            new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    // 각 리스트를 선택했을때 
//                }
//            }).setPositiveButton("Ok",
//            new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
//                }
//            }).setNegativeButton("Cancel",
//            new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    // Cancel 버튼 클릭시
//                }
//            });
//        ab.show();
//    }
     
//    private void DialogHtmlView(){
//        AlertDialog.Builder ab=new AlertDialog.Builder(this);
//        ab.setMessage(Html.fromHtml("<strong><font color='#ff0000'> " +
//                "Html 표현여부 " +"</font></strong><br>HTML 이 제대로 표현되는지 본다."));
//            ab.setPositiveButton("ok", null);
//            ab.show();
//    }
//     

     
//    private void DialogRadio(){
//        final CharSequence[] PhoneModels = {"iPhone", "Nokia", "Android"};
//        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
//        alt_bld.setIcon(R.drawable.icon);
//        alt_bld.setTitle("Select a Phone Model");
//        alt_bld.setSingleChoiceItems(PhoneModels, -1, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//                Toast.makeText(getApplicationContext(), "Phone Model = "+PhoneModels[item], Toast.LENGTH_SHORT).show();
//                dialog.cancel();
//            }
//        });
//        AlertDialog alert = alt_bld.create();
//        alert.show();
//    }
    
//    private void DialogTimePicker(){
//        TimePickerDialog.OnTimeSetListener mTimeSetListener = 
//            new TimePickerDialog.OnTimeSetListener() {
//                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                    Toast.makeText(this,
//                        "Time is=" + hourOfDay + ":" + minute, Toast.LENGTH_SHORT)
//                        .show();
//                }
//        };
//        TimePickerDialog alert = new TimePickerDialog(this, mTimeSetListener, 0, 0, false);
//        alert.show();
//    }
     
//    private void DialogDatePicker(){
//        Calendar c = Calendar.getInstance();
//        int cyear = c.get(Calendar.YEAR);
//        int cmonth = c.get(Calendar.MONTH);
//        int cday = c.get(Calendar.DAY_OF_MONTH);
//         
//        DatePickerDialog.OnDateSetListener mDateSetListener = 
//            new DatePickerDialog.OnDateSetListener() {
//                // onDateSet method
//                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                    String date_selected = String.valueOf(monthOfYear+1)+
//                        " /"+String.valueOf(dayOfMonth)+" /"+String.valueOf(year);
//                    Toast.makeText(this, 
//                            "Selected Date is ="+date_selected, Toast.LENGTH_SHORT).show();
//                }
//        };
//        DatePickerDialog alert = new DatePickerDialog(this,  mDateSetListener,  cyear, cmonth, cday);
//        alert.show();
//    }
}
