package com.khee.dev.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.khee.dev.kinesis.Producer;

@RestController
public class DefaultController {

	@Autowired
	private Producer producer;

	@RequestMapping(method = RequestMethod.GET, value = "/sendData")
	public void MethodTwo() throws Exception {
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String payload = "data_" + timestamp;

		producer.putIntoKinesis(payload);
	}
}
