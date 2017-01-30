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
import org.geowe.client.local.layermanager.tool.export.ExportDataTool;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.messages.UIMessages;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * Responsible to export selected layer
 * 
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class ExportLayerTool extends ButtonTool {


	@Inject
	private ExportDataTool exportDataTool;

	@Inject
	public ExportLayerTool(GeoMap geoMap) {
		super(UIMessages.INSTANCE.exportDataToolText(), ImageProvider.INSTANCE
				.download24());
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.exportDataToolText(),
				UIMessages.INSTANCE.exportAs(), Side.LEFT));
		setEnabled(false);
	}

	@Override
	protected void onRelease() {
		exportDataTool.onClick();
	}



}
