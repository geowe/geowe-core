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
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.info.Info;

/**
 * Responsible for zoom to the maximum of a vector layer.
 * 
 * @author rafa@geowe.org
 * @since 14/10/2016 fix issue 208
 *
 */
@ApplicationScoped
public class ZoomToVectorLayerTool extends LayerTool {

	@Inject
	public ZoomToVectorLayerTool(LayerManagerWidget layerTreeWidget,
			GeoMap geoMap) {
		super(layerTreeWidget, geoMap);
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.zoomToVectorLayerToolText();
	}

	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.zoomToLayer();
	}

	@Override
	public void onClick() {
		Vector layer = (Vector) getSelectedLayer();
		Bounds layerExtent = layer.getDataExtent();
		if (layerExtent == null) {
			Info.display(UIMessages.INSTANCE.zoomToVectorLayerToolText(),
					UIMessages.INSTANCE.emptyVectorLayer());
		}
		geoMap.getMap().zoomToExtent(layerExtent);
	}
}
