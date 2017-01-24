package org.geowe.client.local.model.style;

import org.geowe.client.local.model.vector.FeatureAttributeDef;

public class LabelStyle {
	private FeatureAttributeDef attribute;
	private int fontSize;
	private boolean boldStyle;
	private String backgroundColor;
	
	public boolean isEnabled() {
		return getAttribute() != null; 
	}
	
	public FeatureAttributeDef getAttribute() {
		return attribute;
	}
	
	public void setAttribute(FeatureAttributeDef attribute) {
		this.attribute = attribute;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public boolean isBoldStyle() {
		return boldStyle;
	}
	
	public void setBoldStyle(boolean boldStyle) {
		this.boldStyle = boldStyle;
	}
	
	public String getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
