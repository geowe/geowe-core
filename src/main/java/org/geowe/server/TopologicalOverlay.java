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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.overlay.OverlayOp;
import com.vividsolutions.jts.precision.EnhancedPrecisionOp;
/**
 * 
 * @author jose@geowe.org
 * @since 29-11-2016
 * @author rafa@geowe.org
 * fix issue 222 (reduce tolerance)
 *
 */
public class TopologicalOverlay {
	private static final double TOLERANCIA = -0.00001;
	
	public List<String> getOverlay(final Geometry layer1,
			final Geometry layer2, final int op) {
		final List<String> resultLayer = new ArrayList<String>();
		Geometry geomContorno = null;

		switch (op) {
		case OverlayOp.INTERSECTION:
			geomContorno = EnhancedPrecisionOp.intersection(
					layer1.buffer(TOLERANCIA), layer2.buffer(TOLERANCIA));
			break;
		case OverlayOp.DIFFERENCE:
			geomContorno = EnhancedPrecisionOp.difference(
					layer1.buffer(TOLERANCIA), layer2.buffer(TOLERANCIA));
			break;
		case OverlayOp.SYMDIFFERENCE:
			geomContorno = EnhancedPrecisionOp.symDifference(
					layer1.buffer(TOLERANCIA), layer2.buffer(TOLERANCIA));
			break;
		default:
			break;
		}

		if (geomContorno != null) {

			if (geomContorno instanceof Polygon) {
				resultLayer.add(geomContorno.toText());
			} else if (geomContorno instanceof MultiPolygon) {

				final MultiPolygon multiPolygon = (MultiPolygon) geomContorno;
				for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
					final Polygon pol = (Polygon) multiPolygon.getGeometryN(i);
					resultLayer.add(pol.toText());
				}
			} else if (geomContorno instanceof GeometryCollection) {

				final GeometryCollection gc = (GeometryCollection) geomContorno;
				for (int i = 0; i < gc.getNumGeometries(); i++) {
					final Geometry geom = gc.getGeometryN(i);
					resultLayer.add(geom.toText());
				}
			}
		}

		return resultLayer;
	}
}