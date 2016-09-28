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
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class CustomExtentDialog extends Dialog {
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	private TextField bboxField;	
	private TextButton addToMapButton;
		

	@Inject
	public CustomExtentDialog(final GeoMap geoMap, final LayerManagerWidget layerManager) {
		setHideOnButtonClick(true);
		setPredefinedButtons(PredefinedButton.CLOSE);

		setButtonAlign(BoxLayoutPack.CENTER);
		setResizable(false);
		setWidth(480);
		setHeight(190);
		setHeadingHtml("Extensión máxima personalizada");
		add(createPanel());
		
		addToMapButton = new TextButton(UIMessages.INSTANCE.addToMapButton());
		addToMapButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				String bbox = bboxField.getText();
				if(bbox.isEmpty()) {
					messageDialogBuilder.createError("Atención", "Inserta las coordenadas").show();
					return;
				}
				
				
				String[] coordinates = bbox.split("\\,");
				if(coordinates.length != 4) {
					messageDialogBuilder.createError("Atención", "Las coordenadas no son correctas").show();
					return;
				}
				
				double lowerLeftX = Double.parseDouble(coordinates[0]);
				double lowerLeftY = Double.parseDouble(coordinates[1]);
				double upperRightX = Double.parseDouble(coordinates[2]);
				double upperRightY = Double.parseDouble(coordinates[3]);
				
				Bounds bounds = new Bounds(lowerLeftX, lowerLeftY, upperRightX, upperRightY);
				VectorFeature vf = new VectorFeature(bounds.toGeometry());
				VectorLayer bboxLayer = new VectorLayer("CustomBBox");
				bboxLayer.addFeature(vf);
				layerManager.addVector(bboxLayer);
				geoMap.getMap().zoomToExtent(bboxLayer.getDataExtent());	
			}			
		});
		
		this.getButtonBar().add(addToMapButton);

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
		bboxField.setEmptyText("Intoduce las coordenadas separadas por ,");		
		bboxField.setWidth("450px");
		container.add(bboxField);	
		return container;
	}
}