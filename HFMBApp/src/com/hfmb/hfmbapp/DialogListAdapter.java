package com.hfmb.hfmbapp;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hfmb.hfmbapp.util.CommonUtil;
import com.hfmb.hfmbapp.util.HttpConnectServer;

public class DialogListAdapter extends BaseAdapter {
	
	private Context context;
	private List<HashMap<String,String>> rowItems;
	private int resource;
	private boolean[] thumbnailsselection;
	private int selectedPosition;
	private int priveousPosition;
	private CheckBox priveousCheckbox;
	private HttpConnectServer server;
	
	public DialogListAdapter(Context context, List<HashMap<String, String>> items, int source) {         
		this.context = context;
		this.resource = source;
		this.rowItems = items;
		
		this.thumbnailsselection = new boolean[this.rowItems.size()];
		
		server = new HttpConnectServer();
		
		priveousPosition = -1;
        priveousCheckbox = null;
	}
	
	/*private view holder class*/    
	private class ViewHolder {
		ImageView imgbtn_01;
		TextView ceo_nm;
		TextView company_nm;
		TextView addr;
		TextView meeting_nm;
		TextView phone2;
		CheckBox checkbox;
		
		Bitmap bm;
		String imageUrl;
		String path;
		String company_cd;
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
			holder.meeting_nm = (TextView) convertView.findViewById(R.id.meeting_nm);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			holder.phone2 = (TextView)convertView.findViewById(R.id.phone2);
			
			convertView.setTag(holder);
		} else {             
			holder = (ViewHolder) convertView.getTag();         
		}
		HashMap<String, String> rowItem = rowItems.get(position);
		holder.ceo_nm.setText(rowItem.get("ceo_nm"));
		holder.company_nm.setText(rowItem.get("company_nm"));
		holder.addr.setText(rowItem.get("addr"));
		if (rowItem.get("meeting_nm") != null) {
			holder.meeting_nm.setText(rowItem.get("meeting_nm") + " 교류회");
		} else {
			holder.meeting_nm.setText("");
		}
		holder.phone2.setText(rowItem.get("phone2"));
		
		holder.checkbox.setTag(position);
		holder.checkbox.setChecked(thumbnailsselection[position]);
        holder.checkbox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	
            	if (priveousPosition > -1) {
            		priveousCheckbox.setChecked(false);
                    thumbnailsselection[priveousPosition] = false;
            	}
            	
                CheckBox cb = (CheckBox) v;
                int id = (Integer)cb.getTag();
                if (thumbnailsselection[id]){
                    cb.setChecked(false);
                    thumbnailsselection[id] = false;
                    selectedPosition = -1;
                    priveousPosition = -1;
                    priveousCheckbox = null;
                } else {
                    cb.setChecked(true);
                    thumbnailsselection[id] = true;
                    selectedPosition = id;
                    priveousPosition = id;
                    priveousCheckbox = cb;
                }
            }
        });
        
        //이미지
		String photoStr = rowItem.get("company_cd");
		if (photoStr != null && !photoStr.equals("")) {
			holder.imageUrl = "http://119.200.166.131:8054/JwyWebService/hfmbProWeb/photo/" + photoStr + ".jpg";
			holder.path = context.getApplicationContext().getCacheDir().getPath();
			holder.company_cd = photoStr;
			new ImageCallTask().execute(holder);
		} else {
			holder.imgbtn_01.setImageResource(R.drawable.empty_photo);
		}
			
		return convertView;
	}
	
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
	 * @return the selectedPosition
	 */
	public int getPriveousPosition() {
		return priveousPosition;
	}
	
	/**
	 * @return 
	 */
	public void setPriveousPosition(int priveousPosition) {
		this.priveousPosition = priveousPosition;
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
	
	//이미지정보 가져오기.
	public class ImageCallTask extends AsyncTask<ViewHolder, Void, ViewHolder> {
		@Override
		protected ViewHolder doInBackground(ViewHolder... params) {
        	ViewHolder viewHolder = params[0];
            try {
            	//캐쉬폴더에 존재하면 그걸 보여준다.
            	File file = new File(viewHolder.path + File.separator + viewHolder.company_cd + ".jpg");
            	
            	if (file.isFile()) {
            		Log.e("Tag","File : " + file.getPath());
            		viewHolder.bm = BitmapFactory.decodeFile(viewHolder.path + File.separator + viewHolder.company_cd + ".jpg");
            	} else {
            		viewHolder.bm = searchData(viewHolder.imageUrl);
                	
                	//압축한 파일을 저장한다.
        			CommonUtil.SaveBitmapToFileCache(viewHolder.bm, viewHolder.company_cd + ".jpg", viewHolder.path);
        			Log.e("Tag","File : " + viewHolder.company_cd);
            	}
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

