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
package org.geowe.client.local.main.tool.map.catalog.dialog;

import org.geowe.client.local.main.tool.map.catalog.model.LayerDef;

import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * Interfaz que define la representación de un LayerDef en una lista o tabla,
 * indicando aquellos campos que estaran disponibles para ser mostrados.
 * 
 * @author Atanasio Muñoz
 *
 */
public interface LayerDefProperties extends PropertyAccess<LayerDef> {
	@Path("name")
	ModelKeyProvider<LayerDef> key();

	@Path("name")
	LabelProvider<LayerDef> nameLabel();

	ValueProvider<LayerDef, String> name();

	ValueProvider<LayerDef, String> type();

	ValueProvider<LayerDef, String> description();

	ValueProvider<LayerDef, ImageResource> icon();

}
