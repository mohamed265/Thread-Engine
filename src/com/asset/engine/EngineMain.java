/**
 * 
 */
package com.asset.engine;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.asset.engine.aop.util.Timer;
import com.asset.engine.service.EngineService;
import com.asset.engine.threads.MasterThread;
import com.asset.engine.util.DBConnectionManager;
import com.asset.engine.util.ThreadUtils;
import com.asset.engine.util.constants.DBEnum;

/**
 * @author mohamed.morsy
 *
 */
public class EngineMain {

	static Logger logger = Logger.getLogger(EngineMain.class);

	public static EngineMain em;

	public static void main(String[] args) {

		em = new EngineMain();

		em.start();
	}

	private ConcurrentHashMap<String, Object> configMap;

	public boolean start() {
		EngineService.turnEngineOn();
		MasterThread.getInstance().start();
		logger.info("Engine Start Successfully");
		return true;
	}

	public boolean close() {
		try {
			EngineService.turnEngineOff();
			DBConnectionManager.cleanUp();
			if (ThreadUtils.getNumOfDBFetchers() != 0 || ThreadUtils.getNumOfClassifiers() != 0
					|| ThreadUtils.getNumOfSavers() != 0) {
				logger.error("Engine Fail to close");
				logger.error(
						"Engine Fail to close, There is " + ThreadUtils.getNumOfDBFetchers() + " Alive DBFetchers");
				logger.error(
						"Engine Fail to close, There is " + ThreadUtils.getNumOfClassifiers() + " Alive Classifiers");
				logger.error("Engine Fail to close, There is " + ThreadUtils.getNumOfSavers() + " Alive Savers");
				logger.error("Force Stop");
				Timer.printReportInMillisecond();
				Timer.printReportInSecond();
				System.exit(0);
				return false;
			}
			logger.info("Engine Closed Successfully");
			return true;
		} catch (Exception e) {
			logger.error("Engine Fail to close", e);
			return false;
		}
	}

	public Object getConfigValue(String key) {
		return configMap.get(key);
	}

	public boolean EngineisRunning() {
		return !(boolean) configMap.get(DBEnum.SYS_SHUTDOWN);
	}

	public void updateConfigMap(ConcurrentHashMap<String, Object> configMap) {
		synchronized (this.configMap) {
			this.configMap = configMap;
		}
	}

	public ConcurrentHashMap<String, Object> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(ConcurrentHashMap<String, Object> configMap) {
		this.configMap = configMap;
	}
}
