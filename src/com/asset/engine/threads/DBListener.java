/**
 * 
 */
package com.asset.engine.threads;

import org.apache.log4j.Logger;

import com.asset.engine.service.EngineService;
import com.asset.engine.util.ThreadUtils;
import com.asset.engine.util.constants.DBEnum;

/**
 * @author mohamed.morsy
 *
 */
public class DBListener extends Thread {

	final static Logger logger = Logger.getLogger(DBListener.class);

	private static DBListener listener;

	private Long sleepTime;

	private boolean isRunning;

	private DBListener() {
		setName("listener");
		isRunning = true;
	}

	public static DBListener getInstance() {
		if (listener == null)
			listener = new DBListener();
		return listener;
	}

	@Override
	public void run() {

		logger.info("Listener Thread: " + getName() + " started Successfully");

		while (isRunning) {

			loadConfig();

			loadNumberOfData();

			ThreadUtils.notifyMasterThread();

			// close engine when data run out
			if ((boolean) MasterThread.newMap.get(DBEnum.SYS_CLOSE_ENGINE_WHEN_DATA_RUN_OUT) && !MasterThread.isNewData) {
				stopThread();
			}

			if (isRunning)
				sleep();
		}

		logger.info("Listener Thread: " + getName() + " finished Successfully");
		destroy();
	}

	public void stopThread() {
		isRunning = false;
	}

	public void destroy() {
		logger.info("Listener Thread: " + getName() + " destroyed Successfully");
	}

	private void sleep() {
		try {
			Thread.sleep(sleepTime);
		} catch (Exception e) {
			logger.error("lintener fail to sleep", e);
		}
	}

	private void loadConfig() {
		try {
			MasterThread.newMap = EngineService.LoadSystemConfig();
			sleepTime = (Long) MasterThread.newMap.get(DBEnum.SYS_LISTENER_SLEEP_TIME);
			if (!(boolean) MasterThread.newMap.get(DBEnum.SYS_SHUTDOWN))
				stopThread();

			logger.info("configuration loaded successfully");
		} catch (Exception e) {
			logger.error("load configuration fail", e);
		}
	}

	private void loadNumberOfData() {
		MasterThread.isNewData = EngineService.getNumberOfData() != 0;
		logger.info("dbChecker done successfully " + MasterThread.isNewData);
	}
}
