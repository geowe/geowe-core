/*
 * #%L
 * GeoWE Project
 * %%
 * Copyright (C) 2015 - 2016 GeoWE.org
 * %%
 * This file is part of GeoWE.org.
 * 
 * GeoWE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General  License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GeoWE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 * 
 * You should have received a copy of the GNU General  License
 * along with GeoWE.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.geowe.client.shared.jts;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("jtsService")
public interface JTSService extends RemoteService {
	String getBuffer(String wkt, double buffer) throws IllegalArgumentException;

	List<String> getBuffer(List<String> wktLayer1, double buffer)
			throws IllegalArgumentException;

	String union(String wkt1, String wkt2) throws IllegalArgumentException;

	List<String> divide(String wktCorte, String wktIntersected)
			throws IllegalArgumentException;

	List<String> getCentroid(List<String> wkt) throws IllegalArgumentException;

	List<String> getIntersection(List<String> wktLayer1, List<String> wktLayer)
			throws IllegalArgumentException;

	List<String> getIntersects(List<String> wktLayer1, List<String> wktLayer2)
			throws IllegalArgumentException;

	List<String> getDifference(List<String> wktLayer1, List<String> wktLayer2)
			throws IllegalArgumentException;

	List<String> getUnion(List<String> wktLayer1, List<String> wktLayer2)
			throws IllegalArgumentException;

	List<String> getEnvelope(List<String> wkts) throws IllegalArgumentException;

	List<String> getSymDifference(List<String> wktLayer1, List<String> wktLayer2)
			throws IllegalArgumentException;

	List<ValidationResult> validate(List<String> wktLayer)
			throws IllegalArgumentException;
}
