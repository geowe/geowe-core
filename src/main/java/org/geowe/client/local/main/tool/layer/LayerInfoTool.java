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
package org.geowe.client.local.main.tool.layer;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.main.tool.info.LayerInfoDialog;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

@ApplicationScoped
public class LayerInfoTool extends ButtonTool {
	
	@Inject
	private LayerInfoDialog layerInfoDialog;

	private final LayerManagerWidget layerManagerWidget;
	
	@Inject
	public LayerInfoTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.layerInfoToolText(), ImageProvider.INSTANCE
				.info32(), layerManager);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.layerInfoToolText(),
				UIMessages.INSTANCE.layerInfoToolTip(), Side.LEFT));
		this.layerManagerWidget = layerManager;
        setEnabled(false);
	}

	@Override
	protected void onRelease() {
		VectorLayer layer = (VectorLayer) layerManagerWidget
				.getSelectedLayer(LayerManagerWidget.VECTOR_TAB);
		layerInfoDialog.setSelectedLayer(layer);
		layerInfoDialog.show();

	}

	@Override
	public void onChange(Vector layer) {
		setEnabled(true);
		layerInfoDialog.setSelectedLayer((VectorLayer) layer);
	}

}
