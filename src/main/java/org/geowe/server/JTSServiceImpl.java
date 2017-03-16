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

import org.geowe.client.shared.jts.JTSService;
import org.geowe.client.shared.jts.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.util.LinearComponentExtracter;
import com.vividsolutions.jts.operation.overlay.OverlayOp;

/**
 * JTSServiceImpl representa el motor geométrico del sistema y es el responsable de efectuar todas las operaciones espaciales de edición y 
 * geoprocesamiento
 * 
 * @author jose@geowe.org
 *
 */
public class JTSServiceImpl extends AbstractJTSService implements JTSService {

	private static final long serialVersionUID = 5373831347589551109L;
	private static final Logger LOG = LoggerFactory
			.getLogger(JTSServiceImpl.class.getName());
	private final GeometryValidator validator = new GeometryValidator();
	private static final double TOLERANCIA = 0.001;	
	private static final double TOLERANCIA_ENVELOPE = 0.00001;	
	private final DividePolygonTool dividePolygonTool = new DividePolygonTool();
	private final LineNoder lineNoder = new LineNoder();

	@Override
	public List<String> getCentroid(final List<String> wkts)
			throws IllegalArgumentException {
		final List<String> result = new ArrayList<String>();

		for (final String wkt : wkts) {
			result.add(getGeometry(wkt).getCentroid().toText());
		}
		return result;
	}

	@Override
	public List<String> getEnvelope(final List<String> wkts)
			throws IllegalArgumentException {
		Geometry resultGeometry = null;

		final List<String> result = new ArrayList<String>();

		for (final String wkt : wkts) {
			Geometry geom = getGeometry(wkt);
			geom = geom.buffer(TOLERANCIA_ENVELOPE);

			if (resultGeometry == null) {
				resultGeometry = geom;
			} else {
				resultGeometry = resultGeometry.union(geom);
			}
		}

		if (resultGeometry != null) {
			result.add(resultGeometry.getEnvelope().toText());
		}

		return result;
	}

	public List<String> getBuffer(final List<String> wktLayer1, final double buffer) {
		final List<String> resultLayer = new ArrayList<String>();

		for (final String wkt1 : wktLayer1) {
			resultLayer.add(getBuffer(wkt1, buffer));
		}

		return resultLayer;
	}

	public String getBuffer(final String wkt, final double distance)
			throws IllegalArgumentException {

		return getGeometry(wkt).buffer(distance).toText();
	}

	public String union(final String wkt1, final String wkt2)
			throws IllegalArgumentException {
		return getGeometry(wkt1).union(getGeometry(wkt2)).toText();
	}

	public List<String> getIntersection(final List<String> wktLayer1,
			final List<String> wktLayer2) {
		return getOverlay(wktLayer1, wktLayer2, OverlayOp.INTERSECTION);
	}

	public List<String> getDifference(final List<String> wktLayer1,
			final List<String> wktLayer2) {
		return getOverlay(wktLayer1, wktLayer2, OverlayOp.DIFFERENCE);
	}

	public List<String> getSymDifference(final List<String> wktLayer1,
			final List<String> wktLayer2) {
		return getOverlay(wktLayer1, wktLayer2, OverlayOp.SYMDIFFERENCE);
	}

	public List<String> getIntersects(final List<String> wktLayer1,
			final List<String> wktLayer2) {
		final List<String> resultLayer = new ArrayList<String>();

		for (final String wkt1 : wktLayer1) {
			final Geometry geom = getGeometry(wkt1);
			if (isIntersects(geom, wktLayer2)) {
				resultLayer.add(geom.toText());
			}
		}

		return resultLayer;
	}

	private boolean isIntersects(final Geometry geomToIntersect, final List<String> wktLayer) {
		boolean intersects = false;

		for (final String wktElement : wktLayer) {

			if (geomToIntersect.buffer(TOLERANCIA).intersects(
					getGeometry(wktElement).buffer(TOLERANCIA))) {
				intersects = true;
				break;
			}
		}

		return intersects;
	}

	public List<String> getUnion(final List<String> wktLayer1, final List<String> wktLayer2) {
		final List<String> resultLayer = new ArrayList<String>();

		wktLayer1.addAll(wktLayer2);
		final List<Geometry> linesList = new ArrayList<Geometry>();
		final LinearComponentExtracter lineFilter = new LinearComponentExtracter(
				linesList);
		for (final String wkt : wktLayer1) {
			final Geometry geom = getGeometry(wkt);
			geom.apply(lineFilter);
		}

		final Collection<Geometry> nodedLines = lineNoder.nodeLines(linesList);		
		final Collection<Geometry> polys = lineNoder.polygonizer(nodedLines);

		for (final Geometry geom : polys) {
			resultLayer.add(geom.toText());
		}

		return resultLayer;
	}

	public List<String> divide(final String wktCorte, final String wktIntersected)
			throws IllegalArgumentException {
		if (wktIntersected.startsWith("POLYGON")) {
			return dividePolygonTool.divide(wktCorte, wktIntersected);
		} else {
			return new DivideLineStringTool().divide(wktCorte, wktIntersected);
		}

	}

	@Override
	public List<ValidationResult> validate(final List<String> wktLayer)
			throws IllegalArgumentException {
		return validator.validate(wktLayer);
	}
}
