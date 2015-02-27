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
	
	public static String sign_path = "sign";//�α��� ���� ���丮
	
	public static String draw_path = "draw";//�� ���� ���丮
	
	public static String sn_path = "sn";//���ڵ庰  ���丮
	
	public static String photo_path = "photo";//������ ����  ���丮
	
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
	 * ���ϻ����ϱ�..
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
	 * �������� �ٿ�ε��ϱ�.
	 */
	public static Bitmap download(String strFileName, String paths) {
		HttpConnectServer server = new HttpConnectServer();
		
		String imageUrl = paths + File.separator + strFileName;
		return server.LoadImage(imageUrl);
	}
	
	/*
	 * �˾�â (���ó��)
	 */
	public static void DialogSimple(String title, String message, Context context) {
	    AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
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
