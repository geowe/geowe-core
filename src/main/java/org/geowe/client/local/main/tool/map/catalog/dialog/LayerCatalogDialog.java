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
package org.geowe.client.local.main.tool.map.catalog.dialog;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.main.tool.map.catalog.model.LayerDef;
import org.geowe.client.local.messages.UICatalogMessages;
import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowExpander;

/**
 * Dialog representing the catalog of layers. It is responsible for displaying the
 * available layers and provide user functionality to add more layers to the map.
 * 
 * @author Atanasio Muñoz
 *
 */
@ApplicationScoped
public class LayerCatalogDialog extends Dialog {
	private ListStore<LayerDef> availableStore;
	private ListStore<LayerDef> currentStore;
	private ListStore<LayerDef> addedStore;
	
	private Grid<LayerDef> availableGrid;
	private Grid<LayerDef> addedGrid;
		
	private static final LayerDefProperties layerProps = GWT.create(LayerDefProperties.class);

	public LayerCatalogDialog() {				
		// this.setHeadingText("Layer Catalog");
		this.setHeadingText(UIMessages.INSTANCE.catalogTitle());
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.setPixelSize(850, 425);
		this.setModal(true);
		this.setResizable(false);
		this.setHideOnButtonClick(true);
		this.add(createLayout());
	}
	
	public void initialize(List<LayerDef> availableLayers, List<LayerDef> currentLayers) {
		availableStore.clear();
		currentStore.clear();
		addedStore.clear();
		
		availableStore.addAll(availableLayers);
		currentStore.addAll(currentLayers);		
	}
	
	public List<LayerDef> getAddedLayers() {
		return addedStore.getAll();
	}

