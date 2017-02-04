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
import org.geowe.client.local.ui.KeyShortcutHandler;
import org.geowe.client.local.ui.VertexStyleComboBox;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.IntegerSpinnerField;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Pestaña de configuracion del estilo de representacion de los vertices, 
 * perteneciente al dialogo de gestion de estilos.
 * 
 * @author Atanasio Muñoz
 *
 */
public class VertexStyleTab extends StyleTab {
	private static final String RADIO_GROUP = "VERTEX_GROUP";
	
	private VertexStyleComboBox vertexStyle;
	private TextField externalGraphic;
	private RadioButton basicStyleOption;
	private RadioButton advancedStyleOption;
	private IntegerSpinnerField graphicWidth;
	private IntegerSpinnerField graphicHeight;
				
	@Override
	protected void initializePanel() {
		int fieldWidth = 100;

		vertexStyle = new VertexStyleComboBox(fieldWidth + "px");
		
		basicStyleOption = new RadioButton(RADIO_GROUP);
		basicStyleOption.setText(" " + UIMessages.INSTANCE.vlswBasicVertexStyle());
		basicStyleOption.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {				
				vertexStyle.setEnabled(event.getValue());
				externalGraphic.setEnabled(!event.getValue());
				graphicWidth.setEnabled(!event.getValue());
				graphicHeight.setEnabled(!event.getValue());
			}
		});
				
		externalGraphic = new TextField();
		externalGraphic.setWidth(fieldWidth);
		externalGraphic.setEmptyText(UIMessages.INSTANCE.vlswVertexIconEmptyText());
		
		graphicWidth = new IntegerSpinnerField();
		graphicWidth.setWidth(fieldWidth / 2);
		
		graphicHeight = new IntegerSpinnerField();
		graphicHeight.setWidth(fieldWidth / 2);
		
		advancedStyleOption = new RadioButton(RADIO_GROUP);		
		advancedStyleOption.setText(" " +  UIMessages.INSTANCE.vlswAdvancedVertexStyle());
		advancedStyleOption.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				vertexStyle.setEnabled(!event.getValue());
				externalGraphic.setEnabled(event.getValue());
				graphicWidth.setEnabled(event.getValue());
				graphicHeight.setEnabled(event.getValue());
			}
		});
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.setSpacing(10);
			
		VerticalPanel basicStyleContainer = new VerticalPanel();
		basicStyleContainer.setSpacing(5);
		basicStyleContainer.add(basicStyleOption);
		basicStyleContainer.add(new FieldLabel(vertexStyle, UIMessages.INSTANCE
				.vlswVertexStyle()));		
		
		VerticalPanel advancedStyleContainer = new VerticalPanel();
		advancedStyleContainer.setSpacing(5);
		advancedStyleContainer.addStyleName(ThemeStyles.get().style().borderLeft());
		advancedStyleContainer.add(advancedStyleOption);
		advancedStyleContainer.add(new FieldLabel(externalGraphic, UIMessages.INSTANCE
				.vlswVertexIcon()));
		advancedStyleContainer.add(new FieldLabel(graphicWidth, UIMessages.INSTANCE
				.vlswVertexIconWidth()));
		advancedStyleContainer.add(new FieldLabel(graphicHeight, UIMessages.INSTANCE
				.vlswVertexIconHeight()));
		
		mainPanel.add(basicStyleContainer);
		mainPanel.add(advancedStyleContainer);

		panel.setSpacing(5);			
		panel.add(mainPanel);
	}
	
	public boolean isBasicStyle() {
		return basicStyleOption.getValue();
	}
	
	public boolean isAdvancedStyle() {
		return advancedStyleOption.getValue();
	}
		
	public String getVertexStyle() {
		return vertexStyle.getValue() != null ?
				vertexStyle.getValue().getStyleName() : null;
	}
	
	public String getExternalGraphic() {
		return externalGraphic.getText();
	}
	
	public int getGraphicWidth() {
		return graphicWidth.getValue();
	}
	
	public int getGraphicHeight() {
		return graphicHeight.getValue();
	}
		
	@Override
	protected void updateLayerStyleData() {	
		if (this.panel != null && this.selectedLayer != null) {
			VectorStyleDef style = selectedLayer.getVectorStyle();
			
			basicStyleOption.setValue(style.getPoint().getExternalGraphic() == null, true);
			advancedStyleOption.setValue(style.getPoint().getExternalGraphic() != null, true);
			vertexStyle.setValue(style.getPoint().getVertexStyle());
			externalGraphic.setValue(style.getPoint().getExternalGraphic());
			graphicWidth.setValue(style.getPoint().getGraphicWidth());
			graphicHeight.setValue(style.getPoint().getGraphicHeight());
		}
	}

	@Override
	protected void addKeyShortcut(TextButton button, int keyCode) {
		KeyShortcutHandler keyShortcut = new KeyShortcutHandler(button, keyCode);

		vertexStyle.addKeyDownHandler(keyShortcut);
	}
}
