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

import java.util.Collections;
import java.util.List;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.core.client.Callback;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

/**
 * Proxy de acceso paginado a una lista de features vectoriales.
 * Soporta ordenación ascendente y descendente por uno de los
 * atributos de las features.
 * 
 * @author Atanasio Muñoz
 *
 */
public class FeaturePagingProxy extends ObjectPagingProxy<VectorFeature> {

	public FeaturePagingProxy(List<VectorFeature> data) {
		super(data);		
	}

	@Override
	public void load(final PagingLoadConfig config,
			final Callback<PagingLoadResult<VectorFeature>, Throwable> callback) {			
		//Execute ordering options		
		if(config.getSortInfo() != null && !config.getSortInfo().isEmpty()) {
			String sortAttribute = config.getSortInfo().get(0).getSortField();
			int order = config.getSortInfo().get(0).getSortDir().equals(SortDir.ASC) ?
					FeatureComparator.ORDER_ASC : FeatureComparator.ORDER_DESC;
			
			Collections.sort(data, new FeatureComparator(sortAttribute, order)); 		
		}
		
		super.load(config, callback);		
	}	
}
