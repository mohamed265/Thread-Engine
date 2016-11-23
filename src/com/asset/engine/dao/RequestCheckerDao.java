/**
 * 
 */
package com.asset.engine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.asset.engine.util.constants.DBEnum;

/**
 * @author mohamed.morsy
 *
 */
public class RequestCheckerDao {

	final static Logger logger = Logger.getLogger(RequestCheckerDao.class);

	private static String checkQuery;

	private static PreparedStatement stmt = null;

	private static ResultSet rs = null;

	@SuppressWarnings("finally")
	public static int getNumberOfData(Connection connection) {

		checkQuery = "SELECT COUNT(*) FROM  " + DBEnum.REQUEST
				+ " WHERE in_processing = 0";
		int result = 0;
		try {
			stmt = connection.prepareStatement(checkQuery);

			rs = stmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
			cleanUp();
			return result;
		}
	}

	public static void cleanUp() {
		try {
			if (stmt != null && !stmt.isClosed())
				stmt.close();
			if (rs != null && !rs.isClosed())
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
