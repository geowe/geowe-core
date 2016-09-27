package org.geowe.client.local.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

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
