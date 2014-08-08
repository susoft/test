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
		
		setInit();//�ڽ��� ��ȭ��ȣ ��������.
		
		//��ư �׼�.
		ImageView icon1 = (ImageView)findViewById(R.id.icon1);//����ȸ�Ұ�
		ImageView icon2 = (ImageView)findViewById(R.id.icon2);//����ȸ��Ȳ
		ImageView icon4 = (ImageView)findViewById(R.id.icon4);//����ȸ��Ȳ
		ImageView icon5 = (ImageView)findViewById(R.id.icon5);//ȸ����˻�
		ImageView icon6 = (ImageView)findViewById(R.id.icon6);//����ȸ���
		ImageView icon7 = (ImageView)findViewById(R.id.icon7);//ȸ������
		
		icon1.setOnClickListener(mOnClickListener);//����ȸ�Ұ�
		icon2.setOnClickListener(mOnClickListener);//����ȸ��Ȳ
		icon4.setOnClickListener(mOnClickListener);//����ȸ��Ȳ
		icon5.setOnClickListener(mOnClickListener);//ȸ����˻�
		icon6.setOnClickListener(mOnClickListener);//����ȸ���
		icon7.setOnClickListener(mOnClickListener);//ȸ������
		
		icon1.setOnTouchListener(CommonUtil.imgbtnTouchListener);//����ȸ�Ұ�
		icon2.setOnTouchListener(CommonUtil.imgbtnTouchListener);//����ȸ��Ȳ
		icon4.setOnTouchListener(CommonUtil.imgbtnTouchListener);//����ȸ��Ȳ
		icon5.setOnTouchListener(CommonUtil.imgbtnTouchListener);//ȸ����˻�
		icon6.setOnTouchListener(CommonUtil.imgbtnTouchListener);//����ȸ���
		icon7.setOnTouchListener(CommonUtil.imgbtnTouchListener);//ȸ������
		
		if (DataUtil.phoneNum.equals("01063344115") || DataUtil.phoneNum.equals("01026470771")) {
			//Debugging ...
			openDialogDebug();
  		} else {
			startAsyncTask();
  		}
	}
	
	//�ڱ��ڽ��� ��ȭ��ȣ ��������...
	@SuppressWarnings("static-access")
	public void setInit() {
		TelephonyManager telManager = (TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE); 
		String telNum = telManager.getLine1Number();
		
		DataUtil.phoneNum = "0" + telNum.substring(telNum.indexOf("1"));
	}
	
	//��ȭ��ȣ�� �̿��� ����ó�� thread... start...
	private HashMap<String, String> rowItemData = null;
	
	private ProgressDialog dialog;//��ȸó�� ���α׷�����..
	private int threadMaxTime = 0;
  	private Thread threadgo;
  	public void startAsyncTask() {
  		if (!DataUtil.flag) {
			threadMaxTime = 0; 
	  		dialog = ProgressDialog.show(this, "", DataUtil.phoneNum + " ��ø� ��ٷ� �ּ��� ...", true);
			threadgo = new Thread(mRunnable);
			threadgo.start();
  		}
	}
  	
  	// threadgo �� runnable 
	Runnable mRunnable = new Runnable() {           
        public void run() { 
            while (true) {   
                try {
                	//������ ��ȸ.
                	rowItemData = searchData();
                	
                	Thread.sleep(1000);//1�� ������ ����.
                	
                	//������ ��ȸ �Ϸ� ���� üũ
                	if (rowItemData != null) {
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
    
    //�α������� üũ
  	public HashMap<String, String> searchData() {
  		HashMap<String, String> rowItem = null;
  		
      	//��ȸ���ǿ� ���� ������ ����Ѵ�.
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
  			return rowItem;//�����߻�.
  		}
  		
  		rowItems = server.jsonParserList(resultInfo.toString(), DataUtil.jsonName);
  		
  		return rowItem;
  	}
    
	//thread handler.. ��� ó��.
	Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
    		dialog.dismiss();//��ȸó�� ���α׷�����.. �ݱ�
    		
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
        				//����ȸ��ϰ��� �����. 1=admin, 2=power, 3=general
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
    	//����ȸ��ϰ��� �����. 1=admin, 2=power, 3=general
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
    		openDialogFail(DataUtil.phoneNum + " �α��� ����! �¶��� ���� �Ǵ� ���Կ��θ� Ȯ���ϼ���.");
    	} else {
    		openDialogAlert(DataUtil.ceoNm + "�� �α��� �ϼ̽��ϴ�.");
    	}
    }
    
    //�α��� ������.
    public void openDialogAlert(String title) {
		//Ȯ�� ���̾�α�
		new AlertDialog.Builder(MainActivity.this)
        .setTitle(title)
		.setPositiveButton( "Ȯ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				DataUtil.flag = true;
			}
		})
        .show();
	}
  	
  	//�α��� ���н�
  	public void openDialogFail(String title) {
  		//Ȯ�� ���̾�α�
  		new AlertDialog.Builder(MainActivity.this)
          .setTitle(title)
  		.setNegativeButton( "��õ�", new DialogInterface.OnClickListener() {
  			@Override
  			public void onClick(DialogInterface dialog, int which) {
  				// TODO Auto-generated method stub
  				DataUtil.flag = false;
  				startAsyncTask();
  			}
  		})
  		.setPositiveButton( "�ݱ�", new DialogInterface.OnClickListener() {
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
  	
	//�̹��� Ŭ����.
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			boolean inFlag = true;
			String message = "";
			Intent intent = new Intent(getApplicationContext(), HfmbActivity001.class);//����ȸ�Ұ�
			
			switch (view.getId()) {
			case R.id.icon1://����ȸ�Ұ�
				intent = new Intent(getApplicationContext(), HfmbActivity001.class);
				break;
			case R.id.icon2://����ȸ��Ȳ
				intent = new Intent(getApplicationContext(), HfmbActivity002.class);
				break;
			case R.id.icon4://����ȸ��Ȳ
				if (!DataUtil.searchYn) inFlag = false;
				message = "����ȸ��Ȳ�� ȸ���縸 ��ȸ �����մϴ�.";
				intent = new Intent(getApplicationContext(), HfmbActivity003.class);
				break;
			case R.id.icon5://ȸ����˻�
				if (!DataUtil.searchYn) inFlag = false;
				message = "ȸ����˻��� ȸ���縸 ��ȸ �����մϴ�.";
				intent = new Intent(getApplicationContext(), HfmbActivity004.class);
				break;
			case R.id.icon6://����ȸ���
				//����üũ.. -- �繫�������� ����.
				if (DataUtil.insertYn != 1) inFlag = false;
				message = "����ȸ ����� �繫�������� �����մϴ�.";
				intent = new Intent(getApplicationContext(), HfmbActivity005.class);
				break;
			case R.id.icon7://ȸ������
				//����üũ.. -- �繫������, ����ȸ ȸ��, �ѹ� �� ����.
				if (DataUtil.insertYn != 1 && DataUtil.insertYn != 2) inFlag = false;
				message = "ȸ���� ����� �繫�������� ����ȸ ȸ��,�ѹ��� �����մϴ�.";
				intent = new Intent(getApplicationContext(), HfmbActivity006.class);
				break;
			}
			
			if (inFlag) {
				startActivity(intent);
			} else {
				openDialogAlert(message);//����Ұ�.
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
	
	//�̹������� ����ȭ���� �˾��Ѵ�.
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
        .setTitle("���� ȸ����� �������� �����ϼ���.")
        .setNeutralButton("����", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which){
				int selPosition = listAdapter.getSelectedPosition();
				selectedLogin(rowItems.get(selPosition));
			}
		})
//        .setNegativeButton( "���", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//			}
//		})
		.setView(convertView)
        .create();
        
        subMenu.show();
	}
	
	//list view ���� item�� �����Ͽ�����.
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
     * Debugging .... Test �ϱ� ����. (�����ڸ� ����)
     */
    public EditText phonenumEditText;
	public void openDialogDebug() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); //inflater ����
		View viewInDialog = inflater.inflate(R.layout.daillog_login, null); //inflater�� View ��ü�� ���̾ƿ� �ֱ�
		
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
    	
	//�α��� ������ üũ�Ѵ�.//�׽�Ʈ...
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
     * Debugging .... Test �ϱ� ����. (�����ڸ� ����)
     */
}

