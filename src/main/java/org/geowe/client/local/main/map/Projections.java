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
package org.geowe.client.local.main.map;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArrayString;

public final class Projections {
	private static final List<String> SUPPORTED_PROJECTIONS = getProj4jsProjections();

	private Projections() {

	}
	public static List<String> getSupportedProjections() {
		return SUPPORTED_PROJECTIONS;
	}
		
	public static boolean isSupported(String projection) {
		return SUPPORTED_PROJECTIONS.contains(projection);
	}
	
	private static List<String> getProj4jsProjections() {
		List<String> projections = new ArrayList<String>(); 
					
		JsArrayString projDefs = getProjDefs();

		for (int i = 0; i < projDefs.length(); i++) {
			projections.add(projDefs.get(i).trim());
		}

		return projections;
	}
	
	private static native JsArrayString getProjDefs() /*-{
		return Object.keys($wnd.Proj4js.defs);
	}-*/;
}
