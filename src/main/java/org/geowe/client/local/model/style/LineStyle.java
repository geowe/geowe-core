package org.geowe.client.local.model.style;

import org.geowe.client.local.style.VertexStyleDef;

public class LineStyle {
	public static int MIN_THICKNESS = 0;
	public static int MAX_THICKNESS = 20;
	
	private String color;
	private int thickness;
	private VertexStyleDef vertexStyle;
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public int getThickness() {
		return thickness;
	}
	
	public void setThickness(int thickness) {
		
		if(thickness < MIN_THICKNESS) {
			this.thickness = MIN_THICKNESS;
		} else if(thickness > MAX_THICKNESS) {
			this.thickness = MAX_THICKNESS;		
		} else {
			this.thickness = thickness;
		}
	}

	public VertexStyleDef getVertexStyle() {
		return vertexStyle;
	}

	public void setVertexStyle(VertexStyleDef vertexStyle) {
		this.vertexStyle = vertexStyle;
	}
}
