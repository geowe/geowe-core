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

import java.util.List;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * Vertical container for legends
 * 
 * @author rafa@geowe.org
 *
 */
public abstract class VerticalLegend extends VerticalLayoutContainer {

	private static final String WIDTH = "150px";
	private static final String HEIGHT = "590px";
	protected VerticalPanel contentPanel;

	protected void create() {
		setSize(WIDTH, HEIGHT);
		setVisible(false);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
		getElement().getStyle().setRight(5, Unit.PX);
		getElement().getStyle().setTop(70, Unit.PX);
		ScrollSupport scrollSupport = getScrollSupport();
		scrollSupport.setScrollMode(ScrollMode.AUTO);
		createContentPanel();
		add(contentPanel);

		new Draggable(this);
	}

	private void createContentPanel() {
		this.contentPanel = new VerticalPanel();
		StyleInjector.inject(".mapLegendPanel { " + "background: #FFFFFF;"
				+ "border-radius: 5px 10px;" + "opacity: 0.8}");
		contentPanel.setStyleName("mapLegendPanel");
		contentPanel.setSize(WIDTH, HEIGHT);
		contentPanel.add(getHeader());
		setData(getData());
	}

	private void setData(List<Widget> widgets) {
		for (Widget widget : widgets) {
			contentPanel.add(widget);
		}
	}

	protected abstract List<Widget> getData();

	protected abstract HTML getHeader();
}
