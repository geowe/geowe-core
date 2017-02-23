/*
 * #%L
 * GeoWE Project
 * %%
 * Copyright (C) 2015 - 2017 GeoWE.org
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

import java.util.Arrays;
import java.util.List;

/**
 * Componente gráfico reutilizable que representa una lista desplegable
 * con los posibles tamaños de página (num. de elementos por página)
 * para una lista o un grid de elementos
 * 
 * @author atamunoz
 *
 */
public class PageSizeComboBox extends IntegerValueComboBox {
	private static final List<Integer> pageSizes = Arrays.asList(
			25, 50, 75, 100, 150, 200, 500, 1000);
	
	public PageSizeComboBox(String width) {
		super(width);
		setPageSizes(pageSizes);		
	}
	
	public void setPageSizes(List<Integer> sizes) {
		setValues(sizes);		
	}
	
	public void reset(Integer numElements) {
		setValues(pageSizes);
		
		if(numElements > 1000) {
			addValue(numElements);
		}
	}
}
