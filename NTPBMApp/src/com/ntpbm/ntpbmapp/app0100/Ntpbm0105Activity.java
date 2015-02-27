package com.ntpbm.ntpbmapp.app0100;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ntpbm.ntpbmapp.HttpConnectServer;
import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;
import com.ntpbm.ntpbmapp.Util;

/*
 * ��ġ��ġ�� ���������ȸ �� ����, ����
 */
public class Ntpbm0105Activity extends Activity {
	
	private String barcode;
	private String crud;
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	
	private ImageAdapter adapter;
	private GridView grid;
	private int selPositionImg = -1;
	
	public String fullpath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0105);
		
		Intent intent = getIntent();
		barcode = intent.getStringExtra("barcode");
		
		fullpath = Util.path + Util.sn_path + File.separator + barcode + File.separator;
		File fileDetail = new File(fullpath); 
        if( !fileDetail.exists() ) fileDetail.mkdirs();
		
		grid = (GridView)findViewById(R.id.photoGridView);
		adapter = new ImageAdapter(this, fullpath);
		grid.setAdapter(adapter);
		
		grid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selPositionImg = position;
				
				boolean flag = true;
				for(int i = 0; i < adapter.pictures.length; i++) {
					if (adapter.pictures[i].startsWith(adapter.picture[selPositionImg]) ) {
						BitmapFactory.Options bo = new BitmapFactory.Options();  
						bo.inSampleSize = 8;
						Bitmap bmp = BitmapFactory.decodeFile(fullpath + adapter.pictures[i]);  
						
						ImageView imgView = (ImageView)findViewById(R.id.ntpbm_0105_imgview);
						imgView.setImageBitmap(bmp);
						flag = false;
						break;
					}
				}
				
				if (flag) {
					addPhoto();//�űԻ��� �߰�....
				}
			}
		});

		if (selPositionImg < 0)
			//���ڵ�(sn_cd)�� �̿��� ��������ȸ  server connecting... search barcode...
			searchNtpbm0105Info();

		// Show the Up button in the action bar.
		setupActionBar();
	}

	//��ġ����,	��ġ���,	��ġ�ּ�,	�����,	��ȭ��ȣ,	�޴���ȭ,	�̸���
	private String[] jsonName = {"SN_CD", "ISP_IMG1", "ISP_IMG2", "ISP_IMG3", "ISP_IMG4"};
	private List<HashMap<String, String>> parseredDataList;

	/** 
	 * ���ڵ�(sn_cd)�� �̿��� ��������ȸ  server connecting... search barcode... 
	 */ 
	public void searchNtpbm0105Info() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		
		//���� url ��θ� xml���� �����´�.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0105).toString();
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json:", resultInfo.toString());
		
		//�������� ���� ��������� hashmap ���·� ��ȯ�Ѵ�.
		parseredDataList = server.jsonParserArrayList(resultInfo.toString(), jsonName);

		//�������� ��ȸ�� ������ ȭ�鿡 �����ش�.
		setData();
	}

	/*
	 * �������� ��ȸ�� ������ ȭ�鿡 �����ش�.
	 */
	private void setData() {
		crud = "c";//����
		if (parseredDataList != null) {
			HashMap<String, String> parseredData = null;
			for(int i = 0; i < parseredDataList.size(); i++) {
				parseredData = parseredDataList.get(i);
				
				//�������� ������ �ٿ�ε� �Ͽ� ���ÿ� �����Ѵ�.
				saveImage(parseredData.get("ISP_IMG1").toString());
				saveImage(parseredData.get("ISP_IMG2").toString());
				saveImage(parseredData.get("ISP_IMG3").toString());
				saveImage(parseredData.get("ISP_IMG4").toString());
				
				crud = "u";
			}
			
			adapter.notifyDataSetChanged();
		}
	}
	
	/*
	 * �������� ������ �ٿ�ε� �Ͽ� ���ÿ� �����Ѵ�.
	 */
	private void saveImage(String filename) {
		if (filename == null || filename.equals("")) {
			return;
		}
		
		for(int i = 0; i < adapter.pictures.length; i++) {
			if (adapter.pictures[i].startsWith(filename.substring(0,8)) ) {
				File file = new File(fullpath + adapter.pictures[i]);
				file.delete();
				break;
			}
		}
		
		String imageUrl = MainActivity.domainUrl + MainActivity.ntpbmPath + File.separator + Util.photo_path + File.separator + barcode;
		Util.saveBitmapToFileCache(Util.download(filename, imageUrl), filename, fullpath, 100);
	}
	
	/** 
	 * Called when the user clicks the addPhoto button 
	 * Ȯ�ι�ư �̺�Ʈ
	 */ 
	public void confirmPhoto(View view) {
		this.finish();
	}
	
	/** 
	 * Called when the user clicks the addPhoto button 
	 * ���� �߰���ư �̺�Ʈ
	 */ 
	public void addPhoto(View view) {
		addPhoto();
	}
	
	/** 
	 * Called when the user clicks the addPhoto button 
	 * �����߰��ϱ�... �������� �����Ѵ�.
	 */ 
	public void addPhoto() {
		// Do something in response to button
		// create Intent to take a picture and return control to the calling application
		if (adapter.pictures.length > 3) {
			return;
		}
		
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	    try{
	    	
	    	Log.i("selPositionImg", "selPositionImg = " + selPositionImg);
	    	
	    	for(int i = 0; i < adapter.pictures.length; i++) {
				if (adapter.pictures[i].startsWith(adapter.picture[selPositionImg]) ) {
					File file = new File(fullpath + adapter.pictures[i]);
					file.delete();
					break;
				}
			}
	    	
		    File f = createImageFile();
		    
	    	Log.i("f.getName()", "f.getName() = " + f.getName());
	    	
		    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f)); // set the image file name
		    tempfilename = f.getName();
		    
		    Log.i("tempfilename", "tempfilename     ----------- = " + tempfilename);
	
		    // start the image capture Intent
		    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }
	}
	public String tempfilename;
	// �����ϱ�
	final static String JPEG_FILE_PREFIX = "";
	final static String JPEG_FILE_SUFFIX = ".jpg";

	private File createImageFile() throws IOException{
		String imageFileName = JPEG_FILE_PREFIX + "ISP_IMG" + (selPositionImg + 1);
		File image = File.createTempFile(
			imageFileName,			// prefix
			JPEG_FILE_SUFFIX,		// suffix
			new File(fullpath) // directory [API level 8 �̻�]
		);
		return image;
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 * �����Կ��� ���� - ����....
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.i("resultCode", "resultCode = " + resultCode);
		
		if(resultCode!=0){//-1 �ϰ�� ����ó��...
			try{
				
				Log.i("tempfilename-0", "tempfilename = " + tempfilename);
				
				//������ ������ �����Ѵ�.
				BitmapFactory.Options bo = new BitmapFactory.Options();  
				bo.inSampleSize = 8;
				Bitmap bitmap = BitmapFactory.decodeFile(fullpath + tempfilename, bo);
				Util.saveBitmapToFileCache(bitmap, tempfilename.substring(0,8) + ".jpg", fullpath, 100);
				
				Log.i("tempfilename-1", "tempfilename = " + tempfilename);
				
				File file = new File(fullpath + tempfilename);
				file.delete();
				
				Log.i("tempfilename-2", "tempfilename = " + tempfilename);
				
				adapter = new ImageAdapter(Ntpbm0105Activity.this, fullpath);
				grid.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				
				for(int i = 0; i < adapter.pictures.length; i++) {
					if (adapter.pictures[i].startsWith(adapter.picture[selPositionImg]) ) {
						tempfilename = adapter.pictures[i];
						break;
					}
				}
				
				Log.i("tempfilename-3", "tempfilename = " + tempfilename);
				
				//���� ���� ������ �����ϱ�.... �߰��ؾ� ��.
				addNtpbm0105Info();
				
				ImageView imgView = (ImageView)findViewById(R.id.ntpbm_0105_imgview);
				imgView.setImageBitmap(null);
				
				selPositionImg = -1;
				
			} catch(Exception e){
				return;
			}
		} else {//0�ϰ�� ����ó��..
			Log.i("tempfilename 0", "tempfilename = " + tempfilename);
			File file = new File(fullpath + tempfilename);
			file.delete();
		}
	}
	
	/*
	 * �Կ��� ���� ������ ������ �����Ͽ� �����Ѵ�.
	 * multipart parser�� �����ؾ���.
	 */
	public void addNtpbm0105Info() {
    	StringBuffer urlbuf = new StringBuffer();
    	HashMap<String, String> params = new HashMap<String, String>();
    	
    	params.put("sn_cd", barcode);
    	params.put("isp_img", (selPositionImg+1)+"");
    	params.put("crud", crud);
    	
    	Log.i("json: before", params.get("isp_img"));
    	
    	String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0105_add).toString();
		urlbuf.append(urlStr);
    	
    	HttpConnectServer server = new HttpConnectServer();
    	StringBuffer resultInfo = server.HttpFileUpload(urlbuf.toString(), params, fullpath + tempfilename);
    	
    	Log.i("json:result", resultInfo.toString().trim());
    	
    	crud = "u";
    	
    	DialogSimple("ó�����", "����ó���Ǿ����ϴ�.");
    }
	
	
	/** 
	 * Called when the user clicks the delPhoto button 
	 * ������ ������ �����Ѵ�.
	 */ 
	public void delPhoto(View view) {
		// Do something in response to button
		//MainActivity.logView(getApplicationContext(), selPositionImg+"");
		if (selPositionImg < 0) {
			return;
		}
		
		for(int i = 0; i < adapter.pictures.length; i++) {
			if (adapter.pictures[i].startsWith(adapter.picture[selPositionImg]) ) {
				File file = new File(fullpath + adapter.pictures[i]);
				file.delete();
				break;
			}
		}

		//������ ������ ���� ��û...
		delNtpbm0105Info();
		
		adapter = new ImageAdapter(this, fullpath);
		grid.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		ImageView imgView = (ImageView)findViewById(R.id.ntpbm_0105_imgview);
		imgView.setImageBitmap(null);
		
		selPositionImg = -1;
	}
	
	/*
	 * ������ ������ ������ �����Ͽ� ó���Ѵ�.
	 */
	private void delNtpbm0105Info() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("sn_cd=" + barcode);
		strbuf.append("&isp_img=" + (selPositionImg+1));
		
		//���� url ��θ� xml���� �����´�.
		String urlStr = MainActivity.domainUrl + MainActivity.ntpbmPath0100 + getText(R.string.ntpbm_0105_del).toString();
		
		Log.i("json: before", strbuf.toString());
		
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlStr);
		
		Log.i("json: result", resultInfo.toString());
		
		DialogSimple("ó�����", "����ó���Ǿ����ϴ�.");
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

	private void DialogSimple(String title, String message) {
	    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	    alt_bld.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	dialog.dismiss();     //�ݱ�
	        }
	    });
	    
	    AlertDialog alert = alt_bld.create();
	    alert.setTitle(title);
	    alert.setMessage(message);
	    alert.setCanceledOnTouchOutside(true);
	    alert.show();
	}
	
}

