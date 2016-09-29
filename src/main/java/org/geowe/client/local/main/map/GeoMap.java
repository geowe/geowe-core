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
package org.geowe.client.local.main.map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.AppClientProperties;
import org.geowe.client.local.layermanager.ChangeSelectedLayerListener;
import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.control.Control;
import org.gwtopenmaps.openlayers.client.control.NavigationHistory;
import org.gwtopenmaps.openlayers.client.layer.Vector;

@ApplicationScoped
public class GeoMap implements ChangeSelectedLayerListener{
	public static final String INTERNAL_EPSG = "EPSG:3857";	
	public static final String LAYER_TYPE_RASTER = "RASTER";
	public static final String LAYER_TYPE_VECTORIAL = "VECTORIAL";	
	
	private final MapWidget mapWidget;
	private final NavigationHistory navHistory = new NavigationHistory();	
	private Projection displayProjection;
	private MapOptions mapOptions;
		
	@Inject
	private AppClientProperties appClientProperties;
	
	public GeoMap() {
		final MapOptions defaultMapOptions = new MapOptions();
		defaultMapOptions.removeDefaultControls();
		mapWidget = new MapWidget("100%", "100%", defaultMapOptions);		
		getMap().addControl(navHistory);
	}
	
	public void centerMap(final double lon, final double lat, final String sourceProjection, final String targetProjection){
		final LonLat lonLat = new LonLat(lon, lat);
		lonLat.transform(sourceProjection, targetProjection);
		
		getMap().setCenter(lonLat, 5);		
	}
		
	public Map getMap() {
		return mapWidget.getMap();
	}

	public MapWidget getMapWidget() {
		return mapWidget;
	}
		
	public Projection getDisplayProjection() {
		return displayProjection;
	}
	
	public void configure(final String displayProjection, final Integer numZoomLevels, final String units) {		
		this.configure(new Projection(displayProjection), numZoomLevels, units);
	}
	
 	public void configure(final Projection displayProjection, final Integer numZoomLevels, final String units) {
 		this.displayProjection = displayProjection; 		
		
 		mapOptions = new MapOptions();
		mapOptions.setDisplayProjection(this.displayProjection);
		mapOptions.setNumZoomLevels(numZoomLevels);
		mapOptions.setUnits(units);
		mapOptions.setMaxExtent(getDefaultMapBound());
		mapOptions.setMaxResolution(appClientProperties.getFloatValue("maxResolution"));
		getMap().setOptions(mapOptions);
		getMap().setMinMaxZoomLevel(0, 50);
 	}
 	
 	public MapOptions getMapOptions() {
 		return mapOptions;
 	}
 	
 	public Bounds getDefaultMapBound() {
 		final double lowerLeftX = appClientProperties.getFloatValue("lowerLeftX");
 		final double lowerLeftY = appClientProperties.getFloatValue("lowerLeftY");
 		final double upperRightX = appClientProperties.getFloatValue("upperRightX");
 		final double upperRightY = appClientProperties.getFloatValue("upperRightY");
		
 		return new Bounds(lowerLeftX,lowerLeftY,upperRightX,upperRightY);
 	}

	public void addControl(final Control control) {
		getMap().addControl(control);
	}
	
	public NavigationHistory getNavigationHistory() {
		return navHistory;
	}

	@Override
	public void onChange(final Vector layer) {
		if(((Vector)layer).getNumberOfFeatures() > 0) {
			getMap().zoomToExtent(((Vector)layer).getDataExtent());
		}
	}
}