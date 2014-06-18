package com.ntpbm.ntpbmapp;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomBaseAdapter extends BaseAdapter {
	
	Context context;
	List<RowItem> rowItems;
	int resource;
	private boolean[] isCheckedConfrim;
	
	public CustomBaseAdapter(Context context, List<RowItem> items, int source) {         
		this.context = context;         
		this.rowItems = items;
		this.resource = source;
		
		// CheckBox의 true/false를 구별 하기 위해
		this.isCheckedConfrim = new boolean[items.size()];
	}
	
	/*private view holder class*/    
	private class ViewHolder {         
		ImageView imageView;         
		TextView txtTitle;         
		TextView txtDesc;
		CheckBox chkinfo;
	}
	
	public void setChecked(int position) {
        isCheckedConfrim[position] = !isCheckedConfrim[position];
    }
	
	public View getView(int position, View convertView, ViewGroup parent) {         
		ViewHolder holder = null;           
		LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);         
		if (convertView == null) {             
			convertView = mInflater.inflate(resource, null);             
			holder = new ViewHolder();
			
			if (resource == R.layout.list_view02) {
				holder.chkinfo = (CheckBox) convertView.findViewById(R.id.chkinfo);
				
				// CheckBox는 기본적으로 이벤트를 가지고 있기 때문에 ListView의 아이템
	            // 클릭리즈너를 사용하기 위해서는 CheckBox의 이벤트를 없애 주어야 한다.
				holder.chkinfo.setClickable(false);
				holder.chkinfo.setFocusable(false);
	            
			} else {
				holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
			}
			holder.txtTitle = (TextView) convertView.findViewById(R.id.title);             
			holder.imageView = (ImageView) convertView.findViewById(R.id.icon);  
			
			convertView.setTag(holder);         
		} else {             
			holder = (ViewHolder) convertView.getTag();         
		}
		RowItem rowItem = (RowItem) getItem(position);           
		if (resource == R.layout.list_view02) {
			holder.chkinfo.setText(rowItem.getChkinfo());
			holder.chkinfo.setChecked(isCheckedConfrim[position]);
		} else {
			holder.txtDesc.setText(rowItem.getDesc());
		}
		
		holder.txtTitle.setText(rowItem.getTitle());         
		holder.imageView.setImageResource(rowItem.getImageId());
		
		return convertView;     
	}
	
	@Override    
	public int getCount() {         return rowItems.size();     }
	
	@Override    
	public Object getItem(int position) {         return rowItems.get(position);     }

	@Override    
	public long getItemId(int position) {         return rowItems.indexOf(getItem(position));     }
}
