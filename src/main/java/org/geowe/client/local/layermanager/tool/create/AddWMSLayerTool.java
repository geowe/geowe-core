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
package org.geowe.client.local.layermanager.tool.create;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.LayerTool;
import org.geowe.client.local.layermanager.tool.LoadWMSLayerDialog;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.map.catalog.model.TmsLayerDef;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Herramienta responsable de mostrar el widget para añadir una capa WMS
 *
 * @author geowe
 *
 */
@ApplicationScoped
public class AddWMSLayerTool extends LayerTool {

	@Inject
	private LoadWMSLayerDialog loadWMSLayerDialog;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	@Inject
	public AddWMSLayerTool(LayerManagerWidget layerTreeWidget, GeoMap geoMap) {
		super(layerTreeWidget, geoMap);
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.addWMSLayerToolText();
	}

	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.addLayer();
	}

	@Override
	public void onClick() {
		loadWMSLayerDialog.show();
	}

	@PostConstruct
	private void setOKHandler() {
		loadWMSLayerDialog.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						if (loadWMSLayerDialog.isCorrectFilled()) {

							if (existLayer(loadWMSLayerDialog.getWmsLayerName())) {
								showAlert(
										UIMessages.INSTANCE
												.aWMSltAlertMessageBoxTitle(),
										UIMessages.INSTANCE
												.layerAlreadyExist(loadWMSLayerDialog
														.getWmsLayerName()));
							} else {
								// WmsLayerDef newLayer = new WmsLayerDef();
								// newLayer.setName(loadWMSLayerDialog
								// .getWmsLayerName());
								// newLayer.setUrl(loadWMSLayerDialog.getUrl());
								// newLayer.setWmsLayerName(loadWMSLayerDialog
								// .getWmsLayerName());
								// newLayer.setFormat(loadWMSLayerDialog
								// .getFormat());
								// newLayer.setEpsg(GeoMap.INTERNAL_EPSG);
								//
								// layerManagerWidget.addRaster(newLayer
								// .getLayer());

								TmsLayerDef newLayer = new TmsLayerDef();
								newLayer.setName(loadWMSLayerDialog
										.getWmsLayerName());
								newLayer.setUrl(loadWMSLayerDialog.getUrl());
								newLayer.setWmsLayerName(loadWMSLayerDialog
										.getWmsLayerName());
								newLayer.setFormat(loadWMSLayerDialog
										.getFormat());




								layerManagerWidget.addRaster(newLayer
										.getLayer());



								loadWMSLayerDialog.initialize();
							}
						} else {
							showAlert(UIMessages.INSTANCE
									.aWMSltAlertMessageBoxTitle(),
									UIMessages.INSTANCE
											.aWMSltAlertMessageBoxLabel());
						}
					}
				});
	}

	private boolean existLayer(String layerName) {
		return (layerManagerWidget.getRaster(layerName) == null) ? false : true;
	}

	private void showAlert(String title, String msg) {
		messageDialogBuilder.createError(title, msg).show();

	}
}
