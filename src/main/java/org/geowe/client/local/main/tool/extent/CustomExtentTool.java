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
public class CustomExtentTool extends ButtonTool {
	
	private final GeoMap geoMap;
	@Inject
	private CustomExtentDialog customExtentDialog;
	@Inject
	public CustomExtentTool(GeoMap geoMap) {		
		super("Custom",
				ImageProvider.INSTANCE.customExtension24());
		this.geoMap = geoMap;
		setToolTipConfig(createTooltipConfig(
				"Custom Extent",
				"Restricción de extensión máxima personalizada", Side.LEFT));
	}

	@Override
	protected void onRelease() {
		customExtentDialog.setModal(true);
		customExtentDialog.show();		
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