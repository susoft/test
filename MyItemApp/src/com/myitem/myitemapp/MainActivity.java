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
         * �ʱ� ��Ƽ��Ƽ�� ȭ�鿡 �������� setContentView �޼��带 ���� ȭ�鿡 View �� �ٿ��ش�.
         * res/layout/activity_main.xml �� ��Ƽ��Ƽ�� ���̾ƿ����� �����Ѵ�.
         * ������ activity_main.xml �ȿ����� FrameLayout �ϳ��� ���� �Ǿ� �ִµ�, �̴� �ش� ���̾ƿ���
         * �����׸�Ʈ�� ����ϱ� ���� �̸� ���� �Ǿ� �ִ�.
         */
		setContentView(R.layout.activity_main);

		/*
         * savedInstanceState �� �����ΰ� ?
         * 
         * savedInstanceState ��ü�� onSaveInstanceState �޼��忡�� ������ Bundle ��ü�� ���� ������ ������ ����
         * onSaveInstanceState �޼��带 �����ϱ� ���ؼ� Override �ؾ� �Ѵ�.
         * onSaveInstanceState �޼��尡 ȣ��Ǵ� ������ �޸� ����/�������� �� ��ũ�� Orientation �� ����ɶ� ȣ��ȴ�.
         */
		if (savedInstanceState == null) {//�ʱ� ������Ʈ ������ ��Ƽ��Ƽ�� ���� ���尪�� NULL
			//ID �̸��� container(Frame Layout)�� ���̾ƿ� �ȿ�  �����׸�Ʈ�� ���̱� ���� �Ʒ� �ڵ� ���
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
	 * PlaceholderFragment Ŭ������ ���� ���� Ŭ����(Static Nested Class) �� �����Ͽ�, �ܺο����� �����Ҽ� �ֵ��� �����Ѵ�.
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
		
		//�̹��� Ŭ����.
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
