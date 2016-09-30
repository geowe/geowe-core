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
import java.util.List;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

/**
 * Componente gráfico que representa una tabla paginada con todos los datos
 * alfanuméricos (atributos) de un conjunto de VectorFeature, mostrando
 * un número determinado de registros por página, según se configure
 * en el PagingToolBar que recibe en el constructor. 
 * 
 * @author Atanasio Muñoz
 *
 */
public class PagingFeatureGrid extends FeatureGrid {

	private FeaturePagingProxy proxy;
	private PagingFeatureLoader loader;
	private PagingToolBar toolBar;
	
	public PagingFeatureGrid(int width, int height, PagingToolBar toolBar) {
		super(width, height);
		
		proxy = new FeaturePagingProxy(new ArrayList<VectorFeature>());
		loader = new PagingFeatureLoader(proxy);
		loader.setRemoteSort(true);
		loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, VectorFeature, PagingLoadResult<VectorFeature>>(
				getStore()));
		this.toolBar = toolBar;
		
		this.setLoader(loader);
		this.toolBar.bind(loader);
	}
	
	public PagingFeatureGrid(PagingToolBar toolBar) {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, toolBar);
	}

	@Override
	public void rebuild(List<VectorFeature> features) {		
		this.update(features);
		this.reconfigure(this.getStore(), createColumnList(features));
	}	
	
	@Override
	public void update(List<VectorFeature> features) {
		proxy.setData(features);
		toolBar.refresh();
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
