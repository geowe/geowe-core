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

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.layer.Layer;

/**
 * Union Geoprocess representa la operación de procesamiento espacial responsable de unir las geometrías de la capa de entrada con las
 * geometrías de la capa de overlay. Esta operación no podrá tener elementos solapados y respeta los límites de las fronteras de los 
 * elementos de las capas.
 * 
 * @author jose@geowe.org
 *
 */

@ApplicationScoped
public class UnionGeoprocess extends Geoprocess {
	private static final String UNION_LAYER_NAME = "UnionLayer";	
	
	public UnionGeoprocess() {
		setId(5);
		setName(UIMessages.INSTANCE.union());		
	}
	
	@Override
	public void process() {		
		jtsServiceAsync.getUnion(getInput().getWKTInputLayer(), getInput().getWKTOverlayLayer(), getDefaultAsyncCallback());		
	}
	
	@Override
	public void onSucess(VectorLayer layerResult) {
		LayerManagerWidget layerManager = getInput().getLayerManager();
		Layer layer = layerManager.getVector(UNION_LAYER_NAME);
		if (layer != null) {
			layerManager.removeLayer(LayerManagerWidget.VECTOR_TAB, layer);
		}
		layerResult.setName(UNION_LAYER_NAME);		
		layerManager.addVector(layerResult);		
	}

	@Override
	public void onError(String message) {
		messageDialogBuilder.createWarning(UIMessages.INSTANCE.fail(), "error: " + message).show();		
	}
}