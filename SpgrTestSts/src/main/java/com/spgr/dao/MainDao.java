package com.spgr.dao;

import java.util.List;
import java.util.Map;

import com.spgr.vo.CodeVo;
import com.spgr.vo.HfmbInfoVo;
import com.spgr.vo.MeetingVo;

public interface MainDao {
	public String getToday();
	
	public int writeProc(Map<String, Object> paramMap);
	
	public List<MeetingVo> getMeetingList();
	
	public List<HfmbInfoVo> getHfmbInfoList(Map<String, Object> paramMap);
	
	public int saveMeeting(Map<String, Object> paramMap);
	
	public List<CodeVo> getCodeList(Map<String, Object> paramMap);
	
	public int saveCompany(Map<String, Object> paramMap);
	
	public int modifyMeeting(Map<String, Object> paramMap);
	
	public int deleteMeeting(Map<String, Object> paramMap);
}
