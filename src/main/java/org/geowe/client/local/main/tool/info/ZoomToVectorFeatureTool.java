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
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;

import com.google.gwt.resources.client.ImageResource;

/**
 * Herramienta para hacer zoon a la feature seleccionada en la lista de features de la capa
 * @author atamunoz
 *
 */
@ApplicationScoped
public class ZoomToVectorFeatureTool extends LayerTool implements FeatureTool {
	
	private VectorFeature selectedFeature;

	@Inject
	public ZoomToVectorFeatureTool(LayerManagerWidget layerTreeWidget, GeoMap geoMap) {
		super(layerTreeWidget, geoMap);
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.zoomToVectorFeatureToolText();
	}
	
	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.zoomToFeature24();
	}

	@Override
	public void onClick() {			
		Geometry geom = selectedFeature.getGeometry();
		if(geom.getClassName().equals(Geometry.POINT_CLASS_NAME)){
			geoMap.getMap().setCenter(geom.getBounds().getCenterLonLat(), 18);
		}else{
			geoMap.getMap().zoomToExtent(geom.getBounds());	
		}
	}

	public VectorFeature getSelectedFeature() {
		return selectedFeature;
	}

	public void setSelectedFeature(VectorFeature selectedFeature) {
		this.selectedFeature = selectedFeature;
		setEnabled(selectedFeature != null);
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
