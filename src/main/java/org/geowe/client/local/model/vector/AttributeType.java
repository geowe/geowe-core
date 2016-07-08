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
package org.geowe.client.local.model.vector;

import java.util.Arrays;
import java.util.List;

/**
 * Tipo de datos que puede albergar un atributo de una feature.
 * 
 * @author Atanasio Muñoz
 *
 * NOTA: En la versión 1.0, solamente se soporta el tipo STRING.
 */
public enum AttributeType {
	STRING("String", String.class), INTEGER("Integer", Integer.class), LONG(
			"Number", Long.class), FLOAT("Float", Float.class), DOUBLE(
			"Decimal", Double.class), BOOLEAN("Boolean", Boolean.class), DATE(
			"Date", String.class), LINK("Link", String.class), GEOMETRY(
			"Geometry", String.class), OBJECT("Object", String.class);

	String name;
	Class<?> clazz;

	AttributeType(String name, Class<?> clazz) {
		this.name = name;
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public static List<AttributeType> getAll() {
		return Arrays.asList(AttributeType.values());
	}

	public static List<AttributeType> getAllowedTypes() {
		return Arrays.asList(new AttributeType[] { STRING });
	}
}
