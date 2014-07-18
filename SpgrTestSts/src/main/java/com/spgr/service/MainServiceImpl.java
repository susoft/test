package com.spgr.service;

import java.util.List;
import java.util.Map;

import com.spgr.dao.MainDao;
import com.spgr.vo.CodeVo;
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

	@Override
	public List<CodeVo> getCodeList(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return mainDao.getCodeList(paramMap);
	}

	@Override
	public int saveCompany(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return mainDao.saveCompany(paramMap);
	}

	@Override
	public int modifyMeeting(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return mainDao.modifyMeeting(paramMap);
	}

	@Override
	public int deleteMeeting(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return mainDao.deleteMeeting(paramMap);
	}

}
