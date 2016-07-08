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

import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.OSM;

/**
 * Definicion de una capa raster del proveedor OpenStreetMap
 * 
 * @author Atanasio Muñoz
 *
 */
public class OsmLayerDef extends LayerDef{
	private static final long serialVersionUID = 3223968151195102847L;

	public static final String OSM_CYCLEMAP = "CycleMap";
	public static final String OSM_MAPNIK = "Mapnik";
	public static final String OSM_MAPLINT = "Maplint";
	
	private String osmMap;
	
	@Override
	public String getType() {
		return RASTER_TYPE;
	}	
	
	@Override
	public Layer getLayer() {
		OSM layer = null;
		
		if (osmMap.equals(OSM_CYCLEMAP)) {
			layer = OSM.CycleMap(getName());		
		} else if (osmMap.equals(OSM_MAPNIK)) {			
			layer = OSM.Mapnik(getName());					
		} else {
			layer = OSM.Maplint(getName());
		}
		
		layer.setIsBaseLayer(false);
		
		return layer;
	}

	public String getOsmMap() {
		return osmMap;
	}

	public void setOsmMap(String osmMap) {
		this.osmMap = osmMap;
	}
	
}
