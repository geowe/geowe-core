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
package org.geowe.client.local;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.geowe.client.local.initializer.EventListenerInitializer;
import org.geowe.client.local.initializer.GeoMapInitializer;
import org.geowe.client.local.initializer.RasterLayerInitializer;
import org.geowe.client.local.initializer.URLVectorLayerInitializer;
import org.geowe.client.local.initializer.VectorLayerInitializer;
import org.geowe.client.local.initializer.WidgetInitializer;
import org.geowe.client.local.main.ActionBar;
import org.geowe.client.local.main.LinksWidget;
import org.geowe.client.local.main.ZoomStatusWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.gwtopenmaps.openlayers.client.OpenLayers;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.slf4j.Logger;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;

@EntryPoint
public class App {

	@Inject
	private Logger logger;
	@Inject
	private GeoMap geoMap;
	@Inject
	private LinksWidget linksWidget;
	@Inject
	private ActionBar actionBar;
	@Inject
	private WidgetInitializer widgetInitializer;
	@Inject
	private GeoMapInitializer geoMapInitializer;
	@Inject
	private RasterLayerInitializer rasterLayerInitializer;
	@Inject
	private VectorLayerInitializer vectorLayerInitializer;
	@Inject
	private EventListenerInitializer eventListenerInitializer;
	@Inject
	private URLVectorLayerInitializer uRLVectorLayerInitializer;
	@Inject
	private ZoomStatusWidget zoomStatusWidget;
	
	@PostConstruct
	public void buildUI() {
		
		OpenLayers.setProxyHost("gwtOpenLayersProxy?targetURL=");
		RootLayoutPanel.get().add(geoMap.getMapWidget());
		RootPanel.get().add(actionBar);
		RootPanel.get().add(linksWidget);
		RootPanel.get().add(zoomStatusWidget);

		logger.info("Map initialize...");
		geoMapInitializer.initialize();
		rasterLayerInitializer.initialize();
		vectorLayerInitializer.initialize();
		widgetInitializer.initialize();
		eventListenerInitializer.initialize();

		showDisclaimer();
		
		RestClient.setJacksonMarshallingActive(true);
		RootPanel.get("splash").setVisible(false);
	}

	private void showDisclaimer() {
		final TextArea description = new TextArea();
		description.setText(DisclaimerResourceProvider.INSTANCE.disclaimer()
				.getText());
		description.setReadOnly(true);

		final Dialog disclaimerDialog = new Dialog();
		disclaimerDialog.setHeadingText("Disclaimer");
		disclaimerDialog.setClosable(false);
		disclaimerDialog.setModal(true);
		disclaimerDialog.setPredefinedButtons(PredefinedButton.OK);
		disclaimerDialog.getButton(PredefinedButton.OK).setId(
				"disclaimer_ok_button");
		disclaimerDialog.setBodyStyleName("pad-text");
		disclaimerDialog.add(description);
		disclaimerDialog.getBody().addClassName("pad-text");
		disclaimerDialog.setHideOnButtonClick(true);
		disclaimerDialog.setWidth(350);
		disclaimerDialog.setHeight(400);
		disclaimerDialog.setResizable(false);
		disclaimerDialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(final SelectEvent event) {
				uRLVectorLayerInitializer.createLayerFromURL();				
			}
			
		});
		
		disclaimerDialog.show();
	}
}