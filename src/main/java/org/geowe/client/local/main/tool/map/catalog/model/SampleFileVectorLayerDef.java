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
package org.geowe.client.local.main.tool.map.catalog.model;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.gwtopenmaps.openlayers.client.layer.Layer;

import com.google.gwt.resources.client.TextResource;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

/**
 * Definicion de una capa vectorial que se construye a partir de un fichero
 * local a la aplicacion. Esta destinada unicamente a las capas de ejemplo
 * del SampleLayerSet. 
 * 
 * @author Atanasio Muñoz
 *
 */
public class SampleFileVectorLayerDef extends VectorLayerDef {
	private static final long serialVersionUID = -5486959179151779312L;

	private TextResource file;
	
	public SampleFileVectorLayerDef() {
		super();
	}
	
	@Override
	public Layer getLayer() {
		Layer layer = null;
		
		try {
			final VectorLayerConfig layerConfig = getVectorLayerConfig();
			layerConfig.setGeoDataString(file.getText());
			layer = VectorLayerFactory.createVectorLayerFromGeoData(layerConfig);
		} catch (Exception e) {
			AlertMessageBox messageBox = new AlertMessageBox(
					UIMessages.INSTANCE.warning(),
					UIMessages.INSTANCE.gditAlertMessage());
			messageBox.show();
		}
		
		return layer;
	}

	public TextResource getFile() {
		return file;
	}

	public void setFile(TextResource file) {
		this.file = file;
	}
}
