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
package org.geowe.client.local.layermanager.toolbar;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.geowe.client.local.layermanager.tool.DeleteLayerTool;
import org.geowe.client.local.layermanager.tool.InfoRasterTool;
import org.geowe.client.local.layermanager.tool.create.AddRasterLayerTool;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.widget.core.client.ContentPanel;

/**
 * Barra de herramientas para capas raster en el Administrador de capas
 * 
 * @author jose@geowe.org
 * 
 * Se añade crea la herramienta AddRasterLayer que contempla WMS y WMTS 
 * @author jose@geowe.org
 * @since 25/08/2016
 */
@ApplicationScoped
public class RasterLayerToolBar extends ContentPanel {
	
	@Inject @New
	private DeleteLayerTool deleteLayerTool;
	@Inject @New
	private InfoRasterTool infoRasterTool;
	@Inject
	private AddRasterLayerTool addRasterLayerTool;
	
	private final HorizontalPanel horizontalGroup;
	
	public RasterLayerToolBar(){
		super();
		setHeaderVisible(false);
		addStyleName(ThemeStyles.get().style().borderBottom());
		horizontalGroup = new HorizontalPanel();
		horizontalGroup.setSpacing(5);	
	}
	
	@PostConstruct
	private void initialize(){
		horizontalGroup.add(addRasterLayerTool);				
		horizontalGroup.add(deleteLayerTool);
		horizontalGroup.add(infoRasterTool);
		setWidget(horizontalGroup);
	}	

}
