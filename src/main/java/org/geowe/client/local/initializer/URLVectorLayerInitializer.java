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
package org.geowe.client.local.initializer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.map.catalog.AppLayerCatalog;
import org.geowe.client.local.main.tool.map.catalog.LayerLoader;
import org.geowe.client.local.main.tool.map.catalog.model.LayerDef;
import org.geowe.client.local.main.tool.map.catalog.model.MultiLayerDef;
import org.geowe.client.local.main.tool.map.catalog.model.URLVectorLayerDef;
import org.geowe.client.local.main.tool.map.catalog.model.VectorLayerDef;
import org.geowe.client.local.messages.UICatalogMessages;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.gwtopenmaps.openlayers.client.layer.Layer;

import com.google.gwt.user.client.Window;

/**
 * Responsible to load layers definded in url param.
 * 
 * @author geowe.org
 * @author jose@geowe.org, rafa@geowe.org, ata@geowe.org
 *
 */
@ApplicationScoped
public class URLVectorLayerInitializer {
	private final ProgressBarDialog autoMessageBox = new ProgressBarDialog(
			false, UIMessages.INSTANCE.processing());

	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	@Inject
	private AppLayerCatalog appLayerCatalog;

	public void createLayerFromURL() {
		if (hasParam(getLayerinfo("layerCatalog"))) {
			createLayerFromCatalog(getLayerinfo("layerCatalog"));
		}

		if (hasParam(getLayerinfo("layerUrl"))) {
			createLayer(getLayerinfo("layerUrl"));
		}
	}

	private String getLayerinfo(final String paramName) {
		return Window.Location.getParameter(paramName);
	}

	private boolean hasParam(final String param) {
		boolean hasParam = false;
		if (param != null && !param.isEmpty()) {
			hasParam = true;
		}
		return hasParam;
	}

	private void createLayerFromCatalog(final String layerCatalog) {
		final LayerDef layerDef = appLayerCatalog.getLayer(layerCatalog);
		if (layerDef == null) {
			autoMessageBox.hide();
			messageDialogBuilder.createWarning(UIMessages.INSTANCE.warning(),
					UICatalogMessages.INSTANCE.layerNotInCatalog(layerCatalog))
					.show();
		} else {
			if (layerDef instanceof MultiLayerDef) {
				layerDef.getLayer(); // Internamente realiza la carga de capas
			} else {
				autoMessageBox.show();
				final Layer newLayer = layerDef.getLayer();
				LayerLoader.load(newLayer);
				autoMessageBox.hide();
			}
		}
	}

	private void createLayer(final String layerUri) {
		try {
			
			final URLVectorLayerDef urlLayerDef = new URLVectorLayerDef();		
			urlLayerDef.setEpsg(getLayerinfo("layerProj") == null ? GeoMap.INTERNAL_EPSG
					: getLayerinfo("layerProj"));
			urlLayerDef.setFormat(getLayerinfo("layerFormat") == null ? VectorLayerDef.KML
					: getLayerinfo("layerFormat"));
			urlLayerDef.setName(getLayerinfo("layerName") == null ? "url-layer"
					: getLayerinfo("layerName"));
			urlLayerDef.setUrl(layerUri);
			urlLayerDef.setType(LayerDef.VECTOR_TYPE);
			urlLayerDef.load();		
			
		} catch (Exception e) {
			messageDialogBuilder.createError("error", e.getMessage()).show();
		}
	}
}