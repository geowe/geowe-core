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
import org.geowe.client.local.main.tool.map.catalog.model.BingLayerDef;
import org.geowe.client.local.messages.UICatalogMessages;
import org.gwtopenmaps.openlayers.client.layer.BingType;

/**
 * Conjunto de definiciones de las capas del proveedor Bing.
 * 
 * @author Atanasio Muñoz
 *
 */
@ApplicationScoped
public class BingLayerSet extends AbstractLayerSet{	
				
	public BingLayerSet() {
		final BingLayerDef bingAerial = new BingLayerDef();
		bingAerial.setName("Bing Aerial Layer");
		bingAerial.setDescription(UICatalogMessages.INSTANCE
				.bingAerialLayerDescription());
		bingAerial.setMapType(BingType.AERIAL);
		bingAerial.setIcon(ImageProvider.INSTANCE.bing16());

		final BingLayerDef bingHybrid = new BingLayerDef();
		bingHybrid.setName("Bing Hybrid Layer");
		bingHybrid.setDescription(UICatalogMessages.INSTANCE
				.bingHybridLayerDescription());
		bingHybrid.setMapType(BingType.HYBRID);
		bingHybrid.setIcon(ImageProvider.INSTANCE.bing16());

		final BingLayerDef bingRoad = new BingLayerDef();
		bingRoad.setName("Bing Road Layer");
		bingRoad.setDescription(UICatalogMessages.INSTANCE
				.bingRoadLayerDescription());
		bingRoad.setMapType(BingType.ROAD);
		bingRoad.setIcon(ImageProvider.INSTANCE.bing16());
		
		layers.add(bingAerial);
		layers.add(bingRoad);
		layers.add(bingHybrid);
	}
}
