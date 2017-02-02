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
import org.geowe.client.local.main.tool.map.catalog.SampleLayerDataProvider;
import org.geowe.client.local.main.tool.map.catalog.model.SampleFileVectorLayerDef;
import org.geowe.client.local.main.tool.map.catalog.model.VectorLayerDef;
import org.geowe.client.local.messages.UICatalogMessages;

/**
 * Conjunto de definiciones de las capas de ejemplo, correspondientes a ficheros
 * de geodatos que se suministran con la aplicacion.
 * 
 * @author Atanasio Muñoz
 *
 */
@ApplicationScoped
public class SampleLayerSet extends AbstractLayerSet {
	
	public SampleLayerSet() {
		final SampleFileVectorLayerDef andaluciaLayer = new SampleFileVectorLayerDef();
		andaluciaLayer.setName(UICatalogMessages.INSTANCE
				.andalusianProvincesName());
		andaluciaLayer.setDescription(UICatalogMessages.INSTANCE
				.andalusianProvincesDescription());
		andaluciaLayer.setFormat(VectorLayerDef.KML);
		andaluciaLayer.setFile(SampleLayerDataProvider.INSTANCE
				.andaluciaLayerKml());
		andaluciaLayer.setIcon(ImageProvider.INSTANCE.layer16());
		andaluciaLayer.setEpsg("EPSG:25830");		

		final SampleFileVectorLayerDef blockLayer = new SampleFileVectorLayerDef();
		blockLayer.setName(UICatalogMessages.INSTANCE.lebrijaBlocksName());
		blockLayer.setDescription(UICatalogMessages.INSTANCE
				.lebrijaBlocksDescription());
		blockLayer.setFormat(VectorLayerDef.KML);
		blockLayer.setFile(SampleLayerDataProvider.INSTANCE
				.manzanasLayerKml());
		blockLayer.setIcon(ImageProvider.INSTANCE.layer16());
		blockLayer.setEpsg("EPSG:25830");		

		final SampleFileVectorLayerDef wetlandLayer = new SampleFileVectorLayerDef();
		wetlandLayer.setName(UICatalogMessages.INSTANCE
				.andalusianWetlandsName());
		wetlandLayer.setDescription(UICatalogMessages.INSTANCE
				.andalusianWetlandsDescription());
		wetlandLayer.setFormat(VectorLayerDef.KML);
		wetlandLayer.setFile(SampleLayerDataProvider.INSTANCE
				.humedalesLayerKml());
		wetlandLayer.setIcon(ImageProvider.INSTANCE.layer16());
		wetlandLayer.setEpsg("EPSG:25830");		
		
		layers.add(andaluciaLayer);
		layers.add(blockLayer);
		layers.add(wetlandLayer);		
	}

}
