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
package org.geowe.client.local.main.map;

import java.util.Collections;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.main.tool.draw.PolygonHandlerOptions;
import org.geowe.client.local.main.tool.draw.PolygonHandlerOptions.HoleModifier;
import org.gwtopenmaps.openlayers.client.RenderIntent;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.control.DrawFeature;
import org.gwtopenmaps.openlayers.client.control.DrawFeatureOptions;
import org.gwtopenmaps.openlayers.client.control.Graticule;
import org.gwtopenmaps.openlayers.client.control.GraticuleOptions;
import org.gwtopenmaps.openlayers.client.control.Measure;
import org.gwtopenmaps.openlayers.client.control.MeasureOptions;
import org.gwtopenmaps.openlayers.client.control.ModifyFeature;
import org.gwtopenmaps.openlayers.client.control.ModifyFeature.OnModificationStartListener;
import org.gwtopenmaps.openlayers.client.control.ModifyFeatureOptions;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeature.ClickFeatureListener;
import org.gwtopenmaps.openlayers.client.control.SelectFeature.UnselectFeatureListener;
import org.gwtopenmaps.openlayers.client.control.SelectFeatureOptions;
import org.gwtopenmaps.openlayers.client.control.TransformFeature;
import org.gwtopenmaps.openlayers.client.control.TransformFeatureOptions;
import org.gwtopenmaps.openlayers.client.handler.PathHandler;
import org.gwtopenmaps.openlayers.client.handler.PathHandlerOptions;
import org.gwtopenmaps.openlayers.client.handler.PolygonHandler;
import org.gwtopenmaps.openlayers.client.handler.RegularPolygonHandler;
import org.gwtopenmaps.openlayers.client.handler.RegularPolygonHandlerOptions;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.symbolizer.LineSymbolizer;
import org.gwtopenmaps.openlayers.client.symbolizer.LineSymbolizerOptions;
import org.gwtopenmaps.openlayers.client.symbolizer.TextSymbolizer;
import org.gwtopenmaps.openlayers.client.symbolizer.TextSymbolizerOptions;

@ApplicationScoped
public class MapControlFactory {

	@Inject
	private FeatureToolTipShowListener featureToolTipShowListener;
	@Inject
	private FeatureToolTipHideListener featureToolTipHideListener;

	private SelectFeatureOptions selectFeatureHoverOptions;
	private SelectFeature boxSelectFeature;

	public SelectFeature createSelectHover(Layer layer) {
		createSelectHoverOptions();

		SelectFeature selectHoverFeature = new SelectFeature((Vector) layer,
				selectFeatureHoverOptions);
		selectHoverFeature.setClickOut(false);
		selectHoverFeature
				.addFeatureHighlightedListener(featureToolTipShowListener);
		selectHoverFeature
				.addFeatureUnhighlightedListener(featureToolTipHideListener);
		selectHoverFeature.setToggle(true);

		return selectHoverFeature;
	}

	private void createSelectHoverOptions() {
		if (selectFeatureHoverOptions == null) {
			selectFeatureHoverOptions = new SelectFeatureOptions();
			selectFeatureHoverOptions.setRenderIntent(RenderIntent.TEMPORARY);
			selectFeatureHoverOptions.setHighlightOnly(true);
			selectFeatureHoverOptions.setHover();
		}
	}

	public SelectFeature createClickControl(Layer layer,
			UnselectFeatureListener unSelectListener,
			ClickFeatureListener clickFeatureListener) {

		SelectFeatureOptions clickSelectFeatureOptions = new SelectFeatureOptions();
		clickSelectFeatureOptions.onUnSelect(unSelectListener);
		clickSelectFeatureOptions.clickFeature(clickFeatureListener);
		clickSelectFeatureOptions.setToggle();
		clickSelectFeatureOptions.setMultiple();

		SelectFeature clickSelectFeature = new SelectFeature((Vector) layer,
				clickSelectFeatureOptions);

		clickSelectFeature.setClickOut(true);

		return clickSelectFeature;
	}

	public SelectFeature createSelectionControl(Layer layer) {

		SelectFeature clickSelectFeature = new SelectFeature((Vector) layer);

		clickSelectFeature.setClickOut(true);
		clickSelectFeature.setToggle(true);
		clickSelectFeature.setMultiple(true);
		clickSelectFeature.setToggleKey("ctrlKey");
		clickSelectFeature.setMultipleKey("shiftKey");

		return clickSelectFeature;
	}

	public SelectFeature createBoxSelect(Layer layer) {

		if (boxSelectFeature == null) {
			SelectFeatureOptions boxSelectFeatureOptions = new SelectFeatureOptions();
			boxSelectFeatureOptions.setBox(true);
			boxSelectFeature = new SelectFeature((Vector) layer,
					boxSelectFeatureOptions);
			boxSelectFeature.setClickOut(true);
			boxSelectFeature.setToggle(true);
			boxSelectFeature.setMultiple(false);
			boxSelectFeature.setToggleKey("ctrlKey");
			boxSelectFeature.setMultipleKey("shiftKey");
		} else {
			Vector[] vectors = Collections.singleton(layer).toArray(
					new Vector[] {});

			boxSelectFeature.setLayers(vectors);
		}

		return boxSelectFeature;
	}

