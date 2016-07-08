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

import org.geowe.client.local.layermanager.tool.CopyLayerTool;
import org.geowe.client.local.layermanager.tool.DeleteLayerTool;
import org.geowe.client.local.layermanager.tool.InfoLayerTool;
import org.geowe.client.local.layermanager.tool.LayerSchemaTool;
import org.geowe.client.local.layermanager.tool.ZoomToVectorLayerTool;
import org.geowe.client.local.layermanager.tool.create.AddVectorLayerTool;
import org.geowe.client.local.layermanager.tool.export.ExportDataTool;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.widget.core.client.ContentPanel;

@ApplicationScoped
public class VectorLayerToolBar extends ContentPanel {
	
	@Inject 
	private ExportDataTool exportDataTool;
	@Inject @New
	private DeleteLayerTool deleteLayerTool;
	@Inject
	private ZoomToVectorLayerTool zoomToVectorLayerTool;
	@Inject @New
	private InfoLayerTool infoLayerTool;
	@Inject
	private AddVectorLayerTool addVectorLayerTool;
	@Inject
	private CopyLayerTool copyLayerTool;
	@Inject
	private LayerSchemaTool layerSchemaTool;
	
	private final HorizontalPanel horizontalGroup;
		
	public VectorLayerToolBar(){
		super();
		setHeaderVisible(false);
		addStyleName(ThemeStyles.get().style().borderBottom());
		horizontalGroup = new HorizontalPanel();
		horizontalGroup.setSpacing(5);
	
	}
	
	@PostConstruct
	private void initialize(){
		horizontalGroup.add(addVectorLayerTool);
		horizontalGroup.add(deleteLayerTool);
		horizontalGroup.add(zoomToVectorLayerTool);
		horizontalGroup.add(infoLayerTool);		
		horizontalGroup.add(exportDataTool);
		horizontalGroup.add(copyLayerTool);
		horizontalGroup.add(layerSchemaTool);
		
		setWidget(horizontalGroup);
	}	
}
