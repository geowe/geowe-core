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
package org.geowe.client.local.style;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * A Simple vertical legend based on attribute theming
 * 
 * @author rafa@geowe.org
 *
 */
public class SimpleThemingVerticalLegend extends VerticalLayoutContainer {

	private static final String WIDTH = "150px";
	private static final String HEIGHT = "590px";
	private String attributeName;
	private VectorLayer selectedLayer;
	private VerticalPanel verticalPanel;

	public SimpleThemingVerticalLegend(VectorLayer selectedLayer,
			String attributeName) {
		super();
		this.attributeName = attributeName;
		this.selectedLayer = selectedLayer;
		create();
	}

	private void create() {
		setSize(WIDTH, HEIGHT);
		setVisible(false);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
		getElement().getStyle().setRight(5, Unit.PX);
		getElement().getStyle().setTop(70, Unit.PX);
		ScrollSupport scrollSupport = getScrollSupport();
		scrollSupport.setScrollMode(ScrollMode.AUTO);
		createContentPanel();
		add(verticalPanel);

		new Draggable(this);
	}

	private void createContentPanel() {
		this.verticalPanel = new VerticalPanel();
		StyleInjector.inject(".legendPanel { " + "background: #FFFFFF;"
				+ "border-radius: 5px 10px;" + "opacity: 0.8}");
		verticalPanel.setStyleName("legendPanel");
		verticalPanel.setSize(WIDTH, HEIGHT);
		setData();
	}

	private void setData() {
		verticalPanel.add(getHeader());
		Map<String, String> attrColorMap = createAttributeColorMap();
		for (Entry<String, String> attr : attrColorMap.entrySet()) {
			HorizontalPanel hpanel = new HorizontalPanel();
			hpanel.setSpacing(2);
			hpanel.add(getColorPanel(attr.getValue()));
			hpanel.add(new Label(attr.getKey()));

			verticalPanel.add(hpanel);
		}
	}

	private HTML getHeader() {
		return new HTML(selectedLayer.getName() + "<br><b>" + attributeName
				+ "</b><hr><hr>");
	}

	private Map<String, String> createAttributeColorMap() {
		Map<String, String> attrColorMap = new HashMap<String, String>();
		for (VectorFeature feature : selectedLayer.getFeatures()) {
			String attrValue = feature.getAttributes().getAttributeAsString(
					attributeName);
			attrColorMap.put(attrValue, StyleFactory.stringToColour(attrValue));
		}
		return attrColorMap;
	}

	private HorizontalPanel getColorPanel(String color) {
		HorizontalPanel colorPanel = new HorizontalPanel();
		colorPanel.setSize("20px", "20px");
		colorPanel.getElement().getStyle().setBackgroundColor(color);
		colorPanel.getElement().getStyle().setBorderColor(color);
		return colorPanel;
	}
}
