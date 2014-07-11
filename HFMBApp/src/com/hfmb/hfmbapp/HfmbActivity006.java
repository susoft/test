package com.hfmb.hfmbapp;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.hfmb.hfmbapp.util.CommonUtil;
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
		
		if (CommonUtil.insertYn != 1 && CommonUtil.insertYn != 2) {
			CommonUtil.showMessage(getApplicationContext(), "����� ������ �����ϴ�.");
			return;
		}
		
		if (CommonUtil.insertYn == 3) {
			CommonUtil.showMessage(getApplicationContext(), "�̹� ��� �Ǿ� �ֽ��ϴ�. �ű�ȸ���� ����� ����ȸ ȸ��, �ѹ� �� ����ȸ �Ҽ��ڸ� �����մϴ�.");
			return;
		}
		
		if (!flag) {
			dialog = ProgressDialog.show(this, "", "��ø� ��ٷ� �ּ��� ...", true);
			
			new Thread(new Runnable() {           
	            public void run() { 
	                while (true) {   
	                    try {
	                    	//������ ��ȸ.
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
			
			new Thread(new Runnable() {           
	            public void run() { 
	                while (true) {   
	                    try {
	                    	//������ ��ȸ.
	                    	getCodeData();//�ڵ嵥���� ��������.
	                    	
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
		
		//�����߰�...
		ImageView photo = (ImageView)findViewById(R.id.hfmb_006_photo);
		photo.setOnClickListener(mOnClickListener);
		photo.setOnTouchListener(CommonUtil.imgbtnTouchListener);
		
		//����, ��� ��ư.
		Button saveBtn = (Button)findViewById(R.id.hfmb_006_btn01);
		Button cancelBtn = (Button)findViewById(R.id.hfmb_006_btn02);
		
		saveBtn.setOnClickListener(mOnClickListener);
		cancelBtn.setOnClickListener(mOnClickListener);
		
	}
	
	/*
	 * ����ȸ spinner ���� �ϱ�.
	 */
	private String[] a13s; 
	private String[] a13cds;
	private SpinnerAdapter a13Adapter;
	public void setSpinner13() {
		/** Android-13 , Custom Adapter ���� , ������ String[] �迭 ���*/
		a13Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a13s);
		a13Adapter.setItemCds(a13cds);
		Spinner spin1 = (Spinner)findViewById(R.id.hfmb_006_Spinner_01);
		spin1.setPrompt("����ȸ�� �����ϼ���.");
		spin1.setAdapter(a13Adapter);
		spin1.setOnItemSelectedListener(mOnItemSelectedListener);
	}
	
	/*
	 * ����ȸ��å spinner ���� �ϱ�.
	 */
	private String[] a14s; 
	private String[] a14cds;
	private SpinnerAdapter a14Adapter;
	public void setSpinner14() {
		
		getCodeData("J002");
		
		/** Android-13 , Custom Adapter ���� , ������ String[] �迭 ���*/
		a14Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a14s);
		a14Adapter.setItemCds(a14cds);
		Spinner spin1 = (Spinner)findViewById(R.id.hfmb_006_Spinner_02);
		spin1.setPrompt("����ȸ��å�� �����ϼ���.");
		spin1.setAdapter(a14Adapter);
		spin1.setOnItemSelectedListener(mOnItemSelectedListener);
	}
	
	/*
	 * ����ȸ��å spinner ���� �ϱ�.
	 */
	private String[] a15s; 
	private String[] a15cds;
	private SpinnerAdapter a15Adapter;
	public void setSpinner15() {
		
		getCodeData("J001");
		
		/** Android-13 , Custom Adapter ���� , ������ String[] �迭 ���*/
		a15Adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, a15s);
		a15Adapter.setItemCds(a15cds);
		Spinner spin1 = (Spinner)findViewById(R.id.hfmb_006_Spinner_03);
		spin1.setPrompt("����ȸ��å�� �����ϼ���.");
		spin1.setAdapter(a15Adapter);
		spin1.setOnItemSelectedListener(mOnItemSelectedListener);
	}
	
	public OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
		@Override    
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			//parent.getId()
			switch (parent.getId()) {
			case R.id.hfmb_006_Spinner_01://
				a13Adapter.setSelectedItemCd(a13cds[position]);
				a13Adapter.setSelectedItem(a13s[position]);
				
//				CommonUtil.showMessage(getApplicationContext()
//						, a13Adapter.getSelectedItemCd() + "---"
//						+ a13Adapter.getSelectedItem());
				
				break;
			case R.id.hfmb_006_Spinner_02://
				a14Adapter.setSelectedItemCd(a14cds[position]);
				a14Adapter.setSelectedItem(a14s[position]);
				
//				CommonUtil.showMessage(getApplicationContext()
//						, a14Adapter.getSelectedItemCd() + "---"
//						+ a14Adapter.getSelectedItem());
				
				break;
			case R.id.hfmb_006_Spinner_03://
				a15Adapter.setSelectedItemCd(a15cds[position]);
				a15Adapter.setSelectedItem(a15s[position]);
				
//				CommonUtil.showMessage(getApplicationContext()
//						, a15Adapter.getSelectedItemCd() + "---"
//						+ a15Adapter.getSelectedItem());
				
				break;
			}
			
		}
		@Override    
		public void onNothingSelected(AdapterView<?> parent) {
			//Nothing....
		}
	};
	
	View selectedView;//���õ� button.
	private OnClickListener mOnClickListener = new OnClickListener() {
		//listview�� item ���ý�.
		@Override    
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.hfmb_006_photo://�����߰�
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, 1);
				break;
			case R.id.hfmb_006_btn01://����
				goThread();
				break;
			case R.id.hfmb_006_btn02://���
				finish();
				break;
			}
	    }
	};
	
	private List<HashMap<String, String>> meetingRowItems;
	//����ȸ ������ ��������.
	public void getOrganData() {
		//meetingRowItems = new ArrayList<HashMap<String, String>>();
		
		//��ȸ���ǿ� ���� ������ ����Ѵ�.
    	StringBuffer strbuf = new StringBuffer();
    	StringBuffer urlbuf = new StringBuffer();
    	
		//Log.i("parameter ====== " , strbuf.toString()); 
		
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_0032.jsp");
    	
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlbuf.toString());
		
		//Log.i("json:", resultInfo.toString());
		
		//�������� ���� ��������� hashmap ���·� ��ȯ�Ѵ�.
		String[] jsonName = {"meeting_cd", "meeting_nm", "ceo_nm1", "ceo_nm2", "company_count"};
		
		meetingRowItems = server.jsonParserList(resultInfo.toString(), jsonName);
		
		int count = meetingRowItems.size();
		
		a13s = new String[count+1];
		a13cds = new String[count+1];
		a13s[0] = "����";
		a13cds[0] = "";
		for(int i = 0; i < count; i++) {
			a13s[i+1] = meetingRowItems.get(i).get("meeting_nm");
			a13cds[i+1] = meetingRowItems.get(i).get("meeting_cd");
		}
	}
	//thread ������ 1�ʸ��� �׸��� ������ gridview�� �߰��ϴ� �Լ�
	Handler handler13 = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

    		setSpinner13();
    		
            dialog.dismiss();
        }
    };
	
	private List<HashMap<String, String>> codeItems;
	//Code ������ ��������.
	public void getCodeData() {
		//codeItems = new ArrayList<HashMap<String, String>>();
		
		//��ȸ���ǿ� ���� ������ ����Ѵ�.
    	StringBuffer strbuf = new StringBuffer();
    	StringBuffer urlbuf = new StringBuffer();
    	
		//Log.i("parameter ====== " , strbuf.toString()); 
		
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_Code.jsp");
    	
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlbuf.toString());
		
		//Log.i("json:", resultInfo.toString());
		
		//�������� ���� ��������� hashmap ���·� ��ȯ�Ѵ�.
		String[] jsonName = {"code_Key", "code", "code_Nm", "code_Long_Nm", "depth", "order_sort"};
		
		codeItems = server.jsonParserList(resultInfo.toString(), jsonName);
		
	}
	
	//thread ������ 1�ʸ��� �׸��� ������ gridview�� �߰��ϴ� �Լ�
	Handler handlerCode = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setSpinner14();
    		setSpinner15();
            //dialog.dismiss();
        }
    };
	
	//����ȸ ������ ��������.
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
	//��������ϱ�.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		//CommonUtil.showMessage(getApplicationContext(), resultCode+"");
		switch (resultCode) {
		   case -1:
			   Uri selPhoto = data.getData();
			   
			   //�����θ� ȹ���Ѵ�!!! �߿�~
			   Cursor c = getContentResolver().query(Uri.parse(selPhoto.toString()), null,null,null,null);
			   c.moveToNext();
			   selfileName = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
			   
			   Bitmap bitmap = CommonUtil.SafeDecodeBitmapFile(selfileName);
			   
			   ImageView photo = (ImageView)findViewById(R.id.hfmb_006_photo);
			   photo.setImageBitmap(bitmap);
			   
			   //������ ������ �����Ѵ�.
			   CommonUtil.SaveBitmapToFileCache(bitmap, "test.jpg");
			   
			   selfileName = CommonUtil.path + "test.jpg";
			   
			   c.close();
		   break;

		default:
		   break;
		}
	}
	
	public void goThread() {
		resultInfo = null;
		message = null;
		dialog = ProgressDialog.show(this, "", "��ø� ��ٷ� �ּ��� ...", true);
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
	
	//thread ������ 1�ʸ��� �׸��� ������ gridview�� �߰��ϴ� �Լ�
	Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CommonUtil.showMessage(getApplicationContext(), message);
            
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
		            		CommonUtil.showMessage("File", "�����Ǿ����ϴ�.");
		            	} else {
		            		CommonUtil.showMessage("File", "�������еǾ����ϴ�.");
		            	}
		            } else {
		            	CommonUtil.showMessage("File", "������ �ƴմϴ�.");
		            }
            	}
            }
        }
    };
    
    public String message;
    public StringBuffer resultInfo;
	
	//�����Ѵ�.
	public void saveInfo() {
    	StringBuffer urlbuf = new StringBuffer();
    	HashMap<String, String> params = new HashMap<String, String>();
    	
//    	String[] rtnKey = {"id", "meeting_cd", "ceo_nm", "company_cd", "company_nm"
//    			, "category_business_cd", "category_business_nm", "addr", "phone1", "phone2"
//    			, "phone3", "photo", "email", "meeting_nm", "depth_div_cd"
//    			, "hfmb_organ_div_cd", "hfmb_duty_div_cd", "auth_div_cd", "del_yn", "gita1"
//    			, "gita2", "gita3", "input_dt", "input_tm", "update_dt"
//    			, "update_tm"};
    	
    	EditText hfmb_006_edit_01 = (EditText) findViewById(R.id.hfmb_006_edit_01);//ȸ�����
    	EditText hfmb_006_edit_02 = (EditText) findViewById(R.id.hfmb_006_edit_02);//����
    	EditText hfmb_006_edit_03 = (EditText) findViewById(R.id.hfmb_006_edit_03);//�ּ�
    	EditText hfmb_006_edit_04 = (EditText) findViewById(R.id.hfmb_006_edit_04);//��ȭ��ȣ
    	EditText hfmb_006_edit_05 = (EditText) findViewById(R.id.hfmb_006_edit_05);//�����
    	EditText hfmb_006_edit_06 = (EditText) findViewById(R.id.hfmb_006_edit_06);//�ѽ�
    	EditText hfmb_006_edit_07 = (EditText) findViewById(R.id.hfmb_006_edit_07);//�̸���
    	
    	EditText hfmb_006_edit_011 = (EditText) findViewById(R.id.hfmb_006_edit_011);//
    	
		String company_nm = hfmb_006_edit_01.getText().toString();//ȸ�����
		
		//�� üũ...
    	if(company_nm == null || company_nm.equals("")) {
    		message = "ȸ������� �Է��ϼ���.";
    		return;
    	}
    	if(hfmb_006_edit_011.getText().toString() == null || hfmb_006_edit_011.getText().toString().equals("")) {
    		message = "��ǥ���� �Է��ϼ���.";
    		return;
    	}
    	if(hfmb_006_edit_05.getText().toString() == null || hfmb_006_edit_05.getText().toString().equals("")) {
    		message = "�ڵ�����ȣ�� �Է��ϼ���.";
    		return;
    	}
    	
    	//������ ����.
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
    	params.put("gita1", "");
    	params.put("gita2", "");
    	params.put("gita3", "");
    	params.put("input_dt", "");
    	params.put("input_tm", "");
    	params.put("update_dt", "");
    	params.put("update_tm", "");
    	
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_0062.jsp");
    	
    	HttpConnectServer server = new HttpConnectServer();
    	resultInfo = server.HttpFileUpload(urlbuf.toString(), params, selfileName);
    	message = "����ó���Ǿ����ϴ�.";
		//server connecting... login check...
//		HttpConnectServer server = new HttpConnectServer();
//		StringBuffer resultInfo = server.sendByHttpPost(urlbuf.toString(), nameValuePairs);
//		
//		Log.i("json:", resultInfo.toString());
//		
//		//�������� ���� ��������� hashmap ���·� ��ȯ�Ѵ�.
//		String[] jsonName = {"Result"};	
//		
//		List<HashMap<String, String>> results = server.jsonParserList(resultInfo.toString(), jsonName);
//		
//		if (results != null) {
//			if (results.size() > 0) {
//				String result = results.get(0).get(jsonName[0]);
//				CommonUtil.showMessage(getApplicationContext(), result);
//			}
//		}
    }
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(true);
			ab.setTitle("ȸ������");
			ab.setSubtitle(CommonUtil.ceoNm + "�� �α���");
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
}