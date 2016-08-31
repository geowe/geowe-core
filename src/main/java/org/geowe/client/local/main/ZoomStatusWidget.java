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
package org.geowe.client.local.main;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;

/**
 * Responsible for show actual zoom level
 * 
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class ZoomStatusWidget implements IsWidget {	
	private HorizontalLayoutContainer widget;
	private HorizontalPanel hp;
	private Label label;
	@Inject 
	private GeoMap geoMap;
	@Override
	public Widget asWidget() {
		if (widget == null) {
			final String width = "60px";
			final String height = "10px";
			widget = new HorizontalLayoutContainer();
			widget.getElement().getStyle().setPosition(Position.ABSOLUTE);
			widget.getElement().getStyle().setLeft(10, Unit.PX);
			widget.getElement().getStyle().setBottom(90, Unit.PX);
			widget.setSize(width, height);
			
			hp = new HorizontalPanel();
			hp.setSpacing(2);
			hp.getElement().getStyle().setBackgroundColor("#FFFFFF");
			hp.setSize(width, height);
			
			hp.add(getZoomLabel());
			
			widget.add(hp);
		}
		return widget;
	}

	private Label getZoomLabel() {
		label = new Label(UIMessages.INSTANCE.zoomLevelText(geoMap.getMap().getZoom()));
		label.getElement().getStyle().setFontSize(9, Unit.PX);
		return label;
	}

	public void updateZoomLevel(int zoomLevel) {
		label.setText(UIMessages.INSTANCE.zoomLevelText(zoomLevel));
	}

	public void updateTitle(String htmlColor, String title) {
		label.getElement().getStyle().setColor(htmlColor);
		label.setTitle(title);
	}
}
