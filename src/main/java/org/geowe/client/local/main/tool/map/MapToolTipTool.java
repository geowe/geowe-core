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
package org.geowe.client.local.main.tool.map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.main.map.FeatureToolTipShowListener;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.SelectionTool;
import org.geowe.client.local.main.tool.ToggleTool;
import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.core.client.Style.Side;

@ApplicationScoped
public class MapToolTipTool extends ToggleTool {

	@Inject
	private FeatureToolTipShowListener featureToolTipShowListener;
	@Inject
	private SelectionTool selectionTool;


	@Inject
	public MapToolTipTool(GeoMap geoMap) {
		super(UIMessages.INSTANCE.mapToolTipToolText(), ImageProvider.INSTANCE
				.tooltip(), geoMap);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.mapToolTipToolText(),
				UIMessages.INSTANCE.mapToolTipToolInfoText(), Side.LEFT));
	}

	@Override
	protected ValueChangeHandler<Boolean> getSelectChangeHandler() {
		return new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				featureToolTipShowListener
						.setEnableMapToolTip(event.getValue());
				selectionTool.setValue(event.getValue(), true);
			}
		};
	}
}
