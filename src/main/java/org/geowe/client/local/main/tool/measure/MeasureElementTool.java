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
package org.geowe.client.local.main.tool.measure;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.map.MapControlFactory;
import org.geowe.client.local.main.tool.ToggleTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeature.ClickFeatureListener;
import org.gwtopenmaps.openlayers.client.control.SelectFeature.UnselectFeatureListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.geometry.LineString;
import org.gwtopenmaps.openlayers.client.geometry.LinearRing;
import org.gwtopenmaps.openlayers.client.geometry.Polygon;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.box.MessageBox;

/**
 * Calculate selected element Area and lenght.
 * 
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class MeasureElementTool extends ToggleTool {

	@Inject
	private MapControlFactory mapControlFactory;

	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	private SelectFeature clickSelectFeature;

	private MessageBox measureDialog;

	@Inject
	public MeasureElementTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.measureElementToolText(),
				ImageProvider.INSTANCE.measureElement32(), geoMap, layerManager);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.measureElementToolText(),
				UIMessages.INSTANCE.measureElementToolTip(), Side.LEFT));
		setEnabled(false);

	}

	@PostConstruct
	private void initialize() {
		this.clickSelectFeature = mapControlFactory.createClickControl(
				new Vector("Empty"), getUnselectListener(), getClickEvent());
		add(mapControlFactory.createSelectHover(new Vector("Empty")));
		add(clickSelectFeature);
	}

	private UnselectFeatureListener getUnselectListener() {
		return new UnselectFeatureListener() {

			@Override
			public void onFeatureUnselected(VectorFeature vectorFeature) {
				if (measureDialog != null && measureDialog.isVisible()) {
					measureDialog.hide();
				}
			}
		};
	}

	private ClickFeatureListener getClickEvent() {
		return new ClickFeatureListener() {

			@Override
			public void onFeatureClicked(VectorFeature vectorFeature) {
				clickSelectFeature.select(vectorFeature);
				clickSelectFeature.unselectAll(vectorFeature);

				Geometry geom = vectorFeature.getGeometry();
				Projection proj = new Projection(GeoMap.INTERNAL_EPSG);
				if (geom.getClassName().equals(Geometry.LINESTRING_CLASS_NAME)) {

					LineString lineString = LineString.narrowToLineString(geom
							.getJSObject());
					MeasurementInfo distanceInfo = new MeasurementInfo(
							lineString.getGeodesicLength(proj),
							MeasurementInfo.UNIT_METERS);
					show(distanceInfo, new MeasurementInfo(0D,
							MeasurementInfo.UNIT_SQUARE_METERS));
				} else if (geom.getClassName().equals(
						Geometry.POLYGON_CLASS_NAME)) {
					Polygon polygon = Polygon.narrowToPolygon(geom
							.getJSObject());

					MeasurementInfo areaInfo = new MeasurementInfo(
							polygon.getGeodesicArea(proj),
							MeasurementInfo.UNIT_SQUARE_METERS);

					MeasurementInfo distanceInfo = new MeasurementInfo(
							calculatePolygonLenght(polygon),
							MeasurementInfo.UNIT_METERS);

					show(distanceInfo, areaInfo);

				} else {
					show(new MeasurementInfo(0D,
							MeasurementInfo.UNIT_SQUARE_METERS),
							new MeasurementInfo(0D,
									MeasurementInfo.UNIT_SQUARE_METERS));
				}
			}
		};
	}

	private double calculatePolygonLenght(Polygon polygon) {
		double lenght = 0;
		for (LinearRing ring : polygon.getComponents()) {
			lenght = lenght
					+ ring.getGeodesicLength(new Projection(
							GeoMap.INTERNAL_EPSG));
		}
		return lenght;
	}

	private void show(MeasurementInfo distanceInfo, MeasurementInfo areaInfo) {
		distanceInfo.normalizeLength();
		areaInfo.normalizeArea();

		String msg = UIMessages.INSTANCE.mtDialogLabel(
				distanceInfo.getMeasureValue(),
				distanceInfo.getUnit())
				+ "<br>"
				+ UIMessages.INSTANCE.metDialogLabel(
						areaInfo.getMeasureValue(), "",
						areaInfo.getUnit());

		measureDialog = messageDialogBuilder.createInfo(UIMessages.INSTANCE
				.measure(), msg);
		measureDialog.show();
	}
}
