package com.asset.engine.aop.util;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.asset.engine.EngineMain;
import com.asset.engine.util.constants.DBEnum;

public class Timer {

	private static final Logger logger = Logger.getLogger(Timer.class);

	public static ConcurrentHashMap<Long, Long> tempMap = new ConcurrentHashMap<>();

	private static long engineStartTime = 0;

	private static long engineCloseTime = 0;

	private static long dBFetchersStartTime = 0;

	private static long dBFetchersCloseTime = 0;

	private static long classifiersStartTime = 0;

	private static long classifiersCloseTime = 0;

	private static long saversStartTime = 0;

	private static long saversCloseTime = 0;

	private static long timeWaitingConection = 0;

	private static long timeWaitingDBForDBFetcher = 0;

	private static long timeWaitingWebInterfaceForClassifiers = 0;

	private static long timeWaitingDBForSavers = 0;

	private static long timeWaitingQueueToEmptyForDBFetchers = 0;

	private static long timeWaitingQueueToGetForClassifiers = 0;

	private static long timeWaitingQueueToEmptyForClassifiers = 0;

	private static long timeWaitingQueueToGetForSavers = 0;

	public static void printReportInSecond() {

		System.out.println("*****************************");
		System.out.println("Time Report In Second");
		long tt = engineCloseTime - engineStartTime;
		tt /= 1000;
		long dt = dBFetchersCloseTime - dBFetchersStartTime;
		dt /= 1000;
		long ct = classifiersCloseTime - classifiersStartTime;
		ct /= 1000;
		long st = saversCloseTime - saversStartTime;
		st /= 1000;

		System.out.println("Actual Total Engine Time is: " + ((tt)) + " second");
		long tn = (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_PRODUCERS) * dt
				+ (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_CONSUMERS) * ct
				+ (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_SAVERS) * st;
		System.out.println("Total Threads Time is: " + ((tn)) + " second");
		System.out.println("Total Time Waiting Conection is: " + (timeWaitingConection / 1000) + " second");

		System.out.println("\nDBFetchers Time, Time for 1 Thread " + dt + ", Total time is "
				+ (dt * ((long) (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_PRODUCERS))) + " second");
		System.out.println("Total Time Waiting DB -DBFetcher- is: " + (timeWaitingDBForDBFetcher / 1000) + " second");
		System.out.println("Total Time Waiting Queue To Put Requests -DBFetchers- is: "
				+ (timeWaitingQueueToEmptyForDBFetchers / 1000) + " second");

		System.out.println("\nClassifiers Time, Time for 1 Thread " + ct + ", Total time is "
				+ (ct * ((long) (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_CONSUMERS))) + " second");
		System.out.println("Total Time Waiting To Get Requests -Classifiers- is: "
				+ (timeWaitingQueueToGetForClassifiers / 1000) + " second");
		System.out.println("Total Time Waiting to call WebInterface -Classifiers- is: "
				+ (timeWaitingWebInterfaceForClassifiers / 1000) + " second");
		System.out.println("Total Time Waiting To Put HRequests -Classifiers- is: "
				+ (timeWaitingQueueToEmptyForClassifiers / 1000) + " second");

		System.out.println("\nSavers Time, Time for 1 Thread " + st + ", Total time is "
				+ (st * ((long) (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_SAVERS))) + " second");
		System.out.println("Total Time Waiting Queue To Get HRequests -Savers- is: "
				+ (timeWaitingQueueToGetForSavers / 1000) + " second");
		System.out.println("Total Time Waiting DB -Savers- is: " + (timeWaitingDBForSavers / 1000) + " second");

		System.out.println("*****************************");
	}

