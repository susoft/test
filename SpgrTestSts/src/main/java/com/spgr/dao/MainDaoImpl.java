package com.spgr.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.spgr.vo.HfmbInfoVo;
import com.spgr.vo.MeetingVo;

public class MainDaoImpl extends SqlSessionDaoSupport implements MainDao  {
	public String getToday() {
		System.out.println("MainDaoImpl getToday");
		
		String today  = (String) getSqlSession().selectOne("main.getToday");
		
		System.out.println("MainDaoImpl today = " + today);
		
		return today;
	}

	@Override
	public int writeProc(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
		return 0;
	}

	@Override
	public List<MeetingVo> getMeetingList() {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("main.getMeetingList");
	}

	@Override
	public List<HfmbInfoVo> getHfmbInfoList(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
		HfmbInfoVo hfmbInfoVo = new HfmbInfoVo();
		hfmbInfoVo.setMeetingCd(paramMap.get("meetingCd").toString());
		
		return getSqlSession().selectList("main.getHfmbInfoList", hfmbInfoVo);
	}

	@Override
	public int saveMeeting(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
		HfmbInfoVo hfmbInfoVo = new HfmbInfoVo();
		hfmbInfoVo.setMeetingNm(paramMap.get("meetingNm").toString());
		
		//존재여부 체크
		int count = getSqlSession().selectOne("main.getMeeting", hfmbInfoVo);
		
		if (count > 0) return -1;
		
		//meetingcd 채번
		String meetingCd = "";
		String returnStr = getSqlSession().selectOne("main.getNewMeetingCd");
		if (returnStr != null && !returnStr.equals("")) {
			meetingCd = String.valueOf(Integer.parseInt(returnStr) + 1);
      		for (int i = 0; i < 6; i++) {
      			if (meetingCd.length() == i) {
      				meetingCd = "0" + meetingCd;
      			}
      		}
      	} else {
      		meetingCd = "000001";//최초
      	}
		
		hfmbInfoVo.setMeetingCd(meetingCd);
		
		//시간 가져오기.
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyyMMdd HHmmss", Locale.KOREA );
		String timeStamp = formatter.format(new Date());
		String inputDt = timeStamp.substring(0, 8);
		String inputTm = timeStamp.substring(9);
		String updateDt = timeStamp.substring(0, 8);
		String updateTm = timeStamp.substring(9);
		
		String depthDivCd = "1";//교류회
		String delYn = "N";
		
		hfmbInfoVo.setInputDt(inputDt);
		hfmbInfoVo.setInputTm(inputTm);
		hfmbInfoVo.setUpdateDt(updateDt);
		hfmbInfoVo.setUpdateTm(updateTm);
		hfmbInfoVo.setDelYn(delYn);
		hfmbInfoVo.setDepthDivCd(depthDivCd);
		return 1;
		//return getSqlSession().insert("main.saveMeeting", hfmbInfoVo);
	}
}
