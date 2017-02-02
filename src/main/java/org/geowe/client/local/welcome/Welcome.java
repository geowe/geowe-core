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
package org.geowe.client.local.welcome;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.initializer.URLVectorLayerInitializer;
import org.geowe.client.local.main.AnchorBuilder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * 
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class Welcome {

	@Inject
	private URLVectorLayerInitializer uRLVectorLayerInitializer;

	public interface WelcomeTemplate extends XTemplates {
		@XTemplate(source = "welcomeTemplate.html")
		public SafeHtml renderTemplate(UIWelcomeMessages uimessages);
	}

	public void showDialog() {
		final Dialog simple = new Dialog();
		simple.setHeaderVisible(false);
		simple.setSize("530px", "330px");
		simple.setModal(true);
		simple.setClosable(false);
		simple.setResizable(false);
		simple.setHideOnButtonClick(true);
		simple.setPredefinedButtons(PredefinedButton.OK);
		simple.setBodyStyleName("pad-text");
		simple.getBody().addClassName("pad-text");
		simple.add(getPanel(getHtml()));
		simple.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						uRLVectorLayerInitializer.createLayerFromURL();
					}
				});
		simple.show();
	}

	private HorizontalPanel getPanel(final HTML data) {
		HorizontalPanel panel = new HorizontalPanel();
		panel.setSize("520px", "310px");
		panel.setSpacing(5);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Anchor anchor = new AnchorBuilder().setHref("http://www.geowe.org")
				.setImage(new Image(ImageProvider.INSTANCE.geoweSquareLogo()))
				.build();

		panel.add(anchor);
		panel.add(data);
		return panel;
	}

	public HTML getHtml() {
		WelcomeTemplate template = GWT.create(WelcomeTemplate.class);

		return new HTML(template.renderTemplate(UIWelcomeMessages.INSTANCE));
	}
}
