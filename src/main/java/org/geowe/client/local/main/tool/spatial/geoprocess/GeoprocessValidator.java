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

	public boolean validate(IGeoprocess geoprocess) {
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
					"No se han definido datos de entrada").show();
			valid = false;
		} else {
			VectorLayer inputLayer = inputGeoprocess.getInputLayer();			
			valid = isValid(inputLayer);
		}

		return valid;
	}

	private boolean isCompletelyValid() {

		boolean valid = false;
		if (isPartiallyValid()) {
			VectorLayer overlayLayer = inputGeoprocess.getOverlayLayer();
			valid = isValid(overlayLayer);
		} 
		return valid;
	}

	private boolean isValid(VectorLayer layer) {
		boolean valid = true;

		if (layer == null) {
			messageDialogBuilder.createWarning(UIMessages.INSTANCE.fail(),
					"No se ha especificado capa").show();
			valid = false;
		} else if (layer.getFeatures().length == 0) {
			messageDialogBuilder.createWarning(UIMessages.INSTANCE.fail(),
					"No existen elementos en la capa").show();
			valid = false;
		}

		return valid;
	}
}
