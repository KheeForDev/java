package com.kheefordev.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kheefordev.aws.sns.SnsUtil;
import com.kheefordev.model.RequestDto;

@RestController
public class DefaultController {
	private static final Logger LOG = LogManager.getLogger(DefaultController.class);

	@Autowired
	private SnsUtil snsUtil;

	@RequestMapping(method = RequestMethod.POST, value = "/hit")
	public void SendData(@RequestBody RequestDto requestDto) {
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		requestDto.setInsertedDt(timestamp);

		String message = null;
		try {
			message = new ObjectMapper().writeValueAsString(requestDto);
		} catch (JsonProcessingException e) {
			LOG.error(e.getMessage());
		}

		snsUtil.sendSNSMessage(message);
	}
}
