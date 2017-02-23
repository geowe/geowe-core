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

import org.geowe.client.shared.jts.ValidationResult;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.valid.RepeatedPointTester;

/**
 * GeometryValidator representa el validador geométrico responsable de detectar errores topológicos de las geometrías
 * 
 * @author jose@geowe.org
 *
 */

public class GeometryValidator extends AbstractJTSService {


	private static final long serialVersionUID = -7898968493113039699L;
	private final RepeatedPointTester repeatedPointTester = new RepeatedPointTester();
	private final CoordinateUtil topologyUtil = new CoordinateUtil();

	private List<String> errorCoordinates = new ArrayList<String>();

	private static final double MIN_SEGMENT_LENGTH = 0.001;
	private static final double MIN_POLYGON_AREA = 0.001;

	public List<ValidationResult> validate(final List<String> elements) {
		final List<ValidationResult> validationErrors = new ArrayList<ValidationResult>();

		for (final String wktGeom : elements) {
			validate(getGeometry(wktGeom), validationErrors);
		}

		return validationErrors;
	}

	private void validate(final Geometry geom, final List<ValidationResult> validationErrors) {
		
		if (geom.isEmpty()) {
			return;
		}

		if (geom instanceof GeometryCollection) {
			final GeometryCollection gc = (GeometryCollection) geom;
			for (int numGeom = 0; numGeom < gc.getNumGeometries(); numGeom++) {
				validate(gc.getGeometryN(numGeom), validationErrors);
			}
		}

		final ValidationResult result = new ValidationResult();
		result.setWkt(geom.toText());
		final List<String> messages = new ArrayList<String>();
		
		if (!geom.isValid()) {
			messages.add("Error en topología básica");
		}

		if (!geom.isSimple()) {
			messages.add("No es una geometría simple");
		}

		if (repeatedPointTester.hasRepeatedPoint(geom)) {
			messages.add("Se encuentran vértices repetidos");
		}

		if (geom instanceof Polygon) {
			final Polygon polygon = (Polygon) geom;
			if (CGAlgorithms.isCCW(polygon.getExteriorRing().getCoordinates())) {
				messages.add("Error en orientación del polígono");
			} else {

				for (int numRing = 0; numRing < polygon.getNumInteriorRing(); numRing++) {
					if (!CGAlgorithms.isCCW(polygon.getInteriorRingN(numRing).getCoordinates())) {
						messages.add("Error en orientación del polígono en anillos interiores");
						break;
					}
				}
			}

			if (!validateMinPolygonArea(geom)) {
				messages.add("Error en validación mínima de area de un polígono");
			}
		}

		if (!validateMinSegmentLength(geom)) {
			messages.add("Error en validación mínima de longitud de segmento. Coordenadas");
			result.setErrorsPoints(errorCoordinates);
		}


		if(!messages.isEmpty()) {
			result.setMessages(messages);
			validationErrors.add(result);
		}		
	}

	private boolean validateMinSegmentLength(final Geometry geom) {
		final List<Coordinate[]> arrays = topologyUtil.toCoordinateArrays(geom, false);
		boolean isValid = true;
		for (final Coordinate[] coordinates: arrays) {
			if (!validateMinSegmentLength(geom, coordinates)) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}


	protected boolean validateMinPolygonArea(final Geometry geom) {
		return (((Polygon) geom).getArea() >= MIN_POLYGON_AREA);
	}

	private boolean validateMinSegmentLength(final Geometry geom, final Coordinate... coordinates) {
		boolean isValid = true;
		if (coordinates.length >= 2) {
			for (int i = 1; i < coordinates.length; i++) {

				if (!validateMinSegmentLength(coordinates[i - 1],
						coordinates[i])) {

					GeometryFactory geomFactory = new GeometryFactory();
					errorCoordinates.add(geomFactory
							.createPoint(coordinates[i]).toText());
					isValid = false;
				}
			}
		}
		return isValid;
	}


	private boolean validateMinSegmentLength(final Coordinate coordinate1, final Coordinate coordinate2) {
		return (coordinate1.distance(coordinate2) >= MIN_SEGMENT_LENGTH);
	}
}
