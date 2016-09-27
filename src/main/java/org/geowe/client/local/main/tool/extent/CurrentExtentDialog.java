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
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.format.WKT;

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
	private TextButton setMaxExtentToMap;

	private CurrentExtentInfo model;

	@Inject
	public CurrentExtentDialog(final GeoMap geoMap, final LayerManagerWidget layerManager) {
		setHideOnButtonClick(true);
		setPredefinedButtons(PredefinedButton.CLOSE);

		setButtonAlign(BoxLayoutPack.CENTER);
		setResizable(false);
		setWidth(480);
		setHeight(500);
		setHeadingHtml("Extensión actúal del mapa (WGS84 - EPSG:4326)");
		add(createPanel());
		
		addToMapButton = new TextButton("Add to Map");
		setMaxExtentToMap = new TextButton("Max Extent to Map");
		
		addToMapButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				WKT wktFormat = new WKT();
				VectorLayer extentLayer = new VectorLayer("BBox");
				extentLayer.addFeatures(wktFormat.read(model.getWkt()));
								
				layerManager.addVector(extentLayer);				
			}
			
		});
		
		setMaxExtentToMap.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				MapOptions mapOptions = geoMap.getMapOptions();
				mapOptions.setMaxExtent(model.getBounds());
				geoMap.getMap().setOptions(mapOptions);
				geoMap.getMap().zoomToMaxExtent();
			}
			
		});
		
		
		this.getButtonBar().add(addToMapButton);
		this.getButtonBar().add(setMaxExtentToMap);
	}

	private Widget createPanel() {
		String fieldWidth = "225px";
		VerticalLayoutContainer container = new VerticalLayoutContainer();
		container.setScrollMode(ScrollMode.AUTO);
		container.setSize("450px", "460px");

		container.add(new Label("Center of map"));
		centerField = new TextField();
		centerField.setEnabled(false);
		centerField.setWidth("450px");
		container.add(centerField);

		HorizontalPanel horizontalLowerPanel = new HorizontalPanel();
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.add(new Label("lower Left X"));
		lowerLeftXField = new TextField();
		lowerLeftXField.setEnabled(false);
		lowerLeftXField.setWidth(fieldWidth);
		vPanel.add(lowerLeftXField);
		horizontalLowerPanel.add(vPanel);
		container.add(horizontalLowerPanel);

		vPanel = new VerticalPanel();
		vPanel.add(new Label("lower Left Y"));
		lowerLeftYField = new TextField();
		lowerLeftYField.setEnabled(false);
		lowerLeftYField.setWidth(fieldWidth);
		vPanel.add(lowerLeftYField);
		horizontalLowerPanel.add(vPanel);
		container.add(horizontalLowerPanel);
				
		horizontalLowerPanel = new HorizontalPanel();
		vPanel = new VerticalPanel();
		vPanel.add(new Label("upper Right X"));
		upperRightXField = new TextField();
		upperRightXField.setEnabled(false);
		upperRightXField.setWidth(fieldWidth);
		vPanel.add(upperRightXField);
		horizontalLowerPanel.add(vPanel);
		container.add(horizontalLowerPanel);

		vPanel = new VerticalPanel();
		vPanel.add(new Label("upper Right Y"));
		upperRightYField = new TextField();
		upperRightYField.setEnabled(false);
		upperRightYField.setWidth(fieldWidth);
		vPanel.add(upperRightYField);
		horizontalLowerPanel.add(vPanel);
		container.add(horizontalLowerPanel);
				
		container.add(new Label("bbox (lowerLeftX, lowerLeftY, upperRigthX, upperRigthY)"));
		bboxField = new TextField();
		bboxField.setEnabled(false);
		bboxField.setWidth("450px");
		container.add(bboxField);
		
		container.add(new Label("BBOX WKT WGS84"));
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
		wktTextArea.setEnabled(false);
		wktTextArea.setBorders(true);
		wktTextArea.setSize("450px", "100px");

		return wktTextArea;
	}

}
