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
package org.geowe.client.local.main.tool.zoom;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Collection;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * Zoom to selected elements
 * 
 * @author geowe.org
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class ZoomToSeletionTool extends ButtonTool {

	private final GeoMap geoMap;

	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	@Inject
	public ZoomToSeletionTool(final GeoMap geoMap, final LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.zoomToSeletionToolText(),
				ImageProvider.INSTANCE.zoomToSelection(), layerManager);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.zoomToSeletionToolText(),
				UIMessages.INSTANCE.zoomToSeletionToolTip(), Side.LEFT));
		this.geoMap = geoMap;
		setEnabled(false);
	}

	@Override
	protected void onRelease() {
		final VectorFeature[] features = layer.getSelectedFeatures();
		if (features == null) {
			messageDialogBuilder.createError(UIMessages.INSTANCE.warning(),
					UIMessages.INSTANCE.selectAtLeast(1)).show();
		} else {
			geoMap.getMap().zoomToExtent(
					getGeometryCollection(features).getBounds());
		}

	}

	private Collection getGeometryCollection(final VectorFeature... features) {
		final List<Geometry> geometries = getGeometries(features);
		final Collection geometryCollection = new Collection(
				geometries.toArray(new Geometry[geometries.size()]));
		return geometryCollection;
	}

	private List<Geometry> getGeometries(final VectorFeature... features) {
		final List<Geometry> geometries = new ArrayList<Geometry>();
		for (final VectorFeature feature : features) {
			geometries.add(feature.getGeometry());
		}
		return geometries;
	}
}