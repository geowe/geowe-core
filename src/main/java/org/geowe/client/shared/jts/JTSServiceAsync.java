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
package org.geowe.client.shared.jts;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface JTSServiceAsync {
	void getBuffer(String wkt, double buffer, AsyncCallback<String> callback) throws IllegalArgumentException;
	void getBuffer(List<String> wktLayer1, double buffer, AsyncCallback<List<String>> callback) throws IllegalArgumentException;
	void union(String wkt1, String wkt2, AsyncCallback<String> callback) throws IllegalArgumentException;
	void divide(String wktCorte, String wktIntersected,  AsyncCallback<List<String>> callback) throws IllegalArgumentException;	
	void getCentroid(List<String> wkts,  AsyncCallback<List<String>> callback) throws IllegalArgumentException;	
	void getIntersection(List<String> wktLayer1, List<String> wktLayer, AsyncCallback<List<String>> callback) throws IllegalArgumentException;
	void getIntersects(List<String> wktLayer1,List<String> wktLayer2, AsyncCallback<List<String>> callback) throws IllegalArgumentException;
	void getDifference(List<String> wktLayer1, List<String> wktLayer2, AsyncCallback<List<String>> callback) throws IllegalArgumentException;
	void getUnion(List<String> wktLayer1, List<String> wktLayer2, AsyncCallback<List<String>> callback) throws IllegalArgumentException;
	void getEnvelope(List<String> wkts, AsyncCallback<List<String>> callback) throws IllegalArgumentException;
	void getSymDifference(List<String> wktLayer1, List<String> wktLayer2, AsyncCallback<List<String>> callback) throws IllegalArgumentException;
	void validate(List<String> wktLayer, AsyncCallback<List<ValidationResult>> callback) throws IllegalArgumentException;
}
