package com.spgr.service;

import java.util.List;
import java.util.Map;

import com.spgr.dto.TestDto;

public interface MainService {
	public String getToday();
	
	public int writeProc(Map<String, Object> paramMap);
	
	public List<TestDto> getMeetingList();
}
