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

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * tool responsible for measuring the area
 * 
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class MeasureAreaTool extends ToggleTool {

	@Inject
	private MapControlFactory mapControlFactory;

	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	
	@Inject
	private BasicToolBar basicToolBar;

	@Inject
	public MeasureAreaTool(GeoMap geoMap) {
		super(UIMessages.INSTANCE.measureAreaToolText(), ImageProvider.INSTANCE
				.areaMeasure32(), geoMap);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.measureAreaToolText(),
				UIMessages.INSTANCE.measureAreaToolTip(), Side.LEFT));
	}

	@PostConstruct
	private void initialize() {
		Measure measure = mapControlFactory.createMesaureAreaControl();
		measure.addMeasureListener(getMeasureListener());
		measure.addMeasurePartialListener(getPartialListener());
		add(measure);
	}

	private MeasureListener getMeasureListener() {
		return new MeasureListener() {
			public void onMeasure(MeasureEvent eventObject) {

				messageDialogBuilder
						.createInfo(
								UIMessages.INSTANCE.measure(),
								UIMessages.INSTANCE.matDialogLabel()
										+ getReoundedMeasure(
												eventObject.getMeasure(), 3)
										+ " " + eventObject.getUnits() + "²")
						.show();
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
				+ " " + eventObject.getUnits() + "²";
	}
}
