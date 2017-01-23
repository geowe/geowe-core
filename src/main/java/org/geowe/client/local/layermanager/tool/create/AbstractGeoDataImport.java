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

import java.util.List;

import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.create.vector.source.GitHubLayerVectorSource;
import org.geowe.client.local.layermanager.tool.create.vector.source.LayerVectorSource;
import org.geowe.client.local.layermanager.tool.create.vector.source.URLLayerVectorSource;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.main.tool.map.catalog.model.LayerDef;
import org.geowe.client.local.main.tool.map.catalog.model.URLVectorLayerDef;
import org.geowe.client.local.main.tool.map.catalog.model.WfsVectorLayerDef;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.shared.rest.github.response.GitHubFileListAttributeBean;
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
			createWfsLayer();
		}
		else if (UIMessages.INSTANCE.gitHubResponseTitle().equals(activeTab)) {
			createGitHubLayers();
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
			showAlert(UIMessages.INSTANCE.warning(),
					UIMessages.INSTANCE.gditAlertMessage());
		}

		return layer;
	}
	
	
	protected void createLayerFromURL(LayerVectorSource source) {
		URLLayerVectorSource urlSource = (URLLayerVectorSource)source;
		URLVectorLayerDef urlLayerDef = new URLVectorLayerDef();
		urlLayerDef.setEpsg(geoDataImportDialog.getProjectionName());
		urlLayerDef.setFormat(geoDataImportDialog.getDataFormat());
		urlLayerDef.setName(source.getLayerName());
		urlLayerDef.setUrl(urlSource.getUrl());
		urlLayerDef.setSource(source);		
		urlLayerDef.setType(LayerDef.VECTOR_TYPE);
		urlLayerDef.load();
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

	protected void createWfsLayer() {
		WfsVectorLayerDef urlLayerDef = new WfsVectorLayerDef();
		urlLayerDef.setType(LayerDef.VECTOR_TYPE);
		urlLayerDef.setEpsg(geoDataImportDialog.getProjectionName());
		urlLayerDef.setFormat(geoDataImportDialog.getDataFormat());
		urlLayerDef.setName(geoDataImportDialog.getLayerName());
		urlLayerDef.setServiceUrl(geoDataImportDialog.getWfsUrl());
		urlLayerDef.setVersion(geoDataImportDialog.getWfsVersion());
		urlLayerDef.setNameSpaceFeatureType(geoDataImportDialog
				.getWfsNamespaceTypeName());
		urlLayerDef.setMaxFeatures(geoDataImportDialog.getWfsMaxFeatures());
		if (geoDataImportDialog.isBboxEnabled()) {
			urlLayerDef.generateBbox();
		}else{
			urlLayerDef.setCql(geoDataImportDialog.getWfsCqlfilter());
		}
		urlLayerDef.load();
	}
	
	
	protected void createGitHubLayers() {
		GitHubImportTab getGitHubImportTab = geoDataImportDialog.getGitHubImportTab();
		List<GitHubFileListAttributeBean> files = getGitHubImportTab.getSelectedFiles();
		//eliminar la extension del fichero
		
		
		for(GitHubFileListAttributeBean file: files) {
			final GitHubLayerVectorSource source = new GitHubLayerVectorSource();
			source.setUrl(file.getAttributeUrl());
			String name = file.getAttributeName().substring(0, file.getAttributeName().lastIndexOf("."));
			source.setLayerName(name);
			source.setSha(file.getAttributeSha());
			createLayerFromURL(source);
		}
		
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
	
	protected boolean isValidDataFormat() {
		boolean isValid = true;
		if (geoDataImportDialog.getDataFormat().isEmpty()) {
			isValid = false;
		}
		LOG.info("FORMAT VALID: " + isValid);
		return isValid;
	}

	protected boolean isValidProjection() {

		boolean isValid = true;
		if (geoDataImportDialog.getProjectionName().isEmpty()) {
			isValid = false;
		}
		LOG.info("PROJECTION VALID: " + isValid);
		return isValid;
	}

	protected boolean isValidInputFile() {
		boolean isValid = isValidDataFormat() && isValidProjection();
		if (isValid && geoDataImportDialog.getActiveTab().equals(
				UIMessages.INSTANCE.file())
				&& (geoDataImportDialog.getFileUploadField().getValue() == null || (geoDataImportDialog
						.getFileUploadField().getValue() != null && geoDataImportDialog
						.getFileUploadField().getValue().isEmpty()))) {

			isValid = false;
		}
		return isValid;
	}

	protected boolean isValidInputURL() {
		boolean isValid = isValidDataFormat() && isValidProjection();
		if (isValid && geoDataImportDialog.getActiveTab()
				.equals(UIMessages.INSTANCE.url())
				&& (geoDataImportDialog.getUrl() == null || (geoDataImportDialog
						.getUrl() != null && geoDataImportDialog.getUrl()
						.isEmpty()))) {

			isValid = false;
		}
		return isValid;
	}

	protected boolean isValidInputText() {
		boolean isValid = isValidDataFormat() && isValidProjection();
		if (isValid && geoDataImportDialog.getActiveTab().equals(
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
						.getWfsUrl().isEmpty()) || (geoDataImportDialog
						.getWfsNamespaceTypeName() == null && geoDataImportDialog
						.getWfsNamespaceTypeName().isEmpty()))) {

			isValid = false;
		}
		return isValid;
	}
	
	protected boolean isValidInputGitHub() {
		boolean isValid = isValidDataFormat() && isValidProjection();
		if (isValid && geoDataImportDialog.getActiveTab()
				.equals(UIMessages.INSTANCE.gitHubResponseTitle())) {

			int count = geoDataImportDialog.getGitHubImportTab().getSelectedFiles().size();
			if(count == 0) {
				isValid = false;
			}
		}
		return isValid;
	}
}
