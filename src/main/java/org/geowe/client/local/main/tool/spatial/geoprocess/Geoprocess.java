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
package org.geowe.client.local.main.tool.spatial.geoprocess;

import java.util.List;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.geowe.client.shared.jts.JTSService;
import org.geowe.client.shared.jts.JTSServiceAsync;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Geoprocess Representa una operación de procesamiento espacial. Realiza el
 * análisis de una información espacial de entrada, y como resultado genera una
 * información espacial de salida
 * 
 * @author jose@geowe.org
 *
 */
public abstract class Geoprocess implements IGeoprocess {
	private int id;
	private String name;
	private ProgressBarDialog progressBarDialog;
	private IInputGeoprocess input;
	protected final MessageDialogBuilder messageDialogBuilder = new MessageDialogBuilder();
	protected final JTSServiceAsync jtsServiceAsync = GWT.create(JTSService.class);
	
	public IInputGeoprocess getInputGeoprocess() {
		return input;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setInputGeoprocess(final IInputGeoprocess inputGeoprocess) {
		this.input = inputGeoprocess;
	}	
	
	protected ProgressBarDialog getProgressBar() {
		final ProgressBarDialog progressBarDialog = new ProgressBarDialog(false,
				UIMessages.INSTANCE.processing());
		return progressBarDialog;
	}
		
	public void execute() {
		progressBarDialog = getProgressBar();
		progressBarDialog.show();
		process();
	}
	
	protected abstract void process();
	
	public void finishProgressbar() {
		if(progressBarDialog != null) {
			progressBarDialog.hide();
		}
	}
	
	public IInputGeoprocess getInput() {
		return input;
	}

	public boolean isBufferGeoprocess() {
		boolean isBuffer = false;
		if (this instanceof BufferGeoprocess) {
			isBuffer = true;
		}
		
		return isBuffer;
	}				
	
	protected VectorLayer getResultLayer(final List<String> elements) {
		final VectorLayerConfig layerConfig = new VectorLayerConfig();
		layerConfig.setLayerName("geoprocess_result");		
		final VectorLayer layer = VectorLayerFactory.createEmptyVectorLayer(layerConfig);

		for (final String wkt : elements) {			
			layer.addFeature(createVectorFeature(wkt));
		}

		return layer;
	}
	
	private VectorFeature createVectorFeature(final String wkt) {
		return new VectorFeature(
				Geometry.fromWKT(wkt));
	}
	
	protected AsyncCallback<List<String>> getDefaultAsyncCallback() {
		
		return new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(final Throwable caught) {
				finishProgressbar();		
				onError(caught.getMessage());				
			}

			public void onSuccess(final List<String> wktElements) {
				finishProgressbar();
				onSucess(getResultLayer(wktElements));				
			}
		};
	}	
}
