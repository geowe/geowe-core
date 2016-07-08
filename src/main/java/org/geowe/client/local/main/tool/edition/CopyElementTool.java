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
package org.geowe.client.local.main.tool.edition;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Layer;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Copy element tool
 * 
 * @author jmmluna
 *
 */
@ApplicationScoped
public class CopyElementTool extends ButtonTool {

	private final LayerManagerWidget layerManager;
	@Inject
	private CopyElementDialog copyElementDialog;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	private VectorFeature[] selectedFeatures;

	@Inject
	public CopyElementTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.copyToolText(), ImageProvider.INSTANCE
				.copyElement32(), layerManager);

		this.layerManager = layerManager;
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.copyToolText(),
				UIMessages.INSTANCE.copyToolTip(), Side.LEFT));
		setEnabled(false);
	}

	@PostConstruct
	private void initialize() {

		copyElementDialog.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {

						VectorLayer targetLayer = (VectorLayer) copyElementDialog
								.getLayerCombo1().getValue().getLayer();

						for (VectorFeature feature : selectedFeatures) {
							VectorFeature newVectorFeature = feature.clone();
							targetLayer.addFeature(newVectorFeature);
						}

						targetLayer.redraw();
					}
				});
	}

	@Override
	protected void onRelease() {
		VectorLayer layer = (VectorLayer) layerManager
				.getSelectedLayer(LayerManagerWidget.VECTOR_TAB);
		selectedFeatures = layer.getSelectedFeatures();
		if (selectedFeatures == null || selectedFeatures.length == 0) {
			messageDialogBuilder.createError(UIMessages.INSTANCE.warning(),
					UIMessages.INSTANCE.noSelectedElements()).show();
			return;
		}

		List<Layer> vectorLayers = layerManager.getLayerTree(
				LayerManagerWidget.VECTOR_TAB).getLayers();
		copyElementDialog.setLayers(vectorLayers);
		copyElementDialog.show();
	}

}
