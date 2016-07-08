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

import org.gwtopenmaps.openlayers.client.layer.GoogleV3;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3MapType;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3Options;
import org.gwtopenmaps.openlayers.client.layer.Layer;

/**
 * Definicion de una capa raster del proveedor Google.
 * 
 * @author Atanasio Muñoz
 *
 */
public class GoogleLayerDef extends LayerDef{
	private static final long serialVersionUID = -1229800335189382790L;
	
	private GoogleV3MapType mapType;
		
	@Override
	public String getType() {
		return RASTER_TYPE;
	}	
	
	@Override
	public Layer getLayer() {
		GoogleV3Options googleOptions = new GoogleV3Options();		
		googleOptions.setIsBaseLayer(false);
		googleOptions.setDisplayOutsideMaxExtent(true);
		googleOptions.setSmoothDragPan(true);
		googleOptions.setType(mapType);
		GoogleV3 layer = new GoogleV3(getName(), googleOptions);		
		
		return layer;	
	}

	public GoogleV3MapType getMapType() {
		return mapType;
	}

	public void setMapType(GoogleV3MapType mapType) {
		this.mapType = mapType;
	}		
}
