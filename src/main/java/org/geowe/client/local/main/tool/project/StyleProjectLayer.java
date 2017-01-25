package org.geowe.client.local.main.tool.project;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Modelo de estilo de capa vectorial para la configuración de la sesión de trabajo del proyecto
 * 
 * @author jose@geowe.org
 *
 */
public class StyleProjectLayer {
	private String fillColor;
	private Double fillOpacity;
	private String strokeColor;
	private Double strokeWidth;
	
	public StyleProjectLayer() {
		
	}
	
	public StyleProjectLayer(String fillColor, Double fillOpacity, String strokeColor, Double strokeWidth) {
		this.fillColor = fillColor;
		this.fillOpacity = fillOpacity;
		this.strokeColor = strokeColor;
		this.strokeWidth = strokeWidth;
	}

	public Double getFillOpacity() {
		return fillOpacity;
	}

	public void setFillOpacity(Double fillOpacity) {
		this.fillOpacity = fillOpacity;
	}

	public String getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}

	public Double getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(Double strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public String getFillColor() {
		return fillColor;
	}

	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}
	
	public JSONObject getJSONObject() {
		JSONObject projectLayerObject = new JSONObject();
		
		projectLayerObject.put("fillColor", new JSONString(getFillColor()));
		projectLayerObject.put("fillOpacity", new JSONNumber(getFillOpacity()));
		projectLayerObject.put("strokeColor", new JSONString(getStrokeColor()));
		projectLayerObject.put("strokeWidth", new JSONNumber(getStrokeWidth()));
		
		return projectLayerObject;		
	}
	
	public String toJSON() {
		return getJSONObject().toString();
	}

}
