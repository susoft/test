package com.spgr.dao;

import java.util.List;
import java.util.Map;

import com.spgr.dto.TestDto;

public interface MainDao {
	public String getToday();
	
	public int writeProc(Map<String, Object> paramMap);
	
	public List<TestDto> getMeetingList();
}
