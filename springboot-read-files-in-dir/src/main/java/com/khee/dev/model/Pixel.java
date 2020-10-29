package com.khee.dev.model;

import java.util.List;

public class Pixel {
	private String bkUuid;
	private int bkclear;
	private int campaignId;
	private List<Category> categories;
	private String partnerUuid;
	private String pixelUrl;
	private int utcSeconds;

	public String getBkUuid() {
		return bkUuid;
	}

	public void setBkUuid(String bkUuid) {
		this.bkUuid = bkUuid;
	}

	public int getBkclear() {
		return bkclear;
	}

	public void setBkclear(int bkclear) {
		this.bkclear = bkclear;
	}

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getPartnerUuid() {
		return partnerUuid;
	}

	public void setPartnerUuid(String partnerUuid) {
		this.partnerUuid = partnerUuid;
	}

	public String getPixelUrl() {
		return pixelUrl;
	}

	public void setPixelUrl(String pixelUrl) {
		this.pixelUrl = pixelUrl;
	}

	public int getUtcSeconds() {
		return utcSeconds;
	}

	public void setUtcSeconds(int utcSeconds) {
		this.utcSeconds = utcSeconds;
	}
}
