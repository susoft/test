package com.hfmb.hfmbapp;

import java.io.File;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hfmb.hfmbapp.util.CommonUtil;
import com.hfmb.hfmbapp.util.HttpConnectServer;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {

	public static String phoneNum;
	
	private WebView mWebView;
	private final Handler handler = new Handler();
	private final Handler handler_img = new Handler();
	private String url;
	//private long backKeyPressedTime = 0;
    private Toast toast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_web);
		
		setLayout(); 
		
		url = getText(R.string.url).toString();
		
		certification();
		
		if (phoneNum == "") {
			openDialogAlert1("��Ż翡 ������ ���� ��� �����մϴ�.");
		} else {
        
	        // ���信�� �ڹٽ�ũ��Ʈ���డ��
	        mWebView.getSettings().setJavaScriptEnabled(true); 
	        
	        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	        
	        // Bridge �ν��Ͻ� ���
	        mWebView.addJavascriptInterface(new AndroidBridge(), "GwangjuApp");
	        // hfmp ����..
	        mWebView.loadUrl(url + getText(R.string.home).toString() + "?phonenum=" + phoneNum);
	        // WebViewClient ����
	        mWebView.setWebViewClient(new WebViewClientClass()); 
		}
	}
	
	//�ڱ��ڽ��� ��ȭ��ȣ ��������...
	@SuppressWarnings("static-access")
	public void certification() {
		TelephonyManager telManager = (TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE); 
		String telNum = telManager.getLine1Number();
		
		if (telNum == null || telNum.equals("")) {
			//phoneNum = "01063344115";
			phoneNum = "";
		} else {
			phoneNum = "0" + telNum.substring(telNum.indexOf("1"));
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	mWebView.loadUrl(url + getText(R.string.home).toString());
            return true; 
        } 
        return super.onKeyDown(keyCode, event);
    }

	//back key ����...
    /*@Override
	public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            this.finish();
            toast.cancel();
        }
    }*/
	public void showGuide() {
    	toast = Toast.makeText(getApplicationContext(),
                "\'�ڷ�\'��ư�� �ѹ� �� �����ø� ����˴ϴ�.", Toast.LENGTH_SHORT);
        toast.show();
    }
	
    private class WebViewClientClass extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            return true; 
        }
    }
     
    /*
     * Layout
     */
    private void setLayout(){
        mWebView = (WebView) findViewById(R.id.webView1);
    }
    
    public void closeMain() {
    	this.finish();
    }
    
    final class AndroidBridge {
    	//�������̵���.
    	@JavascriptInterface
    	public void callPage(final String arg) { // must be final
    		handler.post(new Runnable() {
    			public void run() {
    				Log.d("UlsanApp", "callPage("+arg+")");
    				mWebView.loadUrl(url + arg);
    			}
    		});
    	}
    	//�޼���
    	@JavascriptInterface
    	public void setMessage(final String arg) { // must be final
    		handler.post(new Runnable() {
    			public void run() {
    				Log.d("UlsanApp", "setMessage("+arg+")");
    				Toast toast = Toast.makeText(getApplicationContext(), arg, Toast.LENGTH_SHORT);
    				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    				toast.show();
    			}
    		});
    	}
    	//�� �ݱ�
    	@JavascriptInterface
    	public void closeApp() { // must be final
    		handler.post(new Runnable() {
    			public void run() {
    				Log.d("UlsanApp", "Close App...");
    				closeMain();
    			}
    		});
    	}
    	//�α׾ƿ��� �� �ݱ�
    	@JavascriptInterface
    	public void logout() { // must be final
    		handler.post(new Runnable() {
    			public void run() {
    				Log.d("UlsanApp", "Log Out.....");
    				Toast toast = Toast.makeText(getApplicationContext(), "�α׾ƿ��Ǿ����ϴ�.", Toast.LENGTH_SHORT);
    				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    				toast.show();
    			}
    		});
    	}
    	
    	//�������� ����...
    	@JavascriptInterface
    	public void openImg(final String arg) { // must be final
    		handler_img.post(new Runnable() {
    			public void run() {
    				Log.d("UlsanApp", "setMessage("+arg+")");
    				openGallerys(arg);
    			}
    		});
    	}

    	//��ȭ�ɱ�...
    	@JavascriptInterface
    	public void callphone(final String arg) { // must be final
    		handler_img.post(new Runnable() {
    			public void run() {
    				Log.d("UlsanApp", "setMessage("+arg+")");
    				openPhone(arg);
    			}
    		});
    	}
    }
    
    //��ȭ�ɱ�
    public void openPhone(String phoneNum) {
    	Uri u = Uri.parse("tel:" + phoneNum.replace("-", ""));
		Intent intent = new Intent(Intent.ACTION_CALL, u);
        
        this.startActivity(intent);
    }
    
    //������ ����...
    public void openGallerys(String tempFileNm) {
    	if (tempFileNm == null || tempFileNm.equals("")) {
    		fileNm = "ulsanapp_" + phoneNum;
    	} else {
    		fileNm = tempFileNm;
    	}
    	
		//�������� ����.
        Intent intent = new Intent(
                Intent.ACTION_GET_CONTENT,      // �Ǵ� ACTION_PICK
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");              // ��� �̹���
        intent.putExtra("crop", "true");        // Crop��� Ȱ��ȭ
        intent.putExtra(MediaStore.EXTRA_OUTPUT, CommonUtil.getTempUri());     // �ӽ����� ����
        intent.putExtra("outputFormat",         // ���˹��
                Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, 1);
    }
    
    String fileNm;
    String selfileName;
	//��������ϱ�.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 1://�̹������ý�.
			switch (resultCode) {
			case -1 ://������ �����Ë�.
				
				selfileName = Environment.getExternalStorageDirectory() + "/temp.jpg";
				
				Bitmap bitmap = CommonUtil.SafeDecodeBitmapFile(selfileName);

				String path = getApplicationContext().getCacheDir().getPath();

				//������ ������ �����Ѵ�.
				CommonUtil.SaveBitmapToFileCache(bitmap, fileNm + ".jpg", path);

				selfileName = path + File.separator + fileNm + ".jpg";

				updateInfo();
				
				break;
			}
			break;
		}
	}
	
	//�����Ѵ�.
	public void updateInfo() {
    	StringBuffer urlbuf = new StringBuffer();
    	HashMap<String, String> params = new HashMap<String, String>();
    	
    	params.put("filename", fileNm);
    	
    	urlbuf.append(url + "fileUploadByAndroid.jsp");
    	
    	HttpConnectServer server = new HttpConnectServer();
    	StringBuffer resultInfo = server.HttpFileUpload(urlbuf.toString(), params, selfileName);
    	
		Log.i("json:", resultInfo.toString());
		
		HashMap<String, String> results = server.jsonParserList(resultInfo.toString(), CommonUtil.jsonNameResult, "Result");
		
		if (results != null) {
			if (results.get("error") == null || results.get("error").equals("")) {
				openDialogAlert("���� ���ε尡 �����Ͽ����ϴ�. WIFI �Ǵ� ���ͳ��� �Ǿ� �ִ� �������� �����մϴ�.");
			} else if (results.get("error").equals("0")) {
				Log.i("json:", "���ε� ����!!");
				openDialogAlert("���� ���ε尡 �����Ͽ����ϴ�.");
				//������ �ڹٽ�ũ��Ʈ ȣ��
				mWebView.loadUrl("javascript:setImg('"+ fileNm +"', '"+ selfileName +"')");
				//mWebView.clearCache(true);
			}
		} else {
			openDialogAlert("���� ���ε尡 �����Ͽ����ϴ�. WIFI �Ǵ� ���ͳ��� �Ǿ� �ִ� �������� �����մϴ�.");
		}
	}
	
	public void openDialogAlert(String title) {
		//Ȯ�� ���̾�α�
		new AlertDialog.Builder(MainActivity.this)
        .setTitle(title)
		.setPositiveButton( "Ȯ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		})
        .show();
	}
	public void openDialogAlert1(String title) {
		//Ȯ�� ���̾�α�
		new AlertDialog.Builder(MainActivity.this)
        .setTitle(title)
		.setPositiveButton( "Ȯ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finishApp();
			}
		})
        .show();
	}
	
	public void finishApp() {
		this.finish();
//		android.os.Process.killProcess(android.os.Process.myPid());
		//System.exit(0);
	}
}