/*
 * �̹��� ����͸� �̿��� �׸��� ����.
 */
class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private String fullpath;
	
	String[] picture = { "ISP_IMG1", "ISP_IMG2", "ISP_IMG3", "ISP_IMG4" };
	String[] pictures;
	
	public ImageAdapter(Context c, String fullpath) {
		this.mContext = c;
		this.fullpath = fullpath;
		
        File fileDetail = new File(fullpath);
        
		pictures = fileDetail.list(filter);
	}
	
	private FilenameFilter filter = new FilenameFilter(){
		public boolean accept(File dir, String filename) {
			if(filename.startsWith("ISP_IMG")) return true;
			return false;
		}
	};
	
	public int getCount() {
		return picture.length;
	}
	
	public Object getItem(int position) {
		return picture[position];
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		
		if (convertView == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
			imageView.setAdjustViewBounds(false);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8,  8,  8, 8);
		} else {
			imageView = (ImageView) convertView;
		}
		
		BitmapFactory.Options bo = new BitmapFactory.Options();  
		bo.inSampleSize = 8;  
		Bitmap bmp = null;
		
		for(int i = 0; i < pictures.length; i++) {
			if (pictures[i].startsWith(picture[position]) ) {
				bmp = BitmapFactory.decodeFile(fullpath + pictures[i]);
				break;
			}
		}
		
		if(bmp != null) {
			Bitmap resized = Bitmap.createScaledBitmap(bmp, 130, 130, true);
			imageView.setImageBitmap(resized);
		} else {
			imageView.setImageResource(R.drawable.ntpbm_0100);
		}
		
		return imageView;
	}

}
