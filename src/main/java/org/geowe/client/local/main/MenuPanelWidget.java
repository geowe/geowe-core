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
package org.geowe.client.local.main;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.main.tool.BasicToolBarTool;
import org.geowe.client.local.main.tool.ChangeStyleTool;
import org.geowe.client.local.main.tool.FeatureInfoTool;
import org.geowe.client.local.main.tool.PanTool;
import org.geowe.client.local.main.tool.SelectionTool;
import org.geowe.client.local.main.tool.W3wTool;
import org.geowe.client.local.main.tool.draw.CircleTool;
import org.geowe.client.local.main.tool.draw.HolePolygonTool;
import org.geowe.client.local.main.tool.draw.LineStringTool;
import org.geowe.client.local.main.tool.draw.PointTool;
import org.geowe.client.local.main.tool.draw.PolygonTool;
import org.geowe.client.local.main.tool.draw.RegularPolygonTool;
import org.geowe.client.local.main.tool.edition.CopyElementTool;
import org.geowe.client.local.main.tool.edition.DeleteTool;
import org.geowe.client.local.main.tool.edition.DivideTool;
import org.geowe.client.local.main.tool.edition.DragTool;
import org.geowe.client.local.main.tool.edition.ReshapeTool;
import org.geowe.client.local.main.tool.edition.ResizeTool;
import org.geowe.client.local.main.tool.edition.RotateTool;
import org.geowe.client.local.main.tool.edition.SnappingTool;
import org.geowe.client.local.main.tool.edition.UnionTool;
import org.geowe.client.local.main.tool.extent.CurrentExtentTool;
import org.geowe.client.local.main.tool.extent.CustomExtentTool;
import org.geowe.client.local.main.tool.extent.LockCurrentExtentTool;
import org.geowe.client.local.main.tool.info.WmsGetInfoTool;
import org.geowe.client.local.main.tool.layer.AddLayerTool;
import org.geowe.client.local.main.tool.layer.ExportLayerTool;
import org.geowe.client.local.main.tool.layer.LayerInfoTool;
import org.geowe.client.local.main.tool.layer.LayerManagerTool;
import org.geowe.client.local.main.tool.layer.OpenProjectTool;
import org.geowe.client.local.main.tool.layer.SaveLayerTool;
import org.geowe.client.local.main.tool.layer.SearchAttributeTool;
import org.geowe.client.local.main.tool.map.GraticuleTool;
import org.geowe.client.local.main.tool.map.LayerCatalogTool;
import org.geowe.client.local.main.tool.map.MapToolTipTool;
import org.geowe.client.local.main.tool.measure.MeasureAreaTool;
import org.geowe.client.local.main.tool.measure.MeasureElementTool;
import org.geowe.client.local.main.tool.measure.MeasureTool;
import org.geowe.client.local.main.tool.project.SaveProjectTool;
import org.geowe.client.local.main.tool.spatial.BufferTool;
import org.geowe.client.local.main.tool.spatial.CentroidTool;
import org.geowe.client.local.main.tool.spatial.EnvelopeTool;
import org.geowe.client.local.main.tool.spatial.GeometryValidationTool;
import org.geowe.client.local.main.tool.spatial.GeoprocessingTool;
import org.geowe.client.local.main.tool.zoom.ZoomBoxTool;
import org.geowe.client.local.main.tool.zoom.ZoomInTool;
import org.geowe.client.local.main.tool.zoom.ZoomNextTool;
import org.geowe.client.local.main.tool.zoom.ZoomOutTool;
import org.geowe.client.local.main.tool.zoom.ZoomPreviousTool;
import org.geowe.client.local.main.tool.zoom.ZoomToSeletionTool;
import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

@ApplicationScoped
public class MenuPanelWidget implements IsWidget {

	@Inject
	private LayerManagerTool layerManagerTool;
	@Inject
	private SelectionTool selectionTool;
	@Inject
	private ZoomBoxTool zoomBoxTool;
	@Inject
	private ZoomInTool zoomIn;
	@Inject
	private ZoomOutTool zoomOut;
	@Inject
	private ZoomNextTool zoomNext;
	@Inject
	private ZoomPreviousTool zoomPrev;
	@Inject
	private ZoomToSeletionTool zoomToSeletionTool;

