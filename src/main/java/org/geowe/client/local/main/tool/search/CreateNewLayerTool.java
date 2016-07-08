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
package org.geowe.client.local.main.tool.search;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.LayerTool;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.info.FeatureTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Layer;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Herramienta para añadir una nueva capa a partir de unos elementos seleccionados.
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class CreateNewLayerTool extends LayerTool implements FeatureTool {

	private VectorFeature selectedFeature;
	private List<VectorFeature> selectedFeatures;
	private VectorLayer layer;

	@Inject
	public CreateNewLayerTool(final LayerManagerWidget layerManagerWidget,
			final GeoMap geoMap) {
		super(layerManagerWidget, geoMap);
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.createNewLayerTool();
	}

	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.addLayer();
	}

	@Override
	public void onClick() {
		if (selectedFeatures != null) {
			createPrompt();
		}
	}

	private void createPrompt() {
		final TextField epsgTextField = new TextField();
		epsgTextField.setText(layer.getProjection().getProjectionCode());
		epsgTextField.setEnabled(false);
		final PromptMessageBox messageBox = new PromptMessageBox(
				 UIMessages.INSTANCE.celtPromptTitle(), UIMessages.INSTANCE.celtPromptLabel());

		messageBox.add(epsgTextField);
		messageBox.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						final String value = messageBox.getValue();
						layerManagerWidget.addVector(createVectorLayer(
								selectedFeatures, value));
					}
				});
		messageBox.show();
	}

	private Layer createVectorLayer(final List<VectorFeature> selectedFeatures,
			final String layerName) {		
				
		final VectorLayerConfig layerConfig = new VectorLayerConfig();
		layerConfig.setLayerName(layerName);
		layerConfig.setEpsg(layer.getProjection().getProjectionCode());
		final VectorLayer newLayer = VectorLayerFactory.createEmptyVectorLayer(layerConfig);
				
		for (final VectorFeature vf : selectedFeatures) {			
			newLayer.addFeature(vf.clone());
		}

		return newLayer;
	}

	public VectorFeature getSelectedFeature() {
		return selectedFeature;
	}

	public void setSelectedFeature(final VectorFeature selectedFeature) {
		this.selectedFeature = selectedFeature;
		this.selectedFeatures = new ArrayList<VectorFeature>();
		this.selectedFeatures.add(selectedFeature);
		setEnabled(selectedFeature != null);
	}

	public void setSelectedFeatures(final List<VectorFeature> selectedFeatures) {
		this.selectedFeatures = selectedFeatures;
	}

	public void setLayer(final VectorLayer layer) {
		this.layer = layer;
	}

	public void setSelectedLayer(final VectorLayer layer) {
		this.layer = layer;
	}

}
