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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.tool.export.VectorFormat;
import org.geowe.client.local.main.AnchorBuilder;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.KeyShortcutHandler;
import org.geowe.client.local.ui.ProjectionComboBox;
import org.geowe.client.local.ui.VectorFormatComboBox;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Represents the dialog to import layers
 * 
 * @author geowe
 * @since 18/10/2016
 * @author rafa@geowe.org WFS fix
 *
 */
@ApplicationScoped
public class GeoDataImportDialog extends Dialog {
	@Inject
	private Logger logger;
	@Inject
	WfsImportTab wfsImportTab;

	private TextField urlTextField;
	private TextArea geoDataTextArea;
	private TextField layerName;
	private PlainTabPanel tabPanel;
	private ProjectionComboBox projectionName;
	private FormPanel uploadPanel;
	private FileUploadField file;
	private FieldLabel dataFormatField;
	private VectorFormatComboBox vectorFormatCombo;
	private Anchor urlToShareAnchor;
	private TextField urlShared;
	private Label urlLabel;
	private HorizontalPanel urlPanel;
	private CreateEmptyLayerTab createEmptyLayerTab;

	public GeoDataImportDialog() {
		this.setHeadingText(UIMessages.INSTANCE.geoDataImportDialogTitle());
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.setPixelSize(550, 350);
		this.setModal(true);
		this.setResizable(false);
	}

	@PostConstruct
	private void createLayouts() {
		this.add(createLayout());
		addKeyShortcuts();
	}

	public void initialize(String title, String layerName, String epsg) {
		this.setHeadingText(title);
		this.layerName.setText(layerName);
		this.projectionName.setValue(epsg);
		this.vectorFormatCombo.setValue(null);
		this.urlTextField.clear();
		this.geoDataTextArea.clear();
		this.file.clear();
	}

	public String getUrl() {
		return this.urlTextField.getText();
	}

	public String getGeoData() {
		return this.geoDataTextArea.getText();
	}

	public String getLayerName() {
		return this.layerName.getText();
	}

	public void setLayerName(String layerName) {
		this.layerName.setText(layerName);
	}

	public String getProjectionName() {
		return this.projectionName.getValue();
	}

	public String getDataFormat() {
		if (this.vectorFormatCombo.getValue() == null) {
			return "";
		} else {
			return this.vectorFormatCombo.getValue().getName();
		}
	}

	public FormPanel getUploadPanel() {
		return uploadPanel;
	}

	public String getWfsUrl() {
		return wfsImportTab.getWfsUrl();
	}

	public String getWfsNamespaceTypeName() {
		return wfsImportTab.getWfsTypeName();
	}

	public int getWfsMaxFeatures() {
		return (wfsImportTab.getWfsMaxFeaturesType() != null && !wfsImportTab
				.getWfsMaxFeaturesType().isEmpty()) ? Integer
				.parseInt(wfsImportTab.getWfsMaxFeaturesType()) : 0;
	}

	public String getWfsCqlfilter() {
		return wfsImportTab.getCql();
	}

	public String getGeomColumn() {
		return wfsImportTab.getCql();
	}

	public String getWfsVersion() {
		return wfsImportTab.getWfsVersion();
	}

	public boolean isBboxEnabled() {
		return wfsImportTab.isBboxEnabled();
	}

