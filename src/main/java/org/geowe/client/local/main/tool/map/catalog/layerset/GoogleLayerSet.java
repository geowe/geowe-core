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
import org.geowe.client.local.main.tool.map.catalog.model.GoogleLayerDef;
import org.geowe.client.local.messages.UICatalogMessages;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3MapType;

/**
 * Conjunto de definiciones de las capas del proveedor Google.
 * 
 * @author Atanasio Muñoz
 *
 */
@ApplicationScoped
public class GoogleLayerSet extends AbstractLayerSet {
	
	public GoogleLayerSet() {
		final GoogleLayerDef google = new GoogleLayerDef();
		google.setName("Google Satellite");
		google.setDescription(UICatalogMessages.INSTANCE
				.googleSatelliteDescription());
		google.setMapType(GoogleV3MapType.G_SATELLITE_MAP);
		google.setIcon(ImageProvider.INSTANCE.google16());


		final GoogleLayerDef googleNormal = new GoogleLayerDef();
		googleNormal.setName("Google Normal");
		googleNormal.setDescription(UICatalogMessages.INSTANCE
				.googleNormalDescription());
		googleNormal.setMapType(GoogleV3MapType.G_NORMAL_MAP);
		googleNormal.setIcon(ImageProvider.INSTANCE.google16());

		final GoogleLayerDef googleTerrain = new GoogleLayerDef();
		googleTerrain.setName("Google Terrain");
		googleTerrain.setDescription(UICatalogMessages.INSTANCE
				.googleTerrainDescription());
		googleTerrain.setMapType(GoogleV3MapType.G_TERRAIN_MAP);
		googleTerrain.setIcon(ImageProvider.INSTANCE.google16());

		final GoogleLayerDef googleHybrid = new GoogleLayerDef();
		googleHybrid.setName("Google Hibryd");
		googleHybrid.setDescription(UICatalogMessages.INSTANCE
				.googleHibrydDescription());
		googleHybrid.setMapType(GoogleV3MapType.G_HYBRID_MAP);
		googleHybrid.setIcon(ImageProvider.INSTANCE.google16());		
		
		layers.add(google);
		layers.add(googleNormal);
		layers.add(googleTerrain);
		layers.add(googleHybrid);
	}
}
