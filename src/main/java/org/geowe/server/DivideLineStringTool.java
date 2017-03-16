package org.geowe.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;
import com.vividsolutions.jts.geom.util.LinearComponentExtracter;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class DivideLineStringTool {

	private static final Logger LOG = LoggerFactory
			.getLogger(DivideLineStringTool.class.getName());

	public List<String> divide(String divisionLine, String lineToDivide)
			throws IllegalArgumentException {

		Geometry geomToDivide = getGeometry(lineToDivide);
		Geometry divisionGeom = getGeometry(divisionLine);
		Geometry unionGeom = geomToDivide.union(divisionGeom);

		List<Geometry> lines = linealize(unionGeom);
		List<Geometry> segments = getSegments(geomToDivide, lines);
		return getWkts(segments);
	}

	private Geometry getGeometry(final String wkt) {
		Geometry geom = null;
		final GeometryFactory factory = new GeometryFactory(
				PackedCoordinateSequenceFactory.DOUBLE_FACTORY);
		final WKTReader reader = new WKTReader(factory);
		try {
			geom = reader.read(wkt);
		} catch (ParseException e) {
			LOG.error("Invalid WKT" + e.getMessage());
			throw new IllegalArgumentException("Invalid WKT", e);
		}

		return geom;
	}

	public List<Geometry> linealize(Geometry geometry) {
		LinearComponentExtracter extracter = new LinearComponentExtracter(
				getLines(geometry));
		return extracter.getLines(geometry);
	}

	private Collection<Geometry> getLines(final Geometry geom) {
		final List<Geometry> linesList = new ArrayList<Geometry>();
		final LinearComponentExtracter lineFilter = new LinearComponentExtracter(
				linesList);

		geom.apply(lineFilter);

		return linesList;
	}

	private List<Geometry> getSegments(Geometry sourceLines,
			List<Geometry> lines) {
		List<Geometry> segments = new ArrayList<Geometry>();
		for (Geometry line : lines) {
			if (sourceLines.intersects(line) && !line.crosses(sourceLines)) {
				segments.add(line);
			}
		}
		return segments;
	}

	private List<String> getWkts(Collection<Geometry> geometries) {
		List<String> wkts = new ArrayList<String>();
		for (Geometry geometry : geometries) {
			wkts.add(geometry.toText());
		}
		return wkts;
	}
}
