package com.khee.dev.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/server")
public class ServerController {

	@RequestMapping(method = RequestMethod.GET, value = "/auth")
	public void Authentication() throws Exception {
		System.out.println("Authentication Successful");
	}
}
