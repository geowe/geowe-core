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
import org.geowe.client.local.layermanager.tool.export.exporter.GitHubExporter;
import org.geowe.client.local.layermanager.tool.export.exporter.GitHubParameter;
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
public class ExportDataTool extends LayerTool implements ChangeSelectedLayerListener {

	@Inject
	private ExportDataDialog exportDataDialog;
	@Inject
	private ClientTaskManager taskManager;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	@Inject
	private GitHubExporter gitHubExporter;
	@Inject
	private GitHubExportDialog gitHubExportDialog;
	private Exporter exporter;
	private FileParameter fileParameter;

	@Inject
	public ExportDataTool(LayerManagerWidget layerTreeWidget, GeoMap geoMap) {
		super(layerTreeWidget, geoMap);
	}

	@PostConstruct
	private void configureDownloadButton() {
		exportDataDialog.getDownloadFileButton().addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				exporter = new FileExporter();
				fileParameter = new FileParameter();
				fileParameter.setFileName(((VectorLayer) getSelectedLayer()).getName());
				prepareParameterAndExport();
			}
		});

		exportDataDialog.getGitHubButton().addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				gitHubExportDialog.initializeFields();
				gitHubExportDialog.setFileName(getFileName());
				gitHubExportDialog.show();
			}
		});

		gitHubExportDialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				GitHubParameter gitHubParameter = new GitHubParameter();
				gitHubParameter.setUserName(gitHubExportDialog.getUserName());
				gitHubParameter.setPassword(gitHubExportDialog.getPassword());
				gitHubParameter.setRepository(gitHubExportDialog.getRepository());
				gitHubParameter.setPath(gitHubExportDialog.getPath());
				gitHubParameter.setMessageCommit(gitHubExportDialog.getMessage());
				gitHubParameter.setFileName(gitHubExportDialog.getFileName());
				fileParameter = gitHubParameter;
				exporter = gitHubExporter;
				prepareParameterAndExport();

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
		Layer selectedLayer = getLayerManagerWidget().getSelectedLayer(LayerManagerWidget.VECTOR_TAB);
		if (isLayerToExportValid(selectedLayer)) {
			exportDataDialog.setVectorLayer((VectorLayer) selectedLayer);
			exportDataDialog.show();
		}
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
			messageDialogBuilder
					.createError(UIMessages.INSTANCE.edtAlertDialogTitle(), UIMessages.INSTANCE.edtAlertDialogLabel())
					.show();
			isValid = false;
		}
		return isValid;
	}

	private boolean isLayerToExportValid(Layer layer) {
		boolean isValid = true;
		// TODO: controlar si es la raiz del árbol
		if (layer == null) {
			Info.display(UIMessages.INSTANCE.edtAlertDialogTitle(), UIMessages.INSTANCE.edtAlertMessageBoxLabel());
			isValid = false;
		}
		return isValid;
	}

	private void prepareParameterAndExport() {

		if (isLayerToExportValid(exportDataDialog.getVectorLayer())) {

			final VectorLayer selectedLayer = (VectorLayer) getSelectedLayer();
			final VectorFeature[] selectedFeatures = selectedLayer.getSelectedFeatures();
			final VectorFormat vectorFormat = exportDataDialog.getSelectedFormat();

			if (selectedFeatures != null && selectedFeatures.length > 0) {
				confirmOnlySelected(vectorFormat, selectedFeatures, selectedLayer);
			} else {
				createFileParameters(vectorFormat, selectedLayer);
				export();
			}
		}
	}

	private String getFileName() {
		final VectorLayer selectedLayer = (VectorLayer) getSelectedLayer();
		return selectedLayer.getName();
	}

	private void createFileParameters(VectorFormat vectorFormat, VectorLayer selectedLayer) {
		String content = "";
		String extension = "";
		org.gwtopenmaps.openlayers.client.format.VectorFormat format = null;

		if (vectorFormat.getId() == VectorFormat.GML_FORMAT.getId()) {
			format = new GML();
			extension = VectorFormat.GML_FORMAT.getName().toLowerCase();
		} else if (vectorFormat.getId() == VectorFormat.KML_FORMAT.getId()) {
			format = new KML();
			extension = VectorFormat.KML_FORMAT.getName().toLowerCase();
		} else if (vectorFormat.getId() == VectorFormat.GEO_JSON_FORMAT.getId()) {
			format = new GeoJSON();
			extension = VectorFormat.GEO_JSON_FORMAT.getName().toLowerCase();
		} else if (vectorFormat.getId() == VectorFormat.WKT_FORMAT.getId()) {
			format = new WKT();
			extension = VectorFormat.WKT_FORMAT.getName().toLowerCase();
		} else if (vectorFormat.getId() == VectorFormat.CSV_FORMAT.getId()) {
			content = new CSV(exportDataDialog.getSelectedEpsg()).write(selectedLayer);
			extension = VectorFormat.CSV_FORMAT.getName().toLowerCase();
		}

		if (content.isEmpty()) {
			content = format.write(getTransformedFeatures(selectedLayer));
		}

		fileParameter.setContent(content);
		fileParameter.setExtension(extension);

	}

	private VectorLayer getLayerWithSelectedFeature(VectorFeature[] selectedFeatures, FeatureSchema schema) {
		VectorLayer selectedDownloadLayer = new VectorLayer("selectedDownload", schema);
		for (VectorFeature feature : selectedFeatures) {
			selectedDownloadLayer.addFeature(feature);
		}
		return selectedDownloadLayer;
	}

	private void confirmOnlySelected(final VectorFormat vectorFormat, final VectorFeature[] selectedFeatures,
			final VectorLayer selectedLayer) {

		ConfirmMessageBox messageBox = messageDialogBuilder.createConfirm(UIMessages.INSTANCE.edtAlertDialogTitle(),
				UIMessages.INSTANCE.edtConfirmDownload(), ImageProvider.INSTANCE.downloadBlue24());
		messageBox.getButton(PredefinedButton.YES).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {

				VectorLayer tmpLayer = getLayerWithSelectedFeature(selectedFeatures, selectedLayer.getSchema());
				createFileParameters(vectorFormat, tmpLayer);
				export();
			}
		});

		messageBox.getButton(PredefinedButton.NO).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				createFileParameters(vectorFormat, selectedLayer);
				export();
			}
		});
		messageBox.show();
	}

	private VectorFeature[] getTransformedFeatures(Vector layer) {
		List<VectorFeature> transformedFeatures = new ArrayList<VectorFeature>();

		for (VectorFeature feature : layer.getFeatures()) {
			VectorFeature featureToExport = feature.clone();
			featureToExport.getGeometry().transform(new Projection(geoMap.getMap().getProjection()),
					new Projection(exportDataDialog.getSelectedEpsg()));
			transformedFeatures.add(featureToExport);
		}

		VectorFeature[] transArray = new VectorFeature[transformedFeatures.size()];
		return transformedFeatures.toArray(transArray);
	}

	@Override
	public void onChange(Vector layer) {
		exportDataDialog.setVectorLayer((VectorLayer) layer);
	}
}