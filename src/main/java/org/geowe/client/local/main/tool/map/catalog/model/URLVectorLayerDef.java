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
package org.geowe.client.local.main.tool.map.catalog.model;

import org.geowe.client.local.main.tool.map.catalog.LayerLoader;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.geowe.client.shared.rest.URLFileRestService;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;

/**
 * Definicion de una capa vectorial que se construye a partir de unos geodatos
 * alojados en la nube, ya sea a través de un repositorio de ficheros, un
 * servicio web, etc.
 * 
 * @author Atanasio Muñoz
 *
 */
public class URLVectorLayerDef extends VectorLayerDef {
	private static final long serialVersionUID = 1L;
	private static final String URL_BASE = "/gwtOpenLayersProxy";
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public Layer getLayer() {
		throw new RuntimeException("Not implemented yet!.");
	}

	public void load() {
		createLayerFromURL();
	}

	protected void createLayerFromURL() {
		final ProgressBarDialog autoMessageBox = new ProgressBarDialog(false,
				UIMessages.INSTANCE.processing());
		try {
			autoMessageBox.show();
			RestClient.create(URLFileRestService.class, URL_BASE,
					new RemoteCallback<String>() {
						@Override
						public void callback(String response) {
							final VectorLayerConfig layerConfig = getVectorLayerConfig();
							layerConfig.setGeoDataString(response);
							VectorLayer layer = VectorLayerFactory
									.createVectorLayerFromGeoData(layerConfig);
							LayerLoader.load(layer);
							autoMessageBox.hide();
						}
					}, new RestErrorCallback() {
						@Override
						public boolean error(Request message,
								Throwable throwable) {
							autoMessageBox.hide();
							showDialog("Error", message + ". "
									+ UIMessages.INSTANCE.unexpectedError());
							return false;
						}
					}, Response.SC_OK).getContent(getUrl());
		} catch (Exception e) {
			autoMessageBox.hide();
			showDialog(
					UIMessages.INSTANCE.errorLoadingLayer(getVectorLayerConfig()
							.getLayerName()),
					UIMessages.INSTANCE.unexpectedError());
		}
	}

	private void showDialog(String title, String message) {
		final Dialog messageDialog = new Dialog();
		messageDialog.setModal(true);
		messageDialog.setHeadingText(title);
		messageDialog.setPredefinedButtons(PredefinedButton.CLOSE);
		messageDialog.setBodyStyleName("pad-text");
		messageDialog.add(new Label(message));
		messageDialog.getBody().addClassName("pad-text");
		messageDialog.setHideOnButtonClick(true);
		messageDialog.setWidth(300);
		messageDialog.setResizable(false);
		messageDialog.show();
	}
}
