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
package org.geowe.client.local.layermanager.tool.create;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Represents a dialog to load a layer on the map from a Web Map Service (WMS) and Web Map Tile Service
 * 
 * @author jose@geowe.org
 * @since 24/08/2016
 */
@ApplicationScoped
public class LoadRasterLayerDialog extends Dialog {
	private static final String FIELD_WIDTH = "300px";
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	
	private TextField urlWMSField;
	private TextField nameWMSField;
	private TextField formatWMSField;	
	private TextField urlWMTSField;	
	private TextField nameWMTSField;
	private TextField formatWMTSField;
	private TextField tileMatrixSetField;
	
	private PlainTabPanel tabPanel;
	public LoadRasterLayerDialog() {
		super();
		this.setHeadingText(UIMessages.INSTANCE.lrasterdTitle());
		this.getHeader().setIcon(ImageProvider.INSTANCE.layerIcon());
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.setPixelSize(350, 350);		
		this.setModal(true);
		this.setHideOnButtonClick(true);
		add(createPanel());
		
	}
	
	private Widget createPanel() {
		final VerticalPanel panel = new VerticalPanel();
		
		panel.setSpacing(10);
		panel.add(createTabPanel());
		createGetcapabilitiesbutton();
//		addKeyShortcuts();
		return panel;
	}
	
	private void createGetcapabilitiesbutton() {
		final ToolButton tButton = new ToolButton(ToolButton.SEARCH);
		tButton.setToolTip(UIMessages.INSTANCE.showCapabilitiesText());
		addCapabilitiesSelectHandler(tButton);
		getHeader().addTool(tButton);
	}
	
