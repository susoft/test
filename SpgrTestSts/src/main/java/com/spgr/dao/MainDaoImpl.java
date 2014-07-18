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
		
		//���翩�� üũ
		int count = getSqlSession().selectOne("main.getMeeting", hfmbInfoVo);
		
		if (count > 0) return -1;
		
		//meetingcd ä��
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
      		meetingCd = "000001";//����
      	}
		
		String depthDivCd = "1";//����ȸ
		String delYn = "N";
		
		//�ð� ��������.
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
		
		//���翩�� üũ
		int count = getSqlSession().selectOne("main.getCompany", hfmbInfoVo);
		
		if (count > 0) return -1;
		
		//companycd ä��
		String returnStr = getSqlSession().selectOne("main.getLastCompanyCd");
		if (returnStr != null && !returnStr.equals("")) {
			companyCd = String.valueOf(Integer.parseInt(returnStr) + 1);
      		for (int i = 0; i < 7; i++) {
      			if (companyCd.length() == i) {
      				companyCd = "0" + companyCd;
      			}
      		}
      	} else {
      		companyCd = "0000001";//����
      	}
		
		String depthDivCd = "2";//ȸ����
		String delYn = "N";
		
		//�ð� ��������.
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
		
		hfmbInfoVo.setMeetingCd(meetingCd);     // ����ȸ�ڵ�
		hfmbInfoVo.setCeoNm(checkValue(paramMap.get("ceoNm")));     // ȸ���ǥ��
		hfmbInfoVo.setCompanyCd(companyCd);     // ȸ���ڵ�
		hfmbInfoVo.setCompanyNm(companyNm);     // ȸ���ڵ��
		hfmbInfoVo.setCategoryBusinessCd(checkValue(paramMap.get("categoryBusinessCd")));     // �����ڵ�
		hfmbInfoVo.setCategoryBusinessNm(checkValue(paramMap.get("categoryBusinessNm")));     // �����ڵ��
		hfmbInfoVo.setAddr(checkValue(paramMap.get("addr")));     // �ּ�
		hfmbInfoVo.setPhone1(checkValue(paramMap.get("phone1")));     // �Ϲ���ȭ
		hfmbInfoVo.setPhone2(checkValue(paramMap.get("phone2")));     // �ڵ���
		hfmbInfoVo.setPhone3(checkValue(paramMap.get("phone3")));     // �ѽ���ȣ
		//hfmbInfoVo.setPhoto(checkValue(paramMap.get("photo")));     // ����
		hfmbInfoVo.setEmail(checkValue(paramMap.get("meetingNm")));     // �̸���
		hfmbInfoVo.setMeetingNm(meetingNm);     // ����ȸ��
		hfmbInfoVo.setDepthDivCd(depthDivCd);     // ����ȸȸ���籸���ڵ�
		hfmbInfoVo.setHfmbOrganDivCd(hfmbOrganDivCd);     // ����ȸ���������ڵ�
		hfmbInfoVo.setHfmbDutyDivCd(hfmbDutyDivCd);    // ����ȸ��å�����ڵ�
		hfmbInfoVo.setAuthDivCd(authDivCd);     // ���ѱ����ڵ�
		hfmbInfoVo.setDelYn(delYn);     // ��������
		hfmbInfoVo.setGita1(checkValue(paramMap.get("gita1")));     // ��Ÿ1
		hfmbInfoVo.setGita2(checkValue(paramMap.get("gita2")));     // ��Ÿ2
		hfmbInfoVo.setGita3(checkValue(paramMap.get("gita3")));     // ��Ÿ3
		hfmbInfoVo.setInputDt(inputDt);     // �Է�����
		hfmbInfoVo.setInputTm(inputTm);     // �Է½ð�
		hfmbInfoVo.setUpdateDt(updateDt);     // ��������
		hfmbInfoVo.setUpdateTm(updateTm);     // �����ð�
		
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
