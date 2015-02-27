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
			openDialogAlert1("통신사에 가입한 폰만 사용 가능합니다.");
		} else {
        
	        // 웹뷰에서 자바스크립트실행가능
	        mWebView.getSettings().setJavaScriptEnabled(true); 
	        
	        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	        
	        // Bridge 인스턴스 등록
	        mWebView.addJavascriptInterface(new AndroidBridge(), "GwangjuApp");
	        // hfmp 연결..
	        mWebView.loadUrl(url + getText(R.string.home).toString() + "?phonenum=" + phoneNum);
	        // WebViewClient 지정
	        mWebView.setWebViewClient(new WebViewClientClass()); 
		}
	}
	
	//자기자신의 전화번호 가져오기...
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

	//back key 제어...
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
                "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
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
    	//페이지이동시.
    	@JavascriptInterface
    	public void callPage(final String arg) { // must be final
    		handler.post(new Runnable() {
    			public void run() {
    				Log.d("UlsanApp", "callPage("+arg+")");
    				mWebView.loadUrl(url + arg);
    			}
    		});
    	}
    	//메세지
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
    	//앱 닫기
    	@JavascriptInterface
    	public void closeApp() { // must be final
    		handler.post(new Runnable() {
    			public void run() {
    				Log.d("UlsanApp", "Close App...");
    				closeMain();
    			}
    		});
    	}
    	//로그아웃후 앱 닫기
    	@JavascriptInterface
    	public void logout() { // must be final
    		handler.post(new Runnable() {
    			public void run() {
    				Log.d("UlsanApp", "Log Out.....");
    				Toast toast = Toast.makeText(getApplicationContext(), "로그아웃되었습니다.", Toast.LENGTH_SHORT);
    				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    				toast.show();
    			}
    		});
    	}
    	
    	//갤러리앱 열기...
    	@JavascriptInterface
    	public void openImg(final String arg) { // must be final
    		handler_img.post(new Runnable() {
    			public void run() {
    				Log.d("UlsanApp", "setMessage("+arg+")");
    				openGallerys(arg);
    			}
    		});
    	}

    	//전화걸기...
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
    
    //전화걸기
    public void openPhone(String phoneNum) {
    	Uri u = Uri.parse("tel:" + phoneNum.replace("-", ""));
		Intent intent = new Intent(Intent.ACTION_CALL, u);
        
        this.startActivity(intent);
    }
    
    //갤러리 열기...
    public void openGallerys(String tempFileNm) {
    	if (tempFileNm == null || tempFileNm.equals("")) {
    		fileNm = "ulsanapp_" + phoneNum;
    	} else {
    		fileNm = tempFileNm;
    	}
    	
		//갤러리를 띄운다.
        Intent intent = new Intent(
                Intent.ACTION_GET_CONTENT,      // 또는 ACTION_PICK
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");              // 모든 이미지
        intent.putExtra("crop", "true");        // Crop기능 활성화
        intent.putExtra(MediaStore.EXTRA_OUTPUT, CommonUtil.getTempUri());     // 임시파일 생성
        intent.putExtra("outputFormat",         // 포맷방식
                Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, 1);
    }
    
    String fileNm;
    String selfileName;
	//사진등록하기.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 1://이미지선택시.
			switch (resultCode) {
			case -1 ://데이터 가져올떄.
				
				selfileName = Environment.getExternalStorageDirectory() + "/temp.jpg";
				
				Bitmap bitmap = CommonUtil.SafeDecodeBitmapFile(selfileName);

				String path = getApplicationContext().getCacheDir().getPath();

				//압축한 파일을 저장한다.
				CommonUtil.SaveBitmapToFileCache(bitmap, fileNm + ".jpg", path);

				selfileName = path + File.separator + fileNm + ".jpg";

				updateInfo();
				
				break;
			}
			break;
		}
	}
	
	//저장한다.
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
				openDialogAlert("파일 업로드가 실패하였습니다. WIFI 또는 인터넷이 되어 있는 곳에서만 가능합니다.");
			} else if (results.get("error").equals("0")) {
				Log.i("json:", "업로드 성공!!");
				openDialogAlert("파일 업로드가 성공하였습니다.");
				//웹뷰의 자바스크립트 호출
				mWebView.loadUrl("javascript:setImg('"+ fileNm +"', '"+ selfileName +"')");
				//mWebView.clearCache(true);
			}
		} else {
			openDialogAlert("파일 업로드가 실패하였습니다. WIFI 또는 인터넷이 되어 있는 곳에서만 가능합니다.");
		}
	}
	
	public void openDialogAlert(String title) {
		//확인 다이얼로그
		new AlertDialog.Builder(MainActivity.this)
        .setTitle(title)
		.setPositiveButton( "확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		})
        .show();
	}
	public void openDialogAlert1(String title) {
		//확인 다이얼로그
		new AlertDialog.Builder(MainActivity.this)
        .setTitle(title)
		.setPositiveButton( "확인", new DialogInterface.OnClickListener() {
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
