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
package org.geowe.client.local.main.tool.map.catalog;

import java.util.ArrayList;
import java.util.List;

import org.geowe.client.local.main.tool.map.catalog.layerset.LayerSet;
import org.geowe.client.local.main.tool.map.catalog.model.LayerDef;


/**
 * Implementacion base de un catalogo de capas (interfaz LayerCatalog)
 * 
 * @author Atanasio Muñoz
 *
 */
public abstract class AbstractLayerCatalog implements LayerCatalog {

	private final List<LayerDef> layers = new ArrayList<LayerDef>();
	
	@Override
	public LayerDef getLayer(String layerName) {
		LayerDef selectedLayer = null;

		for (LayerDef layerDef : layers) {
			if (layerDef.getName().equals(layerName)) {
				selectedLayer = layerDef;
			}
		}

		return selectedLayer;
	}

	@Override
	public List<LayerDef> getAllLayers() {
		return layers;
	}	
	
	@Override
	public void addLayer(LayerDef layer) {
		layers.add(layer);
	}
	
	@Override
	public void addLayers(List<LayerDef> layers) {
		layers.addAll(layers);
	}
	
	@Override
	public void addLayers(LayerSet layerSet) {
		layers.addAll(layerSet.getLayers());
	}	
	
	@Override
	public void removeLayer(String layerName) {
		LayerDef layerToRemove = getLayer(layerName);
		
		if(layerToRemove == null) { 
			return;
		}
		
		layers.remove(layerToRemove);
	}
	
}
