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
import org.geowe.client.local.main.tool.search.AttributeSearchDialog;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * Attribute search tool.
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class SearchAttributeTool extends ButtonTool {

	@Inject
	private AttributeSearchDialog attributeSearchDialog;
	@Inject
	private LayerManagerWidget layerManagerWidget;

	@Inject
	public SearchAttributeTool(GeoMap geoMap) {
		super(UIMessages.INSTANCE.searchAttributeToolText(),
				ImageProvider.INSTANCE.search32());
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.searchAttributeToolText(),
				UIMessages.INSTANCE.searchAttributeToolTip(), Side.LEFT));
		setEnabled(false);
	}

	@Override
	protected void onRelease() {
		VectorLayer layer = (VectorLayer) layerManagerWidget
				.getSelectedLayer(LayerManagerWidget.VECTOR_TAB);

		attributeSearchDialog.setSelectedLayer(layer);
		attributeSearchDialog.show();
	}

	@Override
	public void onChange(Vector layer) {
		setEnabled(true);
		attributeSearchDialog.setSelectedLayer((VectorLayer) layer);
	}
}