	public Measure createMesaureControl() {
		MeasureOptions measOpts = new MeasureOptions();
		measOpts.setPersist(true);
		measOpts.setGeodesic(true);

		final Style measureStyle = new Style();
		measureStyle.setFillColor("white");
		measureStyle.setGraphicName("x");
		measureStyle.setPointRadius(4);
		measureStyle.setStrokeWidth(3);
		measureStyle.setStrokeColor("#66FFFF");
		measureStyle.setStrokeDashstyle("dash");

		StyleMap measureStyleMap = new StyleMap(measureStyle);
		PathHandlerOptions phOpt = new PathHandlerOptions();
		phOpt.setStyleMap(measureStyleMap);
		measOpts.setHandlerOptions(phOpt);

		return new Measure(new PathHandler(), measOpts);
	}

	public Measure createMesaureAreaControl() {
		MeasureOptions measOpts = new MeasureOptions();
		measOpts.setPersist(true);
		measOpts.setGeodesic(true);

		final Style measureStyle = new Style();
		measureStyle.setFillColor("white");
		measureStyle.setFillOpacity(0.3);
		measureStyle.setGraphicName("x");
		measureStyle.setPointRadius(4);
		measureStyle.setStrokeWidth(3);
		measureStyle.setStrokeColor("#66FFFF");
		measureStyle.setStrokeDashstyle("dash");

		StyleMap measureStyleMap = new StyleMap(measureStyle);
		PolygonHandlerOptions polygonOpt = new PolygonHandlerOptions();
		polygonOpt.setStyleMap(measureStyleMap);
		measOpts.setHandlerOptions(polygonOpt);

		return new Measure(new PolygonHandler(), measOpts);
	}

	public DrawFeature createCircleControl(Layer layer,
			DrawFeatureOptions drawFeatureOptions) {
		RegularPolygonHandlerOptions handlerOptions = new RegularPolygonHandlerOptions();
		handlerOptions.setSides(30);
		handlerOptions.setSnapAngle(0);
		handlerOptions.setIrregular(false);

		drawFeatureOptions.setHandlerOptions(handlerOptions);

		DrawFeature drawCircleFeatureControl = new DrawFeature((Vector) layer,
				new RegularPolygonHandler(), drawFeatureOptions);

		return drawCircleFeatureControl;
	}

	public DrawFeature createRegularPoligonControl(Layer layer) {
		RegularPolygonHandlerOptions boxHandlerOptions = new RegularPolygonHandlerOptions();
		boxHandlerOptions.setIrregular(true);

		DrawFeature drawRegularPolygonControl = new DrawFeature((Vector) layer,
				new RegularPolygonHandler());
		((RegularPolygonHandler) drawRegularPolygonControl.getHandler())
				.setOptions(boxHandlerOptions);

		return drawRegularPolygonControl;
	}

	public DrawFeature createHolePolygonControl(Layer layer) {
		DrawFeatureOptions drawFeatureOptions = new DrawFeatureOptions();
		PolygonHandlerOptions polygonHandlerOptions = new PolygonHandlerOptions();
		polygonHandlerOptions.setHoleModifier(HoleModifier.ctrlKey);
		drawFeatureOptions.setHandlerOptions(polygonHandlerOptions);

		DrawFeature drawPolygonControl = new DrawFeature((Vector) layer,
				new PolygonHandler(), drawFeatureOptions);
		return drawPolygonControl;
	}

	public Graticule createGraticule() {
		// Adds the graticule control
		LineSymbolizerOptions lineOptions = new LineSymbolizerOptions();
		lineOptions.setStrokeColor("#ccccff");
		lineOptions.setStrokeOpacity(0.5);
		lineOptions.setStrokeWidth(1);

		LineSymbolizer line = new LineSymbolizer(lineOptions);

		TextSymbolizerOptions textOptions = new TextSymbolizerOptions();
		textOptions.setFontSize("9px");

		TextSymbolizer text = new TextSymbolizer(textOptions);

		final GraticuleOptions grtOptions = new GraticuleOptions();

		grtOptions.setTargetSize(200);
		grtOptions.setLabelled(true);
		grtOptions.setLineSymbolyzer(line);
		grtOptions.setLabelSymbolizer(text);
		return new Graticule(grtOptions);
	}

	public ModifyFeature createEditingControl(int mode, Layer layer) {
		ModifyFeatureOptions modifyFeatureOptions = new ModifyFeatureOptions();

		ModifyFeature modifyFeature = new ModifyFeature((Vector) layer,
				modifyFeatureOptions);
		modifyFeature.setVirtualStyle(getEditionStyle());
		modifyFeature.setMode(mode);
		return modifyFeature;
	}

	private static Style getEditionStyle() {
		Style style = new Style();
		style.setStrokeColor("#000000");
		style.setStrokeWidth(3);
		style.setFillColor("yellow");
		style.setFillOpacity(0.5);
		style.setPointRadius(5);
		style.setStrokeOpacity(1.0);
		style.setGraphicName("triangle");
		style.setLabel("");

		return style;
	}

	public TransformFeature createTransformTool(Vector layer) {
		final TransformFeatureOptions transformFeatureOptions = new TransformFeatureOptions();

		transformFeatureOptions.setRotate(true);
		transformFeatureOptions.setRotation(0);
		transformFeatureOptions.setRederIntent(RenderIntent.TEMPORARY
				.getValue());
		transformFeatureOptions
				.setRotationHandleSymbolizer(createRotationStyle());

		TransformFeature transformFeature = new TransformFeature(layer,
				transformFeatureOptions);

		return transformFeature;
	}

	private Style createRotationStyle() {
		Style rotationHandlerStyle = new Style();
		rotationHandlerStyle.setFillColor("#eeeeee");
		rotationHandlerStyle.setFillOpacity(1);
		rotationHandlerStyle.setPointRadius(12);
		rotationHandlerStyle.setStrokeColor("yellow");

		return rotationHandlerStyle;
	}

}
