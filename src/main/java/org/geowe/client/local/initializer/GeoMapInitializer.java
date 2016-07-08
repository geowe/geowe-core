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

import org.geowe.client.local.main.StatusPanelWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.W3wTool;
import org.geowe.client.local.main.tool.map.catalog.AppLayerCatalog;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.geowe.client.shared.rest.w3w.W3WService;
import org.geowe.client.shared.rest.w3w.W3WServiceAsync;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Pixel;
import org.gwtopenmaps.openlayers.client.control.Attribution;
import org.gwtopenmaps.openlayers.client.control.Navigation;
import org.gwtopenmaps.openlayers.client.control.Scale;
import org.gwtopenmaps.openlayers.client.control.ScaleLine;
import org.gwtopenmaps.openlayers.client.control.ScaleLineOptions;
import org.gwtopenmaps.openlayers.client.event.EventHandler;
import org.gwtopenmaps.openlayers.client.event.EventObject;
import org.gwtopenmaps.openlayers.client.event.MapZoomListener;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.util.JSObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

/**
 * Responsible for initializing the map
 * 
 * @author geowe.org
 * @author jose@geowe.org, rafa@geowe.org, ata@geowe.org
 *
 */
@ApplicationScoped
public class GeoMapInitializer {
	public static final int MAX_NUM_ZOOM_LEVEL = 50;
	public static final String DEFAUL_MAP_UNITS = "m";

	@Inject
	private GeoMap geoMap;

	@Inject
	private StatusPanelWidget statusPanelWidget;

	@Inject
	private W3wTool w3wTool;
	private final W3WServiceAsync w3wServiceAsync = GWT
			.create(W3WService.class);


	public void initialize() {

		geoMap.configure(GeoMap.INTERNAL_EPSG, MAX_NUM_ZOOM_LEVEL,
				DEFAUL_MAP_UNITS);

		final ScaleLineOptions slOptions = new ScaleLineOptions();
		slOptions.setTopOutUnits(DEFAUL_MAP_UNITS);
		slOptions.setBottomOutUnits("");

		final ScaleLine scaleLine = new ScaleLine(slOptions);
		scaleLine.getJSObject().setProperty("geodesic", true);
		geoMap.addControl(scaleLine);

		final Scale scale = new Scale();
		scale.getJSObject().setProperty("geodesic", true);
		geoMap.addControl(scale);

		geoMap.addControl(new Attribution());
		geoMap.addControl(new Navigation());
		geoMap.getMap()
				.getEvents()
				.register("mousemove", geoMap.getMap(),
						getMouseMoverEventHandler());
		geoMap.getMap()
				.getEvents()
				.register("click", geoMap.getMap(), getMouseClickEventHandler());

		geoMap.getMap()
				.getEvents()
				.register("touchstart", geoMap.getMap(),
						getMouseClickEventHandler());

		geoMap.getMap().addMapZoomListener(getMapZoomListener());
	}

	private EventHandler getMouseMoverEventHandler() {
		return new EventHandler() {
			@Override
			public void onHandle(final EventObject eventObject) {
				final LonLat lonlat = getLonLat(eventObject);
				if (!GeoMap.INTERNAL_EPSG.equals(geoMap.getDisplayProjection()
						.getProjectionCode())) {
					lonlat.transform(GeoMap.INTERNAL_EPSG, geoMap
							.getDisplayProjection().getProjectionCode());
				}
				final Double scale = Double.valueOf((geoMap.getMap().getScale()));
				statusPanelWidget.setScale("1:" + scale.intValue());
				statusPanelWidget.setCurrentCoordinate(lonlat);
			}
		};
	}

