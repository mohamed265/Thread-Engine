/**
 * 
 */
package com.asset.engine.threads;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.asset.engine.entity.HRequest;
import com.asset.engine.service.RequestService;
import com.asset.engine.statics.HRequestsManager;
import com.asset.engine.util.ThreadUtils;

/**
 * @author mohamed.morsy
 *
 */
public class Saver extends Thread {

	final static Logger logger = Logger.getLogger(Saver.class);

	private static int id = 1;

	private int index;

	private RequestService requestService;

	private boolean isRunning;

	private boolean isIdeal;

	public Saver() throws SQLException {
		requestService = new RequestService();
		ThreadUtils.registerMe(this);
		isIdeal = isRunning = true;
		setName("S" + (id++));
	}

	@Override
	public void run() {
		logger.info("Saver Thread: " + getName() + " started Successfully");
		ArrayList<HRequest> hRequests = null;

		while (isRunning) {
			try {
				isIdeal = true;
				hRequests = HRequestsManager.getHRequestsBatch();
			} catch (InterruptedException e) {
				if (isRunning)
					logger.error("InterruptedException", e);
				continue;
			}
			
			isIdeal = false;

			if (requestService.saveHRequestsBatch(hRequests))
				logger.debug("Saver Thread: " + getName() + " Insert and Delete " + hRequests.size());
			else {
				try {
					HRequestsManager.putHRequestsBatch(hRequests);
					logger.error("Exception Hrequests backed to the queue");
				} catch (InterruptedException e) {
					logger.error("Exception Hrequests Failed to back to the queue", e);
				}
			}
		}

		logger.info("Saver Thread: " + getName() + " finished Successfully");
		destroy();
	}

	public void destroy() {
		logger.info("Saver Thread: " + getName() + " destroyed Successfully");
		ThreadUtils.removeMe(this);
	}

	public void stopThread() {
		isRunning = false;
	}

	public boolean isIdeal() {
		return isIdeal;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
