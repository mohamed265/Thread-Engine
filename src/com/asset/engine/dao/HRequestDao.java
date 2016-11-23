package com.asset.engine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.asset.engine.entity.HRequest;
import com.asset.engine.util.constants.DBEnum;

public class HRequestDao {

	final static Logger logger = Logger.getLogger(HRequestDao.class);

	private String insertQuery;

	private String deleteQuery;

	private PreparedStatement stmt = null;

	public HRequestDao() {
		insertQuery = "INSERT INTO " + DBEnum.REQUEST_HISTORY
				+ " VALUES (H_REQUEST_SEQ.NEXTVAL,?,?,?)";

		deleteQuery = "DELETE FROM " + DBEnum.REQUEST + " WHERE id in (";
	}

	public boolean saveHRequestsBatch(Connection connection,
			ArrayList<HRequest> hRequests) {

		String cDeleteQuery = deleteQuery;
		Savepoint sv = null;
		try {

			stmt = connection.prepareStatement(insertQuery);

			for (int i = 0; i < hRequests.size(); i++) {

				HRequest hRequest = hRequests.get(i);
				stmt.setInt(1, hRequest.getNum());
				stmt.setInt(2, hRequest.getResult());
				stmt.setInt(3, hRequest.getSourceId());
				stmt.addBatch();

				cDeleteQuery += (i == 0 ? "" : ",") + hRequest.getRequestId();
			}
			cDeleteQuery += ')';

			connection.setAutoCommit(false);
			sv = connection.setSavepoint();
			stmt.executeBatch();
			if (hRequests != null || hRequests.size() != 0)
				connection.createStatement().executeUpdate(cDeleteQuery);
			connection.commit();
			connection.setAutoCommit(true);
			return true;
		} catch (Throwable th) {
			try {
				connection.setAutoCommit(true);
				connection.rollback(sv);
				logger.error("SQL EXCEPTION CHANGES ROLLBACKED ", th);
			} catch (SQLException e) {
				logger.error("SQL EXCEPTION ROLLBACK FAIL ", e);
			}
			return false;
		} finally {
			cleanUp();
		}
	}

	public void cleanUp() {
		try {
			if (stmt != null && !stmt.isClosed())
				stmt.close();
		} catch (SQLException e) {
			logger.error("SQLException", e);
		}
	}
}
