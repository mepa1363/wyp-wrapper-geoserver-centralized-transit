package org.geoserver.wps.gs;

import org.apache.commons.httpclient.util.URIUtil;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

@DescribeProcess(title = "POI-WPS", description = "POI WPS - Network-based approach (Pedestrian & Transit)")
public class POI_Transit_Centralized implements GSProcess {

	@DescribeResult(name = "POIResult", description = "POI result")
	public String execute(
			@DescribeParameter(name = "Walkshed", description = "Walkshed") String walkshed) {
		return CallPOIService(walkshed);
	}

	public static String CallPOIService(String walkshed) {
		URL url;
		String line;
		HttpURLConnection connection;
		StringBuilder sb = new StringBuilder();
		String url_string;

		try {

			url_string = "http://127.0.0.1:9365/poi?walkshed=" + walkshed;
			url_string = URIUtil.encodeQuery(url_string);
			url = new URL(url_string);

			connection = (HttpURLConnection) url.openConnection();

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			rd.close();

			connection.disconnect();

		} catch (Exception e) {
			System.out.println("Errors...");
		}

		return sb.toString();
	}
}
