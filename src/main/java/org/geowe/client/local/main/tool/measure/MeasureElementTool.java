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

import java.math.BigDecimal;

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
import org.gwtopenmaps.openlayers.client.geometry.Polygon;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.box.MessageBox;

/**
 * Calculate selected element Area or lenght. 
 * @author geowe
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
		this.clickSelectFeature = mapControlFactory.createClickControl(new Vector("Empty"),
				getUnselectListener(), getClickEvent());
		add(mapControlFactory.createSelectHover(new Vector("Empty")));
		add(clickSelectFeature);		
	}
	
	private UnselectFeatureListener getUnselectListener() {
		return new UnselectFeatureListener() {

			@Override
			public void onFeatureUnselected(VectorFeature vectorFeature) {
				if (measureDialog != null && measureDialog.isVisible()){
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
				if(geom.getClassName().equals(Geometry.LINESTRING_CLASS_NAME)){
					
					LineString lineString = LineString
							.narrowToLineString(geom.getJSObject());
					
					show(getReoundedMeasure(lineString.getGeodesicLength(proj),
							3), "distance", "m");
				}else if(geom.getClassName().equals(Geometry.POLYGON_CLASS_NAME)){
					Polygon polygon = Polygon
							.narrowToPolygon(geom.getJSObject());
					
					show(getReoundedMeasure(polygon.getGeodesicArea(proj) , 3), "area", "m²");
				}else{
					show(0, "Not supported " + geom.getClassName(), "");
				}
			}
		};
	}
	
	
	private void show(double value, String type, String unit) {

		measureDialog = messageDialogBuilder.createInfo(
				UIMessages.INSTANCE.measure(),
				UIMessages.INSTANCE.metDialogLabel(value, type, unit));
		measureDialog.show();


	}

	private float getReoundedMeasure(Double measure, int decimal) {
		BigDecimal bd = new BigDecimal(Double.toString(measure));
		bd = bd.setScale(decimal, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
}