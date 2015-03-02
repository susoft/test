package com.ntpbm.ntpbmapp;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListItem0300Adapter extends BaseAdapter {
	
	private Context context;
	private int resource;
	private List<HashMap<String, String>> rowItems;
	
	public ListItem0300Adapter(Context context, List<HashMap<String, String>> items, int source) {         
		this.context = context;         
		this.rowItems = items;
		this.resource = source;
	}
	
	/*private view holder class*/    
	private class ViewHolder {         
		ImageView imageView;         
		TextView txt01;         
		TextView txt02;
		TextView txt03;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {         
		ViewHolder holder = null;           
		LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);         
		if (convertView == null) {             
			convertView = mInflater.inflate(resource, null);             
			holder = new ViewHolder();
			
			holder.imageView = (ImageView) convertView.findViewById(R.id.list_view0300_imgview);
			holder.txt01 = (TextView) convertView.findViewById(R.id.list_view0300_txt01); 
			holder.txt02 = (TextView) convertView.findViewById(R.id.list_view0300_txt02);  
			holder.txt03 = (TextView) convertView.findViewById(R.id.list_view0300_txt03);  
			
			convertView.setTag(holder);         
		} else {             
			holder = (ViewHolder) convertView.getTag();         
		}
		
		HashMap<String, String> rowItem = getItem(position);           
		
		holder.imageView.setImageResource(R.drawable.ntpbm_0100_01);
		holder.txt01.setText(rowItem.get("IT_CD"));  //前格内靛       
		holder.txt02.setText(rowItem.get("IT_NM")); //前格疙
		holder.txt03.setText(rowItem.get("STK_SU")); //犁绊樊
		
		return convertView;     
	}
	
	@Override    
	public int getCount() {         return rowItems.size();     }
	
	@Override    
	public HashMap<String, String> getItem(int position) {         return rowItems.get(position);     }

	@Override    
	public long getItemId(int position) {         return rowItems.indexOf(getItem(position));     }
}
