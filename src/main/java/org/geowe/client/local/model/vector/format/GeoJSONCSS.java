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

package org.geowe.client.local.model.vector.format;

import org.geowe.client.local.main.tool.project.StyleProjectLayer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.GeoJSON;
import org.gwtopenmaps.openlayers.client.format.GeoJSONImpl;
import org.gwtopenmaps.openlayers.client.util.JSObject;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

/**
 * Representa el formato vectorial GeoJSON CSS según la especificación Leaflet
 * 
 * http://leafletjs.com/reference-1.0.2.html#path-option
 * 
 * @author jose@geowe.org
 */

public class GeoJSONCSS extends GeoJSON {
	private StyleProjectLayer style;

	public void setStyle(StyleProjectLayer style) {
		this.style = style;
	}

	protected GeoJSONCSS(JSObject geoJSONFormat) {
		super(geoJSONFormat);
	}

	public GeoJSONCSS() {
		this(GeoJSONImpl.create());
	}

	public String write(VectorFeature[] vectorFeatures) {
		GeoJSON geoJSONObject = new GeoJSON();
		String geojson = geoJSONObject.write(vectorFeatures);

		final JSONValue jsonValue = JSONParser.parseLenient(geojson);
		final JSONObject geoJSONCssObject = jsonValue.isObject();

		geoJSONCssObject.put("style", style.getLeafletJSONObject());
		return geoJSONCssObject.toString();
	}

	public StyleProjectLayer getStyle(String geoDataString) {
		final JSONValue jsonValue = JSONParser.parseLenient(geoDataString);
		final JSONObject geoJSONCssObject = jsonValue.isObject();
		JSONObject styleObject = geoJSONCssObject.get("style").isObject();

		String fillColor = getStringValue(styleObject, "fillColor");
		Double fillOpacity = getDoubleValue(styleObject, "fillOpacity");
		String strokeColor = getStringValue(styleObject, "color");
		Double strokeWidth = getDoubleValue(styleObject, "weight");

		return new StyleProjectLayer(fillColor, fillOpacity, strokeColor,
				strokeWidth);
	}

	private String getStringValue(JSONObject styleObject, String key) {
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

	private Double getDoubleValue(JSONObject styleObject, String key) {
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
