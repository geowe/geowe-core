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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.main.tool.map.catalog.layerset.BingLayerSet;
import org.geowe.client.local.main.tool.map.catalog.layerset.EsriLayerSet;
import org.geowe.client.local.main.tool.map.catalog.layerset.GoogleLayerSet;
import org.geowe.client.local.main.tool.map.catalog.layerset.IgnLayerSet;
import org.geowe.client.local.main.tool.map.catalog.layerset.MiscWmsLayerSet;
import org.geowe.client.local.main.tool.map.catalog.layerset.OsmLayerSet;
import org.geowe.client.local.main.tool.map.catalog.layerset.PNOAWMTSLayerSet;
import org.geowe.client.local.main.tool.map.catalog.layerset.SampleLayerSet;

/**
 * Catalogo de capas oficial de la aplicacion GeoWE que ofrece al usuario las capas
 * de referencia mas demandadas, pertenecientes a los principales proveedores de
 * informacion SIG. Se han definido los distintos conjuntos de definiciones de capas
 * por proveedor en forma de LayerSet.
 * 
 * @author Atanasio Muñoz
 * @author jmmluna
 * @since 24/08/2016
 * Se añade al catálogo la capa WMTS del PNOA
 *
 */
@ApplicationScoped
public class AppLayerCatalog extends AbstractLayerCatalog {
	public static final String GOOGLE_SATELLITE = "Google Satellite";
			
	@Inject
	private BingLayerSet bingLayerSet;
	@Inject
	private EsriLayerSet esriLayerSet;
	@Inject
	private GoogleLayerSet googleLayerSet;
	@Inject
	private IgnLayerSet ignLayerSet;
	@Inject
	private OsmLayerSet osmLayerSet;
	@Inject
	private MiscWmsLayerSet miscWmsLayerSet;
	@Inject
	private SampleLayerSet sampleLayeSet;
	@Inject
	private PNOAWMTSLayerSet pnoaWMTSLayerSet;

	@PostConstruct
	private void initializeCatalog() {
		addLayers(googleLayerSet);
		addLayers(bingLayerSet);
		addLayers(osmLayerSet);
		addLayers(esriLayerSet);
		addLayers(ignLayerSet);
		addLayers(pnoaWMTSLayerSet);
		addLayers(miscWmsLayerSet);
		addLayers(sampleLayeSet);			
	}
}
