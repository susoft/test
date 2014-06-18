package com.ntpbm.ntpbmapp.app0400;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.R;
import com.ntpbm.ntpbmapp.RowItem;

public class Ntpbm0400Activity extends Activity {
	
	ArrayList<String> txtBrand;
	ArrayList<String> txtModel;
	ArrayList<String> txtSuruong;
	ArrayList<String> txtDanga;
	ArrayList<Integer> images;
	
	ListView listView;
	
	static List<RowItem> rowItems;
	static Ntpbm0400Adapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0400);
		
		initListView();
		
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
		    ab.setSubtitle("견적서");
		}
	}
	
	// Option menu에 아이템 채움.
	// Option menu 호출 시 한번말 실행됨.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.main01, menu);
		
	    return super.onPrepareOptionsMenu(menu);
	}
	
	// Option menu item이 선택되었을때 발생한는 event의 handler
	// 발생한 event가 이 메소드나 super 클래스의 원래 메소드에서 처리 되지 않으면 false리턴 
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
			Intent intent2 = new Intent(this, Ntpbm0400Activity.class);
			startActivity(intent2);
			break;
		}
		
		return true;
	}
	
	/** Called when the user clicks the regiItem button */ 
	public void regiItem(View view) {
		// Do something in response to button
		Intent intent = new Intent(this, Ntpbm0401Activity.class);
		startActivityForResult(intent, 1);
		
	}
	
	/** Called when the user clicks the callSend button */ 
	public void callSend(View view) {
		// Do something in response to button
		Intent email = new Intent(Intent.ACTION_SEND);
		
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{"ouyoungdm@lycos.co.kr"});
	    email.putExtra(Intent.EXTRA_SUBJECT, "testing mail..");
	    email.putExtra(Intent.EXTRA_TEXT, "sending.... mail... ");
	    
	    String fileName = "test.html";
		String sendFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ntpbm" + File.separator + "mail" + File.separator + fileName;
		File f = new File(sendFilePath);
		if (f.exists()) {
//			MainActivity.logView(getApplicationContext(), "첨부파일이 생성되지 않앗습니다.");
			final Uri fileUri = Uri.fromFile(f);
			email.putExtra(Intent.EXTRA_STREAM, fileUri);
		}
	    
	    //need this to prompts email client only
	    email.setType("message/rfc822");
	    
	    startActivity(Intent.createChooser(email, "Choose an Email client :"));
	}
	
	/** Called when the user clicks the callCancel button */ 
	public void callCancel(View view) {
		// Do something in response to button
		this.finish();
	}
	
	/**  */ 
	public void initListView() {

		rowItems = new ArrayList<RowItem>();

		txtBrand = new ArrayList<String>();
		txtModel = new ArrayList<String>();
		txtSuruong = new ArrayList<String>();
		txtDanga = new ArrayList<String>();
		images = new ArrayList<Integer>();
		
//		txtBrand.add("Brand...");
//		txtModel.add("Model...");
//		txtSuruong.add("2");
//		txtDanga.add("10000");
//		images.add(R.drawable.img_ntpbm_0101_01);
		
		for (int i = 0; i < txtBrand.size(); i++) {             
			RowItem item = new RowItem(R.drawable.img_ntpbm_0100_01, "Brand...", "Model...", "2", "10000");             
			rowItems.add(item);
		}
		
		listView = (ListView) findViewById(R.id.ntpbm_0400_list);         
		adapter = new Ntpbm0400Adapter(this, rowItems, R.layout.list_view03);         
		listView.setAdapter(adapter);
	}
	
	/**  */ 
	public void addList(String ntpbm0109_regi_result) {
		// Do something in response to button
		
		//server connecting....
		      
		RowItem item = new RowItem(R.drawable.img_ntpbm_0100_01, "Brand...", "Model...", "2", "10000");             
		
		adapter.rowItems.add(item);
		adapter.notifyDataSetChanged();
	}
	
	/**  */ 
	public static void delList(View v, int position) {
		// Do something in response to button
		adapter.rowItems.remove(position);
		adapter.notifyDataSetChanged();
	}
	/**  */ 
	public static void modifyList(View v, int position) {
		// Do something in response to button
//		adapter.rowItems.remove(position);
//		adapter.notifyDataSetChanged();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		MainActivity.logView(getApplicationContext(), requestCode + ":" + resultCode);
		
		if (resultCode == -1) {
			String ntpbm0109_regi_result = data.getStringExtra("ntpbm0109_regi_result");
			
			addList(ntpbm0109_regi_result);
		}
	}
	
}

class Ntpbm0400Adapter extends BaseAdapter {
	
	Context context;
	List<RowItem> rowItems;
	int resource;
	
	public Ntpbm0400Adapter(Context context, List<RowItem> items, int source) {         
		this.context = context;
		this.rowItems = items;
		this.resource = source;
	}
	
	/*private view holder class*/    
	private class ViewHolder {         
		ImageView imageView;         
		TextView txtBrand;         
		TextView txtModel;
		TextView txtSuruong;
		TextView txtDanga;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {         
		ViewHolder holder = null;           
		LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);         
		if (convertView == null) {             
			convertView = mInflater.inflate(resource, null);             
			holder = new ViewHolder();
			
			holder.txtBrand = (TextView) convertView.findViewById(R.id.list_view03_txt01);
			holder.txtModel = (TextView) convertView.findViewById(R.id.list_view03_txt02);             
			holder.txtSuruong = (TextView) convertView.findViewById(R.id.list_view03_txt03);
			holder.txtDanga = (TextView) convertView.findViewById(R.id.list_view03_txt04);
			
			holder.imageView = (ImageView) convertView.findViewById(R.id.list_view03_imgview);
			
			Button btn01 = (Button)convertView.findViewById(R.id.list_view03_btn01);
			Button btn02 = (Button)convertView.findViewById(R.id.list_view03_btn02);
			
			final int posi = position;
			
			btn01.setOnClickListener(new OnClickListener(){
	            @Override
	            public void onClick(View v) {
	            	modifyList(v, posi);
	            }
	        });
			btn02.setOnClickListener(new OnClickListener(){
	            @Override
	            public void onClick(View v) {
	            	delList(v, posi);
	            }
	        });
			
			convertView.setTag(holder);         
		} else {             
			holder = (ViewHolder) convertView.getTag();         
		}
		RowItem rowItem = (RowItem) getItem(position);
		
		holder.txtBrand.setText(rowItem.getBrandNm());
		holder.txtModel.setText(rowItem.getModelNm());         
		holder.txtSuruong.setText(rowItem.getSuruong());
		holder.txtDanga.setText(rowItem.getDanga());
		holder.imageView.setImageResource(rowItem.getImageId());
		
		return convertView;     
	}
	public final void modifyList(View v, int position) {
		Ntpbm0400Activity.modifyList(v, position);
	}
	public final void delList(View v, int position) {
		Ntpbm0400Activity.delList(v, position);
	}
	
	@Override    
	public int getCount() {         return rowItems.size();     }
	
	@Override    
	public Object getItem(int position) {         return rowItems.get(position);     }

	@Override    
	public long getItemId(int position) {         return rowItems.indexOf(getItem(position));     }
}
