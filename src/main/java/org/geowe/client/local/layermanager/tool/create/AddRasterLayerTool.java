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
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.map.catalog.model.TmsLayerDef;
import org.geowe.client.local.main.tool.map.catalog.model.WmsLayerDef;
import org.geowe.client.local.main.tool.map.catalog.model.WmtsLayerDef;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Herramienta responsable de mostrar el widget para añadir una capa Raster
 *
 * @author jose@geowe.org
 * @since 25/08/2016
 *
 */
@ApplicationScoped
public class AddRasterLayerTool extends LayerTool {

	@Inject
	private LoadRasterLayerDialog loadRasterLayerDialog;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	@Inject
	public AddRasterLayerTool(LayerManagerWidget layerTreeWidget, GeoMap geoMap) {
		super(layerTreeWidget, geoMap);
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.addRasterLayerToolText();
	}

	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.addLayer();
	}

	@Override
	public void onClick() {
		loadRasterLayerDialog.initializeWMSFields();
		loadRasterLayerDialog.initializeWMTSFields();
		loadRasterLayerDialog.initializeTMSFields();
		loadRasterLayerDialog.show();
	}

	@PostConstruct
	private void setOKHandler() {
		loadRasterLayerDialog.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						
						if ("WMS".equals(loadRasterLayerDialog.getActiveTab())
								&& loadRasterLayerDialog.isCorrectFilledWMS()) {

							loadWMS();

						} else if ("WMTS".equals(loadRasterLayerDialog
								.getActiveTab())
								&& loadRasterLayerDialog.isCorrectFilledWMTS()) {
							loadWMTS();
						} 
						else if ("TMS".equals(loadRasterLayerDialog
								.getActiveTab())
								&& loadRasterLayerDialog.isCorrectFilledTMS()) {
							loadTMS();
						}
						else {
							showAlert(UIMessages.INSTANCE
									.aRasterltAlertMessageBoxTitle(),
									UIMessages.INSTANCE
											.aRasterltAlertMessageBoxLabel());
						}

					}
				});
	}

	private void loadWMTS() {
		if (existLayer(loadRasterLayerDialog.getLayerNameWMS())) {
			showAlert(UIMessages.INSTANCE.aRasterltAlertMessageBoxTitle(),
					UIMessages.INSTANCE.layerAlreadyExist(loadRasterLayerDialog
							.getLayerNameWMTS()));
		} else {
			WmtsLayerDef wmtsLayer = new WmtsLayerDef();
			wmtsLayer.setUrl(loadRasterLayerDialog.getUrlWMTS());
			wmtsLayer.setLayerName(loadRasterLayerDialog.getLayerNameWMTS());
			wmtsLayer.setTileMatrixSet(loadRasterLayerDialog.getTileMatrixSet());
			wmtsLayer.setName(loadRasterLayerDialog.getLayerNameWMTS());
			wmtsLayer.setFormat(loadRasterLayerDialog.getFormatWMTS());
			layerManagerWidget.addRaster(wmtsLayer.getLayer());
		}		
	}
	
	private void loadTMS() {
		if (existLayer(loadRasterLayerDialog.getNameTMS())) {
			showAlert(UIMessages.INSTANCE.aRasterltAlertMessageBoxTitle(),
					UIMessages.INSTANCE.layerAlreadyExist(loadRasterLayerDialog
							.getNameTMS()));
		} else {
			TmsLayerDef tmsLayer = new TmsLayerDef();
			tmsLayer.setUrl(loadRasterLayerDialog.getUrlTMS());
//			tmsLayer.setLayerName(loadRasterLayerDialog.getNameTMS());			
			tmsLayer.setName(loadRasterLayerDialog.getNameTMS());
			tmsLayer.setFormat(loadRasterLayerDialog.getFormatTMS());
			layerManagerWidget.addRaster(tmsLayer.getLayer());
		}		
	}

	private void loadWMS() {
		if (existLayer(loadRasterLayerDialog.getLayerNameWMS())) {
			showAlert(UIMessages.INSTANCE.aRasterltAlertMessageBoxTitle(),
					UIMessages.INSTANCE.layerAlreadyExist(loadRasterLayerDialog
							.getLayerNameWMS()));
		} else {
			WmsLayerDef newLayer = new WmsLayerDef();
			newLayer.setName(loadRasterLayerDialog.getLayerNameWMS());
			newLayer.setUrl(loadRasterLayerDialog.getUrlWMS());
			newLayer.setLayerName(loadRasterLayerDialog.getLayerNameWMS());
			newLayer.setFormat(loadRasterLayerDialog.getFormatWMS());
			newLayer.setEpsg(GeoMap.INTERNAL_EPSG);

			layerManagerWidget.addRaster(newLayer.getLayer());
		}
	}

	private boolean existLayer(String layerName) {
		return (layerManagerWidget.getRaster(layerName) == null) ? false : true;
	}

	private void showAlert(String title, String msg) {
		messageDialogBuilder.createError(title, msg).show();

	}
}
