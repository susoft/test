package com.ntpbm.ntpbmapp.app0100;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ntpbm.ntpbmapp.MainActivity;
import com.ntpbm.ntpbmapp.Ntpbm0001Activity;
import com.ntpbm.ntpbmapp.Ntpbm0002Activity;
import com.ntpbm.ntpbmapp.R;

public class Ntpbm0109Activity extends Activity {
	
	private DrawView1 dv;
	ArrayList<Vertex1> arVertex;
	LinearLayout linear;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ntpbm_0109);
		
		dv = new DrawView1(this);
		
		linear = (LinearLayout)findViewById(R.id.ntpbm_0109_linearLayout00);
		linear.addView(dv);
		
		arVertex = new ArrayList<Vertex1>();
		
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
		    ab.setSubtitle("고객서명");
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
	
	/** Called when the user clicks the regi button */ 
	public void regi(View view) {
		// Do something in response to button
		
		File myDir=new File(Environment.getExternalStorageDirectory().getPath() + "/ntpbm/Draw");
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis())); //***
        String fname = timeStamp + ".png";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        
        String fnamepath = file.getAbsolutePath ();
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream (fnamepath);
            
            linear.buildDrawingCache();
            Bitmap bitmap = linear.getDrawingCache();
            bitmap.compress (CompressFormat.PNG, 100, fos);
            
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
//            Toast.makeText (getApplicationContext(), "Saved " + fname, Toast.LENGTH_LONG).show ();
            
            //sending file name..
            Intent intent = getIntent();
            intent.putExtra("ntpbm0109_regi_fname", fname);
            intent.putExtra("ntpbm0109_regi_myDir", myDir.toString());
            
            setResult(RESULT_OK, intent);
            
            this.finish();
            
        } catch (Throwable ex) {
        	Toast.makeText (getApplicationContext(), "Error: " + ex.getMessage (), Toast.LENGTH_LONG).show ();
        	ex.printStackTrace ();
        }
	}
	
	/** Called when the user clicks the reset button */ 
	public void reset(View view) {
		// Do something in response to button
		dv = new DrawView1(this);
		
		linear = (LinearLayout)findViewById(R.id.ntpbm_0109_linearLayout00);
		linear.addView(dv);
		
		arVertex = new ArrayList<Vertex1>();
	}
	
	/** Called when the user clicks the cancel button */ 
	public void cancel(View view) {
		// Do something in response to button
		this.finish();
	}
	
	public class Vertex1 {
		float x;
		float y;
		boolean Draw;
		int color;
		   
		Vertex1(float ax, float ay, boolean ad, int color) {
			x = ax;
			y = ay;
			Draw = ad;
			this.color=color;
		}
	}
	
	protected class DrawView1 extends View {
		Paint mPaint;
	    boolean clear;
	    int co;
	    public DrawView1(Context context) {
	        super(context);
	        
	        // Paint 객체 미리 초기화
			mPaint = new Paint();
			mPaint.setStrokeWidth(3); //두께 설정
			mPaint.setAntiAlias(true); //부드러운 표현
	  
			clear = false;
			co=Color.BLACK;
	    }
	    public void onDraw(Canvas canvas) {
	    	canvas.drawColor(0xffe0e0e0);//배경 하얀색으로 덮어(도화지 배경을 하얗게 칠함)
	  
	    	// 정점을 순회하면서 선분으로 잇는다.
	    	for (int i=0;i<arVertex.size();i++) {
	    		if (arVertex.get(i).Draw) {
	    			mPaint.setColor(arVertex.get(i).color);
	    			//
	    			canvas.drawLine(arVertex.get(i-1).x, arVertex.get(i-1).y, 
	    					arVertex.get(i).x, arVertex.get(i).y, mPaint);
	    		}
	    	}
	    }
	    
	    // 터치 이동시마다 정점들을 추가한다.
	    public boolean onTouchEvent(MotionEvent event) {
	    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    		arVertex.add(new Vertex1(event.getX(), event.getY(), false, co));
	    		return true;
	    	}
	    	if (event.getAction() == MotionEvent.ACTION_MOVE) {
	    		arVertex.add(new Vertex1(event.getX(), event.getY(), true, co));
	    		invalidate();//화면에 그림을 그림 -> onDraw()실행함.
	    		return true;
	    	}	
	    	return false;
	    }
	}
}
