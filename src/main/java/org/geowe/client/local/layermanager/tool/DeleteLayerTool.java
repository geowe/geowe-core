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
package org.geowe.client.local.layermanager.tool;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.PanTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.layer.Layer;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;

/**
 * Responsible for removing the selected layer
 * 
 * @author geowe.org
 *
 */
@ApplicationScoped
public class DeleteLayerTool extends LayerTool {

	@Inject
	private PanTool panTool;

	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	@Inject
	public DeleteLayerTool(final LayerManagerWidget layerTreeWidget, final GeoMap geoMap) {
		super(layerTreeWidget, geoMap);
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.deleteLayerToolText();
	}

	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.deleteLayer();
	}

	@Override
	public void onClick() {
		final Layer layer = getSelectedLayer();
		if (layer != null) {
			if (layer.isBaseLayer()) {
				messageDialogBuilder.createError(
						UIMessages.INSTANCE.dltAlertMessageBoxTitle(),
						UIMessages.INSTANCE.dltAlertMessageBoxLabel()).show();
			} else {
				createConfirm();
			}
		}
	}

	private void createConfirm() {
		final List<Layer> layers = layerManagerWidget.getSelectedLayers();
		final ConfirmMessageBox messageBox = messageDialogBuilder.createConfirm(
				UIMessages.INSTANCE.dltConfirMessageBoxTitle(),
				UIMessages.INSTANCE.dltConfirMessageBoxLabel(layers.size()),
				getIcon());

		messageBox.addDialogHideHandler(getHideHandler(layers));
		messageBox.show();
	}

	private DialogHideHandler getHideHandler(final List<Layer> layers) {
		return new DialogHideHandler() {
			@Override
			public void onDialogHide(final DialogHideEvent event) {
				if (PredefinedButton.YES.equals(event.getHideButton())) {
					for (final Layer layer : layers) {
						layerManagerWidget.removeLayer(layer);
						panTool.setValue(true, true);
					}
				}
			}
		};
	}
}
