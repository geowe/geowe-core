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
package org.geowe.client.local.layermanager.tool.export;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.KeyShortcutHandler;
import org.geowe.client.local.ui.ProjectionComboBox;
import org.geowe.client.local.ui.VectorFormatComboBox;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;

public class ExportDataDialog extends Dialog {

	private TextButton downloadFileButton;
	private TextButton gitHubButton;
	private VectorFormatComboBox vectorFormatCombo;
	private ProjectionComboBox epsgCombo;

	private VectorLayer vectorLayer;

	public ExportDataDialog() {
		super();
		setPredefinedButtons(PredefinedButton.CLOSE);
		setButtonAlign(BoxLayoutPack.CENTER);
		setBodyStyleName("pad-text");
		getBody().addClassName("pad-text");
		setHideOnButtonClick(true);
		setResizable(false);
		setWidth(450);

		add(createPanel());
		addKeyShortcuts();
	}

	public VectorLayer getVectorLayer() {
		return vectorLayer;
	}

	public void setVectorLayer(VectorLayer vectorLayer) {
		this.vectorLayer = vectorLayer;

		setHeadingText(UIMessages.INSTANCE
				.exportDataToolDialogTitle(vectorLayer.getName()));
		epsgCombo.setValue(vectorLayer.getProjection().getProjectionCode());
	}

	public TextButton getDownloadFileButton() {
		return downloadFileButton;
	}
	
	public TextButton getGitHubButton() {
		return gitHubButton;
	}

	public VectorFormat getSelectedFormat() {
		return vectorFormatCombo.getValue();
	}

	public String getSelectedEpsg() {
		return epsgCombo.getValue();
	}

	private Widget createPanel() {
		String comboWidth = "150px";

		// Combo initialization
		vectorFormatCombo = new VectorFormatComboBox(comboWidth,
				VectorFormat.getAllFormat());
		vectorFormatCombo.addSelectionHandler(getVectorComboSelectionHandler());
		epsgCombo = new ProjectionComboBox(comboWidth);

		downloadFileButton = new TextButton(
				UIMessages.INSTANCE.downloadfileText());
		downloadFileButton.setIcon(ImageProvider.INSTANCE.download32());
		downloadFileButton.setIconAlign(IconAlign.TOP);
		
		gitHubButton = new TextButton(
				"Guardar en GitHub");
		gitHubButton.setIcon(ImageProvider.INSTANCE.download32());
		gitHubButton.setIconAlign(IconAlign.TOP);

		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.getElement().getStyle().setBackgroundColor("#E0ECF8");
		horizontalGroup.setSpacing(10);

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(5);
		vPanel.add(vectorFormatCombo);
		vPanel.add(epsgCombo);

		horizontalGroup.add(vPanel);
		horizontalGroup.add(downloadFileButton);
		horizontalGroup.add(gitHubButton);
		

		return horizontalGroup;
	}

	private SelectionHandler<VectorFormat> getVectorComboSelectionHandler() {
		return new SelectionHandler<VectorFormat>() {

			@Override
			public void onSelection(SelectionEvent<VectorFormat> event) {
					epsgCombo.enable();
			}
		};
	}

	private void addKeyShortcuts() {
		KeyShortcutHandler keyShortcut = new KeyShortcutHandler(
				getButton(PredefinedButton.OK), KeyCodes.KEY_ENTER);

		vectorFormatCombo.addKeyDownHandler(keyShortcut);
		epsgCombo.addKeyDownHandler(keyShortcut);
	}
}
