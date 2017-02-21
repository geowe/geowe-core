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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.main.tool.help.HelpMessages;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.EditingFeatureGrid;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

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
	public static final int MIN_VISIBLE_ROWS = 15;
	public static final int MAX_VISIBLE_ROWS = 25;
	
	private VectorLayer selectedLayer;
	private EditingFeatureGrid grid;	
	
	public LayerEditDialog() {
		super();
		
		setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		setButtonAlign(BoxLayoutPack.CENTER);
		setHideOnButtonClick(true);
		setResizable(true);
		setMaximizable(true);
		setWidth(computeWidth());
		setHeight(computeHeight());
				
		add(createPanel());
		addButtonHandlers();		
	}
	
	public void setSelectedLayer(VectorLayer layer) {
		if(layer != null) {
			String previousName = this.selectedLayer != null ? 
					this.selectedLayer.getName() : "";
			boolean changedLayer = !layer.getName().equalsIgnoreCase(previousName);
			
			this.selectedLayer = layer;
			VectorFeature[] features = this.selectedLayer.getFeatures() != null 
					? this.selectedLayer.getFeatures()
					: new VectorFeature[0];			

			//TODO No funciona el resize
			setWidth(computeWidth());
			setHeight(computeHeight());		
					
			if(changedLayer) {
				grid.rebuild(features);							
			} else {
				grid.update(features);
			}			
			
			setHeadingText(UIMessages.INSTANCE.lidHeadText() + ": " + 
					selectedLayer.getName());			
		}
	}
	
	@PostConstruct
	private void initialize() {
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
		if(selectedLayer != null && selectedLayer.getFeatures() != null) {
			
			return (selectedLayer.getNumberOfFeatures() > MAX_VISIBLE_ROWS ?
					MAX_VISIBLE_ROWS : 
					selectedLayer.getNumberOfFeatures()) *  ROW_SIZE;
		} else {
			return MIN_VISIBLE_ROWS * ROW_SIZE;
		}
	}
	
	private Widget createPanel() {
		grid = new EditingFeatureGrid();
		grid.setEnableCellRender(true);
				
		final VerticalLayoutContainer vPanel = new VerticalLayoutContainer();		
		vPanel.addStyleName(ThemeStyles.get().style().borderBottom());					
		vPanel.add(grid.getFeatureGrid(), 
				new VerticalLayoutData(1, 1, new Margins(5, 0, 0, 0)));

		final ToolButton tButton = new ToolButton(ToolButton.QUESTION);
		setHelpToolTip(tButton);
		addHelpSelectHandler(tButton);

		getHeader().addTool(tButton);

		return vPanel;
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
}
