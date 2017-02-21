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
package org.geowe.client.local.ui;

import org.geowe.client.local.main.AnchorBuilder;
import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;

/**
 * Renderizador de celdas para las tablas de features, que permite mostrar
 * de la manera adecuada los enlaces y las imágenes (URL a imagen) 
 * 
 * @author Atanasio Muñoz (ata@geowe.org)
 *
 */
public class FeatureGridCellRenderer extends AbstractCell<String> {

	public FeatureGridCellRenderer() {
		super();
	}
	
	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {
		if (value != null) {
			if (value.startsWith("http")) {
				final Anchor anchor = new AnchorBuilder()
						.setText(value)
						.setHref(value)
						.setTitle(UIMessages.INSTANCE.openInNewWindow())
						.build();
				sb.appendHtmlConstant(anchor.getElement().getString());
			} else if (value.startsWith("img:")) {
				final String url = value.replace("img:", "");
				final Image image = new Image(url);
				image.setSize("48px", "48px");
				final Anchor anchor = new AnchorBuilder()
						.setHref(url)
						.setTitle(UIMessages.INSTANCE.openInNewWindow())
						.setImage(image).build();

				sb.appendHtmlConstant(anchor.getElement().getString());
			} else {
				sb.appendHtmlConstant(value);
			}
		}
	}
}
