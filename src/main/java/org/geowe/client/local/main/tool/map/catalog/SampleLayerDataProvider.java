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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * Implementacion singleton del acceso a los ficheros de geodatos locales a la
 * aplicacion. Inicialmente se han definido tres capas de ejemplo para ilustrar
 * la forma de definir dicho acceso.
 *  
 * @author Atanasio Muñoz
 *
 */
public interface SampleLayerDataProvider extends ClientBundle {

	public SampleLayerDataProvider INSTANCE = GWT
			.create(SampleLayerDataProvider.class);

	@Source("data/andalucia.kml")
	TextResource andaluciaLayerKml();
	
	@Source("data/manzanas.kml")
	TextResource manzanasLayerKml();

	@Source("data/humedales.kml")
	TextResource humedalesLayerKml();
	
}
