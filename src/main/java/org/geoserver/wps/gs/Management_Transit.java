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

@DescribeProcess(title = "Management-WPS", description = "Management WPS - Network-based approach (Pedestrian & Transit)")
public class Management_Transit implements GSProcess {

	@DescribeResult(name = "Accessibility Score", description = "Accessibility score calculated based on Network-based approach (Pedestrian & Transit)")
	public String execute(
			@DescribeParameter(name = "StartPoint", description = "Walking start point") String start_point,
			@DescribeParameter(name = "StartTime", description = "Walking start time") String start_time,
			@DescribeParameter(name = "WalkingTimePeriod", description = "Walking time period") String walking_time_period,
			@DescribeParameter(name = "WalkingSpeed", description = "Walking speed") String walking_speed,
			@DescribeParameter(name = "BusWaitingTime", description = "Bus waiting time") String bus_waiting_time,
			@DescribeParameter(name = "BusRideTime", description = "Bus ride time") String bus_ride_time,
			@DescribeParameter(name = "DistanceDecayFunction", description = "Distance Decay Function") String distance_decay_function) {
		return CallManagementService(start_point, start_time,
				walking_time_period, walking_speed, bus_waiting_time,
				bus_ride_time, distance_decay_function);
	}

	public static String CallManagementService(String start_point,
			String start_time, String walking_time_period,
			String walking_speed, String bus_waiting_time,
			String bus_ride_time, String distance_decay_function) {
		URL url;
		String line;
		HttpURLConnection connection;
		StringBuilder sb = new StringBuilder();
		String url_string;

		try {

			url_string = "http://127.0.0.1:9363/management?start_point="
					+ start_point + "&start_time=" + start_time
					+ "&walking_time_period=" + walking_time_period
					+ "&walking_speed=" + walking_speed + "&bus_waiting_time="
					+ bus_waiting_time + "&bus_ride_time=" + bus_ride_time
					+ "&distance_decay_function=" + distance_decay_function;

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