	private HorizontalLayoutContainer createLayout() {
		availableStore = new ListStore<LayerDef>(layerProps.key());		
		currentStore = new ListStore<LayerDef>(layerProps.key());
		addedStore = new ListStore<LayerDef>(layerProps.key());	
		
		Grid<LayerDef> currentGrid = createGrid(currentStore, layerProps);
		availableGrid = createDraggableGrid(availableStore, layerProps);		
		addedGrid = createDraggableGrid(addedStore, layerProps);
		addedGrid.setToolTip(UIMessages.INSTANCE.catalogGridToolTip());
		
		HorizontalLayoutContainer mainContainer = new HorizontalLayoutContainer();		
		
		VerticalPanel availableContainer = new VerticalPanel();
		availableContainer.setWidth("380px");
		availableContainer.setSpacing(5);
		availableContainer.add(new Label(UIMessages.INSTANCE
				.catalogAvailableLayers()));
		availableContainer.add(availableGrid);
		availableGrid.setHeight("315px");		
		
		VerticalPanel buttonsContainer = new VerticalPanel();	
		buttonsContainer.setWidth("40px");		
		buttonsContainer.getElement().getStyle().setPaddingLeft(20, Unit.PX);
		buttonsContainer.getElement().getStyle().setPaddingRight(20, Unit.PX);		
		buttonsContainer.getElement().getStyle().setPaddingTop(40, Unit.PX);
		final TextButton addButton = new TextButton();		
		addButton.getElement().setId("addButton");
		addButton.setIcon(ImageProvider.INSTANCE.right());
		addButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				addSelectedAvailableLayers();				
			}			
		});
		final TextButton removeButton = new TextButton();
		removeButton.getElement().setId("removeButton");
		removeButton.setIcon(ImageProvider.INSTANCE.left());
		removeButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				removeSelectedAddedLayers();				
			}			
		});
		buttonsContainer.add(addButton);
		buttonsContainer.add(removeButton);
		
		VerticalPanel addedContainer = new VerticalPanel();
		addedContainer.setWidth("380px");
		addedContainer.setSpacing(5);		
		addedContainer.add(new Label(UIMessages.INSTANCE
				.catalogLayerAddedLabel()));
		addedContainer.add(addedGrid);
		addedGrid.setHeight("147px");
		addedContainer.add(new Label(UIMessages.INSTANCE
				.catalogLayersCurrentMapLabel()));
		addedContainer.add(currentGrid);
		currentGrid.setHeight("140px");

		mainContainer.add(availableContainer);
		mainContainer.add(buttonsContainer);
		mainContainer.add(addedContainer);
		
		return mainContainer;
	}
	
	private Grid<LayerDef> createDraggableGrid(ListStore<LayerDef> dataStore,
			LayerDefProperties properties) {
		
		Grid<LayerDef> grid = createGrid(dataStore, properties);		
		new GridDragSource<LayerDef>(grid).setGroup("layers");
		
		GridDropTarget<LayerDef> dropTarget = new GridDropTarget<LayerDef>(grid);
		dropTarget.setFeedback(Feedback.INSERT);
		dropTarget.setGroup("layers");
		
		return grid;
	}
	
	private Grid<LayerDef> createGrid(ListStore<LayerDef> dataStore,
			LayerDefProperties properties) {
		
		RowExpander<LayerDef> rowExpander = createRowExpander();
		ColumnModel<LayerDef> columnModel = createColumnList(properties, rowExpander);
		Grid<LayerDef> grid = new Grid<LayerDef>(dataStore, columnModel);				
		grid.setBorders(true);
		grid.getView().setForceFit(true);				
		grid.getView().setAutoExpandColumn(columnModel.getColumn(2));
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		grid.setBorders(true);
		grid.setColumnReordering(true);
		rowExpander.initPlugin(grid);	
		
		return grid;
	}
	
	private ColumnModel<LayerDef> createColumnList(LayerDefProperties props, 
			RowExpander<LayerDef> rowExpander) {
		
		rowExpander.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rowExpander.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		ColumnConfig<LayerDef, String> nameColumn = new ColumnConfig<LayerDef, String>(
				props.name(), 200, SafeHtmlUtils.fromTrustedString("<b>"
						+ UIMessages.INSTANCE.layerManagerToolText() + "</b>"));
		nameColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);		
		
		ColumnConfig<LayerDef, String> typeColumn = new ColumnConfig<LayerDef, String>(
				props.type(), 75, UICatalogMessages.INSTANCE.type());
		typeColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		typeColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);		
		
		ColumnConfig<LayerDef, ImageResource> iconColumn = new ColumnConfig<LayerDef, ImageResource>(
				props.icon(), 32, "");
		iconColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		iconColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		iconColumn.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				super.render(context, value, sb);
			}
		});
				
		List<ColumnConfig<LayerDef, ?>> columns = new ArrayList<ColumnConfig<LayerDef, ?>>();
		columns.add(rowExpander);
		columns.add(iconColumn);		
		columns.add(nameColumn);
		columns.add(typeColumn);		
		
		return new ColumnModel<LayerDef>(columns);
	}

	private RowExpander<LayerDef> createRowExpander() {
		return new RowExpander<LayerDef>(new AbstractCell<LayerDef>() {
			@Override
			public void render(Context context, LayerDef value,
					SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<p style='margin: 5px 5px 10px'><b>"
						+ UICatalogMessages.INSTANCE.description() + ":</b> "
						+ value.getDescription() + "</p>");
			}
		});
	}
	
	private void addSelectedAvailableLayers() {
		List<LayerDef> selectedLayers = availableGrid.getSelectionModel().getSelectedItems();
		addedStore.addAll(selectedLayers);
		
		for(LayerDef layer : selectedLayers) {
			availableStore.remove(layer);
		}
	}
	
	private void removeSelectedAddedLayers() {
		List<LayerDef> selectedLayers = addedGrid.getSelectionModel().getSelectedItems();
		availableStore.addAll(selectedLayers);
		
		for(LayerDef layer : selectedLayers) {
			addedStore.remove(layer);
		}
	}	
}
