/**
 * 
 */
package com.asset.engine.service;

import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.asset.engine.dao.ConfigDao;
import com.asset.engine.dao.RequestCheckerDao;
import com.asset.engine.dao.StatusDao;
import com.asset.engine.util.DBConnectionManager;

/**
 * @author mohamed.morsy
 *
 */
public class EngineService {

	final static Logger logger = Logger.getLogger(EngineService.class);

	public static ConcurrentHashMap<String, Object> LoadSystemConfig() {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnectionFromPool();
			return ConfigDao.LoadSystemConfig(connection);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
			return new ConcurrentHashMap<String, Object>();
		} finally {
			cleanUp(connection);
		}
	}

	public static int getNumberOfData() {
		Connection connection = null;
		try {
			connection = DBConnectionManager.getConnectionFromPool();
			return RequestCheckerDao.getNumberOfData(connection);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
			return 0;
		} finally {
			cleanUp(connection);
		}
	}

	public static boolean turnEngineOn() {
		Connection connection = null;
		boolean flag;
		try {
			connection = DBConnectionManager.getConnectionFromPool();
			flag = StatusDao.turnEngineOn(connection);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
			flag = false;
		} finally {
			cleanUp(connection);
		}
		return flag;
	}

	public static boolean turnEngineOff() {
		Connection connection = null;
		boolean flag;
		try {
			connection = DBConnectionManager.getConnectionFromPool();
			flag = StatusDao.turnEngineOff(connection);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
			flag = false;
		} finally {
			cleanUp(connection);
		}
		return flag;
	}

	private static void cleanUp(Connection connection) {
		try {
			DBConnectionManager.backConnectionToPool(connection);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
	}
}
