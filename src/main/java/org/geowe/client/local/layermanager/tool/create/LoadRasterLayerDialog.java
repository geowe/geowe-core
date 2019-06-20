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
 * Represents a dialog to load a layer on the map from a Web Map Service (WMS)
 * and Web Map Tile Service
 * 
 * @author jose@geowe.org
 * @since 19/10/2016
 * @author rafa@geowe.org fix issue 215
 * @author rafa@geowe.org
 * @since 20/06/2019
 * incluido estilo en capas WMTS
 */
@ApplicationScoped
public class LoadRasterLayerDialog extends Dialog {
	private static final String FIELD_WIDTH = "300px";
	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	private TextField urlWMSField;
	private TextField nameWMSField;
	private TextField formatWMSField;
	private TextField urlTMSField;
	private TextField nameTMSField;
	private TextField formatTMSField;
	private TextField urlWMTSField;
	private TextField nameWMTSField;
	private TextField formatWMTSField;
	private TextField tileMatrixSetField;
	private TextField styleField;

	private PlainTabPanel tabPanel;

	public String getUrlTMS() {
		return urlTMSField.getText();
	}

	public void setUrlTMS(String urlTMS) {
		this.urlTMSField.setText(urlTMS);
	}

	public String getNameTMS() {
		return nameTMSField.getText();
	}

	public void setNameTMS(String nameTMS) {
		this.nameTMSField.setText(nameTMS);
	}

	public String getFormatTMS() {
		return formatTMSField.getText();
	}

	public void setFormatTMS(String formatTMS) {
		this.formatTMSField.setText(formatTMS);
	}

	public LoadRasterLayerDialog() {
		super();
		this.setHeadingText(UIMessages.INSTANCE.lrasterdTitle());
		this.getHeader().setIcon(ImageProvider.INSTANCE.layerIcon());
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.setPixelSize(350, 350);
		this.setModal(true);
		this.setHideOnButtonClick(false);
		add(createPanel());

	}