	private HorizontalLayoutContainer createLayout() {
		HorizontalLayoutContainer mainContainer = new HorizontalLayoutContainer();
		String fieldWidth = "225px";

		// ----- Left Panel ----
		VerticalPanel layerDataContainer = new VerticalPanel();
		layerDataContainer.setWidth("250px");
		layerDataContainer.setSpacing(5);

		layerName = new TextField();
		layerName.setWidth(fieldWidth);
		layerName.setAllowBlank(false);
		layerDataContainer.add(new Label(UIMessages.INSTANCE
				.gdidLayerNameLabel()));
		layerDataContainer.add(layerName);

		projectionName = new ProjectionComboBox(fieldWidth);
		layerDataContainer.add(new Label(UIMessages.INSTANCE
				.gdidProjectionLabel()));
		layerDataContainer.add(projectionName);

		Label padding = new Label("");
		padding.setHeight("75px");
		layerDataContainer.add(padding);

		vectorFormatCombo = new VectorFormatComboBox("120px",
				VectorFormat.getAllVectorFormat());
		dataFormatField = new FieldLabel(vectorFormatCombo,
				UIMessages.INSTANCE.gdidDataFormatLabel());

		layerDataContainer.add(dataFormatField);
		dataFormatField.setVisible(false);

		// ----- Right Panel ----

		mainContainer.add(layerDataContainer);
		mainContainer.add(createTabPanel());

		return mainContainer;
	}

	private PlainTabPanel createTabPanel() {
		tabPanel = new PlainTabPanel();
		tabPanel.setPixelSize(300, 250);
		tabPanel.getElement().setId("tabPanel");
		tabPanel.add(getEmptyPanel(), UIMessages.INSTANCE.empty());
		tabPanel.add(getURLPanel(), UIMessages.INSTANCE.url());
		tabPanel.add(getTextPanel(), UIMessages.INSTANCE.text());
		tabPanel.add(getFilePanel(), UIMessages.INSTANCE.file());
		tabPanel.add(wfsImportTab, UIMessages.INSTANCE.wfs());

		tabPanel.addSelectionHandler(getTabPanelSelectionHandler());
		return tabPanel;
	}

