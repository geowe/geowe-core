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

import java.util.ArrayList;
import java.util.List;

import org.geowe.client.local.main.tool.map.catalog.LayerLoader;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.layer.Layer;

/**
 * Definicion unificada de un conjunto de capas vectoriales y raster que
 * se pueden crear y cargar en una sola operacion.
 * 
 * @author Atanasio Muñoz
 *
 */
public class MultiLayerDef extends LayerDef {
	
	private static final long serialVersionUID = -5626734843063575847L;
	private final List<URLVectorLayerDef> compositeLayers = new ArrayList<URLVectorLayerDef>();
	private final List<LayerDef> rasterLayers = new ArrayList<LayerDef>();
	
	@Override
	public String getType() {
		return LayerDef.COMPOSITE_TYPE;
	}

	@Override
	public Layer getLayer() {
		for(URLVectorLayerDef layerDef: compositeLayers) {
			layerDef.load();
		}
		
		for(LayerDef layerDef: rasterLayers) {
			LayerLoader.load(layerDef.getLayer());
		}
		return null;
	}
		
	public void add(String name, String epsg, String format, String url, StyleMap style) {
		URLVectorLayerDef def = new URLVectorLayerDef();		
		def.setEpsg(epsg);
		def.setFormat(format);
		def.setName(name);
		def.setUrl(url);
		def.setType(LayerDef.VECTOR_TYPE);
		def.setStyle(style);		
		compositeLayers.add(def);		
	}
	
	public void addRaster(LayerDef layerDef) {
		rasterLayers.add(layerDef);
	}
	
	public List<URLVectorLayerDef> getCompositeLayers() {
		return compositeLayers;
	}
}
