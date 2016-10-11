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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geowe.client.local.main.map.VerticalLegend;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * A Simple vertical legend based on attribute theming
 * 
 * @author rafa@geowe.org
 *
 */
public class SimpleThemingVerticalLegend extends VerticalLegend {

	private final String attributeName;
	private final VectorLayer selectedLayer;

	public SimpleThemingVerticalLegend(VectorLayer selectedLayer,
			String attributeName) {
		super();
		this.attributeName = attributeName;
		this.selectedLayer = selectedLayer;
		create();
	}

	@Override
	protected List<Widget> getData() {

		Map<String, String> attrColorMap = createAttributeColorMap();
		List<Widget> widgets = new ArrayList<Widget>();
		for (Entry<String, String> attr : attrColorMap.entrySet()) {
			HorizontalPanel hpanel = new HorizontalPanel();
			hpanel.setSpacing(2);
			hpanel.add(getColorPanel(attr.getValue()));
			hpanel.add(new Label(attr.getKey()));

			widgets.add(hpanel);
		}
		return widgets;
	}

	@Override
	protected HTML getHeader() {
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
