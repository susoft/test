package com.ntpbm.ntpbmapp.app0100;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;

public class Ntpbm0105Activity extends Activity {
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
//	private Uri fileUri;
	
	ImageAdapter adapter;
	GridView grid;
	int selPositionImg = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0105);
		
		grid = (GridView)findViewById(R.id.photoGridView);
		adapter = new ImageAdapter(this);
		grid.setAdapter(adapter);
		
		grid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				MainActivity.logView(getApplicationContext(), "test" + position);
				selPositionImg = position;
				
				BitmapFactory.Options bo = new BitmapFactory.Options();  
				bo.inSampleSize = 8;  
				Bitmap bmp = BitmapFactory.decodeFile(adapter.path + File.separator + adapter.pictures[position], bo);  
				//Bitmap resized = Bitmap.createScaledBitmap(bmp, 320, 320, true);
				
				ImageView imgView = (ImageView)findViewById(R.id.ntpbm_0105_imgview);
				imgView.setImageBitmap(bmp);
			}
		});
		
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
		    ab.setSubtitle("설치확인");
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
			Intent intent2 = new Intent(this, Ntpbm0002Activity.class);
			startActivity(intent2);
			break;
		}
		
		return true;
	}
	
	/** Called when the user clicks the addPhoto button */ 
	public void addPhoto(View view) {
		// Do something in response to button
		// create Intent to take a picture and return control to the calling application
		
		if (adapter.pictures.length > 3) {
			return;
		}
		
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	    try{
		    File f = createImageFile();
		    
//		    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f)); 
		    
//		    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
		    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f)); // set the image file name
	
		    // start the image capture Intent
		    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	// 저장하기
	final static String JPEG_FILE_PREFIX = "IMG_";
	final static String JPEG_FILE_SUFFIX = ".jpg";

	private File createImageFile() throws IOException{
		String timeStamp = new SimpleDateFormat( "yyyyMMdd_HHmmss").format( new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File image = File.createTempFile(
			imageFileName,			// prefix
			JPEG_FILE_SUFFIX,		// suffix
			getAlbumDir()				// directory
		);
//		String mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}
	
	// 저장할 위치를 얻는 방법
	// [API level 8 이상]
	public File getAlbumDir(){
		File storageDir = new File(adapter.path);
		return storageDir;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode!=0){
//			if(requestCode==1&&!data.equals(null)){
				try{
					adapter = new ImageAdapter(Ntpbm0105Activity.this);
					grid.setAdapter(adapter);
					
				} catch(Exception e){
					return;
				}
//			}
		}
	}
	
	/** Called when the user clicks the delPhoto button */ 
	public void delPhoto(View view) {
		// Do something in response to button
		MainActivity.logView(getApplicationContext(), selPositionImg+"");
		if (selPositionImg < 0) {
			return;
		}
		
		File file = new File(adapter.path + adapter.pictures[selPositionImg]);
		file.delete();
		
		adapter = new ImageAdapter(this);
		grid.setAdapter(adapter);
		
		ImageView imgView = (ImageView)findViewById(R.id.ntpbm_0105_imgview);
		imgView.setImageBitmap(null);
		
		selPositionImg = -1;
	}
	
}

class ImageAdapter extends BaseAdapter {
	private Context mContext;
	
	int[] picture = { R.drawable.ntpbm_0100, R.drawable.ntpbm_0200, R.drawable.ntpbm_0300, R.drawable.ntpbm_0400 };
	
	String[] pictures;
	String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ntpbm" + File.separator;
	
	public ImageAdapter(Context c) {
		mContext = c;
		
		File file = new File(path); 
        if( !file.exists() )  // 원하는 경로에 폴더가 있는지 확인
        	file.mkdirs();
		
		File list = new File(path);
		pictures = list.list(filter);
		
		MainActivity.logView(mContext, pictures.length+"");
	}
	
	private FilenameFilter filter = new FilenameFilter(){
		public boolean accept(File dir, String filename) {
			if(filename.endsWith(".JPG") || filename.endsWith(".jpg")) return true;
			return false;
		}
	};
	
	public int getCount() {
		return pictures.length;
	}
	
	public Object getItem(int position) {
		return pictures[position];
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
		Bitmap bmp = BitmapFactory.decodeFile(path + File.separator + pictures[position], bo);  
		Bitmap resized = Bitmap.createScaledBitmap(bmp, 130, 130, true);
		imageView.setImageBitmap(resized);
		
		return imageView;
	}
}