	private Widget createPanel() {
		final VerticalPanel panel = new VerticalPanel();

		panel.setSpacing(10);
		panel.add(createTabPanel());
		createGetcapabilitiesbutton();
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
				final String capabilitiesSufix = "?SERVICE=" + getActiveTab()
						+ "&version=1.1.1&request=GetCapabilities";
				String url = "";

				if ("WMS".equals(getActiveTab()) && urlWMSField.isValid()) {
					url = getUrlWMS() + capabilitiesSufix;
				} else if ("WMTS".equals(getActiveTab())
						&& urlWMTSField.isValid()) {
					url = getUrlWMTS() + capabilitiesSufix;
				}

				if (!url.isEmpty()) {
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
		tabPanel.add(getTMSPanel(), "TMS");

		return tabPanel;
	}

	private VerticalPanel getTMSPanel() {
		final VerticalPanel panel = new VerticalPanel();
		panel.setWidth("350px");
		panel.setSpacing(10);

		urlTMSField = new TextField();
		urlTMSField.setTitle(UIMessages.INSTANCE.lrasterdUrlField());
		urlTMSField.setWidth(FIELD_WIDTH);
		urlTMSField.setAllowBlank(false);

		panel.add(urlTMSField);

		nameTMSField = new TextField();
		nameTMSField.setTitle(UIMessages.INSTANCE.lrasterdLayerNameField(""));
		nameTMSField.setAllowBlank(false);
		nameTMSField.setWidth(FIELD_WIDTH);
		panel.add(nameTMSField);

		formatTMSField = new TextField();
		formatTMSField.setTitle(UIMessages.INSTANCE.lrasterdImageFormatField());
		formatTMSField.setAllowBlank(false);
		formatTMSField.setWidth(FIELD_WIDTH);
		panel.add(formatTMSField);

		return panel;
	}

	private VerticalPanel getWMSPanel() {
		final VerticalPanel panel = new VerticalPanel();
		panel.setWidth("350px");
		panel.setSpacing(10);

		urlWMSField = new TextField();
		urlWMSField.setTitle(UIMessages.INSTANCE.lrasterdUrlField());
		urlWMSField.setWidth(FIELD_WIDTH);
		urlWMSField.setAllowBlank(false);

		panel.add(urlWMSField);

		nameWMSField = new TextField();
		nameWMSField
				.setTitle(UIMessages.INSTANCE.lrasterdLayerNameField("WMS"));
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
		panel.setSpacing(10);

		urlWMTSField = new TextField();
		urlWMTSField.setTitle(UIMessages.INSTANCE.lrasterdUrlField());
		urlWMTSField.setWidth(FIELD_WIDTH);
		urlWMTSField.setAllowBlank(false);

		panel.add(urlWMTSField);

		nameWMTSField = new TextField();
		nameWMTSField.setTitle(UIMessages.INSTANCE
				.lrasterdLayerNameField("WMTS"));
		nameWMTSField.setAllowBlank(false);
		nameWMTSField.setWidth(FIELD_WIDTH);
		panel.add(nameWMTSField);

		formatWMTSField = new TextField();
		formatWMTSField
				.setTitle(UIMessages.INSTANCE.lrasterdImageFormatField());
		formatWMTSField.setAllowBlank(false);
		formatWMTSField.setWidth(FIELD_WIDTH);
		panel.add(formatWMTSField);

		tileMatrixSetField = new TextField();
		tileMatrixSetField.setTitle(UIMessages.INSTANCE
				.lrasterdMatrixSetField());
		tileMatrixSetField.setAllowBlank(false);
		tileMatrixSetField.setWidth(FIELD_WIDTH);
		panel.add(tileMatrixSetField);

		styleField = new TextField();
		styleField.setTitle(UIMessages.INSTANCE
				.vlswHeading());
		styleField.setAllowBlank(true);
		styleField.setWidth(FIELD_WIDTH);
		panel.add(styleField);


		return panel;
	}

	public String getUrlWMS() {
		return urlWMSField.getText();
	}

	public void setUrlWMS(String url) {
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

	public String getStyle() {
		return styleField.getText();
	}

	public void setStyleField(String styleFieldText) {
		this.styleField.setText(styleFieldText);
	}

	public boolean isCorrectFilledTMS() {
		boolean isCorrect = true;
		StringBuffer error = new StringBuffer("");
		if (!isUrlTMSFieldCorrect()) {
			error.append(" URL, ");
			isCorrect = false;
		}
		if (nameTMSField.getText() == null || nameTMSField.getText().isEmpty()) {
			error.append(" Layer Name, ");
			isCorrect = false;
		}
		if (formatTMSField.getText() == null
				|| formatTMSField.getText().isEmpty()) {
			error.append(" Image format. ");
			isCorrect = false;
		}

		if (!"".equals(error.toString())) {
			showAlert(error.toString());
		}
		return isCorrect;
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
		if (formatWMSField.getText() == null
				|| formatWMSField.getText().isEmpty()) {
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
		if (nameWMTSField.getText() == null
				|| nameWMTSField.getText().isEmpty()) {
			error.append(" Layer Name, ");
			isCorrect = false;
		}
		if (formatWMTSField.getText() == null
				|| formatWMTSField.getText().isEmpty()) {
			error.append(" Image format. ");
			isCorrect = false;
		}

		if (tileMatrixSetField.getText() == null
				|| tileMatrixSetField.getText().isEmpty()) {
			error.append(" Tile Matrix Set. ");
			isCorrect = false;
		}

		if (!"".equals(error.toString())) {
			showAlert(error.toString());
		}
		return isCorrect;
	}

	private boolean isUrlWMTSFieldCorrect() {
		return (urlWMTSField.getText() != null && !urlWMTSField.getText()
				.isEmpty());
	}

	private boolean isUrlWMSFieldCorrect() {
		return (urlWMSField.getText() != null && !urlWMSField.getText()
				.isEmpty());
	}

	private boolean isUrlTMSFieldCorrect() {
		return (urlTMSField.getText() != null && !urlTMSField.getText()
				.isEmpty());
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
		nameWMSField.setEmptyText(UIMessages.INSTANCE
				.lrasterdLayerNameField("WMS"));
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

	public void initializeTMSFields() {
		urlTMSField.setEmptyText("http://...");
		nameTMSField.setEmptyText(UIMessages.INSTANCE
				.lrasterdLayerNameField(" "));
		formatTMSField.setEmptyText("png, jpg...");

		urlTMSField.addValueChangeHandler(new ValueChangeHandler<String>() {
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
		nameWMTSField.setEmptyText(UIMessages.INSTANCE
				.lrasterdLayerNameField("WMTS"));
		formatWMTSField.setEmptyText("image/png, image/jpg...");
		tileMatrixSetField.setEmptyText("matrix set identifier");
		styleField.setEmptyText(UIMessages.INSTANCE.vlswHeading());
		
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
