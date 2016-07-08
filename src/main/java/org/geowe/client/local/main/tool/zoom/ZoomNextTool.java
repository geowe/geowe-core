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
package org.geowe.client.local.main.tool.zoom;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.control.NavigationHistory;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
/**
 * Zoom Next tool
 * @author geowe
 *
 */
@ApplicationScoped
public class ZoomNextTool extends ButtonTool {
	
	private final NavigationHistory navHistory;
	
	@Inject
	public ZoomNextTool(GeoMap geoMap) {		
		super(UIMessages.INSTANCE.zoomNextToolText(), ImageProvider.INSTANCE.zoomNext());
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.zoomNextToolText(),
				UIMessages.INSTANCE.zoomNextToolTip(), Side.LEFT));
		navHistory = geoMap.getNavigationHistory();		
	}

	@Override
	protected void onRelease() {
		navHistory.next();		
	}
}
