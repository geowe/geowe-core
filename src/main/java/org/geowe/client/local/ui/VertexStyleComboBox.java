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
package org.geowe.client.local.ui;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.style.VertexStyleDef;
import org.geowe.client.local.style.VertexStyleDefProperties;
import org.geowe.client.local.style.VertexStyles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * Componente gráfico que representa una lista desplegable con los
 * estilos de vértice soportados en GeoWE. Se incluye una representación
 * visual de cada estilo.
 * 
 * @author Atanasio Muñoz
 *
 */
public class VertexStyleComboBox extends ComboBox<VertexStyleDef> {
	
	interface VertexStyleComboTemplates extends XTemplates {
		@XTemplate("<img width=\"16\" height=\"11\" src=\"{imageUri}\"> {name}")
	    SafeHtml vertexStyle(SafeUri imageUri, String name); 
	}

	public VertexStyleComboBox(String width) {
		super(new ListStore<VertexStyleDef>(
				((VertexStyleDefProperties)GWT.create(VertexStyleDefProperties.class)).key()),
				((VertexStyleDefProperties)GWT.create(VertexStyleDefProperties.class)).name(),
				new AbstractSafeHtmlRenderer<VertexStyleDef>() {
					final VertexStyleComboTemplates comboBoxTemplates = GWT
							.create(VertexStyleComboTemplates.class);

					public SafeHtml render(VertexStyleDef item) {
						return comboBoxTemplates.vertexStyle(item.getImage()
								.getSafeUri(), item.getName());
					}
				});
		
		setWidth(width);
		setTypeAhead(true);
		setEmptyText(UIMessages.INSTANCE.sbLayerComboEmptyText());
		setTriggerAction(TriggerAction.ALL);
		setForceSelection(true);
		setEditable(false);
		enableEvents();
		
		getStore().addAll(VertexStyles.getAll());
	}
}
