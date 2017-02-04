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
package org.geowe.client.local.main.tool.project;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.export.exporter.FileExporter;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Herramienta responsable de mostrar la información del proyecto
 * 
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class InfoProjectTool extends ButtonTool {


	private Project project;

	public interface ProjectInfoTemplate extends XTemplates {
		@XTemplate(source = "ProjectInfoTemplate.html")
		public SafeHtml renderTemplate(UIMessages uimessages, Project project,
				List<ProjectVectorLayer> vectors, int totalElements);
	}

	@Inject
	public InfoProjectTool(LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.infoLayerToolText(), ImageProvider.INSTANCE
				.info24(), layerManager);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.infoLayerToolText(),
				UIMessages.INSTANCE.infoProjectToolTipText(), Side.LEFT));
		setEnabled(false);
	}

	@Override
	protected void onRelease() {
		showDialog(getHtmlReport());
	}

	private void showDialog(final HTML htmlReport) {
		final Dialog simple = new Dialog();
		simple.setHeadingText(project.getTitle());
		simple.setSize("420px", "420px");
		simple.setResizable(true);
		simple.setHideOnButtonClick(true);
		simple.setPredefinedButtons(PredefinedButton.CLOSE);
		simple.setBodyStyleName("pad-text");
		simple.getBody().addClassName("pad-text");
		simple.add(getPanel(htmlReport));
		simple.addButton(new TextButton(UIMessages.INSTANCE.download(),
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						FileExporter.saveAs(htmlReport.getHTML(),
								project.getTitle() + ".html");
					}
				}));
		simple.show();
	}

	private HorizontalLayoutContainer getPanel(final HTML data) {
		HorizontalLayoutContainer panel = new HorizontalLayoutContainer();
		panel.setSize("400px", "400px");
		panel.add(data);
		ScrollSupport scrollSupport = panel.getScrollSupport();
		scrollSupport.setScrollMode(ScrollMode.AUTO);
		return panel;
	}

	public HTML getHtmlReport() {
		ProjectInfoTemplate template = GWT.create(ProjectInfoTemplate.class);

		return new HTML(template.renderTemplate(UIMessages.INSTANCE, project,
				project.getVectors(), project.getNumElements()));
	}

	@Override
	public void onChange(Vector layer) {
		this.layer = layer;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
