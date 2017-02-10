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
package org.geowe.client.local.style;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.ChangeSelectedLayerListener;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Dialogo de configuracion de los estilos de representacion de una
 * capa vectorial. 
 * 
 * @author Atanasio Muñoz
 * 
 * @since 31/08/2016
 * @author rafa@geowe.org 
 * Se cambia altura del panel issue #158
 */
@ApplicationScoped
public class VectorLayerStyleWidget implements IsWidget,
		ChangeSelectedLayerListener {

	private static String HEADING_TEXT = UIMessages.INSTANCE.vlswHeading();
	private FramedPanel panel;
	private VectorLayer selectedLayer;
	private PlainTabPanel tabPanel;
	private ColorStyleTab colorStyleTab;
	private LabelStyleTab labelStyleTab;
	private VertexStyleTab vertexStyleTab;
	private ColorThemingStyleTab colorThemingStyleTab;
	private TextButton applyButton;

	@Override
	public Widget asWidget() {
		if (panel == null) {
			applyButton = new TextButton(
					UIMessages.INSTANCE.vlswApplyButtonText());

			HorizontalPanel mainPanel = new HorizontalPanel();
			mainPanel.setSpacing(10);

			VerticalLayoutContainer vlc = new VerticalLayoutContainer();
			vlc.add(createTabPanel(), new VerticalLayoutData(-18, -1));

			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(5);
			hp.add(applyButton);

			vlc.add(hp, new VerticalLayoutData(1, 1, new Margins(35, 0, 0, 0)));

			panel = new FramedPanel();
			panel.setWidth(500);
			panel.setHeight(275);
			panel.setLayoutData(new MarginData(10));

			mainPanel.add(vlc);
			panel.add(mainPanel);

			panel.setAnimCollapse(true);

			panel.getHeader().setIcon(ImageProvider.INSTANCE.layerIcon());
			panel.getElement().getStyle().setPosition(Position.ABSOLUTE);
			panel.setPosition(300, 185);
			panel.getElement().getStyle().setBorderWidth(1.0, Unit.PX);
			panel.setVisible(false);

			new Draggable(panel, panel.getHeader());

			ToolButton tButton = new ToolButton(ToolButton.CLOSE);
			addCloseSelectHandler(tButton);
			panel.getHeader().addTool(tButton);
			panel.setCollapsible(true);
		}
		return panel;
	}

	private PlainTabPanel createTabPanel() {
		tabPanel = new PlainTabPanel();
		tabPanel.setPixelSize(480, 160);

		colorStyleTab = new ColorStyleTab();
		labelStyleTab = new LabelStyleTab();
		vertexStyleTab = new VertexStyleTab();
		colorThemingStyleTab = new ColorThemingStyleTab();

		tabPanel.add(colorStyleTab, UIMessages.INSTANCE.vlsLineAndColor());
		tabPanel.add(labelStyleTab, UIMessages.INSTANCE.vlswLabelAttribute());
		tabPanel.add(vertexStyleTab, UIMessages.INSTANCE.vlswVertexStyle());
		tabPanel.add(colorThemingStyleTab,
				UIMessages.INSTANCE.vlsColorTheming());

		return tabPanel;
	}

	private void addCloseSelectHandler(final ToolButton tButton) {
		tButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				panel.setVisible(false);
			}
		});
	}

	@Override
	public void onChange(Vector layer) {
		setSelectedLayer((VectorLayer) layer);
	}

	public VectorLayer getSelectedLayer() {
		return selectedLayer;
	}

	public void setSelectedLayer(VectorLayer selectedLayer) {
		this.selectedLayer = selectedLayer;
		
		colorStyleTab.setSelectedLayer(selectedLayer);
		labelStyleTab.setSelectedLayer(selectedLayer);
		vertexStyleTab.setSelectedLayer(selectedLayer);
		colorThemingStyleTab.setSelectedLayer(selectedLayer);	
		
		boolean featuresSelected = selectedLayer != null 
				&& selectedLayer.getSelectedFeatures() != null
				&& selectedLayer.getSelectedFeatures().length > 0;
		String title = HEADING_TEXT
				+ (this.selectedLayer != null 
				? ": "+ this.selectedLayer.getName() 
				: UIMessages.INSTANCE.noSelectedLayer());		
				
		tabPanel.getConfig(colorThemingStyleTab.asWidget())
			.setEnabled(!featuresSelected);
		if(featuresSelected) {
			title += " (" + selectedLayer.getSelectedFeatures().length +
					" " + UIMessages.INSTANCE.selectedFeatures() + ")";			
		}			
		
		panel.setHeadingText(title);
	}

	public String getFillColor() {
		return colorStyleTab.getFillColor();
	}

	public Double getFillOpacity() {
		return colorStyleTab.getFillOpacity();
	}

	public String getStrokeColor() {
		return colorStyleTab.getStrokeColor();
	}

	public Integer getStrokeWidth() {
		return colorStyleTab.getStrokeWidth();
	}

	public boolean isBasicVertexStyle() {
		return vertexStyleTab.isBasicStyle();
	}
	
	public boolean isAdvancedVertexStyle() {
		return vertexStyleTab.isAdvancedStyle();
	}	
	
	public String getVertexStyle() {
		return vertexStyleTab.getVertexStyle();
	}

	public String getExternalGraphic() {
		return vertexStyleTab.getExternalGraphic();
	}
	
	public int getGraphicWidth() {
		return vertexStyleTab.getGraphicWidth();
	}
	
	public int getGraphicHeight() {
		return vertexStyleTab.getGraphicHeight();
	}
	
	public String getAttributeLabel() {
		return labelStyleTab.getAttributeLabel();
	}

	public Integer getFontSize() {
		return labelStyleTab.getFontSize();
	}

	public Boolean isUseBoldLabel() {
		return labelStyleTab.isUseBoldLabel();
	}

	public String getBackgroundColor() {
		return labelStyleTab.getBackgroundColor();
	}

	public Boolean isEnableLabeling() {
		return labelStyleTab.isEnableLabeling();
	}

	public String getAttributeTheming() {
		return colorThemingStyleTab.getAttributeTheming();
	}

	public Boolean isEnableTheming() {
		return colorThemingStyleTab.isEnableTheming();
	}

	public Boolean isEnableLegend() {
		return colorThemingStyleTab.isEnableLegend();
	}

	public TextButton getApplyButton() {
		return applyButton;
	}
}
