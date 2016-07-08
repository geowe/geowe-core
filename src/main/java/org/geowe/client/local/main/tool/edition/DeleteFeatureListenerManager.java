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
package org.geowe.client.local.main.tool.edition;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.main.tool.info.DeleteFeatureListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

/**
 * TODO
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class DeleteFeatureListenerManager {

	private List<DeleteFeatureListener> listeners = new ArrayList<DeleteFeatureListener>();

	public void addDeleteFeatureListener(DeleteFeatureListener listener) {
		listeners.add(listener);
	}

	public List<DeleteFeatureListener> getlisteners() {
		return listeners;
	}
	
	public void notifyListeners(VectorFeature vectorFeature){
		for (DeleteFeatureListener listener : getlisteners()) {
			listener.onDeleteFeature(vectorFeature);
		}
	}

}
