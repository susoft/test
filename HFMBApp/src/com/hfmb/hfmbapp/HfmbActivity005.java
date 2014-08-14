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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hfmb.hfmbapp.util.CommonUtil;
import com.hfmb.hfmbapp.util.DataUtil;
import com.hfmb.hfmbapp.util.HttpConnectServer;

public class HfmbActivity005 extends FragmentActivity {
	
	private ProgressDialog dialog;
	DialogListAdapter listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hfmb_005);

		// Show the Up button in the action bar.
		setupActionBar();
		
		if (DataUtil.insertYn != 1) {
			CommonUtil.showMessage(getApplicationContext(), "등록할 권한이 없습니다.");
			return;
		}
		
		//사진추가...
		ImageView photo = (ImageView)findViewById(R.id.hfmb_005_photo);
		photo.setOnClickListener(mOnClickListener);
		photo.setOnTouchListener(CommonUtil.imgbtnTouchListener);
		
		TextView textView = (TextView) findViewById(R.id.hfmb_005_edit_10);
		textView.setOnClickListener(mOnClickListener);
		
		//회장찾기
		ImageView hfmb_005_srch_02 = (ImageView)findViewById(R.id.hfmb_005_srch_02);
		hfmb_005_srch_02.setOnClickListener(mOnClickListener);
		hfmb_005_srch_02.setOnTouchListener(CommonUtil.imgbtnTouchListener);
		
		//총무찾기
		ImageView hfmb_005_srch_06 = (ImageView)findViewById(R.id.hfmb_005_srch_06);
		hfmb_005_srch_06.setOnClickListener(mOnClickListener);
		hfmb_005_srch_06.setOnTouchListener(CommonUtil.imgbtnTouchListener);
		
		//저장, 취소 버튼.
		Button saveBtn = (Button)findViewById(R.id.hfmb_005_btn01);
		Button cancelBtn = (Button)findViewById(R.id.hfmb_005_btn02);
		
		saveBtn.setOnClickListener(mOnClickListener);
		cancelBtn.setOnClickListener(mOnClickListener);
		
	}
	
	View selectedView;//선택된 button.
	private OnClickListener mOnClickListener = new OnClickListener() {
		//listview의 item 선택시.
		@Override    
		public void onClick(View view) {
			int id = view.getId();
			switch (id) {
			case R.id.hfmb_005_photo:
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
			case R.id.hfmb_005_btn01:
				goThread();
				break;
			case R.id.hfmb_005_btn02:
				finish();
				break;
			case R.id.hfmb_005_srch_02:
				selectedView = view;
				searchInfo(R.id.hfmb_005_srch_02);
				break;
			case R.id.hfmb_005_srch_06:
				selectedView = view;
				searchInfo(R.id.hfmb_005_srch_02);
				break;
			case R.id.hfmb_srch:
				searchData();
				break;
			case R.id.hfmb_005_edit_10:
				datePicker();
				break;
			default:
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
			
			TextView textView = (TextView) findViewById(R.id.hfmb_005_edit_10);
			textView.setText(year + "." + month + "." + day);
			//Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
		}
	};
	
	FileInputStream mFileInputStream;
	String selfileName;
	//사진등록하기.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		//CommonUtil.showMessage(getApplicationContext(), resultCode+"");
		switch (resultCode) {
		   case -1:
			   selfileName = Environment.getExternalStorageDirectory() + "/temp.jpg";
			   
			   Bitmap bitmap = CommonUtil.SafeDecodeBitmapFile(selfileName);
			   
			   ImageView photo = (ImageView)findViewById(R.id.hfmb_005_photo);
			   photo.setImageBitmap(bitmap);
			   
			   String path = getApplicationContext().getCacheDir().getPath();
			   
			   //압축한 파일을 저장한다.
			   CommonUtil.SaveBitmapToFileCache(bitmap, "test.jpg", path);
			   
			   selfileName = path + File.separator + "test.jpg";
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
    private StringBuffer resultInfo;
	//저장한다.
	public void saveInfo() {
    	StringBuffer urlbuf = new StringBuffer();
    	HashMap<String, String> params = new HashMap<String, String>();
    	
    	EditText hfmb_005_edit_01 = (EditText) findViewById(R.id.hfmb_005_edit_01);
    	EditText hfmb_005_edit_10 = (EditText) findViewById(R.id.hfmb_005_edit_10);
    	
		String meeting_nm = hfmb_005_edit_01.getText().toString();//교류회명
		String gita1 = hfmb_005_edit_10.getText().toString();//창립일자
		
    	if(meeting_nm == null || meeting_nm.equals("")) {
    		message = "교류회명을 입력하세요.";
    		return;
    	}
    	
    	meeting_nm.replaceAll(" ", "");
    	
    	params.put("meeting_nm", meeting_nm);
    	params.put("ceo1_id", ceo1_id);
    	params.put("ceo2_id", ceo2_id);
    	params.put("gita1", gita1);
    	
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_0052.jsp");
    	
    	HttpConnectServer server = new HttpConnectServer();
    	resultInfo = server.HttpFileUpload(urlbuf.toString(), params, selfileName);
    	
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
	
	private boolean resultFlag = false;
	
	List<HashMap<String, String>> rowItems;
	//회장, 총무 찾기.
	public void searchInfo(int targetId) {
		openDial(targetId);
    }
	
	//이미지정보 수정화면을 팝업한다.
	AlertDialog subMenu;
	View convertView;
	public void openDial(int targetId) {
        LayoutInflater mInflater = (LayoutInflater)this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);  
		convertView = mInflater.inflate(R.layout.dialog_01, null);
		
        if (subMenu != null) {
        	subMenu.dismiss();
        }
        rowItems = new ArrayList<HashMap<String, String>>() ;
        
        ListView list = (ListView)convertView.findViewById(R.id.list);
        listAdapter = new DialogListAdapter(this, rowItems, R.layout.diallog_listview);
        list.setAdapter(listAdapter);
        
        searchData();//조회하기.
        
        //팝업에서 조회버튼 선택시.
        ImageView hfmb_srch = (ImageView)convertView.findViewById(R.id.hfmb_srch);
        hfmb_srch.setOnClickListener(mOnClickListener);
        hfmb_srch.setOnTouchListener(CommonUtil.imgbtnTouchListener);
		
        subMenu = new AlertDialog.Builder(this)
        .setTitle("회원사 정보")
        .setNeutralButton("선택", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which){
				CommonUtil.showMessage("dialog", "--" + which);
				selectInfo(which);
			}
		})
        .setNegativeButton( "취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		})
		.setView(convertView)
        .create();
        
        subMenu.show();
	}
	
	//조회한다.
	public void searchData() {
    	//조회조건에 따라서 서버와 통신한다.
    	StringBuffer strbuf = new StringBuffer();
    	StringBuffer urlbuf = new StringBuffer();
    	
    	EditText hfmb_srch_nm = (EditText) convertView.findViewById(R.id.hfmb_srch_nm);
		String hfmbSrchNm = hfmb_srch_nm.getText().toString();
		
		//Log.i("Search", hfmbSrchNm);
    	
    	if(hfmbSrchNm == null) hfmbSrchNm = "";
    	
    	strbuf.append("srch_gubun=0");
		strbuf.append("&srch_nm=" + hfmbSrchNm);
		
		Log.i("parameter ====== " , strbuf.toString()); 
		
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_0051.jsp");
    	
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlbuf.toString());
		
		//Log.i("json:", resultInfo.toString());
		
		//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
		String[] jsonName = {"id", "meeting_cd", "ceo_nm", "company_cd", "company_nm"
				, "category_business_cd", "category_business_nm", "addr", "phone1", "phone2"
				, "phone3", "photo", "email", "meeting_nm", "depth_div_cd"
				, "hfmb_organ_div_cd", "hfmb_duty_div_cd", "auth_div_cd", "del_yn", "gita1"
				, "gita2", "gita3", "input_dt", "input_tm", "update_dt"
				, "update_tm"};	
		
		rowItems = server.jsonParserList(resultInfo.toString(), jsonName);
		
		if (rowItems != null) {
			listAdapter.setRowItems(rowItems);
			listAdapter.notifyDataSetChanged();
		} else {
			rowItems = new ArrayList<HashMap<String, String>>() ;
		}
    }
	
	String ceo1_id;
	String ceo2_id;
	HashMap<String,String> ceo1_itemMap;//회장
	HashMap<String,String> ceo2_itemMap;//총무
	//선택하였을때.
	public void selectInfo(int which) {
		
		int selPosition = listAdapter.getSelectedPosition();
//		CommonUtil.showMessage("selPosition", "--" + selPosition);
		if (selPosition < 0) {
			CommonUtil.showMessage(getBaseContext(), "선택해 주세요.");
			return;
		}
		
		int id = selectedView.getId();
		if (id == R.id.hfmb_005_srch_02) {
			if (rowItems.size() < 1) {
				CommonUtil.showMessage(getBaseContext(), "선택해 주세요.");
				return;
			}
			ceo1_itemMap = rowItems.get(selPosition);
			ceo1_id = ceo1_itemMap.get("id");
			TextView hfmb_005_edit_02 = (TextView)findViewById(R.id.hfmb_005_edit_02);
			TextView hfmb_005_edit_03 = (TextView)findViewById(R.id.hfmb_005_edit_03);
			TextView hfmb_005_edit_04 = (TextView)findViewById(R.id.hfmb_005_edit_04);
			TextView hfmb_005_edit_05 = (TextView)findViewById(R.id.hfmb_005_edit_05);
			hfmb_005_edit_02.setText(ceo1_itemMap.get("ceo_nm"));
			hfmb_005_edit_03.setText(ceo1_itemMap.get("company_nm"));
			hfmb_005_edit_04.setText(ceo1_itemMap.get("phone1"));
			hfmb_005_edit_05.setText(ceo1_itemMap.get("phone2"));
		} else if (id == R.id.hfmb_005_srch_06) {
			if (rowItems.size() < 1) {
				CommonUtil.showMessage(getBaseContext(), "선택해 주세요.");
				return;
			}
			ceo2_itemMap = rowItems.get(selPosition);
			ceo2_id = ceo2_itemMap.get("id");
			TextView hfmb_005_edit_06 = (TextView)findViewById(R.id.hfmb_005_edit_06);
			TextView hfmb_005_edit_07 = (TextView)findViewById(R.id.hfmb_005_edit_07);
			TextView hfmb_005_edit_08 = (TextView)findViewById(R.id.hfmb_005_edit_08);
			TextView hfmb_005_edit_09 = (TextView)findViewById(R.id.hfmb_005_edit_09);
			hfmb_005_edit_06.setText(ceo2_itemMap.get("ceo_nm"));
			hfmb_005_edit_07.setText(ceo2_itemMap.get("company_nm"));
			hfmb_005_edit_08.setText(ceo2_itemMap.get("phone1"));
			hfmb_005_edit_09.setText(ceo2_itemMap.get("phone2"));
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
			ab.setTitle("교류회등록");
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
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
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
	
	// AsyncTask클래스는 항상 Subclassing 해서 사용 해야 함.
    // 사용 자료형은
    // background 작업에 사용할 data의 자료형: String 형
    // background 작업 진행 표시를 위해 사용할 인자: Integer형
    // background 작업의 결과를 표현할 자료형: Long
    // 인자를 사용하지 않은 경우 Void Type 으로 지정.
    public class MyAsyncTask extends AsyncTask< String//excute()실행시 넘겨줄 데이터타입
     , String//진행정보 데이터 타입 publishProgress(), onProgressUpdate()의 인수 
     , String//doInBackground() 종료시 리턴될 데이터 타입 onPostExecute()의 인수
    > {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
         
        @Override
        protected String doInBackground(String... params) {
             
            String sum = "";
             
            if(params != null){
                for(String s : params){ 
                    sum += s;
                }
            }
            return sum;
        }
         
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
             
            if(result != null){
                //Log.d("ASYNC", "result = " + result);
            }
             
        }
         
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
         
    }
    
    //교류회 생성 성공시.
    public void openDialogAlert(String title) {
		//확인 다이얼로그
		new AlertDialog.Builder(HfmbActivity005.this)
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
