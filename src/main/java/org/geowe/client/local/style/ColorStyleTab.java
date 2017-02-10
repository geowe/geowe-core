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
import org.geowe.client.local.model.style.VectorStyleDef;
import org.geowe.client.local.ui.ColorPicker;
import org.geowe.client.local.ui.KeyShortcutHandler;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.FocusEvent;
import com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Pestaña de configuración de estilos de color de línea y relleno, 
 * opacidad y grosor de línea, perteneciente al diálogo de gestión
 * de estilos.
 * 
 * @author Atanasio Muñoz
 *
 */
public class ColorStyleTab extends StyleTab implements 
	ColorPicker.SelectedColorChangedListener {

	private TextField fillColor;
	private Slider fillOpacity;
	private TextField lineColor;
	private Slider lineThick;
	private ColorPicker colorPicker;

	private TextField lastFocusedTextField;

	@Override
	protected void initializePanel() {
		String fieldWidth = "125px";

		fillColor = new TextField();
		fillColor.setWidth(fieldWidth);
		addFocusHandler(fillColor);
		
		fillOpacity = new Slider();
		fillOpacity.setWidth(fieldWidth);
		fillOpacity.setValue(0);
		fillOpacity.setIncrement(1);
		
		lineColor = new TextField();
		lineColor.setWidth(fieldWidth);
		addFocusHandler(lineColor);
		
		lineThick = new Slider();
		lineThick.setWidth(fieldWidth);
		lineThick.setValue(0);
		lineThick.setMaxValue(20);
		lineThick.setIncrement(1);

		colorPicker = new ColorPicker();
		colorPicker.addSelectedColorChangedListener(this);

		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.setSpacing(10);

		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		vlc.add(new FieldLabel(fillColor, UIMessages.INSTANCE.vlswFillcolor()),
				new VerticalLayoutData(-18, -1));
		vlc.add(new FieldLabel(fillOpacity, UIMessages.INSTANCE
				.vlswFillopacity()), new VerticalLayoutData(-18, -1));
		vlc.add(new FieldLabel(lineColor, UIMessages.INSTANCE.vlswStrokeColor()),
				new VerticalLayoutData(-18, -1));
		vlc.add(new FieldLabel(lineThick, UIMessages.INSTANCE.vlswStrokeWidth()),
				new VerticalLayoutData(-18, -1));

		mainPanel.add(vlc);
		mainPanel.add(colorPicker);
		
		panel.add(mainPanel);
	}
	
	public String getFillColor() {
		return fillColor.getText();
	}
	
	public Double getFillOpacity() {
		return fillOpacity.getValue()/100.0;
	}
	
	public String getStrokeColor() {
		return lineColor.getText();
	}
	
	public Integer getStrokeWidth() {
		return lineThick.getValue();
	}		
	
	@Override
	public void onSelectedColorChanged(String selectedColor) {
		lastFocusedTextField.setText(selectedColor);		
		colorPicker.slide(false);		
	}	
		
	private void addFocusHandler(final TextField textField) {
		textField.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				lastFocusedTextField = textField;
				colorPicker.slide(true);
			}
		});
	}	
	
	@Override
	protected void updateStyleData(VectorStyleDef style) {	
		fillColor.setText(style.getFill().getNormalColor());
		fillOpacity.setValue(new Double(style.getFill().getOpacity() * 100.0).intValue());
		lineColor.setText(style.getLine().getNormalColor());
		lineThick.setValue(style.getLine().getThickness());		
	}

	@Override
	protected void addKeyShortcut(TextButton button, int keyCode) {
		KeyShortcutHandler keyShortcut = new KeyShortcutHandler(button, keyCode);
		
		fillColor.addKeyDownHandler(keyShortcut);
		lineColor.addKeyDownHandler(keyShortcut);
	}
}
