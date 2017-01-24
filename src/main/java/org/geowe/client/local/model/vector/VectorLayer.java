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

import java.util.Collection;

import org.geowe.client.local.layermanager.tool.create.vector.source.LayerVectorSource;
import org.geowe.client.local.model.style.VectorStyleDef;
import org.gwtopenmaps.openlayers.client.event.VectorFeatureAddedListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.VectorOptions;
import org.gwtopenmaps.openlayers.client.util.JSObject;

/**
 * Implementacion mejorada de una capa vectorial. Se ha extendido la clase Vector
 * para incluir a nivel de capa el esquema o conjunto de atributos que comparten 
 * todas y cada una de las features de la misma, asi como las operaciones necesarias
 * para gestionar dicho esquema.
 * 
 * @author Atanasio Muñoz
 *
 */
public class VectorLayer extends Vector {
	
	private FeatureSchema featureSchema;
	private LayerVectorSource source;	
	private VectorStyleDef vectorStyle;

	public VectorLayer(JSObject vector) {
		super(vector);	
		
		this.featureSchema = new FeatureSchema();
		addFeatureAddedListener();
	}
	
    public VectorLayer(String name) {
    	super(name);
    	
    	this.featureSchema = new FeatureSchema();
    	addFeatureAddedListener();
    }
    
    public VectorLayer(String name, VectorOptions options) {
    	super(name, options);    
    	
    	this.featureSchema = new FeatureSchema();
    	addFeatureAddedListener();
    }    
    
    public VectorLayer(String name, FeatureSchema schema) {
    	this(name);
    	this.featureSchema = schema;
    }    
        
    public VectorLayer(String name, VectorOptions options, FeatureSchema schema) {
    	this(name, options);    	
    	this.featureSchema = schema;
    }    

    @Override
    public void addFeature(VectorFeature f) {    	
    	initializeLayerFeatureSchema(f);
    	checkFeature(f);    	    	
    	super.addFeature(f);
    }
    
    @Override
    public void addFeatures(VectorFeature[] features) {
    	if(features != null && features.length > 0) {
    		initializeLayerFeatureSchema(features[0]);
    		
    		/**
    		 * Consideramos que todas las features del array tienen el mismo esquema,
    		 * para asi no tener que comprobarlo para cada una de ellas (por rendimiento)
    		 */
    		if(!featureMatchesSchema(features[0])) {
    			for(VectorFeature f : features) {
    				checkFeature(f);
    			}
    		}
    		
    		super.addFeatures(features);
    	}
    }
    
    public void addAttribute(String name, Boolean showInTooltip) {
    	this.featureSchema.addAttribute(name, AttributeType.STRING, showInTooltip);
    	    	
    	//Actualizamos el esquema de todas las features       	
    	if(getNumberOfFeatures() > 0) {
    		for(VectorFeature feature : getFeatures()) {
    			feature.getAttributes().setAttribute(name, (String)null);     				
    		}
    	}
    	redraw();		
    }
    
    public void removeAttribute(String name) {
    	this.featureSchema.removeAttribute(name);
    	
    	//Actualizamos el esquema de todas las features 
    	if(getNumberOfFeatures() > 0) {
    		for(VectorFeature feature : getFeatures()) {
    			feature.getAttributes().unsetAttribute(name);
    		}
    	}
    	redraw();
    }
    
    public void modifyAttribute(String name, String newName, Boolean showInTooltip) {
    	String finalName = name;    	
    	
    	if(newName != null && !newName.isEmpty() && !name.equals(newName)) {
    		renameAttribute(name, newName);
    		finalName = newName;
    	}    	    
    	
    	this.featureSchema.updateAttribute(finalName, AttributeType.STRING, showInTooltip);
    	    	
    	redraw();
    }
    
    public void renameAttribute(String name, String newName) {
    	FeatureAttributeDef attribute = this.featureSchema.getAttribute(name);
    	
    	this.featureSchema.addAttribute(newName, attribute.getType(), attribute.isShowInTooltip());
    	this.featureSchema.removeAttribute(name);
    	
    	if(getNumberOfFeatures() > 0) {
			for(VectorFeature feature : getFeatures()) {
				String value = feature.getAttributes().getAttributeAsString(name);
				feature.getAttributes().setAttribute(newName, value);
				feature.getAttributes().unsetAttribute(name);
			}
		}    	    	
    }
    
    public FeatureAttributeDef getAttribute(String name) {
    	return this.featureSchema.getAttribute(name);
    }
    
    public Collection<FeatureAttributeDef> getAttributes() {
    	return this.featureSchema.getAttributes();
    }
    
    public int getNumAttributes() {
    	return this.featureSchema.getNumAttributes();
    }
    
    public FeatureSchema getSchema() {
    	return this.featureSchema;
    }
    
    /**
     * Agrega un listener a la capa para que cada vez que se inserte una feature
     * desde una herramienta de edicion, esta se adapte al esquema de atributos 
     * de la capa
     */
    private void addFeatureAddedListener() {
		this.addVectorFeatureAddedListener(new VectorFeatureAddedListener() {

			@Override
			public void onFeatureAdded(FeatureAddedEvent eventObject) {
				VectorFeature feature = eventObject.getVectorFeature();
				checkFeature(feature);							
			}
		});
    }
    
	/**
	 * Si es la primera feature que agregamos a la capa y el VectorLayer no
	 * tiene aun definido el esquema, se define este a partir de los atributos
	 * de la feature, si es que esta tiene algun atributo definido
	 * 
	 * @param feature
	 */
    public void initializeLayerFeatureSchema(VectorFeature feature) {

    	if(this.featureSchema == null || this.featureSchema.getNumAttributes() == 0) {
    		if(feature != null  && feature.getAttributes().getAttributeNames().size() > 0) {
    			this.featureSchema = FeatureSchema.createFromFeature(feature);
    		}
    	}    	
    }

    private void checkFeature(VectorFeature feature) {    	    

    	if(feature != null && !featureMatchesSchema(feature)) {
    		feature.setAttributes(FeatureSchema.toFeatureAttributes(featureSchema));
    	}
    }
        
    private boolean featureMatchesSchema(VectorFeature feature) {
    	boolean matches = true;
    	
    	if(feature.getAttributes().getAttributeNames().size() == featureSchema.getNumAttributes()) {
    		
    		for(String attributeName : feature.getAttributes().getAttributeNames()) {
    			if(!featureSchema.hasAttribute(attributeName)) {
    				matches = false;
    				break;
    			}
    		}
    	} else {
    		matches = false;
    	}
    	return matches;
    }       
    
    public void setSource(LayerVectorSource source) {
		this.source = source;
	}
	
	public LayerVectorSource getSource() {
		return source;
	}

	public VectorStyleDef getVectorStyle() {
		return vectorStyle;
	}

	public void setVectorStyle(VectorStyleDef vectorStyle) {
		this.vectorStyle = vectorStyle;
	}
}
