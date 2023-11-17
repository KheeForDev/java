package com.sia.csl.service.oeotocmtdeliveryservice.aws.dynamodb.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JourneyCampaign {
	private String iid;
	private String journeyId;
	private String campaignId;
	private String campaignName;
	private long timeToLive;
}