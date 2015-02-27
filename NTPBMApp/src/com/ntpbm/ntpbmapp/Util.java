package com.ntpbm.ntpbmapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

public class Util {
	public static String path = "";
	
	public static String path_detail = "";
	
	public static String sign_path = "sign";//로그인 서명 디렉토리
	
	public static String draw_path = "draw";//고객 서명 디렉토리
	
	public static String sn_path = "sn";//바코드별  디렉토리
	
	public static String photo_path = "photo";//서버의 사진  디렉토리
	
	public static void mkdirs() {
		File file = new File(path); 
        if( !file.exists() )  file.mkdirs();
        
        File file1 = new File(path + sign_path); 
        if( !file1.exists() )  file1.mkdirs();
        
        File file2 = new File(path + draw_path); 
        if( !file2.exists() )  file2.mkdirs();
        
        File file3 = new File(path + sn_path); 
        if( !file3.exists() )  file3.mkdirs();
	}
	
	/*
	 * 파일생성하기..
	 */
	public static void saveBitmapToFileCache(Bitmap bitmap, String strFileName, String paths, int size) {
		Log.e("file", paths + strFileName);
		
		if (strFileName == null || strFileName.equals("")) {
			return;
		}
		
		File fileCacheItem = new File(paths + strFileName);
		OutputStream out = null;

		try
		{
			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);
			bitmap.compress(CompressFormat.JPEG, size, out);
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
	
	/*
	 * 서버에서 다운로드하기.
	 */
	public static Bitmap download(String strFileName, String paths) {
		HttpConnectServer server = new HttpConnectServer();
		
		String imageUrl = paths + File.separator + strFileName;
		return server.LoadImage(imageUrl);
	}
	
	/*
	 * 팝업창 (결과처리)
	 */
	public static void DialogSimple(String title, String message, Context context) {
	    AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
	    alt_bld.setPositiveButton("확인", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	dialog.dismiss();     //닫기
	        }
	    });
	    
	    AlertDialog alert = alt_bld.create();
	    alert.setTitle(title);
	    alert.setMessage(message);
	    alert.setCanceledOnTouchOutside(true);
	    alert.show();
	}
	
}
