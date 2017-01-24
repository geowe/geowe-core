package org.geowe.client.local.main.tool.project;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;


public class ProjectLayer {
	private String name;
	private String content;
	private String fillColor;
	private String fillOpacity;
	private String strokeColor;
	private String strokeWidth;
	
//	public StyleMap getStyle() {
//		return style;
//	}

//	public void setStyle(StyleMap style) {
//		this.style = style;
//	}
//
//	private StyleMap style;

	public String getFillOpacity() {
		return fillOpacity;
	}

	public void setFillOpacity(String fillOpacity) {
		this.fillOpacity = fillOpacity;
	}

	public String getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}

	public String getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(String strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public String getFillColor() {
		return fillColor;
	}

	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	public ProjectLayer(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public String getName() {
		return this.name;
	};

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return this.content;
	};

	public void setContent(String content) {
		this.content = content;
	}
	
	public JSONObject getJSONObject() {
		JSONObject projectLayerObject = new JSONObject();
		projectLayerObject.put("name", new JSONString(getName()));
		projectLayerObject.put("content", new JSONString(getContent()));
		projectLayerObject.put("fillColor", new JSONString(getFillColor()));
		projectLayerObject.put("fillOpacity", new JSONString(getFillOpacity()));
		projectLayerObject.put("strokeColor", new JSONString(getStrokeColor()));
		projectLayerObject.put("strokeWidth", new JSONString(getStrokeWidth()));
		
		return projectLayerObject;		
	}
	
	public String toJSON() {
		return getJSONObject().toString();
	}
}
