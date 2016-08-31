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
import javax.inject.Inject;

import org.geowe.client.local.main.ZoomStatusWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.map.catalog.AppLayerCatalog;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.event.MapZoomListener;
import org.gwtopenmaps.openlayers.client.layer.Layer;

/**
 * Responsible for listen zoom Map
 * 
 * @author geowe.org
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class GeoMapZoomListener implements MapZoomListener {

	public static final String RED = "#FF0000";
	public static final String BLACK = "#000000";
	@Inject
	private GeoMap geoMap;

	@Inject
	private ZoomStatusWidget zoomStatusWidget;

	@Override
	public void onMapZoom(MapZoomEvent eventObject) {

		zoomStatusWidget.updateZoomLevel(geoMap.getMap().getZoom());

		final Layer googleSatelliteLayer = geoMap.getMap().getLayerByName(
				AppLayerCatalog.GOOGLE_SATELLITE);
		if ((googleSatelliteLayer != null) && (geoMap.getMap().getZoom() > 18)) {
			zoomStatusWidget.updateTitle(RED, UIMessages.INSTANCE
					.zoomWarning(geoMap.getMap().getZoom(),
							AppLayerCatalog.GOOGLE_SATELLITE));
		} else {
			zoomStatusWidget.updateTitle(BLACK, UIMessages.INSTANCE.currentZoomLevelText());
		}
	}

}