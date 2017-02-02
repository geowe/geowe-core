/*
 * #%L
 * GeoWE Project
 * %%
 * Copyright (C) 2015 - 2016 GeoWE.org
 * %%
 * This file is part of GeoWE.org.
 * 
 * GeoWE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GeoWE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GeoWE.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.geowe.client.local.main.tool.project;

import com.google.gwt.json.client.JSONBoolean;
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
	
	public JSONObject getLeafletJSONObject() {
		
		JSONObject styleObject = new JSONObject();
		styleObject.put("fill", JSONBoolean.getInstance(true));
		styleObject.put("fillColor", new JSONString(getFillColor()));
		styleObject.put("fill-opacity", new JSONNumber(getFillOpacity())); //opacidad de color de relleno
		
		styleObject.put("weight", new JSONNumber(getStrokeWidth())); //stroke-width		
		styleObject.put("color", new JSONString(getStrokeColor()));//stroke-color
		styleObject.put("opacity", new JSONNumber(1)); //stroke opacity	
						
		return styleObject;		
	}
	
	public String toJSON() {
		return getJSONObject().toString();
	}

}
