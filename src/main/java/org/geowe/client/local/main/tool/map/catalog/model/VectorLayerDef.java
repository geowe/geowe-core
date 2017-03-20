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
package org.geowe.client.local.main.tool.map.catalog.model;

import org.geowe.client.local.layermanager.tool.create.vector.source.LayerVectorSource;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.format.GPX;
import org.geowe.client.local.model.vector.format.GeoJSONCSS;
import org.geowe.client.local.model.vector.format.TopoJSON;
import org.geowe.client.local.style.StyleFactory;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.format.Format;
import org.gwtopenmaps.openlayers.client.format.GML;
import org.gwtopenmaps.openlayers.client.format.GeoJSON;
import org.gwtopenmaps.openlayers.client.format.KML;
import org.gwtopenmaps.openlayers.client.format.WKT;

/**
 * Implementacion base de la definicion de una capa vectorial.
 * 
 * @author Atanasio Muñoz
 * @author rafa@geowe.org
 * @since 14/10/2016 addes topojson and gpx format
 *
 */
public abstract class VectorLayerDef extends LayerDef{
	private static final long serialVersionUID = -666986330487995133L;	
	
	public static final String KML = "KML";
	public static final String GEOJSON = "GeoJSON";
	public static final String GEOJSON_CSS = "CSS-GeoJSON";
	public static final String WKT = "WKT";
	public static final String GML = "GML";
	public static final String TOPOJSON = "TopoJSON";
	public static final String GPX = "GPX";

	private String normalColor;
	private String selectedColor;
	private String highlightedColor;
	
	private String epsg;
	private String format;
	private LayerVectorSource source;
	
	private StyleMap styleMap;
	
	public VectorLayerDef() {
		//Default color values
		this.normalColor = "#FF0000";
		this.selectedColor = "#00FF00";
		this.highlightedColor = "#0000FF";
	}
	
	@Override
	public String getType() {
		return VECTOR_TYPE;
	}		
		
	public String getNormalColor() {
		return normalColor;
	}

	public void setNormalColor(String normalColor) {
		this.normalColor = normalColor;
	}

	public String getSelectedColor() {
		return selectedColor;
	}

	public void setSelectedColor(String selectedColor) {
		this.selectedColor = selectedColor;
	}

	public String getHighlightedColor() {
		return highlightedColor;
	}

	public void setHighlightedColor(String highlightedColor) {
		this.highlightedColor = highlightedColor;
	}

	public String getEpsg() {
		return epsg;
	}

	public void setEpsg(String epsg) {
		this.epsg = epsg;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	public StyleMap getStyle() {
		if(styleMap == null) {
			styleMap = StyleFactory.createDefaultStyleMap();
		}
		
		return styleMap;
	}

	public void setStyle(StyleMap style) {
		this.styleMap = style;
	}		
	
	protected Format getLayerFormat() {
		Format layerFormat = null;
		
		switch (format) {
		case KML:
			layerFormat = new KML();
			break;
		case WKT:
			layerFormat = new WKT();
			break;
		case GML:
			layerFormat = new GML();
			break;
		case GPX:
			layerFormat = new GPX();
			break;
		case TOPOJSON:
			layerFormat = new TopoJSON();
			break;
		case GEOJSON_CSS:
			layerFormat = new GeoJSONCSS();
			break;	
		default:
			layerFormat = new GeoJSON();
			break;
		}
		return layerFormat;
	}
	
	public VectorLayerConfig getVectorLayerConfig() {
		final VectorLayerConfig layerConfig = new VectorLayerConfig();
		layerConfig.setEpsg(getEpsg());
		layerConfig.setGeoDataFormat(getFormat());
		layerConfig.setLayerName(getName());
		layerConfig.setSource(source);
		return layerConfig;
	}
	
	public void setSource(LayerVectorSource source) {
		this.source = source;
	}
	
	public LayerVectorSource getSource() {
		return source;
	}
}
