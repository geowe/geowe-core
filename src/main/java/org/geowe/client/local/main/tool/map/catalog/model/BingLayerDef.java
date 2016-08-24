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

import org.gwtopenmaps.openlayers.client.layer.Bing;
import org.gwtopenmaps.openlayers.client.layer.BingOptions;
import org.gwtopenmaps.openlayers.client.layer.BingType;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.protocol.ProtocolType;

/**
 * Definicion de una capa raster del proveedor Bing.
 * 
 * @author Atanasio Muñoz
 *
 */
public class BingLayerDef extends LayerDef{
	private static final long serialVersionUID = -5252615636942381631L;	
	private static final String KEY = BingConstants.INSTANCE.bingKey();
	private BingType mapType;
	
	@Override
	public String getType() {
		return RASTER_TYPE;
	}		
	
	@Override
	public Layer getLayer() {
		BingOptions bingOptions = new BingOptions(getName(), KEY,
				mapType);
		bingOptions.setProtocol(ProtocolType.HTTP);
		bingOptions.setIsBaseLayer(false);
		
        Bing bingLayer = new Bing(bingOptions);
        
        return bingLayer;		
	}

	public BingType getMapType() {
		return mapType;
	}

	public void setMapType(BingType mapType) {
		this.mapType = mapType;
	}

}
