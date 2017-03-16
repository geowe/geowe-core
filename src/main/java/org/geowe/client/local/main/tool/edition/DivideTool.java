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
package org.geowe.client.local.main.tool.edition;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ToggleTool;
import org.geowe.client.local.main.tool.draw.DrawTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.geowe.client.shared.jts.JTSService;
import org.geowe.client.shared.jts.JTSServiceAsync;
import org.gwtopenmaps.openlayers.client.control.Control;
import org.gwtopenmaps.openlayers.client.control.DrawFeature;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureAddedListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.geometry.Polygon;
import org.gwtopenmaps.openlayers.client.handler.PathHandler;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.util.Attributes;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * Divide geom tool
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class DivideTool extends ToggleTool implements DrawTool {
	private boolean enableEvent = true;
	@Inject
	private Logger logger;
	@Inject
	private ClientTaskManager taskManager;

	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	private final JTSServiceAsync jtsServiceAsync = GWT
			.create(JTSService.class);
	private final ProgressBarDialog autoMessageBox = new ProgressBarDialog(
			false, UIMessages.INSTANCE.processing());

	@Inject
	public DivideTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.divideToolText(), ImageProvider.INSTANCE
				.divide32(), geoMap, layerManager);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.divideToolText(),
				UIMessages.INSTANCE.divideToolTip(), Side.LEFT));
		setEnabled(false);
	}

	@PostConstruct
	private void initialize() {
		add(createDrawTool(new Vector("Empty")));
	}

	@Override
	public Control createDrawTool(final Layer layer) {
		return new DrawFeature((Vector) layer, new PathHandler());
	}

	// Se invoca desde el LayerManager
	public VectorFeatureAddedListener getFeatureAddedListener(final Vector layer) {
		return new VectorFeatureAddedListener() {

			@Override
			public void onFeatureAdded(FeatureAddedEvent eventObject) {

				if (getValue() && enableEvent) {

					final VectorFeature lineVectorFeature = eventObject
							.getVectorFeature();

					taskManager.execute(new Runnable() {
						@Override
						public void run() {
							detectPolygonIntersected(lineVectorFeature, layer);

						}
					});
				}
			}
		};
	}

	private Attributes getNewAttribute(VectorFeature feature) {
		Attributes attributes = new Attributes();
		for (String name : feature.getAttributes().getAttributeNames()) {
			attributes.setAttribute(name, ""
					+ feature.getAttributes().getAttributeAsString(name));
		}

		return attributes;
	}

	private void detectPolygonIntersected(
			final VectorFeature lineVectorFeature, final Vector layer) {
		autoMessageBox.show();

		final WKT wktFormat = new WKT();
		jtsServiceAsync.getBuffer(wktFormat.write(lineVectorFeature), 0.5,
				new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						autoMessageBox.hide();
						showAlert(caught.getMessage());
					}

					public void onSuccess(String wktBuffer) {

						VectorFeature lineBufferVectorFeature = new VectorFeature(
								Geometry.fromWKT(wktBuffer));

						Polygon lineToPolygon = Polygon
								.narrowToPolygon(lineBufferVectorFeature
										.getGeometry().getJSObject());

						List<VectorFeature> featuresIntersected = new ArrayList<VectorFeature>();
						for (VectorFeature feature : layer.getFeatures()) {

							if (isPolygon(feature.getGeometry())
									&& lineToPolygon.intersects(feature
											.getGeometry())) {
								featuresIntersected.add(feature);
							} else if (isLineString(feature.getGeometry())
									&& lineToPolygon.intersects(feature
											.getGeometry())) {
								featuresIntersected.add(feature);
							}
						}

						if (!featuresIntersected.isEmpty()) {
							if (featuresIntersected.size() == 1
									&& (wktFormat.write(
											featuresIntersected.get(0)).equals(
											wktFormat.write(lineVectorFeature)))) {
								lineVectorFeature.destroy();
								autoMessageBox.hide();
								showAlert(UIMessages.INSTANCE
										.noIntersectPolygon());

							} else {
								for (VectorFeature intersectedVectorFeature : featuresIntersected) {
									if (!wktFormat.write(
											intersectedVectorFeature).equals(
											wktFormat.write(lineVectorFeature))) {
										applyDivide(lineVectorFeature,
												intersectedVectorFeature);
									}
								}
							}

						} else {
							lineVectorFeature.destroy();
							autoMessageBox.hide();
							showAlert(UIMessages.INSTANCE.noIntersectPolygon());
						}

					}
				});
	}

	private void applyDivide(final VectorFeature lineVectorFeature,
			final VectorFeature geometryIntersectedVectorFeature) {

		WKT wktFormat = new WKT();
		String wktLine = wktFormat.write(lineVectorFeature);
		String wktPolygon = wktFormat.write(geometryIntersectedVectorFeature);
		final Vector vector = (Vector) DivideTool.this.getLayer();

		jtsServiceAsync.divide(wktLine, wktPolygon,
				new AsyncCallback<List<String>>() {

					@Override
					public void onFailure(Throwable caught) {
						autoMessageBox.hide();
						lineVectorFeature.destroy();
						showAlert(caught.getMessage());
					}

					public void onSuccess(List<String> wkts) {

						enableEvent = false;
						logger.info("Geometrías divididas: " + wkts.size());
						for (String wkt : wkts) {
							VectorFeature newVectorFeature = new VectorFeature(
									Geometry.fromWKT(wkt));
							newVectorFeature
									.setAttributes(getNewAttribute(geometryIntersectedVectorFeature));

							vector.addFeature(newVectorFeature);
						}
						enableEvent = true;
						vector.removeFeature(geometryIntersectedVectorFeature);
						geometryIntersectedVectorFeature.destroy();
						vector.removeFeature(lineVectorFeature);
						lineVectorFeature.destroy();
						autoMessageBox.hide();
					}
				});
	}

	private boolean isPolygon(Geometry geometry) {
		return geometry
				.getClassName()
				.equals(org.gwtopenmaps.openlayers.client.geometry.Geometry.POLYGON_CLASS_NAME);
	}

	private boolean isLineString(Geometry geometry) {
		return geometry
				.getClassName()
				.equals(org.gwtopenmaps.openlayers.client.geometry.Geometry.LINESTRING_CLASS_NAME);
	}

	private void showAlert(String errorMsg) {
		messageDialogBuilder.createError(UIMessages.INSTANCE.warning(),
				errorMsg).show();
	}
}
