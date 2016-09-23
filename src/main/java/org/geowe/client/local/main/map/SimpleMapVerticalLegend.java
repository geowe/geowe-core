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
package org.geowe.client.local.main.map;

import java.util.ArrayList;
import java.util.List;

import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.layer.Layer;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * A Simple vertical legend based on vector Layers style
 * 
 * @author rafa@geowe.org
 *
 */
public class SimpleMapVerticalLegend extends VerticalLegend {

	private List<Layer> layers;

	public SimpleMapVerticalLegend(LayerManagerWidget layerManager) {
		super();
		this.layers = layerManager.getLayerTree(LayerManagerWidget.VECTOR_TAB)
				.getLayers();
		create();
	}

	@Override
	protected List<Widget> getData() {
		List<Widget> widgets = new ArrayList<Widget>();
		for (Layer layer : layers) {
			HorizontalPanel hpanel = new HorizontalPanel();
			hpanel.setSpacing(2);
			hpanel.add(getColorPanel(layer));
			hpanel.add(new Label(layer.getName()));

			widgets.add(hpanel);
		}
		return widgets;
	}

	@Override
	protected HTML getHeader() {
		return new HTML("<b>" + UIMessages.INSTANCE.mpLayersLabel() + "</b><hr><hr>");
	}

	private HorizontalPanel getColorPanel(Layer layer) {
		HorizontalPanel colorPanel = new HorizontalPanel();
		colorPanel.setSize("20px", "20px");
		colorPanel.getElement().getStyle()
				.setBackgroundColor(getColor(layer, "fillColor"));
		colorPanel.setBorderWidth(2);
		colorPanel.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		colorPanel.getElement().getStyle()
				.setBorderColor(getColor(layer, "strokeColor"));
		return colorPanel;
	}

	private String getColor(Layer layer, String property) {
		String color = "#0F58BF";
		if ((((VectorLayer) layer).getStyle()) == null) {

			color = ((VectorLayer) layer).getStyleMap().getJSObject()
					.getProperty("styles").getProperty("default")
					.getProperty("defaultStyle").getPropertyAsString(property);
		} else {
			color = ((VectorLayer) layer).getStyle().getFillColor();
		}
		return color;
	}
}
