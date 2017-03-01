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

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.main.tool.map.catalog.LayerLoader;
import org.geowe.client.local.main.tool.map.catalog.model.VectorLayerDef;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.style.VectorStyleDef;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.inject.Inject;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

/**
 * Responsable de cargar los proyectos en GeoWE
 * 
 * @author jose@geowe.org
 *
 */

@ApplicationScoped
public class ProjectLoader {
	@Inject
	private InfoProjectTool infoProjectTool;
	@Inject
	private ClientTaskManager taskManager;
	
	public void load(String jsonProject) {
		
		Project project = new Project(jsonProject);
		List<ProjectVectorLayer> projectLayers = project.getVectors();
		for(final ProjectVectorLayer projectLayer: projectLayers) {
			taskManager.execute(new Runnable() {

				@Override
				public void run() {
					loadLayer( projectLayer);
				}

			});
			
		}
		
		infoProjectTool.setEnabled(true);
		infoProjectTool.setProject(project);
		
	}
	
	
	private void loadLayer(ProjectVectorLayer projectLayer) {
		VectorLayerConfig layerConfig = null;
		Layer layer = null;

		try {
			layerConfig = new VectorLayerConfig();
			layerConfig.setEpsg("WGS84");
			layerConfig.setGeoDataFormat(VectorLayerDef.GEOJSON_CSS);
			layerConfig.setLayerName(projectLayer.getName());
			layerConfig.setGeoDataString(projectLayer.getContent());

			layer = VectorLayerFactory.createVectorLayerFromGeoData(layerConfig);
			VectorLayer vector = (VectorLayer)layer;
			ProjectLayerStyle styleProjectLayer = projectLayer.getStyle();
			VectorStyleDef vectorStyleDef = vector.getVectorStyle();
			vectorStyleDef.getFill().setNormalColor(styleProjectLayer.getFillColor());			
			vectorStyleDef.getFill().setOpacity( styleProjectLayer.getFillOpacity() );
			vectorStyleDef.getLine().setNormalColor(styleProjectLayer.getStrokeColor());
			vectorStyleDef.getLine().setThickness((int) Math.floor(styleProjectLayer.getStrokeWidth()));
			vector.setVectorStyle(vectorStyleDef);

		} catch (Exception e) {
			showAlert(UIMessages.INSTANCE.gditAlertMessage());
		}

		LayerLoader.load(layer);		
	}
	
	private void showAlert(final String errorMsg) {
		AlertMessageBox messageBox = new AlertMessageBox(
				UIMessages.INSTANCE.warning(), errorMsg);
		messageBox.show();
	}

}
