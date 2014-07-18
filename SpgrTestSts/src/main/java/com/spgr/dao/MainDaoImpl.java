package com.spgr.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.spgr.vo.CodeVo;
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
		hfmbInfoVo.setMeetingNm(checkValue(paramMap.get("meetingNm")));
		
		//존재여부 체크
		int count = getSqlSession().selectOne("main.getMeeting", hfmbInfoVo);
		
		if (count > 0) return -1;
		
		//meetingcd 채번
		String meetingCd = "";
		String returnStr = getSqlSession().selectOne("main.getLastMeetingCd");
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
		
		String depthDivCd = "1";//교류회
		String delYn = "N";
		
		//시간 가져오기.
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyyMMdd HHmmss", Locale.KOREA );
		String timeStamp = formatter.format(new Date());
		String inputDt = timeStamp.substring(0, 8);
		String inputTm = timeStamp.substring(9);
		String updateDt = timeStamp.substring(0, 8);
		String updateTm = timeStamp.substring(9);
		
		hfmbInfoVo.setMeetingCd(meetingCd);
		hfmbInfoVo.setCompanyCd(" ");
		hfmbInfoVo.setInputDt(inputDt);
		hfmbInfoVo.setInputTm(inputTm);
		hfmbInfoVo.setUpdateDt(updateDt);
		hfmbInfoVo.setUpdateTm(updateTm);
		hfmbInfoVo.setDelYn(delYn);
		hfmbInfoVo.setDepthDivCd(depthDivCd);
		
		return getSqlSession().insert("main.insertMemberCompany", hfmbInfoVo);
	}

	@Override
	public List<CodeVo> getCodeList(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
		Object codeKey = paramMap.get("codeKey");
		
		CodeVo codeVo = new CodeVo();
		
		if (codeKey == null ) codeVo.setCodeKey("");
		else codeVo.setCodeKey(codeKey.toString());
		
		return getSqlSession().selectList("main.getCodeList", codeVo);
	}
	
	@Override
	public int saveCompany(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
		String companyCd = checkValue(paramMap.get("companyCd"));
		String companyNm = checkValue(paramMap.get("companyNm"));
		String meetingCd = checkValue(paramMap.get("meetingCd"));
		String meetingNm = checkValue(paramMap.get("meetingNm"));
		
		HfmbInfoVo hfmbInfoVo = new HfmbInfoVo();
		hfmbInfoVo.setMeetingCd(meetingCd);
		hfmbInfoVo.setCompanyNm(companyNm);
		
		//존재여부 체크
		int count = getSqlSession().selectOne("main.getCompany", hfmbInfoVo);
		
		if (count > 0) return -1;
		
		//companycd 채번
		String returnStr = getSqlSession().selectOne("main.getLastCompanyCd");
		if (returnStr != null && !returnStr.equals("")) {
			companyCd = String.valueOf(Integer.parseInt(returnStr) + 1);
      		for (int i = 0; i < 7; i++) {
      			if (companyCd.length() == i) {
      				companyCd = "0" + companyCd;
      			}
      		}
      	} else {
      		companyCd = "0000001";//최초
      	}
		
		String depthDivCd = "2";//회원사
		String delYn = "N";
		
		//시간 가져오기.
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyyMMdd HHmmss", Locale.KOREA );
		String timeStamp = formatter.format(new Date());
		String inputDt = timeStamp.substring(0, 8);
		String inputTm = timeStamp.substring(9);
		String updateDt = timeStamp.substring(0, 8);
		String updateTm = timeStamp.substring(9);
		
		//data setting...
		
		String hfmbOrganDivCd = checkValue(paramMap.get("hfmbOrganDivCd"));
		String hfmbDutyDivCd = checkValue(paramMap.get("hfmbDutyDivCd"));
		String authDivCd = "03";
		
		hfmbInfoVo.setMeetingCd(meetingCd);     // 교류회코드
		hfmbInfoVo.setCeoNm(checkValue(paramMap.get("ceoNm")));     // 회사대표명
		hfmbInfoVo.setCompanyCd(companyCd);     // 회사코드
		hfmbInfoVo.setCompanyNm(companyNm);     // 회사코드명
		hfmbInfoVo.setCategoryBusinessCd(checkValue(paramMap.get("categoryBusinessCd")));     // 업종코드
		hfmbInfoVo.setCategoryBusinessNm(checkValue(paramMap.get("categoryBusinessNm")));     // 업종코드명
		hfmbInfoVo.setAddr(checkValue(paramMap.get("addr")));     // 주소
		hfmbInfoVo.setPhone1(checkValue(paramMap.get("phone1")));     // 일반전화
		hfmbInfoVo.setPhone2(checkValue(paramMap.get("phone2")));     // 핸드폰
		hfmbInfoVo.setPhone3(checkValue(paramMap.get("phone3")));     // 팩스번호
		//hfmbInfoVo.setPhoto(checkValue(paramMap.get("photo")));     // 사진
		hfmbInfoVo.setEmail(checkValue(paramMap.get("meetingNm")));     // 이메일
		hfmbInfoVo.setMeetingNm(meetingNm);     // 교류회명
		hfmbInfoVo.setDepthDivCd(depthDivCd);     // 교류회회원사구분코드
		hfmbInfoVo.setHfmbOrganDivCd(hfmbOrganDivCd);     // 교류회조직구분코드
		hfmbInfoVo.setHfmbDutyDivCd(hfmbDutyDivCd);    // 교류회직책구분코드
		hfmbInfoVo.setAuthDivCd(authDivCd);     // 권한구분코드
		hfmbInfoVo.setDelYn(delYn);     // 삭제여부
		hfmbInfoVo.setGita1(checkValue(paramMap.get("gita1")));     // 기타1
		hfmbInfoVo.setGita2(checkValue(paramMap.get("gita2")));     // 기타2
		hfmbInfoVo.setGita3(checkValue(paramMap.get("gita3")));     // 기타3
		hfmbInfoVo.setInputDt(inputDt);     // 입력일자
		hfmbInfoVo.setInputTm(inputTm);     // 입력시간
		hfmbInfoVo.setUpdateDt(updateDt);     // 수정일자
		hfmbInfoVo.setUpdateTm(updateTm);     // 수정시간
		
		return getSqlSession().insert("main.insertMemberCompany", hfmbInfoVo);
	}
	
	public String checkValue(Object obValue) {
		if (obValue == null) return "";
		return obValue.toString();
	}

	@Override
	public int modifyMeeting(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
		String meetingCd = checkValue(paramMap.get("meetingCd"));
		String meetingNm = checkValue(paramMap.get("meetingNm"));
		
		HfmbInfoVo hfmbInfoVo = new HfmbInfoVo();
		hfmbInfoVo.setMeetingCd(meetingCd);
		hfmbInfoVo.setCompanyNm(meetingNm);
		
		return getSqlSession().update("main.modifyMeeting", hfmbInfoVo);
	}

	@Override
	public int deleteMeeting(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
		String meetingCd = checkValue(paramMap.get("meetingCd"));
		
		HfmbInfoVo hfmbInfoVo = new HfmbInfoVo();
		hfmbInfoVo.setMeetingCd(meetingCd);
		
		return getSqlSession().update("main.deleteMeeting", hfmbInfoVo);
	}
}
