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
package org.geowe.client.local.main;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.W3wTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.geowe.client.shared.rest.w3w.W3WService;
import org.geowe.client.shared.rest.w3w.W3WServiceAsync;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.control.Geolocate;
import org.gwtopenmaps.openlayers.client.control.GeolocateOptions;
import org.gwtopenmaps.openlayers.client.event.LocationFailedEvent;
import org.gwtopenmaps.openlayers.client.event.LocationFailedListener;
import org.gwtopenmaps.openlayers.client.event.LocationUncapableListener;
import org.gwtopenmaps.openlayers.client.event.LocationUpdateEvent;
import org.gwtopenmaps.openlayers.client.event.LocationUpdateListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * Represents the geocoding panel
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class GeocodingPanelWidget implements IsWidget {

	@Inject
	private GeoMap geoMap;
	@Inject
	private CoordinateGeolocationDialog coordinateGeolocationDialog;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	@Inject
	private W3wTool w3wTool;
	private final W3WServiceAsync w3wServiceAsync = GWT.create(W3WService.class);
	private ProgressBarDialog progressBar;
	private final TextField addressTextField = new TextField();
	private ContentPanel panel;
	private Geolocate geolocate;

	@Override
	public Widget asWidget() {

		if (panel == null) {
			panel = new ContentPanel();
			panel.setBorders(true);
			panel.setPixelSize(490, 47);
			panel.setHeaderVisible(false);
			panel.setPosition(300, 0);
			panel.getElement().getStyle().setPosition(Position.ABSOLUTE);

			StyleInjector.inject(".statusBarStyle { " + "position: absolute; "
					+ "bottom: 35 px;" + "background: #E0ECF8;"
					+ "border-radius: 5px 10px;" + "opacity: 0.8}");
			panel.setStyleName("geocodingPanelStyle");

			final HorizontalPanel horizontalGroup = new HorizontalPanel();
			horizontalGroup.getElement().getStyle()
					.setVerticalAlign(VerticalAlign.MIDDLE);
			horizontalGroup
					.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			horizontalGroup.setSpacing(5);
			horizontalGroup.getElement().getStyle()
					.setBackgroundColor("#E0ECF8");

			addressTextField.setWidth("320px");
			addressTextField.setEmptyText(UIMessages.INSTANCE
					.gcAddressTextField());
			addressTextField.getElement().setId("autocompletar");

			addressTextField.addKeyDownHandler(new KeyDownHandler() {
				@Override
				public void onKeyDown(final KeyDownEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						searchGeocoding(addressTextField.getText());
					}
				}
			});

			horizontalGroup.add(addressTextField);
			horizontalGroup.add(getSearchButton());
			horizontalGroup.add(getW3WLocationButton());
			horizontalGroup.add(getLocationMenuButton());
			panel.setWidget(horizontalGroup);

			panel.setVisible(false);			
			geolocate();
		}
		return panel;
	}

	@PostConstruct
	private void publishJS() {
		bridge(this);

		coordinateGeolocationDialog.getButton(PredefinedButton.OK)
				.addSelectHandler(new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						if (coordinateGeolocationDialog.isCorrectFilled()) {

							final Double latitud = Double
									.parseDouble(coordinateGeolocationDialog
											.getLatitud());
							final Double longitud = Double
									.parseDouble(coordinateGeolocationDialog
											.getLongitud());
							updateMap(latitud, longitud, 20);
						} else {
							showDialog(UIMessages.INSTANCE.fail(),
									UIMessages.INSTANCE.gcBadCoord());
						}
					}
				});
	}

	public void updateMap(final double lat, final double lon) {
		updateMap(lat, lon, 15);
	}

	private void startProgressBar() {
		progressBar = new ProgressBarDialog(false,
				UIMessages.INSTANCE.processing());
		progressBar.auto();
		progressBar.show();
	}

	public void finishProgressBar() {
		progressBar.hide();
	}

	public void updateMap(final double lat, final double lon, final int zoom) {
		finishProgressBar();
		final LonLat lonLat = new LonLat(lon, lat);
		transformToInternalProjection(lonLat);
		geoMap.getMap().panTo(lonLat);
		geoMap.getMap().setCenter(lonLat, zoom);
	}

	private void transformToInternalProjection(final LonLat lonLat) {
		lonLat.transform("EPSG:4326", geoMap.getMap().getProjection());
	}
	
	public native void bridge(final GeocodingPanelWidget instance)/*-{
		$wnd.updateMap = function(lat, lon) {
			instance.@org.geowe.client.local.main.GeocodingPanelWidget::updateMap(DD)(lat,lon);
		};

		$wnd.finishGeocoding = function() {
			instance.@org.geowe.client.local.main.GeocodingPanelWidget::finishProgressBar()();
		};

	}-*/;

	private static native void getLatLongByAddress(final String address) /*-{
		$wnd.getLatLongByAddress(address);
	}-*/;

	private TextButton getSearchButton() {
		final TextButton searchButton = new TextButton();
		searchButton.setToolTip(UIMessages.INSTANCE.gcSearchButtonToolTip());
		searchButton.setIcon(ImageProvider.INSTANCE.searchGeocoding24());
		searchButton.setId("geocoding_button");
		searchButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(final SelectEvent event) {
				searchGeocoding(addressTextField.getText());
			}
		});

		return searchButton;
	}

	private void searchGeocoding(final String address) {
		startProgressBar();
		getLatLongByAddress(address);
	}

	private TextButton getLocationMenuButton() {
		final TextButton locationButton = new TextButton();
		locationButton.setIcon(ImageProvider.INSTANCE.geolocation24());
		locationButton.setMenu(getLocationMenu());

		return locationButton;
	}

	private Menu getLocationMenu() {
		final Menu menu = new Menu();
		menu.add(new MenuItem(
				UIMessages.INSTANCE.gcCurrentLotationButtonText(),
				new SelectionHandler<MenuItem>() {

					@Override
					public void onSelection(final SelectionEvent<MenuItem> event) {
						geolocate.activate();
					}
				}));

		menu.add(new MenuItem(UIMessages.INSTANCE
				.gcCoordinateLocationbuttonText(),
				new SelectionHandler<MenuItem>() {

					@Override
					public void onSelection(final SelectionEvent<MenuItem> event) {
						coordinateGeolocationDialog.setLongitud("");
						coordinateGeolocationDialog.setLatitud("");
						coordinateGeolocationDialog.show();
					}

				}));

		return menu;
	}

	private TextButton getW3WLocationButton() {
		final TextButton locationButton = new TextButton();
		locationButton.setToolTip(UIMessages.INSTANCE
				.gcW3WlocationButtonToolTip());
		locationButton.setIcon(ImageProvider.INSTANCE.w3w24());
		locationButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(final SelectEvent event) {
				final String text = addressTextField.getText().trim();

				final String words[] = text.split("\\.");
				if (words.length != 3) {
					showDialog(UIMessages.INSTANCE.warning(),
							UIMessages.INSTANCE.gcBadWords());
					return;
				}
				getW3WPosition(text);
			}
		});

		return locationButton;
	}

	private void getW3WPosition(final String words) {
		startProgressBar();

		w3wServiceAsync.getPosition(words, w3wTool.getLocale(),
				new AsyncCallback<String>() {
					public void onFailure(final Throwable caught) {
						finishProgressBar();

						final AlertMessageBox messageBox = new AlertMessageBox(
								UIMessages.INSTANCE.warning(),
								UIMessages.INSTANCE.w3wErrorText());
						messageBox.show();
					}

					public void onSuccess(final String response) {
						finishProgressBar();
						if (response.isEmpty()) {
							showException(UIMessages.INSTANCE.w3wErrorText());
							return;
						}

						final JSONValue jsonValue = JSONParser.parseLenient(response);
						final JSONObject jsonObject = jsonValue.isObject();

						if (jsonObject.containsKey("position")) {

							final JSONArray jsonCoords = jsonObject.get("position")
									.isArray();
							final double[] coords = new double[2];
							coords[0] = jsonCoords.get(0).isNumber()
									.doubleValue();
							coords[1] = jsonCoords.get(1).isNumber()
									.doubleValue();

							final double latitud = coords[0];
							final double longitud = coords[1];

							updateMap(latitud, longitud, 20);
							final LonLat lonLat = new LonLat(longitud, latitud);
							transformToInternalProjection(lonLat);
							w3wTool.addElementToW3wLayer(lonLat, words);

						} else if (jsonObject.containsKey("error")) {
							showException(UIMessages.INSTANCE.fail()
									+ jsonObject.get("message").toString());
						} else {
							showException(UIMessages.INSTANCE.w3wErrorText());
						}
					}
				});
	}

	private void showException(final String msg) {
		final AlertMessageBox messageBox = new AlertMessageBox(
				UIMessages.INSTANCE.warning(), msg);
		messageBox.show();
	}

	private void geolocate() {
		final GeolocateOptions geoLocateOptions = new GeolocateOptions();
		geoLocateOptions.setEnableHighAccuracy(false);
		geoLocateOptions.setMaximumAge(0);
		geoLocateOptions.setTimeout(7000);

		geolocate = new Geolocate(geoLocateOptions);
		geolocate.setBind(false);

		geoMap.getMap().addControl(geolocate);

		geolocate.addLocationFailedListener(new LocationFailedListener() {

			public void onLocationFailed(final LocationFailedEvent eventObject) {

				showDialog(UIMessages.INSTANCE.fail(), eventObject.getError());
			}

		});

		geolocate.addLocationUncapableListener(new LocationUncapableListener() {
			public void onLocationUncapable() {
				showDialog(UIMessages.INSTANCE.notSupported(),
						UIMessages.INSTANCE.geolocationNotSupported());
			}
		});

		geolocate.addLocationUpdateListener(new LocationUpdateListener() {
			public void onLocationUpdate(final LocationUpdateEvent eventObject) {
				updateMap(eventObject.getPosition().getCoords().lat(),
						eventObject.getPosition().getCoords().lon(), 20);
				geolocate.deactivate();
			}
		});
	}

	private void showDialog(final String title, final String message) {
		messageDialogBuilder.createError(title, message).show();
	}
}