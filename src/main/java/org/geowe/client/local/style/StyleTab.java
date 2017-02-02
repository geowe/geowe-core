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

import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.util.JSObject;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.MarginData;

/**
 * Implementacion base abstracta de una pestaña del dialogo de 
 * gestion de estilos de una capa vectorial. 
 * 
 * @author Atanasio Muñoz
 *
 */
public abstract class StyleTab implements IsWidget {
	
	protected VerticalPanel panel;
	protected VectorLayer selectedLayer;
	
	@Override
	public Widget asWidget() {
		if (panel == null) {			
			panel = new VerticalPanel();
			panel.setWidth("480px");
			panel.setLayoutData(new MarginData(10));
			
			initializePanel();
			this.setSelectedLayer(null);
		}
		
		return panel;
	}	
	
	public void setSelectedLayer(VectorLayer selectedLayer) {
		this.selectedLayer = selectedLayer;	
		updateLayerStyleData();
	}
	
	protected JSObject getDefaultStyle() {
		return selectedLayer.getStyleMap().getJSObject()
				.getProperty("styles").getProperty("default")
				.getProperty("defaultStyle");
	}
	
	protected abstract void initializePanel();	
	protected abstract void updateLayerStyleData();
	protected abstract void addKeyShortcut(TextButton button, int keyCode);
}
