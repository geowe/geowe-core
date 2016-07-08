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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;
import com.vividsolutions.jts.geom.util.GeometryCombiner;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * AbstractJTSService representa la funcionalidad base que ofrece la operativa del motor geométrico.
 * 
 * @author jose@geowe.org
 *
 */

public abstract class AbstractJTSService extends RemoteServiceServlet {
	private static final long serialVersionUID = -1256164249031779251L;
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractJTSService.class.getName());
	private final TopologicalOverlay topologicalOverlay = new TopologicalOverlay();
	private static final String INVALID_WKT_MESSAGE = "Invalid WKT";
	
	protected Geometry getGeometry(final String wkt) {
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

	protected List<Geometry> getList(final List<String> wktLayer) {

		final List<Geometry> result = new ArrayList<Geometry>();
		for (final String wkt : wktLayer) {
			result.add(getGeometry(wkt));
		}

		return result;
	}
	
	protected List<String> getOverlay(final List<String> wktLayer1,
			final List<String> wktLayer2, final int op) {
		final Geometry layer1 = GeometryCombiner.combine(getList(wktLayer1));
		final Geometry layer2 = GeometryCombiner.combine(getList(wktLayer2));
		return topologicalOverlay.getOverlay(layer1, layer2, op);
	}	
}