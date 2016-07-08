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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.info.LayerInfoDialog;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;

import com.google.gwt.resources.client.ImageResource;

/**
 * Responsible for displaying the information of the selected layer
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class InfoLayerTool extends LayerTool {

	@Inject
	private LayerInfoDialog layerInfoDialog;
	@Inject
	public InfoLayerTool(LayerManagerWidget layerTreeWidget, GeoMap geoMap) {
		super(layerTreeWidget, geoMap);
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.infoLayerToolText();
	}

	@Override
	public void onClick() {
		VectorLayer layer = (VectorLayer) getSelectedLayer();
		layerInfoDialog.setSelectedLayer(layer);
		layerInfoDialog.show();
	}
	
	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.info24();
	}


}
