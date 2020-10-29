package com.khee.dev.model;

import java.util.List;

public class DataDto {
	private String deliveryTime;
	private List<Pixel> pixels;

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public List<Pixel> getPixels() {
		return pixels;
	}

	public void setPixels(List<Pixel> pixels) {
		this.pixels = pixels;
	}
}
