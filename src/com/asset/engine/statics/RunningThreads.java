/**
 * 
 */
package com.asset.engine.statics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.asset.engine.threads.Classifier;
import com.asset.engine.threads.DBFetcher;
import com.asset.engine.threads.Saver;

/**
 * @author mohamed.morsy
 *
 */
public class RunningThreads {

	public static List<DBFetcher> dbFetchers;

	public static List<Classifier> classifiers;

	public static List<Saver> savers;

	static {
		classifiers = Collections.synchronizedList(new ArrayList<Classifier>());
		dbFetchers = Collections.synchronizedList(new ArrayList<DBFetcher>());
		savers = Collections.synchronizedList(new ArrayList<Saver>());
	}
}
