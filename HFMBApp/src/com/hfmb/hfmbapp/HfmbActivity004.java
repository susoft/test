package com.hfmb.hfmbapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
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

public class HfmbActivity004 extends FragmentActivity {
	
	private static final String TAG = "HfmbActivity004";
	
	private String meeting_cd;
	private String meeting_nm;
	
	private HfmbListAdapter listAdapter;
	private ListView listView;
	private ProgressDialog dialog;
	private View footer;
	
	public List<HashMap<String, String>> rowItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hfmb_004);
		
		dialog = null;
		searchFlag = true;
		lastDataFlag = false;
		
		if (!DataUtil.searchYn) {
			openDialogAlert("조회할 권한이 없습니다.");
			return;
		}
		
		Intent intent = getIntent();
		meeting_cd = intent.getStringExtra("meeting_cd");
		meeting_nm = intent.getStringExtra("meeting_nm");
		
		//콤보박스 세팅.
		setSpinner();
		
		//서치버튼
		ImageView hfmb_srch = (ImageView) findViewById(R.id.hfmb_srch);
		hfmb_srch.setOnClickListener(mOnClickListener);
		hfmb_srch.setOnTouchListener(CommonUtil.imgbtnTouchListener);
		
		//회원사 목록
		listView = (ListView) findViewById(R.id.list);
		
		listAdapter = new HfmbListAdapter(this, new ArrayList<HashMap<String, String>>(), R.layout.hfmbactivity_listview);
		listView.setAdapter(listAdapter);
		
		listView.setOnItemClickListener(mOnItemClickListener);
		listView.setOnScrollListener(mOnScrollListener);
		
		//회원사 삭제는 admin, power 권한만 가능하다. 즉 사무국, 연합회, 교류회 직책을 가지고 있을때만.
		if (DataUtil.insertYn == 1 || DataUtil.insertYn == 2) {
			listView.setOnItemLongClickListener(mOnItemLongClickListener);
		}
		
		//회원사 목록 페이징 처리..
		footer = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.footer_progressbar, null, false);

		// Show the Up button in the action bar.
		setupActionBar();
		
		//자동조회한다.
		searchInfo();
	}
	
	/*
	 * spinner 세팅 하기. - 교류회 리스트.
	 */
	private String[] a13s; 
	private String[] a13cds;
	private SpinnerAdapter a13Adapter;
	public void setSpinner() {
		a13s = DataUtil.a13sArray;
		a13cds = DataUtil.a13cdsArray;
		
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
	
	//콤보박스 선택 이벤트
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

	//서치 버튼 선택시 이벤트.
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override    
		public void onClick(View view) {
			int id = view.getId();
			if (id == R.id.hfmb_srch) {
				lastDataFlag = false;
				searchFlag = true;
				searchInfo();
			}
	    }
	};
	
	
	private boolean lastDataFlag = false;
	private boolean threadFlag = false;
	private boolean searchFlag = false;
	private boolean scrollFlag = false;
	
	private StringBuffer resultInfo;
	
	private int offset = 1;
	private int threadMaxTime = 0;
	
	private Thread threadgo;
	
	public void searchInfo() {
		if (lastDataFlag) return;
		
		if (searchFlag) {
			offset = 0;
			rowItems = null;
			
			dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요 ...", true);
		} else {
			listView.addFooterView(footer);
		}
		
		try {
			lastDataFlag = false;
			threadMaxTime = 0;
			threadgo = new Thread(mRunnable);
			threadgo.start();
		} catch (Exception e) {
			Log.e("error", e.toString());
		}
	}
	
	Runnable mRunnable = new Runnable() {           
        public void run() { 
        	threadFlag = true;
            while (true) {   
                try {
                	searchData();
                	Thread.sleep(1000);
                	if (!threadFlag) {
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
	
	//thread 동작후 1초마다 그림을 가져와 gridview에 추가하는 함수
	Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            
            try {
            	if (listView.getFooterViewsCount() > 0) listView.removeFooterView(footer);
            } catch (ClassCastException e) {
            	Log.e("Tag", e.toString());
            }
            listAdapter.setRowItems(rowItems);
            listAdapter.notifyDataSetChanged();
            
            try {
            	threadgo.join();
            } catch(InterruptedException e) {
            	Log.i("thread", "Thread  stopping...... enterrupted Exception...");
            }
            
            searchFlag = false;
            scrollFlag = false;
        }
    };
	
	//조회한다.
	public void searchData() {
    	//조회조건에 따라서 서버와 통신한다.
    	StringBuffer strbuf = new StringBuffer();
    	StringBuffer urlbuf = new StringBuffer();
    	
    	EditText hfmb_srch_nm = (EditText) findViewById(R.id.hfmb_srch_nm);
		
		String hfmbSrchNm = hfmb_srch_nm.getText().toString();
		String itemcd = a13Adapter.getSelectedItemCd();
		String item = a13Adapter.getSelectedItem();
		
		//Log.i("Search", hfmbSrchNm + "-" + item + "-" + itemcd);
    	
    	if(itemcd == null) itemcd = "";
    	if(item == null) item = "";
    	if(hfmbSrchNm == null) hfmbSrchNm = "";
    	
    	strbuf.append("srch_gubun=" + itemcd);
		strbuf.append("&srch_nm=" + hfmbSrchNm);
		strbuf.append("&offset=" + offset);
		strbuf.append("&meeting_cd=" + meeting_cd);
		
		Log.i("parameter ====== " , strbuf.toString()); 
		
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_SearchTest.jsp");//jwy_Hfmb_004.jsp");
    	
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		
		resultInfo = server.sendByHttp(strbuf, urlbuf.toString());
		
		Log.i("json:", "End........" + resultInfo);
		
		HashMap<String, String> resultMap = server.jsonParserList(resultInfo.toString(), DataUtil.jsonNameResult, "Result");
		
		if (resultMap != null) {
			if (rowItems == null) {
				rowItems = new ArrayList<HashMap<String, String>>();
			}
			if (resultMap.get(DataUtil.jsonNameResult[2]).equals("0")) {
				lastDataFlag = true;
			} else {
				List<HashMap<String, String>> rowItems_temp;
				rowItems_temp = server.jsonParserList(resultInfo.toString(), DataUtil.jsonName);
				
				if (rowItems_temp != null) {
					rowItems.addAll(rowItems_temp);
				}
				offset += DataUtil.offsetAdd;
			}
		}
		threadFlag = false;
    }
	
	//list view 의 스크롤 처리.
	private OnScrollListener mOnScrollListener = new OnScrollListener() {
		@Override
		public void onScroll(AbsListView lw, final int firstVisibleItem,
		                 final int visibleItemCount, final int totalItemCount) {

			final int lastItem = firstVisibleItem + visibleItemCount;
			if (lastItem == totalItemCount) {
				if (!scrollFlag && !searchFlag) {
					//lastDataFlag = false;
					scrollFlag = true;
					searchInfo();
				}
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			// TODO Auto-generated method stub
		}
	};
	
	//list view 에서 item을 길게 선택하였을때.
	private OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {
		//listview의 item 선택시.
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
	
	//list view 에서 item을 선택하였을때.
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override    
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			modifyInfoActivity(position);
	    }
	};
	
	//수정화면으로 이동.
	public void modifyInfoActivity(int position) {
		String tempPhone = rowItems.get(position).get("phone2");//핸드폰 번호
		tempPhone = tempPhone.replaceAll("-", "");

		if (DataUtil.insertYn == 3) {
			if (!tempPhone.equals(DataUtil.phoneNum)) {
				openDialogAlert("본인외 수정할 권한이 없습니다.");
				return;
			}
		} else if (DataUtil.insertYn == 2) {
			if (!rowItems.get(position).get("meeting_cd").equals(DataUtil.meetingCd)) {
				openDialogAlert("소속 교류회외 수정할 권한이 없습니다.");
				return;
			}
		}
		
		Intent intent = new Intent(getApplicationContext(), HfmbActivity007.class);
		intent.putExtra("position", position);
		intent.putExtra("company_cd", rowItems.get(position).get("company_cd"));
		startActivityForResult(intent, 0);
	}
	
	//수정완료 후 처리
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		CommonUtil.showMessage(TAG, resultCode+"");
		switch (resultCode) {
		case -1:
			openDialogAlert("수정 요청 완료 하였습니다. 인증 요청 하시기 바랍니다.");
			
	   		/*//저장후 정보세팅하는 부분...
			int _position = data.getExtras().getInt("position");
			//String _company_cd = data.getExtras().getString("_company_cd");
	   		rowItems.get(_position).put("company_nm", data.getExtras().getString("company_nm"));
	   		rowItems.get(_position).put("ceo_nm", data.getExtras().getString("ceo_nm"));
	   		rowItems.get(_position).put("category_business_nm", data.getExtras().getString("category_business_nm"));
	   		rowItems.get(_position).put("addr", data.getExtras().getString("addr"));
	   		rowItems.get(_position).put("phone1", data.getExtras().getString("phone1"));
	   		rowItems.get(_position).put("phone2", data.getExtras().getString("phone2"));
		   
	   		listAdapter.setRowItems(rowItems);
            listAdapter.notifyDataSetChanged();*/
            
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
			
			if (meeting_nm == null || meeting_nm.equals("")) {
				ab.setTitle("회원사검색");
			} else {
				ab.setTitle(meeting_nm);
			}
			
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
	
	public void openBar() {
		// ProgressDialog 띄워서 1~100까지 게이지를 채우고 사라질 것.
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.setMessage("접속 시작");
        dialog.setCancelable(false);
        dialog.setMax(3);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
	}
	
	public void openDialogAlert(String title) {
		//확인 다이얼로그
		new AlertDialog.Builder(HfmbActivity004.this)
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
		new AlertDialog.Builder(HfmbActivity004.this)
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
