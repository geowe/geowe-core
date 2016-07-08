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
package org.geowe.client.local.main.tool.schema;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.AttributeType;
import org.geowe.client.local.model.vector.FeatureAttributeDef;
import org.geowe.client.local.model.vector.VectorLayer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * Layer Attributes dialog
 * 
 * @author ata@geowe.org
 *
 */
@ApplicationScoped
public class FeatureSchemaDialog extends Dialog {
	
	private ListStore<FeatureAttributeEditingDef> featureAttributes;
	private Grid<FeatureAttributeEditingDef> grid;
	private GridEditing<FeatureAttributeEditingDef> gridEditing;
	
	private VectorLayer vectorLayer;
	private int attrOrdinal = 0;

	public FeatureSchemaDialog() {
		super();
		setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		setButtonAlign(BoxLayoutPack.CENTER);
		setHideOnButtonClick(true);
		setResizable(true);
		setWidth(435);
		setHeight(300);
				
		add(createPanel());		
	}
	
	private Widget createPanel() {
		VerticalLayoutContainer vPanel = new VerticalLayoutContainer();
		vPanel.setBorders(true);		

		// Creacion del Grid Base 
		FeatureAttributeEditingDefProperties props = GWT.create(FeatureAttributeEditingDefProperties.class);		
		featureAttributes = new ListStore<FeatureAttributeEditingDef>(props.key());
		featureAttributes.setAutoCommit(true);
		
		ColumnConfig<FeatureAttributeEditingDef, String> nameCol = new ColumnConfig<FeatureAttributeEditingDef, String>(
				props.name(), 170, UIMessages.INSTANCE.fedColumnNameText());
		ColumnConfig<FeatureAttributeEditingDef, String> newNameCol = new ColumnConfig<FeatureAttributeEditingDef, String>(
				props.newName(), 170,
				UIMessages.INSTANCE.fedNewAttributeDefaultNameText());
		ColumnConfig<FeatureAttributeEditingDef, Boolean> showInTooltipCol = new ColumnConfig<FeatureAttributeEditingDef, Boolean>(
				props.showInTooltip(), 70, "Tooltip");
		showInTooltipCol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		showInTooltipCol.setCell(new CheckBoxCell());
		showInTooltipCol.setSortable(false);

		List<ColumnConfig<FeatureAttributeEditingDef, ?>> columns = new ArrayList<ColumnConfig<FeatureAttributeEditingDef, ?>>();
		columns.add(nameCol);
		columns.add(newNameCol);
		// columns.add(typeCol);
		columns.add(showInTooltipCol);
		
		grid = new Grid<FeatureAttributeEditingDef>(featureAttributes, 
				new ColumnModel<FeatureAttributeEditingDef>(columns));
		grid.setSelectionModel(new CellSelectionModel<FeatureAttributeEditingDef>());		 
	    grid.getColumnModel().getColumn(1).setHideable(false);	    
		grid.setAllowTextSelection(true);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		grid.setBorders(false);
		setGridDragable();

	 
		// Definicion del Grid Editable
		gridEditing = new GridInlineEditing<FeatureAttributeEditingDef>(grid);
		gridEditing.addEditor(newNameCol, new TextField());
		gridEditing.addEditor(showInTooltipCol, new CheckBox());
		
		//Herramientas de edicion
		TextButton addButton = new TextButton(UIMessages.INSTANCE.fedAddButtonText());
		addButton.addSelectHandler(new SelectHandler() {
	        @Override
	        public void onSelect(SelectEvent event) {
	        	addAttribute();
	        }
		});
		
		TextButton removeButton = new TextButton(UIMessages.INSTANCE.fedRemoveButtonText());
		removeButton.addSelectHandler(new SelectHandler() {
	        @Override
	        public void onSelect(SelectEvent event) {
	        	removeAttribute();
	        }
		});		
	 
	    ToolBar toolBar = new ToolBar();
	    toolBar.add(addButton);		
	    toolBar.add(removeButton);
		
	    vPanel.add(toolBar, new VerticalLayoutData(1, -1, new Margins(10, 0, 0, 0)));
	    vPanel.add(grid, new VerticalLayoutData(1, 1, new Margins(5, 0, 0, 0)));
	    
		return vPanel;
	}
	
	private void setGridDragable() {
		new GridDragSource<FeatureAttributeEditingDef>(grid);

		GridDropTarget<FeatureAttributeEditingDef> gridDrop = new GridDropTarget<FeatureAttributeEditingDef>(
				grid);
		gridDrop.setAllowSelfAsSource(true);
		gridDrop.setFeedback(Feedback.BOTH);
	}

	private void updateDataGrid() {
		featureAttributes.clear();
		
		List<FeatureAttributeEditingDef> attributes = new ArrayList<FeatureAttributeEditingDef>();
		for(FeatureAttributeDef featureAttr : vectorLayer.getAttributes()) {
			attributes.add(new FeatureAttributeEditingDef((++attrOrdinal), featureAttr));
		}
		
		featureAttributes.addAll(attributes);
		setHeadingText(UIMessages.INSTANCE
				.fedHeadingText(vectorLayer.getName()));
	}	
	
	public VectorLayer getVectorLayer() {
		return vectorLayer;
	}

	public void setVectorLayer(VectorLayer vectorLayer) {
		this.vectorLayer = vectorLayer;
		this.attrOrdinal = 0;
		
		updateDataGrid();		
	}
	
	public List<FeatureAttributeEditingDef> getSchemaEditing() {
		return featureAttributes.getAll();
	}
	
	private void addAttribute() {		
		FeatureAttributeEditingDef attribute = new FeatureAttributeEditingDef();
		attribute.setId((++attrOrdinal));
		attribute.setNewName(UIMessages.INSTANCE
				.fedNewAttributeDefaultNameText());
		attribute.setType(AttributeType.STRING);

        gridEditing.cancelEditing();
        featureAttributes.add(attribute);       
        
        int row = featureAttributes.indexOf(attribute);
        gridEditing.startEditing(new GridCell(row, 1));		
	}
	
	private void removeAttribute() {		
		featureAttributes.remove(grid.getSelectionModel().getSelectedItem());
	}
}
