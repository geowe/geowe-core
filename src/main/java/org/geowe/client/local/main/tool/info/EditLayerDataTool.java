/*
 * #%L
 * GeoWE Project
 * %%
 * Copyright (C) 2015 - 2017 GeoWE.org
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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.LayerTool;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Herramienta que muestra el diálogo de edición de datos alfanuméticos
 * de la capa seleccionada
 * 
 * @author Atanasio Muñoz (ata@geowe.org)
 *
 */
@ApplicationScoped
public class EditLayerDataTool extends LayerTool {
	@Inject
	private LayerEditDialog layerEditDialog;
	
	@Inject
	public EditLayerDataTool(LayerManagerWidget layeManagerWidget, GeoMap geoMap) {
		super(layeManagerWidget, geoMap);
		setText(getName());
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.layerEditToolText();
	}
	
	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.edit32();
	}	

	@PostConstruct
	private void initialize() {
		addFinishEditingListeners();
	}	
	
	/**
	 * Añade un listener al dialogo de edición de datos de la capa
	 * para que, al confirmar la edición, se refresque la capa seleccionada 
	 * y con ello todos los dialogos que muestran información sobre la misma
	 */
	private void addFinishEditingListeners() {
		layerEditDialog.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						layerManagerWidget.setSelectedLayer(
								LayerManagerWidget.VECTOR_TAB,
								getSelectedVectorLayer());
					}
				});	
		
		layerEditDialog.getButton(PredefinedButton.CANCEL).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						layerManagerWidget.setSelectedLayer(
								LayerManagerWidget.VECTOR_TAB,
								getSelectedVectorLayer());
					}
				});		
	}
	
	@Override
	public void onClick() {
		layerEditDialog.setSelectedLayer(getSelectedVectorLayer());
		layerEditDialog.show();		
	}
}
