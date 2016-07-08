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
import org.geowe.client.local.messages.UIMessages;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Direction;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.fx.client.FxElement;

@ApplicationScoped
public class LayerManagerTool extends ButtonTool {
	
	@Inject
	private LayerManagerWidget layerMangerWidget;
	
	@Inject
	public LayerManagerTool(GeoMap geoMap) {		
		super(UIMessages.INSTANCE.layerManagerToolText(),
				ImageProvider.INSTANCE.layerIcon());
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.layerManagerToolText(),
				UIMessages.INSTANCE.layerManagerToolTip(), Side.LEFT));
						
	}

	@Override
	protected void onRelease() {
		if (layerMangerWidget.asWidget().isVisible()) {
			// TODO para moverlo a la posición original
			// Rectangle bounds = MenuPanelWidget.this.getBounds();
			// layerTreeWidget.asWidget().getElement().<FxElement>
			// cast().setXY(bounds.getX() + 50, bounds.getY() + 50, new
			// Fx());
			layerMangerWidget.asWidget().getElement().<FxElement> cast()
					.slideOut(Direction.LEFT);
		} else {
			layerMangerWidget.asWidget().setVisible(true);
			layerMangerWidget.asWidget().getElement().<FxElement> cast()
					.slideIn(Direction.RIGHT);
		}
	}
	
	
	
	
	
	
}
