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
package org.geowe.client.local.main.tool.measure;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.main.BasicToolBar;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.map.MapControlFactory;
import org.geowe.client.local.main.tool.ToggleTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.control.Measure;
import org.gwtopenmaps.openlayers.client.event.MeasureEvent;
import org.gwtopenmaps.openlayers.client.event.MeasureListener;
import org.gwtopenmaps.openlayers.client.event.MeasurePartialListener;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class MeasureTool extends ToggleTool {

	@Inject
	private Logger logger;
	@Inject
	private MapControlFactory mapControlFactory;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	@Inject
	private BasicToolBar basicToolBar;
	private Measure measure;

	@Inject
	public MeasureTool(GeoMap geoMap) {
		super(UIMessages.INSTANCE.measureToolText(), ImageProvider.INSTANCE
				.measureLength32(), geoMap);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.measureToolText(),
				UIMessages.INSTANCE.measureToolTip(), Side.LEFT));
	}

	@PostConstruct
	private void initialize() {
		measure = mapControlFactory.createMesaureControl();
		measure.addMeasureListener(getMeasureListener());
		measure.addMeasurePartialListener(getPartialListener());

		add(measure);
	}

	private MeasureListener getMeasureListener() {
		return new MeasureListener() {
			@Override
			public void onMeasure(MeasureEvent eventObject) {
				String measure = getToolBarMessage(eventObject);
				basicToolBar.setWhat3Words(measure);
				messageDialogBuilder
						.createInfo(
								UIMessages.INSTANCE.measure(),
								UIMessages.INSTANCE.mtDialogLabel(
										getReoundedMeasure(
												eventObject.getMeasure(), 3),
										eventObject.getUnits())).show();
			}
		};
	}

	private MeasurePartialListener getPartialListener() {
		return new MeasurePartialListener() {
			@Override
			public void onMeasurePartial(MeasureEvent eventObject) {
				String partialMeasure = getToolBarMessage(eventObject);
				basicToolBar.setWhat3Words(partialMeasure);
			}
		};
	}

	private float getReoundedMeasure(Float measure, int decimal) {
		BigDecimal bd = new BigDecimal(Float.toString(measure));
		bd = bd.setScale(decimal, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	private String getToolBarMessage(MeasureEvent eventObject) {
		return UIMessages.INSTANCE.measure()
				+ ": "
				+ getReoundedMeasure(eventObject.getMeasure(), 3)
				+ " " + eventObject.getUnits();
	}
}