	@Inject
	private MeasureTool measureTool;
	@Inject
	private MeasureAreaTool measureAreaTool;
	@Inject
	private MeasureElementTool measureElementTool;

	@Inject
	private PointTool pointTool;
	@Inject
	private CircleTool circleTool;
	@Inject
	private LineStringTool lineStringTool;
	@Inject
	private PolygonTool polygonTool;

	@Inject
	private RegularPolygonTool regularPolygonTool;
	@Inject
	private PanTool panTool;
	@Inject
	private DragTool dragTool;
	@Inject
	private ResizeTool resizeTool;
	@Inject
	private ReshapeTool reshapeTool;
	@Inject
	private RotateTool rotateTool;
	@Inject
	private DeleteTool deleteTool;
	@Inject
	private SnappingTool snappingTool;
	@Inject
	private HolePolygonTool holePolygonTool;
	@Inject
	private FeatureInfoTool infoFeatureTool;
	@Inject
	private ChangeStyleTool changeStyleTool;
	@Inject
	private LayerInfoTool layerInfoTool;

	@Inject
	private LayerCatalogTool layerCatalogTool;
	@Inject
	private GraticuleTool graticuleTool;
	@Inject
	private W3wTool w3wTool;

	@Inject
	private MapToolTipTool mapToolTipTool;

	@Inject
	private BufferTool bufferTool;
	@Inject
	private UnionTool unionTool;
	@Inject
	private DivideTool divideTool;
	@Inject
	private SearchAttributeTool searchAttributeTool;
	@Inject
	private EnvelopeTool envelopeTool;
	@Inject
	private CentroidTool centroidTool;
	@Inject
	private GeometryValidationTool geometricValidationTool;
	@Inject
	private GeoprocessingTool geometricAnalysisTool;
	@Inject
	private CopyElementTool copyElementTool;
	@Inject
	private BasicToolBarTool basicToolBarTool;
	@Inject
	private AddLayerTool addLayerTool;

	@Inject
	private WmsGetInfoTool wmsGetInfoTool;
	
	@Inject
	private CurrentExtentTool currentExtentTool;
	@Inject
	private LockCurrentExtentTool lockCurrentExtentTool;
	@Inject
	private CustomExtentTool customExtentTool;

	private ContentPanel panel;
	
	@Inject
	private SaveLayerTool saveLayerTool;
	@Inject
	private ExportLayerTool exportLayerTool;
	@Inject
	private SaveProjectTool saveProjectTool;
	@Inject
	private OpenProjectTool openProjectTool;
	
	@Override
	public Widget asWidget() {

		if (panel == null) {
			panel = new ContentPanel();
			panel.setBorders(true);
			panel.setPixelSize(300, 590);
			panel.setHeaderVisible(false);
			panel.setPosition(0, 50);
			panel.getElement().getStyle().setPosition(Position.ABSOLUTE);

			VerticalLayoutContainer groupsContainer = getVerticalGroupButtons();
			groupsContainer.setAdjustForScroll(true);
			groupsContainer.setScrollMode(ScrollMode.AUTOY);
			panel.setWidget(groupsContainer);

			panel.setVisible(false);
			createButtonGroup();
		}
		return panel;
	}

	private void createButtonGroup() {
		ToggleGroup group = new ToggleGroup();
		group.add(panTool);
		group.add(selectionTool);
		group.add(zoomBoxTool);
		group.add(measureTool);
		group.add(measureAreaTool);
		group.add(measureElementTool);
		group.add(circleTool);
		group.add(pointTool);
		group.add(lineStringTool);
		group.add(polygonTool);
		group.add(regularPolygonTool);
		group.add(dragTool);
		group.add(resizeTool);
		group.add(rotateTool);
		group.add(reshapeTool);
		group.add(deleteTool);
		group.add(holePolygonTool);
		group.add(infoFeatureTool);
		// group.add(bufferTool);
		group.add(divideTool);
		group.add(wmsGetInfoTool);
	}

	private VerticalLayoutContainer getVerticalGroupButtons() {
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(createMapTabPanel());
		verticalLayoutContainer.add(createFirstTabPanel());
		verticalLayoutContainer.add(createSecondTabPanel());

		return verticalLayoutContainer;
	}

