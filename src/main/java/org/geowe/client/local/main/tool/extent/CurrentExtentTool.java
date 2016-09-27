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

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class CurrentExtentTool extends ButtonTool {
	
	private final GeoMap geoMap;
	@Inject
	private CurrentExtentDialog currentExtentDialog;
	@Inject
	public CurrentExtentTool(GeoMap geoMap) {		
		super("Current Extent",
				ImageProvider.INSTANCE.currentExtent24());
		this.geoMap = geoMap;
		setToolTipConfig(createTooltipConfig(
				"Current Extent",
				"The extent of current map viewport ", Side.LEFT));
	}

	@Override
	protected void onRelease() {
		Bounds bounds = geoMap.getMap().getExtent();		
		LonLat center = bounds.getCenterLonLat();//transformToWGS84(bounds.getCenterLonLat());
		
		LonLat lower = new LonLat(bounds.getLowerLeftX(), bounds.getLowerLeftY());
		LonLat upper = new LonLat(bounds.getUpperRightX(), bounds.getUpperRightY());
		
		lower = transformToWGS84(lower);
		upper = transformToWGS84(upper);
		
		CurrentExtentInfo model = new CurrentExtentInfo();
		model.setCenter(center.lat() + ", " + center.lon());
		model.setLowerLeftX(lower.lon());
		model.setLowerLeftY(lower.lat());
		model.setUpperRightX(upper.lon());
		model.setUpperRightY(upper.lat());
		
		model.setBounds(bounds);		
		model.setWkt(getWKT(bounds));
		model.setWktWGS84(getWKTToWGS84(bounds));
		
		currentExtentDialog.setModel(model);
		currentExtentDialog.setModal(true);
		currentExtentDialog.show();		
	}
	
	
	private String getWKTToWGS84(Bounds bounds) {
		Geometry geom = bounds.toGeometry().clone();
		geom.transform(new Projection(geoMap.getMap().getProjection()), new Projection("EPSG:4326"));
		VectorFeature extentFeature = new VectorFeature(geom);
		WKT wktFormat = new WKT();
		return wktFormat.write(extentFeature);
	}
	
	private String getWKT(Bounds bounds) {
		Geometry geom = bounds.toGeometry();		
		VectorFeature extentFeature = new VectorFeature(geom);
		WKT wktFormat = new WKT();
		return wktFormat.write(extentFeature);
	}
	
	private LonLat transformToWGS84(final LonLat lonLat) {
		lonLat.transform(geoMap.getMap().getProjection(), "EPSG:4326");
		return lonLat;
	}
	
}
