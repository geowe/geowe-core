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
import org.geowe.client.local.main.tool.featureinfo.FeatureInfoDialog;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.resources.client.ImageResource;

/**
 * Show info for a Feature
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class SingleFeatureInfoTool extends LayerTool implements FeatureTool {

	@Inject
	private FeatureInfoDialog infoDialog;

	private VectorFeature selectedFeature;

	@Inject
	public SingleFeatureInfoTool(LayerManagerWidget layerTreeWidget,
			GeoMap geoMap) {
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
		return UIMessages.INSTANCE.singleFeatureInfoToolText();
	}

	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.info24();
	}

	@Override
	public void onClick() {
		infoDialog.setVectorFeature((VectorLayer) getSelectedVectorLayer(),
				selectedFeature);

		if (!infoDialog.isVisible()) {
			infoDialog.show();
		}
	}

	// TODO: solo selecciona el primer elemento seleccionado
	@Override
	public void setSelectedFeatures(List<VectorFeature> selectedFeatures) {
		this.selectedFeature = selectedFeatures.get(0);
	}

	@Override
	public void setSelectedLayer(VectorLayer layer) {
		// TODO Auto-generated method stub

	}

}
