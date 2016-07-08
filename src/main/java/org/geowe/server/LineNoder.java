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

package org.geowe.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;

/**
 * LineNoder representa la entidad responsable de nodar las líneas/segmentos que forman una geometría.
 * 
 * @author jose@geowe.org
 *
 */

public class LineNoder {
	
	public Collection<Geometry> nodeLines(final Collection<Geometry> lines) {
		final GeometryFactory gf = new GeometryFactory();
		final Geometry linesGeom = gf.createMultiLineString(GeometryFactory
				.toLineStringArray(lines));
		Geometry unionInput = gf.createMultiLineString(null);
		final Geometry point = extractPoint(lines);
		if (point != null)
			unionInput = point;

		final Geometry noded = linesGeom.union(unionInput);
		final List<Geometry> nodedList = new ArrayList<Geometry>();
		nodedList.add(noded);
		return nodedList;
	}

	public Geometry extractPoint(final Collection<Geometry> lines) {
		Geometry point = null;
		for (final Iterator<Geometry> i = lines.iterator(); i.hasNext();) {
			final Geometry geometry = i.next();
			if (!geometry.isEmpty()) {
				point = geometry.getFactory().createPoint(geometry.getCoordinate());
			}
		}
		return point;
	}
	
	protected Collection<Geometry> polygonizer(final Collection<Geometry> nodedLines) {
		final Polygonizer polygonizer = new Polygonizer();		
		polygonizer.add(nodedLines);
		return polygonizer.getPolygons();
	}
}