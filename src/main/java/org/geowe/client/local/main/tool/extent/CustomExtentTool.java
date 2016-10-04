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
package org.geowe.client.local.main.tool.extent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Show map current extent
 * 
 * @author jose@geowe.org
 * @since 03-10-2016
 * @author rafa@geowe.org Create layer from VectorLayerFactory.
 *
 */
@ApplicationScoped
public class CustomExtentTool extends ButtonTool {
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	@Inject
	private CustomExtentDialog customExtentDialog;
	@Inject
	private LayerManagerWidget layerManager;
	@Inject
	private GeoMap geoMap;

	public CustomExtentTool() {
		super(UIMessages.INSTANCE.nameCustomExtentTool(),
				ImageProvider.INSTANCE.customExtension24());

		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.titleCustomExtentToolTip(),
				UIMessages.INSTANCE.descriptionCustomExtentToolTip(), Side.LEFT));
	}

	@PostConstruct
	private void initialize() {

		customExtentDialog.getAddToMapButton().addSelectHandler(
				new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (!isBBoxEmpty() && has4Coordinates()) {
							Bounds bounds = getBounds();

							Geometry geom = bounds.toGeometry();
							geom.transform(new Projection("EPSG:4326"),
									new Projection(geoMap.getMap()
											.getProjection()));
							VectorFeature vf = new VectorFeature(geom);

							VectorLayer bboxLayer = VectorLayerFactory
									.createEmptyVectorLayer(createBBoxLayerConfig());
							bboxLayer.addFeature(vf);

							layerManager.addVector(bboxLayer);
						}
					}
				});
	}

	private boolean isBBoxEmpty() {
		boolean isEmpty = false;
		String bbox = customExtentDialog.getBbox();
		if (bbox.isEmpty()) {
			messageDialogBuilder.createError(UIMessages.INSTANCE.warning(),
					UIMessages.INSTANCE.insertCoordinatesCustomExtent()).show();
			isEmpty = true;
		}
		return isEmpty;
	}

	private boolean has4Coordinates() {
		boolean has4coordinates = true;
		String[] coordinates = customExtentDialog.getBbox().split("\\,");
		if (coordinates.length != 4) {
			messageDialogBuilder.createError(UIMessages.INSTANCE.warning(),
					UIMessages.INSTANCE.incorrectCoordinatesCustomExtent())
					.show();
			has4coordinates = false;
		}
		return has4coordinates;
	}

	private Bounds getBounds() {
		String[] coordinates = customExtentDialog.getBbox().split("\\,");
		double lowerLeftX = Double.parseDouble(coordinates[0]);
		double lowerLeftY = Double.parseDouble(coordinates[1]);
		double upperRightX = Double.parseDouble(coordinates[2]);
		double upperRightY = Double.parseDouble(coordinates[3]);

		return new Bounds(lowerLeftX, lowerLeftY, upperRightX, upperRightY);
	}

	private VectorLayerConfig createBBoxLayerConfig() {
		VectorLayerConfig layerConfig = new VectorLayerConfig();
		layerConfig.setLayerName("BBox");
		layerConfig.setEpsg(GeoMap.INTERNAL_EPSG);
		return layerConfig;
	}

	@Override
	protected void onRelease() {
		customExtentDialog.initialize();
	}
}
