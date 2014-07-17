package com.spgr.service;

import java.util.List;
import java.util.Map;

import com.spgr.vo.HfmbInfoVo;
import com.spgr.vo.MeetingVo;

public interface MainService {
	public String getToday();
	
	public int writeProc(Map<String, Object> paramMap);
	
	public List<MeetingVo> getMeetingList();
	
	public List<HfmbInfoVo> getHfmbInfoList(Map<String, Object> paramMap);
	
	public int saveMeeting(Map<String, Object> paramMap);
	
}
