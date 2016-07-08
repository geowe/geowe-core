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

import javax.enterprise.context.ApplicationScoped;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;

/**
 * <h2>MessageDialogBuilder</h2>
 * 
 * @Description It responsible to create a Message box
 * @author rltorres
 *
 */
@ApplicationScoped
public class MessageDialogBuilder {

	/**
	 * Create an Alert Message box
	 * 
	 * @param title
	 * @param message
	 * @return
	 */
	public AlertMessageBox createError(final String title, final String message) {
		return new AlertMessageBox(title, message);
	}

	/**
	 * Create an Info Message box
	 * 
	 * @param title
	 * @param message
	 * @return
	 */
	public MessageBox createInfo(final String title, final String message) {
		final MessageBox messageBox = new MessageBox(title, message);
		messageBox.setIcon(MessageBox.ICONS.info());

		return messageBox;
	}

	/**
	 * Create a warning Message box
	 * 
	 * @param title
	 * @param message
	 * @return
	 */
	public MessageBox createWarning(final String title, final String message) {
		final MessageBox messageBox = new MessageBox(title, message);
		messageBox.setIcon(MessageBox.ICONS.warning());

		return messageBox;
	}

	/**
	 * Create a modal Confirm Message Box with YES and NO Buttons
	 * 
	 * @param title
	 * @param question
	 * @param icon
	 * @return ConfirmMessageBox
	 */
	public ConfirmMessageBox createConfirm(final String title, final String question,
			final ImageResource icon) {
		final ConfirmMessageBox confirmDialog = new ConfirmMessageBox(title, question);
		confirmDialog.setModal(true);
		confirmDialog.setIcon(icon);
		confirmDialog.setBlinkModal(true);

		return confirmDialog;
	}
}