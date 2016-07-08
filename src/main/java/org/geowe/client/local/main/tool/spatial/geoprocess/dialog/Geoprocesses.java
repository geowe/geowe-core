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
package org.geowe.client.local.main.tool.spatial.geoprocess.dialog;

/**
 * Geoprocesses representa al conjunto de operaciones de geoprocesamiento necesarios para el análisis espacial entre capas 
 * 
 * @author jose@geowe.org
 *
 */

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.main.tool.spatial.geoprocess.BufferGeoprocess;
import org.geowe.client.local.main.tool.spatial.geoprocess.DifferenceGeoprocess;
import org.geowe.client.local.main.tool.spatial.geoprocess.Geoprocess;
import org.geowe.client.local.main.tool.spatial.geoprocess.IntersectGeoprocess;
import org.geowe.client.local.main.tool.spatial.geoprocess.IntersectionGeoprocess;
import org.geowe.client.local.main.tool.spatial.geoprocess.MergeGeoprocess;
import org.geowe.client.local.main.tool.spatial.geoprocess.SymDifferenceGeoprocess;
import org.geowe.client.local.main.tool.spatial.geoprocess.UnionGeoprocess;
@ApplicationScoped
public class Geoprocesses {
	@Inject
	private IntersectionGeoprocess intersectionGeoprocess;
	@Inject
	private IntersectGeoprocess intersectGeoprocess;
	@Inject
	private DifferenceGeoprocess differenceGeoprocess;	
	@Inject
	private BufferGeoprocess bufferGeoprocess;
	@Inject
	private UnionGeoprocess unionGeoprocess;
	@Inject
	private MergeGeoprocess mergeGeoprocess;	
	@Inject 
	private SymDifferenceGeoprocess symDifferenceGeoprocess;
	
	private List<Geoprocess> spatialOperations;

	public List<Geoprocess> getAll() {
		if (spatialOperations == null) {
			spatialOperations = new ArrayList<Geoprocess>();
			spatialOperations.add(intersectionGeoprocess);
			spatialOperations.add(intersectGeoprocess);
			spatialOperations.add(differenceGeoprocess);
			spatialOperations.add(symDifferenceGeoprocess);
			spatialOperations.add(bufferGeoprocess);
			spatialOperations.add(unionGeoprocess);
			spatialOperations.add(mergeGeoprocess);
		}
		return spatialOperations;
	}
}
