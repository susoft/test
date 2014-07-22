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
		
		if (DataUtil.phoneNum.equals("01063344115")) {
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
    
    //��ȸ�Ѵ�.
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
  		
  		List<HashMap<String, String>> rowItems = server.jsonParserList(resultInfo.toString(), DataUtil.jsonName);
  		
  		if (rowItems != null) {
  			if (rowItems.size() > 0) {
  				rowItem = rowItems.get(0);
  				
				//����ȸ��ϰ��� �����. 1=admin, 2=power, 3=general
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
    		
    		if (rowItemData == null) {
        		openDialogFail(DataUtil.phoneNum + " �α��� ����! �¶��� ���� �Ǵ� ���Կ��θ� Ȯ���ϼ���.");
        	} else {
        		openDialogAlert(DataUtil.ceoNm + "�� �α��� �ϼ̽��ϴ�.");
        	}
        }
    };
    
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
  				//finishApp();
  			}
  		})
          .show();
  	}
  	
  	public void finishApp() {
  		this.finish();
  	}
	
	//�̹��� Ŭ����.
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

