package com.shaw.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.PieChartData;
import com.shaw.service.impl.ChartBuildService;
import com.shaw.utils.ResponseMap;

@Controller
public class ChartController {
	@Autowired
	ChartBuildService chartBuildService;

	@RequestMapping(value = "/getData", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseMap<PieChartData> getData(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResponseMap<PieChartData> result = new ResponseMap<PieChartData>();
		List<String> titles = new ArrayList<String>();
		titles.add("指标1");
		titles.add("指标2");
		titles.add("指标3");
		titles.add("指标4");
		titles.add("指标5");
		titles.add("指标6");
		PieChartData data = chartBuildService.getFinalData(titles);
		result.setData(data);
		return result;
	}

	@RequestMapping(value = "/getDataView", method = { RequestMethod.GET })
	public ModelAndView getDataView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		List<String> titles = new ArrayList<String>();
		titles.add("指标1");
		titles.add("指标2");
		titles.add("指标3");
		titles.add("指标4");
		titles.add("指标5");
		titles.add("指标6");
		PieChartData data = chartBuildService.getFinalData(titles);
		mav.setViewName("charts");
		// 前端js无法直接操作java对象  这里将对象转化为json串，再在前端转为javascript object 使用
		mav.addObject("data", JSONObject.toJSONString(data));
		return mav;
	}
}
