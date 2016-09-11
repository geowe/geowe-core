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
package org.geowe.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.geowe.client.shared.rest.w3w.W3WService;
import org.jboss.errai.security.server.properties.ErraiAppPropertiesProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * W3WServiceImpl representa la comunicación con what3words para obtener la geolocalización por tres palabras
 * 
 * @author jose@geowe.org
 *
 */

public class W3WServiceImpl extends RemoteServiceServlet implements W3WService {

	private static final long serialVersionUID = 3229953777081292540L;
	private static final Logger LOG = LoggerFactory
			.getLogger(W3WServiceImpl.class.getName());
	private String apiKey;
	private String baseUrl;

	public W3WServiceImpl() {
		initProperties();
	}

	@Override
	public String getPosition(final String words, final String lang) {
		String response = "";
		final String url = String.format("%sv2/forward?key=%s&addr=%s&lang=%s",
				this.baseUrl, this.apiKey, words, lang);
		try {
			response = doHttpGet(url);
		} catch (RuntimeException e) {
			LOG.error("getPosition: " + url + ": " + e.getMessage());
		}
		return response;
	}

	@Override
	public String get3Words(final String latLong, final String lang) {
		String response = "";
		final String url = String.format("%sv2/reverse?key=%s&coords=%s&lang=%s",
				this.baseUrl, this.apiKey, latLong, lang);
		try {
			response = doHttpGet(url);
		} catch (RuntimeException e) {
			LOG.error("get3Words: " + url + ": " + e.getMessage());
		}
		return response;
	}


	private String doHttpGet(final String url) {
		HttpURLConnection con;
		BufferedReader in = null;
		final StringBuffer response = new StringBuffer();
		try {
			final URL urlObject = new URL(url);
			con = (HttpURLConnection) urlObject.openConnection();
			con.setRequestMethod("GET");

			in = new BufferedReader(new InputStreamReader(con.getInputStream(),
					StandardCharsets.UTF_8));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (IOException e) {
			LOG.error("w3w IOException", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				LOG.error("w3w getException(): Fail to close Reader");
			}
		}
		
		LOG.info(response.toString());
		return response.toString();
	}

	private void initProperties() {
		final ErraiAppPropertiesProducer producer = new ErraiAppPropertiesProducer();
		final Properties prop = producer.getErraiAppProperties();
		this.apiKey = prop.getProperty("w3w.key");
		this.baseUrl = prop.getProperty("w3w.url");
	}
}