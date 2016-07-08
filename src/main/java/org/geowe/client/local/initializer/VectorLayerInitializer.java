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
package org.geowe.client.local.initializer;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.LayerTree;
import org.geowe.client.local.layermanager.toolbar.VectorLayerToolBar;
import org.geowe.client.local.main.map.GeoMap;
import org.gwtopenmaps.openlayers.client.layer.Layer;

import com.google.gwt.core.client.GWT;

@ApplicationScoped
public class VectorLayerInitializer {
	private static final String VECTOR_ROOT_NAME = "Vector layers";	
	@Inject
	private GeoMap geoMap;
	@Inject
	private LayerManagerWidget layerManagerWidget;
	@Inject
	private VectorLayerToolBar vectorLayerToolBar;
	
	public void initialize() {
		final List<Layer> vectorLayers = new ArrayList<Layer>();

		final LayerTree vectorialLayerTree = GWT.create(LayerTree.class);
		vectorialLayerTree.createTree(vectorLayers, VECTOR_ROOT_NAME);
		vectorialLayerTree.setToolbar(vectorLayerToolBar);
		vectorialLayerTree.setMap(geoMap);
		vectorialLayerTree.setCell(layerManagerWidget.getTreeClickEvent());
		layerManagerWidget.add(LayerManagerWidget.VECTOR_TAB,
				vectorialLayerTree);		
	}	
}