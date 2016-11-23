/**
 * 
 */
package com.asset.engine.threads;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.asset.engine.entity.Request;
import com.asset.engine.service.RequestService;
import com.asset.engine.statics.ProducerIndexManager;
import com.asset.engine.statics.RequestsManager;
import com.asset.engine.util.ThreadUtils;

/**
 * @author mohamed.morsy
 *
 */
public class DBFetcher extends Thread {

	final static Logger logger = Logger.getLogger(DBFetcher.class);

	private static int id = 1;

	private int index = -1;

	private RequestService requestService;

	private boolean isRunning;

	public DBFetcher() throws SQLException {
		requestService = new RequestService();
		ThreadUtils.registerMe(this);
		isRunning = true;
		setName("P" + (id++));
	}

	@Override
	public void run() {
		logger.info("DBFetcher Thread: " + getName() + " started Successfully");

		while (isRunning) {

			setIndex(ProducerIndexManager.getIndex(getIndex()));

			ArrayList<Request> requests = requestService.loadRequestsBatch(
					getIndex(), ThreadUtils.getNumOfDBFetchers());

			logger.debug("DBFetcher: " + getName() + " Index " + getIndex()
					+ " Fetch " + requests.size() + " Item");

			if (requests != null && requests.size() == 0) {
				ThreadUtils.waitMe();
			} else {
				try {
					RequestsManager.putRequestsBatch(requests);
				} catch (InterruptedException e) {
					logger.error("Fail to add requests to queue", e);
				}
			}
		}

		logger.info("DBFetcher Thread: " + getName() + " finished Successfully");
		destroy();
	}

	public void destroy() {
		logger.info("DBFetcher Thread: " + getName()
				+ " destroyed Successfully");
		ThreadUtils.removeMe(this);
	}

	public void stopThread() {
		isRunning = false;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
