package com.kheefordev.springbootsftp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

@Configuration
public class SftpSessionFactory {
	public DefaultSftpSessionFactory getDefaultSftpSessionFactory() {
		DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
		factory.setHost("secureftp.sq.com.sg");
		factory.setPort(1022);
		factory.setUser("rtpftp");
		factory.setPassword("rtpftp");
		return factory;
	}
}
