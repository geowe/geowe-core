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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.main.tool.map.catalog.model.VectorLayerDef;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.util.JSObject;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DOM;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;

/**
 * Responsable de cargar la configuración de un proyecto de geowe
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class OpenProjectTool extends ButtonTool {

	private ProgressBarDialog autoMessageBox;
	@Inject
	private ClientTaskManager taskManager;
	@Inject
	private OpenProjectDialog openProjectDialog;
	private LayerManagerWidget layerManagerWidget;

	@Inject
	public OpenProjectTool(final LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.openProject(), ImageProvider.INSTANCE
				.openProject24());
		setToolTipConfig(createTooltipConfig(UIMessages.INSTANCE.openProject(),
				UIMessages.INSTANCE.openProjectToolTipText(), Side.LEFT));
		this.layerManagerWidget = layerManager;
	}
	
	@Override
	protected void onRelease() {
		openProjectDialog.clear();
		openProjectDialog.show();
	}
	
	@PostConstruct
	private void initialize() {
		addDialogListener();
		addCloseButtonHandler();
		openProjectDialog.getUploadPanel().addSubmitCompleteHandler(new SubmitCompleteHandler() {
			public void onSubmitComplete(final SubmitCompleteEvent event) {

				final Element label = DOM.createLabel();
				label.setInnerHTML(event.getResults());

				final String contentFile = label.getInnerText();
				
				openProjectDialog.hide();
				if (hasError(contentFile)) {
					autoMessageBox.hide();
					showAlert("Error: " + contentFile);
					return;
				}
				
				Project project = getProject(contentFile);
				List<ProjectVectorLayer> projectLayers = project.getVectors();
				for(final ProjectVectorLayer projectLayer: projectLayers) {
					taskManager.execute(new Runnable() {

						@Override
						public void run() {
							loadLayer( projectLayer);
						}

					});
					
				}
				
				autoMessageBox.hide();
				
			}

			private boolean hasError(final String contentFile) {
				return contentFile.startsWith("413")
						|| contentFile.startsWith("500");
			}

			
		});
	}
	
	private void loadLayer(ProjectVectorLayer projectLayer) {
		VectorLayerConfig layerConfig = null;
		Layer layer = null;

		try {
			layerConfig = new VectorLayerConfig();
			layerConfig.setEpsg("WGS84");
			layerConfig.setGeoDataFormat(VectorLayerDef.GEOJSON);
			layerConfig.setLayerName(projectLayer.getName());
			layerConfig.setGeoDataString(projectLayer.getContent());

			layer = VectorLayerFactory.createVectorLayerFromGeoData(layerConfig);
			Vector vector = (Vector)layer;
			JSObject style = getDefaultStyle(vector);
			StyleProjectLayer styleProjectLayer = projectLayer.getStyle();
			
			style.setProperty("fillColor", styleProjectLayer.getFillColor());
			style.setProperty("fillOpacity", styleProjectLayer.getFillOpacity());
			style.setProperty("strokeColor", styleProjectLayer.getStrokeColor());
			style.setProperty("strokeWidth", styleProjectLayer.getStrokeWidth());

		} catch (Exception e) {
			showAlert(UIMessages.INSTANCE.gditAlertMessage());
		}

		layerManagerWidget.addVector(layer);
		layerManagerWidget.setSelectedLayer(LayerManagerWidget.VECTOR_TAB, layer);
	}
	
	protected JSObject getDefaultStyle(Vector layer) {
		return layer.getStyleMap().getJSObject()
				.getProperty("styles").getProperty("default")
				.getProperty("defaultStyle");
	}
	
	private Project getProject(String json) {
		Project project = new Project();
		
		final JSONValue jsonValue = JSONParser.parseLenient(json);
		final JSONObject jsonObject = jsonValue.isObject();
		
		//String projectName = jsonObject.get("name").isString().stringValue();
		String projectDate = jsonObject.get("date").isString().stringValue();
		String projectTitle = jsonObject.get("title").isString().stringValue();
		String projectVersion = jsonObject.get("version").isString().stringValue();
		String projectDescription = jsonObject.get("description").isString().stringValue();
		
		//project.setName(projectName);
		project.setDate(projectDate);
		project.setTitle(projectTitle);
		project.setVersion(projectVersion);
		project.setDescription(projectDescription);
		
		JSONArray layersArray = jsonObject.get("vectors").isArray();
		
		if (layersArray != null) {
	        for (int i = 0; i < layersArray.size(); i++) {
	            JSONObject projectLayerObj = layersArray.get(i).isObject();
	            String name = projectLayerObj.get("name").isString().stringValue();
	            String content = projectLayerObj.get("content").isString().stringValue();	            
	            JSONObject styleProjectLayer = projectLayerObj.get("style").isObject();
	            
	            String fillColor = styleProjectLayer.get("fillColor").isString().stringValue();
	            Double fillOpacity = styleProjectLayer.get("fillOpacity").isNumber().doubleValue();
	            String strokeColor = styleProjectLayer.get("strokeColor").isString().stringValue();
	            Double strokeWidth = styleProjectLayer.get("strokeWidth").isNumber().doubleValue();
	            
	            project.add(name, content, fillColor, fillOpacity, strokeColor, strokeWidth);	           
	        }
		
		}
		
		return project;
	}
	
	private void showAlert(final String errorMsg) {
		AlertMessageBox messageBox = new AlertMessageBox(
				UIMessages.INSTANCE.warning(), errorMsg);
		messageBox.show();
	}
	
	private void addDialogListener() {
		openProjectDialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(final SelectEvent event) {
				autoMessageBox = new ProgressBarDialog(false,
						UIMessages.INSTANCE.processing());
				taskManager.execute(new Runnable() {

					@Override
					public void run() {
						autoMessageBox.show();
						openProjectDialog.getUploadPanel().submit();
					}

				});
			}
		});
	}

	protected void addCloseButtonHandler() {
		openProjectDialog.getButton(PredefinedButton.CANCEL).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						openProjectDialog.hide();
					}
				});
	}
}
