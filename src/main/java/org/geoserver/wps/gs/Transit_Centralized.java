package org.geoserver.wps.gs;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

@DescribeProcess(title = "Transit-WPS", description = "Transit WPS - Network-based approach (Pedestrian & Transit)")
public class Transit_Centralized implements GSProcess {

	@DescribeResult(name = "TransitResult", description = "Transit data")
	public String execute(
			@DescribeParameter(name = "StartTime", description = "Walking start time") String start_time,
			@DescribeParameter(name = "Walkshed", description = "Walkshed") String walkshed,
			@DescribeParameter(name = "WalkingTime", description = "Walking time period") String walking_time_period,
			@DescribeParameter(name = "WalkingSpeed", description = "Walking speed") String walking_speed,
			@DescribeParameter(name = "BusWaitingTime", description = "Bus waiting time") String bus_waiting_time,
			@DescribeParameter(name = "BusRideTime", description = "Bus riding time") String bus_ride_time) {
		return CallTransitService(start_time, walkshed, walking_time_period,
				walking_speed, bus_waiting_time, bus_ride_time);
	}

	public static String CallTransitService(String start_time, String walkshed,
			String walking_time_period, String walking_speed,
			String bus_waiting_time, String bus_ride_time) {
		URL url;
		String line;
		HttpURLConnection connection;
		StringBuilder sb = new StringBuilder();
		String url_string;

		try {
			url_string = "http://127.0.0.1:9367/transit?start_time="
					+ start_time + "&walkshed=" + walkshed
					+ "&walking_time_period=" + walking_time_period
					+ "&walking_speed=" + walking_speed + "&bus_waiting_time="
					+ bus_waiting_time + "&bus_ride_time=" + bus_ride_time;

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
