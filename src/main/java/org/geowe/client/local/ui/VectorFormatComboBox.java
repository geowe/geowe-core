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

import java.util.Collection;

import org.geowe.client.local.layermanager.tool.export.VectorFormat;
import org.geowe.client.local.layermanager.tool.export.VectorFormatProperties;
import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * Componente gráfico reutilizable que representa una lista desplegable
 * con un cojunto de formatos vectoriales soportados.
 * 
 * @author Atanasio Muñoz
 *
 */
public class VectorFormatComboBox extends ComboBox<VectorFormat> {

	public VectorFormatComboBox(String width,
			Collection<VectorFormat> vectorFormats) {
		super(new ListStore<VectorFormat>(
				((VectorFormatProperties)GWT.create(VectorFormatProperties.class)).key()),
				((VectorFormatProperties)GWT.create(VectorFormatProperties.class)).name());
		initialize(width);
		getStore().addAll(vectorFormats);
	}

	private void initialize(String width) {
		setWidth(width);
		setTypeAhead(true);		
		setEmptyText(UIMessages.INSTANCE.comboEmptyText());
		setTriggerAction(TriggerAction.ALL);
		setForceSelection(true);
		setEditable(false);
		enableEvents();
		createSelectionHandler();
	}

	private void createSelectionHandler() {
		addSelectionHandler(new SelectionHandler<VectorFormat>() {
			@Override
			public void onSelection(SelectionEvent<VectorFormat> event) {
				setValue(event.getSelectedItem(), true);
			}
		});
	}
}
