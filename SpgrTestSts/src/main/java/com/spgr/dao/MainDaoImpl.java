package com.spgr.dao;

import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

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
}
