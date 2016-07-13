package com.shaw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.shaw.utils.PropertiesUtil;
import com.shaw.vo.FoodMenu;
import com.shaw.vo.News;
import com.shaw.vo.TURINGFoodMenuResponse;
import com.shaw.vo.TURINGMsg;
import com.shaw.vo.TURINGNewsResponse;
import com.shaw.vo.TURINGResponse;

/**
 * ai智能回复
 */
@Service
public class AIService {
	@Autowired
	RestTemplate restTemplate;

	// 小黄鸭接口
	public String askToYY(String question) throws Exception {
		String url = PropertiesUtil.getConfiguration().getString("chat.chat.yy");
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
		headers.setContentType(type);
		headers.add("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(params,
				headers);
		params.add("txt", question);
		return restTemplate.postForObject(url, httpEntity, String.class);
	}

	// 图灵机器人接口
	/**
	 * 可选参数 loc 位置信息 lon 经度 lat 维度
	 */
	private TURINGMsg askToTuRingGetMsg(String info, String userid) throws Exception {
		String url = PropertiesUtil.getConfiguration().getString("tr.api.url");
		String key = PropertiesUtil.getConfiguration().getString("tr.api.key");
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
		headers.setContentType(type);
		headers.add("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(params,
				headers);
		params.add("key", key);
		params.add("info", info);
		params.add("userid", userid);
		String response = restTemplate.postForObject(url, httpEntity, String.class);
		JSONObject jsonObj = JSONObject.parseObject(response);
		Integer code = jsonObj.getInteger("code");
		TURINGMsg msg = new TURINGMsg();
		switch (code) {
		case 100000: // 单文本回答
			TURINGResponse obj = JSONObject.parseObject(response, TURINGResponse.class);
			msg.setDetail(obj.getText());
			return msg;
		case 200000: // 带url的回答
			TURINGResponse obj2 = JSONObject.parseObject(response, TURINGResponse.class);
			msg.setDetail(obj2.getText());
			msg.setUrl(obj2.getUrl());
			return msg;
		case 302000: // 新闻
			TURINGNewsResponse obj3 = JSONObject.parseObject(response, TURINGNewsResponse.class);
			System.out.println("response:\n" + obj3.getText());
			msg.setDetail(obj3.getText());
			String news = "";
			for (News temp : obj3.getList()) {
				news += temp.getArticle() + "\n" + temp.getDetailurl() + "\n\n";
			}
			msg.setNews(news);
			return msg;
		case 308000: // 菜谱
			TURINGFoodMenuResponse obj4 = JSONObject.parseObject(response, TURINGFoodMenuResponse.class);
			msg.setTitle(obj4.getText());
			FoodMenu temp = obj4.getList().get(0);
			msg.setDetail(temp.getName() + "\n食材：" + temp.getInfo());
			msg.setUrl(temp.getDetailurl());
			return msg;
		default: // 异常
			System.out.println("啊啦拉，好像出了点问题");
			return null;
		}
	}

	public String askToTuRing(String info, String userid) throws Exception {
		TURINGMsg msg = askToTuRingGetMsg(info, userid);
		if (msg == null) {
			return null;
		}
		StringBuffer toMsg = new StringBuffer("");
		if (msg.getTitle() != null) {
			toMsg.append(msg.getTitle() + "\n");
		}
		if (msg.getDetail() != null) {
			toMsg.append(msg.getDetail() + "\n");
		}
		if (msg.getNews() != null) {
			toMsg.append(msg.getNews() + "\n");
		}
		if (msg.getUrl() != null) {
			toMsg.append("参考链接:" + msg.getUrl());
		}
		return toMsg.toString();
	}

	// simsimi入口
	public String askToSimsimi(String req, String uid) throws Exception {
		String url = PropertiesUtil.getConfiguration().getString("simsim.url");
		url = url + "?lc=ch&ft=1.0&req=" + req + "&uid=" + uid + "&did=0";
		String response = restTemplate.getForObject(url, String.class);
		JSONObject jsonObj = JSONObject.parseObject(response);
		if (jsonObj.getInteger("status") == 200) {
			String msg = JSONObject.parseObject(jsonObj.getString("res")).getString("msg");
			if (msg.startsWith("I HAVE NO RESPONSE.<br>PLEASE TEACH ME."))
				return "不懂你说什么，喵~";
			else {
				return msg;
			}
		} else {
			return "啊啦拉，好像出了点问题";
		}

	}
}
