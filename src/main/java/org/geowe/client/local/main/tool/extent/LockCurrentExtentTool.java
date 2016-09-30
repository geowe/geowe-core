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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.Bounds;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.inject.Inject;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class LockCurrentExtentTool extends ToggleButton {
	private static final String WIDTH = "80px";
	private static final String HEIGHT = "80px";
	
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	private final GeoMap geoMap;
	
	@Inject
	public LockCurrentExtentTool(GeoMap geoMap) {
		super(UIMessages.INSTANCE.nameLockCurrentExtentTool());
		setIconAlign(IconAlign.TOP);
		setSize(WIDTH, HEIGHT);
		setIcon(ImageProvider.INSTANCE.unlockedExtension24());		
		this.geoMap = geoMap;
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.titleLockCurrentExtentToolTip(),
				UIMessages.INSTANCE.descriptionLockCurrentExtentToolTip(), Side.LEFT));
	}
	
	private ToolTipConfig createTooltipConfig(String title, String body,
			Side position) {
		ToolTipConfig toolTipconfig = new ToolTipConfig();
		toolTipconfig.setTitleHtml(title);
		toolTipconfig.setBodyHtml(body);
		toolTipconfig.setMouseOffsetX(0);
		toolTipconfig.setMouseOffsetY(0);
		toolTipconfig.setAnchor(position);

		return toolTipconfig;
	}

	@PostConstruct
	private void registerValueChangeHandler() {
		addValueChangeHandler(getSelectChangeHandler());
	}

	protected ValueChangeHandler<Boolean> getSelectChangeHandler() {
		return new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(final ValueChangeEvent<Boolean> event) {

				if (event.getValue()) {					
					confirmSetMaxExtent(event.getValue());					
				} else {
					confirmClearMaxExtent(event.getValue());					
				}
			}
		};
	}
	
	private void confirmSetMaxExtent(final boolean value) {

		ConfirmMessageBox messageBox = messageDialogBuilder.createConfirm(
				UIMessages.INSTANCE.edtAlertDialogTitle(),
				UIMessages.INSTANCE.confirmSetMaxExtent(),
				ImageProvider.INSTANCE.currentExtent24());
		messageBox.getButton(PredefinedButton.YES).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						geoMap.getMap().setRestrictedExtent(geoMap.getMap().getExtent());
						geoMap.getMap().setMaxExtent(geoMap.getMap().getExtent());
						geoMap.getMap().zoomToMaxExtent();
						setText("Desbloquear");
						setIcon(ImageProvider.INSTANCE.lockedExtension24());						
					}
				});
		
		messageBox.getButton(PredefinedButton.NO).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						setValue(!value);
					}
				});
		messageBox.show();
	}	
	
	private void confirmClearMaxExtent(final boolean value) {

		ConfirmMessageBox messageBox = messageDialogBuilder.createConfirm(
				UIMessages.INSTANCE.edtAlertDialogTitle(),
				UIMessages.INSTANCE.confirmClearMaxExtent(),
				ImageProvider.INSTANCE.currentExtent24());
		messageBox.getButton(PredefinedButton.YES).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						Bounds defaultBounds = geoMap.getDefaultMapBound();
						geoMap.getMap().setRestrictedExtent(defaultBounds);
						geoMap.getMap().setMaxExtent(defaultBounds);
						setText("Bloquear");
						setIcon(ImageProvider.INSTANCE.unlockedExtension24());						
					}
				});
		
		messageBox.getButton(PredefinedButton.NO).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						setValue(!value);						
					}
				});
		
		messageBox.show();
	}	
	
}