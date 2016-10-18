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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

@ApplicationScoped
public class WfsImportTab extends VerticalPanel {
	private static final String FIELD_WIDTH = "170px";
	private final TextField wfsUrlField;
	private final TextField typeNameField;
	private final TextField maxFeaturesField;
	private final TextField cqlFilterField;
	private final TextField versionField;
	private CheckBox bbox;
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

		typeNameField = createTextField(FIELD_WIDTH, false,
				UIMessages.INSTANCE
				.gdidWfsLayerFieldStakeholder());
		this.add(new FieldLabel(typeNameField, UIMessages.INSTANCE
				.gdidWfsNameSpaceField()));
		
		versionField = createTextField(FIELD_WIDTH, true, "X.X.X");
		this.add(new FieldLabel(versionField, UIMessages.INSTANCE
				.gdidWfsVersionField()));

		maxFeaturesField = createTextField(FIELD_WIDTH, false, "no limit");
		maxFeaturesField.setText("100");
		this.add(new FieldLabel(maxFeaturesField, UIMessages.INSTANCE
				.gdidWfsMaxFeaturesField()));

		bbox = new CheckBox();
		bbox.setBoxLabel("BBox");
		bbox.setValue(true);
		bbox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				cqlFilterField.setEnabled(!bbox.getValue());
			}
		});
		this.add(bbox);

		cqlFilterField = createTextField(FIELD_WIDTH, false, "no filter");
		cqlFilterField.setEnabled(false);
		this.add(new FieldLabel(cqlFilterField, UIMessages.INSTANCE
				.gdidWfsCqlField()));
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

	public String getWfsUrl() {
		return wfsUrlField.getText();
	}

	public String getWfsTypeName() {
		return typeNameField.getText();
	}

	public String getWfsMaxFeaturesType() {
		return maxFeaturesField.getText();
	}

	public String getCql() {
		return cqlFilterField.getText();
	}

	public String getWfsVersion() {
		return versionField.getText();
	}	
	
	public boolean isBboxEnabled() {
		return bbox.getValue();
	}
}
