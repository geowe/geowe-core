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
package org.geowe.client.local.main.tool.spatial;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.main.tool.spatial.geoprocess.GeoprocessValidator;
import org.geowe.client.local.main.tool.spatial.geoprocess.IGeoprocess;
import org.geowe.client.local.main.tool.spatial.geoprocess.dialog.GeoprocessDialog;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Geoprocessing tool representa la herramienta responsable de realizar el análisis espacial entre capas vectoriales. Ofrece
 * un conjunto de procesamientos espaciales.
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class GeoprocessingTool extends ButtonTool {	
	@Inject
	private GeoprocessDialog spatialOperationDialog;
	private final LayerManagerWidget layerManager;	
	@Inject
	private ClientTaskManager taskManager;
	@Inject
	private GeoprocessValidator geoprocessValidator;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	@Inject
	private GeometryValidator geometryValidator;
	private static final int LAYER_1 = 1;
	private static final int LAYER_2 = 2;

	@Inject
	public GeoprocessingTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.analysisToolText(), ImageProvider.INSTANCE
				.analysis32(), layerManager);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.analysisToolText(),
				UIMessages.INSTANCE.analysisToolTip(), Side.LEFT));
		this.layerManager = layerManager;
		setEnabled(false);
	}

	@PostConstruct
	private void initialize() {

		spatialOperationDialog.getValidateLayer1button().addSelectHandler(getValidationSelectHandler(LAYER_1));
		spatialOperationDialog.getValidateLayer2button().addSelectHandler(getValidationSelectHandler(LAYER_2));

		spatialOperationDialog.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {

						taskManager.execute(new Runnable() {

							@Override
							public void run() {
								if (!spatialOperationDialog
										.hasGeoprocessSelected()) {
									messageDialogBuilder.createWarning(
											UIMessages.INSTANCE.fail(),
											"No se ha especificado geoproceso")
											.show();
									return;
								}

								IGeoprocess geoprocess = spatialOperationDialog
										.getGeoProcess();
								if (geoprocessValidator.validate(geoprocess)) {
									spatialOperationDialog.hide();
									geoprocess.execute();									
								}
							}
						});
					}
				});
	}
	
	private SelectHandler getValidationSelectHandler(final int layer) {
		return new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {

				taskManager.execute(new Runnable() {

					@Override
					public void run() {
						VectorLayer layerToValidate = spatialOperationDialog.getLayer1();
						if (layer == LAYER_2) {
							layerToValidate = spatialOperationDialog.getLayer2();
						}
						
						if(layerToValidate == null) {
							messageDialogBuilder.createWarning(
									UIMessages.INSTANCE.fail(),
									"No se ha especificado una capa")
									.show();
							return;
						}
						geometryValidator.requestValidate(layerToValidate, layerManager);
					}
				});
			}
		};
	}

	@Override
	protected void onRelease() {
		List<Layer> vectorLayers = layerManager.getLayerTree(
				LayerManagerWidget.VECTOR_TAB).getLayers();
		spatialOperationDialog.setLayers(vectorLayers);
		spatialOperationDialog.clearFields();
		spatialOperationDialog.show();
	}
}