	private EventHandler getMouseClickEventHandler() {
		return new EventHandler() {
			@Override
			public void onHandle(final EventObject eventObject) {
				final LonLat lonlat = getLonLat(eventObject);

				if (!GeoMap.INTERNAL_EPSG.equals(geoMap.getDisplayProjection()
						.getProjectionCode())) {
					lonlat.transform(GeoMap.INTERNAL_EPSG, geoMap
							.getDisplayProjection().getProjectionCode());
				}

				statusPanelWidget.setClickedCoordinates(lonlat.lon(),
						lonlat.lat());
				if (w3wTool.isActive()) {
					showW3WPosition(eventObject);
				}
			}
		};
	}

	private LonLat getLonLat(final EventObject eventObject) {
		final JSObject xy = eventObject.getJSObject().getProperty("xy");
		final Pixel px = Pixel.narrowToPixel(xy);
		return geoMap.getMap().getLonLatFromPixel(px);		 
	}

	/*
	 * Las coordenadas deben estar en WGS84
	 */
	private void showW3WPosition(final EventObject eventObject) {
		final ProgressBarDialog progressBar = new ProgressBarDialog(false,
				UIMessages.INSTANCE.processing());
		progressBar.show();
		final LonLat internalLonlat = getLonLat(eventObject);
		final LonLat lonlat = getLonLat(eventObject);

		lonlat.transform(GeoMap.INTERNAL_EPSG, "WGS84");

		final String position = lonlat.lat() + "," + lonlat.lon();
		w3wTool.set3Words(UIMessages.INSTANCE.processing());

		w3wServiceAsync.get3Words(position, w3wTool.getLocale(),
				new AsyncCallback<String>() {
			public void onFailure(final Throwable caught) {
				progressBar.hide();

				final AlertMessageBox messageBox = new AlertMessageBox(
						UIMessages.INSTANCE.warning(), UIMessages.INSTANCE
								.w3wErrorText());
				messageBox.show();
				w3wTool.set3Words(UIMessages.INSTANCE.w3wErrorText());
				w3wTool.addElementToW3wLayer(internalLonlat,
						(UIMessages.INSTANCE.w3wErrorText()));
			}

			public void onSuccess(final String response) {
				progressBar.hide();
				if (response.isEmpty()) {
					showException(UIMessages.INSTANCE.w3wErrorText());
					final String wordsW3W = UIMessages.INSTANCE.w3wErrorText();
					w3wTool.set3Words(wordsW3W);
					w3wTool.addElementToW3wLayer(internalLonlat, wordsW3W);
					return;
				}

				final JSONValue jsonValue = JSONParser.parseLenient(response);
				final JSONObject jsonObject = jsonValue.isObject();

						if (jsonObject.containsKey("words")) {
					final JSONArray jsonWords = jsonObject.get("words").isArray();
					final String[] words = new String[3];
					words[0] = jsonWords.get(0).isString().stringValue();
					words[1] = jsonWords.get(1).isString().stringValue();
					words[2] = jsonWords.get(2).isString().stringValue();

					final String wordsW3W = words[0] + "." + words[1] + "."
							+ words[2];
					w3wTool.set3Words(wordsW3W);
					w3wTool.addElementToW3wLayer(internalLonlat, wordsW3W);

				} else if (jsonObject.containsKey("error")) {
					showException("Error returned from w3w API: "
							+ jsonObject.get("message").toString());
				} else {
					showException("Undefined error while fetching words by position");
				}
			}
		});
	}


	private void showException(final String msg) {
		final AlertMessageBox messageBox = new AlertMessageBox(
				UIMessages.INSTANCE.warning(), msg);
		messageBox.show();
	}

	private MapZoomListener getMapZoomListener() {
		return new MapZoomListener() {
			@Override
			public void onMapZoom(final MapZoomEvent eventObject) {
				final Layer googleSatelliteLayer = geoMap.getMap().getLayerByName(
						AppLayerCatalog.GOOGLE_SATELLITE);

				if (geoMap.getMap().getZoom() > 17) {
					googleSatelliteLayer.setIsVisible(false);
				} else {
					googleSatelliteLayer.setIsVisible(true);
				}
			}
		};
	}
}