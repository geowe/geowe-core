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

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

@ApplicationScoped
public class WfsImportTab extends VerticalPanel {
	private static final String FIELD_WIDTH = "170px";
	private final TextField wfsUrlField;
	private final TextField wfsNameSpaceField;
	private final TextField wfsFeatureTypeField;
	private final TextField geomColumnField;
	private final TextField versionField;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	
	public WfsImportTab() {
		super();
		
		this.setSpacing(5);		
		wfsUrlField = createTextField(FIELD_WIDTH, false, "http://...");
		this.add(new FieldLabel(wfsUrlField, UIMessages.INSTANCE
				.gdidWfsUrlField()));

		wfsUrlField.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(final ValueChangeEvent<String> event) {
				if (!event.getValue().startsWith("http")) {
					messageDialogBuilder.createError(
							UIMessages.INSTANCE.warning(),
							UIMessages.INSTANCE.gdidWfsUrlField()).show();
				}
			}
		});

		wfsNameSpaceField = createTextField(FIELD_WIDTH, false, null);
		this.add(new FieldLabel(wfsNameSpaceField, UIMessages.INSTANCE
				.gdidWfsNameSpaceField()));
		
		wfsFeatureTypeField = createTextField(FIELD_WIDTH, false, null);
		this.add(new FieldLabel(wfsFeatureTypeField, UIMessages.INSTANCE
				.gdidWfsLayerField()));		

		geomColumnField = createTextField(FIELD_WIDTH, true, null);
		this.add(new FieldLabel(geomColumnField, UIMessages.INSTANCE
				.gdidWfsGeomColumnField()));

		versionField = createTextField(FIELD_WIDTH, true, "X.X.X");
		this.add(new FieldLabel(versionField, UIMessages.INSTANCE
				.gdidWfsVersionField()));
	}

	public String getWfsUrl() {
		return wfsUrlField.getText();
	}

	public String getWfsNamespace() {
		return wfsNameSpaceField.getText();
	}

	public String getWfsFeatureType() {
		return wfsFeatureTypeField.getText();
	}

	public String getGeomColumn() {
		return geomColumnField.getText();
	}

	public String getVersion() {
		return versionField.getText();
	}	
	
	private TextField createTextField(final String width, final boolean allowBlank,
			final String stakeHolder) {
		final TextField newTextField = new TextField();

		newTextField.setWidth(width);
		newTextField.setAllowBlank(allowBlank);

		if (stakeHolder != null && !stakeHolder.isEmpty()) {
			newTextField.setEmptyText(stakeHolder);
		}

		return newTextField;
	}	
}