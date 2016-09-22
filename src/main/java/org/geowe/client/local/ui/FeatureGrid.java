package org.geowe.client.local.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class FeatureGrid extends Grid<VectorFeature> {
	public static final int DEFAULT_WIDTH = 430;
	public static final int DEFAULT_HEIGHT = 200;

	public FeatureGrid(int width, int height) {
		super(
				new ListStore<VectorFeature>(
						new ModelKeyProvider<VectorFeature> () {
							@Override
							public String getKey(VectorFeature item) {						
								return item.getFeatureId();
							}					
						}),
				
				new ColumnModel<VectorFeature>(
						new ArrayList<ColumnConfig<VectorFeature, ?>>())
				);
		
		this.setBorders(true);						
		this.getView().setStripeRows(true);
		this.getView().setColumnLines(true);		
		this.setColumnReordering(true);		
		
		this.setWidth(width);
		this.setHeight(height);	
	}
	
	public FeatureGrid() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public void setFeatures(VectorFeature[] vectorFeatures) {
		this.getStore().clear();
		this.getStore().addAll(Arrays.asList(vectorFeatures));
		this.reconfigure(this.getStore(), createColumnList());
	}
	
	private ColumnModel<VectorFeature> createColumnList() {
		List<ColumnConfig<VectorFeature, ?>> columns = new ArrayList<ColumnConfig<VectorFeature, ?>>();
		
		if(this.getStore().size() > 0) {
			VectorFeature feature = this.getStore().get(0);

			if(feature.getAttributes() != null) {
				for(String attributeName : feature.getAttributes().getAttributeNames()) {	
					AttributeValueProvider attributeProvider = new AttributeValueProvider(attributeName);
					
					ColumnConfig<VectorFeature, String> attributeColumn = new ColumnConfig<VectorFeature, String>(
							attributeProvider, 100, attributeName);
					attributeColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
					columns.add(attributeColumn);
				}
			}					
		}
		
		return new ColumnModel<VectorFeature>(columns);
	}
	
	private class AttributeValueProvider implements ValueProvider<VectorFeature, String> {

	    public String attributeName;
	    
	    public AttributeValueProvider(String attributeName) {
	        this.attributeName = attributeName;
	    }

	    @Override
	    public String getValue(VectorFeature feature) {
	        if(feature.getAttributes().getAttributeAsString(attributeName) == null) {
	            return "";
	        } else {
	            return feature.getAttributes().getAttributeAsString(attributeName);
	        }
	    }

	    @Override
	    public void setValue(VectorFeature object, String value) {
	    }

	    @Override
	    public String getPath() {
	        return attributeName;
	    }
	}
}


