package com.kheefordev.springbootsftp.sftp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Component;

import com.kheefordev.springbootsftp.config.SftpSessionFactory;

@Component
public class SftpAction {
	@Autowired
	private SftpSessionFactory sftpSessionFactory;
	private String sftpPath = "upload/";
	private String sftpFilename = "test_data.txt";

	public void upload() {
		System.out.println("Upload action start");

		SftpSession session = sftpSessionFactory.getDefaultSftpSessionFactory().getSession();

		InputStream inputStream = SftpAction.class.getClassLoader().getResourceAsStream(sftpFilename);

		try {
			session.write(inputStream, sftpPath + sftpFilename);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		session.close();
	}

	public String download() {
		String data = "";
		SftpSession session = sftpSessionFactory.getDefaultSftpSessionFactory().getSession();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		try {
			session.read(sftpPath + sftpFilename, byteArrayOutputStream);
			data = new String(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		session.close();

		return data;
	}
}
