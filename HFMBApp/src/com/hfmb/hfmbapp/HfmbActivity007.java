package com.hfmb.hfmbapp;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.hfmb.hfmbapp.util.CommonUtil;
import com.hfmb.hfmbapp.util.DataUtil;
import com.hfmb.hfmbapp.util.HttpConnectServer;
import com.hfmb.hfmbapp.util.SpinnerAdapter;

public class HfmbActivity007 extends FragmentActivity {
 
	private ProgressDialog dialog;
	private boolean flag = false;
	private boolean flagCode = false;
	DialogListAdapter listAdapter;
	
	private int _position;
	private String _company_cd;
	private String modify_yn;//����������������(0 ����, 1 ����)
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hfmb_007);
		
		Intent intent = getIntent();
		_position = intent.getExtras().getInt("position", 0);
		_company_cd = intent.getExtras().getString("company_cd");
		
		modify_yn = "0";
		
		if (!flag) {
			dialog = ProgressDialog.show(this, "", "��ø� ��ٷ� �ּ��� ...", true);
			
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
			
			new Thread(new Runnable() {           
	            public void run() { 
	                while (true) {   
	                    try {
	                    	//������ ��ȸ.
	                    	getOrganData();
	                    	
	                    	Thread.sleep(1000);
	                    	if (meetingRowItems != null && flagCode) {
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
		}
		
		flag = true;
		
		//�����߰�...
		ImageView photo = (ImageView)findViewById(R.id.hfmb_007_photo);
		photo.setOnClickListener(mOnClickListener);
		photo.setOnTouchListener(CommonUtil.imgbtnTouchListener);
		
		//����, ��� ��ư.
		Button saveBtn = (Button)findViewById(R.id.hfmb_007_btn01);
		Button cancelBtn = (Button)findViewById(R.id.hfmb_007_btn02);
		
		saveBtn.setOnClickListener(mOnClickListener);
		cancelBtn.setOnClickListener(mOnClickListener);
		
		// Show the Up button in the action bar.
		setupActionBar();
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
		Spinner spin1 = (Spinner)findViewById(R.id.hfmb_007_Spinner_01);
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
		Spinner spin1 = (Spinner)findViewById(R.id.hfmb_007_Spinner_02);
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
		Spinner spin1 = (Spinner)findViewById(R.id.hfmb_007_Spinner_03);
		spin1.setPrompt("����ȸ��å�� �����ϼ���.");
		spin1.setAdapter(a15Adapter);
		spin1.setOnItemSelectedListener(mOnItemSelectedListener);
	}
	
	public OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
		@Override    
		public void onItemSelected(AdapterView<?> parent, View view, int position, long ids) {
			int id = parent.getId();
			if (id == R.id.hfmb_007_Spinner_01) {
				a13Adapter.setSelectedItemCd(a13cds[position]);
				a13Adapter.setSelectedItem(a13s[position]);
			} else if (id == R.id.hfmb_007_Spinner_02) {
				a14Adapter.setSelectedItemCd(a14cds[position]);
				a14Adapter.setSelectedItem(a14s[position]);
			} else if (id == R.id.hfmb_007_Spinner_03) {
				a15Adapter.setSelectedItemCd(a15cds[position]);
				a15Adapter.setSelectedItem(a15s[position]);
			}
			
		}
		@Override    
		public void onNothingSelected(AdapterView<?> parent) {
			//Nothing....
		}
	};
	
	View selectedView;//���õ� button.
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override    
		public void onClick(View view) {
			int id = view.getId();
			if (id == R.id.hfmb_007_photo) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, 1);
			} else if (id == R.id.hfmb_007_btn01) {
				saveInfo();
			} else if (id == R.id.hfmb_007_btn02) {
				finish();
			}
	    }
	};
	
	private List<HashMap<String, String>> meetingRowItems;
	//����ȸ ������ ��������.
	public void getOrganData() {
		//��ȸ���ǿ� ���� ������ ����Ѵ�.
    	StringBuffer strbuf = new StringBuffer();
    	StringBuffer urlbuf = new StringBuffer();
    	
		//Log.i("parameter ====== " , strbuf.toString()); 
		
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_0032.jsp");
    	
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlbuf.toString());
		
		//Log.i("json:", resultInfo.toString());
		
		meetingRowItems = server.jsonParserList(resultInfo.toString(), DataUtil.jsonNameMeeting);
		
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
	
	//ȸ�������� ��ȸ thread handler.
	Handler handler13 = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

    		setSpinner13();

    		getSingleData();
    		
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
            setSpinner14();//����ȸ��å
    		setSpinner15();//����ȸ����
    		flagCode = true;
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
			   
			   Log.e("test", "selfileName = " + selfileName);
			   
			   Bitmap bitmap = CommonUtil.SafeDecodeBitmapFile(selfileName);
			   
			   ImageView photo = (ImageView)findViewById(R.id.hfmb_007_photo);
			   photo.setImageBitmap(bitmap);
			   
			   String path = getApplicationContext().getCacheDir().getPath();
			   
			   //������ ������ �����Ѵ�.
			   CommonUtil.SaveBitmapToFileCache(bitmap, _company_cd + ".jpg", path);
			   
			   selfileName = path + File.separator + _company_cd + ".jpg";
			   
			   c.close();
		   break;

		default:
		   break;
		}
	}
	
	public List<HashMap<String, String>> singleItems;
	//������ȸ
	public void getSingleData() {
		singleItems = null;
		
		//��ȸ���ǿ� ���� ������ ����Ѵ�.
    	StringBuffer strbuf = new StringBuffer();
    	StringBuffer urlbuf = new StringBuffer();
    	
		//Log.i("parameter ====== " , strbuf.toString()); 
		
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_0071.jsp");
    	
    	strbuf.append("srch_gubun=0");
		strbuf.append("&srch_nm=" + _company_cd);
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlbuf.toString());
		
		//Log.i("json:", resultInfo.toString());
		
		//�������� ���� ��������� hashmap ���·� ��ȯ�Ѵ�.
		String[] jsonName = {"id", "meeting_cd", "ceo_nm", "company_cd", "company_nm"
    			, "category_business_cd", "category_business_nm", "addr", "phone1", "phone2"
    			, "phone3", "photo", "email", "meeting_nm", "depth_div_cd"
    			, "hfmb_organ_div_cd", "hfmb_duty_div_cd", "auth_div_cd", "del_yn", "gita1"
    			, "gita2", "gita3", "input_dt", "input_tm", "update_dt"
    			, "update_tm", "modify_yn"};
		
		singleItems = server.jsonParserList(resultInfo.toString(), jsonName);
		
		if (singleItems != null) {
			if (singleItems.size() > 0) {
				HashMap<String, String> sinlgeData = singleItems.get(0);
				
				ImageView hfmb_007_photo = (ImageView) findViewById(R.id.hfmb_007_photo);//����
				
				EditText hfmb_007_edit_01 = (EditText) findViewById(R.id.hfmb_007_edit_01);//ȸ�����
		    	EditText hfmb_007_edit_02 = (EditText) findViewById(R.id.hfmb_007_edit_02);//����
		    	EditText hfmb_007_edit_03 = (EditText) findViewById(R.id.hfmb_007_edit_03);//�ּ�
		    	EditText hfmb_007_edit_04 = (EditText) findViewById(R.id.hfmb_007_edit_04);//��ȭ��ȣ
		    	EditText hfmb_007_edit_05 = (EditText) findViewById(R.id.hfmb_007_edit_05);//�����
		    	EditText hfmb_007_edit_06 = (EditText) findViewById(R.id.hfmb_007_edit_06);//�ѽ�
		    	EditText hfmb_007_edit_07 = (EditText) findViewById(R.id.hfmb_007_edit_07);//�̸���
		    	
		    	EditText hfmb_007_edit_011 = (EditText) findViewById(R.id.hfmb_007_edit_011);//��ǥ��
		    	EditText hfmb_007_edit_012 = (EditText) findViewById(R.id.hfmb_007_edit_012);//��ȸ����
		    	
				String imageUrl = "http://119.200.166.131:8054/JwyWebService/hfmbProWeb/photo/" + _company_cd + ".jpg";
				Bitmap bitmap = server.LoadImage(imageUrl);
				if (bitmap == null) {
					hfmb_007_photo.setImageResource(R.drawable.empty_photo);
				} else {
					hfmb_007_photo.setImageBitmap(bitmap);
				}
				
				//������ ������ �����Ѵ�.
				String path = getApplicationContext().getCacheDir().getPath();
			    CommonUtil.SaveBitmapToFileCache(bitmap, _company_cd + ".jpg", path);
			    selfileName = path + File.separator + _company_cd + ".jpg";
				
		    	hfmb_007_edit_01.setText(sinlgeData.get("company_nm"));
		    	hfmb_007_edit_02.setText(sinlgeData.get("category_business_nm"));
		    	hfmb_007_edit_03.setText(sinlgeData.get("addr"));
		    	hfmb_007_edit_04.setText(sinlgeData.get("phone1"));//��ȭ
		    	hfmb_007_edit_05.setText(sinlgeData.get("phone2"));//�ڵ���
		    	hfmb_007_edit_06.setText(sinlgeData.get("phone3"));//�ѽ�
		    	hfmb_007_edit_07.setText(sinlgeData.get("email"));
		    	
		    	hfmb_007_edit_011.setText(sinlgeData.get("ceo_nm"));
		    	hfmb_007_edit_012.setText(DataUtil.checkNull(sinlgeData.get("gita1")));
		    	
		    	CommonUtil.showMessage("HfmbActivity007", 
		    					sinlgeData.get("meeting_cd") + " ^ " + 
		    					sinlgeData.get("hfmb_duty_div_cd") + " ^ " + 
		    					sinlgeData.get("hfmb_organ_div_cd"));
		    	
		    	//a13Adapter
		    	Spinner spin13 = (Spinner)findViewById(R.id.hfmb_007_Spinner_01);
		    	int  position13 = a13Adapter.getPosition(sinlgeData.get("meeting_cd"));
		    	spin13.setSelection(position13);
		    	
		    	//a14Adapter - ����ȸ��å
		    	Spinner spin14 = (Spinner)findViewById(R.id.hfmb_007_Spinner_02);
		    	int  position14 = a14Adapter.getPosition(sinlgeData.get("hfmb_duty_div_cd"));
		    	spin14.setSelection(position14);
		    	
		    	//a15Adapter - ����ȸ����
		    	Spinner spin15 = (Spinner)findViewById(R.id.hfmb_007_Spinner_03);
		    	int  position15 = a15Adapter.getPosition(sinlgeData.get("hfmb_organ_div_cd"));
		    	spin15.setSelection(position15);
		    	
		    	modify_yn = sinlgeData.get("modify_yn");
		    	
		    	CommonUtil.showMessage("HfmbActivity007", "modify_yn = " + modify_yn);
			}
		}
	}
	
	//�����Ѵ�.
	public void saveInfo() {
		
		if (modify_yn.equals("1")) {
			openDialogAlert("������ �����Ͻ� ������ �ֽ��ϴ�. ���� ���� �� �����Ͻñ� �ٶ��ϴ�.");
			return;
		}
		
    	StringBuffer urlbuf = new StringBuffer();
    	HashMap<String, String> params = new HashMap<String, String>();
    	
    	EditText hfmb_007_edit_01 = (EditText) findViewById(R.id.hfmb_007_edit_01);//ȸ�����
    	EditText hfmb_007_edit_02 = (EditText) findViewById(R.id.hfmb_007_edit_02);//����
    	EditText hfmb_007_edit_03 = (EditText) findViewById(R.id.hfmb_007_edit_03);//�ּ�
    	EditText hfmb_007_edit_04 = (EditText) findViewById(R.id.hfmb_007_edit_04);//��ȭ��ȣ
    	EditText hfmb_007_edit_05 = (EditText) findViewById(R.id.hfmb_007_edit_05);//�����
    	EditText hfmb_007_edit_06 = (EditText) findViewById(R.id.hfmb_007_edit_06);//�ѽ�
    	EditText hfmb_007_edit_07 = (EditText) findViewById(R.id.hfmb_007_edit_07);//�̸���
    	
    	EditText hfmb_007_edit_011 = (EditText) findViewById(R.id.hfmb_007_edit_011);//��ǥ��
    	EditText hfmb_007_edit_012 = (EditText) findViewById(R.id.hfmb_007_edit_012);//��ȸ����
    	
		String company_nm = hfmb_007_edit_01.getText().toString();//ȸ�����
		
		//�� üũ...
    	if(company_nm == null || company_nm.equals("")) {
    		CommonUtil.showMessage(getApplicationContext(), "ȸ������� �Է��ϼ���.");
    		return;
    	}
    	
    	CommonUtil.showMessage("selfileName", "selfileName = " + selfileName);
    	
    	//������ ����.
    	company_nm.replaceAll(" ", "");
    	
    	params.put("company_nm", company_nm);
    	
    	params.put("meeting_cd", a13Adapter.getSelectedItemCd());//hfmb_007_Spinner_01
    	params.put("ceo_nm", hfmb_007_edit_011.getText().toString());
    	params.put("company_cd", _company_cd);//�����ϱ����� Ű...
    	params.put("category_business_cd", "");
    	params.put("category_business_nm", hfmb_007_edit_02.getText().toString());
    	params.put("addr", hfmb_007_edit_03.getText().toString());
    	params.put("phone1", hfmb_007_edit_04.getText().toString());
    	params.put("phone2", hfmb_007_edit_05.getText().toString());
    	params.put("phone3", hfmb_007_edit_06.getText().toString());
    	params.put("photoyn", selfileName);
    	params.put("email", hfmb_007_edit_07.getText().toString());
    	params.put("meeting_nm", a13Adapter.getSelectedItem());//hfmb_007_Spinner_01
    	params.put("depth_div_cd", "2");
    	params.put("hfmb_organ_div_cd", a15Adapter.getSelectedItemCd());//hfmb_007_Spinner_03
    	params.put("hfmb_duty_div_cd", a14Adapter.getSelectedItemCd());//hfmb_007_Spinner_02
    	params.put("auth_div_cd", "03");
    	
    	params.put("del_yn", "N");//N
    	params.put("gita1", hfmb_007_edit_012.getText().toString());
    	params.put("gita2", "");
    	params.put("gita3", "");
    	params.put("input_dt", "");
    	params.put("input_tm", "");
    	params.put("update_dt", "");
    	params.put("update_tm", "");
    	
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_0072.jsp");
    	
    	HttpConnectServer server = new HttpConnectServer();
    	StringBuffer resultInfo = server.HttpFileUpload(urlbuf.toString(), params, selfileName);
    	
    	Log.i("json:result", resultInfo.toString().trim());
    	
    	HashMap<String, String> resultList = server.jsonParserList(resultInfo.toString().trim(), DataUtil.jsonNameResult, "Result");
    	
    	Intent intent = getIntent();
        
    	intent.putExtra("position", _position);
    	intent.putExtra("company_cd", _company_cd);
    	intent.putExtra("company_nm", params.get("company_nm"));
    	intent.putExtra("ceo_nm", params.get("ceo_nm"));
    	intent.putExtra("category_business_nm", params.get("category_business_nm"));
    	intent.putExtra("addr", params.get("addr"));
    	intent.putExtra("phone1", params.get("phone1"));
    	intent.putExtra("phone2", params.get("phone2"));
    	intent.putExtra("selfileName", selfileName);
        intent.putExtra("photoUrl", "http://119.200.166.131:8054/JwyWebService/hfmbProWeb/photo/" + _company_cd +".jpg");
		
        setResult(RESULT_OK, intent);
        
    	if (resultList != null) {
    		if (resultList.get("error").equals("0")) {
    			//����
    			openDialogAlert("���� ��û �Ϸ� �Ͽ����ϴ�. ���� ��û �Ͻñ� �ٶ��ϴ�.");
    			
    			if (selfileName != null && !selfileName.equals("")) {
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
    			if (dialogTemp.isShowing()) dialogTemp.dismiss();
    			this.finish();
    		} else {
    			//����
    			openDialogAlert("���� ��û ���� �Ͽ����ϴ�. �����ڿ��� �����ϼ���.");
    		}
    	} else {
    		openDialogAlert("���� ��û ���� �Ͽ����ϴ�. �����ڿ��� �����ϼ���. null");
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
			ab.setTitle("ȸ�������");
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
	
	public AlertDialog dialogTemp;
	public void openDialogAlert(String title) {
		//Ȯ�� ���̾�α�
		dialogTemp = new AlertDialog.Builder(HfmbActivity007.this)
        .setTitle(title)
		.setPositiveButton( "Ȯ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		})
        .show();
	}
}
