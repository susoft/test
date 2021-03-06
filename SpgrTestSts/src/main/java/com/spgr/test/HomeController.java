package com.spgr.test;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spgr.service.MainService;
import com.spgr.vo.CodeVo;
import com.spgr.vo.HfmbInfoVo;
import com.spgr.vo.MeetingVo;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	//테스트 App....
	@RequestMapping(value = "/hfmp_home.do", method = RequestMethod.GET)
	public String hfmp_home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "hfmp/hfmp_home";
	}
	
	//Search App....
	@RequestMapping(value = "/hfmp_0003.do", method = RequestMethod.GET)
	public String hfmp_0003(@RequestParam Map<String, Object> paramMap, ModelMap model) {
		logger.info("Welcome home! The client locale is {}.", "hfmp_0003.do");
		
		return "hfmp/MeetingList";
	}
	
	//Search App.... 
	@RequestMapping(value = "/getHfmp0003.do", method = RequestMethod.GET)
	public String getHfmp0003(@RequestParam Map<String, Object> paramMap, ModelMap model) {
		logger.info("Welcome home! The client locale is {}.", "getHfmp0003.do");
		
		List<MeetingVo> rtnList = mainService.getMeetingList();
		
		logger.info("{} count inputed....", rtnList.size());
		
		model.addAttribute("result", rtnList);
		
		return "hfmp/hfmp_0003";
	}
	
	//Search App....
	@RequestMapping(value = "/getHfmp0004.do", method = RequestMethod.POST)
	public String getHfmp0004(@RequestParam Map<String, Object> paramMap, ModelMap model) {
		logger.info("Welcome home! The client locale is {}.", "getHfmp0004.do");
		logger.info("meetingCd = {}", paramMap.get("meetingCd"));
		
		List<HfmbInfoVo> rtnList = mainService.getHfmbInfoList(paramMap);
		
		logger.info("{} count inputed....", rtnList.size());
		
		model.addAttribute("result", rtnList);
		
		return "hfmp/hfmp_0004";
	}
	
	//Search App....
	@RequestMapping(value = "/regiHfmp0005.do", method = RequestMethod.POST)
	public String regiHfmp0005(@RequestParam Map<String, Object> paramMap, ModelMap model) {
		logger.info("Welcome home! The client locale is {}.", "regiHfmp0005.do");
		return "hfmp/hfmp_0005";
	}
	
	//Search App....
	@RequestMapping(value = "/saveHfmp0005.do", method = RequestMethod.POST)
	public @ResponseBody Map<?,?> saveHfmp0005(@RequestParam Map<String, Object> paramMap, ModelMap model) {
		logger.info("Welcome home! The client locale is {}.", "saveHfmp0005.do");
		logger.info("meetingNm = {}", paramMap.get("meetingNm"));
		
		int result = mainService.saveMeeting(paramMap);
		
		logger.info("{} count inputed....", result);
		
		Map<String, String> mapinfo = new HashMap<String, String>();
		
		mapinfo.put("result", result+"");
		
		model.addAttribute("result", mapinfo);
		
		return mapinfo;
	}

	//Search App....
	@RequestMapping(value = "/regiHfmp0006.do", method = RequestMethod.POST)
	public String regiHfmp0006(@RequestParam Map<String, Object> paramMap, ModelMap model) {
		logger.info("Welcome home! The client locale is {}.", "regiHfmp0006.do");
		
		//코드조회
		List<CodeVo> rtnList = mainService.getCodeList(paramMap);
		model.addAttribute("result", rtnList);
		
		//교류회조회
		List<MeetingVo> rtnList1 = mainService.getMeetingList();
		model.addAttribute("result1", rtnList1);
		
		return "hfmp/hfmp_0006";
	}

	//Search App....
	@RequestMapping(value = "/saveHfmp0006.do", method = RequestMethod.POST)
	public @ResponseBody Map<?,?> saveHfmp0006(@RequestParam Map<String, Object> paramMap, ModelMap model) {
		logger.info("Welcome home! The client locale is {}.", "saveHfmp0006.do");
		logger.info("companyNm = {}", paramMap.get("companyNm"));
		logger.info("ceoNm = {}", paramMap.get("ceoNm"));
		
		int result = mainService.saveCompany(paramMap);
		
		logger.info("{} count inputed....", result);
		
		Map<String, String> mapinfo = new HashMap<String, String>();
		
		mapinfo.put("result", result+"");
		
		model.addAttribute("result", mapinfo);
		
		return mapinfo;
	}
	
	//Search App....
	@RequestMapping(value = "/modifyMeeting.do", method = RequestMethod.POST)
	public @ResponseBody Map<?,?> modifyMeeting(@RequestParam Map<String, Object> paramMap, ModelMap model) {
		logger.info("Welcome home! The client locale is {}.", "modifyMeeting.do");
		logger.info("meetingNm = {}", paramMap.get("meetingNm"));
		logger.info("meetingCd = {}", paramMap.get("meetingCd"));
		
		int result = mainService.modifyMeeting(paramMap);
		
		logger.info("{} count inputed....", result);
		
		Map<String, String> mapinfo = new HashMap<String, String>();
		
		mapinfo.put("result", result+"");
		
		model.addAttribute("result", mapinfo);
		
		return mapinfo;
	}

	//Search App....
	@RequestMapping(value = "/deleteMeeting.do", method = RequestMethod.POST)
	public @ResponseBody Map<?,?> deleteMeeting(@RequestParam Map<String, Object> paramMap, ModelMap model) {
		logger.info("Welcome home! The client locale is {}.", "deleteMeeting.do");
		logger.info("meetingNm = {}", paramMap.get("meetingNm"));
		logger.info("meetingCd = {}", paramMap.get("meetingCd"));
		
		int result = mainService.deleteMeeting(paramMap);
		
		logger.info("{} count inputed....", result);
		
		Map<String, String> mapinfo = new HashMap<String, String>();
		
		mapinfo.put("result", result+"");
		
		model.addAttribute("result", mapinfo);
		
		return mapinfo;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	///////Test....////////////////////////////////////////////
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "test11";
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/test.do", method = RequestMethod.GET)
	public String test(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "html5_start";
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/test1.do", method = RequestMethod.GET)
	public @ResponseBody Map<?,?> test1(@RequestParam Map<String, Object> paramMap, ModelMap model) {
		
		Map<String, String> mapinfo = new HashMap<String, String>();
		
		mapinfo.put("test1", "value1");
		mapinfo.put("test2", "value2");
		mapinfo.put("test3", "value3");
		
		model.addAttribute("result", mapinfo);
		
		System.out.println("test");
		
		return model;
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/testAjax.do", method = RequestMethod.GET)
	public String testAjax(@RequestParam Map<String, Object> paramMap, ModelMap model) {
		return "testAjax";
	}
	
	@Autowired
	private MainService mainService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/testDb.do", method = RequestMethod.GET)
	public @ResponseBody Map<?,?> testDb(@RequestParam Map<String, Object> paramMap, ModelMap model) {
		model.put("test", mainService.getToday());
		return model;
	} 
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/writeProc.do", method = RequestMethod.GET)
	public ModelAndView writeProc(@RequestParam Map<String, Object> paramMap, ModelMap model) {
		
		paramMap.put("subject", "test Subject");
		paramMap.put("context", "Test Context.......");
		
		int writeCnt = mainService.writeProc(paramMap);
		
		System.out.println(writeCnt + "건 입력되었습니다.");
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/testDb.do");
		
		return mav;
	}
	//////Test..../////////////////////////////////////////////
	
}
