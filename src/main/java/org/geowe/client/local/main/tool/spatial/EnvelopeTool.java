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

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.geowe.client.shared.jts.JTSService;
import org.geowe.client.shared.jts.JTSServiceAsync;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Envelope tool representa la herramienta responsable de realizar el envolvente
 * de una geometría.
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class EnvelopeTool extends ButtonTool {
	public static final String LAYER_SUFFIX = " Envelopes";
	private final LayerManagerWidget layerManagerWidget;
	@Inject
	private ClientTaskManager taskManager;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	private static final JTSServiceAsync JTS_SERVICE_ASYNC = GWT.create(JTSService.class);
	private ProgressBarDialog progressBar;
	private String layerName;

	@Inject
	public EnvelopeTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.envelopeToolText(), ImageProvider.INSTANCE.envelope32(), layerManager);
		setToolTipConfig(createTooltipConfig(UIMessages.INSTANCE.envelopeToolText(),
				UIMessages.INSTANCE.envelopeToolTip(), Side.LEFT));
		this.layerManagerWidget = layerManager;
		setEnabled(false);
	}

	@Override
	protected void onRelease() {
		final VectorLayer selectedFeatures = (VectorLayer) layerManagerWidget
				.getSelectedLayer(LayerManagerWidget.VECTOR_TAB);

		final VectorFeature[] elements = selectedFeatures.getSelectedFeatures();
		if (elements != null && elements.length > 0) {
			calculateEnvelope(selectedFeatures, elements);
		} else {
			confirm(selectedFeatures);
		}
	}

	private void calculateEnvelope(final VectorLayer layer, final VectorFeature... elements) {
		this.layerName = layer.getName();
		progressBar = new ProgressBarDialog(true, UIMessages.INSTANCE.processing());
		progressBar.show();
		taskManager.execute(new Runnable() {

			@Override
			public void run() {
				drawEnvelope(elements);
			}

		});
	}

	private void confirm(final VectorLayer layer) {

		final ConfirmMessageBox messageBox = messageDialogBuilder.createConfirm(UIMessages.INSTANCE.dtMessageBoxTitle(),
				UIMessages.INSTANCE.envelopToolConfirmText(), ImageProvider.INSTANCE.envelope32());

		messageBox.getButton(PredefinedButton.YES).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(final SelectEvent event) {
				calculateEnvelope(layer, layer.getFeatures());
			}
		});
		messageBox.show();
	}

	private void drawEnvelope(final VectorFeature... vectorFeatures) {
		final WKT wktFormat = new WKT();
		final List<String> wktElements = new ArrayList<String>();
		for (final VectorFeature feature : vectorFeatures) {
			wktElements.add(wktFormat.write(feature));
		}

		JTS_SERVICE_ASYNC.getEnvelope(wktElements, new AsyncCallback<List<String>>() {
			public void onFailure(final Throwable caught) {
				progressBar.hide();
				messageDialogBuilder.createError(UIMessages.INSTANCE.fail(), "Error: " + caught.getMessage()).show();
			}

			public void onSuccess(final List<String> wktEnvelopes) {
				final VectorLayer vector = getEnvelopeLayer();
				for (final String wktEnvelope : wktEnvelopes) {
					final VectorFeature newVectorFeature = new VectorFeature(Geometry.fromWKT(wktEnvelope));
					vector.addFeature(newVectorFeature);
				}
				progressBar.hide();
			}
		});
	}

	private VectorLayer getEnvelopeLayer() {
		final String tmpLayerName = layerName + LAYER_SUFFIX;

		VectorLayer tmpLayer = (VectorLayer) layerManagerWidget.getVector(tmpLayerName);
		if (tmpLayer == null) {
			final VectorLayerConfig layerConfig = new VectorLayerConfig();
			layerConfig.setLayerName(tmpLayerName);
			tmpLayer = VectorLayerFactory.createEmptyVectorLayer(layerConfig);
			layerManagerWidget.addVector(tmpLayer);
		}

		return tmpLayer;
	}
}