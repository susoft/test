package com.hfmb.hfmbapp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfmb.hfmbapp.util.CommonUtil;
import com.hfmb.hfmbapp.util.DataUtil;
import com.hfmb.hfmbapp.util.HttpConnectServer;

public class HfmbActivity003 extends FragmentActivity {
	
	private boolean flag = true;
	private ProgressDialog dialog;
	private List<HashMap<String, String>> rowItems;
	private GridViewAdapter gridAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hfmb_003);

		// Show the Up button in the action bar.
		setupActionBar();
		
		startThread();
	}
	
	public void startThread() {
		if (flag) {
			dialog = ProgressDialog.show(this, "", "��ø� ��ٷ� �ּ��� ...", true);
			
			new Thread(new Runnable() {           
	            public void run() { 
	                while (true) {   
	                    try {
	                    	//������ ��ȸ.
	                    	getOrganData();
	                    	
	                    	Thread.sleep(1000);
	                    	if (rowItems != null) {
		                    	Message msg = handler.obtainMessage();
		                        handler.sendMessage(msg);
		                        break;
	                    	}
	                    } catch (InterruptedException ie) {
	                        ie.printStackTrace();
	                    }
	                }
	            }
	        }).start();
			flag = false;
		}
	}
	
	//thread ������ 1�ʸ��� �׸��� ������ gridview�� �߰��ϴ� �Լ�
	Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            setOrganListView(R.layout.hfmbactivity_003);//����ȸ ����Ʈ �����ϱ�.
    		
            dialog.dismiss();
            
            flag = false;
        }
    };
	
	//����ȸ ������ ��������.
	public void getOrganData() {
		rowItems = new ArrayList<HashMap<String, String>>();
		
		//��ȸ���ǿ� ���� ������ ����Ѵ�.
    	StringBuffer strbuf = new StringBuffer();
    	StringBuffer urlbuf = new StringBuffer();
    	
		Log.i("parameter ====== " , strbuf.toString()); 
		
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_003.jsp");
    	
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		StringBuffer resultInfo = server.sendByHttp(strbuf, urlbuf.toString());
		
		//Log.i("json:", resultInfo.toString());
		
		//�������� ���� ��������� hashmap ���·� ��ȯ�Ѵ�.
		String[] jsonName = {"meeting_cd", "meeting_nm", "photo", "ceo_nm1", "ceo_nm2", "company_count"};
		
		rowItems = server.jsonParserList(resultInfo.toString(), jsonName);
	}
	
	//����ȸ ����Ʈ �����ϱ�.
	public void setOrganListView(int resourceId) {
		GridView gridLayout = (GridView)findViewById(R.id.gridView);
		gridAdapter = new GridViewAdapter(this, resourceId, rowItems);
		gridLayout.setAdapter(gridAdapter);
		gridLayout.setOnItemClickListener(mOnItemClickListener);
		if (gridAdapter.getLayoutResourceId() == R.layout.hfmbactivity_003) {
			//����ȸ ������ �繫�������� ���� �ϵ��� �Ѵ�.
			if (DataUtil.insertYn == 1) {
				gridLayout.setOnItemLongClickListener(mOnItemLongClickListener);
			}
		}
	}
	
	//�׸���, ����Ʈ���� item�� �����Ͽ�����.
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override    
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			goHfmbActivity004(position);
	    }
	};
	
	//����ȸ�� �ش�Ǵ� ȸ���縦 ��ȸ�Ѵ�.
	private void goHfmbActivity004(int position) {
		Intent intent = new Intent(getApplicationContext(), HfmbActivity004.class);
    	intent.putExtra("meeting_cd", rowItems.get(position).get("meeting_cd"));
    	intent.putExtra("meeting_nm", rowItems.get(position).get("meeting_nm"));
    	intent.putExtra("position", position);
    	startActivityForResult(intent, 2);
	}
	
	//�׸���, ����Ʈ���� item�� ��Լ����Ͽ�����.
	private OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {
		@Override    
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			setOrganListView(R.layout.hfmbactivity_003_check);
			_menu.clear();
			getMenuInflater().inflate(R.menu.main01, _menu);
			return false;
	    }
	};
	
	String position;
	String meetingCd;
	String meetingNm;
	String selfileName;
	
	public void modifyMeetingPic(String position, String meetingCd, String meetingNm) {

      	if (DataUtil.insertYn == 2) {
      		if (!meetingCd.equals(DataUtil.meetingCd)) {
      			openDialogAlert("����ȸ ȸ��, �ѹ��� �ڽ��� ����ȸ�� ���� ���� �մϴ�.");
      			return;
      		}
      	}	
      	
		this.position = position;
		this.meetingCd = meetingCd;
		this.meetingNm = meetingNm;
		
		//�������� ����.
        Intent intent = new Intent(
                Intent.ACTION_GET_CONTENT,      // �Ǵ� ACTION_PICK
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");              // ��� �̹���
        intent.putExtra("crop", "true");        // Crop��� Ȱ��ȭ
        intent.putExtra(MediaStore.EXTRA_OUTPUT, CommonUtil.getTempUri());     // �ӽ����� ����
        intent.putExtra("outputFormat",         // ���˹��
                Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, 1);
	}
	
	//��������ϱ�.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		CommonUtil.showMessage("test", "[" + requestCode + "]-[" + resultCode+"]");
		switch (requestCode) {
		case 1://�̹������ý�.
			switch (resultCode) {
			case -1 ://������ �����Ë�.
				
				selfileName = Environment.getExternalStorageDirectory() + "/temp.jpg";
				
				Bitmap bitmap = CommonUtil.SafeDecodeBitmapFile(selfileName);

				String path = getApplicationContext().getCacheDir().getPath();

				//������ ������ �����Ѵ�.
				CommonUtil.SaveBitmapToFileCache(bitmap, meetingCd + ".jpg", path);

				selfileName = path + File.separator + meetingCd + ".jpg";

				openDialogModify("����ȸ ������ �����Ͻðڽ��ϱ�?");
				
				break;
			}
			break;
		case 2://ȸ���� ȭ�鿡�� ���ϵɶ�.
			switch (resultCode) {
			case -1 ://������ �����Ë�.
				
				//������ ������ �����ϸ� ����ȸ�Ѵ�.
				String delflag = data.getExtras().getString("delflag");
				//int positionTemp = Integer.parseInt(data.getExtras().getString("position"));
				//int delCount = Integer.parseInt(data.getExtras().getString("delCount"));
				
				if (delflag.equals("1")) {
					flag = true;
					startThread();
					
//					HashMap<String, String> tempMap = rowItems.get(positionTemp);
//					
//					tempMap.put("company_count", (Integer.parseInt(tempMap.get("company_count").toString()) - delCount) + "");
//					
//					gridAdapter.setData(rowItems);
//					gridAdapter.notifyDataSetChanged();
				}
				
				break;
			}
			break;
		}
	}
	
	public void modifySelecedData() {
		/*int idx = Integer.parseInt(position);
		View row = gridAdapter.getView(idx, null, null);
		ViewHolder viewHolder = (ViewHolder)row.getTag();
		viewHolder.image.setImageBitmap(bitmap);
		gridAdapter.notifyDataSetChanged();*/
		
		updateInfo();
	}
	
	//�����Ѵ�.
	public void updateInfo() {
    	StringBuffer urlbuf = new StringBuffer();
    	HashMap<String, String> params = new HashMap<String, String>();
    	
    	params.put("meeting_cd", meetingCd);
    	params.put("meeting_nm", meetingNm);
    	
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_updateMeeting.jsp");
    	
    	HttpConnectServer server = new HttpConnectServer();
    	StringBuffer resultInfo = server.HttpFileUpload(urlbuf.toString(), params, selfileName);
    	
		Log.i("json:", resultInfo.toString());
		
		HashMap<String, String> results = server.jsonParserList(resultInfo.toString(), DataUtil.jsonNameResult, "Result");
		
		if (results != null) {
			if (results.get("error").equals("0")) {
				openDialogAlert("���� �Ϸ� �Ǿ����ϴ�.");
				gridAdapter.notifyDataSetChanged();
			} else {
				openDialogAlert("���� ���� �Ǿ����ϴ�.");
			}
		}
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(false);
			ab.setTitle("����ȸ");
			ab.setSubtitle(DataUtil.ceoNm + DataUtil.temp_01);
		}
	}
	
	private Menu _menu;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		_menu = menu;
		return true;
	}
	@SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
	    
	    if (item.getItemId() == R.id.subMenu_1) {
	    	Intent intent = new Intent( this, HfmbActivity001.class);
			startActivity(intent);
			this.finish();
	    } else if (item.getItemId() == R.id.subMenu_2) {
	    	Intent intent = new Intent( this, HfmbActivity002.class);
			startActivity(intent);
    		this.finish();
	    } else if (item.getItemId() == R.id.subMenu_3) {
	    	Intent intent = new Intent( this, HfmbActivity003.class);
			startActivity(intent);
    		this.finish();
	    } else if (item.getItemId() == R.id.subMenu_4) {
	    	Intent intent = new Intent( this, HfmbActivity004.class);
			startActivity(intent);
    		this.finish();
	    } else if (item.getItemId() == R.id.delete) {
	    	delete();
	    }
	    
	    return super.onMenuItemSelected(featureId, item);
	}
	
	//������ ȸ���� ������ �����Ѵ�.
	public void delete() {
		List<Integer> selectedCheckBox = gridAdapter.getSelectedCheckBox();
		int count = selectedCheckBox.size();
		if (count > 0) {
			//����Ȯ�� ���̾�α�
	    	openDialogDelete("������ ����ȸ ������ �����Ͻðڽ��ϱ�?");
		} else {
			openDialogAlert("���õ� ������ �����ϴ�.");
		}
	}
	
	public void deleteSelecedData() {
		//��ȸ���ǿ� ���� ������ ����Ѵ�.
    	StringBuffer strbuf = new StringBuffer();
    	StringBuffer urlbuf = new StringBuffer();
    	
    	strbuf.append("del_id=");
    	
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	
    	List<Integer> selectedCheckBox = gridAdapter.getSelectedCheckBox();
    	int count = selectedCheckBox.size();
    	
    	nameValuePairs.add(new BasicNameValuePair("delCount", count+""));
    	for (int i = 0; i < count; i++) {
    		strbuf.append(rowItems.get(selectedCheckBox.get(i)).get("meeting_cd") + "#");
    		nameValuePairs.add(new BasicNameValuePair("del_cd"+i, rowItems.get(selectedCheckBox.get(i)).get("meeting_cd")));
    	}
    	
		Log.i("parameter ====== " , strbuf.toString()); 
		
    	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_Del_Meeting.jsp");
    	
		//server connecting... login check...
		HttpConnectServer server = new HttpConnectServer();
		//server.sendByHttp(strbuf, urlbuf.toString());
		StringBuffer rtnValue = server.sendByHttpPost(urlbuf.toString(), nameValuePairs);
		
		Log.i("rtnValue ====== " , rtnValue.toString()); 
		
		setOrganListView(R.layout.hfmbactivity_003);
		_menu.clear();
		getMenuInflater().inflate(R.menu.main, _menu);
		
		//����ȸ�Ѵ�.
		flag = true;
		startThread();
	}
	
	@Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
		if (gridAdapter == null) {
			super.onBackPressed();
		} else if (gridAdapter.getLayoutResourceId() == R.layout.hfmbactivity_003) {
			super.onBackPressed();
		} else {
			setOrganListView(R.layout.hfmbactivity_003);
			_menu.clear();
			getMenuInflater().inflate(R.menu.main, _menu);
		}
    }
	
	public void openDialogAlert(String title) {
		//Ȯ�� ���̾�α�
		new AlertDialog.Builder(HfmbActivity003.this)
        .setTitle(title)
		.setPositiveButton( "Ȯ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		})
        .show();
	}
	
	//�����ϱ� ���� ���̾�α�
	public void openDialogDelete(String title) {
		//Ȯ�� ���̾�α�
		new AlertDialog.Builder(HfmbActivity003.this)
        .setTitle(title)
		.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		})
		.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				deleteSelecedData();
			}
		})
        .show();
	}
	
	//�����ϱ� ���� ���̾�α�
	public void openDialogModify(String title) {
		//Ȯ�� ���̾�α�
		new AlertDialog.Builder(HfmbActivity003.this)
        .setTitle(title)
		.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		})
		.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				modifySelecedData();
			}
		})
        .show();
	}
}

