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
package org.geowe.client.local.main.tool.info;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.LayerTool;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.edition.DeleteFeatureListenerManager;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Responsible for removing the selected features
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class DeleteVectorFeatureTool extends LayerTool implements FeatureTool {

	private VectorFeature selectedFeature;
	private List<VectorFeature> selectedFeatures;
	private VectorLayer layer;
	
	@Inject
	private DeleteFeatureListenerManager deleteFeatureListenerManager;	
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	
	@Inject
	public DeleteVectorFeatureTool(LayerManagerWidget layerTreeWidget, GeoMap geoMap) {
		super(layerTreeWidget, geoMap);				
	}			
	
	@Override
	public VectorFeature getSelectedFeature() {
		return selectedFeature;
	}

	@Override
	public void setSelectedFeature(VectorFeature selectedFeature) {
		this.selectedFeature = selectedFeature;
		this.selectedFeatures = new ArrayList<VectorFeature>();
		this.selectedFeatures.add(selectedFeature);
		setEnabled(selectedFeature != null);
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.deleteVectorFeatureToolText();
	}
	
	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.deleteVector24();
	}		

	@Override
	public void onClick() {
		confirmDeletion(selectedFeatures);
	}
	
	private void confirmDeletion(final List<VectorFeature> selectedFeatures) {
		ConfirmMessageBox messageBox = messageDialogBuilder.createConfirm(
				UIMessages.INSTANCE.dtMessageBoxTitle(),
				UIMessages.INSTANCE.dtMessageBoxLabel(),
				ImageProvider.INSTANCE.deleteVector24());

		messageBox.getButton(PredefinedButton.YES).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						for (VectorFeature vectorFeature : selectedFeatures) {
							vectorFeature.destroy();
							notifyListener(vectorFeature);							
						}
					}
					
					private void notifyListener(VectorFeature vectorFeature) {
						for (DeleteFeatureListener listener : deleteFeatureListenerManager
								.getlisteners()) {
							listener.onDeleteFeature(vectorFeature);
						}
					}
				});
		messageBox.show();
	}

	@Override
	public void setSelectedFeatures(List<VectorFeature> selectedFeatures) {
		this.selectedFeatures = selectedFeatures;
	}

	@Override
	public void setSelectedLayer(VectorLayer layer) {
		this.layer = layer;
	}
}
