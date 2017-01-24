package org.geowe.client.local.model.style;

import org.geowe.client.local.model.vector.FeatureAttributeDef;
import org.geowe.client.local.style.StyleFactory;
import org.gwtopenmaps.openlayers.client.util.JSObject;

public class VectorStyleDef {
	private FillStyle fill;
	private LineStyle line;
	private LabelStyle label;
	
	private FeatureAttributeDef colorThemingAttribute;
	private boolean enableLegend;
	
	public FillStyle getFill() {
		return fill;
	}
	
	public void setFill(FillStyle fill) {
		this.fill = fill;
	}
	
	public LineStyle getLine() {
		return line;
	}
	
	public void setLine(LineStyle line) {
		this.line = line;
	}
	
	public LabelStyle getLabel() {
		return label;
	}
	
	public void setLabel(LabelStyle label) {
		this.label = label;
	}
	
	public FeatureAttributeDef getColorThemingAttribute() {
		return colorThemingAttribute;
	}
	
	public void setColorThemingAttribute(FeatureAttributeDef colorThemingAttribute) {
		this.colorThemingAttribute = colorThemingAttribute;
	}
	
	public boolean isEnableLegend() {
		return enableLegend;
	}
	
	public void setEnableLegend(boolean enableLegend) {
		this.enableLegend = enableLegend;
	}
	
	public boolean isColorThemingEnabled() {
		return getColorThemingAttribute() != null;
	}
	
	public JSObject toJSStyle(JSObject style) {			
		/*
		 * Antes de nada, si se ha activado el color tematico, se debe
		 * aplicar este como primera operacion, ya que conlleva la
		 * creacion de un nuevo StyleMap
		 */
		if(isColorThemingEnabled() || getLabel().isEnabled()) {
			StyleFactory.createStyleMap(
					StyleFactory.DEFAULT_NORMAL_COLOR, 
					StyleFactory.DEFAULT_SELECTED_COLOR, 
					StyleFactory.DEFAULT_HIGHLIGHTED_COLOR, 
					getLabel().isEnabled() ? getLabel().getAttribute().getName() : null, 
					isColorThemingEnabled() ? getColorThemingAttribute().getName() : null);
		} 
		
		/*
		 * Solo se aplica el fillColor si NO se ha activado el color tematico
		 */
		if (!isColorThemingEnabled()) {
			style.setProperty("fillColor", getFill().getColor());
		}
		style.setProperty("fillOpacity", getFill().getOpacity() / 100.0);
		style.setProperty("strokeColor", getLine().getColor());
		style.setProperty("strokeWidth", getLine().getThickness());

		return style;
	}
}
