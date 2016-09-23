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

import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.map.catalog.LayerLoader;
import org.geowe.client.local.main.tool.schema.FeatureSchemaDialog;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.inject.Inject;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;

/**
 * Representa la herramienta responsable de realizar las cargas de datos
 * espaciales
 * 
 * @author geowe.org
 * @author jose@geowe.org, rafa@geowe.org, ata@geowe.org
 *
 */
@ApplicationScoped
public class GeoDataImportTool extends AbstractGeoDataImport {
//	private static final Logger LOG = LoggerFactory
//			.getLogger(GeoDataImportTool.class.getName());

	protected final GeoDataImportDialog geoDataImportDialog;
	protected final LayerManagerWidget layerManagerWidget;
	
	@Inject
	private ClientTaskManager taskManager;
	@Inject
	private FeatureSchemaDialog featureSchemaDialog;
	private ProgressBarDialog autoMessageBox;
	private String dialogTitle;
	private String layerName;
	private String projection;

	@Inject
	public GeoDataImportTool(final GeoDataImportDialog geoDataImportDialog,
			final LayerManagerWidget layerManager,
			final MessageDialogBuilder messageDialogBuilder) {

		super(geoDataImportDialog, layerManager, messageDialogBuilder);
		configureDialog(UIMessages.INSTANCE.geoDataImportDialogTitle(),
				UIMessages.INSTANCE.defaultLayerName(), GeoMap.INTERNAL_EPSG);

		this.geoDataImportDialog = geoDataImportDialog;
		this.layerManagerWidget = layerManager;
	}

	@PostConstruct
	private void initialize() {
		addDialogListener();
		addCloseButtonHandler();
		geoDataImportDialog.getUploadPanel().addSubmitCompleteHandler(
				new SubmitCompleteHandler() {
					public void onSubmitComplete(final SubmitCompleteEvent event) {

						final Element label = DOM.createLabel();
						label.setInnerHTML(event.getResults());

						final String contentFile = label.getInnerText();
						autoMessageBox.hide();
						geoDataImportDialog.hide();
						if (hasError(contentFile)) {
							showAlert(contentFile);
							return;
						}

						VectorLayerConfig layerConfig = null;
						Layer layer = null; 
						
						try {
							layerConfig = new VectorLayerConfig();
							layerConfig.setEpsg(geoDataImportDialog
								.getProjectionName());
							layerConfig.setGeoDataFormat(geoDataImportDialog
								.getDataFormat());
							layerConfig.setLayerName(geoDataImportDialog
								.getLayerName());
							layerConfig.setGeoDataString(contentFile);

							layer = VectorLayerFactory
									.createVectorLayerFromGeoData(layerConfig);
							
						} catch (Exception e) {							
							showAlert(UIMessages.INSTANCE.gditAlertMessage());
						}

						layerManagerWidget.addVector(layer);
						layerManagerWidget.setSelectedLayer(
								LayerManagerWidget.VECTOR_TAB, layer);

					}

					private boolean hasError(final String contentFile) {
						return contentFile.startsWith("413")
								|| contentFile.startsWith("500");
					}

					private void showAlert(final String errorMsg) {
						AlertMessageBox messageBox = new AlertMessageBox(
								UIMessages.INSTANCE.warning(), errorMsg);
						messageBox.show();
					}
				});
	}

	private void configureDialog(final String title, final String newLayerName,
			String projection) {
		this.dialogTitle = title;
		this.layerName = newLayerName;
		this.projection = projection;
	}

	@Override
	public void onRelease() {
		geoDataImportDialog.initialize(dialogTitle, layerName, projection);
		geoDataImportDialog.show();
	}

	private void addDialogListener() {
		geoDataImportDialog.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						autoMessageBox = new ProgressBarDialog(false,
								UIMessages.INSTANCE.processing());
						taskManager.execute(new Runnable() {

							@Override
							public void run() {

								if (isValid(geoDataImportDialog.getActiveTab())) {
									autoMessageBox.setClosable(true);
									autoMessageBox.clear();
									autoMessageBox.center();
									if (!geoDataImportDialog.getActiveTab()
											.equals(UIMessages.INSTANCE.url())) {
										autoMessageBox.show();
									}

									final Layer layer = getlayer(geoDataImportDialog
											.getActiveTab());

									if (geoDataImportDialog.getActiveTab()
											.equals(UIMessages.INSTANCE.file())) {
										return;
									}

									if (layer != null) {
										LayerLoader.load(layer);
										if (geoDataImportDialog.getActiveTab()
												.equals(UIMessages.INSTANCE
														.empty())
												&& isCreateAttrAfterLayerCreation()) {

											featureSchemaDialog
													.setVectorLayer((VectorLayer) layer);
											featureSchemaDialog.show();

										}
									}

									geoDataImportDialog.hide();

									if (!geoDataImportDialog.getActiveTab()
											.equals(UIMessages.INSTANCE.url())) {
										autoMessageBox.hide();
									}
								} else {									
									showAlert(UIMessages.INSTANCE.warning(),
											UIMessages.INSTANCE
													.gditAlertMessage());
								}
							}

							private boolean isValid(final String activeTab) {
								boolean valid = isLayerNameValid(geoDataImportDialog
										.getLayerName());
								if (valid
										&& UIMessages.INSTANCE.url().equals(
												activeTab)) {
									valid = isValidInputURL();
								} else if (valid
										&& UIMessages.INSTANCE.text().equals(
												activeTab)) {
									valid = isValidInputText();
								} else if (valid
										&& UIMessages.INSTANCE.file().equals(
												activeTab)) {
									valid = isValidInputFile();
								} else if (valid
										&& UIMessages.INSTANCE.wfs().equals(
												activeTab)) {
									valid = isValidInputWFS();
								}

								return valid;
							}

						});
					}
				});
	}
}