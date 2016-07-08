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

import java.util.List;

import org.geowe.client.local.main.tool.map.catalog.layerset.LayerSet;
import org.geowe.client.local.main.tool.map.catalog.model.LayerDef;

/**
 * Interfaz que representa un catalogo de capas, entendiendose como un conjunto o
 * lista de definiciones de capas que pueden ser gestionadas por la aplicacion 
 * agregando o eliminando capas.
 * 
 * @author Atanasio Muñoz
 *
 */
public interface LayerCatalog {
	
	LayerDef getLayer(String layerName);
	
	List<LayerDef> getAllLayers();
	
	void addLayer(LayerDef layer);
	
	void addLayers(List<LayerDef> layers);
	
	void addLayers(LayerSet layerSet);
	
	void removeLayer(String layerName);
}