package com.ntpbm.ntpbmapp.app0100;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.ntpbm.ntpbmapp.CustomBaseAdapter;
import com.ntpbm.ntpbmapp.HttpConnectServer;
import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;
import com.ntpbm.ntpbmapp.RowItem;

/*
 * ��ġ��ġ�� �Ǽ��縮���� ��ȸ
 */
public class Ntpbm0103Activity extends Activity implements OnItemClickListener, OnItemLongClickListener {
	
	private static String[] titles = new String[] { "�귣��", "���͸�", "�����е�", "����", "�޴���", "������" };
	
	private static final Integer[] images = { R.drawable.img_ntpbm_0100_01,             
		R.drawable.img_ntpbm_0100_02, R.drawable.img_ntpbm_0100_03, R.drawable.img_ntpbm_0100_04,
		R.drawable.img_ntpbm_0100_05, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06
		, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06, R.drawable.img_ntpbm_0100_06};
	
	private static String[] descriptions = new String[] {"", "", "", "", "", "" };
	private static String[] chkboxInfo = new String[] {"", "", "", "", "", "" };
	
	private String barcode;
	
	private ListView listView;
	private List<RowItem> rowItems;
	private CustomBaseAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0103);
		
		Intent intent = getIntent();
		barcode = intent.getStringExtra("barcode");
		
		//���ڵ�(sn_cd)�� �̿��� ��������ȸ  server connecting... search barcode...
		searchNtpbm0103Info();
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 * ��Ͽ��� ���ý� �̺�Ʈ �߻� - üũ�ڽ� ����
	 */
	@Override    
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//MainActivity.logView(getApplicationContext(), position+"");
		adapter.setChecked(position);
        // Data ����� ȣ�� Adapter�� Data ���� ����� �˷��༭ Update ��.
		adapter.notifyDataSetChanged();
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, android.view.View, int, long)
	 * ��Ͽ��� ��Լ��ý� �̺�Ʈ �߻� - ������ ȭ������ �̵��ϴ� ��츸 �ش�.
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg2 == parseredDataList.size()-1) {
    		//�������ϋ��� ȣ��ȴ�.
    		Intent intent = new Intent(this, Ntpbm0108Activity.class);
			startActivity(intent);
    	}
		return false;
	}
	
	/** 
	 * Called when the user clicks the saveAccessoryInfo button
	 * �Ǽ��縮 ����� ���� ����
	 */ 
	public void saveAccessoryInfo(View view) {
		
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//�������� �����ϱ�.
		boolean[] isChecked = adapter.getIsCheckedConfrim();
		for (int i = 0; i < isChecked.length; i++) {
			//���������� ���������� Ȯ�� �Ͽ� ������ ��� ������Ʈ �������� ó���Ѵ�.
			strbuf.append("&seq=" + parseredDataList.get(i).get("SEQ"));
			strbuf.append("&chk_tf=" + (isChecked[i] ? "1" : "0"));
		}
		
		//���� url ��θ� xml���� �����´�.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0103_update).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		DialogSimple("ó�����", "����ó���Ǿ����ϴ�.", this);
	}
	
	/** 
	 * Called when the user clicks the cancelAccessoryInfo button
	 * �Ǽ��縮���� ��� ó��(����ȭ������ �̵��Ѵ�.)
	 */ 
	public void cancelAccessoryInfo(View view) {
		// Do something in response to button
		this.finish();
	}
	
	//                            ����, ǰ���ڵ�, �귣���, �𵨸�, �ĺ���, �Ҹ�ǰS/N��ȣ, ��ȿ����, Ȯ�ο���
	private String[] jsonName = {"SEQ", "IT_CD", "IT_NM", "MD_NM", "GD_OT", "SN_S_CD", "EXP_DATE", "CHK_TF"};
	private List<HashMap<String, String>> parseredDataList;
	
	/** 
	 * ���ڵ�(sn_cd)�� �̿��� ��������ȸ  (�Ǽ��縮�� ��ȸ�Ѵ�.)
	 * server connecting... search barcode... 
	 */ 
	public void searchNtpbm0103Info() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//���� url ��θ� xml���� �����´�.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0103).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//�������� ���� ��������� hashmap ���·� ��ȯ�Ѵ�.
		parseredDataList = server.jsonParserArrayList(resultInfo.toString(), jsonName);
		
		//�������� ��ȸ�� ������ ȭ�鿡 �����ش�.
		setData();
		
		//����� refresh �Ѵ�.
		setDisplay();
	}

	/*
	 * �������� ��ȸ�� ������ ȭ�鿡 �����ش�.
	 */
	private void setData() {
		if (parseredDataList != null) {
			titles = new String[parseredDataList.size()];//�Ǽ��縮 ����Ʈ
			descriptions = new String[parseredDataList.size()];//temp
			chkboxInfo = new String[parseredDataList.size()];//üũ����
			
			HashMap<String, String> parseredData = null;
			for(int i = 0; i < parseredDataList.size(); i++) {
				parseredData = parseredDataList.get(i);
				
				titles[i] = parseredData.get(jsonName[2]);//�Ǽ��縮��.
				chkboxInfo[i] = parseredData.get(jsonName[7]);//üũ��.
				descriptions[i] = "";
				
				parseredData.put("CHK_YN", chkboxInfo[i]);
			}
		}
	}
	
	/*
	 * ����� refresh �Ѵ�.
	 */
	private void setDisplay() {
		rowItems = new ArrayList<RowItem>();         
		for (int i = 0; i < titles.length; i++) {             
			RowItem item = new RowItem(images[0], titles[i], descriptions[i], chkboxInfo[i]);
			rowItems.add(item);
		}
		listView = (ListView) findViewById(R.id.list1);
		
		adapter = new CustomBaseAdapter(this, rowItems, R.layout.list_view02);         
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}
	

	/*
	 * �˾�â (���ó��)
	 */
	public void DialogSimple(String title, String message, Context context) {
	    AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
	    alt_bld.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	dialog.dismiss();     //�ݱ�
	        	close();
	        }
	    });
	    
	    AlertDialog alert = alt_bld.create();
	    alert.setTitle(title);
	    alert.setMessage(message);
	    alert.setCanceledOnTouchOutside(true);
	    alert.show();
	}
	
	public void close() {
		this.finish();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(false);
		    ab.setTitle(R.string.app_name);
		    ab.setSubtitle("��ġȮ��");
		}
	}
	
	// Option menu�� ������ ä��.
	// Option menu ȣ�� �� �ѹ��� �����.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.main01, menu);
		
	    return super.onPrepareOptionsMenu(menu);
	}
	
	// Option menu item�� ���õǾ����� �߻��Ѵ� event�� handler
	// �߻��� event�� �� �޼ҵ峪 super Ŭ������ ���� �޼ҵ忡�� ó�� ���� ������ false���� 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		int itemId = item.getItemId();
		
		switch (itemId) {
		case R.id.action_login:
			Intent intent = new Intent(this, Ntpbm0001Activity.class);
			startActivity(intent);
			break;
		case R.id.action_logout:
			MainActivity.loginYn = -1;
			this.finish();
			break;
		case R.id.action_personinfo:
			Intent intent2 = new Intent(this, Ntpbm0002Activity.class);
			startActivity(intent2);
			break;
		}
		
		return true;
	}
}
