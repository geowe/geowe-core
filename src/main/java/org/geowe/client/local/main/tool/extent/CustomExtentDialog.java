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

package org.geowe.client.local.main.tool.extent;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class CustomExtentDialog extends Dialog {
	
	private TextField bboxField;	
	private final TextButton addToMapButton;
		
	public CustomExtentDialog() {
		setHideOnButtonClick(true);
		setPredefinedButtons(PredefinedButton.CLOSE);

		setButtonAlign(BoxLayoutPack.CENTER);
		setResizable(false);
		setWidth(480);
		setHeight(190);
		setHeadingHtml(UIMessages.INSTANCE.headCustomExtentDialog());
		add(createPanel());
		
		addToMapButton = new TextButton(UIMessages.INSTANCE.addToMapButton());		
		this.getButtonBar().add(addToMapButton);
	}
	
	public void initialize() {
		bboxField.clear();
		setModal(true);
		show();		
	}
	
	public TextButton getAddToMapButton() {
		return addToMapButton;
	}
	
	public String getBbox() {
		return bboxField.getText();
	}
	
	private Widget createPanel() {
		
		VerticalLayoutContainer container = new VerticalLayoutContainer();
		container.setScrollMode(ScrollMode.AUTO);
		container.setSize("450px", "160px");
				
		Label bboxLabel = new Label("Bbox (" + UIMessages.INSTANCE.lowerLeftXField() + ", " + UIMessages.INSTANCE.lowerLeftYField() + 
				", " + UIMessages.INSTANCE.upperRightXField() + ", " + UIMessages.INSTANCE.upperRightYField() + ")");
		bboxLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);		
		container.add(bboxLabel);
		bboxField = new TextField();
		bboxField.setEmptyText(UIMessages.INSTANCE.bboxFieldCustomExtent());		
		bboxField.setWidth("450px");
		container.add(bboxField);	
		return container;
	}
}
