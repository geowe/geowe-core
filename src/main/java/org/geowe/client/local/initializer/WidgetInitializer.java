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

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.LayerTree;
import org.geowe.client.local.main.BasicToolBar;
import org.geowe.client.local.main.GeocodingPanelWidget;
import org.geowe.client.local.main.MenuPanelWidget;
import org.geowe.client.local.main.StatusPanelWidget;
import org.geowe.client.local.style.VectorLayerStyleWidget;
import org.jboss.errai.ioc.client.api.builtin.RootPanelProvider;

import com.google.inject.Inject;
@ApplicationScoped
public class WidgetInitializer {

	@Inject
	private RootPanelProvider rootPanelProvider;
	
	@Inject
	private MenuPanelWidget menuPanelWidget;
	
	@Inject
	private LayerManagerWidget layerManagerWidget;
	
	@Inject
	private VectorLayerStyleWidget vectorLayerStyleWidget;
	
	@Inject 
	private StatusPanelWidget statusPanelWidget;
	
	@Inject 
	private BasicToolBar basicToolBar;	
	
	@Inject 
	private GeocodingPanelWidget geocodingPanelWidget;
	
	public void initialize(){
		rootPanelProvider.get().add(menuPanelWidget);
		rootPanelProvider.get().add(layerManagerWidget);
		rootPanelProvider.get().add(vectorLayerStyleWidget);
		rootPanelProvider.get().add(basicToolBar);
		rootPanelProvider.get().add(geocodingPanelWidget);
		rootPanelProvider.get().add(statusPanelWidget);
		
		iniLayerTree(LayerManagerWidget.RASTER_TAB);
		iniLayerTree(LayerManagerWidget.VECTOR_TAB);
		
		layerManagerWidget.updateStatusBar();		
		statusPanelWidget.setVectorLayers(layerManagerWidget.
				getLayerTree(LayerManagerWidget.VECTOR_TAB).getLayers());
	}

	private void iniLayerTree(final String tabName) {
		final LayerTree layerTree = layerManagerWidget.getLayerTree(tabName);
		layerTree.getTree().expandAll();
		layerTree.getTree().setCheckedSelection(layerTree.getTree().getStore().getAll());		
	}
}