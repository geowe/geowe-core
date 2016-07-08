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
package org.geowe.client.local.main.tool.map.catalog;

import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.jboss.errai.ioc.client.container.IOC;

/**
 * Clase de utilidad que permite insertar cargar una nueva capa en el mapa
 * desde cualquier contexto de la aplicacion.
 * 
 * @author geowe.org
 *
 */
public final class LayerLoader {

	private static LayerManagerWidget layerManagerWidget = IOC.getBeanManager()
			.lookupBean(LayerManagerWidget.class).getInstance();

	private LayerLoader() {
	}

	/**
	 * Add a layer to the map
	 * 
	 * @param layer
	 */
	public static void load(Layer layer) {
		if (layer != null) {
			if (layer instanceof Vector) {
				layerManagerWidget.addVector(layer);
				layerManagerWidget.setSelectedLayer(
						LayerManagerWidget.VECTOR_TAB, layer);
			} else {
				layerManagerWidget.addRaster(layer);
			}
		}
	}


}
