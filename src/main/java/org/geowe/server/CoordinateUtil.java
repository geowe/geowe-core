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
import java.util.List;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

/**
 * CoordinateUtil representa una entidad de utilidad para el trabajo con coordenadas y es responsable de realizar las operaciones
 * con conjuntos de coordenadas de una geometría
 * 
 * @author jose@geowe.org
 *
 */
public class CoordinateUtil {

	public void addCoordinateArrays(final Geometry geometry, final boolean orientPolygons,
			final List<Coordinate[]> coordArrayList) {
		if (geometry.getDimension() <= 0) {
			return;
		} else if (geometry instanceof LineString) {
			final LineString l = (LineString) geometry;
			coordArrayList.add(l.getCoordinates());
		} else if (geometry instanceof Polygon) {
			final Polygon poly = (Polygon) geometry;
			Coordinate[] shell = poly.getExteriorRing().getCoordinates();

			if (orientPolygons) {
				shell = ensureOrientation(CGAlgorithms.CLOCKWISE, shell);
			}

			coordArrayList.add(shell);

			for (int numRing = 0; numRing < poly.getNumInteriorRing(); numRing++) {
				Coordinate[] hole = poly.getInteriorRingN(numRing).getCoordinates();

				if (orientPolygons) {
					hole = ensureOrientation(
							CGAlgorithms.COUNTERCLOCKWISE, hole);
				}

				coordArrayList.add(hole);
			}
		} else if (geometry instanceof GeometryCollection) {
			final GeometryCollection gc = (GeometryCollection) geometry;

			for (int numGeom = 0; numGeom < gc.getNumGeometries(); numGeom++) {
				addCoordinateArrays(gc.getGeometryN(numGeom), orientPolygons,
						coordArrayList);
			}
		}
	}

	public Coordinate[] ensureOrientation(
			final int desiredOrientation, final Coordinate... coord) {
		if (coord.length == 0) {
			return coord;
		}

		final int orientation = CGAlgorithms.isCCW(coord) ? CGAlgorithms.COUNTERCLOCKWISE
				: CGAlgorithms.CLOCKWISE;

		if (orientation != desiredOrientation) {
			final Coordinate[] reverse = (Coordinate[]) coord.clone();
			reverse(reverse);

			return reverse;
		}

		return coord;
	}

	public List<Coordinate[]> toCoordinateArrays(final Geometry geometry,
			final boolean orientPolygons) {
		final List<Coordinate[]> coordArrayList = new ArrayList<Coordinate[]>();
		addCoordinateArrays(geometry, orientPolygons, coordArrayList);

		return coordArrayList;
	}

	public void reverse(final Coordinate... coord) {
		final int last = coord.length - 1;
		final int mid = last / 2;

		for (int i = 0; i <= mid; i++) {
			final Coordinate tmp = coord[i];
			coord[i] = coord[last - i];
			coord[last - i] = tmp;
		}
	}
}
