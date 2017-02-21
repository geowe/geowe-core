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
package org.geowe.client.local.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

/**
 * Componente gráfico que representa una tabla con todos los datos
 * alfanuméricos (atributos) de un conjunto de VectorFeature 
 * 
 * @author Atanasio Muñoz
 *
 */
public class FeatureGrid extends Grid<VectorFeature> {
	public static final int DEFAULT_WIDTH = 430;
	public static final int DEFAULT_HEIGHT = 200;
	
	private boolean enableCellRender;
	
	public FeatureGrid(int width, int height) {
		super(
				new ListStore<VectorFeature>(
						new ModelKeyProvider<VectorFeature> () {
							@Override
							public String getKey(VectorFeature item) {						
								return item.getFeatureId();
							}					
						}),
				
				new ColumnModel<VectorFeature>(
						new ArrayList<ColumnConfig<VectorFeature, ?>>())
				);
				
		this.setBorders(true);
		this.setAllowTextSelection(true);
		this.getView().setStripeRows(true);
		this.getView().setColumnLines(true);		
		this.setColumnReordering(true);					
		this.setLoadMask(true);
		
		this.setWidth(width);
		this.setHeight(height);	
		
		this.setEnableCellRender(false);
	}
	
	public FeatureGrid() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public boolean isEnableCellRender() {
		return enableCellRender;
	}

	public void setEnableCellRender(boolean enableCellRender) {
		this.enableCellRender = enableCellRender;
	}

	/**
	 * Reconstruye la tabla completa en base a los atributos de las
	 * features que se reciben como parámetro.
	 * @param features
	 */
	public void rebuild(List<VectorFeature> features) {
		update(features);
		this.reconfigure(this.getStore(), createColumnList(features));		
	}
	
	/**
	 * Reconstruye la tabla completa en base a los atributos de las
	 * features que se reciben como parámetro.
	 * @param vectorFeatures
	 */
	public void rebuild(VectorFeature[] vectorFeatures) {
		rebuild(Arrays.asList(vectorFeatures));
	}
	
	/**
	 * Actualiza los datos de la tabla con los datos de los atributos
	 * de las features que se pasan como parámetro. El esquema de las
	 * features debe coincidir con las columnas de la tabla
	 * @param features
	 */
	public void update(List<VectorFeature> features) {
		this.getStore().clear();
		this.getStore().addAll(features);
	}
	
	/**
	 * Actualiza los datos de la tabla con los datos de los atributos
	 * de las features que se pasan como parámetro. El esquema de las
	 * features debe coincidir con las columnas de la tabla
	 * @param vectorFeatures
	 */
	public void update(VectorFeature[] vectorFeatures) {
		update(Arrays.asList(vectorFeatures));
	}
	
	/**
	 * Limpia todos los datos de la tabla manteniendo la definición
	 * de las columnas
	 */
	public void clear() {
		this.getStore().clear();
	}
	
	protected ColumnModel<VectorFeature> createColumnList(List<VectorFeature> features) {
		List<ColumnConfig<VectorFeature, ?>> columns = new ArrayList<ColumnConfig<VectorFeature, ?>>();
		
		if(features != null && features.size() > 0) {
			VectorFeature feature = features.get(0);

			if(feature.getAttributes() != null) {
				for(String attributeName : feature.getAttributes().getAttributeNames()) {	
					AttributeValueProvider attributeProvider = new AttributeValueProvider(attributeName);
					
					ColumnConfig<VectorFeature, String> attributeColumn = new ColumnConfig<VectorFeature, String>(
							attributeProvider, 100, attributeName);
					attributeColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
					if(isEnableCellRender()) {
						attributeColumn.setCell(new FeatureGridCellRenderer());
					}					
					
					columns.add(attributeColumn);
				}
			}					
		}
			
		return new ColumnModel<VectorFeature>(columns);
	}
	
	private class AttributeValueProvider implements ValueProvider<VectorFeature, String> {

	    public String attributeName;
	    
	    public AttributeValueProvider(String attributeName) {
	        this.attributeName = attributeName;
	    }

	    @Override
	    public String getValue(VectorFeature feature) {
	        if(feature.getAttributes().getAttributeAsString(attributeName) == null) {
	            return "";
	        } else {
	            return feature.getAttributes().getAttributeAsString(attributeName);
	        }
	    }

	    @Override
	    public void setValue(VectorFeature feature, String value) {
	    	feature.getAttributes().setAttribute(attributeName, value);
	    }

	    @Override
	    public String getPath() {
	        return attributeName;
	    }
	}
}


