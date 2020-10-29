package com.khee.dev.model;

public class CsvDataDto {
	private int campaignId;
	private String partnerUuid;
	private String bkUuid;
	private String deliveryTime;

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public String getPartnerUuid() {
		return partnerUuid;
	}

	public void setPartnerUuid(String partnerUuid) {
		this.partnerUuid = partnerUuid;
	}

	public String getBkUuid() {
		return bkUuid;
	}

	public void setBkUuid(String bkUuid) {
		this.bkUuid = bkUuid;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
}
