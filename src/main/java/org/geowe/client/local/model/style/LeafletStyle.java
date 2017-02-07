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

package org.geowe.client.local.model.style;

import org.geowe.client.local.main.tool.project.ProjectLayerStyle;
import org.geowe.client.local.model.vector.format.GeoJSONCSS;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * Representa el estilo para Leaflet
 * 
 * @author jose@geowe.org
 */

public class LeafletStyle {

	public static final String FILL_NAME = "fill";
	public static final String FILL_COLOR_NAME = "fillColor";	
	public static final String FILL_OPACITY_NAME = "fillOpacity";
	public static final String FILL_OPACITY2_NAME = "fill-opacity";
	public static final String STROKE_COLOR_NAME = "color";
	public static final String STROKE_WIDTH_NAME = "weight";
	public static final String STROKE_OPACITY_NAME = "opacity";

	public static JSONObject getStyle(ProjectLayerStyle style) {

		JSONObject styleObject = new JSONObject();
		styleObject.put(FILL_NAME, JSONBoolean.getInstance(true));
		styleObject.put(FILL_COLOR_NAME, new JSONString(style.getFillColor()));
		styleObject.put(FILL_OPACITY_NAME,
				new JSONNumber(style.getFillOpacity())); // opacidad de color de relleno
		styleObject.put(STROKE_WIDTH_NAME,
				new JSONNumber(style.getStrokeWidth())); // stroke-width
		styleObject.put(STROKE_COLOR_NAME,
				new JSONString(style.getStrokeColor()));// stroke-color
		styleObject.put(STROKE_OPACITY_NAME, new JSONNumber(1)); // stroke
																	// opacity

		return styleObject;
	}

	public static ProjectLayerStyle getStyle(String geoJSONCSS) {
		ProjectLayerStyle style = null;
		final JSONValue jsonValue = JSONParser.parseLenient(geoJSONCSS);
		final JSONObject geoJSONCssObject = jsonValue.isObject();

		if (geoJSONCssObject.containsKey(GeoJSONCSS.STYLE_NAME)) {

			JSONObject styleObject = geoJSONCssObject
					.get(GeoJSONCSS.STYLE_NAME).isObject();

			String fillColor = getStringValue(styleObject, FILL_COLOR_NAME);
			Double fillOpacity = getDoubleValue(styleObject, FILL_OPACITY_NAME);
			if(fillOpacity == null) {
				fillOpacity = getDoubleValue(styleObject, FILL_OPACITY2_NAME);				
			}
			String strokeColor = getStringValue(styleObject, STROKE_COLOR_NAME);
			Double strokeWidth = getDoubleValue(styleObject, STROKE_WIDTH_NAME);

			style = new ProjectLayerStyle(fillColor, fillOpacity, strokeColor,
					strokeWidth);
		}

		return style;
	}

	private static String getStringValue(JSONObject styleObject, String key) {
		String newValue = "";

		if (styleObject.containsKey(key)) {
			try {
				newValue = styleObject.get(key).isString().stringValue();
			} catch (Exception e) {
				// si el valor del atributo no está bien definido es ignorado
			}
		}

		return newValue;

	}

	private static Double getDoubleValue(JSONObject styleObject, String key) {
		Double newValue = null;

		if (styleObject.containsKey(key)) {
			try {
				newValue = styleObject.get(key).isNumber().doubleValue();
			} catch (Exception e) {
				// si el valor del atributo no está bien definido es ignorado
			}
		}

		return newValue;
	}
}
