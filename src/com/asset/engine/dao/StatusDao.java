/**
 * 
 */
package com.asset.engine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.asset.engine.util.constants.DBEnum;

/**
 * @author mohamed.morsy
 *
 */
public class StatusDao {

	final static Logger logger = Logger.getLogger(StatusDao.class);

	public static boolean turnEngineOn(Connection connection) {
		return updateStatusMethod(connection, false);
	}

	public static boolean turnEngineOff(Connection connection) {
		return updateStatusMethod(connection, true);
	}

	private static boolean updateStatusMethod(Connection connection,
			boolean status) {
		PreparedStatement stmt = null;
		try {
			String sql = "UPDATE " + DBEnum.SYSTEM_CONFIGURATION
					+ " SET value = ? WHERE key = ?";
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, "true");
			stmt.setString(2, DBEnum.SYS_SHUTDOWN);
			return stmt.executeUpdate() == 1;
		} catch (Exception e) {
			logger.error("Exception", e);
			return false;
		} finally {
			try {
				if (stmt != null && !stmt.isClosed())
					stmt.close();
			} catch (SQLException e) {
				logger.error("SQLException", e);
			}
		}
	}
}
