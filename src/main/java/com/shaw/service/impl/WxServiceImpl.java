package com.shaw.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.ParkingCardType;
import com.shaw.common.CacheConstants;
import com.shaw.service.IParkingCardTypeService;
import com.shaw.utils.ParamVO;
import com.shaw.utils.PropertiesUtil;
import com.shaw.utils.TimeUtils;
import com.shaw.utils.XmlUtils;
import com.shaw.utils.wx.aes.SHA1;
import com.shaw.utils.wx.aes.WXBizMsgCrypt;

@Service
public class WxServiceImpl {
	@Value("#{ config['wechat.verify.token'] }")
	private String TOKEN;
	@Value("#{ config['wechat.encodingAesKey'] }")
	private String ENCODINGAESKEY;
	@Value("#{ config['wechat.appid'] }")
	private String APPID;
	@Value("#{ config['wechat.send.msg.url'] }")
	private String SEND_MSG_RUL;
	@Value("#{ config['wechat.appsecret'] }")
	private String SECRET;

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	private AIService aiService;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private IParkingCardTypeService parkingCardTypeService;
	// @Autowired
	// private GuavaCacheUtils guavaCacheUtils;

	public String getUserOpenId() {
		return null;
	}

	public void verifyURL(String signature, String timestamp, String nonce, String echostr, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (StringUtils.isNotBlank(signature) && StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(nonce)
				&& StringUtils.isNotBlank(echostr)) {
			if (verifyURL(signature, timestamp, nonce, echostr)) {
				response.setContentType("text/plain");
				response.getWriter().write(echostr);
			}
		}
	}

	public void acceptMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			ServletInputStream in = request.getInputStream();
			StringBuilder xmlMsg = new StringBuilder();
			byte[] b = new byte[4096];
			for (int n; (n = in.read(b)) != -1;) {
				xmlMsg.append(new String(b, 0, n, "UTF-8"));
			}
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			String msgSignature = request.getParameter("msg_signature");
			ParamVO param = null;
			WXBizMsgCrypt pc = null;
			if (StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(nonce)
					&& StringUtils.isNotBlank(msgSignature)) {
				pc = new WXBizMsgCrypt(TOKEN, ENCODINGAESKEY, APPID);
				String msg = pc.decryptMsg(msgSignature, timestamp, nonce, xmlMsg.toString());
				param = new ParamVO(XmlUtils.string2Xml(msg));
			} else {
				param = new ParamVO(XmlUtils.string2Xml(xmlMsg.toString()));
			}

			String servername = param.getStringValue("ToUserName");// 服务端
			String custermname = param.getStringValue("FromUserName");// 客户端
			String msgType = param.getStringValue("MsgType");

			if ("event".equals(msgType)) {// 收到按钮事件
				String eventKey = param.getStringValue("EventKey");
				sendTextMsg(response, "点击了按钮" + eventKey, custermname, servername, pc, timestamp, nonce);
				return;
			}

