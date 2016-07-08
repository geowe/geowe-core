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
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.TransitionEffect;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.layer.WMSOptions;
import org.gwtopenmaps.openlayers.client.layer.WMSParams;

/**
 * Definicion de una capa raster WMS
 * 
 * @author Atanasio Muñoz
 *
 */
public class WmsLayerDef extends LayerDef{
	private static final long serialVersionUID = 2520310257932517715L;

	private String url;
	private String wmsLayerName;
	private String epsg;
	private String format;	
	
	@Override
	public String getType() {
		return RASTER_TYPE;
	}		
	
	@Override
	public Layer getLayer() {
		WMSParams wmsParams = new WMSParams();
		wmsParams.setFormat(format);
		wmsParams.setLayers(wmsLayerName);
		wmsParams.setTransparent(true);		
		
		WMSOptions wmsLayerParams = new WMSOptions();
		wmsLayerParams.setProjection(epsg);
		wmsLayerParams.setTransitionEffect(TransitionEffect.RESIZE);
		wmsLayerParams.setDisplayOutsideMaxExtent(true);			
		wmsLayerParams.setNumZoomLevels(GeoMapInitializer.MAX_NUM_ZOOM_LEVEL);
		wmsLayerParams.setIsBaseLayer(true);
		
		WMS wmsLayer = new WMS(getName(), url, wmsParams, wmsLayerParams);
		wmsLayer.setIsBaseLayer(false);

		return wmsLayer;	
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWmsLayerName() {
		return wmsLayerName;
	}

	public void setWmsLayerName(String wmsLayerName) {
		this.wmsLayerName = wmsLayerName;
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
}
