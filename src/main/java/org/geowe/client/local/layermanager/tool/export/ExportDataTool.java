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
package org.geowe.client.local.layermanager.tool.export;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.ChangeSelectedLayerListener;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.LayerTool;
import org.geowe.client.local.layermanager.tool.create.CSV;
import org.geowe.client.local.layermanager.tool.export.exporter.Exporter;
import org.geowe.client.local.layermanager.tool.export.exporter.FileExporter;
import org.geowe.client.local.layermanager.tool.export.exporter.FileParameter;
import org.geowe.client.local.layermanager.tool.export.exporter.GitHubCreateFileExporter;
import org.geowe.client.local.layermanager.tool.export.exporter.GitHubUpdateFileExporter;
import org.geowe.client.local.layermanager.tool.export.exporter.github.GitHubParameter;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.FeatureSchema;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.GML;
import org.gwtopenmaps.openlayers.client.format.GeoJSON;
import org.gwtopenmaps.openlayers.client.format.KML;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;

/**
 * Export GeoData Tool
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class ExportDataTool extends LayerTool implements
		ChangeSelectedLayerListener {

	@Inject
	private ExportDataDialog exportDataDialog;
	@Inject
	private ClientTaskManager taskManager;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	@Inject
	private GitHubCreateFileExporter gitHubCreateFileExporter;
	@Inject
	private GitHubUpdateFileExporter gitHubUpdateFileExporter;
	@Inject
	private GitHubExportDialog gitHubExportDialog;
	@Inject
	private GitHubRepositoryListDialog repositoryListDialog;
	private Exporter exporter;
	private FileParameter fileParameter;
	
	@Inject
	public ExportDataTool(LayerManagerWidget layerTreeWidget, GeoMap geoMap) {
		super(layerTreeWidget, geoMap);
	}

	@PostConstruct
	private void configureDownloadButton() {
		exportDataDialog.getDownloadFileButton().addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						exporter = new FileExporter();
						fileParameter = new FileParameter();
						fileParameter.setFileName(getFileName());
						fileParameter.setExtension(getExtension());

						if (isSelectedFeatures()) {
							confirmDownloadSelected();
						} else {
							fileParameter
									.setContent(getContent((VectorLayer) getSelectedLayer()));
							export();
						}
					}
				});

		exportDataDialog.getGitHubButton().addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {						
						gitHubExportDialog.setFileName(getFileName());
						gitHubExportDialog.show();
					}
				});

		gitHubExportDialog.getCreateButton().addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						if (isValidFieldGitHub()) {
							gitHubCreateFileExporter
									.export(getGitHubParameter());
						} else {
							messageDialogBuilder.createInfo(
									UIMessages.INSTANCE.gitHubResponseTitle(),
									UIMessages.INSTANCE.gitHubCheckAllFields()).show();
						}
					}
				});

		gitHubExportDialog.getUpdateButton().addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						if (isValidFieldGitHub()) {
							gitHubUpdateFileExporter
									.export(getGitHubParameter());
						} else {
							messageDialogBuilder.createInfo(
									UIMessages.INSTANCE.gitHubResponseTitle(),
									UIMessages.INSTANCE.gitHubCheckAllFields()).show();
						}
					}
				});

		gitHubExportDialog.getRepositoriesButton().addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						String userName = gitHubExportDialog.getUserName();
						if (userName.trim().isEmpty()) {
							messageDialogBuilder.createInfo(
									UIMessages.INSTANCE.gitHubResponseTitle(),
									UIMessages.INSTANCE.gitHubUserNameCheckField())
									.show();
							return;
						}
						repositoryListDialog.load(userName);
					}
				});
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.exportDataToolText();
	}

	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.download24();
	}

	@Override
	public void onClick() {
		Layer selectedLayer = getLayerManagerWidget().getSelectedLayer(
				LayerManagerWidget.VECTOR_TAB);
		if (isLayerToExportValid(selectedLayer)) {
			exportDataDialog.setVectorLayer((VectorLayer) selectedLayer);
			exportDataDialog.show();
		}
	}

	private boolean isValidFieldGitHub() {
		boolean isValid = true;
		if (gitHubExportDialog.getUserName().trim().isEmpty()
				|| gitHubExportDialog.getPassword().trim().isEmpty()
				|| gitHubExportDialog.getRepository().trim().isEmpty()
				|| gitHubExportDialog.getPath().trim().isEmpty()
				|| gitHubExportDialog.getFileName().trim().isEmpty()
				|| gitHubExportDialog.getMessage().trim().isEmpty()) {
			isValid = false;
		}
		return isValid;
	}

	private void export() {
		taskManager.execute(new Runnable() {
			@Override
			public void run() {

				if (isContentValid(fileParameter.getContent())) {
					exporter.export(fileParameter);
				}
			}
		});
	}

	private boolean isContentValid(String content) {
		boolean isValid = true;
		if (content.isEmpty()) {
			messageDialogBuilder.createError(
					UIMessages.INSTANCE.edtAlertDialogTitle(),
					UIMessages.INSTANCE.edtAlertDialogLabel()).show();
			isValid = false;
		}
		return isValid;
	}

	private boolean isLayerToExportValid(Layer layer) {
		boolean isValid = true;
		// TODO: controlar si es la raiz del árbol
		if (layer == null) {
			Info.display(UIMessages.INSTANCE.edtAlertDialogTitle(),
					UIMessages.INSTANCE.edtAlertMessageBoxLabel());
			isValid = false;
		}
		return isValid;
	}

	private boolean isSelectedFeatures() {
		boolean isSelected = false;
		if (isLayerToExportValid(exportDataDialog.getVectorLayer())) {

			final VectorLayer selectedLayer = (VectorLayer) getSelectedLayer();
			final VectorFeature[] selectedFeatures = selectedLayer
					.getSelectedFeatures();

			if (selectedFeatures != null && selectedFeatures.length > 0) {
				isSelected = true;
			}
		}

		return isSelected;
	}

	private GitHubParameter getGitHubParameter() {
		final GitHubParameter gitHubParameter = new GitHubParameter();
		gitHubParameter.setUserName(gitHubExportDialog.getUserName());
		gitHubParameter.setPassword(gitHubExportDialog.getPassword());
		gitHubParameter.setRepository(gitHubExportDialog.getRepository());
		gitHubParameter.setPath(gitHubExportDialog.getPath());
		gitHubParameter.setMessageCommit(gitHubExportDialog.getMessage());
		gitHubParameter.setFileName(gitHubExportDialog.getFileName());
		gitHubParameter.setExtension(getExtension());		
		gitHubParameter
				.setContent(getContent((VectorLayer) getSelectedLayer()));
		return gitHubParameter;
	}

	private String getFileName() {
		final VectorLayer selectedLayer = (VectorLayer) getSelectedLayer();
		return selectedLayer.getName();
	}

	private String getExtension() {
		return exportDataDialog.getSelectedFormat().getName().toLowerCase();
	}

	private String getContent(VectorLayer selectedLayer) {

		final VectorFormat vectorFormat = exportDataDialog.getSelectedFormat();
		String content = "";
		org.gwtopenmaps.openlayers.client.format.VectorFormat format = null;

		if (vectorFormat.getId() == VectorFormat.GML_FORMAT.getId()) {
			format = new GML();
		} else if (vectorFormat.getId() == VectorFormat.KML_FORMAT.getId()) {
			format = new KML();
		} else if (vectorFormat.getId() == VectorFormat.GEO_JSON_FORMAT.getId()) {
			format = new GeoJSON();
		} else if (vectorFormat.getId() == VectorFormat.WKT_FORMAT.getId()) {
			format = new WKT();
		} else if (vectorFormat.getId() == VectorFormat.CSV_FORMAT.getId()) {
			content = new CSV(exportDataDialog.getSelectedEpsg())
					.write(selectedLayer);
		}

		if (content.isEmpty()) {
			content = format.write(getTransformedFeatures(selectedLayer));
		}

		return content;
	}

	private VectorLayer getLayerWithSelectedFeature() {
		final VectorLayer selectedLayer = (VectorLayer) getSelectedLayer();
		final FeatureSchema schema = selectedLayer.getSchema();
		VectorFeature[] features = selectedLayer.getSelectedFeatures();

		VectorLayer selectedDownloadLayer = new VectorLayer("selectedDownload",
				schema);
		for (VectorFeature feature : features) {
			selectedDownloadLayer.addFeature(feature);
		}
		return selectedDownloadLayer;
	}

	private void confirmDownloadSelected() {

		ConfirmMessageBox messageBox = messageDialogBuilder.createConfirm(
				UIMessages.INSTANCE.edtAlertDialogTitle(),
				UIMessages.INSTANCE.edtConfirmDownload(),
				ImageProvider.INSTANCE.downloadBlue24());
		messageBox.getButton(PredefinedButton.YES).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						VectorLayer tmpLayer = getLayerWithSelectedFeature();
						fileParameter.setContent(getContent(tmpLayer));
						export();
					}
				});

		messageBox.getButton(PredefinedButton.NO).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						export();
					}
				});
		messageBox.show();
	}

	private VectorFeature[] getTransformedFeatures(Vector layer) {
		List<VectorFeature> transformedFeatures = new ArrayList<VectorFeature>();

		for (VectorFeature feature : layer.getFeatures()) {
			VectorFeature featureToExport = feature.clone();
			featureToExport.getGeometry().transform(
					new Projection(geoMap.getMap().getProjection()),
					new Projection(exportDataDialog.getSelectedEpsg()));
			transformedFeatures.add(featureToExport);
		}

		VectorFeature[] transArray = new VectorFeature[transformedFeatures
				.size()];
		return transformedFeatures.toArray(transArray);
	}

	@Override
	public void onChange(Vector layer) {
		exportDataDialog.setVectorLayer((VectorLayer) layer);
	}
}