package com.spgr.dao;

import java.util.Map;

public interface MainDao {
	public String getToday();
	
	public int writeProc(Map<String, Object> paramMap);
}