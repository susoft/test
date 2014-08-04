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
			openDialogAlert("��ȸ�� ������ �����ϴ�.");
			return;
		}
		
		Intent intent = getIntent();
		meeting_cd = intent.getStringExtra("meeting_cd");
		meeting_nm = intent.getStringExtra("meeting_nm");
		
		//�޺��ڽ� ����.
		setSpinner();
		
		//��ġ��ư
		ImageView hfmb_srch = (ImageView) findViewById(R.id.hfmb_srch);
		hfmb_srch.setOnClickListener(mOnClickListener);
		hfmb_srch.setOnTouchListener(CommonUtil.imgbtnTouchListener);
		
		//ȸ���� ���
		listView = (ListView) findViewById(R.id.list);
		
		listAdapter = new HfmbListAdapter(this, new ArrayList<HashMap<String, String>>(), R.layout.hfmbactivity_listview);
		listView.setAdapter(listAdapter);
		
		listView.setOnItemClickListener(mOnItemClickListener);
		listView.setOnScrollListener(mOnScrollListener);
		
		//ȸ���� ������ admin, power ���Ѹ� �����ϴ�. �� �繫��, ����ȸ, ����ȸ ��å�� ������ ��������.
		if (DataUtil.insertYn == 1 || DataUtil.insertYn == 2) {
			listView.setOnItemLongClickListener(mOnItemLongClickListener);
		}
		
		//ȸ���� ��� ����¡ ó��..
		footer = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.footer_progressbar, null, false);

		// Show the Up button in the action bar.
		setupActionBar();
		
		//�ڵ���ȸ�Ѵ�.
		searchInfo();
	}
	
	/*
	 * spinner ���� �ϱ�. - ����ȸ ����Ʈ.
	 */
	private String[] a13s; 
	private String[] a13cds;
	private SpinnerAdapter a13Adapter;
	public void setSpinner() {
		a13s = DataUtil.a13sArray;
		a13cds = DataUtil.a13cdsArray;
		
		/** Android-13 , Custom Adapter ���� , ������ String[] �迭 ���*/
		a13Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a13s);
		a13Adapter.setItemCds(a13cds);
		Spinner spin1 = (Spinner)findViewById(R.id.galSpinner);
		spin1.setPrompt("��з��� �����ϼ���.");
		spin1.setAdapter(a13Adapter);
		spin1.setOnItemSelectedListener(mOnItemSelectedListener);
		
		a13Adapter.setSelectedItemCd(a13cds[0]);
		a13Adapter.setSelectedItem(a13s[0]);
	}
	
	//�޺��ڽ� ���� �̺�Ʈ
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

	//��ġ ��ư ���ý� �̺�Ʈ.
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
			
			dialog = ProgressDialog.show(this, "", "��ø� ��ٷ� �ּ��� ...", true);
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
                	//������ 4ȸ �̻��ϋ� stop ó��.
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
	
	//thread ������ 1�ʸ��� �׸��� ������ gridview�� �߰��ϴ� �Լ�
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
	
	//��ȸ�Ѵ�.
	public void searchData() {
    	//��ȸ���ǿ� ���� ������ ����Ѵ�.
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
	
	//list view �� ��ũ�� ó��.
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
	
	//list view ���� item�� ��� �����Ͽ�����.
	private OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {
		//listview�� item ���ý�.
		@Override    
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			goImageInfoActivity(R.layout.hfmbactivity_listview1);
			_menu.clear();
			getMenuInflater().inflate(R.menu.main01, _menu);
			return false;
	    }
	};
	
	//���� ���� ȭ������ �̵�.(���߼��� ����) �Ǵ� �ݴ��..
	public void goImageInfoActivity(int resource) {
		ListView listView = (ListView) findViewById(R.id.list);
		listAdapter = new HfmbListAdapter(this, rowItems, resource);
		listView.setAdapter(listAdapter);
		
		listView.setOnItemClickListener(mOnItemClickListener);
	}
	
	//list view ���� item�� �����Ͽ�����.
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override    
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			modifyInfoActivity(position);
	    }
	};
	
	//����ȭ������ �̵�.
	public void modifyInfoActivity(int position) {
		String tempPhone = rowItems.get(position).get("phone2");//�ڵ��� ��ȣ
		tempPhone = tempPhone.replaceAll("-", "");

		if (DataUtil.insertYn == 3) {
			if (!tempPhone.equals(DataUtil.phoneNum)) {
				openDialogAlert("���ο� ������ ������ �����ϴ�.");
				return;
			}
		} else if (DataUtil.insertYn == 2) {
			if (!rowItems.get(position).get("meeting_cd").equals(DataUtil.meetingCd)) {
				openDialogAlert("�Ҽ� ����ȸ�� ������ ������ �����ϴ�.");
				return;
			}
		}
		
		Intent intent = new Intent(getApplicationContext(), HfmbActivity007.class);
		intent.putExtra("position", position);
		intent.putExtra("company_cd", rowItems.get(position).get("company_cd"));
		startActivityForResult(intent, 0);
	}
	
	//�����Ϸ� �� ó��
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		CommonUtil.showMessage(TAG, resultCode+"");
		switch (resultCode) {
		case -1:
			openDialogAlert("���� ��û �Ϸ� �Ͽ����ϴ�. ���� ��û �Ͻñ� �ٶ��ϴ�.");
			
	   		/*//������ ���������ϴ� �κ�...
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
				ab.setTitle("ȸ����˻�");
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
	
	//������ ȸ���� ������ �����Ѵ�.
	public void delete() {
		List<Integer> selectedCheckBox = listAdapter.getSelectedCheckBox();
		int count = selectedCheckBox.size();
		if (count > 0) {
			if (DataUtil.insertYn != 1) {
				String meetingCdTemp = "";
		    	for (int i = 0; i < count; i++) {
		    		meetingCdTemp = rowItems.get(selectedCheckBox.get(i)).get("meeting_cd");
		    		if (!DataUtil.meetingCd.equals(meetingCdTemp)) {
		    			//CommonUtil.showMessage(getApplicationContext(), "�ڽ��� ���� ����ȸ�� ȸ���縸 ���� �����մϴ�.");
		    			openDialogAlert("�ڽ��� ���� ����ȸ�� ȸ���縸 ���� �����մϴ�.");
		    			return;
		    		}
		    	}
			}
	    	
			//����Ȯ�� ���̾�α�
	    	openDialogDelete("������ ȸ���� ������ �����Ͻðڽ��ϱ�?");
			
		} else {
			openDialogAlert("���õ� ������ �����ϴ�.");
			//CommonUtil.showMessage(getApplicationContext(), "���õ� ������ �����ϴ�.");
		}
	}
	
	public void deleteSelecedData() {
		//��ȸ���ǿ� ���� ������ ����Ѵ�.
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
		// ProgressDialog ����� 1~100���� �������� ä��� ����� ��.
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.setMessage("���� ����");
        dialog.setCancelable(false);
        dialog.setMax(3);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
	}
	
	public void openDialogAlert(String title) {
		//Ȯ�� ���̾�α�
		new AlertDialog.Builder(HfmbActivity004.this)
        .setTitle(title)
		.setPositiveButton( "Ȯ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		})
        .show();
	}
	
	//�����ϱ� ���� ���̾�α�
	public void openDialogDelete(String title) {
		//Ȯ�� ���̾�α�
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
