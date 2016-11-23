/**
 * 
 */
package com.asset.engine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.asset.engine.EngineMain;
import com.asset.engine.entity.Request;
import com.asset.engine.util.constants.DBEnum;

/**
 * @author mohamed.morsy
 *
 */
public class RequestLoaderDao {

	final static Logger logger = Logger.getLogger(RequestLoaderDao.class);

	private String selectQuery, updateQuery;

	private PreparedStatement stmt = null;

	private ResultSet rs = null;

	public RequestLoaderDao() {
		selectQuery = "SELECT * FROM " + DBEnum.REQUEST
				+ " WHERE MOD (id, ?) = ? and in_processing = ? and ROWNUM <= ?";
		updateQuery = "update request set in_processing = 1 where id in (";
	}

	public ArrayList<Request> loadRequestsBatch(Connection connection,
			int index, int size) {

		ArrayList<Request> requests = new ArrayList<Request>();

		try {
			stmt = connection.prepareStatement(selectQuery);

			String s = updateQuery;

			stmt.setInt(1, size);
			stmt.setInt(2, index);
			stmt.setBoolean(3, false);
			stmt.setInt(4, (int) EngineMain.em
					.getConfigValue(DBEnum.SYS_MAX_BATCH_SIZE));

			rs = stmt.executeQuery();
			boolean flag = true;

			while (rs.next()) {
				Request re = new Request();
				re.setId(rs.getInt(1));
				re.setNum(rs.getInt(2));
				re.setSourceId(rs.getInt(3));
				requests.add(re);
				if (flag) {
					s += re.getId();
					flag = false;
				} else
					s += " , " + re.getId();
			}
			s += ')';
			if (!flag)
				connection.createStatement().executeUpdate(s);
		} catch (Exception e) {
			requests = new ArrayList<Request>();
			logger.error("SQLException", e);
		} finally {
			cleanUp();
		}
		return requests;
	}

	public void cleanUp() {
		try {
			if (stmt != null && !stmt.isClosed())
				stmt.close();
			if (rs != null && !rs.isClosed())
				rs.close();
		} catch (SQLException e) {
			logger.error("SQLException", e);
		}
	}
}