//����ȸ ����Ʈ �����Ѵ�.
class GridViewAdapter extends ArrayAdapter<HashMap<String,String>> {
	private Context context;
	private int layoutResourceId;
	private List<HashMap<String,String>> data;
	private boolean[] thumbnailsselection;
	private HttpConnectServer server;
	private int selectedPosition;
	/**
	 * @return the layoutResourceId
	 */
	public int getLayoutResourceId() {
		return layoutResourceId;
	}

	/**
	 * @param layoutResourceId the layoutResourceId to set
	 */
	public void setLayoutResourceId(int layoutResourceId) {
		this.layoutResourceId = layoutResourceId;
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

	public List<HashMap<String,String>> getData() {
		return data;
	}

	public void setData(List<HashMap<String,String>> data) {
		this.data = data;
		this.thumbnailsselection = new boolean[data.size()];
	}
	
	public boolean[] getThumbnailsselection() {
		return thumbnailsselection;
	}

	public void setThumbnailsselection(boolean[] thumbnailsselection) {
		this.thumbnailsselection = thumbnailsselection;
	}

	public GridViewAdapter(Context context, int layoutResourceId,
			List<HashMap<String,String>> data) {
		super(context, layoutResourceId, data);
      
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.thumbnailsselection = new boolean[data.size()];
		server = new HttpConnectServer();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
          
			holder.image = (ImageView) row.findViewById(R.id.icon1);
			holder.imageTitle1 = (TextView) row.findViewById(R.id.title1);
			holder.imageTitle2 = (TextView) row.findViewById(R.id.title2);
			holder.imageTitle3 = (TextView) row.findViewById(R.id.title3);
			holder.imageTitle4 = (TextView) row.findViewById(R.id.title4);
			
			if (layoutResourceId == R.layout.hfmbactivity_003_check) {
				holder.checkbox = (CheckBox) row.findViewById(R.id.checkbox);
			}
			
			row.setTag(holder);
      	} else {
      		holder = (ViewHolder) row.getTag();
      	}
		
		holder.image.setImageResource(R.drawable.logo_1);
		
		String photoStr = data.get(position).get("meeting_cd");
		if (photoStr != null && !photoStr.equals("")) {
			holder.imageUrl = "http://119.200.166.131:8054/JwyWebService/hfmbProWeb/photo/" + photoStr+".jpg";
			new ImageCallTask().execute(holder);
		} else {
			holder.image.setImageResource(R.drawable.logo_1);
		}
		
		String ceo_nm1 = data.get(position).get("ceo_nm1");
		String ceo_nm2 = data.get(position).get("ceo_nm2");
		
		if (ceo_nm1 == null || ceo_nm1.equals("null")) ceo_nm1 = "";
		if (ceo_nm2 == null || ceo_nm2.equals("null")) ceo_nm2 = "";
      	
      	holder.imageTitle1.setText(data.get(position).get("meeting_nm"));
      	holder.imageTitle2.setText("ȸ��  " + ceo_nm1);
      	holder.imageTitle3.setText("�ѹ�  " + ceo_nm2);
      	holder.imageTitle4.setText("ȸ����  " + data.get(position).get("company_count"));
      	
      	holder.image.setTag(R.string.position, position+"");
      	holder.image.setTag(R.string.meeting_cd, data.get(position).get("meeting_cd"));
      	holder.image.setTag(R.string.meeting_nm, data.get(position).get("meeting_nm"));
      	
      	//����ȸ ������ �繫�������� ����ȸ ȸ��, �ѹ��� ���������ϵ��� �Ѵ�.
  		if (DataUtil.insertYn == 1 || DataUtil.insertYn == 2) {
  			holder.image.setOnClickListener(new OnClickListener() {
  	            public void onClick(View v) {
  	                // TODO Auto-generated method stub
  	            	ImageView imgView = (ImageView) v;
  	                
  	            	String position = (String)imgView.getTag(R.string.position);
  	            	String meetingCd = (String)imgView.getTag(R.string.meeting_cd);
  	                String meetingNm = (String)imgView.getTag(R.string.meeting_nm);
  	                
  	                modifyMeeting(position, meetingCd, meetingNm);
  	            }
  	        });
  		}
		
      	if (layoutResourceId == R.layout.hfmbactivity_003_check) {
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
      	
      	return row;
	}
	
	public void modifyMeeting(String position, String meetingCd, String meetingNm) {
		Log.i("info", position + "-" + meetingCd + "-" + meetingNm);
		((HfmbActivity003)context).modifyMeetingPic(position, meetingCd, meetingNm);
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

	public class ViewHolder {
		TextView imageTitle1;
		TextView imageTitle2;
		TextView imageTitle3;
		TextView imageTitle4;
		ImageView image;
		CheckBox checkbox;
		
		Bitmap bm;
		String imageUrl;
		int position;
	}
	
	//�̹������� ��������.
	public class ImageCallTask extends AsyncTask<ViewHolder, Void, ViewHolder> {
		@Override
		protected ViewHolder doInBackground(ViewHolder... params) {
        	ViewHolder viewHolder = params[0];
            try {
            	viewHolder.bm = searchData(viewHolder.imageUrl);
            } catch (Exception e) {
                Log.e("net","����",e);
            }
            return viewHolder;
        }
		@Override
        protected void onPostExecute(ViewHolder result) {
        	if (result.bm == null) {
	            result.image.setImageResource(R.drawable.logo_1);
	        } else {
	            result.image.setImageBitmap(result.bm);
	        }
        }
	};
	//��ȸ�Ѵ�.
  	public Bitmap searchData(String imageUrl) {
    	return server.LoadImage(imageUrl);
  	}
}
