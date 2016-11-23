package com.asset.engine.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

public class DBConnectionManager {

	final static Logger logger = Logger.getLogger(DBConnectionManager.class);

	private static String url;
	private static String user;
	private static String password;
	private static String driver;
	private static int poolSize;

	private static ArrayBlockingQueue<Connection> pool;

	static {
		try {
			DBProperties dpl = new DBProperties();
			dpl.loadConfig();

			url = dpl.getUrl();
			password = dpl.getPassword();
			driver = dpl.getDriver();
			user = dpl.getUser();
			poolSize = dpl.getPoolSize();

			Class.forName(driver);
			init();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void init() {
		try {
			pool = new ArrayBlockingQueue<Connection>(poolSize);
			for (int i = 0; i < poolSize; i++) {
				try {
					pool.put(getNewConnection());
				} catch (InterruptedException | SQLException e) {
					logger.error("init connection fail", e);
				}
			}
			logger.info("pool created num of available connection: "
					+ pool.size());
		} catch (Exception c) {
			logger.info("pool creation fail ");
		}
	}

	public static Connection getConnectionFromPool()
			throws InterruptedException {
		return pool.take();
	}

	public static void backConnectionToPool(Connection connection)
			throws InterruptedException {
		try {
			if (connection != null && !connection.isClosed())
				pool.put(connection);
			else {
				logger.info("cloesd connetion");
				pool.put(getNewConnection());
			}
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	private static Connection getNewConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public static void cleanUp() {
		for (Connection connection : pool) {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
					pool.remove();
				}
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		logger.info("pool closed num of alive connection: " + pool.size());
		if (pool.size() == 0)
			pool = null;
	}

	public static void reloadDBConfig() throws SQLException {
		// TODO to be implemented
	}
}
