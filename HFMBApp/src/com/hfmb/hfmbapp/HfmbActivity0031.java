package com.hfmb.hfmbapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.hfmb.hfmbapp.util.CommonUtil;
import com.hfmb.hfmbapp.util.DataUtil;
import com.hfmb.hfmbapp.util.HttpConnectServer;
import com.hfmb.hfmbapp.util.SpinnerAdapter;

//사용하지 않는 클래스임.... -- 삭제해야함....
public class HfmbActivity0031 extends FragmentActivity {
	
	private static final String TAG = "HfmbActivity0031";
	
	private ProgressDialog dialog;
	private String meeting_cd;
	private String meeting_nm;
	private HfmbListAdapter listAdapter;
	private List<HashMap<String, String>> rowItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hfmb_004);
		
		if (!DataUtil.searchYn) {
			openDialogAlert("조회할 권한이 없습니다.");
			CommonUtil.showMessage(getApplicationContext(), "조회할 권한이 없습니다.");
			return;
		}
		
		Intent intent = getIntent();
		meeting_cd = intent.getStringExtra("meeting_cd");
		meeting_nm = intent.getStringExtra("meeting_nm");
		
		setSpinner();
		
		ImageView hfmb_srch = (ImageView) findViewById(R.id.hfmb_srch);
		hfmb_srch.setOnClickListener(mOnClickListener);
		hfmb_srch.setOnTouchListener(CommonUtil.imgbtnTouchListener);
		
		rowItems = new ArrayList<HashMap<String, String>>();
		
		ListView listView = (ListView) findViewById(R.id.list);
		listAdapter = new HfmbListAdapter(this, rowItems, R.layout.hfmbactivity_listview);
		listView.setAdapter(listAdapter);
		
		listView.setOnItemClickListener(mOnItemClickListener);
		//회원사 삭제는 admin, power 권한만 가능하다. 즉 사무국, 연합회, 교류회 직책을 가지고 있을때만.
		if (DataUtil.insertYn == 1 || DataUtil.insertYn == 2) {
			listView.setOnItemLongClickListener(mOnItemLongClickListener);
		}
		
		dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요 ...", true);
		
		rowItems = null;
		threadgo = new Thread(mRunnable);
		threadgo.start();
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	Thread threadgo;
	Runnable mRunnable = new Runnable() {           
        public void run() { 
            while (true) {   
                try {
                	//데이터 조회.
                	searchInfo();
                	
                	Thread.sleep(1000);
                	if (rowItems != null) {
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
            
            //CommonUtil.bitmap = new Bitmap[rowItems.size()];
            
    		listAdapter.setRowItems(rowItems);
    		listAdapter.notifyDataSetChanged();
    		
    		dialog.dismiss();
    		
    		try {
            	threadgo.join();
            } catch(InterruptedException e) {
            	Log.i("thread", "Thread  stopping...... enterrupted Exception...");
            }
        }
    };
	
	/*
	 * spinner 세팅 하기.
	 */
	private String[] a13s; 
	private String[] a13cds;
	private SpinnerAdapter a13Adapter;
	public void setSpinner() {
		a13s = new String[4];
		a13cds = new String[4];
		
		a13s[0] = "선택";
		a13cds[0] = "0";
		a13s[1] = "회원사명";
		a13cds[1] = "1";
		a13s[2] = "대표명";
		a13cds[2] = "2";
		a13s[3] = "업종명";
		a13cds[3] = "3";
		
		/** Android-13 , Custom Adapter 참조 , 임의의 String[] 배열 사용*/
		a13Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a13s);
		a13Adapter.setItemCds(a13cds);
		Spinner spin1 = (Spinner)findViewById(R.id.galSpinner);
		spin1.setPrompt("대분류를 선택하세요.");
		spin1.setAdapter(a13Adapter);
		spin1.setOnItemSelectedListener(mOnItemSelectedListener);
		
		a13Adapter.setSelectedItemCd(a13cds[0]);
		a13Adapter.setSelectedItem(a13s[0]);
	}
	
	//spinner의 item을 선택하였을때.
	public OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
		@Override    
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			a13Adapter.setSelectedItemCd(a13cds[position]);
			a13Adapter.setSelectedItem(a13s[position]);
		}
		@Override    
		public void onNothingSelected(AdapterView<?> parent) {
			//Nothing....
		}
	};
	
	//조회버튼 클릭시.
	private OnClickListener mOnClickListener = new OnClickListener() {
		//listview의 item 선택시.
		@Override    
		public void onClick(View view) {
			int id = view.getId();
			if (id == R.id.hfmb_srch) {
				goThread();
			}
	    }
	};
	
	//thread 시작한다.
	public void goThread() {
		dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요 ...", true);
		
		rowItems = null;
		threadgo = new Thread(mRunnable);
		threadgo.start();
	}
	
	//조회한다.
	public void searchInfo() {
		StringBuffer strbuf = new StringBuffer();
    	StringBuffer urlbuf = new StringBuffer();
    	
    	EditText hfmb_srch_nm = (EditText) findViewById(R.id.hfmb_srch_nm);
		
		String hfmbSrchNm = hfmb_srch_nm.getText().toString();
		String itemcd = a13Adapter.getSelectedItemCd();
		String item = a13Adapter.getSelectedItem();
		
		Log.i("Search", hfmbSrchNm + "-" + item + "-" + itemcd);
    	
    	if(itemcd == null) itemcd = "";
    	if(item == null) item = "";
    	if(hfmbSrchNm == null) hfmbSrchNm = "";
    	
    	strbuf.append("srch_gubun=" + itemcd);
		strbuf.append("&srch_nm=" + hfmbSrchNm);
		strbuf.append("&meeting_cd=" + meeting_cd.trim());
		
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_0031.jsp");
    	
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
		
	}
	
	//그리드, 리스트에서 item을 길게선택하였을때.
	private OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {
		@Override    
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			goImageInfoActivity(R.layout.hfmbactivity_listview1);
			_menu.clear();
			getMenuInflater().inflate(R.menu.main01, _menu);
			return false;
	    }
	};
	
	//삭제 가능 화면으로 이동.(다중선택 가능) 또는 반대로..
	public void goImageInfoActivity(int resource) {
		ListView listView = (ListView) findViewById(R.id.list);
		listAdapter = new HfmbListAdapter(this, rowItems, resource);
		listView.setAdapter(listAdapter);
		
		listView.setOnItemClickListener(mOnItemClickListener);
	}
	
	//그리드, 리스트에서 item을 선택하였을때.
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override    
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			modifyInfoActivity(position);
	    }
	};
	
	//수정화면으로 이동.
	public void modifyInfoActivity(int position) {
		String tempPhone = rowItems.get(position).get("phone2");
		tempPhone = tempPhone.replaceAll("-", "");
		
		if (DataUtil.insertYn == 3) {
			if (!tempPhone.equals(DataUtil.phoneNum)) {
				//CommonUtil.showMessage(getApplicationContext(), "수정할 권한이 없습니다.");
				openDialogAlert("본인외 수정할 권한이 없습니다.");
				return;
			}
		} else if (DataUtil.insertYn == 2) {
			if (!rowItems.get(position).get("meeting_cd").equals(DataUtil.meetingCd)) {
				//CommonUtil.showMessage(getApplicationContext(), "수정할 권한이 없습니다.");
				openDialogAlert("소속 교류회외 수정할 권한이 없습니다.");
				return;
			}
		}
		
		Intent intent = new Intent(getApplicationContext(), HfmbActivity007.class);
		intent.putExtra("position", position);
		intent.putExtra("company_cd", rowItems.get(position).get("company_cd"));
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		CommonUtil.showMessage(TAG, resultCode+"");
		switch (resultCode) {
		case -1:
			if (true) return;
			
	   		//저장후 정보세팅하는 부분...
			int _position = data.getExtras().getInt("position");
			//String _company_cd = data.getExtras().getString("_company_cd");
	   		rowItems.get(_position).put("company_nm", data.getExtras().getString("company_nm"));
	   		rowItems.get(_position).put("ceo_nm", data.getExtras().getString("ceo_nm"));
	   		rowItems.get(_position).put("category_business_nm", data.getExtras().getString("category_business_nm"));
	   		rowItems.get(_position).put("addr", data.getExtras().getString("addr"));
	   		rowItems.get(_position).put("phone1", data.getExtras().getString("phone1"));
	   		rowItems.get(_position).put("phone2", data.getExtras().getString("phone2"));
		   
	   		listAdapter.setRowItems(rowItems);
            listAdapter.notifyDataSetChanged();
            
		   break;
		default:
		   break;
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
			ab.setTitle(meeting_nm);
			ab.setSubtitle(DataUtil.ceoNm + DataUtil.temp_01);
		}
	}
	
	private Menu _menu;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		_menu = menu;
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
                Intent upIntent = new Intent(this, HfmbActivity003.class);
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
	    	Intent intent = new Intent( this, HfmbActivity004.class);
			startActivity(intent);
    		this.finish();
	    } else if (item.getItemId() == R.id.delete) {
	    	delete();
	    }
	    
	    return super.onMenuItemSelected(featureId, item);
	}
	
	//선택한 회원사 정보를 삭제한다.
	public void delete() {
		List<Integer> selectedCheckBox = listAdapter.getSelectedCheckBox();
		int count = selectedCheckBox.size();
		if (count > 0) {
			if (DataUtil.insertYn != 1) {
				String meetingCdTemp = "";
		    	for (int i = 0; i < count; i++) {
		    		meetingCdTemp = rowItems.get(selectedCheckBox.get(i)).get("meeting_cd");
		    		if (!DataUtil.meetingCd.equals(meetingCdTemp)) {
		    			//CommonUtil.showMessage(getApplicationContext(), "자신이 속한 교류회의 회원사만 삭제 가능합니다.");
		    			openDialogAlert("자신이 속한 교류회의 회원사만 삭제 가능합니다.");
		    			return;
		    		}
		    	}
			}
	    	
			//삭제확인 다이얼로그
	    	openDialogDelete("선택한 회원사 정보를 삭제하시겠습니까?");
			
		} else {
			openDialogAlert("선택된 정보가 없습니다.");
			//CommonUtil.showMessage(getApplicationContext(), "선택된 정보가 없습니다.");
		}
	}
	
	public void deleteSelecedData() {
		//조회조건에 따라서 서버와 통신한다.
    	StringBuffer strbuf = new StringBuffer();
    	StringBuffer urlbuf = new StringBuffer();
    	
    	strbuf.append("del_id=");
    	
    	List<Integer> selectedCheckBox = listAdapter.getSelectedCheckBox();
    	int count = selectedCheckBox.size();
    	for (int i = 0; i < count; i++) {
    		strbuf.append(rowItems.get(selectedCheckBox.get(i)).get("id") + "#");
    	}
    	
		//Log.i("parameter ====== " , strbuf.toString()); 
		
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_Del.jsp");
    	
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		server.sendByHttp(strbuf, urlbuf.toString());
		
		//Log.i("json:", resultInfo.toString());
		
		searchInfo();
	}
	
	@Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
		if (listAdapter == null) {
			super.onBackPressed();
		} else if (listAdapter.getResource() == R.layout.hfmbactivity_listview) {
			super.onBackPressed();
		} else {
			goImageInfoActivity(R.layout.hfmbactivity_listview);
			_menu.clear();
			getMenuInflater().inflate(R.menu.main, _menu);
		}
    }
	
	public void openDialogAlert(String title) {
		//확인 다이얼로그
		new AlertDialog.Builder(HfmbActivity0031.this)
        .setTitle(title)
		.setPositiveButton( "확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		})
        .show();
	}
	
	//삭제하기 위한 다이얼로그
	public void openDialogDelete(String title) {
		//확인 다이얼로그
		new AlertDialog.Builder(HfmbActivity0031.this)
        .setTitle(title)
		.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		})
		.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				deleteSelecedData();
			}
		})
        .show();
	}
}
