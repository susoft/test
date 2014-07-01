package com.hfmb.hfmbapp.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String> {
	
	private Context context;
	private String[] items1;
	private String[] itemCds = new String[] {};
    private String selectedItem;
    private String selectedItemCd;

	public SpinnerAdapter(final Context context, final int textViewResourceId, final String[] objects) {
        super(context, textViewResourceId, objects);
        this.items1 = objects;
        this.context = context;
    }
 
    /**
     * 스피너 클릭시 보여지는 View의 정의
     */
    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
 
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
 
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(items1[position]);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(18);
        
        return convertView;
    }
 
    /**
     * 기본 스피너 View 정의
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
 
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(items1[position]);
        tv.setTextColor(Color.BLUE);
        tv.setTextSize(12);
        
        return convertView;
    }
    
    @Override
    public String getItem(int position) {
    	return items1[position];
    }
    
    public int getPosition(String selectId) {
    	for (int i = 0; i < itemCds.length; i++) {
    		if (selectId.equals(itemCds[i])) return i;
    	}
    	return -1;
    }
    
    /**
	 * @return the selectedItem
	 */
	public String getSelectedItem() {
		return selectedItem;
	}

	/**
	 * @param selectedItem the selectedItem to set
	 */
	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}
	
	
	/**
	 * @return the selectedItemCd
	 */
	public String getSelectedItemCd() {
		return selectedItemCd;
	}

	/**
	 * @param selectedItemCd the selectedItemCd to set
	 */
	public void setSelectedItemCd(String selectedItemCd) {
		this.selectedItemCd = selectedItemCd;
	}

	/**
	 * @return the itemCds
	 */
	public String[] getItemCds() {
		return itemCds;
	}

	/**
	 * @param itemCds the itemCds to set
	 */
	public void setItemCds(String[] itemCds) {
		this.itemCds = itemCds;
	}

	/**
	 * @return the items
	 */
	public String[] getItems() {
		return items1;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(String[] items) {
		this.items1 = items;
	}

}
