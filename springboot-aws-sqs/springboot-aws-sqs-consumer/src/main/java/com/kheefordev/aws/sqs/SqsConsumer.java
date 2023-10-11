package com.kheefordev.aws.sqs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;

@Service
public class SqsConsumer {
	private static final Logger LOG = LogManager.getLogger(SqsConsumer.class);

	@SqsListener(value = "${aws.sqs.queue.url}", messageVisibilitySeconds = "${aws.sqs.message.visibility.second}", maxMessagesPerPoll = "1", maxConcurrentMessages = "1")
	private void processMessage(String message, Acknowledgement acknowledgment) {
		LOG.info(message);
		acknowledgment.acknowledge();
	}
}
