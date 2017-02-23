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
package org.geowe.client.local.main.tool.info;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.FeatureAttributeComboBox;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.Style.Direction;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Join Data to layer dialog.
 * 
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class JoinDataDialog extends Dialog {

	private static final String DEFAULT_CSV_SEPARATOR = ",";
	private PlainTabPanel tabPanel;
	private FormPanel uploadPanel;

	private TextField urlTextField;
	private SimpleComboBox<String> csvAttributeCombo;
	private TextButton loadFileButton;
	private FileUploadField file;
	private HorizontalPanel comboPanel;
	private HorizontalPanel separatorPanel;
	private TextField separatorTextField;
	private FeatureAttributeComboBox layerAttributeCombo;
	private HorizontalPanel layerAttributeComboPanel;

	public JoinDataDialog() {
		super();
		setHeadingText(UIMessages.INSTANCE.joinDialogHeadingText());
		setSize("420px", "340px");
		setModal(true);
		setClosable(true);
		setHideOnButtonClick(false);
		setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CLOSE);
		setBodyStyleName("pad-text");

		initialize();
	}

	public void init() {
		csvAttributeCombo.clear();
		layerAttributeCombo.clear();
		file.clear();
		separatorTextField.setText(DEFAULT_CSV_SEPARATOR);
		hidePanels();
	}

	@PostConstruct
	private void initialize() {
		createFilePanel();
		createCSVAttributeComboBox();
		getCSVComboPanel();
		createSeparatorPanel();
		createLoadFileButton();
		createAttributeCombo();
		createLayerAttributeComboPanel();
		add(createPanel());
	}

	private Widget createPanel() {
		final VerticalPanel vPanel = new VerticalPanel();
		vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_JUSTIFY);

		vPanel.add(createTabPanel());
		vPanel.add(separatorPanel);
		vPanel.add(loadFileButton);
		vPanel.add(comboPanel);
		vPanel.add(layerAttributeComboPanel);

		return vPanel;
	}

	private void createLoadFileButton() {
		loadFileButton = new TextButton(UIMessages.INSTANCE.loadFile());
		loadFileButton.setWidth("100%");
	}

	private PlainTabPanel createTabPanel() {
		tabPanel = new PlainTabPanel();
		tabPanel.setPixelSize(400, 80);
		tabPanel.add(uploadPanel, UIMessages.INSTANCE.file());
		tabPanel.add(getURLPanel(), UIMessages.INSTANCE.url());

		return tabPanel;
	}

	private FormPanel createFilePanel() {
		final VerticalLayoutContainer layoutContainer = new VerticalLayoutContainer();

		file = new FileUploadField();
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

	private VerticalPanel getURLPanel() {
		final VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("100%");

		vPanel.add(new Label(UIMessages.INSTANCE.messageURLPanel()));

		urlTextField = new TextField();
		urlTextField.setBorders(true);
		urlTextField.setEmptyText("http://");
		urlTextField.setWidth(390);
		urlTextField.setAllowBlank(false);
		vPanel.add(urlTextField);

		return vPanel;
	}

	private void createCSVAttributeComboBox() {
		csvAttributeCombo = new SimpleComboBox<String>(
				new LabelProvider<String>() {
					@Override
					public String getLabel(String item) {
						return item;
					}
				});
		csvAttributeCombo.setEnabled(false);
		csvAttributeCombo.setWidth("200px");
		csvAttributeCombo.setTypeAhead(true);
		csvAttributeCombo.setEmptyText(UIMessages.INSTANCE
				.asdAttributeComboEmptyText());
		csvAttributeCombo.setTriggerAction(TriggerAction.ALL);
		csvAttributeCombo.setForceSelection(true);
		csvAttributeCombo.setEditable(false);
		csvAttributeCombo.enableEvents();
	}

	private void getCSVComboPanel() {
		comboPanel = new HorizontalPanel();
		comboPanel.setWidth("100%");
		comboPanel.addStyleName(ThemeStyles.get().style().borderTop());
		comboPanel.setSpacing(5);
		comboPanel.setVisible(false);
		comboPanel.add(new Label(UIMessages.INSTANCE.bindableAttribute()));
		comboPanel.add(csvAttributeCombo);
	}

	private void createSeparatorPanel() {
		separatorPanel = new HorizontalPanel();
		separatorPanel.setSpacing(1);
		separatorPanel.setWidth("100%");
		separatorPanel.addStyleName(ThemeStyles.get().style().borderTop());
		separatorPanel.addStyleName(ThemeStyles.get().style().borderBottom());
		separatorPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		separatorPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		separatorPanel.add(new Label(UIMessages.INSTANCE
				.separator(DEFAULT_CSV_SEPARATOR)));
		separatorTextField = new TextField();
		separatorTextField.setText(DEFAULT_CSV_SEPARATOR);
		separatorTextField.setWidth(30);

		separatorPanel.add(separatorTextField);
	}

	private void createLayerAttributeComboPanel() {
		layerAttributeComboPanel = new HorizontalPanel();
		layerAttributeComboPanel.setWidth("100%");
		layerAttributeComboPanel.addStyleName(ThemeStyles.get().style()
				.borderBottom());
		layerAttributeComboPanel.setSpacing(5);
		layerAttributeComboPanel.setVisible(false);
		layerAttributeComboPanel.add(new Label(UIMessages.INSTANCE
				.layerSchemaToolText()));
		layerAttributeComboPanel.add(layerAttributeCombo);

	}

	private void createAttributeCombo() {
		layerAttributeCombo = new FeatureAttributeComboBox("200px");
	}

	public SimpleComboBox<String> getCsvAttributeCombo() {
		return csvAttributeCombo;
	}

	public FormPanel getUploadFormPanel() {
		return uploadPanel;
	}

	public TextButton getLoadFileButton() {
		return loadFileButton;
	}

	public String getActiveTab() {
		return tabPanel.getConfig(tabPanel.getActiveWidget()).getText();
	}

	public String getUrl() {
		return this.urlTextField.getText();
	}

	public boolean isFileFieldCorrectFilled() {
		return file.isValid();
	}

	public boolean isUrlFieldCorrectFilled() {
		return urlTextField.isValid();
	}

	public void showPanels() {
		comboPanel.setVisible(true);
		comboPanel.asWidget().getElement().<FxElement> cast()
				.slideIn(Direction.UP);
		layerAttributeComboPanel.setVisible(true);
		layerAttributeComboPanel.asWidget().getElement().<FxElement> cast()
				.slideIn(Direction.UP);

	}

	public void hidePanels() {
		comboPanel.setVisible(false);
		layerAttributeComboPanel.setVisible(false);
	}

	public String getSeparator() {
		return separatorTextField.getText();
	}

	public FeatureAttributeComboBox getLayerAttributeCombo() {
		return layerAttributeCombo;
	}

}