	private TabPanel createMapTabPanel() {
		TabPanel tabPanel = new TabPanel();
		tabPanel.setBorders(true);
		tabPanel.setPixelSize(300, 100);
		tabPanel.add(getMapToolTab(), UIMessages.INSTANCE.mpMapLabel());
		tabPanel.add(getViewToolTab(), UIMessages.INSTANCE.viewText());
		tabPanel.add(getExtentToolTab(), UIMessages.INSTANCE.extentGroupTools());
		tabPanel.add(getProjectToolTab(), UIMessages.INSTANCE.projectGroupTools());
		
		return tabPanel;
	}

	private VerticalLayoutContainer getMapToolTab() {
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(getMapGroupTools());

		return verticalLayoutContainer;
	}

	private HorizontalPanel getMapGroupTools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.setSpacing(5);
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);

		horizontalGroup.add(layerManagerTool);
		horizontalGroup.add(layerCatalogTool);
		horizontalGroup.add(basicToolBarTool);

		return horizontalGroup;
	}

	private Widget getViewToolTab() {
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(getViewGroupTools());

		return verticalLayoutContainer;
	}

	private HorizontalPanel getViewGroupTools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.setSpacing(5);
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);

		horizontalGroup.add(w3wTool);
		horizontalGroup.add(mapToolTipTool);
		horizontalGroup.add(graticuleTool);

		return horizontalGroup;
	}
	
	private VerticalLayoutContainer getExtentToolTab() {
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(getExtentGroupTools());

		return verticalLayoutContainer;
	}
	
	private HorizontalPanel getExtentGroupTools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.setSpacing(5);
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);

		horizontalGroup.add(currentExtentTool);
		horizontalGroup.add(customExtentTool);		
		horizontalGroup.add(lockCurrentExtentTool);
		
		return horizontalGroup;
	}
	
	private VerticalLayoutContainer getProjectToolTab() {
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(getProjectGroupTools());

		return verticalLayoutContainer;
	}
	
	private HorizontalPanel getProjectGroupTools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.setSpacing(5);
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);

		horizontalGroup.add(openProjectTool);
		horizontalGroup.add(saveProjectTool);		
		
		
		return horizontalGroup;
	}

	private TabPanel createFirstTabPanel() {
		TabPanel tabPanel = new TabPanel();
		tabPanel.setBorders(true);
		tabPanel.setPixelSize(300, 190);
		tabPanel.add(getLayerToolTab(), UIMessages.INSTANCE.mpLayersLabel());
		tabPanel.add(getMeasureToolTab(),
				UIMessages.INSTANCE.mpMeasureToolsLabel());
		tabPanel.add(getZoomToolTab(), UIMessages.INSTANCE.mpZoomToolsLabel());
		tabPanel.add(getSaveToolTab(), UIMessages.INSTANCE.save());
		return tabPanel;
	}

	private TabPanel createSecondTabPanel() {
		TabPanel tabPanel = new TabPanel();
		tabPanel.setBorders(true);
		tabPanel.setPixelSize(300, 190);
		tabPanel.add(getDrawToolTab(), UIMessages.INSTANCE.mpDrawToolsLabel());
		tabPanel.add(getEditionToolTab(),
				UIMessages.INSTANCE.mpEditionToolsLabel());
		tabPanel.add(getSpatialToolTab(),
				UIMessages.INSTANCE.mpSpatialToolsLabel());

		return tabPanel;
	}

	private VerticalLayoutContainer getLayerToolTab() {
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(getLayerGroupTools());
		verticalLayoutContainer.add(getLayer2GroupTools());
		return verticalLayoutContainer;
	}

	private HorizontalPanel getLayerGroupTools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.setSpacing(5);
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);

		horizontalGroup.add(addLayerTool);
		horizontalGroup.add(layerInfoTool);
		horizontalGroup.add(changeStyleTool);

		return horizontalGroup;
	}

	private HorizontalPanel getLayer2GroupTools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.setSpacing(5);
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);

		horizontalGroup.add(searchAttributeTool);
		horizontalGroup.add(snappingTool);
		horizontalGroup.add(geometricAnalysisTool);

		return horizontalGroup;
	}

	private VerticalLayoutContainer getMeasureToolTab() {
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(getMeasureToolGroupTools());
		// verticalLayoutContainer.add(getEmptyHorizontalPanel());
		return verticalLayoutContainer;
	}

	private HorizontalPanel getMeasureToolGroupTools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);
		horizontalGroup.setSpacing(5);

		horizontalGroup.add(measureAreaTool);
		horizontalGroup.add(measureTool);
		horizontalGroup.add(measureElementTool);

		return horizontalGroup;
	}

	private VerticalLayoutContainer getZoomToolTab() {
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(getZoomToolGroupTools());
		verticalLayoutContainer.add(getZoomToolGroup2Tools());

		return verticalLayoutContainer;
	}

	private HorizontalPanel getZoomToolGroupTools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);
		horizontalGroup.setSpacing(5);

		horizontalGroup.add(zoomPrev);
		horizontalGroup.add(zoomNext);
		horizontalGroup.add(zoomBoxTool);

		return horizontalGroup;
	}

	private HorizontalPanel getZoomToolGroup2Tools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);
		horizontalGroup.setSpacing(5);

		horizontalGroup.add(zoomIn);
		horizontalGroup.add(zoomOut);
		horizontalGroup.add(zoomToSeletionTool);

		return horizontalGroup;
	}
	
	private VerticalLayoutContainer getSaveToolTab() {
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(getSaveToolGroup1Tools());

		return verticalLayoutContainer;
	}
	
	private Widget getSaveToolGroup1Tools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);
		horizontalGroup.setSpacing(5);
		horizontalGroup.add(saveLayerTool);
		horizontalGroup.add(exportLayerTool);
		//horizontalGroup.add(saveProjectTool);
		return horizontalGroup;
	}

	private Widget getDrawToolTab() {
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(getDrawToolGroup1Tools());
		verticalLayoutContainer.add(getDrawToolGroup2Tools());

		return verticalLayoutContainer;
	}

	private HorizontalPanel getDrawToolGroup1Tools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);
		horizontalGroup.setSpacing(5);
		horizontalGroup.add(polygonTool);
		horizontalGroup.add(holePolygonTool);
		horizontalGroup.add(regularPolygonTool);

		return horizontalGroup;
	}

	private HorizontalPanel getDrawToolGroup2Tools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);
		horizontalGroup.setSpacing(5);
		horizontalGroup.add(lineStringTool);
		horizontalGroup.add(pointTool);
		horizontalGroup.add(circleTool);

		return horizontalGroup;
	}

	private Widget getEditionToolTab() {
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(getEditionToolGroup1Tools());
		verticalLayoutContainer.add(getEditionToolGroup2Tools());

		return verticalLayoutContainer;
	}

	private HorizontalPanel getEditionToolGroup1Tools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);
		horizontalGroup.setSpacing(5);

		horizontalGroup.add(reshapeTool);
		horizontalGroup.add(resizeTool);
		horizontalGroup.add(rotateTool);

		return horizontalGroup;
	}

	private HorizontalPanel getEditionToolGroup2Tools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);
		horizontalGroup.setSpacing(5);

		horizontalGroup.add(dragTool);
		horizontalGroup.add(deleteTool);
		horizontalGroup.add(copyElementTool);

		return horizontalGroup;
	}

	private Widget getSpatialToolTab() {
		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(getSpatialToolGroup1Tools());
		verticalLayoutContainer.add(getSpatialToolGroup2Tools());

		return verticalLayoutContainer;
	}

	private HorizontalPanel getSpatialToolGroup1Tools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);
		horizontalGroup.setSpacing(5);

		horizontalGroup.add(unionTool);
		horizontalGroup.add(divideTool);
		horizontalGroup.add(geometricValidationTool);

		return horizontalGroup;
	}

	private HorizontalPanel getSpatialToolGroup2Tools() {
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);
		horizontalGroup.setSpacing(5);

		horizontalGroup.add(bufferTool);
		horizontalGroup.add(envelopeTool);
		horizontalGroup.add(centroidTool);

		return horizontalGroup;
	}
}
