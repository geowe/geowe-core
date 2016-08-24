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
package org.geowe.client.local.main.tool.map.catalog.layerset;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.main.tool.map.catalog.model.WmsLayerDef;
import org.geowe.client.local.messages.UICatalogMessages;

/**
 * Conjunto de definiciones de las capas del proveedor IGN (Instituto
 * Geografico Nacional de España).
 * 
 * @author Atanasio Muñoz
 *
 */
@ApplicationScoped
public class IgnLayerSet extends AbstractLayerSet {
	
	public IgnLayerSet() {
		final WmsLayerDef ignBaseAll = new WmsLayerDef();
		ignBaseAll.setName(UICatalogMessages.INSTANCE.ignBaseName());
		ignBaseAll.setDescription(UICatalogMessages.INSTANCE
				.ignBaseDescription());
		ignBaseAll
				.setUrl("http://www.ign.es/wms-inspire/ign-base?SERVICE=WMS&");
		ignBaseAll.setLayerName("IGNBaseTodo");
		ignBaseAll.setFormat("image/png");
		ignBaseAll.setEpsg("EPSG:3857");
		ignBaseAll.setIcon(ImageProvider.INSTANCE.ign16());	
		
		layers.add(ignBaseAll);
	}
}
