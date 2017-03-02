/*
 * #%L
 * GeoWE Project
 * %%
 * Copyright (C) 2015 - 2017 GeoWE.org
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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.geowe.client.local.main.tool.help.HelpMessages;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.EditingFeatureGrid;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.PageSizeComboBox;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

/**
 * Diálogo de edición directa de los atributos alfanuméricos de las
 * features de una capa a través de un grid de datos
 * 
 * @author Atanasio Muñoz (ata@geowe.org)
 *
 */
@ApplicationScoped
public class LayerEditDialog extends Dialog {
	public static final int HORIZONTAL_MARGIN = 50;
	public static final int ROW_SIZE = 32;
	public static final int VISIBLE_ROWS = 15;
	
	@Inject
	@New
	private LayerInfoToolBar layerInfoToolBar;
	@Inject
	private MessageDialogBuilder dialogBuilder;
	
	private VectorLayer selectedLayer;
	private EditingFeatureGrid grid;
	private PagingToolBar pagingToolBar;
	private PageSizeComboBox pageSize;
	
	public LayerEditDialog() {
		super();
		
		setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		setButtonAlign(BoxLayoutPack.CENTER);
		setHideOnButtonClick(true);
		setResizable(true);
		setMaximizable(true);
		setWidth(computeWidth());
		setHeight(computeHeight());				
	}
	
	public void setSelectedLayer(VectorLayer layer) {
		if(layer != null) {
			String previousName = this.selectedLayer != null ? 
					this.selectedLayer.getName() : "";
			boolean changedLayer = !layer.getName().equalsIgnoreCase(previousName);			
			this.selectedLayer = layer;
					
			VectorFeature[] features = getLayerFeatures();
			if(changedLayer) {											
				pageSize.reset(features.length);								
				pageSize.setValue(pageSize.getMaxValue());				
				pagingToolBar.setPageSize(pageSize.getValue());				
				grid.rebuild(features);
			} else {
				grid.update(features);
			}			
			
			setHeadingText(UIMessages.INSTANCE.lidHeadText() + ": " + 
					selectedLayer.getName());			
		}
	}
	
	private VectorFeature[] getLayerFeatures() {
		return this.selectedLayer.getFeatures() != null 
				? this.selectedLayer.getFeatures()
				: new VectorFeature[0];	
	}
	
	@PostConstruct
	private void initialize() {
		add(createPanel());
		addButtonHandlers();
		/**
		 * Es necesario mostrar durante un instante el diálogo para que
		 * la configuración del tamaño y del grid se realice correctamente
		 */
		this.show();
		this.hide();
	}
	
	private int computeWidth() {
		return Window.getClientWidth() - (HORIZONTAL_MARGIN * 2);
	}
	
	private int computeHeight() {		
		return VISIBLE_ROWS * ROW_SIZE;		
	}
	
	private Widget createPanel() {			
		pagingToolBar = new PagingToolBar(VISIBLE_ROWS);
		pagingToolBar.setBorders(false);		
		
		grid = new EditingFeatureGrid(pagingToolBar);
		grid.setEnableCellRender(true);
		grid.getFeatureGrid().getSelectionModel().addSelectionChangedHandler(
				new SelectionChangedHandler<VectorFeature>() {
					@Override
					public void onSelectionChanged(
							SelectionChangedEvent<VectorFeature> event) {
						setSelectedElement();
					}
				});
		
		pageSize = new PageSizeComboBox("75px");		
		pageSize.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				pagingToolBar.setPageSize(event.getValue());				
				grid.rebuild(getLayerFeatures());
								
				/**
				 * Si no se están mostrando todas las features de la capa,
				 * es decir, si existe paginación, se activa el auto-commit 
				 * para evitar perder los datos al cambiar de página
				 */
				boolean multiPage = event.getValue() < getLayerFeatures().length;
				grid.setAutoCommit(multiPage);
				if(multiPage) {
					dialogBuilder.createWarning(
							UIMessages.INSTANCE.lidWarningDialogHeadText(), 
							UIMessages.INSTANCE.lidWarningDialogBodytText()).show();
				}
			}			
		});
		pageSize.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				pageSize.setValue(event.getSelectedItem(), true);
			}
		});
							
		final VerticalLayoutContainer gridContainer = new VerticalLayoutContainer();		
		gridContainer.addStyleName(ThemeStyles.get().style().borderBottom());					
		gridContainer.add(grid.getFeatureGrid(), 
				new VerticalLayoutData(1, 1));
		
		HorizontalLayoutContainer pagingContainer = new HorizontalLayoutContainer();
		pagingContainer.setHeight(38);		
		pagingContainer.add(pagingToolBar, new HorizontalLayoutData(1, -1));
		pagingContainer.add(new FieldLabel(pageSize, "| " 
				+ UIMessages.INSTANCE.lidElementesPerPage()), 
				new HorizontalLayoutData(-1, -1, new Margins(6, 0, 0, 5)));
		gridContainer.add(pagingContainer, 
				new VerticalLayoutData(1, -1));

		layerInfoToolBar.setTools(LayerInfoToolBar.ZOOM_TOOL, 
				LayerInfoToolBar.SELECT_TOOL, 
				LayerInfoToolBar.EXPORT_TOOL);
		layerInfoToolBar.setHeight(125);		
		
		HorizontalLayoutContainer hPanel = new HorizontalLayoutContainer();
		hPanel.add(gridContainer, 
				new HorizontalLayoutData(1, 1, new Margins(5, 0, 0, 5)));		
		hPanel.add(layerInfoToolBar, 
				new HorizontalLayoutData(-1, -1, new Margins(5, 0, 0, 0)));		
		
		final ToolButton tButton = new ToolButton(ToolButton.QUESTION);
		setHelpToolTip(tButton);
		addHelpSelectHandler(tButton);

		getHeader().addTool(tButton);

		return hPanel;
	}
		
	private void addHelpSelectHandler(final ToolButton tButton) {
		tButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(final SelectEvent event) {
				tButton.getToolTip().show();
			}
		});
	}

	private void setHelpToolTip(final ToolButton tButton) {
		final ToolTipConfig helpToolTip = new ToolTipConfig();
		helpToolTip.setTitleText(HelpMessages.INSTANCE.help());
		helpToolTip.setBodyHtml(HelpMessages.INSTANCE
				.featureInfoDialogContent());
		helpToolTip.setCloseable(true);
		tButton.setToolTipConfig(helpToolTip);
	}
	
	private void addButtonHandlers() {
		this.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						grid.commitChanges();					
					}
				});

		this.getButton(PredefinedButton.CANCEL).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						grid.rejectChanges();
					}
				});
	}	
	
	private void setSelectedElement() {
		List<VectorFeature> selectedElements =  
				grid.getFeatureGrid().getSelectionModel().getSelectedItems();

		if (selectedElements != null && !selectedElements.isEmpty()) {
			for (FeatureTool tool : layerInfoToolBar.getTools()) {

				tool.setSelectedLayer(selectedLayer);

				if (selectedElements.size() > 1) {
					tool.setSelectedFeatures(selectedElements);
				} else {
					tool.setSelectedFeature(selectedElements.get(0));
				}
			}
		}
	}

}
