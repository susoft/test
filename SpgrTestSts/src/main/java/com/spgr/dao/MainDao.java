package com.spgr.dao;

import java.util.List;
import java.util.Map;

public interface MainDao {
	public String getToday();
	
	public int writeProc(Map<String, Object> paramMap);
	
	public List<Map<String, String>> getMeetingList();
}
