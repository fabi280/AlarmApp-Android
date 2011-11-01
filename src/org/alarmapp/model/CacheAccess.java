package org.alarmapp.model;

/**
 * 
 * @author frank
 * 
 */
public enum CacheAccess {
	/**
	 * Do not use the cache
	 */
	None,

	/**
	 * Automatically decice if cache access makes sense
	 */
	Auto,

	/***
	 * Always use the cache. Even if it's empty
	 */
	Force
}
