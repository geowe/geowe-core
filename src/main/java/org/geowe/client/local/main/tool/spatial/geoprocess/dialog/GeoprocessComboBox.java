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
package org.geowe.client.local.main.tool.spatial.geoprocess.dialog;

import java.util.Collection;

import org.geowe.client.local.main.tool.spatial.geoprocess.Geoprocess;
import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * GeoprocessComboBox representa la lista desplegable del conjunto de operaciones de geoprocesamiento.
 * 
 * @author jose@geowe.org
 *
 */

public class GeoprocessComboBox extends ComboBox<Geoprocess> {

	public GeoprocessComboBox(Collection<Geoprocess> spatialOperations) {
		super(new ListStore<Geoprocess>(
				((GeoprocessProperties)GWT.create(GeoprocessProperties.class)).key()),
				((GeoprocessProperties)GWT.create(GeoprocessProperties.class)).name());
		initialize();
		getStore().addAll(spatialOperations);
	}

	private void initialize() {		
		setTypeAhead(true);		
		setEmptyText(UIMessages.INSTANCE.comboEmptyText());
		setTriggerAction(TriggerAction.ALL);
		setForceSelection(true);
		setEditable(false);
		enableEvents();
		createSelectionHandler();
	}

	private void createSelectionHandler() {
		addSelectionHandler(new SelectionHandler<Geoprocess>() {
			@Override
			public void onSelection(SelectionEvent<Geoprocess> event) {
				setValue(event.getSelectedItem(), true);
			}
		});
	}
}