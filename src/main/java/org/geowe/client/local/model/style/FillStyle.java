package org.geowe.client.local.model.style;

public class FillStyle {
	public static int MIN_OPACITY = 0;
	public static int MAX_OPACITY = 100;
	
	private String color;
	private int fillOpacity;
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String fillColor) {
		this.color = fillColor;
	}
	
	public int getOpacity() {
		return fillOpacity;
	}
	
	public void setOpacity(int fillOpacity) {
		
		if(fillOpacity < MIN_OPACITY) {
			this.fillOpacity = MIN_OPACITY;
		} else if(fillOpacity > MAX_OPACITY) {
			this.fillOpacity = MAX_OPACITY;
		} else {		
			this.fillOpacity = fillOpacity;
		}
	}
}
