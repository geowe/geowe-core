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
package org.geowe.client.local.layermanager.tool.create;

import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.form.CheckBox;

/**
 * 
 * @author rltorres
 *
 */
public class CreateEmptyLayerTab {

	private CheckBox createAttributes;

	public VerticalPanel getEmptyPanel() {
		VerticalPanel geoDataContainer = new VerticalPanel();
		geoDataContainer.setSpacing(5);
		geoDataContainer.setWidth("280px");
		geoDataContainer.setSpacing(5);
		geoDataContainer.add(new Label(UIMessages.INSTANCE
				.createEmptyLayerToolText()));

		createAttributes = new CheckBox();
		createAttributes.setBoxLabel(UIMessages.INSTANCE
				.celtAddAttributesLabel());
		createAttributes.setValue(false);

		geoDataContainer.add(createAttributes);
		return geoDataContainer;
	}

	public boolean getCreateAttributes() {
		return createAttributes.getValue();
	}

	public void setCreateAttributes(boolean value) {
		this.createAttributes.setValue(value, true);
	}

}
