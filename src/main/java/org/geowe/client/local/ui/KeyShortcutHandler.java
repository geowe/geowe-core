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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * Manejador de eventos para asociar una tecla a un botón de la interfaz,
 * de manera que cuando se pulse dicha tecla se dispare el evento de 
 * pulsar el botón.
 * 
 * @author atamunoz
 *
 */
public class KeyShortcutHandler implements KeyDownHandler {
	private final TextButton actionButton;
	private final int keyCode;
	
	public KeyShortcutHandler(final TextButton button, final int keyCode) {
		this.actionButton = button;
		this.keyCode = keyCode;
	}
	
	@Override
	public void onKeyDown(final KeyDownEvent event) {

		if (event.getNativeKeyCode() == keyCode) {				
			actionButton.fireEvent( new GwtEvent<ClickHandler>() {
		        @Override
		        public com.google.gwt.event.shared.GwtEvent.Type<ClickHandler> getAssociatedType() {
		        	return ClickEvent.getType();
		        }
		        @Override
		        protected void dispatch(final ClickHandler handler) {
		            handler.onClick(null);
		        }
		   });
		}
	}		
}