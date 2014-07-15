package com.spgr.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.spgr.dto.TestDto;

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
		
		return getSqlSession().insert("main.writeProc", paramMap);
	}

	@Override
	public List<TestDto> getMeetingList() {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("main.getMeetingList");
	}
}
