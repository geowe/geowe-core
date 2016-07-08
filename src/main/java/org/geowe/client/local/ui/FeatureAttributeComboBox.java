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

import org.geowe.client.local.model.vector.FeatureAttributeDef;
import org.geowe.client.local.model.vector.VectorLayer;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class FeatureAttributeComboBox extends ComboBox<FeatureAttributeDef> {

	public FeatureAttributeComboBox(String width) {
		super(new ListStore<FeatureAttributeDef>(
				new ModelKeyProvider<FeatureAttributeDef>() {
					@Override
					public String getKey(FeatureAttributeDef item) {
						return item != null ? item.getName() : null;
					}

				}),
				new LabelProvider<FeatureAttributeDef>() {
					@Override
					public String getLabel(FeatureAttributeDef item) {
						return item != null ? item.getName() : null;
					}
			});
		
		setWidth(width);
		setTypeAhead(true);
		setEmptyText("Select attribute...");
		setTriggerAction(TriggerAction.ALL);
		setForceSelection(true);
		setEditable(false);
		enableEvents();
	}
	
	public void loadValues(VectorLayer layer) {
		getStore().clear();

		if (layer != null && layer.getAttributes() != null) {
			getStore().addAll(layer.getAttributes());
		}	
	}
}
