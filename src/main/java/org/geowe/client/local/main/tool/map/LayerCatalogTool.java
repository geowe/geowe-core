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
package org.geowe.client.local.main.tool.map;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.main.tool.map.catalog.AppLayerCatalog;
import org.geowe.client.local.main.tool.map.catalog.dialog.LayerCatalogDialog;
import org.geowe.client.local.main.tool.map.catalog.model.MultiLayerDef;
import org.geowe.client.local.main.tool.map.catalog.model.LayerDef;
import org.geowe.client.local.main.tool.map.catalog.model.WfsVectorLayerDef;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.info.Info;

/**
 * 
 * @author geowe.org
 *
 */
@ApplicationScoped
public class LayerCatalogTool extends ButtonTool {

	@Inject
	private LayerCatalogDialog layerCatalogDialog;
	@Inject
	private LayerManagerWidget layerManagerWidget;
	@Inject
	private ClientTaskManager taskManager;
	@Inject
	private AppLayerCatalog appLayerCatalog;		
	private final GeoMap geoMap;
	private List<LayerDef> addedLayers;
	private ProgressBarDialog autoMessageBox;

	@Inject
	public LayerCatalogTool(final GeoMap geoMap) {
		super(UIMessages.INSTANCE.layerCatalogToolText(),
				ImageProvider.INSTANCE.layerCatalog());
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.layerCatalogToolText(),
				UIMessages.INSTANCE.layerCatalogToolTip(), Side.LEFT));
		this.geoMap = geoMap;
	}

	@Override
	protected void onRelease() {
		final Layer[] mapLayers = geoMap.getMap().getLayers();
		final List<LayerDef> catalogLayers = appLayerCatalog.getAllLayers();
		final List<LayerDef> availableLayers = new ArrayList<LayerDef>();
		addedLayers = new ArrayList<LayerDef>();

		for (final LayerDef layerDef : catalogLayers) {
			if (layerAlreadyExists(layerDef.getName(), mapLayers)) {
				addedLayers.add(layerDef);				
			} else {
				availableLayers.add(layerDef);
			}
		}

		layerCatalogDialog.initialize(availableLayers, addedLayers);
		layerCatalogDialog.show();
	}

	@PostConstruct
	private void addDialogHideHandler() {
		layerCatalogDialog.addDialogHideHandler(new DialogHideHandler() {
			@Override
			public void onDialogHide(final DialogHideEvent event) {
				if (event.getHideButton().equals(PredefinedButton.OK)) {
					autoMessageBox = new ProgressBarDialog(
							false, UIMessages.INSTANCE.processing());
					autoMessageBox.show();
					taskManager.execute(new Runnable() {

						@Override
						public void run() {
							addSelectedLayers();
						}
					});

				}
			}
		});
	}

	private void addSelectedLayers() {
		final Layer[] mapLayers = geoMap.getMap().getLayers();
		addedLayers = layerCatalogDialog.getAddedLayers();

		for (final LayerDef layerDef : addedLayers) {
			try {
				
				if (!layerAlreadyExists(layerDef.getName(), mapLayers)) {
					if(layerDef instanceof MultiLayerDef) {
						layerDef.getLayer(); //Internamente realiza la carga de capas						
					}
					else {
						final Layer newLayer = layerDef.getLayer();
						addLayer(newLayer, layerDef);
					}					
				}
			} catch (Exception ex) {
				Info.display(
						UIMessages.INSTANCE.layerCatalogToolText(),
						UIMessages.INSTANCE.errorLoadingLayer(layerDef.getName()));
			}
		}
		autoMessageBox.hide();
	}

	private boolean layerAlreadyExists(final String layerName, final Layer... layers) {
		boolean exists = false;

		for (int i = 0; i < layers.length; i++) {
			if (layers[i].getName().equals(layerName)) {
				exists = true;
				break;
			}
		}

		return exists;
	}
	
	private void addLayer(final Layer newLayer, final LayerDef layerDef) {
		if (newLayer == null) {
			if (!(layerDef instanceof WfsVectorLayerDef)) {
				Info.display(UIMessages.INSTANCE.layerCatalogToolText(),
						UIMessages.INSTANCE.errorLoadingLayer(layerDef.getName()));
			}			
		} 
		else {
			if (layerDef.getType().equals(LayerDef.RASTER_TYPE)) {
				layerManagerWidget.addRaster(newLayer);
				layerManagerWidget.setSelectedLayer(
						LayerManagerWidget.RASTER_TAB, newLayer);
			} else {
				layerManagerWidget.addVector(newLayer);
				layerManagerWidget.setSelectedLayer(
						LayerManagerWidget.VECTOR_TAB, newLayer);
			}
		}
	}
}
