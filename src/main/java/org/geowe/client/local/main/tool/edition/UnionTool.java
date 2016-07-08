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

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.shared.jts.JTSService;
import org.geowe.client.shared.jts.JTSServiceAsync;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * Union tool
 * 
 * @author rltorres
 *
 */
@ApplicationScoped
public class UnionTool extends ButtonTool {

	private final LayerManagerWidget layerManagerWidget;
	private static final JTSServiceAsync JTS_SERVICE_ASYNC = GWT.create(JTSService.class);

	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	@Inject
	public UnionTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.unionToolText(), ImageProvider.INSTANCE
				.union32(), layerManager);
		this.layerManagerWidget = layerManager;
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.unionToolText(),
				UIMessages.INSTANCE.unionToolTip(), Side.LEFT));
		setEnabled(false);
	}

	@Override
	protected void onRelease() {

		Vector layer = (Vector) layerManagerWidget
				.getSelectedLayer(LayerManagerWidget.VECTOR_TAB);
		final VectorFeature[] selectedFeatures = layer.getSelectedFeatures();
		if (selectedFeatures == null || selectedFeatures.length != 2) {
			messageDialogBuilder.createError(
					UIMessages.INSTANCE.utAlertMessageBoxTitle(),
					UIMessages.INSTANCE.utAlertMessageBoxLabel()).show();
			return;
		}

		WKT wktFormat = new WKT();
		String wkt1 = wktFormat.write(selectedFeatures[0]);
		String wkt2 = wktFormat.write(selectedFeatures[1]);

		JTS_SERVICE_ASYNC.union(wkt1, wkt2, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {

				messageDialogBuilder.createError("Error", caught.getMessage())
						.show();
			}

			@Override
			public void onSuccess(String wktUnion) {
				VectorFeature newVectorFeature = new VectorFeature(Geometry
						.fromWKT(wktUnion));

				newVectorFeature.setAttributes(selectedFeatures[0]
						.getAttributes());
				newVectorFeature.setFeatureId(selectedFeatures[0]
						.getFeatureId());
				selectedFeatures[0].destroy();
				selectedFeatures[1].destroy();
				VectorLayer vector = (VectorLayer) UnionTool.this.layer;
				vector.addFeature(newVectorFeature);
			}
		});
	}

}
