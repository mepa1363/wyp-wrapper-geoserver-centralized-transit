package org.geoserver.wps.gs;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

@DescribeProcess(title = "Aggreagtaion-WPS", description = "Aggregation WPS - Network-based approach (Pedestrian & Transit)")
public class Aggregation_Transit_Centralized implements GSProcess {

	@DescribeResult(name = "AggregationResult", description = "Aggregated data")
	public String execute(
			@DescribeParameter(name = "POI", description = "POI data") String poi,
			@DescribeParameter(name = "Crime", description = "Crime data") String crime,
			@DescribeParameter(name = "Transit", description = "Transit data") String transit,
			@DescribeParameter(name = "StartPoint", description = "Walking start point") String start_point,
			@DescribeParameter(name = "WalkshedCollection", description = "Walkshed collection") String walkshed_collection,
			@DescribeParameter(name = "WalkshedUnion", description = "Walkshed union") String walkshed_union,
			@DescribeParameter(name = "DistanceDecayFunction", description = "Distance Decay Function") String distance_decay_function,
			@DescribeParameter(name = "WalkingTimePeriod", description = "Walking time period") String walking_time_period) {
		return CallAggregationService(start_point, walkshed_collection,
				walkshed_union, poi, crime, transit, distance_decay_function,
				walking_time_period);
	}

	public static String CallAggregationService(String start_point,
			String walkshed_collection, String walkshed_union, String poi,
			String crime, String transit, String distance_decay_function,
			String walking_time_period) {
		URL url;
		String line;
		HttpURLConnection connection;
		StringBuilder sb = new StringBuilder();
		String url_string;
		String url_parameters;
		String USER_AGENT = "Mozilla/5.0";
		DataOutputStream wr;

		try {

			url_string = "http://127.0.0.1:9364/aggregation";
			url_parameters = "start_point=" + start_point
					+ "&walkshed_collection=" + walkshed_collection
					+ "&walkshed_union=" + walkshed_union + "&poi=" + poi
					+ "&crime=" + crime + "&transit=" + transit
					+ "&walking_time_period=" + walking_time_period
					+ "&distance_decay_function=" + distance_decay_function;

			url = new URL(url_string);

			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			connection.setDoOutput(true);
			wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(url_parameters);
			wr.flush();
			wr.close();

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
