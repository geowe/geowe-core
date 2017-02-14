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
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.FeatureAttributeComboBox;
import org.geowe.client.local.ui.KeyShortcutHandler;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

/**
 * Pestaña de configuración de color temático o cartografia temática, 
 * perteneciente al diálogo de gestión de estilos.
 * 
 * @author Atanasio Muñoz
 *
 */
public class ColorThemingStyleTab extends StyleTab {

	private CheckBox enableTheming;
	private FeatureAttributeComboBox attributeTheming;	
	private CheckBox enableLegend;
	
	@Override
	protected void initializePanel() {
		String fieldWidth = "125px";

		attributeTheming = new FeatureAttributeComboBox(fieldWidth);
		attributeTheming.setEnabled(false);
		enableTheming = new CheckBox();
		enableTheming.setBoxLabel(UIMessages.INSTANCE.enableColorTheming());
		enableTheming.setValue(false);
		enableTheming.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				attributeTheming.setEnabled(event.getValue());
				enableLegend.setValue(event.getValue());
				enableLegend.setEnabled(event.getValue());

				if(event.getValue() && isFeatureStyleApplied(selectedLayer)) {
					showColorThemingStyleWarning();
				}
			}
		});

		enableLegend = new CheckBox();
		enableLegend.setBoxLabel(UIMessages.INSTANCE.colorThemingShowLegend());
		enableLegend.setValue(false);
		enableLegend.setEnabled(false);

		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		vlc.add(enableTheming, new VerticalLayoutData(-18, -1));
		vlc.add(new FieldLabel(attributeTheming, UIMessages.INSTANCE
				.vlswLabelAttribute()), new VerticalLayoutData(-18, -1));
		vlc.add(enableLegend, new VerticalLayoutData(-18, -1));

		panel.setSpacing(5);
		panel.add(vlc);
	}
	
	public Boolean isEnableTheming() {
		return enableTheming.getValue();
	}
	
	public Boolean isEnableLegend() {
		return enableLegend.getValue();
	}

	public String getAttributeTheming() {
		return attributeTheming.getValue() != null ?
				attributeTheming.getValue().toString() : null;
	}
	
	@Override
	protected void updateStyleData(VectorStyleDef style) {		
		attributeTheming.loadValues(selectedLayer);
							
		enableTheming.setValue(style.isColorThemingEnabled(), true);
		if(style.isColorThemingEnabled()) {
			attributeTheming.setValue(style.getColorThemingAttribute());
			enableLegend.setValue(style.isEnableLegend(), true);
		} else {
			attributeTheming.setValue(null);
			attributeTheming.setEmptyText(UIMessages.INSTANCE
					.sbLayerComboEmptyText());
			enableLegend.setValue(false, true);
		}	
	}

	@Override
	protected void addKeyShortcut(TextButton button, int keyCode) {
		KeyShortcutHandler keyShortcut = new KeyShortcutHandler(button, keyCode);
		attributeTheming.addKeyDownHandler(keyShortcut);		
	}
	
	private boolean isFeatureStyleApplied(final VectorLayer selectedLayer) {		
		for(VectorFeature feature : selectedLayer.getFeatures()) {
			if(feature.getStyle() != null) {
				return true;
			}
		}
		
		return false;
	}
	
	private void showColorThemingStyleWarning() {
		final MessageBox messageBox = new MessageBox(
				UIMessages.INSTANCE.vlswWarningDialogTitle(), 
				UIMessages.INSTANCE.vlswClearFeatureStyleText());
		messageBox.setIcon(MessageBox.ICONS.info());
		
		messageBox.show();
	}
}