			if ("text".equals(msgType)) {// 收到文本事件
				String content = param.getStringValue("Content");
				if ("停车卡".equals(content)) {
					String templateId = PropertiesUtil.getConfiguration().getString("wechat.msg.template1");
					String token = getToken();
					ParkingCardType type = this.parkingCardTypeService.selectByPrimaryKey(25);
					sendTemplateMsg(getJsonMsg(type, templateId, custermname, "http://www.baidu.com"), token);
					return;
				}
				StringBuffer toMsg = new StringBuffer("");
				try {
					String msg = aiService.askToTuRing(content, custermname);
					if (msg != null) {
						toMsg = toMsg.append(msg);
					} else {
						toMsg = toMsg.append(aiService.askToSimsimi(content, custermname));
					}
				} catch (Exception e) {
					toMsg = toMsg.append("好像出了点什么错，喵~!");
				}
				if (toMsg.equals("")) {
					toMsg = toMsg.append("好像出了点什么错，喵~!");
				}
				sendTextMsg(response, toMsg.toString(), custermname, servername, pc, timestamp, nonce);
				return;
			}
		} catch (Exception e) {
			response.setContentType("text/plain");
			response.getWriter().write("");
		}

	}

	private Boolean verifyURL(String msgSignature, String timeStamp, String nonce, String echoStr) {
		try {
			final String token = PropertiesUtil.getConfiguration().getString("wechat.verify.token");
			List<String> params = new ArrayList<String>();
			params.add(token);
			params.add(timeStamp);
			params.add(nonce);
			Collections.sort(params, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
			String temp = SHA1.encode(params.get(0) + params.get(1) + params.get(2));

			if (temp.equals(msgSignature)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getJsonMsg(ParkingCardType type, String tempId, String touser, String url) {
		StringBuffer msgJsn = new StringBuffer();
		String strColor = "#173177";
		if (type != null) {
			msgJsn.append("{");
			msgJsn.append("\"touser\":\"").append(touser).append("\", ");
			msgJsn.append("\"template_id\":\"").append(tempId).append("\", ");
			msgJsn.append("\"url\":\"").append(url).append("\", ");
			msgJsn.append("\"data\":{");
			msgJsn.append("\"first\":").append("{").append("\"value\":\"").append("欢迎使用").append("\",\"color\":\"")
					.append(strColor).append("\"},");
			msgJsn.append("\"remark\":").append("{").append("\"value\":\"").append("谢谢使用").append("\",\"color\":\"")
					.append(strColor).append("\"},");
			msgJsn.append("\"name\":").append("{").append("\"value\":\"").append(type.getCardType())
					.append("\",\"color\":\"").append(strColor).append("\"},");
			msgJsn.append("\"price\":").append("{").append("\"value\":\"¥ ").append(type.getFee().toString())
					.append("\",\"color\":\"").append(strColor).append("\"},");
			msgJsn.append("\"time\":").append("{").append("\"value\":\"")
					.append(TimeUtils.getFormatTimeYMDHM(type.getCreateTime())).append("\",\"color\":\"")
					.append(strColor).append("\"}");
			msgJsn.append("}}");
		}
		return msgJsn.toString();
	}

	private void sendTemplateMsg(String msg, String token) {
		String url = SEND_MSG_RUL + "?access_token=" + token;
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		String jsonData = msg;
		HttpEntity<String> entity = new HttpEntity<String>(jsonData, headers);
		String response = restTemplate.postForObject(url, entity, String.class);
		System.out.println(response);
	}

	private void sendTextMsg(HttpServletResponse response, String toMsg, String custermname, String servername,
			WXBizMsgCrypt pc, String timestamp, String nonce) throws Exception {
		StringBuffer str = new StringBuffer();
		str.append("<xml>");
		str.append("<ToUserName><![CDATA[" + custermname + "]]></ToUserName>");
		str.append("<FromUserName><![CDATA[" + servername + "]]></FromUserName>");
		str.append("<CreateTime>" + System.currentTimeMillis() + "</CreateTime>");
		str.append("<MsgType><![CDATA[" + "text" + "]]></MsgType>");
		str.append("<Content><![CDATA[" + toMsg + "]]></Content>");
		str.append("</xml>");
		String msg = str.toString();
		if (pc != null) {
			msg = pc.encryptMsg(str.toString(), timestamp, nonce);
		}
		response.setContentType("text/plain");
		response.getWriter().write(msg);
	}

	public String getToken() {
		String token = "";
		try {
			token = getWeChatTokenAccess(false);
			return token;
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return token;
	}

	private String getWeChatTokenAccess(Boolean byNetwork) throws ExecutionException {
		String token = "";
		token = (String) this.redisClient.get(CacheConstants.WECHAT_TOKEN_KEY);
		if (byNetwork || StringUtils.isBlank(token)) {
			return getWeChatTokenAccessByHttp();
		} else {
			return token;
		}
	}

	// 取得wxtoken 后放入redis缓存，设置过期时间为1小时59分钟。保证每次token有效。
	@Transactional(rollbackFor = Exception.class)
	private String getWeChatTokenAccessByHttp() {
		String token = "";
		String TOKENURL = PropertiesUtil.getConfiguration().getString("wechat.getTokenUrl");
		if (StringUtils.isBlank(TOKENURL)) {
			return null;
		}
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "client_credential");
		params.add("appid", APPID);
		params.add("secret", SECRET);
		String response = restTemplate.postForObject(TOKENURL, params, String.class);
		JSONObject responseObj = JSONObject.parseObject(response);
		token = responseObj.getString("access_token");
		if (token != null) {
			redisClient.set(CacheConstants.WECHAT_TOKEN_KEY, token);
			redisClient.expire(CacheConstants.WECHAT_TOKEN_KEY, CacheConstants.EXPIRE_AN_HOUR * 2 - 60L);
			return token;
		} else
			return "";
	}

}
