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
import org.geowe.client.local.main.BasicToolBar;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.map.MapControlFactory;
import org.geowe.client.local.main.tool.ToggleTool;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.control.Control;
import org.gwtopenmaps.openlayers.client.control.DrawFeature;
import org.gwtopenmaps.openlayers.client.control.Measure;
import org.gwtopenmaps.openlayers.client.event.MeasureEvent;
import org.gwtopenmaps.openlayers.client.event.MeasurePartialListener;
import org.gwtopenmaps.openlayers.client.handler.PolygonHandler;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * Draw polygon tool
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class PolygonTool extends ToggleTool implements DrawTool {

	@Inject
	private BasicToolBar basicToolBar;

	@Inject
	private MapControlFactory mapControlFactory;

	@Inject
	public PolygonTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.polygonToolText(), ImageProvider.INSTANCE.polygon(), geoMap, layerManager);
		setToolTipConfig(createTooltipConfig(UIMessages.INSTANCE.polygonToolText(),
				UIMessages.INSTANCE.drawPolygonToolTip(), Side.LEFT));
		setEnabled(false);
	}

	@PostConstruct
	private void initialize() {
		add(createDrawTool(new Vector("Empty")));
		add(createMeasure());
		setCancelable();
		setUndoadable();
		setRedoadable();
	}

	@Override
	public Control createDrawTool(Layer layer) {
		DrawFeature drawFeature = new DrawFeature((Vector) layer, new PolygonHandler());

		return drawFeature;
	}

	private Control createMeasure() {
		Measure measure = mapControlFactory.createNonPersistImmediateMeasure();
		measure.addMeasurePartialListener(new MeasurePartialListener() {
			@Override
			public void onMeasurePartial(MeasureEvent eventObject) {
				basicToolBar.setWhat3Words(String.valueOf(eventObject.getMeasure())
						+ " " + eventObject.getUnits());
			}
		});
		return measure;
	}

}
