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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.core.client.Style.Direction;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.widget.core.client.ColorPalette;

/**
 * Componente gráfico que representa una paleta de selección de color
 * que soporta listeners del color seleccionado. También implementa
 * un efecto de slide para mostrarlo u ocultarlo.
 * 
 * @author Atanasio Muñoz
 *
 */
public class ColorPicker extends ColorPalette {
	
	public interface SelectedColorChangedListener {
		public void onSelectedColorChanged(String selectedColor);
	}
	
	private List<SelectedColorChangedListener> listeners;
	
	public ColorPicker() {
		super();
		
		this.listeners = new ArrayList<SelectedColorChangedListener>();
		initListener();
	}
	
	public void addSelectedColorChangedListener(SelectedColorChangedListener listener) {
		this.listeners.add(listener);
	}
	
	public void slide(boolean show) {				
		if (show) {			
			this.getElement().<FxElement> cast().slideIn(Direction.RIGHT);
		} else {
			this.getElement().<FxElement> cast().slideOut(Direction.LEFT);
		}
	}
	
	private void initListener() {
		this.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				for(SelectedColorChangedListener listener : listeners) {
					listener.onSelectedColorChanged("#" + getValue());
				}
			}
		});
	}
}
