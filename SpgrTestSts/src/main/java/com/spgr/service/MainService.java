package com.spgr.service;

import java.util.List;
import java.util.Map;

public interface MainService {
	public String getToday();
	
	public int writeProc(Map<String, Object> paramMap);
	
	public List<Map<String, String>> getMeetingList();
}
