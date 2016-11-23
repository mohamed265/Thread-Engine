///**
// * 
// */
//package com.asset.engine.aop;
//
//import java.sql.Connection;
//import java.util.ArrayList;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Before;
//
//import com.asset.engine.aop.util.Timer;
//import com.asset.engine.entity.HRequest;
//import com.asset.engine.entity.Request;
//
///**
// * @author mohamed.morsy
// *
// */
//public aspect TimeAspect_copy {
//
//	@Before("execution(* com.asset.engine.EngineMain.start(..))")
//	public void startAspect() {
//		Timer.setEngineStartTime(System.currentTimeMillis());
//	}
//
//	@After("execution(* com.asset.engine.EngineMain.close(..))")
//	public void closeAspect() {
//		Timer.setEngineCloseTime(System.currentTimeMillis());
//		Timer.printReport();
//	}
//
//	@Around("execution(* com.asset.engine.util.DBConnectionManager.getConnectionFromPool(..))")
//	public Connection getConnectionFromPoolAspect(ProceedingJoinPoint joinPoint) throws InterruptedException {
//		Object rValue = null;
//		long b = System.currentTimeMillis();
//		try {
//			rValue = joinPoint.proceed();
//		} catch (InterruptedException e) {
//			throw e;
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		System.out.println(rValue.toString());
//		long a = System.currentTimeMillis();
//		Timer.addToTimeWaitingConection(a - b);
//		Timer.printReport();
//		return (Connection) rValue;
//	}
//
//	@Around("execution(* com.asset.engine.dao.RequestLoaderDao.loadRequestsBatch(..))")
//	public ArrayList<Request> loadRequestsBatchAspect(ProceedingJoinPoint joinPoint) {
//		Object rValue = null;
//		long b = System.currentTimeMillis();
//		try {
//			rValue = joinPoint.proceed();
//		} catch (Throwable e) {
//
//			e.printStackTrace();
//		}
//		long a = System.currentTimeMillis();
//		Timer.addToTimeWaitingDBForDBFetcher(a - b);
//		return (ArrayList<Request>) rValue;
//	}
//
//	@Around("execution(* com.asset.engine.util.WebInterfaceConnector.processSingleRequest(..))")
//	public HRequest processSingleRequestAspect(ProceedingJoinPoint joinPoint) {
//		Object rValue = null;
//		long b = System.currentTimeMillis();
//		try {
//			rValue = joinPoint.proceed();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		long a = System.currentTimeMillis();
//		Timer.addToTimeWaitingWebInterfaceForClassifiers(a - b);
//		return (HRequest) rValue;
//	}
//
//	@Around("execution(* com.asset.engine.util.WebInterfaceConnector.processBatchRequests(..))")
//	public ArrayList<HRequest> processBatchRequestsAspect(ProceedingJoinPoint joinPoint) {
//		Object rValue = null;
//		long b = System.currentTimeMillis();
//		try {
//			rValue = joinPoint.proceed();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		long a = System.currentTimeMillis();
//		Timer.addToTimeWaitingWebInterfaceForClassifiers(a - b);
//		return (ArrayList<HRequest>) rValue;
//	}
//
//	@Around("execution(* com.asset.engine.dao.HRequestDao.saveHRequestsBatch(..))")
//	public boolean saveHRequestsBatchAspect(ProceedingJoinPoint joinPoint) {
//		Object rValue = null;
//		long b = System.currentTimeMillis();
//		try {
//			rValue = joinPoint.proceed();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		long a = System.currentTimeMillis();
//		Timer.addToTimeWaitingDBForSavers(a - b);
//		return (boolean) rValue;
//	}
//
//	@Around("execution(* com.asset.engine.statics.RequestsManager.putRequestsBatch(..))")
//	public void putRequestsBatchAspect(ProceedingJoinPoint joinPoint) throws InterruptedException {
//
//		long b = System.currentTimeMillis();
//		try {
//			joinPoint.proceed();
//		} catch (InterruptedException e) {
//			throw e;
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		long a = System.currentTimeMillis();
//		Timer.addToTimeWaitingQueueToEmptyForDBFetchers(a - b);
//	}
//
//	@Around("execution(* com.asset.engine.statics.RequestsManager.getRequestsBatch(..))")
//	public ArrayList<Request> getRequestsBatchAspect(ProceedingJoinPoint joinPoint) throws InterruptedException {
//		Object rValue = null;
//		long b = System.currentTimeMillis();
//		try {
//			rValue = joinPoint.proceed();
//		} catch (InterruptedException e) {
//			throw e;
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		long a = System.currentTimeMillis();
//		Timer.addToTimeWaitingQueueToGetForClassifiers(a - b);
//		return (ArrayList<Request>) rValue;
//	}
//
//	@Around("execution(* com.asset.engine.statics.HRequestsManager.putHRequestsBatch(..))")
//	public void putHRequestsBatchAspect(ProceedingJoinPoint joinPoint) throws InterruptedException {
//
//		long b = System.currentTimeMillis();
//		try {
//			joinPoint.proceed();
//		} catch (InterruptedException e) {
//			throw e;
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		long a = System.currentTimeMillis();
//		Timer.addToTimeWaitingQueueToEmptyForClassifiers(a - b);
//	}
//
//	@Around("execution(* com.asset.engine.statics.HRequestsManager.getHRequestsBatch(..))")
//	public ArrayList<HRequest> getHRequestsBatchAspect(ProceedingJoinPoint joinPoint) throws InterruptedException {
//		Object rValue = null;
//		long b = System.currentTimeMillis();
//		try {
//			rValue = joinPoint.proceed();
//		} catch (InterruptedException e) {
//			throw e;
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		long a = System.currentTimeMillis();
//		Timer.addToTimeWaitingQueueToGetForSavers(a - b);
//		return (ArrayList<HRequest>) rValue;
//	}
//
//}
///*
// * @Around("execution(* com.asset.engine.EngineMain.print(..))") public void
// * logAround(ProceedingJoinPoint joinPoint) {
// * 
// * System.out.println("logAround() is running!"); System.out.println(
// * "hijacked method : " + joinPoint.getSignature().getName());
// * System.out.println("hijacked arguments : " +
// * Arrays.toString(joinPoint.getArgs()));
// * 
// * System.out.println("Around before is running!"); joinPoint.proceed(); //
// * continue on the intercepted method System.out.println(
// * "Around after is running!");
// * 
// * System.out.println("******");
// * 
// * }
// */