package com.spgr.service;

import java.util.List;
import java.util.Map;

import com.spgr.dao.MainDao;
import com.spgr.vo.HfmbInfoVo;
import com.spgr.vo.MeetingVo;

public class MainServiceImpl implements MainService {

	private MainDao mainDao;
	
	public void setMainDao(MainDao mainDao) {
		System.out.println("MainServiceImpl setMainDao");
		this.mainDao = mainDao;
	}
	
	@Override
	public String getToday() {
		// TODO Auto-generated method stub
		System.out.println("MainServiceImpl getToday");
		return mainDao.getToday();
	}

	@Override
	public int writeProc(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
		return mainDao.writeProc(paramMap);
	}

	@Override
	public List<MeetingVo> getMeetingList() {
		// TODO Auto-generated method stub
		return mainDao.getMeetingList();
	}

	@Override
	public List<HfmbInfoVo> getHfmbInfoList(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return mainDao.getHfmbInfoList(paramMap);
	}

	@Override
	public int saveMeeting(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return mainDao.saveMeeting(paramMap);
	}

}
