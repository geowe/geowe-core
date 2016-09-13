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

import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.main.tool.map.catalog.model.LayerDef;
import org.geowe.client.local.main.tool.map.catalog.model.URLVectorLayerDef;
import org.geowe.client.local.main.tool.map.catalog.model.WfsVectorLayerDef;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public abstract class AbstractGeoDataImport extends ButtonTool {
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractGeoDataImport.class.getName());
	
	private final GeoDataImportDialog geoDataImportDialog;
	private final LayerManagerWidget layerManager;
	private final MessageDialogBuilder messageDialogBuilder;
	private boolean createAttrAfterLayerCreation;

	public boolean isCreateAttrAfterLayerCreation() {
		return createAttrAfterLayerCreation;
	}

	public AbstractGeoDataImport(GeoDataImportDialog geoDataImportDialog,
			LayerManagerWidget layerManager,
			MessageDialogBuilder messageDialogBuilder) {
		super(null);
		this.geoDataImportDialog = geoDataImportDialog;
		this.layerManager = layerManager;
		this.messageDialogBuilder = messageDialogBuilder;
	}

	protected VectorLayer getlayer(String activeTab) {
		VectorLayer layer = null;

		if (UIMessages.INSTANCE.url().equals(activeTab)) {
			createLayerFromURL();
		} else if (UIMessages.INSTANCE.text().equals(activeTab)) {
			layer = createLayerFromText();
		} else if (UIMessages.INSTANCE.file().equals(activeTab)) {
			createLayerFromFile();
		} else if (UIMessages.INSTANCE.empty().equals(activeTab)) {
			layer = createEmptyLayer();
		} else if (UIMessages.INSTANCE.wfs().equals(activeTab)) {
			layer = createWfsLayer();
		}

		return layer;
	}

	protected VectorLayer createLayerFromText() {
		VectorLayer layer = null;
		VectorLayerConfig layerConfig = null;
		
		try {
			layerConfig = new VectorLayerConfig();
			layerConfig.setEpsg(geoDataImportDialog.getProjectionName());
			layerConfig.setGeoDataFormat(geoDataImportDialog.getDataFormat());
			layerConfig.setLayerName(geoDataImportDialog.getLayerName());
			layerConfig.setGeoDataString(geoDataImportDialog.getGeoData());

			layer = VectorLayerFactory
					.createVectorLayerFromGeoData(layerConfig);
		} catch (Exception e) {
			LOG.info("Creation of VectorLayer failed: " + layerConfig);
			showAlert(UIMessages.INSTANCE.warning(),
					UIMessages.INSTANCE.gditAlertMessage());
		}

		return layer;
	}

	protected void createLayerFromURL() {
		URLVectorLayerDef urlLayerDef = new URLVectorLayerDef();
		urlLayerDef.setEpsg(geoDataImportDialog.getProjectionName());
		urlLayerDef.setFormat(geoDataImportDialog.getDataFormat());
		urlLayerDef.setName(geoDataImportDialog.getLayerName());
		urlLayerDef.setUrl(geoDataImportDialog.getUrl());
		urlLayerDef.setType(LayerDef.VECTOR_TYPE);
		urlLayerDef.load();
	}

	protected void createLayerFromFile() {
		geoDataImportDialog.getUploadPanel().submit();
	}

	protected VectorLayer createWfsLayer() {
		WfsVectorLayerDef wfsLayer = new WfsVectorLayerDef();
		wfsLayer.setName(geoDataImportDialog.getLayerName());
		wfsLayer.setUrl(geoDataImportDialog.getWfsUrl());
		wfsLayer.setNameSpace(geoDataImportDialog.getWfsNamespace());
		wfsLayer.setFeatureType(geoDataImportDialog.getWfsFeatureType());
		wfsLayer.setGeometryColumn(geoDataImportDialog.getGeomColumn());
		wfsLayer.setFormat(geoDataImportDialog.getDataFormat());
		wfsLayer.setEpsg(geoDataImportDialog.getProjectionName());
		wfsLayer.setVersion(geoDataImportDialog.getVersion());

		return (VectorLayer) wfsLayer.getLayer();
	}

	protected VectorLayer createEmptyLayer() {

		final VectorLayerConfig layerConfig = new VectorLayerConfig();
		layerConfig.setEpsg(geoDataImportDialog.getProjectionName());
		layerConfig.setLayerName(geoDataImportDialog.getLayerName());

		createAttrAfterLayerCreation = geoDataImportDialog
				.getCreateAttributes();
		return VectorLayerFactory.createEmptyVectorLayer(layerConfig);
	}

	protected boolean isLayerNameValid(final String layerName) {
		boolean isValid = false;
		if (layerName != null && !layerName.isEmpty() && !existLayer(layerName)) {
			isValid = true;
		}

		return isValid;
	}

	private boolean existLayer(final String layerName) {
		boolean exist = true;
		if (layerManager.getVector(layerName) == null) {
			exist = false;
		}
		return exist;
	}

	protected void showAlert(String title, String msg) {
		messageDialogBuilder.createError(title, msg).show();
	}

	protected void addCloseButtonHandler() {
		geoDataImportDialog.getButton(PredefinedButton.CANCEL)
				.addSelectHandler(new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						geoDataImportDialog.hide();
					}
				});
	}
	
	protected boolean isValidInputFile() {
		boolean isValid = true;
		if (geoDataImportDialog.getActiveTab().equals(
				UIMessages.INSTANCE.file())
				&& (geoDataImportDialog.getFileUploadField().getValue() == null || (geoDataImportDialog
						.getFileUploadField().getValue() != null && geoDataImportDialog
						.getFileUploadField().getValue().isEmpty()))) {

			isValid = false;
		}

		return isValid;
	}

	protected boolean isValidInputURL() {
		boolean isValid = true;
		if (geoDataImportDialog.getActiveTab()
				.equals(UIMessages.INSTANCE.url())
				&& (geoDataImportDialog.getUrl() == null || (geoDataImportDialog
						.getUrl() != null && geoDataImportDialog.getUrl()
						.isEmpty()))) {

			isValid = false;
		}

		return isValid;
	}

	protected boolean isValidInputText() {
		boolean isValid = true;
		if (geoDataImportDialog.getActiveTab().equals(
				UIMessages.INSTANCE.text())
				&& (geoDataImportDialog.getGeoData() == null || geoDataImportDialog
						.getGeoData().isEmpty())) {

			isValid = false;
		}

		return isValid;
	}

	protected boolean isValidInputWFS() {
		boolean isValid = true;
		if (geoDataImportDialog.getActiveTab()
				.equals(UIMessages.INSTANCE.wfs())
				&& ((geoDataImportDialog.getWfsUrl() == null && geoDataImportDialog
						.getWfsUrl().isEmpty())
						|| (geoDataImportDialog.getWfsNamespace() == null && geoDataImportDialog
								.getWfsNamespace().isEmpty()) || (geoDataImportDialog
						.getWfsFeatureType() == null && geoDataImportDialog
						.getWfsFeatureType().isEmpty()))) {

			isValid = false;
		}
		return isValid;
	}
}