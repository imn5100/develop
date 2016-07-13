package com.shaw.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.shaw.bo.ParkingCardType;
import com.shaw.mallmapper.RestshopReportStatisticsMapper;
import com.shaw.service.IParkingCardTypeService;
import com.shaw.utils.PropertiesUtil;
import com.shaw.utils.ResponseMap;

@Controller
public class ParkingController {

	@Autowired
	IParkingCardTypeService parkingCardTypeService;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	RestshopReportStatisticsMapper mapper;
	
	@RequestMapping("/getType")
	public ModelAndView getParkingCardType(HttpServletRequest request) {
		String name = "科大地面晚上月卡";
		ParkingCardType type2 = this.parkingCardTypeService.selectByTypeName(name);
		System.out.println(mapper.getById(1).getReportTime());
		ModelAndView mav = new ModelAndView();
		mav.setViewName("NewFile");
		mav.addObject("type", type2);
		return mav;
	}

	@RequestMapping("/getTypeJson")
	@ResponseBody
	public ResponseMap<ParkingCardType> getParkingCardType2(HttpServletRequest request) {
		String name = "科大地面晚上月卡";
		ResponseMap<ParkingCardType> returnMap = new ResponseMap<>();
		returnMap.setData(this.parkingCardTypeService.selectByTypeName(name));
		return returnMap;
	}

	@RequestMapping("/getOpenApiData")
	@ResponseBody
	public ResponseMap<String> getOpenApiData() {
		final String appId = PropertiesUtil.getConfiguration().getString("wechat.appid");
		final String appSecret = PropertiesUtil.getConfiguration().getString("wechat.appsecret");
		final String serverUrl = PropertiesUtil.getConfiguration().getString("wechat.getTokenUrl");
		ResponseMap<String> responseMap = new ResponseMap<String>();
		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "client_credential");
		params.add("appid", appId);
		params.add("secret", appSecret);
		String response = rest.postForObject(serverUrl, params, String.class);
		System.out.println(response);
		responseMap.setData(response);
		return responseMap;
	}

}
