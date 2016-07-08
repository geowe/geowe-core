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
package org.geowe.client.local.style;

import java.util.ArrayList;
import java.util.List;

import org.geowe.client.local.ImageProvider;

/**
 * Conjunto de estilos permitidos para la representacion de los vertices
 * de una geometría.
 * 
 * @author Atanasio Muñoz
 *
 */
public class VertexStyles {
	private static List<VertexStyleDef> allStyles = loadAllStyles();

	public static List<VertexStyleDef> getAll() {
		return allStyles;
	}

	public static VertexStyleDef getByStyleName(String styleName) {
		VertexStyleDef selectedStyle = null;
		
		for(VertexStyleDef vertexStyleDef : allStyles) {
			if(vertexStyleDef.getStyleName().equals(styleName)) {
				selectedStyle = vertexStyleDef;
			}
		}
		
		return selectedStyle;
	}
	
	private static List<VertexStyleDef> loadAllStyles() {
		List<VertexStyleDef> styles = new ArrayList<VertexStyleDef>();
		
		VertexStyleDef circle = new VertexStyleDef();
		circle.setName("Circle");
		circle.setStyleName("circle");
		circle.setImage(ImageProvider.INSTANCE.circleVertex());
		
		VertexStyleDef square = new VertexStyleDef();
		square.setName("Square");
		square.setStyleName("square");
		square.setImage(ImageProvider.INSTANCE.squareVertex());
		
		VertexStyleDef triangle = new VertexStyleDef();
		triangle.setName("Triangle");
		triangle.setStyleName("triangle");
		triangle.setImage(ImageProvider.INSTANCE.triangleVertex());
		
		VertexStyleDef star = new VertexStyleDef();
		star.setName("Star");
		star.setStyleName("star");
		star.setImage(ImageProvider.INSTANCE.starVertex());
		
		VertexStyleDef cross = new VertexStyleDef();
		cross.setName("Cross");
		cross.setStyleName("cross");
		cross.setImage(ImageProvider.INSTANCE.crossVertex());
		
		VertexStyleDef axis = new VertexStyleDef();
		axis.setName("Axis");
		axis.setStyleName("x");		
		axis.setImage(ImageProvider.INSTANCE.axisVertex());
		
		styles.add(circle);
		styles.add(square);
		styles.add(triangle);
		styles.add(star);
		styles.add(cross);
		styles.add(axis);
				
		return styles;
	}
}
