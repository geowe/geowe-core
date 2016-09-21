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

import org.geowe.client.local.AppClientProperties;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.LayerTree;
import org.geowe.client.local.layermanager.toolbar.RasterLayerToolBar;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.map.catalog.AppLayerCatalog;
import org.geowe.client.local.main.tool.map.catalog.model.LayerDef;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.layer.EmptyLayer;
import org.gwtopenmaps.openlayers.client.layer.Layer;

import com.google.gwt.core.client.GWT;

/**
 * Responsible for initializing the default Raster Layers.
 * 
 * @author jose@geowe.org, rafa@geowe.org, ata@geowe.org
 * @since 13-09-2016
 * @author rafa@geowe.org Configurable startup layer. Must exists in Catalog
 *
 */
@ApplicationScoped
public class RasterLayerInitializer {
	private static final String RASTER_ROOT_NAME = "Raster layers";

	@Inject
	private GeoMap geoMap;
	@Inject
	private RasterLayerToolBar rasterLayerToolBar;
	@Inject
	private LayerManagerWidget layerManagerWidget;
	@Inject
	private AppLayerCatalog appLayerCatalog;
	@Inject
	private AppClientProperties appClientProperties;

	public void initialize() {

		geoMap.getMap().addLayer(createEmptyBaseLayer());

		final Layer googleSatelliteLayer = createStartupLayer();
		geoMap.getMap().addLayer(googleSatelliteLayer);

		final List<Layer> wmsLayers = new ArrayList<Layer>();
		wmsLayers.add(googleSatelliteLayer);

		layerManagerWidget.add(LayerManagerWidget.RASTER_TAB,
				createRasterLayerTree(wmsLayers));

		geoMap.centerMap(-2, 37, "EPSG:4326", geoMap.getMap().getProjection());
	}

	private EmptyLayer createEmptyBaseLayer() {
		final EmptyLayer.Options emptyLayerOptions = new EmptyLayer.Options();
		emptyLayerOptions.setProjection(GeoMap.INTERNAL_EPSG);
		emptyLayerOptions.setAttribution("GeoWE: GeoData Web Editing. "
				+ UIMessages.INSTANCE.attributionEmptyLayer());
		emptyLayerOptions.setIsBaseLayer(true);
		return new EmptyLayer("Empty Base layer", emptyLayerOptions);
	}

	private Layer createStartupLayer() {
		final LayerDef layerDef = appLayerCatalog.getLayer(appClientProperties
				.getStringValue("startupLayer"));
		final Layer startupLayer = layerDef.getLayer();
		startupLayer.setIsBaseLayer(false);
		return startupLayer;
	}

	private LayerTree createRasterLayerTree(final List<Layer> wmsLayers) {
		final LayerTree rasterLayerTree = GWT.create(LayerTree.class);
		rasterLayerTree.setMap(geoMap);
		rasterLayerTree.createTree(wmsLayers, RASTER_ROOT_NAME);
		rasterLayerTree.setToolbar(rasterLayerToolBar);
		rasterLayerTree.setCell(layerManagerWidget.getTreeClickEvent());
		return rasterLayerTree;
	}
}
