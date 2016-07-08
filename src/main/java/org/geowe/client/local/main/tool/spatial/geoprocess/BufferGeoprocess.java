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

/**
 * Buffer Geoprocess representa la operación de procesamiento espacial responsable de generar el área de influencia de los elementos
 * vectoriales especificados como parámetros de entrada.
 * 
 * @author jose@geowe.org
 *
 */

@ApplicationScoped
public class BufferGeoprocess extends Geoprocess {	
	private static final String BUFFER_LAYER_NAME = "BufferLayer";
	
	public BufferGeoprocess() {
		setId(4);
		setName(UIMessages.INSTANCE.buffer());		
	}
	
	@Override
	public void process() {			
		jtsServiceAsync.getBuffer(getInput().getWKTInputLayer(), getInput().getDistance(), getDefaultAsyncCallback());
	}

	@Override
	public void onSucess(VectorLayer layerResult) {
		LayerManagerWidget layerManager = getInput().getLayerManager();
		String inputLayerName = getInput().getInputLayer().getName();
		VectorLayer layer = (VectorLayer) layerManager.getVector(inputLayerName
				+ BUFFER_LAYER_NAME);
		if (layer == null) {
			layerResult.setName(inputLayerName + BUFFER_LAYER_NAME);
			layerManager.addVector(layerResult);
		} else {
			layer.addFeatures(layerResult.getFeatures());
		}
	}

	@Override
	public void onError(String message) {
		messageDialogBuilder.createWarning(UIMessages.INSTANCE.fail(), "error: " + message).show();		
	}
}