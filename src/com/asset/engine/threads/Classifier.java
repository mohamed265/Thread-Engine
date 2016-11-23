/**
 * 
 */
package com.asset.engine.threads;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.asset.engine.entity.HRequest;
import com.asset.engine.entity.Request;
import com.asset.engine.statics.HRequestsManager;
import com.asset.engine.statics.RequestsManager;
import com.asset.engine.util.ThreadUtils;
import com.asset.engine.util.WebInterfaceConnector;

/**
 * @author mohamed.morsy
 *
 */
public class Classifier extends Thread {

	final static Logger logger = Logger.getLogger(Classifier.class);

	private static int id = 1;

	private int index;

	private WebInterfaceConnector webInterfaceConnector;

	private boolean isRunning;

	private boolean isIdeal;

	private boolean isSingleProcessing;

	private String url;

	public Classifier(String url, boolean isSingleProcessing) {
		ThreadUtils.registerMe(this);
		isIdeal = isRunning = true;
		this.isSingleProcessing = isSingleProcessing;
		this.url = url;
		setName("C" + (id++));
	}

	@Override
	public void run() {

		logger.info("Classifier Thread: " + getName() + " started Successfully");
		webInterfaceConnector = new WebInterfaceConnector(url);
		ArrayList<Request> requests = null;

		while (isRunning) {
			try {
				isIdeal = true;
				requests = RequestsManager.getRequestsBatch();
			} catch (InterruptedException e) {
				if (isRunning)
					logger.error("", e);
				continue;
			}

			isIdeal = false;

			try {

				ArrayList<HRequest> hRequests;

				if (isSingleProcessing)
					hRequests = singleProcessing(requests);
				else
					hRequests = batchProcessing(requests);

				if (hRequests == null) {
					RequestsManager.putRequestsBatch(requests);
					logger.error("Requests Backed to Queue");
				} else {
					HRequestsManager.putHRequestsBatch(hRequests);
				}
			} catch (InterruptedException e) {
				logger.error("", e);
				try {
					RequestsManager.putRequestsBatch(requests);
					logger.error("requests backed to queue");
				} catch (InterruptedException e1) {
					logger.error("Failed to back requests to queue", e1);
				}
			}
		}
		logger.info("Classifier Thread: " + getName() + " finished Successfully");
		destroy();
	}

	private ArrayList<HRequest> singleProcessing(ArrayList<Request> requests) {
		ArrayList<HRequest> hRequests = new ArrayList<HRequest>();
		for (Request request : requests) {
			hRequests.add(webInterfaceConnector.processSingleRequest(request));
		}
		logger.debug("response from web interface for all " + requests.size() + " requests");
		return hRequests;
	}

	private ArrayList<HRequest> batchProcessing(ArrayList<Request> requests) {
		return webInterfaceConnector.processBatchRequests(requests);
	}

	public void destroy() {
		logger.info("Classifier Thread: " + getName() + " destroyed Successfully");
		ThreadUtils.removeMe(this);
	}

	public void stopThread() {
		isRunning = false;
	}

	public void setUrl(String url) {
		this.url = url;
		webInterfaceConnector.setUrl(url);
	}

	public int getIndex() {
		return index;
	}

	public boolean isIdeal() {
		return isIdeal;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isSingleProcessing() {
		return isSingleProcessing;
	}

	public void setSingleProcessing(boolean isSingleProcessing) {
		this.isSingleProcessing = isSingleProcessing;
	}

	public String getUrl() {
		return url;
	}
}
