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

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.main.tool.spatial.geoprocess.BufferGeoprocess;
import org.geowe.client.local.main.tool.spatial.geoprocess.IInputGeoprocess;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Buffer tool representa la herramienta responsable de realizar el geoproceso de buffer (área de inluencia) sobre los elementos vectoriales de una capa
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class BufferTool extends ButtonTool {
	private final LayerManagerWidget layerManagerWidget;
	@Inject
	private BufferGeoprocess bufferGeoprocess;
	@Inject
	private IInputGeoprocess inputGeoprocess;
	@Inject
	private ClientTaskManager taskManager;

	@Inject
	public BufferTool(final GeoMap geoMap, final LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.bufferToolText(), ImageProvider.INSTANCE
				.buffer32(), layerManager);
		this.layerManagerWidget = layerManager;
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.bufferToolText(),
				UIMessages.INSTANCE.bufferToolTip(), Side.LEFT));
		setEnabled(false);
	}

	@Override
	protected void onRelease() {
		final VectorLayer layer = (VectorLayer) layerManagerWidget
				.getSelectedLayer(LayerManagerWidget.VECTOR_TAB);

		final VectorFeature[] elements = layer.getSelectedFeatures();
		if (elements != null && elements.length > 0) {
			confirmBufferSelectedElements(elements);
		} else {
			confirmBufferLayer(layer);
		}
	}

	private void confirmBufferSelectedElements(final VectorFeature... vectorFeatures) {

		final PromptMessageBox messageBox = new PromptMessageBox(
				UIMessages.INSTANCE.btMessageBoxPromptTitle(),
				UIMessages.INSTANCE.btMessageBoxPromptLabel());
		messageBox.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {

						final String value = messageBox.getValue();
						applyBuffer(Double.parseDouble(value), vectorFeatures);
					}
				});
		messageBox.show();
	}

	private void confirmBufferLayer(final VectorLayer layer) {

		final ConfirmMessageBox messageBox = new ConfirmMessageBox(
				UIMessages.INSTANCE.dtMessageBoxTitle(),
				UIMessages.INSTANCE.bufferToolConfirmText());
		messageBox.setModal(true);
		messageBox.setIcon(ImageProvider.INSTANCE.buffer32());
		messageBox.getButton(PredefinedButton.YES).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						calculateBuffer(layer);
					}
				});
		messageBox.show();
	}

	private void calculateBuffer(final VectorLayer layer) {

		final PromptMessageBox messageBox = new PromptMessageBox(
				UIMessages.INSTANCE.btMessageBoxPromptTitle(),
				UIMessages.INSTANCE.btMessageBoxPromptLabel());
		messageBox.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {

						final String value = messageBox.getValue();
						calculateBuffer(layer, Double.parseDouble(value));
					}
				});
		messageBox.show();
	}

	private void applyBuffer(final double distance, final VectorFeature... vectorFeatures) {
		final List<VectorFeature> features = new ArrayList<VectorFeature>();
		for (VectorFeature vf : vectorFeatures) {			
			features.add(vf.clone());
		}
		
		final VectorLayerConfig layerConfig = new VectorLayerConfig();
		layerConfig.setLayerName(layer.getName());
		
		final VectorLayer layer = VectorLayerFactory.createEmptyVectorLayer(layerConfig);
		layer.addFeatures(features.toArray(new VectorFeature[features.size()]));
		calculateBuffer(layer, distance);
	}

	private void calculateBuffer(final VectorLayer vectorLayer,
			final double distance) {
		taskManager.execute(new Runnable() {

			@Override
			public void run() {
				inputGeoprocess.setLayerManager(layerManagerWidget);
				inputGeoprocess.setInputLayer(vectorLayer);
				inputGeoprocess.setDistance(distance);
				bufferGeoprocess.setInputGeoprocess(inputGeoprocess);
				bufferGeoprocess.execute();
			}
		});
	}
}