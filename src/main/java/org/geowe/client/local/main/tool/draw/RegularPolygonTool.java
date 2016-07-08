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
package org.geowe.client.local.main.tool.draw;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.map.MapControlFactory;
import org.geowe.client.local.main.tool.ToggleTool;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.control.Control;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * Draw regular polygon tool
 * 
 * @author rltorres
 *
 */
@ApplicationScoped
public class RegularPolygonTool extends ToggleTool implements DrawTool {

	
	@Inject
	private MapControlFactory mapControlFactory;
	
	@Inject
	public RegularPolygonTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.regularPolygonToolText(),
				ImageProvider.INSTANCE.regularPolygon(), geoMap, layerManager);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.regularPolygonToolText(),
				UIMessages.INSTANCE.drawRegularPolygonToolTip(), Side.LEFT));
		setEnabled(false);
	}
	
	@PostConstruct
	private void initialize() {
		add(createDrawTool(new Vector("Empty")));
		setCancelable();
	}

	@Override
	public Control createDrawTool(Layer layer) {
		return mapControlFactory.createRegularPoligonControl(layer);
	}
	
}
