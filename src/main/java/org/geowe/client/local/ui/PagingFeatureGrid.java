package org.geowe.client.local.ui;

import java.util.ArrayList;
import java.util.List;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

public class PagingFeatureGrid extends FeatureGrid {

	private FeaturePagingProxy proxy;
	private PagingFeatureLoader loader;
	
	public PagingFeatureGrid(int width, int height) {
		super(width, height);
		
		proxy = new FeaturePagingProxy(new ArrayList<VectorFeature>());
		loader = new PagingFeatureLoader(proxy);
		loader.setRemoteSort(true);
		loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, VectorFeature, PagingLoadResult<VectorFeature>>(
				getStore()));
		
		this.setLoader(loader);
	}
	
	public PagingFeatureGrid() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	@Override
	public void rebuild(List<VectorFeature> features) {		
		this.update(features);
		this.reconfigure(this.getStore(), createColumnList());
	}	

	@Override
	public void update(List<VectorFeature> features) {
		proxy.setData(features);
	}
	
	@Override
	public PagingFeatureLoader getLoader() {
		return loader;
	}

	private class PagingFeatureLoader extends
			PagingLoader<PagingLoadConfig, PagingLoadResult<VectorFeature>> {

		public PagingFeatureLoader(
				DataProxy<PagingLoadConfig, PagingLoadResult<VectorFeature>> proxy) {
			super(proxy);
		}

		public void loadData(PagingLoadConfig config) {
			super.loadData(config);
		}
	}
}