	public static void printReportInMillisecond() {

		System.out.println("*****************************");
		System.out.println("Time Report In Millisecond");
		long tt = engineCloseTime - engineStartTime;
		System.out.println("Actual Total Engine Time is: " + ((tt)) + " millisecond");

		long dt = dBFetchersCloseTime - dBFetchersStartTime;
		long ct = classifiersCloseTime - classifiersStartTime;
		long st = saversCloseTime - saversStartTime;

		long tn = (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_PRODUCERS) * dt
				+ (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_CONSUMERS) * ct
				+ (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_SAVERS) * st;
		System.out.println("Total Threads Time is: " + (tn) + " second");
		System.out.println("Total Time Waiting Conection is: " + (timeWaitingConection) + " millisecond");

		System.out.println("\nDBFetchers Time, Time for 1 Thread " + dt + ", Total time is "
				+ (dt * ((long) (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_PRODUCERS))) + " millisecond");
		System.out.println("Total Time Waiting DB -DBFetcher- is: " + (timeWaitingDBForDBFetcher) + " millisecond");
		System.out.println("Total Time Waiting Queue To Put Requests -DBFetchers- is: "
				+ (timeWaitingQueueToEmptyForDBFetchers) + " millisecond");

		System.out.println("\nClassifiers Time, Time for 1 Thread " + ct + ", Total time is "
				+ (ct * ((long) (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_CONSUMERS))) + " millisecond");
		System.out.println("Total Time Waiting To Get Requests -Classifiers- is: "
				+ (timeWaitingQueueToGetForClassifiers) + " millisecond");
		System.out.println("Total Time Waiting to call WebInterface -Classifiers- is: "
				+ (timeWaitingWebInterfaceForClassifiers) + " millisecond");
		System.out.println("Total Time Waiting To Put HRequests -Classifiers- is: "
				+ (timeWaitingQueueToEmptyForClassifiers) + " millisecond");

		System.out.println("\nSavers Time, Time for 1 Thread " + st + ",Total time is "
				+ (st * ((long) (int) EngineMain.em.getConfigValue(DBEnum.SYS_NUM_OF_SAVERS))) + " millisecond");
		System.out.println("Total Time Waiting Queue To Get HRequests -Savers- is: " + (timeWaitingQueueToGetForSavers)
				+ " millisecond");
		System.out.println("Total Time Waiting DB -Savers- is: " + (timeWaitingDBForSavers) + " millisecond");

		System.out.println("*****************************");
	}

	public static synchronized void setEngineStartTime(long engineStartTim) {
		logger.info("engine start time " + engineStartTim);
		engineStartTime = engineStartTim;
	}

	public static synchronized void setEngineCloseTime(long engineCloseTim) {
		logger.info("engine close time " + engineCloseTim);
		engineCloseTime = engineCloseTim;
	}

	public static synchronized void addToTimeWaitingConection(long diff) {
		logger.info("waiting connection pool " + diff);
		timeWaitingConection += diff;
	}

	public static synchronized void addToTimeWaitingDBForDBFetcher(long diff) {
		logger.info("waiting db to response " + diff);
		timeWaitingDBForDBFetcher += diff;
	}

	public static synchronized void addToTimeWaitingWebInterfaceForClassifiers(long diff) {
		logger.info("waiting webinterface to response " + diff);
		timeWaitingWebInterfaceForClassifiers += diff;
	}

	public static synchronized void addToTimeWaitingDBForSavers(long diff) {
		logger.info("waiting db to response " + diff);
		timeWaitingDBForSavers += diff;
	}

	public static synchronized void addToTimeWaitingQueueToEmptyForDBFetchers(long diff) {
		logger.info("waiting queue - Empty - " + diff);
		timeWaitingQueueToEmptyForDBFetchers += diff;
	}

	public static synchronized void addToTimeWaitingQueueToGetForClassifiers(long diff) {
		logger.info("waiting queue - Full - " + diff);
		timeWaitingQueueToGetForClassifiers += diff;
	}

	public static synchronized void addToTimeWaitingQueueToEmptyForClassifiers(long diff) {
		logger.info("waiting queue - Empty - " + diff);
		timeWaitingQueueToEmptyForClassifiers += diff;
	}

	public static synchronized void addToTimeWaitingQueueToGetForSavers(long diff) {
		logger.info("waiting queue - Full - " + diff);
		timeWaitingQueueToGetForSavers += diff;
	}

	public static void setdBFetchersStartTime(long dBFetchersStartTime) {

		if (Timer.dBFetchersStartTime == 0) {
			Timer.dBFetchersStartTime = dBFetchersStartTime;
			logger.info("start DBFethcer time" + dBFetchersStartTime);
		}
	}

	public static void setdBFetchersCloseTime(long dBFetchersCloseTime) {
		if (Timer.dBFetchersCloseTime == 0) {
			Timer.dBFetchersCloseTime = dBFetchersCloseTime;
			logger.info("close DBFethcer time" + dBFetchersCloseTime);
		}
	}

	public static void setClassifiersStartTime(long classifiersStartTime) {
		if (Timer.classifiersStartTime == 0) {
			Timer.classifiersStartTime = classifiersStartTime;
			logger.info("start Classifier time" + classifiersStartTime);
		}
	}

	public static void setClassifiersCloseTime(long classifiersCloseTime) {
		if (Timer.classifiersCloseTime == 0) {
			Timer.classifiersCloseTime = classifiersCloseTime;
			logger.info("close Classifier time" + classifiersCloseTime);
		}
	}

	public static void setSaversStartTime(long saversStartTime) {
		if (Timer.saversStartTime == 0) {
			Timer.saversStartTime = saversStartTime;
			logger.info("start saver time" + saversStartTime);
		}
	}

	public static void setSaversCloseTime(long saversCloseTime) {
		if (Timer.saversCloseTime == 0) {
			Timer.saversCloseTime = saversCloseTime;
			logger.info("close saver time" + saversCloseTime);
		}
	}

}
