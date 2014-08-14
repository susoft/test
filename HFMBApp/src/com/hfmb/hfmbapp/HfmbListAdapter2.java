package com.hfmb.hfmbapp;

import java.util.ArrayList;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfmb.hfmbapp.util.CommonUtil;
import com.hfmb.hfmbapp.util.HttpConnectServer;

//사무국직원 리스트
public class HfmbListAdapter2 extends BaseAdapter {
	
	private Context context;
	private int resource;
	private HttpConnectServer server;
	
	private List<HashMap<String,String>> rowItems;
	private boolean[] thumbnailsselection;
	
	private int selectedPosition;
	
	public HfmbListAdapter2(Context context, List<HashMap<String, String>> items, int source) {         
		this.context = context;
		this.resource = source;
		this.rowItems = items;
		
		this.thumbnailsselection = new boolean[this.rowItems.size()];
		
		server = new HttpConnectServer();
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
			holder.category_business_nm = (TextView) convertView.findViewById(R.id.category_business_nm);
			holder.addr = (TextView) convertView.findViewById(R.id.addr);
			holder.phone1 = (TextView) convertView.findViewById(R.id.phone1);
			holder.phone2 = (TextView) convertView.findViewById(R.id.phone2);
			
			if (resource == R.layout.hfmbactivity_listview) {
				holder.imgbtn_02 = (ImageView) convertView.findViewById(R.id.imgbtn_02);
				convertView.findViewById(R.id.imgbtn_03).setVisibility(View.GONE);
			} else {
				holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			}
			
			convertView.setTag(holder);
		} else {             
			holder = (ViewHolder) convertView.getTag();         
		}
		
		//이미지
		String photoStr = rowItems.get(position).get("company_cd");
		if (photoStr != null && !photoStr.equals("")) {
			holder.imageUrl = "http://119.200.166.131:8054/JwyWebService/hfmbProWeb/photo/" + photoStr + ".jpg";
			
			new ImageCallTask().execute(holder);
		} else {
			holder.imgbtn_01.setImageResource(R.drawable.empty_photo);
		}
		
		holder.ceo_nm.setText(rowItems.get(position).get("ceo_nm"));
		holder.company_nm.setText(rowItems.get(position).get("company_nm"));
		holder.category_business_nm.setText(rowItems.get(position).get("category_business_nm"));
		holder.addr.setText(rowItems.get(position).get("addr"));
		holder.phone1.setText(rowItems.get(position).get("phone1"));
		holder.phone2.setText(rowItems.get(position).get("phone2"));
		
		if (resource == R.layout.hfmbactivity_listview) {
			holder.imgbtn_02.setTag(position);
			holder.imgbtn_02.setOnClickListener(goClickListener);
			holder.imgbtn_02.setOnTouchListener(CommonUtil.imgbtnTouchListener);
			
		} else {
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
		}
		
		return convertView;
	}
	
	private View.OnClickListener goClickListener = new View.OnClickListener() {
		@Override
        public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imgbtn_02:
				callTel((Integer)v.getTag());
				break;
			}
		}
	};
	
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
		this.thumbnailsselection = new boolean[this.rowItems.size()];
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

	public boolean[] getThumbnailsselection() {
		return thumbnailsselection;
	}

	public void setThumbnailsselection(boolean[] thumbnailsselection) {
		this.thumbnailsselection = thumbnailsselection;
	}
	
	public List<Integer> getSelectedCheckBox() {
		List<Integer> selectedCheckBox = new ArrayList<Integer>();
		for (int i = 0; i < thumbnailsselection.length; i++) {
			if (thumbnailsselection[i]) {
				selectedCheckBox.add(i);
			}
		}
		
		return selectedCheckBox;
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
	
	/*private view holder class*/    
	private class ViewHolder {
		ImageView imgbtn_01;
		TextView ceo_nm;
		TextView company_nm;
		TextView category_business_nm;
		TextView addr;
		TextView phone1;
		TextView phone2;
		ImageView imgbtn_02;//전화기
		CheckBox checkbox;
		
		Bitmap bm;
		String imageUrl;
	}
	
	//이미지정보 가져오기.
	public class ImageCallTask extends AsyncTask<ViewHolder, Void, ViewHolder> {
		@Override
		protected ViewHolder doInBackground(ViewHolder... params) {
        	ViewHolder viewHolder = params[0];
            try {
            	viewHolder.bm = searchData(viewHolder.imageUrl);
            } catch (Exception e) {
                Log.e("net","Not File : " + viewHolder.imageUrl);
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

