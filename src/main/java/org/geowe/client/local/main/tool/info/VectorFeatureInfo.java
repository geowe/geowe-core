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
package org.geowe.client.local.main.tool.info;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.util.Attributes;

/**
 * Feature info
 * 
 * @author geowe
 *
 */
public class VectorFeatureInfo {
	public static final int ATTR_BRIEF_LENGTH = 25;
	public static final String ATTR_BRIEF_SEPARATOR = " - ";
	
	private VectorFeature feature;
	
	public VectorFeatureInfo() {
		this.feature = null;
	}
	
	public VectorFeatureInfo(VectorFeature vectorFeature) {		
		this.feature = vectorFeature;
	}

	public String getFID() {
		String fid = feature.getFID();
		
		if(fid == null) {
			//TODO Pensar una solucion mas elegante cuando el FID viene nulo
			fid = Integer.toString(feature.getGeometry().getVertices(false).length);
		}
		
		return fid;
	}
	
	public String getFeatureId() {
		String featureId = feature.getFeatureId();
		
		if(featureId != null && featureId.length() > 0) {
			int cutPosition = featureId.lastIndexOf("_");
			featureId = "Feature " +  featureId.substring(cutPosition+1);
		}
				
		return featureId;
	}
	
	public String getAttributeBrief() {
		Attributes attributes = feature.getAttributes();
		StringBuffer attributeData = new StringBuffer();
		
		if(attributes != null) {
			for(String attr : attributes.getAttributeValues()){
				//Agregamos el separador a partir del segundo elemento
				if(attributeData.length() > 0) {
					attributeData.append(ATTR_BRIEF_SEPARATOR);
				}
				
				attributeData.append(attr);				
				
				if(attributeData.length() > ATTR_BRIEF_LENGTH) {
					break;					
				}
			}
		} 
		
		attributeData.trimToSize();
		
		if(attributeData.length() == 0) {		
			//Si la feature no tiene atributos, se coge por defecto el FeatureId
			attributeData.append(getFeatureId());
		}

			
		if(attributeData.length() > ATTR_BRIEF_LENGTH) {
			return attributeData.substring(0, ATTR_BRIEF_LENGTH) + "...";			
		} else {
			return attributeData.toString();
		}		
	}
	
	public VectorFeature getFeature() {
		return feature;
	}

	public void setFeature(VectorFeature feature) {
		this.feature = feature;
	}		
}
