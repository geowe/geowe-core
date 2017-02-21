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

import java.util.List;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;

/**
 * Componente gráfico que representa una tabla editable con todos los datos
 * alfanuméricos (atributos) de un conjunto de VectorFeature, desde la que
 * se pueden modificar directamente las features 
 * 
 * @author Atanasio Muñoz
 *
 */
public class EditingFeatureGrid extends GridInlineEditing<VectorFeature> {

	public EditingFeatureGrid(int width, int height) {
		super(new FeatureGrid(width, height));				
	}
	
	public EditingFeatureGrid() {
		super(new FeatureGrid());			
	}	
	
	public FeatureGrid getFeatureGrid() {
		return (FeatureGrid) this.getEditableGrid();
	}

	/**
	 * Reconstruye la tabla completa en base a los atributos de las
	 * features que se reciben como parámetro.
	 * @param features
	 */
	public void rebuild(List<VectorFeature> features) {
		getFeatureGrid().rebuild(features);
		setGridEditors();
	}
	
	/**
	 * Reconstruye la tabla completa en base a los atributos de las
	 * features que se reciben como parámetro.
	 * @param vectorFeatures
	 */
	public void rebuild(VectorFeature[] vectorFeatures) {
		getFeatureGrid().rebuild(vectorFeatures);
		setGridEditors();
	}
	
	/**
	 * Actualiza los datos de la tabla con los datos de los atributos
	 * de las features que se pasan como parámetro. El esquema de las
	 * features debe coincidir con las columnas de la tabla
	 * @param features
	 */
	public void update(List<VectorFeature> features) {
		getFeatureGrid().update(features);
	}
	
	/**
	 * Actualiza los datos de la tabla con los datos de los atributos
	 * de las features que se pasan como parámetro. El esquema de las
	 * features debe coincidir con las columnas de la tabla
	 * @param vectorFeatures
	 */
	public void update(VectorFeature[] vectorFeatures) {
		getFeatureGrid().update(vectorFeatures);
	}
	
	/**
	 * Limpia todos los datos de la tabla manteniendo la definición
	 * de las columnas
	 */
	public void clear() {
		getFeatureGrid().clear();
	}
	
	/**
	 * Confirma los cambios realizados en la edición de la tabla, 
	 * aplicandolos a las Features afectadas
	 */
	public void commitChanges() {
		getFeatureGrid().getStore().commitChanges();
	}
	
	/**
	 * Cancela los cambios realizados en la edición de la tabla,
	 * dejando los Features sin modificar
	 */
	public void rejectChanges() {
		getFeatureGrid().getStore().rejectChanges();
	}
	
	public void setEnableCellRender(boolean enableCellRender) {
		getFeatureGrid().setEnableCellRender(enableCellRender);
	}
	
	private void setGridEditors() {
		ColumnModel<VectorFeature> columnModel = getFeatureGrid().getColumnModel();
		
		for(ColumnConfig<VectorFeature, ?> column : columnModel.getColumns()) {
			if(!column.isHidden()) {
				this.addEditor((ColumnConfig<VectorFeature, String>)column, new TextField());
			}
		}	
	}	
}
