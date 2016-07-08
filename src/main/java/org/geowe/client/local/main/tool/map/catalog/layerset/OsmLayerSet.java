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
import org.geowe.client.local.main.tool.map.catalog.model.OsmLayerDef;
import org.geowe.client.local.messages.UICatalogMessages;

/**
 * Conjunto de definiciones de las capas del proveedor OpenStreetMap.
 * 
 * @author Atanasio Muñoz
 *
 */
@ApplicationScoped
public class OsmLayerSet extends AbstractLayerSet {
	
	public OsmLayerSet() {
		final OsmLayerDef osmCycle = new OsmLayerDef();
		osmCycle.setName("OSM Cycle Map");
		osmCycle.setDescription(UICatalogMessages.INSTANCE
				.osmCycleMapDescription());
		osmCycle.setOsmMap(OsmLayerDef.OSM_CYCLEMAP);
		osmCycle.setIcon(ImageProvider.INSTANCE.osm16());

		final OsmLayerDef osmMapnik = new OsmLayerDef();
		osmMapnik.setName("OSM Mapnik");
		osmMapnik.setDescription(UICatalogMessages.INSTANCE
				.osmMapnikDescription());
		osmMapnik.setOsmMap(OsmLayerDef.OSM_MAPNIK);
		osmMapnik.setIcon(ImageProvider.INSTANCE.osm16());
		
		layers.add(osmCycle);
		layers.add(osmMapnik);
	}

}
