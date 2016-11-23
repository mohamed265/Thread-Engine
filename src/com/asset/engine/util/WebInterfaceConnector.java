package com.asset.engine.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.asset.engine.entity.HRequest;
import com.asset.engine.entity.Request;

public class WebInterfaceConnector {

	final static Logger logger = Logger.getLogger(WebInterfaceConnector.class);

	private String webInterfaceUrl;

	public WebInterfaceConnector(String url) {
		this.webInterfaceUrl = url;
	}

	public HRequest processSingleRequest(Request request) {

		HRequest hRequest = new HRequest();
		try {

			String s = webInterfaceUrl + "?num=" + request.getNum();

			URL url = new URL(s);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));

				hRequest.setRequestId(request.getId());
				hRequest.setNum(request.getNum());
				hRequest.setSourceId(request.getSourceId());
				hRequest.setResult(in.read());
				in.close();

				//logger.debug("response from web interface 1");

			} else if (connection.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
				hRequest = null;
				logger.error("internal server error");
			}
			connection.disconnect();
		} catch (Exception e) {
			logger.error("Crash In getResponeString URL IS " + webInterfaceUrl,
					e);
			hRequest = null;
		}
		return hRequest;
	}

	public ArrayList<HRequest> processBatchRequests(ArrayList<Request> requests) {
		ArrayList<HRequest> hRequests = new ArrayList<HRequest>();
		try {
			String s = webInterfaceUrl + "?";
			for (int i = 0; i < requests.size(); i++) {
				s += (i == 0 ? "" : "&") + "num=" + requests.get(i).getNum();
			}
			URL url = new URL(s);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));

				for (int i = 0; i < requests.size(); i++) {
					HRequest hRequest = new HRequest();
					hRequest.setRequestId(requests.get(i).getId());
					hRequest.setNum(requests.get(i).getNum());
					hRequest.setSourceId(requests.get(i).getSourceId());
					hRequest.setResult(in.read());

					hRequests.add(hRequest);
				}

				logger.debug("response from web interface " + requests.size());

				in.close();
			} else if (connection.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
				hRequests = null;
				logger.error("internal server error");
			}
			connection.disconnect();
		} catch (Exception e) {
			logger.error("Crash In getResponeString URL IS " + webInterfaceUrl,
					e);
			hRequests = null;
		}
		return hRequests;
	}

	public void setUrl(String url) {
		this.webInterfaceUrl = url;
	}
}
