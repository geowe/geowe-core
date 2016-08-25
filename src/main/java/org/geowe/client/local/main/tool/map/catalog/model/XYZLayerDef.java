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
import org.gwtopenmaps.openlayers.client.layer.XYZ;
import org.gwtopenmaps.openlayers.client.layer.XYZOptions;

/**
 * Definicion de una capa raster del proveedor ESRI.
 * 
 * @author Atanasio Muñoz
 *
 */
public class XYZLayerDef extends LayerDef{
	private static final long serialVersionUID = 2520310257932517715L;

	private String url;
	private String layerName;
	
	
	@Override
	public String getType() {
		return RASTER_TYPE;
	}		
	
	@Override
	public Layer getLayer() {				
		XYZOptions layerOptions = new XYZOptions();		
		layerOptions.setSphericalMercator(true);
		
		XYZ xyzLayer = new XYZ(layerName, url, layerOptions);
		xyzLayer.setIsBaseLayer(false);

		return xyzLayer;	
	}
	
	@Override
	public void setName(String name) {
		/**
		 * Comprobacion para que el name siempre sea igual al esriLayerName,
		 * con objeto de que la capa sea correctamente identificada en el
		 * catalogo y en el layerManager y no siga apareciendo como disponible
		 * en el catalogo aunque ya haya sido agregada al layerManager
		 */
		if(layerName == null || name.equals(layerName)) {
			super.setName(name);
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.setName(layerName);
		this.layerName = layerName;
	}
}
