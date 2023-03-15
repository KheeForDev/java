package com.dev.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dev.model.RequestDto;
import com.dev.sns.SnsUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class DefaultController {

	@Autowired
	private SnsUtil snsUtil;

	@RequestMapping(method = RequestMethod.POST, value = "/hit")
	public void SendData(@RequestBody RequestDto requestDto) throws Exception {
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		requestDto.setInsertedDt(timestamp);

		String message = new ObjectMapper().writeValueAsString(requestDto);

		snsUtil.sendSNSMessage(message);
	}
}
