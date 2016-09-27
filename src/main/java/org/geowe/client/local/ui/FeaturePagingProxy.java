package org.geowe.client.local.ui;

import java.util.Collections;
import java.util.List;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.core.client.Callback;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

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
