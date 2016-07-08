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
package org.geowe.client.local.main;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.KeyShortcutHandler;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Represents a dialog to load a layer on the map from a Wem Map Service (WMS)
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class CoordinateGeolocationDialog extends Dialog {

	private static final String FIELD_WIDTH = "180px";
	private TextField latitudTextField;
	private TextField longitudTextField;

	public CoordinateGeolocationDialog() {
		super();
		this.setHeadingText(UIMessages.INSTANCE.coordWGS84());
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.setPixelSize(280, 150);
		this.setModal(true);
		this.setHideOnButtonClick(true);
		add(createPanel());
		addKeyShortcuts();
	}

	private Widget createPanel() {
		final VerticalLayoutContainer panel = new VerticalLayoutContainer();
		
		latitudTextField = new TextField();
		latitudTextField.setTitle(UIMessages.INSTANCE.latitude());
		latitudTextField.setWidth(FIELD_WIDTH);
		latitudTextField.setAllowBlank(false);

		final FieldLabel latitudLabel = new FieldLabel(latitudTextField,
				UIMessages.INSTANCE.latitude());
		panel.add(latitudLabel, new VerticalLayoutData(1, -1));

		longitudTextField = new TextField();
		longitudTextField.setTitle(UIMessages.INSTANCE.longitude());
		longitudTextField.setAllowBlank(false);

		longitudTextField.setWidth(FIELD_WIDTH);

		final FieldLabel longitudLabel = new FieldLabel(longitudTextField,
				UIMessages.INSTANCE.longitude());
		panel.add(longitudLabel, new VerticalLayoutData(1, -1));
		initializeFields();

		return panel;
	}

	private void initializeFields() {
		latitudTextField.setEmptyText(UIMessages.INSTANCE.latitude()
				+ ": WGS84");
		longitudTextField.setEmptyText(UIMessages.INSTANCE.longitude()
				+ ": WGS84");
	}

	public String getLatitud() {
		return latitudTextField.getText();
	}

	public void setLatitud(final String latitud) {
		this.latitudTextField.setText(latitud);
	}

	public String getLongitud() {
		return longitudTextField.getText();
	}

	public void setLongitud(final String longitud) {
		this.longitudTextField.setText(longitud);
	}

	public boolean isCorrectFilled() {
		boolean isCorrect = true;

		if (latitudTextField.getText() == null
				|| latitudTextField.getText().isEmpty()) {
			isCorrect = false;
		} else if (longitudTextField.getText() == null
				|| longitudTextField.getText().isEmpty()) {
			isCorrect = false;
		}

		return isCorrect;
	}

	private void addKeyShortcuts() {
		final KeyShortcutHandler keyShortcut = new KeyShortcutHandler(
				getButton(PredefinedButton.OK), KeyCodes.KEY_ENTER);

		latitudTextField.addKeyDownHandler(keyShortcut);
		longitudTextField.addKeyDownHandler(keyShortcut);
	}
}
