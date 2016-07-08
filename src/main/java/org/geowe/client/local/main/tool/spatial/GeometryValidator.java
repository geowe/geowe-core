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
package org.geowe.client.local.main.tool.spatial;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.FeatureSchema;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.style.StyleFactory;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.geowe.client.shared.jts.JTSService;
import org.geowe.client.shared.jts.JTSServiceAsync;
import org.geowe.client.shared.jts.ValidationResult;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.util.JSObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * Geometry Validator representa un validador geométrico y es responsable de la detección de errores topológicos sobre los elementos 
 * de una capa vectorial.
 * 
 * @author jose@geowe.org
 *
 */

@ApplicationScoped
public class GeometryValidator {
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	private static final WKT wktFormat = new WKT();
	private static final String VALIDATED_LAYER_NAME = "ValidationResults";

	public void requestValidate(final VectorLayer layer, final LayerManagerWidget layerManager) {
		List<String> wktLayer = getWKT(layer);
		requestValidate(wktLayer, layer.getName(), layerManager);
	}

	public void requestValidate(final VectorFeature[] features, String layerName,
			final LayerManagerWidget layerManager) {
		List<String> wktLayer = getWKT(features);
		requestValidate(wktLayer, layerName, layerManager);
	}

	public void requestValidate(final String wkt, String layerName, final LayerManagerWidget layerManager) {
		List<String> wktLayer = new ArrayList<String>();
		wktLayer.add(wkt);
		requestValidate(wktLayer, layerName, layerManager);
	}

	public void requestValidate(List<String> wktLayer, final String layerName, final LayerManagerWidget layerManager) {

		final ProgressBarDialog autoMessageBox = new ProgressBarDialog(false, UIMessages.INSTANCE.processing());
		autoMessageBox.show();
		JTSServiceAsync jtsServiceAsync = GWT.create(JTSService.class);
		jtsServiceAsync.validate(wktLayer, new AsyncCallback<List<ValidationResult>>() {

			@Override
			public void onFailure(Throwable caught) {
				autoMessageBox.hide();
				messageDialogBuilder.createWarning(UIMessages.INSTANCE.fail(), "error: invalid geometry").show();
			}

			public void onSuccess(List<ValidationResult> wktElements) {
				autoMessageBox.hide();
				if (wktElements.isEmpty()) {
					messageDialogBuilder.createWarning(UIMessages.INSTANCE.utAlertMessageBoxTitle(),
									UIMessages.INSTANCE.msgNoErrors(layerName))
									.show();

					return;
				} else {
					messageDialogBuilder.createWarning(UIMessages.INSTANCE.utAlertMessageBoxTitle(),
									UIMessages.INSTANCE.msgValidationErrorsDetected(
											layerName, wktElements.size()))
									.show();
				}

				VectorLayer resultLayer = createLayerResult(layerName, wktElements);
				layerManager.addVector(resultLayer);
				updateElements(resultLayer, wktElements);
			}
		});
	}

	private VectorLayer createLayerResult(String layerName, List<ValidationResult> wktElements) {
		String normalColor = StyleFactory.stringToColour("result" + (int) (Math.random() * 0x1000000));
		final StyleMap style = StyleFactory.createStyleMap(normalColor, StyleFactory.DEFAULT_SELECTED_COLOR,
				StyleFactory.DEFAULT_HIGHLIGHTED_COLOR, null, null);

		List<String> names = new ArrayList<String>();

		int numMaxMessages = 1;
		for (ValidationResult result : wktElements) {
			if (result.getMessages().size() > numMaxMessages) {
				numMaxMessages = result.getMessages().size();
			}
		}

		for (int i = 0; i < numMaxMessages; i++) {
			names.add("message" + (i + 1));
		}

		FeatureSchema schema = new FeatureSchema(names);
		VectorLayer resultLayer = new VectorLayer(VALIDATED_LAYER_NAME + "_" + layerName, schema);
		resultLayer.setStyleMap(style);
		JSObject defaultStyle = getDefaultStyle(resultLayer);
		defaultStyle.setProperty("fillColor", "#FFFF00");
		defaultStyle.setProperty("strokeColor", "#FFFF00");
		defaultStyle.setProperty("strokeWidth", 8);
		return resultLayer;
	}

	private void updateElements(VectorLayer resultLayer, List<ValidationResult> wktElements) {
		for (ValidationResult result : wktElements) {
			VectorFeature newVectorFeature = new VectorFeature(Geometry.fromWKT(result.getWkt()));
			for (int i = 0; i < result.getMessages().size(); i++) {
				newVectorFeature.getAttributes().setAttribute("message" + (i + 1), result.getMessages().get(i));
			}
			resultLayer.addFeature(newVectorFeature);
		}
		resultLayer.redraw();
	}

	private JSObject getDefaultStyle(VectorLayer layer) {
		return layer.getStyleMap().getJSObject().getProperty("styles").getProperty("default")
				.getProperty("defaultStyle");
	}

	private List<String> getWKT(VectorLayer layer) {
		return getWKT(layer.getFeatures());
	}

	private List<String> getWKT(VectorFeature... features) {
		List<String> wktLayer = new ArrayList<String>();
		for (VectorFeature feature : features) {
			String wkt = wktFormat.write(feature);
			wktLayer.add(wkt);
		}
		return wktLayer;
	}
}