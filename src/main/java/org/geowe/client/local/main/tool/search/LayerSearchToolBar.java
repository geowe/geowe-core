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
package org.geowe.client.local.main.tool.search;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.geowe.client.local.main.tool.info.FeatureTool;
import org.geowe.client.local.main.tool.info.SelectVectorFeatureTool;
import org.geowe.client.local.main.tool.info.SingleFeatureInfoTool;
import org.geowe.client.local.main.tool.info.ZoomToVectorFeatureTool;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;

@ApplicationScoped
public class LayerSearchToolBar extends ContentPanel {

	@Inject
	@New
	private ZoomToVectorFeatureTool zoomToVectorFeatureTool;
	@Inject
	@New
	private SingleFeatureInfoTool singleFeatureInfoTool;
	@Inject
	@New
	private SelectVectorFeatureTool selectVectorFeatureTool;
	@Inject
	@New
	private ExportCSVLayerTool exportCSVLayerTool;
	@Inject
	private CreateNewLayerTool createNewLayerTool;

	private final List<FeatureTool> tools;
	private final VerticalPanel verticalGroup;

	public LayerSearchToolBar() {
		super();
		setHeight("200px");
		setHeaderVisible(false);

		tools = new ArrayList<FeatureTool>();
		verticalGroup = new VerticalPanel();
		verticalGroup.setSpacing(3);
	}

	@PostConstruct
	private void initialize() {
		addTool(zoomToVectorFeatureTool);
		addTool(selectVectorFeatureTool);
		addTool(singleFeatureInfoTool);
		addTool(createNewLayerTool);
		addTool(exportCSVLayerTool);
		setWidget(verticalGroup);
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
