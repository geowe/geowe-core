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
package org.geowe.client.local.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface UICatalogMessages extends Messages {

	public UICatalogMessages INSTANCE = GWT.create(UICatalogMessages.class);

	String type();

	String description();

	String catastroName();

	String catastroDescription();

	String ignBaseName();

	String ignBaseDescription();

	String enclosuresSigPacName();

	String enclosuresSigPacDescription();

	String parcelSigPacName();

	String parcelSigPacDescription();

	String googleSatelliteName();

	String googleSatelliteDescription();

	String googleNormalName();

	String googleNormalDescription();

	String googleTerrainName();

	String googleTerrainDescription();

	String googleHibrydName();

	String googleHibrydDescription();

	String osmCycleMapName();

	String osmCycleMapDescription();

	String osmMapnikName();

	String osmMapnikDescription();

	String bingAerialLayerName();

	String bingAerialLayerDescription();

	String bingHybridLayerName();

	String bingHybridLayerDescription();

	String bingRoadLayerName();

	String bingRoadLayerDescription();

	String andalusianProvincesName();

	String andalusianProvincesDescription();

	String lebrijaBlocksName();

	String lebrijaBlocksDescription();

	String andalusianWetlandsName();

	String andalusianWetlandsDescription();

	String esriBasicName();

	String esriBasicDescription();

	String ideAndalucia2010Name();

	String ideAndalucia2010Description();
	
	String rutasGastronomicasName();
	
	String rutasGastronomicasDescription();

	String layerNotInCatalog(String layerCatalog);

}