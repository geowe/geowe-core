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

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ToggleTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Lock/unlock map current map extent
 * 
 * @author jose@geowe.org
 * @since 03-10-2016
 * @author rafa@geowe.org Revolved issue 198
 */
@ApplicationScoped
public class LockCurrentExtentTool extends ToggleTool {

	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	private VectorLayer layer;

	@Inject
	public LockCurrentExtentTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.nameLockCurrentExtentTool(),
				ImageProvider.INSTANCE.unlockedExtension24(), geoMap,
				layerManager);

		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.titleLockCurrentExtentToolTip(),
				UIMessages.INSTANCE.descriptionLockCurrentExtentToolTip(),
				Side.LEFT));
	}

	@Override
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
						getGeoMap().getMap().setRestrictedExtent(
								getGeoMap().getMap().getExtent());
						getGeoMap().getMap().setMaxExtent(
								getGeoMap().getMap().getExtent());
						// TODO: meter en UIMessages
						setText("Desbloquear");
						setIcon(ImageProvider.INSTANCE.lockedExtension24());
						createVectorLayer(getGeoMap().getMap().getExtent());
					}
				});

		messageBox.getButton(PredefinedButton.NO).addSelectHandler(
				getNoSelectHandler(value));
		messageBox.show();
	}

	private void createVectorLayer(Bounds bounds) {
		if (layer == null) {
			layer = new VectorLayer("LockedBBox");
			layer.setStyleMap(new StyleMap(createStyle()));
		}
		layer.removeAllFeatures();
		Geometry geom = bounds.toGeometry();
		VectorFeature vf = new VectorFeature(geom);
		layer.addFeature(vf);
		getGeoMap().getMap().addLayer(layer);
	}

	private Style createStyle() {
		Style style = new Style();
		style.setStrokeColor("#FF0000");
		style.setStrokeWidth(2);
		style.setStrokeDashstyle("longdash");
		style.setFill(false);
		return style;
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

						Bounds defaultBounds = getGeoMap().getDefaultMapBound();
						getGeoMap().getMap().setRestrictedExtent(defaultBounds);
						getGeoMap().getMap().setMaxExtent(defaultBounds);
						setText(UIMessages.INSTANCE.nameLockCurrentExtentTool());
						setIcon(ImageProvider.INSTANCE.unlockedExtension24());

						getGeoMap().getMap().zoomToExtent(
								layer.getDataExtent(), true);

						getGeoMap().getMap().removeLayer(layer);
					}
				});

		messageBox.getButton(PredefinedButton.NO).addSelectHandler(
				getNoSelectHandler(value));
		messageBox.show();
	}

	private SelectHandler getNoSelectHandler(final boolean value){
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				setValue(!value);
			}
		};
	}
}
