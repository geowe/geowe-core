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

package org.geowe.client.local.main.tool.extent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextArea;

/**
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class CurrentExtentDialog extends Dialog {
	private TextArea wktTextArea;
	@Inject
	private CurrentExtentBean model;
	
	private CurrentExtentDialog() {
		setHideOnButtonClick(true);
		setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
				
		setButtonAlign(BoxLayoutPack.CENTER);		
		setResizable(false);
		setWidth(335);
		setHeight(350);
		setHeadingHtml("Extensión actúal del mapa");
		add(createPanel());
	}
	
	private Widget createPanel() {
		HorizontalLayoutContainer container = new HorizontalLayoutContainer();
		container.setScrollMode(ScrollMode.AUTO);
		container.setSize("300px", "260px");

		container.add(getTextPanel());

		return container;
	}

	private TextArea getTextPanel() {
		wktTextArea = new TextArea();
		wktTextArea.setBorders(true);
		
		wktTextArea.setSize("300px", "250px");
		wktTextArea.setText(model.getWkt());

		return wktTextArea;
	}

}
