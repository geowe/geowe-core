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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.main.tool.FeatureInfoTool;
import org.geowe.client.local.main.tool.PanTool;
import org.geowe.client.local.main.tool.SelectionTool;
import org.geowe.client.local.main.tool.info.WmsGetInfoTool;
import org.geowe.client.local.main.tool.map.ZoomToFullExtendTool;
import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

@ApplicationScoped
public class BasicToolBar implements IsWidget {

	@Inject
	private SelectionTool selectionTool;

	@Inject
	private PanTool panTool;
	@Inject
	private ZoomToFullExtendTool zoomToFullExtendTool;
	@Inject
	private FeatureInfoTool infoFeatureTool;	

	@Inject
	private WmsGetInfoTool wmsGetInfoTool;

	private VerticalLayoutContainer widget;
	private HorizontalPanel toolBar;

	private HorizontalPanel w3wPanel;

	private Label wordsLabel;
	private Anchor w3wAnchor;

	@PostConstruct
	private void initialize() {
		initializeToolbar();
		initializew3wPanel();
	}

	private void initializeToolbar() {
		toolBar = new HorizontalPanel();
		toolBar.setSpacing(3);

		toolBar.add(selectionTool);
		toolBar.add(infoFeatureTool);
		toolBar.add(wmsGetInfoTool);
		toolBar.add(panTool);
		toolBar.add(zoomToFullExtendTool);

		createButtonGroup();
	}

	private void initializew3wPanel() {
		w3wPanel = new HorizontalPanel();
		w3wPanel.setSpacing(5);
		w3wPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		StyleInjector.inject(".w3wPanel { " + "background: #E0ECF8;"
				+ "border-radius: 5px 10px;" + "opacity: 0.8}");
		w3wPanel.setStyleName("w3wPanel");
		w3wPanel.setWidth("415px");

		wordsLabel = new Label();
		w3wAnchor = new AnchorBuilder().setHref("https://what3words.com/")
				.setText(UIMessages.INSTANCE.what3Words())
				.setTitle("https://what3words.com/").build();
		w3wAnchor.getElement().getStyle().setColor("#FF0000");
		w3wPanel.add(w3wAnchor);
		w3wPanel.add(wordsLabel);
	}

	@Override
	public Widget asWidget() {
		if (widget == null) {
			widget = new VerticalLayoutContainer();

			widget.getElement().getStyle().setPosition(Position.ABSOLUTE);
			widget.getElement().getStyle().setRight(5, Unit.PX);
			widget.getElement().getStyle().setBottom(5, Unit.PX);
			widget.setHeight("120px");
			widget.setWidth("420px");
			ScrollSupport scrollSupport = widget.getScrollSupport();
			scrollSupport.setScrollMode(ScrollMode.AUTOX);

			widget.add(w3wPanel);
			widget.add(toolBar);
			widget.setVisible(true);
		}
		return widget;
	}

	private void createButtonGroup() {
		ToggleGroup group = new ToggleGroup();
		group.add(selectionTool);
		group.add(infoFeatureTool);
		group.add(wmsGetInfoTool);
	}

	public void addTool(Widget buttonTool) {
		toolBar.add(buttonTool);
	}

	public void removeAllTools() {
		toolBar.clear();
	}

	public void setWhat3Words(String w3w) {
		wordsLabel.setText(w3w);
	}

	public void setAnchorColor(String htmlColor) {
		w3wAnchor.getElement().getStyle().setColor(htmlColor);
	}

	public void hide() {
		widget.hide();
	}

	public void show() {
		widget.show();
	}

}
