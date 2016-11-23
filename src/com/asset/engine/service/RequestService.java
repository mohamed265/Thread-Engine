/**
 * 
 */
package com.asset.engine.service;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.asset.engine.dao.HRequestDao;
import com.asset.engine.dao.RequestLoaderDao;
import com.asset.engine.entity.HRequest;
import com.asset.engine.entity.Request;
import com.asset.engine.util.DBConnectionManager;

/**
 * @author mohamed.morsy
 *
 */
public class RequestService {

	final static Logger logger = Logger.getLogger(RequestService.class);

	private RequestLoaderDao requestLoaderDao;

	private HRequestDao hRequestDao;

	public ArrayList<Request> loadRequestsBatch(int index, int size) {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnectionFromPool();
			return getRequestLoaderDao().loadRequestsBatch(connection, index,
					size);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
			return new ArrayList<Request>();
		} finally {
			cleanUp(connection);
		}
	}

	public boolean saveHRequestsBatch(ArrayList<HRequest> hRequests) {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnectionFromPool();
			return getHRequestDao().saveHRequestsBatch(connection, hRequests);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
			return false;
		} finally {
			cleanUp(connection);
		}
	}

	private void cleanUp(Connection connection) {
		try {
			DBConnectionManager.backConnectionToPool(connection);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
	}

	public RequestLoaderDao getRequestLoaderDao() {
		if (requestLoaderDao == null)
			requestLoaderDao = new RequestLoaderDao();
		return requestLoaderDao;
	}

	public HRequestDao getHRequestDao() {
		if (hRequestDao == null)
			hRequestDao = new HRequestDao();
		return hRequestDao;
	}
}
