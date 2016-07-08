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
package org.geowe.client.local.style;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.KeyShortcutHandler;
import org.geowe.client.local.ui.VertexStyleComboBox;
import org.gwtopenmaps.openlayers.client.util.JSObject;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

/**
 * Pestaña de configuracion del estilo de representacion de los vertices, 
 * perteneciente al dialogo de gestion de estilos.
 * 
 * @author Atanasio Muñoz
 *
 */
public class VertexStyleTab extends StyleTab {
	
	private VertexStyleComboBox vertexStyle;
			
	@Override
	protected void initializePanel() {
		String fieldWidth = "125px";

		vertexStyle = new VertexStyleComboBox(fieldWidth);
		
		VerticalLayoutContainer vertexStyleContainer = new VerticalLayoutContainer();		
		vertexStyleContainer.add(new FieldLabel(vertexStyle, UIMessages.INSTANCE
				.vlswVertexStyle()), new VerticalLayoutData(-18, -1));

		panel.setSpacing(5);
		panel.add(vertexStyleContainer);		
	}
		
	public String getVertexStyle() {
		return vertexStyle.getValue() != null ?
				vertexStyle.getValue().getStyleName() : null;
	}
		
	@Override
	protected void updateLayerStyleData() {	
		if (this.panel != null && this.selectedLayer != null) {
			VertexStyleDef currentVertexStyle = null;
			
			// Estilo simple
			if (selectedLayer.getStyle() != null) {				
				currentVertexStyle = getCurrentVertexStyle(
						selectedLayer.getStyle().getGraphicName());											

			// Estilo compuesto (StyleMap)
			} else {				
				JSObject defaultStyle = getDefaultStyle();						
				
				currentVertexStyle = getCurrentVertexStyle(
						defaultStyle.getPropertyAsString("graphicName"));															
			}
			
			vertexStyle.setValue(currentVertexStyle);
		}
	}

	@Override
	protected void addKeyShortcut(TextButton button, int keyCode) {
		KeyShortcutHandler keyShortcut = new KeyShortcutHandler(button, keyCode);

		vertexStyle.addKeyDownHandler(keyShortcut);
	}
}
