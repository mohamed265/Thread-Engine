/**
 * 
 */
package com.asset.engine.statics;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.asset.engine.EngineMain;
import com.asset.engine.entity.HRequest;
import com.asset.engine.util.constants.DBEnum;

/**
 * @author mohamed.morsy
 *
 */
public class HRequestsManager {

	final static Logger logger = Logger.getLogger(HRequestsManager.class);

	private static int MAX_SIZE;

	private static BlockingQueue<ArrayList<HRequest>> bq;

	static {
		MAX_SIZE = (int) EngineMain.em.getConfigValue(DBEnum.SYS_MAX_HREQUEST_QUEUE);
		bq = new ArrayBlockingQueue<>(MAX_SIZE);
	}

	private static void resizeTo(int newSize) {
		BlockingQueue<ArrayList<HRequest>> bqbq = new ArrayBlockingQueue<ArrayList<HRequest>>(newSize);
		ArrayList<HRequest> list;
		while ((list = bq.poll()) != null)
			bqbq.add(list);
		bq = bqbq;
	}

	public static void putHRequestsBatch(ArrayList<HRequest> hRequests) throws InterruptedException {
		if (hRequests != null && hRequests.size() != 0) {
			bq.put(hRequests);
			// ThreadUtils.notifySavers();
		} else
			logger.info("Empty batch");

	}

	public static ArrayList<HRequest> getHRequestsBatch() throws InterruptedException {

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