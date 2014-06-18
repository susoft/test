package com.ntpbm.ntpbmapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;

public class HttpConnectServer {
	
	//Tomcat 서버 호출
	public StringBuffer sendByHttp(StringBuffer strbuf, String urlStr) {
		
		StringBuffer resultSrt = new StringBuffer();
		
		DefaultHttpClient client = new DefaultHttpClient();
		
		try {
			//개발시 디버깅 하기 위함.. 실제로는 제거 해야 함. 만약 제거하여 테스트시 안되면 이 부분을 사용하도록 한다.
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			
			HttpPost post = new HttpPost(urlStr + "?" + strbuf);
			
			//9초가 기다린다.
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params,  9000);
			HttpConnectionParams.setSoTimeout(params, 9000);
			
			HttpResponse response = client.execute(post);
			BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
			
			String line = null;
			
			while((line = bufreader.readLine()) != null) {
				resultSrt.append(line);
			}
			
			return resultSrt.append(resultSrt);
		} catch(Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown();
			
			return resultSrt.append("error:"+e.toString());
		}
	}
	
	InputStream is;
	
	//서버에서 받은 json 결과를 hashmap에 넣어 리턴한다.
	public HashMap<String, String> jsonParserList(String pRecvServerPage, String[] jsonName) {
		
		try {
			JSONObject json = new JSONObject(pRecvServerPage);
			JSONArray jArr = json.getJSONArray("List");
			
			HashMap<String, String> parseredData = new HashMap<String, String>();
			
			for (int i = 0; i < jArr.length(); i++) {
				json = jArr.getJSONObject(i);
				
				if (json != null) {
					for (int j = 0; j < jsonName.length; j++) {
						parseredData.put(jsonName[j], json.getString(jsonName[j]));
						
						Log.i("test:", json.getString(jsonName[j]));
					}
				}
			}
			return parseredData;
		} catch(JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Bitmap LoadImage( String imagePath ) {  
        // TODO Auto-generated method stub  
        InputStream inputStream = OpenHttpConnection( imagePath ) ;  
        Bitmap bm = BitmapFactory.decodeStream( inputStream ) ;  
        
        return bm;  
    }  
  
    private InputStream OpenHttpConnection(String $imagePath) {  
        // TODO Auto-generated method stub  
        InputStream stream = null ;  
        try {  
            URL url = new URL( $imagePath ) ;  
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection() ;  
            urlConnection.setRequestMethod( "GET" ) ;  
            urlConnection.connect() ;  
            if( urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK ) {  
                stream = urlConnection.getInputStream() ;  
            }  
        } catch (MalformedURLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return stream ;  
    }  
}
