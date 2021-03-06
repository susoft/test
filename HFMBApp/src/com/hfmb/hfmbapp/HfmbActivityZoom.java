package com.hfmb.hfmbapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.hfmb.hfmbapp.util.CommonUtil;

public class HfmbActivityZoom extends Activity implements OnTouchListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hfmb_zoom);
		
		Intent intent = getIntent();
		
		int display = intent.getIntExtra("display", 1);
		int index = intent.getIntExtra("index", 1);
		
    	setImage(display, index);
    	
    	ImageView imageView = (ImageView) findViewById(R.id.btnClose);
    	imageView.setOnTouchListener(CommonUtil.imgbtnTouchListener);
    	imageView.setOnClickListener(mOnClickListener);
	}
	
	public void setImage(int display, int index) {
		ImageView imageView = (ImageView) findViewById(R.id.imageview_zoom);
		
		switch (index) {
        case 1:
        	//Log.e("LongClick", "회장인사말");
        	if (display == 1) imageView.setImageResource(R.drawable.intro_1);
        	else imageView.setImageResource(R.drawable.info_1);
        	break;
        case 2:
        	//Log.e("LongClick", "중소기업융합이란");
        	if (display == 2) imageView.setImageResource(R.drawable.intro_2);
        	else imageView.setImageResource(R.drawable.info_2);
        	break;
        case 3:
        	//Log.e("LongClick", "찾아오시는길");
        	if (display == 3) imageView.setImageResource(R.drawable.intro_3);
        	else imageView.setImageResource(R.drawable.info_3);
        	break;
		}
		
		imageView.setOnTouchListener(this);
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override    
		public void onClick(View view) {
			activityClose();
	    }
	};
	
	public void activityClose() {
		this.finish();
	}

	private static final String TAG = "Touch";
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	private Matrix savedMatrix2 = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	private static final int WIDTH = 0;
	private static final int HEIGHT = 1;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;

		// Dump touch event to log
		dumpEvent(event);

		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			//Log.d(TAG, "mode=DRAG");
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			//Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				//Log.d(TAG, "mode=ZOOM");
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			//Log.d(TAG, "mode=NONE");
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				// ...
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x,
						event.getY() - start.y);
			}
			else if (mode == ZOOM) {
				float newDist = spacing(event);
				//Log.d(TAG, "newDist=" + newDist);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}

		matrixTurning(matrix, view);
		view.setImageMatrix(matrix);
		return true; // indicate event was handled
	}

	/** Show an event in the LogCat view, for debugging */
	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
				"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
				|| actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid ").append(
					action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";");
		}
		sb.append("]");
		//Log.d(TAG, sb.toString());
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	private void matrixTurning(Matrix matrix, ImageView view){
		float[] value = new float[9];
		matrix.getValues(value);
		float[] savedValue = new float[9];
		savedMatrix2.getValues(savedValue);

		int width = view.getWidth();
		int height = view.getHeight();

		Drawable d = view.getDrawable();
		if (d == null)  return;
		int imageWidth = d.getIntrinsicWidth();
		int imageHeight = d.getIntrinsicHeight();
		int scaleWidth = (int) (imageWidth * value[0]);
		int scaleHeight = (int) (imageHeight * value[4]);

		if (value[2] < width - scaleWidth)   value[2] = width - scaleWidth;
		if (value[5] < height - scaleHeight)   value[5] = height - scaleHeight;
		if (value[2] > 0)   value[2] = 0;
		if (value[5] > 0)   value[5] = 0;

		if (value[0] > 10 || value[4] > 10){
			value[0] = savedValue[0];
			value[4] = savedValue[4];
			value[2] = savedValue[2];
			value[5] = savedValue[5];
		}

		if (imageWidth > width || imageHeight > height){
			if (scaleWidth < width && scaleHeight < height){
				int target = WIDTH;
				if (imageWidth < imageHeight) target = HEIGHT;

				if (target == WIDTH) value[0] = value[4] = (float)width / imageWidth;
				if (target == HEIGHT) value[0] = value[4] = (float)height / imageHeight;

				scaleWidth = (int) (imageWidth * value[0]);
				scaleHeight = (int) (imageHeight * value[4]);

				if (scaleWidth > width) value[0] = value[4] = (float)width / imageWidth;
				if (scaleHeight > height) value[0] = value[4] = (float)height / imageHeight;
			}
		}else{
			if (value[0] < 1)   value[0] = 1;
			if (value[4] < 1)   value[4] = 1;
		}

		scaleWidth = (int) (imageWidth * value[0]);
		scaleHeight = (int) (imageHeight * value[4]);
		if (scaleWidth < width){
			value[2] = (float) width / 2 - (float)scaleWidth / 2;
		}
		if (scaleHeight < height){
			value[5] = (float) height / 2 - (float)scaleHeight / 2;
		}

		matrix.setValues(value);
		savedMatrix2.set(matrix);
	}
}
