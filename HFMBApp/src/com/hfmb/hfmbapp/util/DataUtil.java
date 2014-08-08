package com.hfmb.hfmbapp.util;

public class DataUtil {
	public static boolean flag = false;
	public static String phoneNum;
	public static boolean searchYn;
	public static int insertYn;
	public static String meetingCd;
	public static String meetingNm;
	public static String ceoNm;
	
	public final static String temp_01 = "님 로그인";
	public final static int offsetAdd = 20;
	
	public final static String[] a13sArray = {"선택", "회원사명", "대표명", "업종명"};
	public final static String[] a13cdsArray = {"0", "1", "2", "3"};
	
	//서버에서 받은 결과정보를 hashmap 형태로 변환한다.
	public final static String[] jsonName = {"id", "meeting_cd", "ceo_nm", "company_cd", "company_nm"
			, "category_business_cd", "category_business_nm", "addr", "phone1", "phone2"
			, "phone3", "photo", "email", "meeting_nm", "depth_div_cd"
			, "hfmb_organ_div_cd", "hfmb_duty_div_cd", "auth_div_cd", "del_yn", "gita1"
			, "gita2", "gita3", "input_dt", "input_tm", "update_dt"
			, "update_tm"};	
	
	public final static String[] jsonNameResult = {"srch_gubun", "srch_nm", "count", "error", "error_nm"};
	
	public final static String[] jsonNameMeeting = {"meeting_cd", "meeting_nm", "ceo_nm1", "ceo_nm2", "company_count"};
	
	public static String checkNull(String str) {
		if (str == null || str.equals("") || str.equals("null")) return "";
		
		return str;
	}
}
