package com.hfmb.hfmbapp;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogListAdapter extends BaseAdapter {
	
	private Context context;
	private List<HashMap<String,String>> rowItems;
	private int resource;
	private boolean[] thumbnailsselection;
	private int selectedPosition;
	
	public DialogListAdapter(Context context, List<HashMap<String, String>> items, int source) {         
		this.context = context;
		this.resource = source;
		this.rowItems = items;
		
		this.thumbnailsselection = new boolean[this.rowItems.size()];
	}
	
	/*private view holder class*/    
	private class ViewHolder {
		ImageView imgbtn_01;
		TextView ceo_nm;
		TextView company_nm;
		TextView addr;
		TextView phone1;
		CheckBox checkbox;
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
			holder.addr = (TextView) convertView.findViewById(R.id.addr);
			holder.phone1 = (TextView) convertView.findViewById(R.id.phone1);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			
			convertView.setTag(holder);
		} else {             
			holder = (ViewHolder) convertView.getTag();         
		}
		
		holder.ceo_nm.setText(rowItems.get(position).get("ceo_nm"));
		holder.company_nm.setText(rowItems.get(position).get("company_nm"));
		holder.addr.setText(rowItems.get(position).get("addr"));
		holder.phone1.setText(rowItems.get(position).get("phone1"));
		
		holder.checkbox.setTag(position);
		holder.checkbox.setChecked(thumbnailsselection[position]);
        holder.checkbox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CheckBox cb = (CheckBox) v;
                int id = (Integer)cb.getTag();
                if (thumbnailsselection[id]){
                    cb.setChecked(false);
                    thumbnailsselection[id] = false;
                    selectedPosition = -1;
                } else {
                    cb.setChecked(true);
                    thumbnailsselection[id] = true;
                    selectedPosition = id;
                }
            }
        });
		
		return convertView;
	}
	
	private View.OnClickListener goClickListener = new View.OnClickListener() {
		@Override
        public void onClick(View v) {
			Log.i("tel", "");
		}
	};
	
	/**
	 * @return the rowItems
	 */
	public List<HashMap<String, String>> getRowItems() {
		return rowItems;
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

	/**
	 * @param rowItems the rowItems to set
	 */
	public void setRowItems(List<HashMap<String, String>> rowItems) {
		this.rowItems = rowItems;
		this.thumbnailsselection = new boolean[this.rowItems.size()];
	}
	
	@Override    
	public int getCount() {         return rowItems.size();     }
	
	@Override    
	public Object getItem(int position) {         return rowItems.get(position);     }

	@Override    
	public long getItemId(int position) {         return rowItems.indexOf(getItem(position));     }
	
}

