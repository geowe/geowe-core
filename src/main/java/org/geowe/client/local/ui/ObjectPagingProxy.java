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

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

/**
 * Proxy genérico de acceso paginado a una lista de objetos
 * 
 * @author Atanasio Muñoz
 *
 * @param <M>
 */
public class ObjectPagingProxy<M> implements
		DataProxy<PagingLoadConfig, PagingLoadResult<M>> {

	protected List<M> data;
	private int delay = 200;

	public ObjectPagingProxy(List<M> data) {
		this.data = data;
	}

	public List<M> getData() {
		return data;
	}

	public void setData(List<M> data) {
		this.data = data;
	}

	public int getDelay() {
		return delay;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}

	@Override
	public void load(final PagingLoadConfig config,
			final Callback<PagingLoadResult<M>, Throwable> callback) {
		final ArrayList<M> temp = new ArrayList<M>();

		for (M model : data) {
			temp.add(model);
		}

		final ArrayList<M> sublist = new ArrayList<M>();
		int start = config.getOffset();
		int limit = temp.size();
		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}
		for (int i = config.getOffset(); i < limit; i++) {
			sublist.add(temp.get(i));
		}

		Timer t = new Timer() {

			@Override
			public void run() {
				callback.onSuccess(new PagingLoadResultBean<M>(sublist, temp
						.size(), config.getOffset()));
			}
		};
		t.schedule(delay);

	}
}
