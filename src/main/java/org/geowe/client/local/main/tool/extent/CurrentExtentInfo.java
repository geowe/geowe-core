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

package org.geowe.client.local.main.tool.extent;

import javax.enterprise.context.ApplicationScoped;

import org.gwtopenmaps.openlayers.client.Bounds;
/**
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class CurrentExtentInfo {
		
	private String wkt;
	private String wktWGS84;
	public String getWktWGS84() {
		return wktWGS84;
	}

	public void setWktWGS84(String wktWGS84) {
		this.wktWGS84 = wktWGS84;
	}

	private String center;
	private double lowerLeftX;
	private double lowerLeftY;
	private double upperRightX;
	private double upperRightY;
	private Bounds bounds;
	
	public Bounds getBounds() {
		return bounds;
	}

	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public double getLowerLeftX() {
		return lowerLeftX;
	}

	public void setLowerLeftX(double lowerLeftX) {
		this.lowerLeftX = lowerLeftX;
	}

	public double getLowerLeftY() {
		return lowerLeftY;
	}

	public void setLowerLeftY(double lowerLeftY) {
		this.lowerLeftY = lowerLeftY;
	}

	public double getUpperRightX() {
		return upperRightX;
	}

	public void setUpperRightX(double upperRightX) {
		this.upperRightX = upperRightX;
	}

	public double getUpperRightY() {
		return upperRightY;
	}

	public void setUpperRightY(double upperRightY) {
		this.upperRightY = upperRightY;
	}

	public String getBbox() {
		return lowerLeftX + ", " + lowerLeftY + ", " + upperRightX + ", " + upperRightY;
	}
	
	public String getWkt() {
		return wkt;
	}

	public void setWkt(String wkt) {
		this.wkt = wkt;
	}		
}
