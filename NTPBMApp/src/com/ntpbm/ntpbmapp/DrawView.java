package com.ntpbm.ntpbmapp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
	Paint mPaint;
    boolean clear;
    int co;
    ArrayList<Vertex> arVertex;
    
    public DrawView(Context context, ArrayList<Vertex> arVertex) {
        super(context);
        
        this.arVertex = arVertex;
        
        // Paint ��ü �̸� �ʱ�ȭ
		mPaint = new Paint();
		mPaint.setStrokeWidth(3); //�β� ����
		mPaint.setAntiAlias(true); //�ε巯�� ǥ��
  
		clear = false;
		co=Color.BLACK;
		/*
		LinearLayout.LayoutParams paramtext =
         new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                  LinearLayout.LayoutParams.WRAP_CONTENT);
	  //    
	  btn= new Button(context);
	  btn.setText("X");
	  linear1.addView(btn,paramtext);
	  btn.setOnClickListener(this);
	  //
	  btn1= new Button(context); //btn1.setText("����");
	  btn1.setBackgroundColor(Color.BLACK);
	  linear1.addView(btn1,paramtext);
	  btn1.setOnClickListener(this);
	  //
	  btn2= new Button(context); //btn2.setText("����");
	  btn2.setBackgroundColor(Color.RED);
	  linear1.addView(btn2,paramtext);    
	  btn2.setOnClickListener(this);
	  //
	  btn3= new Button(context); //btn3.setText("�Ķ�");
	  btn3.setBackgroundColor(Color.BLUE);
	  linear1.addView(btn3,paramtext);
	  btn3.setOnClickListener(this);
	  //
	  btn4= new Button(context); //btn3.setText("���");
	  btn4.setBackgroundColor(Color.YELLOW);
	  linear1.addView(btn4,paramtext);
	  btn4.setOnClickListener(this);
	  //
	  btn5= new Button(context); //btn3.setText("");
	  btn5.setBackgroundColor(Color.CYAN);
	  linear1.addView(btn5,paramtext);
	  btn5.setOnClickListener(this);
	  //
	  btn6= new Button(context); //btn3.setText("���");
	  btn6.setBackgroundColor(Color.GREEN);
	  linear1.addView(btn6,paramtext);
	  btn6.setOnClickListener(this);
	  */
  
    }
    public void onDraw(Canvas canvas) {
    	canvas.drawColor(0xffe0e0e0);//��� �Ͼ������ ����(��ȭ�� ����� �Ͼ�� ĥ��)
  
    	// ������ ��ȸ�ϸ鼭 �������� �մ´�.
    	for (int i=0;i<arVertex.size();i++) {
    		if (arVertex.get(i).Draw) {
    			mPaint.setColor(arVertex.get(i).color);
    			//
    			canvas.drawLine(arVertex.get(i-1).x, arVertex.get(i-1).y, 
    					arVertex.get(i).x, arVertex.get(i).y, mPaint);
    		}
    	}
    }
    
    // ��ġ �̵��ø��� �������� �߰��Ѵ�.
    public boolean onTouchEvent(MotionEvent event) {
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		arVertex.add(new Vertex(event.getX(), event.getY(), false, co));
    		return true;
    	}
    	if (event.getAction() == MotionEvent.ACTION_MOVE) {
    		arVertex.add(new Vertex(event.getX(), event.getY(), true, co));
    		invalidate();//ȭ�鿡 �׸��� �׸� -> onDraw()������.
    		return true;
    	}	
    	return false;
    }
    // �̰��� canvas�� �ٽ� ���Ͼ� ��ȭ���� ����� ��ư ó���̴�.
    // onDraw()�۵� ������ �����Ѵٸ�
    // arVeertex�� clear() �����ְ� �ٽ� �׸���
    // �׸� ���� ���� ������ �������Ƿ� ���Ͼ� ��ȭ���� �ȴ�.
//    @Override
//    public void onClick(View v) {
//    	if(v==btn){
//    		arVertex.clear();
//    		invalidate();
//    	}else if(v==btn1){ co = Color.BLACK;
//    	}else if(v==btn2){ co = Color.RED;
//    	}else if(v==btn3){ co = Color.BLUE;
//    	}else if(v==btn4){ co = Color.YELLOW;
//    	}else if(v==btn5){ co = Color.CYAN;
//    	}else if(v==btn6){ co = Color.GREEN;
//    	}
//  
//    }
}
