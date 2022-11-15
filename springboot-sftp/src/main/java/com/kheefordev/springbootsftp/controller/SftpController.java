package com.kheefordev.springbootsftp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kheefordev.springbootsftp.sftp.SftpAction;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class SftpController {
	@Autowired
	private SftpAction sftpAction;
	
	@GetMapping("/test")
	public ResponseEntity<String> test() {
		sftpAction.upload();
		return ResponseEntity.status(HttpStatus.OK).body("stfp upload action completed");
	}
}
