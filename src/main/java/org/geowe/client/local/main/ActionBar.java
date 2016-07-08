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

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.sencha.gxt.core.client.Style.Direction;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;

/**
 * Represents the main menu bar
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class ActionBar extends ContentPanel {

	@Inject
	private MenuPanelWidget menuPanelWidget;
	
	@Inject
	private GeocodingPanelWidget geocodingPanelWidget;

	public ActionBar() {
		super();
		setWidth(300);
		setHeaderVisible(false);
		addStyleName(ThemeStyles.get().style().borderBottom());
		setPosition(0, 0);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.add(getMenuButton());
		horizontalGroup.add(new SeparatorToolItem());
		Image image = new Image(ImageProvider.INSTANCE.menulogoGeowe());
		horizontalGroup.add(image);
		horizontalGroup.add(new SeparatorToolItem());
		horizontalGroup.add(new SeparatorToolItem());						
		horizontalGroup.add(getGeocodingButton());
		setWidget(horizontalGroup);		
	}
	
	private ToggleButton getGeocodingButton() {
		ToggleButton geocodingButton = new ToggleButton();
		geocodingButton.setIcon(ImageProvider.INSTANCE.geocoding24());
		geocodingButton.setId("geocoding_button");
		geocodingButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				
				geocodingSlideFx(event);
			}
		});
		
		return geocodingButton;
	}
	
	private void geocodingSlideFx(SelectEvent event) {
		if (geocodingPanelWidget.asWidget().isVisible()) {
			geocodingPanelWidget.asWidget().getElement().<FxElement> cast()
					.slideOut(Direction.LEFT);
		} else {
			geocodingPanelWidget.asWidget().setVisible(true);
			geocodingPanelWidget.asWidget().getElement().<FxElement> cast()
					.slideIn(Direction.RIGHT);
		}
	}

	private TextButton getMenuButton() {
		TextButton menuButton = new TextButton(UIMessages.INSTANCE.abMenu());
		menuButton.setIcon(ImageProvider.INSTANCE.menuIcon());
		menuButton.setId("menu_button");
		menuButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				slideFx(event);
			}
		});
		
		return menuButton;
	}

	private void slideFx(SelectEvent event) {
		if (menuPanelWidget.asWidget().isVisible()) {
			menuPanelWidget.asWidget().getElement().<FxElement> cast()
					.slideOut(Direction.UP);
		} else {
			menuPanelWidget.asWidget().setVisible(true);
			menuPanelWidget.asWidget().getElement().<FxElement> cast()
					.slideIn(Direction.DOWN);
		}
	}
}
