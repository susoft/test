package com.spgr.test;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
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

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
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
