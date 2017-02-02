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
import org.geowe.client.local.ui.FeatureAttributeComboBox;
import org.geowe.client.local.ui.FontSizeComboBox;
import org.geowe.client.local.ui.KeyShortcutHandler;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.FocusEvent;
import com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Pestaña de configuracion de etiquetado de features, perteneciente al dialogo
 * de gestion de estilos.
 * 
 * @author Atanasio Muñoz
 * @since 31/08/2016
 * @author rafa@geowe.org 
 * Se corrige error al quitar etiquetado issue #158
 */
public class LabelStyleTab extends StyleTab implements 
	ColorPicker.SelectedColorChangedListener{

	private CheckBox enableLabeling;
	private FeatureAttributeComboBox attributeLabel;
	private FontSizeComboBox fontSize;
	private CheckBox boldFont;
	private TextField labelBackColor;
	private ColorPicker colorPicker;
			
	@Override
	protected void initializePanel() {
		String fieldWidth = "125px";

		attributeLabel = new FeatureAttributeComboBox(fieldWidth);
		attributeLabel.setEnabled(false);

		fontSize = new FontSizeComboBox(fieldWidth);		
		fontSize.setEnabled(false);
		
		boldFont = new CheckBox();		
		boldFont.setValue(false);		
		boldFont.setEnabled(false);
		
		labelBackColor = new TextField();
		labelBackColor.setEnabled(false);
		labelBackColor.setWidth(fieldWidth);
		labelBackColor.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {				
				colorPicker.slide(true);
			}
		});
		
		colorPicker = new ColorPicker();
		colorPicker.addSelectedColorChangedListener(this);
		colorPicker.setEnabled(false);

		enableLabeling = new CheckBox();
		enableLabeling.setBoxLabel(UIMessages.INSTANCE.enableLabelStyle());
		enableLabeling.setValue(false);
		enableLabeling.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				attributeLabel.setEnabled(event.getValue());
				fontSize.setEnabled(event.getValue());
				boldFont.setEnabled(event.getValue());
				labelBackColor.setEnabled(event.getValue());
				colorPicker.slide(event.getValue());
				colorPicker.setEnabled(event.getValue());
//				if (!event.getValue()) {
//					selectedLayer.setStyleMap(StyleFactory
//							.createDefaultStyleMap());
//				}
			}
		});
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.setSpacing(8);
		
		VerticalLayoutContainer attrStyleContainer = new VerticalLayoutContainer();
		attrStyleContainer.add(new FieldLabel(attributeLabel, UIMessages.INSTANCE
				.vlswLabelAttribute()), new VerticalLayoutData(-18, -1));
		attrStyleContainer.add(
				new FieldLabel(fontSize, UIMessages.INSTANCE.vlsFontSize()),
				new VerticalLayoutData(-18, -1));
		attrStyleContainer.add(
				new FieldLabel(boldFont, UIMessages.INSTANCE.vlsFontBold()),
				new VerticalLayoutData(-18, -1));
		attrStyleContainer.add(new FieldLabel(labelBackColor,
				UIMessages.INSTANCE.background()),
				new VerticalLayoutData(-18, -1));
		
		mainPanel.add(attrStyleContainer);
		mainPanel.add(colorPicker);
		
		panel.setSpacing(2);
		panel.add(enableLabeling);
		panel.add(mainPanel);
	}
	
	public Boolean isEnableLabeling() {
		return enableLabeling.getValue();
	}
		
	public String getAttributeLabel() {
		return attributeLabel.getValue() != null ?
				attributeLabel.getValue().toString() : null;
	}	
	
	public Integer getFontSize() {
		return fontSize.getValue() != null ?
				fontSize.getValue() : null;
	}
	
	public Boolean isUseBoldLabel() {
		return boldFont.getValue();
	}
	
	public String getBackgroundColor() {
		return labelBackColor.getText();
	}
	
	@Override
	protected void updateLayerStyleData() {	
		if (this.panel != null && this.selectedLayer != null) {
			attributeLabel.loadValues(selectedLayer);					
			VectorStyleDef style = selectedLayer.getVectorStyle();
			
			this.enableLabeling.setValue(style.getLabel().isEnabled(), true);
			
			if(style.getLabel().isEnabled()) {
				this.attributeLabel.setValue(style.getLabel().getAttribute());
				this.fontSize.setValue(style.getLabel().getFontSize());
				this.boldFont.setValue(style.getLabel().isBoldStyle());
				this.labelBackColor.setText(style.getLabel().getBackgroundColor());
			} else {
				this.attributeLabel.setValue(null);
			}
		}
	}

	@Override
	protected void addKeyShortcut(TextButton button, int keyCode) {
		KeyShortcutHandler keyShortcut = new KeyShortcutHandler(button, keyCode);
		
		attributeLabel.addKeyDownHandler(keyShortcut);
		fontSize.addKeyDownHandler(keyShortcut);
		labelBackColor.addKeyDownHandler(keyShortcut);
	}

	@Override
	public void onSelectedColorChanged(String selectedColor) {
		labelBackColor.setText(selectedColor);
		colorPicker.slide(false);		
	}	
}
