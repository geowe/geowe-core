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
package org.geowe.client.local.main.tool;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.map.MapControlFactory;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * Selection tool
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class SelectionTool extends ToggleTool {

	@Inject
	private MapControlFactory mapControlFactory;

	@Inject
	public SelectionTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.selectionToolText(), ImageProvider.INSTANCE
				.select(), geoMap, layerManager);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.selectionToolText(),
				UIMessages.INSTANCE.selectionToolTip(), Side.TOP));

		setEnabled(false);
		setId("selectionTool");
	}



	@PostConstruct
	private void initialize() {
		add(mapControlFactory.createBoxSelect(new Vector("Empty")));
		add(mapControlFactory.createSelectHover(new Vector("Empty")));
		add(mapControlFactory.createSelectionControl(new Vector("Empty")));
	}

}
