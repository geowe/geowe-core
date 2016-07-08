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
package org.geowe.client.local.main.tool.spatial.geoprocess;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;

/**
 * InputGeoprocess representa a la información de entrada para realizar las operaciones de geoprocesamiento
 * 
 * @author jose@geowe.org
 *
 */

@ApplicationScoped
public class InputGeoprocess implements IInputGeoprocess {
	private VectorLayer inputLayer;
	private VectorLayer overlayLayer;
	private LayerManagerWidget layerManagerWidget;
	private double distance;
	
	
	@Override
	public VectorLayer getInputLayer() {
		return inputLayer;
	}
	
	@Override
	public void setInputLayer(VectorLayer inputLayer) {
		this.inputLayer = inputLayer;
	}
	
	@Override
	public VectorLayer getOverlayLayer() {
		return overlayLayer;
	}
	
	@Override
	public void setOverlayLayer(VectorLayer overlayLayer) {
		this.overlayLayer = overlayLayer;
	}
	
	@Override
	public double getDistance() {
		return distance;
	}
	
	@Override
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	@Override
	public List<String> getWKTInputLayer() {
		return getWKT(inputLayer.getFeatures());
	}
	
	
	@Override
	public List<String> getWKTOverlayLayer() {
		return getWKT(overlayLayer.getFeatures());
	}
	
	private List<String> getWKT(VectorFeature... features) {
		WKT wktFormat = new WKT();
		List<String> wktLayer = new ArrayList<String>();
		for (VectorFeature feature : features) {
			String wkt = wktFormat.write(feature);
			wktLayer.add(wkt);
		}
		return wktLayer;
	}
	
	@Override
	public void setLayerManager(LayerManagerWidget layerManagerWidget) {
		this.layerManagerWidget = layerManagerWidget;		
	}
	
	
	@Override
	public LayerManagerWidget getLayerManager() {
		return layerManagerWidget;
	}
}