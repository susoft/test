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

public class HfmbListAdapter extends BaseAdapter {
	
	private Context context;
	private List<HashMap<String,String>> rowItems;
	private int resource;
	private boolean[] thumbnailsselection;
	private int selectedPosition;
	private HttpConnectServer server;
	
//	private int gubun;
	
	public HfmbListAdapter(Context context, List<HashMap<String, String>> items, int source) {         
		this.context = context;
		this.resource = source;
		this.rowItems = items;
		
		this.thumbnailsselection = new boolean[this.rowItems.size()];
		
		server = new HttpConnectServer();
//		this.gubun = 0;
	}
	
	public HfmbListAdapter(Context context, List<HashMap<String, String>> items, int source, int gubun) {         
		this.context = context;
		this.resource = source;
		this.rowItems = items;
		
		this.thumbnailsselection = new boolean[this.rowItems.size()];
		
		server = new HttpConnectServer();
//		this.gubun = gubun;
	}
	
	/*private view holder class*/    
	private class ViewHolder {
		ImageView imgbtn_01;
		TextView ceo_nm;
		TextView company_nm;
		TextView category_business_nm;
		TextView addr;
		TextView phone1;
		TextView phone2;
		ImageView imgbtn_02;
		CheckBox checkbox;
		
		Bitmap bm;
		String imageUrl;
		//int position;
//		LinearLayout list_linear_01;
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
			} else {
				holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			}
			
			//holder.list_linear_01 = (LinearLayout) convertView.findViewById(R.id.list_linear_01);
			
			convertView.setTag(holder);
		} else {             
			holder = (ViewHolder) convertView.getTag();         
		}
		
		String photoStr = rowItems.get(position).get("company_cd");
		if (photoStr != null && !photoStr.equals("")) {
			//holder.position = position;
			holder.imageUrl = "http://119.200.166.131:8054/JwyWebService/hfmbProWeb/photo/" + photoStr+".jpg";
			new ImageCallTask().execute(holder);
		} else {
			holder.imgbtn_01.setImageResource(R.drawable.empty_photo);
			//CommonUtil.bitmap[position] = null;
		}
//		if (CommonUtil.bitmap[position] == null) {
//			holder.imgbtn_01.setImageResource(R.drawable.empty_photo);
//		} else {
//			holder.imgbtn_01.setImageBitmap(CommonUtil.bitmap[position]);
//		}
		
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
		
//		if (gubun == 1) {
//			holder.checkbox.setVisibility(View.VISIBLE);//�����ش�.
//			holder.imgbtn_02.setVisibility(View.GONE);//�����.
//		} else {
//			holder.checkbox.setVisibility(View.GONE);//�����.
//			holder.imgbtn_02.setVisibility(View.VISIBLE);//�����ش�.
//		}
		
		return convertView;
	}
	
	private View.OnClickListener goClickListener = new View.OnClickListener() {
		@Override
        public void onClick(View v) {
			callTel((Integer)v.getTag());
		}
	};
	
	//��ȭ��ȣ ����
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
	
	//�̹������� ��������.
	public class ImageCallTask extends AsyncTask<ViewHolder, Void, ViewHolder> {
		@Override
		protected ViewHolder doInBackground(ViewHolder... params) {
        	ViewHolder viewHolder = params[0];
            try {
            	viewHolder.bm = searchData(viewHolder.imageUrl);
            } catch (Exception e) {
                Log.e("net","���Ͼ���. : " + viewHolder.imageUrl);
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
	//��ȸ�Ѵ�.
  	public Bitmap searchData(String imageUrl) {
    	return server.LoadImage(imageUrl);
  	}
}
