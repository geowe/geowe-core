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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;
import com.vividsolutions.jts.geom.util.LinearComponentExtracter;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * DividePolygonTool representa la herramienta responsable de realizar la división de polígonos a través de una línea. 
 * 
 * @author jose@geowe.org
 *
 */

public class DividePolygonTool {
	private static final Logger LOG = LoggerFactory
			.getLogger(DividePolygonTool.class.getName());
	private static final String INVALID_WKT_MESSAGE = "Invalid WKT";
	
	private final LineNoder lineNoder = new LineNoder();
	
	public List<String> divide(final String wktCorte, final String wktIntersected)
			throws IllegalArgumentException {

		final List<String> wkts = new ArrayList<String>();
		try {
			final Geometry geomCorte = getGeometry(wktCorte);
			final Geometry geomIntersected = getGeometry(wktIntersected);

			if (geomIntersected == null || geomCorte == null) {
				throw new RuntimeException("Existen geometría nulas");
			}

			final Collection<Geometry> lines = getLines((Polygon) geomIntersected, (LineString) geomCorte);
			final Collection<Geometry> nodedLines = lineNoder.nodeLines(lines);			
			final Collection<Geometry> polygons = lineNoder.polygonizer(nodedLines);

			for (final Geometry pol : polygons) {
				wkts.add(pol.toText());
			}

		} catch (Exception e) {
			throw new IllegalArgumentException("Error no controlado: "
					+ e.getMessage(), e);
		}

		return wkts;
	}
	
	private Collection<Geometry> getLines(final Geometry geom1, final Geometry geom2) {
		final List<Geometry> linesList = new ArrayList<Geometry>();
		final LinearComponentExtracter lineFilter = new LinearComponentExtracter(
				linesList);

		geom1.apply(lineFilter);
		geom2.apply(lineFilter);

		return linesList;
	}
		
	private Geometry getGeometry(final String wkt) {
		Geometry geom = null;
		final GeometryFactory factory = new GeometryFactory(
				PackedCoordinateSequenceFactory.DOUBLE_FACTORY);
		final WKTReader reader = new WKTReader(factory);
		try {
			geom = reader.read(wkt);
		} catch (ParseException e) {			
			LOG.error(INVALID_WKT_MESSAGE + e.getMessage());
			throw new IllegalArgumentException(INVALID_WKT_MESSAGE, e);
		}

		return geom;
	}
}