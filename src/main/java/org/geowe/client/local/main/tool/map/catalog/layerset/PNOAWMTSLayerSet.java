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
import org.geowe.client.local.main.tool.map.catalog.model.WmtsLayerDef;

/**
 * Imágenes de satélite Spot y ortofotos PNOA
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class PNOAWMTSLayerSet extends AbstractLayerSet {
	
	public PNOAWMTSLayerSet() {
		
		WmtsLayerDef wmtsLayer = new WmtsLayerDef();
		wmtsLayer.setUrl("http://www.ign.es/wmts/pnoa-ma?");
		wmtsLayer.setLayerName("OI.OrthoimageCoverage");
		wmtsLayer.setTileMatrixSet("GoogleMapsCompatible");
		wmtsLayer.setName("Imágenes de satélite Spot y ortofotos PNOA");
		wmtsLayer.setFormat("image/png");
		wmtsLayer.setIcon(ImageProvider.INSTANCE.ign16());	
		wmtsLayer.setDescription("Imagen de satélite Spot5 a escalas menores de 1:70 000 y las ortofotografías PNOA de máxima actualidad para escalas mayores, para toda España");
		wmtsLayer.setAttribution("«PNOA cedido por © Instituto Geográfico Nacional de España»");
		layers.add(wmtsLayer);
	}
}