	private void addCapabilitiesSelectHandler(final ToolButton tButton) {
		tButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(final SelectEvent event) {
				if (urlWMSField.isValid()) {
					final String capabilitiesSufix = "?SERVICE=" + getActiveTab() + "&version=1.1.1&request=GetCapabilities";
					final String url = getUrl() + capabilitiesSufix;

					Window.open(url, "_blank", null);
				}
			}
		});
	}
	
	private PlainTabPanel createTabPanel() {
		tabPanel = new PlainTabPanel();
		tabPanel.setPixelSize(350, 250);
		tabPanel.getElement().setId("tabPanel");
		tabPanel.add(getWMSPanel(), "WMS");
		tabPanel.add(getWMTSPanel(), "WMTS");
		
		return tabPanel;
	}
	
	private VerticalPanel getWMSPanel() {
		final VerticalPanel panel = new VerticalPanel();
		panel.setWidth("350px");
		//panel.setHeight("20px");
		panel.setSpacing(10);

		urlWMSField = new TextField();
		urlWMSField.setTitle(UIMessages.INSTANCE.lrasterdUrlField());
		urlWMSField.setWidth(FIELD_WIDTH);
		urlWMSField.setAllowBlank(false);

		panel.add(urlWMSField);

		nameWMSField = new TextField();
		nameWMSField.setTitle(UIMessages.INSTANCE.lrasterdLayerNameField());
		nameWMSField.setAllowBlank(false);
		nameWMSField.setWidth(FIELD_WIDTH);
		panel.add(nameWMSField);

		formatWMSField = new TextField();
		formatWMSField.setTitle(UIMessages.INSTANCE.lrasterdImageFormatField());
		formatWMSField.setAllowBlank(false);
		formatWMSField.setWidth(FIELD_WIDTH);
		panel.add(formatWMSField);

		return panel;
	}
	
	private VerticalPanel getWMTSPanel() {
		final VerticalPanel panel = new VerticalPanel();
		panel.setWidth("350px");
		//panel.setHeight("250px");
		panel.setSpacing(10);

		urlWMTSField = new TextField();
		urlWMTSField.setTitle(UIMessages.INSTANCE.lrasterdUrlField());
		urlWMTSField.setWidth(FIELD_WIDTH);
		urlWMTSField.setAllowBlank(false);

		panel.add(urlWMTSField);

		nameWMTSField = new TextField();
		nameWMTSField.setTitle(UIMessages.INSTANCE.lrasterdLayerNameField());
		nameWMTSField.setAllowBlank(false);
		nameWMTSField.setWidth(FIELD_WIDTH);
		panel.add(nameWMTSField);

		formatWMTSField = new TextField();
		formatWMTSField.setTitle(UIMessages.INSTANCE.lrasterdImageFormatField());
		formatWMTSField.setAllowBlank(false);
		formatWMTSField.setWidth(FIELD_WIDTH);
		panel.add(formatWMTSField);
		
		tileMatrixSetField = new TextField();
		tileMatrixSetField.setTitle(UIMessages.INSTANCE.lrasterdMatrixSetField());
		tileMatrixSetField.setAllowBlank(false);
		tileMatrixSetField.setWidth(FIELD_WIDTH);
		panel.add(tileMatrixSetField);
		
		return panel;
	}
	
	public String getUrl() {
		return urlWMSField.getText();
	}

	public void setUrl(String url) {
		this.urlWMSField.setText(url);
	}

	public String getLayerNameWMS() {
		return nameWMSField.getText();
	}

	public void setLayerNameWMS(String wmsLayerName) {
		this.nameWMSField.setText(wmsLayerName);
	}

	public String getFormatWMS() {
		return formatWMSField.getText();
	}

	public void setFormatWMS(final String format) {
		this.formatWMSField.setText(format);
	}
	
	public String getUrlWMTS() {
		return urlWMTSField.getText();
	}

	public void setUrlWMTS(String urlWMTSField) {
		this.urlWMTSField.setText(urlWMTSField);
	}

	public String getLayerNameWMTS() {
		return nameWMTSField.getText();
	}

	public void setLayerNameWMTS(String nameWMTSField) {
		this.nameWMTSField.setText(nameWMTSField);
	}

	public String getFormatWMTS() {
		return formatWMTSField.getText();
	}

	public void setFormatWMTS(String formatWMTSField) {
		this.formatWMTSField.setText(formatWMTSField);
	}

	public String getTileMatrixSet() {
		return tileMatrixSetField.getText();
	}

	public void setTileMatrixSetField(String tileMatrixSetField) {
		this.tileMatrixSetField.setText(tileMatrixSetField);
	}

	public boolean isCorrectFilledWMS() {
		boolean isCorrect = true;
		StringBuffer error = new StringBuffer("");
		if (!isUrlWMSFieldCorrect()) {
			error.append(" URL, ");
			isCorrect = false;
		}
		if (nameWMSField.getText() == null || nameWMSField.getText().isEmpty()) {
			error.append(" Layer Name, ");
			isCorrect = false;
		}
		if (formatWMSField.getText() == null || formatWMSField.getText().isEmpty()) {
			error.append(" Image format. ");
			isCorrect = false;
		}

		if (!"".equals(error.toString())) {
			showAlert(error.toString());
		}
		return isCorrect;
	}
	
	public boolean isCorrectFilledWMTS() {
		boolean isCorrect = true;
		StringBuffer error = new StringBuffer("");
		if (!isUrlWMTSFieldCorrect()) {
			error.append(" URL, ");
			isCorrect = false;
		}
		if (nameWMTSField.getText() == null || nameWMTSField.getText().isEmpty()) {
			error.append(" Layer Name, ");
			isCorrect = false;
		}
		if (formatWMTSField.getText() == null || formatWMTSField.getText().isEmpty()) {
			error.append(" Image format. ");
			isCorrect = false;
		}
		
		if (tileMatrixSetField.getText() == null || tileMatrixSetField.getText().isEmpty()) {
			error.append(" Tile Matrix Set. ");
			isCorrect = false;
		}

		if (!"".equals(error.toString())) {
			showAlert(error.toString());
		}
		return isCorrect;
	}

	private boolean isUrlWMTSFieldCorrect() {
		return (urlWMTSField.getText() != null && !urlWMTSField.getText().isEmpty());
	}
	
	private boolean isUrlWMSFieldCorrect() {
		return (urlWMSField.getText() != null && !urlWMSField.getText().isEmpty());
	}

	private void showAlert(String error) {
		messageDialogBuilder.createError(
				UIMessages.INSTANCE.lrasterdAlertMessageBoxTitle(),
				UIMessages.INSTANCE.lrasterdAlertMessageBoxLabel(error)).show();
	}
	
	public String getActiveTab() {
		return tabPanel.getConfig(tabPanel.getActiveWidget()).getText();
	}
	
	public void initializeWMSFields() {
		urlWMSField.setEmptyText("http://...");
		nameWMSField.setEmptyText(UIMessages.INSTANCE.lrasterdLayerNameField());
		formatWMSField.setEmptyText("image/png, image/jpg...");

		urlWMSField.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (!event.getValue().startsWith("http")) {
					showAlert("URL");
				}
			}
		});
	}
	
	public void initializeWMTSFields() {
		urlWMTSField.setEmptyText("http://...");
		nameWMTSField.setEmptyText(UIMessages.INSTANCE.lrasterdLayerNameField());
		formatWMTSField.setEmptyText("image/png, image/jpg...");
		tileMatrixSetField.setEmptyText("matrix set identifier");

		urlWMTSField.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (!event.getValue().startsWith("http")) {
					showAlert("URL");
				}
			}
		});
	}
}
