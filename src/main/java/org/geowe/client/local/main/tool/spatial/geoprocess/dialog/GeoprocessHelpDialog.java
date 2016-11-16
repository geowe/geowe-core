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
package org.geowe.client.local.main.tool.spatial.geoprocess.dialog;

import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.main.tool.help.HelpMessages;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
/**
 * Help for geoprocess
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class GeoprocessHelpDialog extends Dialog {

	public interface VectorLayerTemplate extends XTemplates {
		@XTemplate(source = "GeoprocessHelpTemplate.html")
		public SafeHtml renderTemplate(HelpMessages helpMessages, UIMessages uiMessages);
	}

	public GeoprocessHelpDialog() {
		super();
		setHeadingText(HelpMessages.INSTANCE.help());
		setPredefinedButtons(PredefinedButton.CLOSE);
		setSize("420px", "420px");
		setResizable(true);
		setClosable(true);
		setBodyStyleName("pad-text");
		getBody().addClassName("pad-text");
		add(getHelpPanel(getHtmlReport()));
	}

	private HorizontalLayoutContainer getHelpPanel(final HTML data) {
		HorizontalLayoutContainer panel = new HorizontalLayoutContainer();
		panel.setSize("400px", "400px");
		panel.add(data);
		ScrollSupport scrollSupport = panel.getScrollSupport();
		scrollSupport.setScrollMode(ScrollMode.AUTO);
		return panel;
	}
	
	private HTML getHtmlReport() {
		VectorLayerTemplate template = GWT.create(VectorLayerTemplate.class);
		VectorFeature[] features = null;
		if (features == null) {
			features = new ArrayList<VectorFeature>()
					.toArray(new VectorFeature[0]);
		}
		return new HTML(template.renderTemplate(HelpMessages.INSTANCE, UIMessages.INSTANCE));
	}

}
