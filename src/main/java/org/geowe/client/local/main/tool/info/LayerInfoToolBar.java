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
package org.geowe.client.local.main.tool.info;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.geowe.client.local.main.tool.search.ExportCSVLayerTool;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;

@ApplicationScoped
public class LayerInfoToolBar extends ContentPanel {
	public static final int ZOOM_TOOL = 1;
	public static final int SELECT_TOOL = 2;
	public static final int DELETE_TOOL = 3;
	public static final int INFO_TOOL = 4;
	public static final int EXPORT_TOOL = 5;
	
	@Inject
	@New
	private ZoomToVectorFeatureTool zoomToVectorFeatureTool;	
	@Inject
	@New
	private SelectVectorFeatureTool selectVectorFeatureTool;
	@Inject
	private SingleFeatureInfoTool singleFeatureInfoTool;
	@Inject
	private DeleteVectorFeatureTool deleteVectorFeatureTool;
	@Inject
	@New
	private ExportCSVLayerTool exportCSVLayerTool;
	

	private final List<FeatureTool> tools;
	private final VerticalPanel verticalGroup;
	

	public LayerInfoToolBar() {
		super();
		setHeight("200px");
		setHeaderVisible(false);		
		
		tools = new ArrayList<FeatureTool>();
		verticalGroup = new VerticalPanel();		
		verticalGroup.setSpacing(3);
		verticalGroup.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
	}
	
	@PostConstruct
	private void initialize(){		
		setWidget(verticalGroup);
	}		
	
	public void setAllTools() {
		setTools(ZOOM_TOOL, SELECT_TOOL, DELETE_TOOL, INFO_TOOL, EXPORT_TOOL);
	}
	
	public void setTools(int... toolIDs) {
		for(int toolID : toolIDs) {
			switch(toolID) {
				case ZOOM_TOOL:
					addTool(zoomToVectorFeatureTool);
					break;
				case SELECT_TOOL:
					addTool(selectVectorFeatureTool);
					break;
				case DELETE_TOOL:
					addTool(deleteVectorFeatureTool);
					break;
				case INFO_TOOL:
					addTool(singleFeatureInfoTool);
					break;
				case EXPORT_TOOL:
					addTool(exportCSVLayerTool);
			}
		}
	}
	
	@Override
	public void addTool(Widget tool) {
		verticalGroup.add(tool);
		tools.add((FeatureTool) tool);
	}
	
	public List<FeatureTool> getTools() {
		return tools;
	}
}
