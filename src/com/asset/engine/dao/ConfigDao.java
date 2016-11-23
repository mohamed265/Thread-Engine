/**
 * 
 */
package com.asset.engine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.asset.engine.util.constants.DBEnum;

/**
 * @author mohamed.morsy
 *
 */
public class ConfigDao {

	final static Logger logger = Logger.getLogger(ConfigDao.class);

	public static ConcurrentHashMap<String, Object> LoadSystemConfig(Connection connection) {
		ConcurrentHashMap<String, Object> ma = new ConcurrentHashMap<String, Object>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM " + DBEnum.SYSTEM_CONFIGURATION;
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("KEY");
				String value = rs.getString("VALUE");

				ma.put(key, mapKeyValueToType(key, value));
			}
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
			try {
				if (rs != null && !rs.isClosed())
					rs.close();

				if (stmt != null && !stmt.isClosed())
					stmt.close();
			} catch (SQLException e) {
				logger.error("SQLException", e);
			}
		}

		return ma;
	}

	private static Object mapKeyValueToType(String key, String value) {
		if (key.equals(DBEnum.SYS_NUM_OF_SOURCES)) {
			return Integer.parseInt(value);
		} else if (key.equals(DBEnum.SYS_SHUTDOWN)) {
			return Boolean.parseBoolean(value);
		} else if (key.equals(DBEnum.SYS_URL)) {
			return value;
		} else if (key.equals(DBEnum.SYS_MAX_NUM_OF_REQUEST_PER_SOURCE)) {
			return Integer.parseInt(value);
		} else if (key.equals(DBEnum.SYS_NUM_OF_CONSUMERS)) {
			return Integer.parseInt(value);
		} else if (key.equals(DBEnum.SYS_NUM_OF_PRODUCERS)) {
			return Integer.parseInt(value);
		} else if (key.equals(DBEnum.SYS_NUM_OF_SAVERS)) {
			return Integer.parseInt(value);
		} else if (key.equals(DBEnum.SYS_MAX_BATCH_SIZE)) {
			return Integer.parseInt(value);
		} else if (key.equals(DBEnum.SYS_LISTENER_SLEEP_TIME)) {
			return Long.parseLong(value);
		} else if (key.equals(DBEnum.SYS_MAX_HREQUEST_QUEUE)) {
			return Integer.parseInt(value);
		} else if (key.equals(DBEnum.SYS_MAX_REQUEST_QUEUE)) {
			return Integer.parseInt(value);
		} else if (key.equals(DBEnum.SYS_CLOSE_ENGINE_WHEN_DATA_RUN_OUT)) {
			return Boolean.parseBoolean(value);
		} else if (key.equals(DBEnum.SYS_IS_SINGLE_CALLING_WEB_INTERFACE)) {
			return Boolean.parseBoolean(value);
		}
		return null;

	}

	public static ConcurrentHashMap<String, Object> LoadDefaultSystemConfig() {
		ConcurrentHashMap<String, Object> ma = new ConcurrentHashMap<String, Object>();

		ma.put(DBEnum.SYS_NUM_OF_SOURCES, 3);

		ma.put(DBEnum.SYS_SHUTDOWN, false);

		ma.put(DBEnum.SYS_URL, "http://localhost:8080/WebInterface/webinterface");

		ma.put(DBEnum.SYS_NUM_OF_CONSUMERS, 1);

		ma.put(DBEnum.SYS_NUM_OF_PRODUCERS, 1);

		ma.put(DBEnum.SYS_NUM_OF_SAVERS, 1);

		ma.put(DBEnum.SYS_LISTENER_SLEEP_TIME, (long) 5000);

		ma.put(DBEnum.SYS_CLOSE_ENGINE_WHEN_DATA_RUN_OUT, true);

		return ma;
	}

}
