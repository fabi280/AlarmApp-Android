package org.alarmapp.util.store;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.alarmapp.util.Store;

import android.content.Context;

public class PersistentMap<A, B> implements Map<A, B> {

	final Map<A, B> innerMap;
	final Store<Map<A, B>> store;

	private PersistentMap(Map<A, B> innerMap, Store<Map<A, B>> store) {
		super();
		this.innerMap = innerMap;
		this.store = store;
	}

	public static <A, B> PersistentMap<A, B> createNew(Context ctxt,
			String name, Map<A, B> initalValues) {

		if (initalValues == null)
			initalValues = new HashMap<A, B>();

		Store<Map<A, B>> store = new Store<Map<A, B>>(ctxt, name);
		store.write(initalValues);

		return new PersistentMap<A, B>(initalValues, store);
	}

	public static <KEY, VAL> PersistentMap<KEY, VAL> restoreOrCreateEmpty(
			Context ctxt, String name) {
		Store<Map<KEY, VAL>> store = new Store<Map<KEY, VAL>>(ctxt, name);
		Map<KEY, VAL> map = store.read();

		if (map == null)
			map = new HashMap<KEY, VAL>();

		return new PersistentMap<KEY, VAL>(map, store);
	}

	public synchronized void sync() {
		store.write(innerMap);
	}

	public void clear() {
		innerMap.clear();
		sync();
	}

	public boolean containsKey(Object arg0) {
		return innerMap.containsKey(arg0);
	}

	public boolean containsValue(Object arg0) {
		return innerMap.containsValue(arg0);
	}

	public Set<java.util.Map.Entry<A, B>> entrySet() {
		return innerMap.entrySet();
	}

	public B get(Object arg0) {
		return innerMap.get(arg0);
	}

	public boolean isEmpty() {
		return innerMap.isEmpty();
	}

	public Set<A> keySet() {
		return innerMap.keySet();
	}

	public B put(A arg0, B arg1) {
		B result = innerMap.put(arg0, arg1);
		sync();

		return result;
	}

	public void putAll(Map<? extends A, ? extends B> arg0) {
		innerMap.putAll(arg0);
		sync();
	}

	public B remove(Object arg0) {
		B r = innerMap.remove(arg0);
		sync();
		return r;
	}

	public int size() {
		return innerMap.size();
	}

	public Collection<B> values() {
		return innerMap.values();
	}

}
