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
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Layer;

/**
 * Merge Geoprocess representa la operación de procesamiento espacial responsable de juntar los elementos 
 * de la capa vectorial de entrada con los elementos de la capa de overlay, permitiendo solapes entre ellos.
 * 
 * @author jose@geowe.org
 *
 */

@ApplicationScoped
public class MergeGeoprocess extends Geoprocess {
	private static final String MERGE_LAYER_NAME = "MergeLayer";	
	
	public MergeGeoprocess() {
		setId(6);
		setName(UIMessages.INSTANCE.merge());		
	}
	
	@Override
	public void process() {		
		VectorLayer layerResult = getResultLayer(new ArrayList<String>());
		layerResult.addFeatures(duplicate(getInput().getInputLayer()));
		layerResult.addFeatures(duplicate(getInput().getOverlayLayer()));
		finishProgressbar();
		onSucess(layerResult);
	}
	
	private VectorFeature[] duplicate(VectorLayer layer) {
		List<VectorFeature> features = new ArrayList<VectorFeature>();

		for (VectorFeature vf : layer.getFeatures()) {
			VectorFeature vf2 = vf.clone();
			features.add(vf2);
		}

		return features.toArray(new VectorFeature[features.size()]);
	}

	@Override
	public void onSucess(VectorLayer layerResult) {
		LayerManagerWidget layerManager = getInput().getLayerManager();
		Layer layer = layerManager.getVector(MERGE_LAYER_NAME);
		if (layer != null) {
			layerManager.removeLayer(LayerManagerWidget.VECTOR_TAB, layer);
		}
		layerResult.setName(MERGE_LAYER_NAME);		
		layerManager.addVector(layerResult);		
	}

	@Override
	public void onError(String message) {
		messageDialogBuilder.createWarning(UIMessages.INSTANCE.fail(), "error: " + message).show();		
	}
}