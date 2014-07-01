package com.hfmb.hfmbapp;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfmb.hfmbapp.util.HttpConnectServer;

public class HfmbListAdapter1 extends BaseAdapter {
	
	private Context context;
	private List<HashMap<String,String>> rowItems;
	private int resource;
	private int selectedPosition;
	private HttpConnectServer server;
	
	public HfmbListAdapter1(Context context, List<HashMap<String, String>> items, int source) {         
		this.context = context;
		this.resource = source;
		this.rowItems = items;
		
		server = new HttpConnectServer();
	}
	
	/*private view holder class*/    
	private class ViewHolder {
		ImageView imgbtn_01;
		TextView ceo_nm;
		TextView company_nm;
		TextView phone1;
		TextView phone2;
		
		Bitmap bm;
		String imageUrl;
		//int position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {   
		
		ViewHolder holder = null;           
		LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);         
		if (convertView == null) {             
			convertView = mInflater.inflate(resource, null);             
			holder = new ViewHolder();
			
			holder.imgbtn_01 = (ImageView) convertView.findViewById(R.id.imgbtn_01);
			holder.ceo_nm = (TextView) convertView.findViewById(R.id.ceo_nm);
			holder.company_nm = (TextView) convertView.findViewById(R.id.company_nm);
			holder.phone1 = (TextView) convertView.findViewById(R.id.phone1);
			holder.phone2 = (TextView) convertView.findViewById(R.id.phone2);
			
			convertView.setTag(holder);
		} else {             
			holder = (ViewHolder) convertView.getTag();         
		}
		
		String photoStr = rowItems.get(position).get("company_cd");
		if (photoStr != null && !photoStr.equals("")) {
			//holder.position = position;
			holder.imageUrl = "http://119.200.166.131:8054/JwyWebService/hfmbProWeb/ceohistory/" + photoStr+".jpg";
			new ImageCallTask().execute(holder);
		} else {
			holder.imgbtn_01.setImageResource(R.drawable.empty_photo);
		}
		
		String gubun_cd = rowItems.get(position).get("gubun_cd");
		if (gubun_cd != null && !gubun_cd.equals("")) {
			if (gubun_cd.equals("99")) {
				holder.ceo_nm.setText("제 " + rowItems.get(position).get("order_num") + " 대  " 
					+ "故)"
					+ rowItems.get(position).get("ceo_nm"));
			} else {
				holder.ceo_nm.setText("제 " + rowItems.get(position).get("order_num") + " 대  " 
						+ rowItems.get(position).get("ceo_nm"));
			}
		} else {
			holder.ceo_nm.setText("제 " + rowItems.get(position).get("order_num") + " 대  " 
					+ rowItems.get(position).get("ceo_nm"));
		}
		
		holder.company_nm.setText(rowItems.get(position).get("company_nm"));
		holder.phone1.setText(rowItems.get(position).get("phone1"));
		holder.phone2.setText(rowItems.get(position).get("phone2"));
		
		
		return convertView;
	}
	
	//전화번호 오픈
	public void callTel(int position) {
		Uri u = Uri.parse("tel:" + rowItems.get(position).get("phone2").replace("-", ""));
		Intent intent = new Intent(Intent.ACTION_CALL, u);
        
        context.startActivity(intent);
	}
	
	/**
	 * @return the rowItems
	 */
	public List<HashMap<String, String>> getRowItems() {
		return rowItems;
	}

	/**
	 * @param rowItems the rowItems to set
	 */
	public void setRowItems(List<HashMap<String, String>> rowItems) {
		this.rowItems = rowItems;
	}

	/**
	 * @return the resource
	 */
	public int getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(int resource) {
		this.resource = resource;
	}

	/**
	 * @return the selectedPosition
	 */
	public int getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * @param selectedPosition the selectedPosition to set
	 */
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	@Override    
	public int getCount() {         return rowItems.size();     }
	
	@Override    
	public Object getItem(int position) {         return rowItems.get(position);     }

	@Override    
	public long getItemId(int position) {         return rowItems.indexOf(getItem(position));     }
	
	//이미지정보 가져오기.
	public class ImageCallTask extends AsyncTask<ViewHolder, Void, ViewHolder> {
		@Override
		protected ViewHolder doInBackground(ViewHolder... params) {
        	ViewHolder viewHolder = params[0];
            try {
            	viewHolder.bm = searchData(viewHolder.imageUrl);
            } catch (Exception e) {
                Log.e("net","파일없음.");
            }
            return viewHolder;
        }
		@Override
        protected void onPostExecute(ViewHolder result) {
        	if (result.bm == null) {
	            result.imgbtn_01.setImageResource(R.drawable.empty_photo);
	        } else {
	            result.imgbtn_01.setImageBitmap(result.bm);
	        }
        }
	};
	//조회한다.
  	public Bitmap searchData(String imageUrl) {
    	return server.LoadImage(imageUrl);
  	}
}

