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

import org.geowe.client.local.initializer.GeoMapInitializer;
import org.geowe.client.local.main.map.GeoMap;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.TransitionEffect;
import org.gwtopenmaps.openlayers.client.layer.WMTS;
import org.gwtopenmaps.openlayers.client.layer.WMTSOptions;


/**
 * Definicion de una capa raster Web Map Tile Service (WMTS)
 * 
 * @author jose@geowe.org
 * @since 24/08/2016
 */
public class WmtsLayerDef extends WmsLayerDef {	
	private static final long serialVersionUID = 2330903692450317231L;
	private String tileMatrixSet;
	
	public String getTileMatrixSet() {
		return tileMatrixSet;
	}
	public void setTileMatrixSet(final String tileMatrixSet) {
		this.tileMatrixSet = tileMatrixSet;
	}
	
	@Override
	public Layer getLayer() {
		
		final WMTSOptions wmtsOptions = new WMTSOptions(getUrl(), getLayerName(),
                "", getTileMatrixSet());
        wmtsOptions.setName(getLayerName());
        wmtsOptions.setIsBaseLayer(Boolean.FALSE);
        wmtsOptions.setFormat(getFormat());
        wmtsOptions.setDisplayOutsideMaxExtent(true);	
        wmtsOptions.setNumZoomLevels(GeoMapInitializer.MAX_NUM_ZOOM_LEVEL);
        wmtsOptions.setProjection(GeoMap.INTERNAL_EPSG);
        wmtsOptions.setTransitionEffect(TransitionEffect.RESIZE);
        wmtsOptions.setAttribution(getAttribution());

        final WMTS wmtsLayer = new WMTS(wmtsOptions);		
		return wmtsLayer;			
	}
}