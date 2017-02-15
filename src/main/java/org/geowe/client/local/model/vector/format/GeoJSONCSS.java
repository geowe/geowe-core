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

import org.geowe.client.local.model.style.LeafletStyle;
import org.geowe.client.local.model.style.VectorFeatureStyleDef;
import org.geowe.client.local.model.style.VectorStyleDef;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.FormatImpl;
import org.gwtopenmaps.openlayers.client.format.VectorFormat;
import org.gwtopenmaps.openlayers.client.util.JObjectArray;
import org.gwtopenmaps.openlayers.client.util.JSObject;

import com.google.gwt.core.client.JsArrayInteger;
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

public class GeoJSONCSS extends VectorFormat {
	
	public static final String STYLE_NAME = "style";
	private VectorLayer layer;

	public void setLayer(VectorLayer layer) {
		this.layer = layer;		
	}

	protected GeoJSONCSS(JSObject geoJSONFormat) {
		super(geoJSONFormat);
	}

	public GeoJSONCSS() {
		this(GeoJSONCSSImpl.create());
	}

	/**
	 * Para exportar a formato GeoJSON CSS. Se incluye el atributo "style" a
	 * nivel de capa
	 */
	public String write(VectorFeature[] vectorFeatures) {

		for (VectorFeature vf : vectorFeatures) {
			Style style = vf.getStyle();
			if (style != null) {
				VectorFeatureStyleDef def = new VectorFeatureStyleDef(vf, layer);
				vf.getJSObject().setProperty(STYLE_NAME, LeafletStyle.getStyle(def));
			}
		}

		String geojson = super.write(vectorFeatures);

		final JSONValue jsonValue = JSONParser.parseLenient(geojson);
		final JSONObject geoJSONCssObject = jsonValue.isObject();
		
		geoJSONCssObject.put(STYLE_NAME, new JSONObject(LeafletStyle.getStyle(layer.getVectorStyle())));
		
		return geoJSONCssObject.toString();
	}

	public VectorFeature[] read(String vectorFormatString) {
		JSObject out = FormatImpl.read(getJSObject(), vectorFormatString);
		JObjectArray jObjectArray = JObjectArray.narrowToJObjectArray(out.ensureOpaqueArray());
		int nr = jObjectArray.length();
		VectorFeature[] vfs = new VectorFeature[nr];
		for (int i = 0; i < nr; i++) {

			VectorFeature vf = VectorFeature.narrowToVectorFeature(jObjectArray.get(i));

			JSObject styleObject = jObjectArray.get(i).getProperty(STYLE_NAME);
			if (styleObject != null) {
				VectorFeatureStyleDef def = getStyleDef(styleObject);								
				vf.setStyle(def.toStyle(vf));
			}

			vfs[i] = vf;
		}

		return vfs;
	}
	
	public VectorStyleDef getLayerStyle(String vectorFormatString) {
		JSObject out = FormatImpl.read(getJSObject(), vectorFormatString);
		VectorFeatureStyleDef def = null;
		
		JSObject styleObject = out.getProperty(STYLE_NAME);
			if (styleObject != null) {
												
				def = getStyleDef(styleObject);
			}

		return def;
	}
	
	public VectorFeatureStyleDef getStyleDef(JSObject styleObject) {
		VectorFeatureStyleDef def = new VectorFeatureStyleDef();
		
		String fillColor = styleObject.getPropertyAsString(LeafletStyle.FILL_COLOR_NAME);
		Double fillOpacity = styleObject.getPropertyAsDouble(LeafletStyle.FILL_OPACITY_NAME);
		String strokeColor = styleObject.getPropertyAsString(LeafletStyle.STROKE_COLOR_NAME);
		Double strokeWidth = styleObject.getPropertyAsDouble(LeafletStyle.STROKE_WIDTH_NAME);
		
		def.getFill().setNormalColor(fillColor);
		def.getFill().setOpacity(fillOpacity);
		def.getLine().setNormalColor(strokeColor);
		def.getLine().setThickness(strokeWidth.intValue());
		
		JSObject iconObject = styleObject.getProperty(LeafletStyle.ICON_NAME);
		if(iconObject != null) {
			String iconUrl = iconObject.getPropertyAsString(LeafletStyle.ICON_URL_NAME);
			
			JsArrayInteger iconSize = iconObject.getProperty(LeafletStyle.ICON_SIZE_NAME).cast();
			
			int iconWidth = iconSize.get(0);
			int iconHeight = iconSize.get(1);
			
			def.getPoint().setExternalGraphic(iconUrl);
			def.getPoint().setGraphicWidth(iconWidth);
			def.getPoint().setGraphicHeight(iconHeight);
			
		}
		
		return def;
	}
}
