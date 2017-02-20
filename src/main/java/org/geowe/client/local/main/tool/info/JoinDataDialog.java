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

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.data.shared.LabelProvider;
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

	private PlainTabPanel tabPanel;
	private FormPanel uploadPanel;
	
	private TextField urlTextField;
	private SimpleComboBox<String> attributeCombo;
	private TextButton loadFileButton;
	private FileUploadField file;
	private HorizontalPanel comboPanel;

	public JoinDataDialog() {
		super();
		setHeadingText(UIMessages.INSTANCE.joinDialogHeadingText());
		setSize("420px", "280px");
		setResizable(true);
		setHideOnButtonClick(false);
		setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CLOSE);
		setBodyStyleName("pad-text");
		loadFileButton = new TextButton(UIMessages.INSTANCE.loadFile());

		initialize();
	}

	public void init() {
		attributeCombo.clear();
		comboPanel.setVisible(false);
	}
	
	@PostConstruct
	private void initialize() {
		createFilePanel();
		createAttributeComboBox();
		getComboPanel();
		add(createPanel());
	}
	
	private Widget createPanel() {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(1);
		vPanel.add(createTabPanel());
		vPanel.add(loadFileButton);
		vPanel.add(comboPanel);
		return vPanel;
	}
	
	private PlainTabPanel createTabPanel() {
		tabPanel = new PlainTabPanel();
		
		tabPanel.setPixelSize(400, 80);
		tabPanel.add(uploadPanel, UIMessages.INSTANCE.file());
		tabPanel.add(getURLPanel(), UIMessages.INSTANCE.url());		
						
		return tabPanel;
	}

	private FormPanel createFilePanel() {
		VerticalLayoutContainer layoutContainer = new VerticalLayoutContainer();

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
		vPanel.setWidth("400px");

		vPanel.add(new Label(UIMessages.INSTANCE.messageURLPanel()));

		urlTextField = new TextField();
		urlTextField.setBorders(true);
		urlTextField.setEmptyText("http://");
		urlTextField.setWidth(390);
		urlTextField.setAllowBlank(false);
		vPanel.add(urlTextField);

		return vPanel;
	}

	private void createAttributeComboBox() {
		attributeCombo = new SimpleComboBox<String>(
				new LabelProvider<String>() {
					@Override
					public String getLabel(String item) {
						return item;
					}
				});
		attributeCombo.setEnabled(false);

		attributeCombo.setTypeAhead(true);
		attributeCombo.setEmptyText(UIMessages.INSTANCE
				.asdAttributeComboEmptyText());
		attributeCombo.setTriggerAction(TriggerAction.ALL);
		attributeCombo.setForceSelection(true);
		attributeCombo.setEditable(false);
		attributeCombo.enableEvents();
	}

	private void getComboPanel() {
		comboPanel = new HorizontalPanel();
		comboPanel.setWidth("380px");
		comboPanel.addStyleName(ThemeStyles.get().style().borderBottom());
		comboPanel.addStyleName(ThemeStyles.get().style().borderTop());
		comboPanel.setSpacing(5);
		comboPanel.setVisible(false);
		comboPanel.add(new Label(UIMessages.INSTANCE.bindableAttribute()));
		comboPanel.add(attributeCombo);
	}


	public SimpleComboBox<String> getAttributeCombo() {
		return attributeCombo;
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

	public void showComboPanel() {
		comboPanel.setVisible(true);
	}
}
