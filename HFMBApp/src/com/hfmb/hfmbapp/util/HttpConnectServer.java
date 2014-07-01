package com.hfmb.hfmbapp.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
			
			//Log.i("json:", strbuf.toString());
			
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
	
	//Tomcat 서버 호출
	public StringBuffer sendByHttpPost(String urlStr, ArrayList<NameValuePair> nameValuePairs) {
		
		StringBuffer resultSrt = new StringBuffer();
		DefaultHttpClient client = new DefaultHttpClient();
		
		try {
			//개발시 디버깅 하기 위함.. 실제로는 제거 해야 함. 만약 제거하여 테스트시 안되면 이 부분을 사용하도록 한다.
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			
			HttpPost post = new HttpPost(urlStr);
			
			//9초가 기다린다.
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params,  9000);
			HttpConnectionParams.setSoTimeout(params, 9000);
			
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
	public List<HashMap<String, String>> jsonParserList(String pRecvServerPage, String[] jsonName) {
		
		try {
			JSONObject json = new JSONObject(pRecvServerPage);
			JSONArray jArr = json.getJSONArray("List");
			
			List<HashMap<String, String>> parseredDataList = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> parseredData = new HashMap<String, String>();
			
			for (int i = 0; i < jArr.length(); i++) {
				json = jArr.getJSONObject(i);
				
				if (json != null) {
					
					parseredData = new HashMap<String, String>();
					for (int j = 0; j < jsonName.length; j++) {
						parseredData.put(jsonName[j], json.getString(jsonName[j]));
						
						//Log.i(jsonName[j] + " : ", json.getString(jsonName[j]));
					}
					
					parseredDataList.add(parseredData);
				}
			}
			return parseredDataList;
		} catch(JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Bitmap LoadImage( String imagePath ) { 
		//개발시 디버깅 하기 위함.. 실제로는 제거 해야 함. 만약 제거하여 테스트시 안되면 이 부분을 사용하도록 한다.
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
		
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
    
    public StringBuffer HttpFileUpload(String urlString, HashMap<String, String> params, String fileName) {
    	// 데이터 경계선
    	String boundary = "*****";
        String delimiter = "\r\n--" + boundary + "\r\n";
        
		try {
			//개발시 디버깅 하기 위함.. 실제로는 제거 해야 함. 만약 제거하여 테스트시 안되면 이 부분을 사용하도록 한다.
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
			
			FileInputStream mFileInputStream = null; 
			
			if (fileName != null && !fileName.equals("")) {
				mFileInputStream = new FileInputStream(fileName); 
			}
			URL connectUrl = new URL(urlString);
			
			//Log.d("Test", "fileName  is " + fileName);
			
			//Log.d("Test", "mFileInputStream  is " + mFileInputStream);
		   
			// 1. open connection 
			HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection(); 
			
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
		    
			// write data
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			
	        StringBuffer postDataBuilder = new StringBuffer();
			
	        // 2. 텍스트 추가...
	        // 추가하고 싶은 Key & Value 추가
	        // key & value를 추가한 후 꼭 경계선을 삽입해줘야 데이터를 구분할 수 있다.
	        postDataBuilder.append(delimiter);
	        
	        Object[] keySet = params.keySet().toArray();
	        for (int i = 0; i < keySet.length; i++) {
	        	postDataBuilder.append(setValue(keySet[i].toString(), params.get(keySet[i])));
		        postDataBuilder.append(delimiter);
	        }
	        
	        // 3. 이미지 추가
	        postDataBuilder.append(setFile("uploadedFile", fileName));
	        postDataBuilder.append("\r\n");
	        
	        // 4. 위에서 작성한 메타데이터를 먼저 전송한다. 
	        //    (한글이 포함되어 있으므로 UTF-8 메소드 사용)
	        dos.writeUTF(postDataBuilder.toString());
			
	        if (mFileInputStream != null) {
				// 5. 파일 복사 작업 시작
		        int bytesAvailable = mFileInputStream.available();
				int maxBufferSize = 1024;
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);
				byte[] buffer = new byte[bufferSize];
				// 버퍼 크기만큼 파일로부터 바이트 데이터를 읽는다.
				int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
			   
				//Log.d("Test", "image byte is " + bytesRead);
			   
				// 6. 전송
				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = mFileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
				}
	        }
		    
			dos.writeBytes(delimiter); // 반드시 작성해야 한다.
			
			if (mFileInputStream != null) {
				// close streams
				mFileInputStream.close();
			}
			
			// finish upload...   
			dos.flush(); 
			
			// 7. 결과 반환 (HTTP RES CODE)
			int ch;
			StringBuffer resultSrt =new StringBuffer();
			InputStream is = conn.getInputStream();
			while( ( ch = is.read() ) != -1 ){
				resultSrt.append( (char)ch );
			}
			//String s = resultSrt.toString(); 
			
			//close
			dos.close();
			is.close();
			conn.disconnect();
			
			//Log.e("Test", "result = " + s);
			
			return resultSrt.append(resultSrt);
		   
		} catch (Exception e) {
			Log.d("Test", "exception " + e.getMessage());
			// TODO: handle exception
		}
		
		return null;
	}
    
    /**
     * Map 형식으로 Key와 Value를 셋팅한다.
     * @param key : 서버에서 사용할 변수명
     * @param value : 변수명에 해당하는 실제 값
     * @return
     */
    public static String setValue(String key, String value) {
        return "Content-Disposition: form-data; name=\"" + key + "\"r\n\r\n"
                + value;
    }
 
    /**
     * 업로드할 파일에 대한 메타 데이터를 설정한다.
     * @param key : 서버에서 사용할 파일 변수명
     * @param fileName : 서버에서 저장될 파일명
     * @return
     */
    public static String setFile(String key, String fileName) {
        return "Content-Disposition: form-data; name=\"" + key
                + "\";filename=\"" + fileName + "\"\r\n";
    }
}
