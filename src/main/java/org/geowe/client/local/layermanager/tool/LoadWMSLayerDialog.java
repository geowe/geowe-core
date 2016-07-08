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
package org.geowe.client.local.layermanager.tool;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.KeyShortcutHandler;
import org.geowe.client.local.ui.MessageDialogBuilder;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Represents a dialog to load a layer on the map from a Wem Map Service (WMS)
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class LoadWMSLayerDialog extends Dialog {

	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	private TextField urlField;
	private TextField wmsNameField;
	private TextField formatoField;


	public LoadWMSLayerDialog() {
		super();
		this.setHeadingText(UIMessages.INSTANCE.lWMSdTitle());
		this.getHeader().setIcon(ImageProvider.INSTANCE.layerIcon());
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.setPixelSize(350, 250);
		this.setModal(true);
		this.setHideOnButtonClick(true);
		add(createPanel());
		addKeyShortcuts();
	}

	private Widget createPanel() {
		VerticalPanel panel = new VerticalPanel();

		String fieldWidth = "300px";

		panel.setSpacing(10);

		urlField = new TextField();
		urlField.setTitle(UIMessages.INSTANCE.lWMSdUrlField());
		urlField.setWidth(fieldWidth);
		urlField.setAllowBlank(false);

		panel.add(urlField);

		wmsNameField = new TextField();
		wmsNameField.setTitle(UIMessages.INSTANCE.lWMSdLayerNameField());
		wmsNameField.setAllowBlank(false);

		wmsNameField.setWidth(fieldWidth);
		panel.add(wmsNameField);

		formatoField = new TextField();
		formatoField.setTitle(UIMessages.INSTANCE.lWMSdImageFormatField());
		formatoField.setAllowBlank(false);

		formatoField.setWidth(fieldWidth);
		panel.add(formatoField);

		initializeFields();
		createGetcapabilitiesbutton();

		return panel;
	}

	private void createGetcapabilitiesbutton() {
		ToolButton tButton = new ToolButton(ToolButton.SEARCH);
		tButton.setToolTip(UIMessages.INSTANCE.showCapabilitiesText());

		addCapabilitiesSelectHandler(tButton);

		getHeader().addTool(tButton);
	}

	private void addCapabilitiesSelectHandler(final ToolButton tButton) {
		tButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (urlField.isValid()) {
					String capabilitiesSufix = "?SERVICE=WMS&version=1.1.1&request=GetCapabilities";
					String url = getUrl() + capabilitiesSufix;

					Window.open(url, "_blank", null);
				}
			}
		});
	}

	private void initializeFields() {
		urlField.setEmptyText("http://...");
		wmsNameField.setEmptyText(UIMessages.INSTANCE.lWMSdLayerNameField());
		formatoField.setEmptyText("image/png, image/jpg...");

		urlField.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (!event.getValue().startsWith("http")) {
					showAlert("URL");
				}
			}
		});
	}

	public String getUrl() {
		return urlField.getText();
	}

	public void setUrl(String url) {
		this.urlField.setText(url);
	}

	public String getWmsLayerName() {
		return wmsNameField.getText();
	}

	public void setWmsLayerName(String wmsLayerName) {
		this.wmsNameField.setText(wmsLayerName);
	}

	public String getFormat() {
		return formatoField.getText();
	}

	public void setFormat(String format) {
		this.formatoField.setText(format);
	}

	public boolean isCorrectFilled() {
		boolean isCorrect = true;
		StringBuffer error = new StringBuffer("");
		if (!isUrlFieldCorrect()) {
			error.append(" URL, ");
			isCorrect = false;
		}
		if (wmsNameField.getText() == null || wmsNameField.getText().isEmpty()) {
			error.append(" Layer Name, ");
			isCorrect = false;
		}
		if (formatoField.getText() == null || formatoField.getText().isEmpty()) {
			error.append(" Image format. ");
			isCorrect = false;
		}

		if (!"".equals(error.toString())) {
			showAlert(error.toString());
		}
		return isCorrect;
	}

	private boolean isUrlFieldCorrect() {
		return (urlField.getText() != null && !urlField.getText().isEmpty());
	}

	private void addKeyShortcuts() {
		KeyShortcutHandler keyShortcut = new KeyShortcutHandler(
				getButton(PredefinedButton.OK), KeyCodes.KEY_ENTER);

		urlField.addKeyDownHandler(keyShortcut);
		wmsNameField.addKeyDownHandler(keyShortcut);
		formatoField.addKeyDownHandler(keyShortcut);
	}

	private void showAlert(String error) {
		messageDialogBuilder.createError(
				UIMessages.INSTANCE.lWMSdAlertMessageBoxTitle(),
				UIMessages.INSTANCE.lWMSdAlertMessageBoxLabel(error)).show();
	}

	public void initialize() {
		initializeFields();
	}
}
