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
package org.geowe.client.local.main.tool.info;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.LayerTool;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.resources.client.ImageResource;

/**
 * Responsible to select a feature
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class SelectVectorFeatureTool extends LayerTool implements FeatureTool {

	private VectorFeature selectedFeature;

	private VectorLayer layer;
	
	@Inject
	public SelectVectorFeatureTool(LayerManagerWidget layerTreeWidget, GeoMap geoMap) {
		super(layerTreeWidget, geoMap);
	}
	
	@Override
	public VectorFeature getSelectedFeature() {		
		return selectedFeature;
	}

	@Override
	public void setSelectedFeature(VectorFeature selectedFeature) {
		this.selectedFeature = selectedFeature;		
		setEnabled(selectedFeature != null);
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.selectVectorFeatureToolText();
	}
	
	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.select24();
	}		

	@Override
	public void onClick() {
		SelectFeature selectFeatureControl = new SelectFeature((Vector)
				layerManagerWidget.getSelectedLayer(LayerManagerWidget.VECTOR_TAB));
		geoMap.addControl(selectFeatureControl);
		selectFeatureControl.activate();
		selectFeatureControl.select(selectedFeature);
		selectFeatureControl.unselectAll(selectedFeature);
		selectFeatureControl.deactivate();
		geoMap.getMap().removeControl(selectFeatureControl);
	}

	// TODO: solo selecciona el primer elemento seleccionado
	@Override
	public void setSelectedFeatures(List<VectorFeature> selectedFeatures) {
		this.selectedFeature = selectedFeatures.get(0);
	}

	@Override
	public void setSelectedLayer(VectorLayer layer) {
		this.layer = layer;
	}
}
