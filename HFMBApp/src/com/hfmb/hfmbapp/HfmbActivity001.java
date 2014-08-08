package com.hfmb.hfmbapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.hfmb.hfmbapp.util.CommonUtil;
import com.hfmb.hfmbapp.util.DataUtil;
import com.hfmb.hfmbapp.util.HttpConnectServer;

public class HfmbActivity001 extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    public static HfmbListAdapter listAdapter;
    
    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hfmb_001);
        
        HashMap<String, String> submenuMap = new HashMap<String, String>();
        submenuMap.put("1", "찾아오시는길");
        submenuMap.put("2", "중소기업융합이란");
        submenuMap.put("3", "회장인사말");
        submenuMap.put("4", "사무국직원");

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(), submenuMap);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        
        //사무국직원 조회하기. - 회원사만 조회 가능하도록...
        if (DataUtil.searchYn) {
	        mTask.execute();
        }
        listAdapter = new HfmbListAdapter(this, new ArrayList<HashMap<String, String>>(), R.layout.hfmbactivity_listview);
        
        // Show the Up button in the action bar.
     	setupActionBar();
    }
    
    public static List<HashMap<String, String>> rowItems_001;
    
    //사무국직원 조회하기.
	private AsyncTask<Void, Void, Boolean> mTask = new AsyncTask<Void, Void, Boolean>() {
        protected Boolean doInBackground(Void... p) {
            try {
            	searchData();
            } catch (Exception e) {
                Log.e("doInBackground","오류",e);
            }
            return true;
        }
        protected void onPostExecute(Boolean flag) {
        	//Log.i("onPostExecute", "mTask.execute end......");
        }
	};
	
	//사무국정보를 조회한다.
  	public void searchData() {
  		rowItems_001 = null;
  		
      	//조회조건에 따라서 서버와 통신한다.
      	StringBuffer strbuf = new StringBuffer();
      	StringBuffer urlbuf = new StringBuffer();
      	
      	strbuf.append("srch_gubun=0");
  		
  		Log.i("HfmbActivity001" , strbuf.toString()); 
  		
      	urlbuf.append("http://119.200.166.131:8054/JwyWebService/hfmbProWeb/jwy_Hfmb_0011.jsp");
      	
  		//server connecting... login check...
  		HttpConnectServer server = new HttpConnectServer();
  		StringBuffer resultInfo = server.sendByHttp(strbuf, urlbuf.toString());
  		
  		Log.i("HfmbActivity001", resultInfo.toString());
  		
  		rowItems_001 = server.jsonParserList(resultInfo.toString(), DataUtil.jsonName);
  		
  		if (rowItems_001 != null) {
  			if (rowItems_001.size() > 0) {
  			}
  		}
  	}

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

    	private HashMap<String, String> submenuMap;
        public AppSectionsPagerAdapter(FragmentManager fm, HashMap<String, String> submenuMap) {
            super(fm);
            this.submenuMap = submenuMap;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    //return new LaunchpadSectionFragment();

                	// The other sections of the app are dummy placeholders.
                    Fragment fragment1 = new DummySectionFragment();
                    
                    Bundle args1 = new Bundle();
                    args1.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    args1.putString(DummySectionFragment.ARG_SECTION_NM, getPageTitle(i).toString());
                    
                    fragment1.setArguments(args1);
                    return fragment1;
                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new DummySectionFragment();
                    
                    Bundle args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    args.putString(DummySectionFragment.ARG_SECTION_NM, getPageTitle(i).toString());
                    
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return submenuMap.keySet().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return submenuMap.get(submenuMap.keySet().toArray()[position].toString());
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class LaunchpadSectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);

            // Demonstration of a collection-browsing activity.
            rootView.findViewById(R.id.demo_collection_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), CollectionDemoActivity.class);
                            startActivity(intent);
                        }
                    });

            // Demonstration of navigating to external activities.
            rootView.findViewById(R.id.demo_external_activity)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Create an intent that asks the user to pick a photo, but using
                            // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
                            // the application from the device home screen does not return
                            // to the external activity.
                            Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
                            externalActivityIntent.setType("image/*");
                            externalActivityIntent.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            startActivity(externalActivityIntent);
                        }
                    });

            return rootView;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";
        public static final String ARG_SECTION_NM = "section_nm";
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	
        	Bundle args = getArguments();
        	int page = args.getInt(ARG_SECTION_NUMBER);
        	View rootView;
        	if (page == 2) {
        		//중소기업융합이란
        		rootView = setSecondView(inflater, container, savedInstanceState, args);
        	} else if (page == 3) {
        		//찾아오시는길
        		rootView = setThirdView(inflater, container, savedInstanceState, args);
        	} else if (page == 4) {
        		//사무국직원
        		rootView = setForthView(inflater, container, savedInstanceState, args);
        	} else {
        		//회장인사말
        		rootView = setFirstView(inflater, container, savedInstanceState, args);
        	}
        	
            return rootView;
        }
        
        //회장인사말
        private View setFirstView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState, Bundle args) {
        	
        	View rootView = inflater.inflate(R.layout.hfmbactivity_001_01, container, false);
            
//        	ImageView imageView = (ImageView) rootView.findViewById(R.id.imageview01);
//        	imageView.setOnLongClickListener(mOnLongClickListener);
        	
        	ImageView btnGlasses = (ImageView) rootView.findViewById(R.id.btnGlasses01);
        	btnGlasses.setOnClickListener(mOnClickListener);
        	btnGlasses.setOnTouchListener(CommonUtil.imgbtnTouchListener);
            
            return rootView;
        }
        
        //중소기업융합이란
        private View setSecondView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState, Bundle args) {
        	
        	View rootView = inflater.inflate(R.layout.hfmbactivity_001_02, container, false);
            
//        	ImageView imageView = (ImageView) rootView.findViewById(R.id.imageview02);
//        	imageView.setOnLongClickListener(mOnLongClickListener);
        	
        	ImageView btnGlasses = (ImageView) rootView.findViewById(R.id.btnGlasses02);
        	btnGlasses.setOnClickListener(mOnClickListener);
        	btnGlasses.setOnTouchListener(CommonUtil.imgbtnTouchListener);
        	
            return rootView;
        }
        
        //찾아오시는길
        private View setThirdView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState, Bundle args) {
        	
        	View rootView = inflater.inflate(R.layout.hfmbactivity_001_03, container, false);
            
//        	ImageView imageView = (ImageView) rootView.findViewById(R.id.imageview03);
//        	imageView.setOnLongClickListener(mOnLongClickListener);
        	
        	ImageView btnGlasses = (ImageView) rootView.findViewById(R.id.btnGlasses03);
        	btnGlasses.setOnClickListener(mOnClickListener);
        	btnGlasses.setOnTouchListener(CommonUtil.imgbtnTouchListener);
        	
            return rootView;
        }
        
        //사무국직원
        private View setForthView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState, Bundle args) {
        	//Log.i("DummySectionFragment", "setForthView 사무국직원");
        	
        	View rootView = inflater.inflate(R.layout.hfmbactivity_001_04, container, false);
        	
        	ListView listView = (ListView) rootView.findViewById(R.id.list);
        	listView.setAdapter(listAdapter);
        	
        	if (rowItems_001 != null) {
	        	listAdapter.setRowItems(rowItems_001);
	        	listAdapter.notifyDataSetChanged();
        	}
    		
            //((ImageView) rootView.findViewById(android.R.id.imageview1)).setText(args.getString(ARG_SECTION_NM));
            
            return rootView;
        }
        
    	private OnClickListener mOnClickListener = new OnClickListener() {
    		@Override    
    		public void onClick(View view) {
    			int index = 0;
    			int display = 1;
    			int id = view.getId();
				if (id == R.id.btnGlasses01) {
                	index = 1;
                	display = 1;
				} else if (id == R.id.btnGlasses02) {
                	index = 2;
                	display = 2;
				} else if (id == R.id.btnGlasses03) {
                	index = 3;
                	display = 3;
				}
    			goZoom(display, index);
    	    }
    	};
    	
    	public void goZoom(int display, int index) {
    		Intent intent = new Intent( this.getActivity(), HfmbActivityZoom.class);
    		intent.putExtra("display", display);
	    	intent.putExtra("index", index);
    		startActivity(intent);
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
			
			ab.setTitle("연합회소개");
			ab.setSubtitle(DataUtil.ceoNm + DataUtil.temp_01);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (DataUtil.searchYn) {
			getMenuInflater().inflate(R.menu.main, menu);
        }
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
	    }
	    
	    return super.onMenuItemSelected(featureId, item);
	}
	
}
