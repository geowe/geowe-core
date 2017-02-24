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

import org.geowe.client.local.ImageProvider;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.resources.ThemeStyles;

/**
 * 
 * @author geowe
 *
 */
public class AnchorBuilder {

	private Image image;
	private String href;
	private String title;
	private String text;
	private ClickHandler clickHandler;
	private boolean bottomBorderOnMouseOver;


	public AnchorBuilder setImage(Image image) {
		this.image = image;
		return this;
	}

	public AnchorBuilder setHref(String href) {
		this.href = href;
		return this;
	}

	public AnchorBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public AnchorBuilder setText(String text) {
		this.text = text;
		return this;
	}

	public AnchorBuilder setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
		return this;
	}

	public boolean isMouseOver() {
		return bottomBorderOnMouseOver;
	}

	public AnchorBuilder setBottomBorderOnMouseOver(
			boolean bottomBorderOnMouseOver) {
		this.bottomBorderOnMouseOver = bottomBorderOnMouseOver;
		return this;
	}

	/**
	 * Build the Anchor with target="_blank"
	 * 
	 * @return Anchor
	 */
	public Anchor build() {
		final Anchor anchor = new Anchor();

		if (image != null) {
			anchor.getElement().appendChild(image.getElement());
		}
		if (text != null) {
			anchor.setText(text);
		}
		if (title != null) {
			anchor.setTitle(title);
		}
		if (bottomBorderOnMouseOver) {
			anchor.addMouseOverHandler(getMouseOverhandler(anchor));
			anchor.addMouseOutHandler(getMouseOutHandler(anchor));
		}
		anchor.setHref(href);
		anchor.setTarget("_blank");

		return anchor;
	}

	private MouseOverHandler getMouseOverhandler(final Anchor anchor) {
		return new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				anchor.addStyleName(ThemeStyles.get().style().borderBottom());
			}
		};
	}

	private MouseOutHandler getMouseOutHandler(final Anchor anchor) {
		return new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				anchor.removeStyleName(ThemeStyles.get().style().borderBottom());
			}
		};
	}

	public Anchor buildClick() {
		Anchor anchor = new Anchor();

		if (image != null) {
			anchor.getElement().appendChild(image.getElement());
		}
		if (text != null) {
			anchor.setText(text);
		}
		if (title != null) {
			anchor.setTitle(title);
		}
		if (clickHandler != null) {
			anchor.addClickHandler(clickHandler);
		}

		return anchor;
	}

	public Anchor getFaceBookLink() {
		return setHref("https://www.facebook.com/geowe.org/")
				.setImage(new Image(ImageProvider.INSTANCE.facebook()))
				.setTitle("GeoWE in FaceBook").setBottomBorderOnMouseOver(true).build();
	}

	public Anchor getGooglePlusLink() {
		return setHref("https://plus.google.com/+GeoweOrgGIS")
				.setImage(new Image(ImageProvider.INSTANCE.googlePlus()))
				.setTitle("GeoWE in G+").setBottomBorderOnMouseOver(true).build();
	}


	public Anchor getGeoWEWebLink() {
		return setHref("http://www.geowe.org")
				.setImage(new Image(ImageProvider.INSTANCE.home()))
				.setTitle("GeoWE.org").setBottomBorderOnMouseOver(true).build();
	}

	public Anchor getMailLink() {
		return setHref("mailto:info@geowe.org")
				.setImage(new Image(ImageProvider.INSTANCE.mail()))
				.setTitle("e-mail us").setBottomBorderOnMouseOver(true).build();
	}

	public Widget getBugLink() {
		return setHref("http://www.geowe.org/index.php?id=contacto")
				.setImage(new Image(ImageProvider.INSTANCE.bug()))
				.setTitle("Issue tracker").setBottomBorderOnMouseOver(true).build();
	}

	public Widget getHelpLink() {
		return setHref("http://www.geowe.org")
				.setImage(new Image(ImageProvider.INSTANCE.question()))
				.setTitle("Help").setBottomBorderOnMouseOver(true).build();
	}

	public Widget getTwiterLink() {
		return setHref("https://twitter.com/geowe_org")
				.setImage(new Image(ImageProvider.INSTANCE.twiter()))
				.setTitle("Twiter").setBottomBorderOnMouseOver(true).build();
	}
	
}
