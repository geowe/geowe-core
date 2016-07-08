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
package org.geowe.client.local.main;

import org.gwtopenmaps.openlayers.client.layer.Vector;

public class VectorLayerInfo {
	public static final int MAX_LAYER_NAME_LENGTH = 45;	
	public static final int MAX_ATTR_BRIEF_LENGTH = 45;
	
	private Vector layer;
	
	public VectorLayerInfo() {
		this.layer = null;
	}
	
	public VectorLayerInfo(Vector layer) {		
		this.layer = layer;
	}
	
	public String getId() {
		return layer.getId();
	}

	public String getName() {
		if(layer.getName().length() > MAX_LAYER_NAME_LENGTH) {
			return layer.getName().substring(0, MAX_LAYER_NAME_LENGTH) + "...";
		}
		
		return layer.getName();		
	}
	
	public String getEpsg() {
		return layer.getProjection().getProjectionCode();
	}
	
	public int getNumFeatures() {
		return layer.getNumberOfFeatures();
	}
	
	public String getAttributeBrief() {		
		StringBuffer attributeData = new StringBuffer();
		
		attributeData.append(getName());
		attributeData.append(":");
		attributeData.append(getEpsg());
		
		attributeData.trimToSize();					
		
		if(attributeData.length() > MAX_ATTR_BRIEF_LENGTH) {
			return attributeData.substring(0, MAX_ATTR_BRIEF_LENGTH) + "...";			
		} else {
			return attributeData.toString();
		}		
	}
	
	public Vector getLayer() {
		return layer;
	}

	public void setLayer(Vector layer) {
		this.layer = layer;
	}		
}
