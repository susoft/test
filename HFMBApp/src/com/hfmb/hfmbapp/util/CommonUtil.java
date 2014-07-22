package com.hfmb.hfmbapp.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class CommonUtil {
	
	public static Bitmap[] bitmap_003;
	public static Bitmap[] bitmap;
	public static String phoneNum;
	public static boolean searchYn;
	public static int insertYn;
	public static String meetingCd;
	public static String ceoNm;
	
	public String checkNull(String str) {
		if (str == null || str.equals("") || str.equals("null")) return "";
		
		return str;
	}
	
	/** log view */ 
	public static void showMessage(Context context, String strValue) {
		Toast toast = Toast.makeText(context, strValue, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
	
	/** log view */ 
	public static void showMessage(String tagnm, String strValue) {
		Log.i(tagnm, strValue);
	}
	
	//imageview 선택한건 처럼 처리한다.
	public static OnTouchListener imgbtnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				((ImageView)v).setColorFilter(0xaa111111, android.graphics.PorterDuff.Mode.SRC_OVER);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				((ImageView)v).setColorFilter(0x00000000, android.graphics.PorterDuff.Mode.SRC_OVER);
			}
			return false;
		}
	};
	
	public synchronized static Bitmap SafeDecodeBitmapFile(String strFilePath) {
		//DEBUG.SHOW_DEBUG(TAG, "[ImageDownloader] SafeDecodeBitmapFile : " + strFilePath);
		try {
			File file = new File(strFilePath);
			if (file.exists() == false) {
				//DEBUG.SHOW_ERROR(TAG, "[ImageDownloader] SafeDecodeBitmapFile : File does not exist !!");
				return null;
			}
			// Max image size
			final int IMAGE_MAX_SIZE    = 300;//GlobalConstants.getMaxImagePixelSize(); 
			BitmapFactory.Options bfo   = new BitmapFactory.Options();
			bfo.inJustDecodeBounds      = true;
			
			BitmapFactory.decodeFile(strFilePath, bfo);
			if(bfo.outHeight * bfo.outWidth >= IMAGE_MAX_SIZE * IMAGE_MAX_SIZE) {
				bfo.inSampleSize = (int)Math.pow(2, (int)Math.round(Math.log(IMAGE_MAX_SIZE
						/ (double) Math.max(bfo.outHeight, bfo.outWidth)) / Math.log(0.5)));
			}
			bfo.inJustDecodeBounds = false;
			bfo.inPurgeable = true;
			bfo.inDither = true;
			
			final Bitmap bitmap = BitmapFactory.decodeFile(strFilePath, bfo);
			
			int degree = GetExifOrientation(strFilePath);
		
			return GetRotatedBitmap(bitmap, degree);
		} catch(OutOfMemoryError ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public synchronized static int GetExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
	
		try { 
			exif = new ExifInterface(filepath); 
		} 
		catch (IOException e) 
		{ 
			Log.e("test", "cannot read exif"); 
			e.printStackTrace(); 
		} 
		if (exif != null) { 
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1); 
			if (orientation != -1) { 
				// We only recognize a subset of orientation tag values. 
				switch(orientation) { 
					case ExifInterface.ORIENTATION_ROTATE_90: 
					degree = 90; 
					break; 
					case ExifInterface.ORIENTATION_ROTATE_180: 
					degree = 180; 
					break; 
					case ExifInterface.ORIENTATION_ROTATE_270: 
					degree = 270; 
					break; 
				} 
			} 
		} 
		 
		return degree; 
	}
	
	public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees)
	{
		if ( degrees != 0 && bitmap != null )
		{
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2 );
			try
			{
				Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
				if (bitmap != b2)
				{
					bitmap.recycle();
					bitmap = b2;
				}
			}
			catch (OutOfMemoryError ex)
			{
				// We have no memory to rotate. Return the original bitmap.
				Log.e("OutOfMemoryError-----", "OutOfMemoryError"); 
			}
		}
		return bitmap;
	}
	
	public static byte[] bitmapToByteArray( Bitmap $bitmap ) {  
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;  
        $bitmap.compress( CompressFormat.JPEG, 100, stream) ;  
        byte[] byteArray = stream.toByteArray() ;  
        return byteArray ;  
    }
	
	public static ByteArrayOutputStream bitmapToSteam( Bitmap $bitmap ) {  
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;  
        $bitmap.compress( CompressFormat.JPEG, 100, stream) ; 
        return stream ;  
    }
	
	public static Bitmap byteArrayToBitmap( byte[] $byteArray ) {  
	    Bitmap bitmap = BitmapFactory.decodeByteArray( $byteArray, 0, $byteArray.length ) ;  
	    return bitmap ;  
	}  
	
	public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "apk" + File.separator;
	
	public static void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {
		Log.e("file", path + "" + strFilePath);
		File fileCacheItem = new File(path + strFilePath);
		OutputStream out = null;

		try
		{
			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);

			bitmap.compress(CompressFormat.JPEG, 100, out);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(out != null) out.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath, String paths) {
		Log.e("file", paths + "/" + strFilePath);
		File fileCacheItem = new File(paths + File.separator + strFilePath);
		OutputStream out = null;

		try
		{
			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);

			bitmap.compress(CompressFormat.JPEG, 100, out);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(out != null) out.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
