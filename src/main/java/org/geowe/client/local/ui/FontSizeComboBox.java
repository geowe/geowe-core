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

import java.util.Arrays;
import java.util.List;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

/**
 * Componente gráfico reutilizable que representa una lista desplegable
 * con tamaños de fuente. Por defecto se cargar con los tamaños
 * soportados en GeoWE.
 * 
 * @author Atanasio Muñoz
 *
 */
public class FontSizeComboBox extends SimpleComboBox<Integer> {
	private static final List<Integer> fontSizes = Arrays.asList(
			8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36);
	
	public FontSizeComboBox(String width) {
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
		
		add(fontSizes);		
	}
	
	public void setFontSizes(List<Integer> sizes) {
		getStore().clear();
		add(sizes);
	}
}
