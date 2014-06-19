package com.spgr.service;

import java.util.Map;

public interface MainService {
	public String getToday();
	
	public int writeProc(Map<String, Object> paramMap);
}
