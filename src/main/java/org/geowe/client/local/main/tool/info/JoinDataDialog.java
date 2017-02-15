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

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

/**
 * Join Data to layer dialog.
 * 
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class JoinDataDialog extends Dialog {

	private FormPanel uploadPanel;
	private SimpleComboBox<String> attributeCombo;
	private TextButton loadFileButton;

	public JoinDataDialog() {
		super();
		setHeadingText(UIMessages.INSTANCE.joinDialogHeadingText());
		setSize("420px", "250px");
		setResizable(true);
		setHideOnButtonClick(false);
		setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CLOSE);
		setBodyStyleName("pad-text");
		loadFileButton = new TextButton(UIMessages.INSTANCE.loadFile());

		initialize();
	}

	@PostConstruct
	private void initialize() {
		createFilePanel();
		createAttributeComboBox();
		add(createPanel());
	}

	public void init() {
		attributeCombo.clear();
		attributeCombo.setVisible(false);
	}

	private FormPanel createFilePanel() {
		VerticalLayoutContainer layoutContainer = new VerticalLayoutContainer();

		FileUploadField file = new FileUploadField();
		file.setName(UIMessages.INSTANCE.gdidFileUploadFieldText());
		file.setAllowBlank(false);

		layoutContainer.add(new FieldLabel(file, UIMessages.INSTANCE.file()),
				new VerticalLayoutData(-18, -1));
		layoutContainer.add(new Label(UIMessages.INSTANCE.maxFileSizeText()),
				new VerticalLayoutData(-18, -1));

		layoutContainer.add(new FieldLabel(loadFileButton),
				new VerticalLayoutData(-18, -1));

		uploadPanel = new FormPanel();
		uploadPanel.setMethod(Method.POST);
		uploadPanel.setEncoding(Encoding.MULTIPART);
		uploadPanel.setAction("fileupload.do");
		uploadPanel.setWidget(layoutContainer);

		return uploadPanel;
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
		attributeCombo.setVisible(false);
		attributeCombo.setTypeAhead(true);
		attributeCombo.setEmptyText(UIMessages.INSTANCE
				.asdAttributeComboEmptyText());
		attributeCombo.setTriggerAction(TriggerAction.ALL);
		attributeCombo.setForceSelection(true);
		attributeCombo.setEditable(false);
		attributeCombo.enableEvents();
	}

	private Widget createPanel() {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(2);
		vPanel.add(uploadPanel);
		vPanel.add(new FieldLabel(attributeCombo, UIMessages.INSTANCE
				.bindableAttribute()));
		return vPanel;
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

}
