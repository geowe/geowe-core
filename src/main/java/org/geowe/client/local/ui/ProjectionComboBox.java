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

import org.geowe.client.local.main.map.Projections;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

/**
 * Componente gráfico reutilizable que representa una lista desplegable
 * con las proyecciones o sistemas de referencia (SRS) soportados 
 * en GeoWE.
 * 
 * @author Atanasio Muñoz
 *
 */
public class ProjectionComboBox extends SimpleComboBox<String> {
	
	public ProjectionComboBox(String width) {
		super(new LabelProvider<String>() {
			@Override
			public String getLabel(String item) {
				return item;
			}			
		});
		
		setWidth(width);
		setTypeAhead(true);	
		setTriggerAction(TriggerAction.ALL);			
		setForceSelection(true);
		setEditable(false);
		enableEvents();		
		
		add(Projections.getSupportedProjections());		
	}
}
