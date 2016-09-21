package org.geowe.client.local.model.vector;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.sencha.gxt.core.client.ValueProvider;

public class AttributeValueProvider implements ValueProvider<VectorFeature, String> {

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