	private SelectionHandler<Widget> getTabPanelSelectionHandler() {
		return new SelectionHandler<Widget>() {

			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				if (UIMessages.INSTANCE.empty().equals(getActiveTab())) {
					dataFormatField.setVisible(false);
				} else {
					dataFormatField.setVisible(true);
				}
				if (UIMessages.INSTANCE.wfs().equals(getActiveTab())) {
					vectorFormatCombo.setValue(VectorFormat.GML_FORMAT);
				} else {
					vectorFormatCombo.setValue(null);
				}
			}
		};
	}

	private VerticalPanel getEmptyPanel() {
		this.createEmptyLayerTab = new CreateEmptyLayerTab();
		return createEmptyLayerTab.getEmptyPanel();
	}

	public boolean getCreateAttributes() {
		return createEmptyLayerTab.getCreateAttributes();
	}

	private VerticalPanel getURLPanel() {
		final VerticalPanel geoDataContainer = new VerticalPanel();
		geoDataContainer.setWidth("280px");
		geoDataContainer.setSpacing(3);

		geoDataContainer.add(new Label(UIMessages.INSTANCE.messageURLPanel()));

		urlTextField = new TextField();
		urlTextField.setBorders(true);
		urlTextField.setEmptyText("http://");
		urlTextField.setWidth(270);
		geoDataContainer.add(urlTextField);

		TextButton createUrlButton = new TextButton(
				UIMessages.INSTANCE.urlToShareButtonText());
		createUrlButton.addSelectHandler(createUrlToShare(geoDataContainer));

		geoDataContainer.add(createUrlButton);
		geoDataContainer.add(createUrlToShareAnchor());

		urlShared = new TextField();
		urlShared.setBorders(true);
		urlShared.setWidth(270);
		urlShared.setVisible(false);
		geoDataContainer.add(urlShared);

		urlTextField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				setAutoFormat(event.getValue());
			}

		});

		urlTextField.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				setAutoFormat(urlTextField.getText());
			}

		});

		urlTextField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				setAutoFormat(urlTextField.getText());
			}

		});

		return geoDataContainer;
	}

	private void setAutoFormat(String url) {
		vectorFormatCombo.setValue(null);

		if (!url.isEmpty()) {
			String extension = url.substring(url.lastIndexOf(".") + 1);
			VectorFormat vectorFormat = VectorFormat.getFromName(extension);
			vectorFormatCombo.setValue(vectorFormat);
		}
	}

	private Widget createUrlToShareAnchor() {
		urlPanel = new HorizontalPanel();
		urlPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		urlPanel.setSpacing(1);
		urlToShareAnchor = new AnchorBuilder().build();
		urlPanel.add(urlToShareAnchor);
		Image img = new Image(ImageProvider.INSTANCE.externalLink16());
		urlPanel.add(img);
		urlPanel.setVisible(false);
		return urlPanel;
	}

	private SelectHandler createUrlToShare(final VerticalPanel geoDataContainer) {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				urlToShareAnchor.setHref(getHref());
				urlToShareAnchor.setText(
						UIMessages.INSTANCE.seeOtherWindow(getLayerName()),
						Direction.LTR);

				urlShared.setText(getHref());
				urlPanel.setVisible(true);
				urlShared.setVisible(true);
			}

			private String getHref() {
				String baseUrl = GWT.getHostPageBaseURL();

				baseUrl += "?layerUrl="
						+ URL.encodeQueryString(urlTextField.getValue())
						+ "&layerName=" + getLayerName() + "&layerProj="
						+ getProjectionName() + "&layerFormat="
						+ getDataFormat();

				return baseUrl;
			}
		};
	}

	private VerticalPanel getTextPanel() {
		VerticalPanel geoDataContainer = new VerticalPanel();
		geoDataContainer.setWidth("230px");
		geoDataContainer.setSpacing(5);

		geoDataTextArea = new TextArea();
		geoDataTextArea.setBorders(true);
		geoDataTextArea.setEmptyText(UIMessages.INSTANCE
				.gdidTextAreaEmptyText());
		geoDataTextArea.setWidth(270);
		geoDataTextArea.setHeight(180);
		geoDataContainer.add(new Label(UIMessages.INSTANCE
				.gdidTextAreaTitleLabel()));

		geoDataContainer.add(geoDataTextArea);
		return geoDataContainer;
	}

	private void addKeyShortcuts() {
		KeyShortcutHandler keyShortcut = new KeyShortcutHandler(
				getButton(PredefinedButton.OK), KeyCodes.KEY_ENTER);

		layerName.addKeyDownHandler(keyShortcut);
		projectionName.addKeyDownHandler(keyShortcut);
		vectorFormatCombo.addKeyDownHandler(keyShortcut);
		geoDataTextArea.addKeyDownHandler(keyShortcut);
	}

	private FormPanel getFilePanel() {
		VerticalLayoutContainer layoutContainer = new VerticalLayoutContainer();

		file = new FileUploadField();

		file.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				setAutoFormat(file.getValue());
				String name = file.getValue().substring(0, file.getValue().lastIndexOf("."));
				name = name.substring(file.getValue().lastIndexOf("\\") +1);
				layerName.setText(name);
			}
		});

		file.setName(UIMessages.INSTANCE.gdidFileUploadFieldText());
		file.setAllowBlank(false);

		layoutContainer.add(new FieldLabel(file, UIMessages.INSTANCE.file()),
				new VerticalLayoutData(-18, -1));
		layoutContainer.add(new Label(UIMessages.INSTANCE.maxFileSizeText()),
				new VerticalLayoutData(-18, -1));

		uploadPanel = new FormPanel();
		uploadPanel.setMethod(Method.POST);
		uploadPanel.setEncoding(Encoding.MULTIPART);
		uploadPanel.setAction("fileupload.do");
		uploadPanel.setWidget(layoutContainer);

		return uploadPanel;
	}

	public FileUploadField getFileUploadField() {
		return this.file;
	}

	public String getActiveTab() {
		return tabPanel.getConfig(tabPanel.getActiveWidget()).getText();
	}

	public native static void reatachIframe(FormPanel form) /*-{
		var i = form.@com.google.gwt.user.client.ui.FormPanel::synthesizedFrame;
		var o = i.onload;
		i.onload = undefined;
		var p = i.parentElement;
		p.removeChild(i);
		p.appendChild(i);
		i.onload = o;
	}-*/;
}
