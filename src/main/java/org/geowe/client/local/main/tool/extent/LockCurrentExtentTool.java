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
		super("Bloquear");
		setIconAlign(IconAlign.TOP);
		setSize(WIDTH, HEIGHT);
		setIcon(ImageProvider.INSTANCE.unlockedExtension24());		
		this.geoMap = geoMap;
		setToolTipConfig(createTooltipConfig(
				"Bloquear extensión",
				"Establecer la restricción de máxima extensión al marco actual del mapa", Side.LEFT));
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
					confirmSetMaxExtent();					
				} else {
					confirmClearMaxExtent();
				}
			}
		};
	}
	
	private void confirmSetMaxExtent() {

		ConfirmMessageBox messageBox = messageDialogBuilder.createConfirm(
				UIMessages.INSTANCE.edtAlertDialogTitle(),
				"Se va a establecer la restricción de máxima extensión del mapa. ¿Está seguro?",
				ImageProvider.INSTANCE.currentExtent24());
		messageBox.getButton(PredefinedButton.YES).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						geoMap.getMap().setRestrictedExtent(geoMap.getMap().getExtent());
						geoMap.getMap().setMaxExtent(geoMap.getMap().getExtent());
						geoMap.getMap().zoomToMaxExtent();
						setIcon(ImageProvider.INSTANCE.lockedExtension24());
						messageDialogBuilder.createInfo("Atención", "Se ha establecido nueva restricción de extensión máxima del mapa").show();
					}
				});
		
		messageBox.show();
	}	
	
	private void confirmClearMaxExtent() {

		ConfirmMessageBox messageBox = messageDialogBuilder.createConfirm(
				UIMessages.INSTANCE.edtAlertDialogTitle(),
				"Se va a eliminar la restricción de máxima extensión del mapa. ¿Está seguro?",
				ImageProvider.INSTANCE.currentExtent24());
		messageBox.getButton(PredefinedButton.YES).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						Bounds defaultBounds = geoMap.getDefaultMapBound();
						geoMap.getMap().setRestrictedExtent(defaultBounds);
						geoMap.getMap().setMaxExtent(defaultBounds);
						setIcon(ImageProvider.INSTANCE.unlockedExtension24());
						messageDialogBuilder.createInfo("Atención", "Se ha eliminado la restricción de extensión máxima del mapa").show();
					}
				});
		
		messageBox.show();
	}	
	
}