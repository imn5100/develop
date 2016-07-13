package com.shaw.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shaw.service.impl.WxServiceImpl;
import com.shaw.utils.ResponseMap;

@Controller
public class WxController {
	@Autowired
	WxServiceImpl wxService;

	/**
	 * 微信验证,接收消息，回复消息入口
	 */
	@RequestMapping(value = "/msgAccept", method = { RequestMethod.GET, RequestMethod.POST })
	public String validWx(@RequestParam(value = "signature", required = false) String signature,
			@RequestParam(value = "timestamp", required = false) String timestamp,
			@RequestParam(value = "nonce", required = false) String nonce,
			@RequestParam(value = "echostr", required = false) String echostr, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isGet = request.getMethod().toLowerCase().equals("get");
		if (isGet) {
			wxService.verifyURL(signature, timestamp, nonce, echostr, request, response);
		} else {
			wxService.acceptMessage(request, response);
		}
		return null;
	}

	@RequestMapping(value = "/tokenTest", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResponseMap<String> getTokenTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResponseMap<String> result = new ResponseMap<String>();
		result.setData(wxService.getToken());
		return result;
	}
}
