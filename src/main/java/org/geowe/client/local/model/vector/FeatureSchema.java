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
package org.geowe.client.local.model.vector;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.util.Attributes;

/**
 * Clase que representa el conjunto de atributos de una capa vectorial. Siguiendo con el estándar SFS, 
 * estos atributos son compartidos por todas las features de la capa. Los metadatos que se registran
 * para cada atributo son: nombre, tipo de datos y si se muestra en el Map Tooltip.
 * 
 * NOTA: Actualmente solamente está soportado el tipo de dato STRING, debido la limitación existente
 * a la hora de exportar los datos de la capa, puesto que en los formatos ligeros interoperables los
 * datos viajan en formato texto plano.
 * 
 * @author Atanasio Muñoz
 *
 */
public class FeatureSchema implements Serializable {
	private static final long serialVersionUID = -3913377745944808092L;
		
	private final Map<String, FeatureAttributeDef> attributes;
	
	public FeatureSchema() {
		attributes = new HashMap<String, FeatureAttributeDef>();			
	}
	
	public FeatureSchema(List<String> attributeNames) {
		attributes = new HashMap<String, FeatureAttributeDef>();
		
		for(String attributeName : attributeNames) {
			if(attributeName != null && !attributeName.isEmpty()) {
				addAttribute(attributeName, AttributeType.STRING, false);
			}			
		}			
	}
	
	public FeatureAttributeDef getAttribute(int index) {
		if(index >= 0 && index < attributes.size()) {	
			return (FeatureAttributeDef) attributes.values().toArray()[index];
		} else {
			return null;
		}
	}
	
	public FeatureAttributeDef getAttribute(String name) {
		return attributes.get(name);
	}
	
	public void addAttribute(String name, AttributeType type, Boolean showInTooltip) {
		FeatureAttributeDef attribute = new FeatureAttributeDef();
		attribute.setName(name);
		attribute.setType(type);
		attribute.setShowInTooltip(showInTooltip);
		
		attributes.put(name, attribute);
	}
	
	public void removeAttribute(String name) {
		attributes.remove(name);
	}
	
	public void updateAttribute(String name, AttributeType type, Boolean showInTooltip) {
		if(hasAttribute(name)) {
			FeatureAttributeDef attribute = attributes.get(name);
			attribute.setType(type);
			attribute.setShowInTooltip(showInTooltip);
			
			attributes.put(name, attribute);
		} else {
			addAttribute(name, type, showInTooltip);
		}
	}
	
	public Collection<FeatureAttributeDef> getAttributes() {
		return attributes.values();
	}
	
	public int getNumAttributes() {
		return attributes.keySet().size();
	}
	
	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}
	
	@Override
	public String toString() {
		StringBuffer stringSchema = new StringBuffer("Feature Schema [");

		for (Entry<String, FeatureAttributeDef> attributeEntry : attributes
				.entrySet()) {
			stringSchema.append(attributeEntry.getValue().getName());
			stringSchema.append(" - ");
			stringSchema.append(attributeEntry.getValue().getType());
			stringSchema.append(";");
		}
		
		stringSchema.append("]");
		return stringSchema.toString();
	}

	public static FeatureSchema createFromFeature(VectorFeature feature) {
		return new FeatureSchema(feature.getAttributes().getAttributeNames());
	}
	
	public static Attributes toFeatureAttributes(FeatureSchema schema) {
		Attributes featureAttributes = new Attributes();
		
		Collection<FeatureAttributeDef> attributes = schema.getAttributes();
		for(FeatureAttributeDef attribute : attributes) {
			featureAttributes.setAttribute(attribute.getName(), (String)null);		
		}	
		
		return featureAttributes;
	}
						
	public static String getFeatureId(VectorFeature f) {
		String featureId = f.getFeatureId();
		
		if(featureId != null && featureId.length() > 0) {
			int cutPosition = featureId.lastIndexOf("_");
			featureId = "Feature " +  featureId.substring(cutPosition+1);
		}
				
		return featureId;		
	}

	public Set<String> getAttributeNames() {
		Set<String> attributeNames = new HashSet<String>();
		for (FeatureAttributeDef featureAttributeDef : getAttributes()) {
			attributeNames.add(featureAttributeDef.getName());
		}
		return attributeNames;
	}
}
