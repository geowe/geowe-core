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

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.inject.Inject;

/**
 * Geoprocess validator representa al responsable de realizar las validaciones mínimas para poder llevar a cabo
 * el análisis de las operaciones de geoprocesamiento. En función del tipo de geoproceso se realizará una validación
 * parcial o total de los datos de entrada.
 * 
 * @author jose@geowe.org
 *
 */

@ApplicationScoped
public class GeoprocessValidator {
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	private IInputGeoprocess inputGeoprocess;

	public boolean validate(final IGeoprocess geoprocess) {
		this.inputGeoprocess = geoprocess.getInputGeoprocess();
		boolean isValid = true;
		if (geoprocess.isBufferGeoprocess()) {			
			isValid = isPartiallyValid();
		} else {
			isValid = isCompletelyValid();
		}
		return isValid;
	}

	private boolean isPartiallyValid() {

		boolean valid = true;
		if (inputGeoprocess == null) {
			messageDialogBuilder.createWarning(UIMessages.INSTANCE.fail(),
					UIMessages.INSTANCE.noInputDataSpecified()).show();
			valid = false;
		} else {				
			valid = isValid(inputGeoprocess.getInputLayer());
		}

		return valid;
	}

	private boolean isCompletelyValid() {

		boolean valid = false;
		if (isPartiallyValid()) {			
			valid = isValid(inputGeoprocess.getOverlayLayer());
		} 
		return valid;
	}

	public boolean isValid(final VectorLayer layer) {
		boolean valid = true;

		if (layer == null) {
			messageDialogBuilder.createWarning(UIMessages.INSTANCE.fail(),
					UIMessages.INSTANCE.noVectorLayerSpecify()).show();
			valid = false;
		} else if (isEmptyLayer(layer)) {
			messageDialogBuilder.createWarning(UIMessages.INSTANCE.fail(),
					UIMessages.INSTANCE.emptyVectorLayer()).show();
			valid = false;
		}

		return valid;
	}
	
	private boolean isEmptyLayer(final VectorLayer layer) {
		boolean empty = false;
		final VectorFeature[] features = layer.getFeatures();
		if(features == null || features != null && features.length == 0) {
			 empty = true;			
		}
		
		return empty;
	}
}