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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.schema.FeatureAttributeEditingDef;
import org.geowe.client.local.main.tool.schema.FeatureSchemaDialog;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.FeatureAttributeDef;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.layer.Layer;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Diálogo de edición / visualización de los atributos de una capa vectorial. Para cada atributo
 * se permite modificar el nombre, el tipo de dato y si se muestra o no en el Map Tooltip.
 * 
 * @author Atanasio Muñoz
 *
 */
@ApplicationScoped
public class LayerSchemaTool extends LayerTool {
	@Inject
	private FeatureSchemaDialog featureSchemaDialog;
	
	@Inject
	public LayerSchemaTool(LayerManagerWidget layerTreeWidget, GeoMap geoMap) {
		super(layerTreeWidget, geoMap);
	}	
	
	@Override
	public String getName() {
		return UIMessages.INSTANCE.layerSchemaToolText();
	}

	@Override
	public void onClick() {
		VectorLayer layer = getSelectedVectorLayer();
		featureSchemaDialog.setVectorLayer(layer);
		featureSchemaDialog.show();	
	}
	
	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.attributeList24();
	}	
	
	@PostConstruct
	private void addDialogButtonHandler() {
		featureSchemaDialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {				
				editFeatureSchema(featureSchemaDialog.getVectorLayer(), 
						featureSchemaDialog.getSchemaEditing());
				
				/**
				 * Actualizamos / refrescamos la capa seleccionada actualmente para
				 * que los cambios realizados en el esquema esten disponibles para
				 * el resto de herramientas
				 */
				getLayerManagerWidget().setSelectedLayer(
						LayerManagerWidget.VECTOR_TAB, featureSchemaDialog.getVectorLayer());
			}
		});		
	}
	
	private void editFeatureSchema(VectorLayer layer, List<FeatureAttributeEditingDef> modifiedAttr) {
		clearRemovedAttributes(layer, modifiedAttr);
		
		for(FeatureAttributeEditingDef modAttr : modifiedAttr) {			
			if(isExistingAttribute(modAttr)) {		
				layer.modifyAttribute(modAttr.getName(), modAttr.getNewName(), modAttr.isShowInTooltip());				
			} else {
				layer.addAttribute(modAttr.getNewName(), modAttr.isShowInTooltip());
			}			
		}
	}
	
	private void clearRemovedAttributes(VectorLayer layer, List<FeatureAttributeEditingDef> modifiedAttr) {
		for(FeatureAttributeDef currentAttr : layer.getAttributes()) {
			if(isRemovedAttribute(currentAttr.getName(), modifiedAttr)) {
				layer.removeAttribute(currentAttr.getName());
			}
		}
	}
	
	/**
	 * Comprueba si el atributo existia en la capa previamente a la edicion
	 * @param attribute
	 * @return
	 */
	private boolean isExistingAttribute(FeatureAttributeEditingDef attribute) {
		return attribute.getName() != null && !attribute.getName().isEmpty();
	}
	
	private boolean isRemovedAttribute(String attrName, List<FeatureAttributeEditingDef> modifiedAttr) {
		boolean isRemoved = true;
		
		for(FeatureAttributeEditingDef attr : modifiedAttr) {
			if(attr.getName().equals(attrName)) {
				isRemoved = false;
				break;
			}
		}
		
		return isRemoved;
	}	
}
