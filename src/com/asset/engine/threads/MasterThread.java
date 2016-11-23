package com.asset.engine.threads;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.asset.engine.EngineMain;
import com.asset.engine.statics.HRequestsManager;
import com.asset.engine.statics.RequestsManager;
import com.asset.engine.util.ThreadUtils;
import com.asset.engine.util.constants.DBEnum;

public class MasterThread extends Thread {

	final static Logger logger = Logger.getLogger(MasterThread.class);

	public static ConcurrentHashMap<String, Object> newMap;

	private DBListener listener;

	public static boolean isNewData;

	private boolean isRunning;

	private boolean isClosing;

	private static MasterThread masterThread;

	public static MasterThread getInstance() {
		if (masterThread == null) {
			masterThread = new MasterThread();
		}
		return masterThread;
	}

	private MasterThread() {
		setName("Master Thread");
		isRunning = true;
		isClosing = false;
	}

	@Override
	public void run() {

		logger.info("MasterThread Thread: " + getName() + " started Successfully");

		try {
			listener = DBListener.getInstance();
			listener.start();
		} catch (Exception e) {
			logger.error("Listener Fail to start", e);
			logger.error("Engine Fail to start :");
			destroy();
			return;
		}

		while (isRunning) {

			ThreadUtils.waitMe();

			System.out.println("================================");
			updateConfig();

			if (isRunning)
				initThreads();

			if (isNewData && isRunning) {
				ThreadUtils.notifyDBFetchers();
				// ThreadUtils.notifyAllThreads();
			}

			logger.debug("System Request queue is: " + RequestsManager.getbqsize());

			logger.debug("System Responce queue is: " + HRequestsManager.getbqsize());
			System.out.println("================================");
		}

		do {
			sleep();
			try {
				if (RequestsManager.getbqsize() == 0 && ThreadUtils.getNumOfDBFetchers() == 0
						&& ThreadUtils.isAllClassifiersIdeal()) {
					ThreadUtils.stopAllClassifiers();
					ThreadUtils.interruptClassifiers();
				}

				if (HRequestsManager.getbqsize() == 0 && ThreadUtils.getNumOfClassifiers() == 0
						&& ThreadUtils.isAllSaversIdeal()) {
					ThreadUtils.stopAllSavers();
					ThreadUtils.interruptSavers();
					isClosing = false;
				}
			} catch (Exception e) {
				logger.error("ERROR", e);
			}
		} while (isClosing);

		if (ThreadUtils.getNumOfDBFetchers() != 0) {
			ThreadUtils.interruptDBFetchers();
			logger.error("Alive fetchers " + ThreadUtils.getNumOfDBFetchers() + " Thread");
		}

		if (ThreadUtils.getNumOfClassifiers() != 0) {
			ThreadUtils.interruptClassifiers();
			logger.error("Alive Classifiers " + ThreadUtils.getNumOfClassifiers() + " Thread");
		}

		if (ThreadUtils.getNumOfSavers() != 0) {
			ThreadUtils.interruptSavers();
			logger.error("Alive savers " + ThreadUtils.getNumOfSavers() + " Thread");
		}

		logger.info("MasterThread Thread: " + getName() + " finished Successfully");
		destroy();
	}

	private void sleep() {
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			logger.error("Master Thread fail to sleep", e);
		}
	}

	public void destroy() {
		logger.info("MasterThread Thread: " + getName() + " destroyed Successfully");
		EngineMain.em.close();
	}

	public void stopThread() {
		isRunning = false;
		isClosing = true;
	}

	private void initThreads() {
		try {
			ThreadUtils.initThreads();
			logger.info("init threads successfully");
		} catch (SQLException e) {
			logger.error("init threads fail", e);
		}
	}

	private void updateConfig() {
		if (EngineMain.em.getConfigMap() != null) {

			// if (!EngineMain.em.getConfigValue(DBEnum.SYS_MAX_HREQUEST_QUEUE)
			// .equals(newMap.get(DBEnum.SYS_MAX_HREQUEST_QUEUE))) {
			// int newSzie = (int) newMap.get(DBEnum.SYS_MAX_HREQUEST_QUEUE);
			// HRequestsManager.resizeTo(newSzie);
			// }
			//
			// if (!EngineMain.em.getConfigValue(DBEnum.SYS_MAX_REQUEST_QUEUE)
			// .equals(newMap.get(DBEnum.SYS_MAX_REQUEST_QUEUE))) {
			// int newSzie = (int) newMap.get(DBEnum.SYS_MAX_REQUEST_QUEUE);
			// RequestsManager.resizeTo(newSzie);
			// }

			if (!EngineMain.em.getConfigValue(DBEnum.SYS_URL).equals(newMap.get(DBEnum.SYS_URL))) {
				String url = (String) newMap.get(DBEnum.SYS_URL);
				ThreadUtils.updateConsumersUrl(url);
			}

			if (!EngineMain.em.getConfigValue(DBEnum.SYS_IS_SINGLE_CALLING_WEB_INTERFACE)
					.equals(newMap.get(DBEnum.SYS_IS_SINGLE_CALLING_WEB_INTERFACE))) {
				boolean type = (boolean) newMap.get(DBEnum.SYS_IS_SINGLE_CALLING_WEB_INTERFACE);
				ThreadUtils.updateConsumersWebCallingMechanizm(type);
			}

			// close engine by flag
			if (!EngineMain.em.getConfigValue(DBEnum.SYS_SHUTDOWN).equals(newMap.get(DBEnum.SYS_SHUTDOWN))) {
				if (!(Boolean) newMap.get(DBEnum.SYS_SHUTDOWN)) {
					try {
						ThreadUtils.stopAllDBFetchers();
						ThreadUtils.notifyDBFetchers();
						logger.info("ALL DBFetchers Stoped");
						DBListener.getInstance().stopThread();
						ThreadUtils.notifySavers();
						stopThread();
					} catch (Exception e) {
						logger.error("Exception", e);
					}
				}
			}

			// close engine when data run out
			if ((boolean) MasterThread.newMap.get(DBEnum.SYS_CLOSE_ENGINE_WHEN_DATA_RUN_OUT)
					&& !MasterThread.isNewData) {
				try {
					logger.info("data run out engine will close");
					ThreadUtils.stopAllDBFetchers();
					ThreadUtils.notifyDBFetchers();
					logger.info("ALL DBFetchers Stoped");
					DBListener.getInstance().stopThread();
					ThreadUtils.notifySavers();
					stopThread();
				} catch (Exception e) {
					logger.error("Exception", e);
				}
			}

			EngineMain.em.updateConfigMap(newMap);
		} else {
			EngineMain.em.setConfigMap(newMap);
		}

	}
}
