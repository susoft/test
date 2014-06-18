package com.ntpbm.ntpbmapp;

public class RowItem {

	private int imageId;     
	private String title;     
	private String desc;
	private String chkinfo;
	
	private String brandNm;
	private String modelNm;
	private String suruong;
	private String danga;
	
	
	public RowItem(int imageId, String title, String desc, String chkinfo) {         
		this.imageId = imageId;         
		this.title = title;         
		this.desc = desc;     
		this.chkinfo = chkinfo;
	}     
	
	public RowItem(int imageId, String brandNm, String modelNm, String suruong, String danga) {         
		this.imageId = imageId;         
		this.brandNm = brandNm;         
		this.modelNm = modelNm;     
		this.suruong = suruong;
		this.danga = danga;
	}
	
	public String getBrandNm() {
		return brandNm;
	}

	public void setBrandNm(String brandNm) {
		this.brandNm = brandNm;
	}

	public String getModelNm() {
		return modelNm;
	}

	public void setModelNm(String modelNm) {
		this.modelNm = modelNm;
	}

	public String getSuruong() {
		return suruong;
	}

	public void setSuruong(String suruong) {
		this.suruong = suruong;
	}

	public String getDanga() {
		return danga;
	}

	public void setDanga(String danga) {
		this.danga = danga;
	}

	public int getImageId() {         
		return imageId;     
	}     
	
	public void setImageId(int imageId) {         this.imageId = imageId;     }     
	public String getDesc() {         return desc;     }     
	public void setDesc(String desc) {         this.desc = desc;     }     
	public String getTitle() {         return title;     }     
	public void setTitle(String title) {         this.title = title;     }     
	public String getChkinfo() {         return chkinfo;     }     
	public void setChkinfo(String chkinfo) {         this.chkinfo = chkinfo;     }
	
	@Override    
	public String toString() {         return title + "\n" + desc;     }
}
