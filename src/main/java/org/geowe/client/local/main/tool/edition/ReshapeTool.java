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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.map.MapControlFactory;
import org.geowe.client.local.main.tool.ToggleTool;
import org.geowe.client.local.main.tool.draw.DrawTool;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.control.Control;
import org.gwtopenmaps.openlayers.client.control.ModifyFeature;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * Reshape tool
 * 
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class ReshapeTool extends ToggleTool implements DrawTool {

	@Inject
	private MapControlFactory mapControlFactory;

	@Inject
	public ReshapeTool(final GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.reshapeToolText(), ImageProvider.INSTANCE
				.reshape(), geoMap, layerManager);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.reshapeToolText(),
				UIMessages.INSTANCE.reshapeToolTip(), Side.LEFT));
		setEnabled(false);
	}
	
	@PostConstruct
	private void initialize() {
		add(mapControlFactory.createSelectHover(new Vector("Empty")));		
		add(createDrawTool(new Vector("Empty")));
	}
	
	@Override
	public Control createDrawTool(Layer layer) {
		return mapControlFactory.createEditingControl(ModifyFeature.RESHAPE,
				layer);
	}
	
}
