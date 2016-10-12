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
package org.geowe.client.local.layermanager.tool;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Tool responsible for duplicate the selected Vector layer
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class CopyLayerTool extends LayerTool {
	
	@Inject
	private ClientTaskManager taskManager;

	@Inject
	public CopyLayerTool(final LayerManagerWidget layerManagerWidget, final GeoMap geoMap) {
		super(layerManagerWidget, geoMap);
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.copyLayerToolText();
	}

	@Override
	public void onClick() {
		createPrompt();
	}
	
	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.copyLayer24();
	}
	
	private void createPrompt() {
		final PromptMessageBox messageBox = new PromptMessageBox(
				UIMessages.INSTANCE.cltMessageBoxTitle(),
				UIMessages.INSTANCE.cltMessageBoxLabel());
		messageBox.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
		     @Override
		     public void onSelect(final SelectEvent event) {
		    		final VectorLayer layer = (VectorLayer) getSelectedLayer();
		    		layerManagerWidget.addVector(duplicate(layer, messageBox.getValue()));
		     }
		   });
		
		messageBox.show();
	}
	
	public VectorLayer duplicate(final VectorLayer layer, final String layerName) {
		final ProgressBarDialog autoMessageBox = new ProgressBarDialog(false,
				UIMessages.INSTANCE.processing());
		autoMessageBox.show();
		final VectorLayerConfig layerConfig = new VectorLayerConfig();
		layerConfig.setLayerName(layerName);
		layerConfig.setEpsg(layer.getProjection().getProjectionCode());
		final VectorLayer newLayer = VectorLayerFactory.createEmptyVectorLayer(layerConfig);
		
		if (layer.getFeatures() != null) {

			taskManager.execute(new Runnable() {

				@Override
				public void run() {

					for (final VectorFeature vf : layer.getFeatures()) {
						final VectorFeature vf2 = vf.clone();
						newLayer.addFeature(vf2);
					}
					autoMessageBox.hide();
				}
			});

		}
		return newLayer;
	}
}
