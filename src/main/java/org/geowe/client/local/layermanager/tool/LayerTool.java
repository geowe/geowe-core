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
package org.geowe.client.local.layermanager.tool;

import javax.inject.Inject;

import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.layer.Layer;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Tool to incorporate a tool in the toolbar (preferably related to layer management)
 * 
 * @author geowe.org
 *
 */
public abstract class LayerTool extends TextButton {

	protected LayerManagerWidget layerManagerWidget;
	protected GeoMap geoMap;

	public LayerTool(LayerManagerWidget layeManagerWidget, GeoMap geoMap) {
		super();
		setIcon();
		this.layerManagerWidget = layeManagerWidget;
		this.geoMap = geoMap;
		initializeHandler();
	}
	
	public LayerManagerWidget getLayerManagerWidget() {
		return layerManagerWidget;
	}

	@Inject
	public void setLayerManagerWidget(LayerManagerWidget layerTreeWidget) {
		this.layerManagerWidget = layerTreeWidget;
	}

	public GeoMap getGeoMap() {
		return geoMap;
	}

	@Inject
	public void setGeoMap(GeoMap geoMap) {
		this.geoMap = geoMap;
	}

	private void setIcon() {
		if (getIcon() == null) {
			setText(getName());
		} else {
			setIcon(getIcon());
			setToolTip(getName());
		}
	}

	public abstract String getName();	
	public abstract void onClick();

	private void initializeHandler() {
		addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				onClick();
			}
		});
	}

	protected Layer getSelectedLayer() {
		return layerManagerWidget.getSelectedLayer(layerManagerWidget.getSelectedTabName());
	}

	protected VectorLayer getSelectedVectorLayer() {
		return (VectorLayer) layerManagerWidget
				.getSelectedLayer(LayerManagerWidget.VECTOR_TAB);
	}

	protected Layer getSelectedRasterLayer() {
		return layerManagerWidget
				.getSelectedLayer(LayerManagerWidget.RASTER_TAB);
	}
}
