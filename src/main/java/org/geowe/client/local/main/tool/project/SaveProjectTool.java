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
package org.geowe.client.local.main.tool.project;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.export.exporter.Exporter;
import org.geowe.client.local.layermanager.tool.export.exporter.FileExporter;
import org.geowe.client.local.layermanager.tool.export.exporter.FileParameter;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.style.VectorStyleDef;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.GeoJSON;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Exporta a fichero en formato JSON la sesión de trabajo
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class SaveProjectTool extends ButtonTool {

	@Inject
	private Logger logger;
	@Inject
	private LayerManagerWidget layerManagerWidget;
	@Inject
	private ClientTaskManager taskManager;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	@Inject
	private SaveProjectDialog saveProjectDialog;

	private Exporter exporter;
	private FileParameter fileParameter;
	private ProgressBarDialog progressBar;
	private List<Layer> vectorLayers;
	private static final String PROJECT_EXTENSION = "gpj";
	
	public SaveProjectTool() {
		super(UIMessages.INSTANCE.saveProject(), ImageProvider.INSTANCE
				.saveProject24());
		setToolTipConfig(createTooltipConfig(UIMessages.INSTANCE.saveProject(),
				UIMessages.INSTANCE.saveProjectToolTipText(), Side.LEFT));
		setEnabled(false);
	}

	@Override
	protected void onRelease() {
		vectorLayers = layerManagerWidget.getLayerTree(
				LayerManagerWidget.VECTOR_TAB).getLayers();
		
		if(vectorLayers.isEmpty()) {
			showAlert("No vector layers found");
			return;
		}
		saveProjectDialog.clear();
		saveProjectDialog.setVectorLayerCount(vectorLayers.size());
		saveProjectDialog.show();
	}
	
	@PostConstruct
	private void initialize() {
		addDialogListener();		
	}

	protected void loadProject() {
		progressBar = new ProgressBarDialog(false,
				UIMessages.INSTANCE.processing());
		progressBar.show();
		
		Project project = getProyect(vectorLayers);
						
		exporter = new FileExporter();
		fileParameter = new FileParameter();

		if (layer == null) {
			messageDialogBuilder.createWarning(UIMessages.INSTANCE.warning(),
					UIMessages.INSTANCE.edtAlertMessageBoxLabel()).show();
		}
		fileParameter.setFileName(project.getName());
		fileParameter.setExtension(PROJECT_EXTENSION);
		fileParameter.setContent(project.toJSON());
		export();
	}

	private Project getProyect(List<Layer> layers) {
		Project project = saveProjectDialog.getProject();
		
		for (Layer layer : layers) {
			
			VectorStyleDef vectorStyleDef = ((VectorLayer) layer).getVectorStyle();
						
			String fillColor = vectorStyleDef.getFill().getNormalColor();											
			Double fillOpacity =  new Double(vectorStyleDef.getFill().getOpacity()/100);					
			String strokeColor = vectorStyleDef.getLine().getNormalColor();					
			Double strokeWidth =  new Double(vectorStyleDef.getLine().getThickness()/100);
					
			project.add(layer.getName(), getContentAsGeoJSON((Vector)layer),
					fillColor, fillOpacity, strokeColor, strokeWidth);
		}

		return project;

	}	

	private String getContentAsGeoJSON(Vector vectorLayer) {
		org.gwtopenmaps.openlayers.client.format.VectorFormat format = new GeoJSON();

		return format.write(getTransformedFeatures(vectorLayer));
	}

	private VectorFeature[] getTransformedFeatures(Vector vectorLayer) {
		List<VectorFeature> transformedFeatures = new ArrayList<VectorFeature>();
		if (vectorLayer.getFeatures() != null) {
//			logger.info("N. features de la Capa: " + layer.getFeatures().length);
			for (VectorFeature feature : vectorLayer.getFeatures()) {
				VectorFeature featureToExport = feature.clone();
				featureToExport.getGeometry().transform(
						new Projection(GeoMap.INTERNAL_EPSG),
						new Projection("WGS84"));
				transformedFeatures.add(featureToExport);
			}
		}
		VectorFeature[] transArray = new VectorFeature[transformedFeatures
				.size()];
		return transformedFeatures.toArray(transArray);
	}

	private void export() {
		taskManager.execute(new Runnable() {
			@Override
			public void run() {
				
				((FileExporter)exporter).exportZip(fileParameter);
				progressBar.hide();
			}
		});
	}

	private void addDialogListener() {
		saveProjectDialog.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						
						if(saveProjectDialog.getName().trim().isEmpty()) {
							 showAlert("filename is required");
							return;
						}

						taskManager.execute(new Runnable() {

							@Override
							public void run() {
								loadProject();
							}

						});
					}
				});
	}
	
	private void showAlert(final String errorMsg) {
		AlertMessageBox messageBox = new AlertMessageBox(
				UIMessages.INSTANCE.warning(), errorMsg);
		messageBox.show();
	}

}
