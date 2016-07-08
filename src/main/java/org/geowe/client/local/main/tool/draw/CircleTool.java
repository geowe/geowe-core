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
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.map.MapControlFactory;
import org.geowe.client.local.main.tool.ToggleTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.shared.jts.JTSService;
import org.geowe.client.shared.jts.JTSServiceAsync;
import org.gwtopenmaps.openlayers.client.control.Control;
import org.gwtopenmaps.openlayers.client.control.DrawFeature;
import org.gwtopenmaps.openlayers.client.control.DrawFeature.FeatureAddedListener;
import org.gwtopenmaps.openlayers.client.control.DrawFeatureOptions;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * Draw circle tool. After draw a circle we must apply a buffer of 0 to repair
 * The geom (clockwise)
 * 
 * @author geowe.org
 *
 */
@ApplicationScoped
public class CircleTool extends ToggleTool implements DrawTool {

	@Inject
	private MapControlFactory mapControlFactory;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	private final JTSServiceAsync jtsServiceAsync = GWT
			.create(JTSService.class);

	@Inject
	public CircleTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.circleToolText(), ImageProvider.INSTANCE
				.circle(), geoMap, layerManager);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.circleToolText(),
				UIMessages.INSTANCE.drawCircleToolTip(), Side.LEFT));
		setEnabled(false);
	}

	@PostConstruct
	private void initialize() {
		add(createDrawTool(new Vector("Empty")));
		setCancelable();
	}

	@Override
	public Control createDrawTool(Layer layer) {
		DrawFeatureOptions drawFeatureOptions = new DrawFeatureOptions();
		drawFeatureOptions.onFeatureAdded(getFeatureAddedListener());

		DrawFeature drawCircleControl = mapControlFactory.createCircleControl(
				layer, drawFeatureOptions);

		return drawCircleControl;
	}

	private FeatureAddedListener getFeatureAddedListener() {
		return new FeatureAddedListener() {
			@Override
			public void onFeatureAdded(VectorFeature vectorFeature) {
				applyBufferZero(vectorFeature);
			}
		};
	}

	private void applyBufferZero(final VectorFeature vectorFeature) {
		WKT wktFormat = new WKT();
		int BufferDistance = 0;
		jtsServiceAsync.getBuffer(wktFormat.write(vectorFeature),
				BufferDistance,
				new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						messageDialogBuilder.createError(
								UIMessages.INSTANCE.fail(),
								"Error: " + caught.getMessage()).show();
					}

					public void onSuccess(String wktBuffer) {

						VectorLayer vector = (VectorLayer) getLayer();

						VectorFeature newVectorFeature = new VectorFeature(
								Geometry.fromWKT(wktBuffer));

						vector.addFeature(newVectorFeature);
						vectorFeature.destroy();
					}
				});
	}

}
