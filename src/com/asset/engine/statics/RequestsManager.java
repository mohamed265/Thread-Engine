/**
 * 
 */
package com.asset.engine.statics;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.asset.engine.EngineMain;
import com.asset.engine.entity.Request;
import com.asset.engine.util.constants.DBEnum;

/**
 * @author mohamed.morsy
 *
 */
public class RequestsManager {

	final static Logger logger = Logger.getLogger(RequestsManager.class);

	private static int MAX_SIZE;

	private static BlockingQueue<ArrayList<Request>> bq;

	static {
		MAX_SIZE = (int) EngineMain.em.getConfigValue(DBEnum.SYS_MAX_REQUEST_QUEUE);
		bq = new ArrayBlockingQueue<>(MAX_SIZE);
	}

	@SuppressWarnings("downsize may lead to stuck progrem")
	/**
	 * downsize may lead to stuck progrem
	 * 
	 * @param newSize
	 */
	private static void resizeTo(int newSize) {
		BlockingQueue<ArrayList<Request>> bqbq = new ArrayBlockingQueue<ArrayList<Request>>(newSize);
		ArrayList<Request> list;
		while ((list = bq.poll()) != null)
			bqbq.add(list);
		bq = bqbq;
	}

	public static void putRequestsBatch(ArrayList<Request> requests) throws InterruptedException {
		if (requests != null && requests.size() != 0) {
			bq.put(requests);
			// ThreadUtils.notifyClassifiers();
			// ThreadUtils.notifySavers();
		} else
			logger.info("Empty batch");
	}

	public static ArrayList<Request> getRequestsBatch() throws InterruptedException {
		return bq.take();
	}

	public static int getbqsize() {
		return bq.size();
	}
}
// BQLock();
// private static void BQLock() {
// while (lock)
// ;
// }