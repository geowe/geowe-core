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

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.main.tool.spatial.geoprocess.GeoprocessValidator;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Geometry validation tool representa la herramienta responsable de realizar las validaciones geométricas sobre los elementos vectoriales
 * de una capa
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class GeometryValidationTool extends ButtonTool {
	private final LayerManagerWidget layerManager;
	@Inject
	private GeometryValidator geometryValidator;	
	@Inject
	private GeoprocessValidator geoprocessValidator;
	@Inject
	public GeometryValidationTool(final GeoMap geoMap, final LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.validationToolText(), ImageProvider.INSTANCE
				.validation24(), layerManager);
		this.layerManager = layerManager;
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.validationToolText(),
				UIMessages.INSTANCE.descValidationToolText(), Side.LEFT));
		setEnabled(false);		
	}

	@Override
	protected void onRelease() {
		final VectorLayer layer = (VectorLayer) layerManager
				.getSelectedLayer(LayerManagerWidget.VECTOR_TAB);
		if(geoprocessValidator.isValid(layer)) {
			final VectorFeature[] elements = layer.getSelectedFeatures();
			if (elements != null && elements.length > 0) {			
				geometryValidator.requestValidate(layer.getName(), layerManager, elements);
			} else {
				confirm(layer);
			}		
		}		
	}
	
	private void confirm(final VectorLayer layer) {

		ConfirmMessageBox messageBox = new ConfirmMessageBox(
				UIMessages.INSTANCE.dtMessageBoxTitle(),
				UIMessages.INSTANCE.confirmValidationTool(layer.getName()));
		messageBox.setModal(true);
		messageBox.setIcon(ImageProvider.INSTANCE.buffer32());
		messageBox.getButton(PredefinedButton.YES).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						geometryValidator.requestValidate(layer, layerManager);
					}
				});
		messageBox.show();
	}
}