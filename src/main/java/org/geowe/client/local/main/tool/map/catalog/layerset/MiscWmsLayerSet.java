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
package org.geowe.client.local.main.tool.map.catalog.layerset;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.main.tool.map.catalog.model.WmsLayerDef;
import org.geowe.client.local.messages.UICatalogMessages;

/**
 * Conjunto de varias definiciones de las capas de interes: Catastro, SIGPAC, etc.
 * 
 * @author Atanasio Muñoz
 *
 */
@ApplicationScoped
public class MiscWmsLayerSet extends AbstractLayerSet {

	public MiscWmsLayerSet() {		
		final WmsLayerDef catastro = new WmsLayerDef();
		catastro.setName(UICatalogMessages.INSTANCE.catastroName());
		catastro.setDescription(UICatalogMessages.INSTANCE
				.catastroDescription());
		catastro.setUrl("http://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx?");
		catastro.setLayerName("CATASTRO");
		catastro.setFormat("PNG");
		catastro.setEpsg("EPSG:3785");
		catastro.setIcon(ImageProvider.INSTANCE.catastro16());

		final WmsLayerDef recintosSp = new WmsLayerDef();
		recintosSp.setName(UICatalogMessages.INSTANCE.enclosuresSigPacName());
		recintosSp.setDescription(UICatalogMessages.INSTANCE
				.enclosuresSigPacDescription());
		recintosSp
				.setUrl("http://ws128.juntadeandalucia.es/agriculturaypesca/geoservergis/wms");
		recintosSp.setLayerName("dgpa_spa_sigpac:SPAD_RECINTOS_2017");
		recintosSp.setFormat("image/png");
		recintosSp.setEpsg("EPSG:25830");
		recintosSp.setIcon(ImageProvider.INSTANCE.sigpac16());

		final WmsLayerDef parcelasSp = new WmsLayerDef();
		parcelasSp.setName(UICatalogMessages.INSTANCE.parcelSigPacName());
		parcelasSp.setDescription(UICatalogMessages.INSTANCE
				.parcelSigPacDescription());
		parcelasSp
				.setUrl("http://ws128.juntadeandalucia.es/agriculturaypesca/geoservergis/wms");
		parcelasSp
				.setLayerName("dgpa_spa_sigpac:SPAD_PARCELAS_2017");
		parcelasSp.setFormat("image/png");
		parcelasSp.setEpsg("EPSG:25830");
		parcelasSp.setIcon(ImageProvider.INSTANCE.sigpac16());
		
		final WmsLayerDef ortoAndalucia = new WmsLayerDef();
		ortoAndalucia
				.setName(UICatalogMessages.INSTANCE.ideAndalucia2010Name());
		ortoAndalucia.setDescription(UICatalogMessages.INSTANCE
				.ideAndalucia2010Description());
		ortoAndalucia.setUrl("http://www.ideandalucia.es/wms/ortofoto2010");
		ortoAndalucia.setLayerName("orto_2010-11");
		ortoAndalucia.setFormat("image/png");
		ortoAndalucia.setEpsg("EPSG:3857");
		ortoAndalucia.setIcon(ImageProvider.INSTANCE.ideAndalucia16());
		
		layers.add(catastro);
		layers.add(parcelasSp);
		layers.add(recintosSp);		
		layers.add(ortoAndalucia);
	}
}
