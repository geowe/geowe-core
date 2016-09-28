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
import javax.inject.Inject;

import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.format.WKT;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class CurrentExtentDialog extends Dialog {
	private TextArea wktTextArea;
	private TextField centerField;
	private TextField bboxField;
	private TextField lowerLeftXField;
	private TextField lowerLeftYField;
	private TextField upperRightXField;
	private TextField upperRightYField;
	private TextButton addToMapButton;
	private CurrentExtentInfo model;	

	@Inject
	public CurrentExtentDialog(final GeoMap geoMap, final LayerManagerWidget layerManager) {
		setHideOnButtonClick(true);
		setPredefinedButtons(PredefinedButton.CLOSE);

		setButtonAlign(BoxLayoutPack.CENTER);
		setResizable(false);
		setWidth(480);
		setHeight(390);
		setHeadingHtml(UIMessages.INSTANCE.headCurrentExtentDialog());
		add(createPanel());
		
		addToMapButton = new TextButton(UIMessages.INSTANCE.addToMapButton());
		addToMapButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				WKT wktFormat = new WKT();
				VectorLayer extentLayer = new VectorLayer("BBox");
				extentLayer.addFeatures(wktFormat.read(model.getWkt()));								
				layerManager.addVector(extentLayer);	
				geoMap.getMap().zoomToExtent(extentLayer.getDataExtent());
			}			
		});
		
		this.getButtonBar().add(addToMapButton);

	}
	
	private Widget createPanel() {
		String fieldWidth = "225px";
		VerticalLayoutContainer container = new VerticalLayoutContainer();
		container.setScrollMode(ScrollMode.AUTO);
		container.setSize("450px", "360px");
		Label centerLabel = new Label(UIMessages.INSTANCE.centerField());
		centerLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		container.add(centerLabel);
		centerField = new TextField();
		centerField.setReadOnly(true);		
		centerField.setWidth("450px");
		container.add(centerField);

		HorizontalPanel horizontalLowerPanel = new HorizontalPanel();
		VerticalPanel vPanel = new VerticalPanel();
		Label lowerLeftX = new Label(UIMessages.INSTANCE.lowerLeftXField());
		lowerLeftX.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		
		vPanel.add(lowerLeftX);
		lowerLeftXField = new TextField();
		lowerLeftXField.setReadOnly(true);
		lowerLeftXField.setWidth(fieldWidth);
		vPanel.add(lowerLeftXField);
		horizontalLowerPanel.add(vPanel);
		container.add(horizontalLowerPanel);

		vPanel = new VerticalPanel();
		Label lowerLeftY = new Label(UIMessages.INSTANCE.lowerLeftYField());
		lowerLeftY.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		
		vPanel.add(lowerLeftY);
		lowerLeftYField = new TextField();
		lowerLeftYField.setReadOnly(true);
		lowerLeftYField.setWidth(fieldWidth);
		vPanel.add(lowerLeftYField);
		horizontalLowerPanel.add(vPanel);
		container.add(horizontalLowerPanel);
				
		horizontalLowerPanel = new HorizontalPanel();
		vPanel = new VerticalPanel();
		Label upperRightX = new Label(UIMessages.INSTANCE.upperRightXField());
		upperRightX.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		
		vPanel.add(upperRightX);
		upperRightXField = new TextField();
		upperRightXField.setReadOnly(true);
		upperRightXField.setWidth(fieldWidth);
		vPanel.add(upperRightXField);
		horizontalLowerPanel.add(vPanel);
		container.add(horizontalLowerPanel);

		vPanel = new VerticalPanel();
		
		Label upperRightY = new Label(UIMessages.INSTANCE.upperRightYField());
		upperRightY.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		
		vPanel.add(upperRightY);
		upperRightYField = new TextField();
		upperRightYField.setReadOnly(true);
		upperRightYField.setWidth(fieldWidth);
		vPanel.add(upperRightYField);
		horizontalLowerPanel.add(vPanel);
		container.add(horizontalLowerPanel);
		
		Label bboxLabel = new Label("Bbox (" + UIMessages.INSTANCE.lowerLeftXField() + ", " + UIMessages.INSTANCE.lowerLeftYField() + 
				", " + UIMessages.INSTANCE.upperRightXField() + ", " + UIMessages.INSTANCE.upperRightYField() + ")");
		bboxLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);		
		container.add(bboxLabel);
		bboxField = new TextField();
		bboxField.setReadOnly(true);		
		bboxField.setWidth("450px");
		container.add(bboxField);
		Label wktLabel = new Label("Bbox WKT WGS84");
		wktLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		container.add(wktLabel);
		container.add(getTextPanel());

		return container;
	}

	public void setModel(CurrentExtentInfo model) {
		this.model = model;
		wktTextArea.setText(model.getWktWGS84());
		centerField.setText(model.getCenter());
		bboxField.setText(model.getBbox());
		lowerLeftXField.setText(Double.toString(model.getLowerLeftX()));
		lowerLeftYField.setText(Double.toString(model.getLowerLeftY()));		
		upperRightXField.setText(Double.toString(model.getUpperRightX()));
		upperRightYField.setText(Double.toString(model.getUpperRightY()));		
	}

	private TextArea getTextPanel() {
		wktTextArea = new TextArea();
		wktTextArea.setReadOnly(true);
		wktTextArea.setBorders(true);
		wktTextArea.setSize("450px", "100px");

		return wktTextArea;
	}
}