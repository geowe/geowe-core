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
package org.geowe.client.local.ui;

import java.util.List;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

/**
 * Componente gráfico reutilizable que representa una lista desplegable
 * de valores numéricos de tipo entero
 * 
 * @author Atanasio Muñoz (ata@geowe.org)
 *
 */
public class IntegerValueComboBox extends SimpleComboBox<Integer> {
		
	public IntegerValueComboBox(String width) {
		super(new LabelProvider<Integer>() {
			@Override
			public String getLabel(Integer item) {
				return Integer.toString(item);
			}			
		});
		
		setWidth(width);
		setTypeAhead(true);	
		setTriggerAction(TriggerAction.ALL);			
		setForceSelection(true);
		setEditable(false);
		enableEvents();		
	}
	
	public void setValues(List<Integer> values) {
		getStore().clear();
		add(values);
	}
	
	public void addValue(Integer value) {
		add(value);
	}
	
	public Integer getMaxValue() {
		List<Integer> values = getStore().getAll();
		int maxValue = values.get(0);
		
		for(Integer value : values) {
			if(maxValue < value.intValue()) {
				maxValue = value.intValue();
			}
		}
		
		return maxValue;
	}	
}
