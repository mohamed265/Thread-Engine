/**
 * 
 */
package com.asset.engine.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;

import com.asset.engine.aop.util.Timer;

/**
 * @author mohamed.morsy
 *
 */
public aspect TimeAspect {

	@Before("execution(* com.asset.engine.EngineMain.start(..))")
	public void startAspect() {
		Timer.setEngineStartTime(System.currentTimeMillis());
	}

	@After("execution(* com.asset.engine.EngineMain.close(..))")
	public void closeAspect() {
		Timer.setEngineCloseTime(System.currentTimeMillis());
		Timer.printReportInSecond();
		Timer.printReportInMillisecond();
	}

	@Before("execution(* com.asset.engine.util.DBConnectionManager.getConnectionFromPool(..))")
	public void BeforeGetConnectionFromPool(ProceedingJoinPoint joinPoint) {
		Timer.tempMap.put(Thread.currentThread().getId(), System.currentTimeMillis());
	}

	@After("execution(* com.asset.engine.util.DBConnectionManager.getConnectionFromPool(..))")
	public void AfterGetConnectionFromPool(ProceedingJoinPoint joinPoint) {
		Timer.addToTimeWaitingConection(System.currentTimeMillis() - Timer.tempMap.get(Thread.currentThread().getId()));
	}

	@Before("execution(* com.asset.engine.dao.RequestLoaderDao.loadRequestsBatch(..))")
	public void BeforeLoadRequestsBatch() {
		Timer.tempMap.put(Thread.currentThread().getId(), System.currentTimeMillis());
	}

	@After("execution(* com.asset.engine.dao.RequestLoaderDao.loadRequestsBatch(..))")
	public void afterLoadRequestsBatch() {
		Timer.addToTimeWaitingDBForDBFetcher(
				System.currentTimeMillis() - Timer.tempMap.get(Thread.currentThread().getId()));
	}

	@Before("execution(* com.asset.engine.util.WebInterfaceConnector.processSingleRequest(..))")
	public void beforeProcessSingleRequest() {
		Timer.tempMap.put(Thread.currentThread().getId(), System.currentTimeMillis());
	}

	@After("execution(* com.asset.engine.util.WebInterfaceConnector.processSingleRequest(..))")
	public void afterProcessSingleRequest() {
		Timer.addToTimeWaitingWebInterfaceForClassifiers(
				System.currentTimeMillis() - Timer.tempMap.get(Thread.currentThread().getId()));
	}

	@Before("execution(* com.asset.engine.util.WebInterfaceConnector.processBatchRequests(..))")
	public void beforeProcessBatchRequests() {
		Timer.tempMap.put(Thread.currentThread().getId(), System.currentTimeMillis());
	}

	@After("execution(* com.asset.engine.util.WebInterfaceConnector.processBatchRequests(..))")
	public void afterProcessBatchRequests() {
		Timer.addToTimeWaitingWebInterfaceForClassifiers(
				System.currentTimeMillis() - Timer.tempMap.get(Thread.currentThread().getId()));
	}

	@Before("execution(* com.asset.engine.dao.HRequestDao.saveHRequestsBatch(..))")
	public void beforeSaveHRequestsBatch(ProceedingJoinPoint joinPoint) {
		Timer.tempMap.put(Thread.currentThread().getId(), System.currentTimeMillis());
	}

	@After("execution(* com.asset.engine.dao.HRequestDao.saveHRequestsBatch(..))")
	public void afterSaveHRequestsBatch() {
		Timer.addToTimeWaitingDBForSavers(
				System.currentTimeMillis() - Timer.tempMap.get(Thread.currentThread().getId()));
	}

	@Before("execution(* com.asset.engine.statics.RequestsManager.putRequestsBatch(..))")
	public void beforePutRequestsBatct() {
		Timer.tempMap.put(Thread.currentThread().getId(), System.currentTimeMillis());
	}

	@After("execution(* com.asset.engine.statics.RequestsManager.putRequestsBatch(..))")
	public void afterPutRequestsBatch() {
		Timer.addToTimeWaitingQueueToEmptyForDBFetchers(
				System.currentTimeMillis() - Timer.tempMap.get(Thread.currentThread().getId()));
	}

	@Before("execution(* com.asset.engine.statics.RequestsManager.getRequestsBatch(..))")
	public void beforeGetRequestsBatch() {
		Timer.tempMap.put(Thread.currentThread().getId(), System.currentTimeMillis());
	}

	@After("execution(* com.asset.engine.statics.RequestsManager.getRequestsBatch(..))")
	public void afterGetRequestsBatch() {
		Timer.addToTimeWaitingQueueToGetForClassifiers(
				System.currentTimeMillis() - Timer.tempMap.get(Thread.currentThread().getId()));
	}

	@Before("execution(* com.asset.engine.statics.HRequestsManager.putHRequestsBatch(..))")
	public void beforePutHRequestsBatch() {
		Timer.tempMap.put(Thread.currentThread().getId(), System.currentTimeMillis());
	}

	@After("execution(* com.asset.engine.statics.HRequestsManager.putHRequestsBatch(..))")
	public void afterPutHRequestsBatch() {
		Timer.addToTimeWaitingQueueToEmptyForClassifiers(
				System.currentTimeMillis() - Timer.tempMap.get(Thread.currentThread().getId()));
	}

	@Before("execution(* com.asset.engine.statics.HRequestsManager.getHRequestsBatch(..))")
	public void beforeGetHRequestsBatch() {
		Timer.tempMap.put(Thread.currentThread().getId(), System.currentTimeMillis());
	}

	@After("execution(* com.asset.engine.statics.HRequestsManager.getHRequestsBatch(..))")
	public void afterGetHRequestsBatch() {
		Timer.addToTimeWaitingQueueToGetForSavers(
				System.currentTimeMillis() - Timer.tempMap.get(Thread.currentThread().getId()));
	}

	@Before("execution(* com.asset.engine.threads.DBFetcher.run(..))")
	public void beforeRunDBFetcher() {
		Timer.setdBFetchersStartTime(System.currentTimeMillis());
	}

	@Before("execution(* com.asset.engine.threads.Classifier.run(..))")
	public void beforeRunClassifier() {
		Timer.setClassifiersStartTime(System.currentTimeMillis());
	}

	@Before("execution(* com.asset.engine.threads.Saver.run(..))")
	public void beforeRunSaver() {
		Timer.setSaversStartTime(System.currentTimeMillis());
	}

	@After("execution(* com.asset.engine.threads.DBFetcher.run(..))")
	public void afterRunDBFetcher() {
		Timer.setdBFetchersCloseTime(System.currentTimeMillis());
	}

	@After("execution(* com.asset.engine.threads.Classifier.run(..))")
	public void afterRunClassifier() {
		Timer.setClassifiersCloseTime(System.currentTimeMillis());
	}

	@After("execution(* com.asset.engine.threads.Saver.run(..))")
	public void afterRunSaver() {
		Timer.setSaversCloseTime(System.currentTimeMillis());
	}

}
/*
 * @Around("execution(* com.asset.engine.EngineMain.print(..))") public void
 * logAround(ProceedingJoinPoint joinPoint) {
 * 
 * System.out.println("logAround() is running!"); System.out.println(
 * "hijacked method : " + joinPoint.getSignature().getId()); System.out.println(
 * "hijacked arguments : " + Arrays.toString(joinPoint.getArgs()));
 * 
 * System.out.println("Around before is running!"); joinPoint.proceed(); //
 * continue on the intercepted method System.out.println(
 * "Around after is running!");
 * 
 * System.out.println("******");
 * 
 * }
 */