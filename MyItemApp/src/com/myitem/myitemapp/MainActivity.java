package com.myitem.myitemapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*
         * 초기 액티비티가 화면에 보여질때 setContentView 메서드를 통해 화면에 View 를 붙여준다.
         * res/layout/activity_main.xml 을 액티비티의 레이아웃으로 설정한다.
         * 하지만 activity_main.xml 안에서는 FrameLayout 하나만 선언 되어 있는데, 이는 해당 레이아웃에
         * 프레그먼트를 사용하기 위해 미리 선언 되어 있다.
         */
		setContentView(R.layout.activity_main);

		/*
         * savedInstanceState 란 무엇인가 ?
         * 
         * savedInstanceState 객체는 onSaveInstanceState 메서드에서 저장한 Bundle 객체에 대한 정보를 가지고 있음
         * onSaveInstanceState 메서드를 구현하기 위해선 Override 해야 한다.
         * onSaveInstanceState 메서드가 호출되는 시점은 메모리 부족/강제종료 및 스크린 Orientation 이 변경될때 호출된다.
         */
		if (savedInstanceState == null) {//초기 프로젝트 실행후 액티비티의 상태 저장값은 NULL
			//ID 이름이 container(Frame Layout)인 레이아웃 안에  프래그먼트를 붙이기 위해 아래 코드 사용
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 * PlaceholderFragment 클래스를 정적 내부 클래스(Static Nested Class) 로 선언하여, 외부에서도 접근할수 있도록 선언한다.
	 */
	public static class PlaceholderFragment extends Fragment {

		ImageView imageview_icon1;
		ImageView imageview_icon2;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			imageview_icon1 = (ImageView) rootView.findViewById(R.id.icon1);
			
			imageview_icon1.setOnClickListener(mOnClickListener);
			
			imageview_icon2 = (ImageView) rootView.findViewById(R.id.icon2);
			
			imageview_icon2.setOnClickListener(mOnClickListener);
			
			return rootView;
		}
		
		public void image_icon1() {
			Intent intent;
			intent = new Intent(getActivity(), Myitem001Activity.class);
			startActivity(intent);
		}
		
		public void image_icon2() {
			Intent intent;
			intent = new Intent(getActivity(), MyItem002Activity.class);
			startActivity(intent);
		}
		
		//이미지 클릭시.
		private OnClickListener mOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				switch (view.getId()) {
				case R.id.icon1:
					image_icon1();
					break;
				case R.id.icon2:
					image_icon2();
					break;
				default:
					image_icon1();
					break;
				}
			}
		};
	}
}
