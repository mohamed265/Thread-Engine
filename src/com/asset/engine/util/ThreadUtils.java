/**
 * 
 */
package com.asset.engine.util;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.asset.engine.EngineMain;
import com.asset.engine.statics.ProducerIndexManager;
import com.asset.engine.statics.RunningThreads;
import com.asset.engine.threads.Classifier;
import com.asset.engine.threads.DBFetcher;
import com.asset.engine.threads.DBListener;
import com.asset.engine.threads.MasterThread;
import com.asset.engine.threads.Saver;
import com.asset.engine.util.constants.DBEnum;

/**
 * @author mohamed.morsy
 *
 */
public class ThreadUtils {

	final static Logger logger = Logger.getLogger(ThreadUtils.class);

	private static int numOfDBFetchers;

	private static int numOfClassifiers;

	private static int numOfSavers;

	public static void initThreads() throws SQLException {

		numOfSavers = (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_SAVERS);

		int diff = numOfSavers - RunningThreads.savers.size();
		while (diff > 0) {
			new Saver().start();
			diff--;
		}

		for (int i = 0; i < Math.abs(diff); i++) {
			RunningThreads.savers.get(i).stopThread();
		}

		numOfClassifiers = (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_CONSUMERS);

		String url = (String) EngineMain.em.getConfigValue(DBEnum.SYS_URL);
		boolean type = (boolean) EngineMain.em.getConfigValue(DBEnum.SYS_IS_SINGLE_CALLING_WEB_INTERFACE);

		diff = numOfClassifiers - RunningThreads.classifiers.size();
		while (diff > 0) {
			new Classifier(url, type).start();
			diff--;
		}

		for (int i = 0; i < Math.abs(diff); i++) {
			RunningThreads.classifiers.get(i).stopThread();
		}

		numOfDBFetchers = (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_PRODUCERS);

		ProducerIndexManager.update(numOfDBFetchers);

		diff = numOfDBFetchers - RunningThreads.dbFetchers.size();
		while (diff > 0) {
			new DBFetcher().start();
			diff--;
		}

		for (int i = 0; i < Math.abs(diff); i++) {
			RunningThreads.dbFetchers.get(i).stopThread();
		}
	}

	public static void updateConsumersUrl(String url) {

		for (Classifier consumer : RunningThreads.classifiers) {
			consumer.setUrl(url);
		}
		logger.info("classifiers Updated");
	}

	public static void updateConsumersWebCallingMechanizm(boolean type) {
		for (Classifier consumer : RunningThreads.classifiers) {
			consumer.setSingleProcessing(type);
		}
		logger.info("classifiers Updated");
	}

	public static void stopAllThreads() {

		DBListener.getInstance().stopThread();
		MasterThread.getInstance().stopThread();

		for (Classifier consumer : RunningThreads.classifiers) {
			consumer.stopThread();
		}

		for (DBFetcher producer : RunningThreads.dbFetchers) {
			producer.stopThread();
		}

		for (Saver saver : RunningThreads.savers) {
			saver.stopThread();
		}

		logger.info("All Threads Stopped");
	}

	public static void stopAllDBFetchers() {
		for (DBFetcher producer : RunningThreads.dbFetchers) {
			producer.stopThread();
		}
		logger.info("All DBFetchers Stopped");
	}

	public static void stopAllClassifiers() {
		for (Classifier classifier : RunningThreads.classifiers) {
			classifier.stopThread();
		}
		logger.info("All Classifier Stopped");
	}

	public static void stopAllSavers() {
		for (Saver saver : RunningThreads.savers) {
			saver.stopThread();
		}
		logger.info("All Savers Stopped");
	}

	public static void waitMe() {
		try {
			synchronized (Thread.currentThread()) {
				logger.info(Thread.currentThread().getName() + " waiting");
				Thread.currentThread().wait();
			}
		} catch (InterruptedException e) {
			logger.error(Thread.currentThread().getName() + "Fail to wait", e);
		}
	}

	public synchronized static void registerMe(Classifier consumer) {
		RunningThreads.classifiers.add(consumer);
	}

	public synchronized static void registerMe(DBFetcher producer) {
		RunningThreads.dbFetchers.add(producer);
	}

	public synchronized static void registerMe(Saver saver) {
		RunningThreads.savers.add(saver);
	}

	public synchronized static void removeMe(Classifier consumer) {
		RunningThreads.classifiers.remove(consumer);
	}

	public synchronized static void removeMe(DBFetcher producer) {
		RunningThreads.dbFetchers.remove(producer);
	}

	public synchronized static void removeMe(Saver saver) {
		RunningThreads.savers.remove(saver);
	}

	public static int getNumOfClassifiers() {
		return RunningThreads.classifiers.size();
	}

	public static int getNumOfDBFetchers() {
		return RunningThreads.dbFetchers.size();
	}

	public static int getNumOfSavers() {
		return RunningThreads.savers.size();
	}

	public static void notifyDBFetchers() {
		for (DBFetcher producer : RunningThreads.dbFetchers) {
			synchronized (producer) {
				producer.notify();
			}
		}
		logger.info("dbFetchers notified");
	}

	public static void notifyClassifiers() {
		for (Classifier consumer : RunningThreads.classifiers) {
			synchronized (consumer) {
				consumer.notify();
			}
		}
		logger.info("classifiers notified");
	}

	public static void notifySavers() {
		for (Saver saver : RunningThreads.savers) {
			synchronized (saver) {
				saver.notify();
			}
		}
		logger.info("savers notified");
	}

	public static void notifyMasterThread() {
		synchronized (MasterThread.getInstance()) {
			MasterThread.getInstance().notify();
		}
		logger.info("MasterThread notified");
	}

	public static void notifyListener() {
		synchronized (DBListener.getInstance()) {
			DBListener.getInstance().notify();
		}
		logger.info("Listener notified");
	}

	public static void notifyAllThreads() {

		for (DBFetcher producer : RunningThreads.dbFetchers) {
			synchronized (producer) {
				producer.notify();
			}
		}

		for (Classifier consumer : RunningThreads.classifiers) {
			synchronized (consumer) {
				consumer.notify();
			}
		}

		for (Saver saver : RunningThreads.savers) {
			synchronized (saver) {
				saver.notify();
			}
		}
		logger.info("all threads notified");
	}

	public static boolean isAllClassifiersIdeal() {
		boolean flag = true;
		for (Classifier consumer : RunningThreads.classifiers) {
			flag &= consumer.isIdeal();
		}
		return flag;
	}

	public static boolean isAllSaversIdeal() {
		boolean flag = true;
		for (Saver saver : RunningThreads.savers) {
			flag &= saver.isIdeal();
		}
		return flag;
	}

	public static void interruptClassifiers() {
		try {
			for (Classifier consumer : RunningThreads.classifiers) {
				try {
					consumer.interrupt();
				} catch (Exception e) {

				}
			}
		} catch (Exception w) {

		}
	}

	public static void interruptSavers() {
		try {
			for (Saver saver : RunningThreads.savers) {
				try {
					saver.interrupt();
				} catch (Exception e) {

				}
			}
		} catch (Exception s) {

		}
	}

	public static void interruptDBFetchers() {
		try {
			for (DBFetcher dbFetcher : RunningThreads.dbFetchers) {
				try {
					dbFetcher.interrupt();
				} catch (Exception e) {

				}
			}
		} catch (Exception s) {

		}
	}

}
