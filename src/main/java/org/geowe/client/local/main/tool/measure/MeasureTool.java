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
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.map.MapControlFactory;
import org.geowe.client.local.main.tool.ToggleTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.control.Measure;
import org.gwtopenmaps.openlayers.client.event.MeasureEvent;
import org.gwtopenmaps.openlayers.client.event.MeasureListener;

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
	private MapControlFactory mapControlFactory;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;

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
		Measure measure = mapControlFactory.createMesaureControl();
		measure.addMeasureListener(getMeasureListener());
		add(measure);
	}

	private MeasureListener getMeasureListener() {
		return new MeasureListener() {
			public void onMeasure(MeasureEvent eventObject) {

				messageDialogBuilder
						.createInfo(
								UIMessages.INSTANCE.measure(),
								UIMessages.INSTANCE.mtDialogLabel(
										getReoundedMeasure(
												eventObject.getMeasure(), 3),
										eventObject.getUnits())).show();
			}

			private float getReoundedMeasure(Float measure, int decimal) {
				BigDecimal bd = new BigDecimal(Float.toString(measure));
				bd = bd.setScale(decimal, BigDecimal.ROUND_HALF_UP);
				return bd.floatValue();
			}
		};
	}
}
