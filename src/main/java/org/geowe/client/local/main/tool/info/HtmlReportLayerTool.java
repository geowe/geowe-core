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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.LayerTool;
import org.geowe.client.local.layermanager.tool.export.FileExporter;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Herramienta para generar informe html de una capa
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class HtmlReportLayerTool extends LayerTool {

	public interface VectorLayerTemplate extends XTemplates {
		@XTemplate(source = "LayerInfoTemplate.html")
		public SafeHtml renderTemplate(UIMessages uimessages, VectorLayer data);
	}

	@Inject
	public HtmlReportLayerTool(LayerManagerWidget layerManagerWidget,
			GeoMap geoMap) {
		super(layerManagerWidget, geoMap);
		setText(UIMessages.INSTANCE.htmlReportButtonText());
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.htmlReportButtonText();
	}

	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.fileText24();
	}

	@Override
	public void onClick() {
		showDialog(getHtmlReport());
	}

	private void showDialog(final HTML htmlReport) {
		final Dialog simple = new Dialog();
		simple.setHeadingText("GeoWe Report");
		simple.setSize("400px", "400px");
		simple.setResizable(true);
		simple.setHideOnButtonClick(true);
		simple.setPredefinedButtons(PredefinedButton.CLOSE);
		simple.setBodyStyleName("pad-text");
		simple.getBody().addClassName("pad-text");
		simple.add(htmlReport);
		simple.addButton(new TextButton(UIMessages.INSTANCE.download(),
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						FileExporter.saveAs(htmlReport.getHTML(),
								getSelectedVectorLayer().getName() + ".html");
					}
				}));

		simple.show();
	}

	public HTML getHtmlReport() {
		VectorLayerTemplate template = GWT.create(VectorLayerTemplate.class);
		return new HTML(template.renderTemplate(UIMessages.INSTANCE,
				getSelectedVectorLayer()));
	}

}
