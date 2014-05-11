/*
 * Copyright (c) 2006 - 2008 Aduna and Deutsches Forschungszentrum fuer Kuenstliche Intelligenz DFKI GmbH.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

/**
 * 
 * A Map like structure where each key maps to a list of values. 
 * I couldn't get the generics to work the way I wanted, so it doesn't inherit anything.
 * TODO: move this to a utils project. 
 * 
 * @author grimnes, sauermann
 *
 */
public class ListMap<K,V> {

	private final HashMap<K, List<V>> data;

	public ListMap() { 
		data=new HashMap<K,List<V>>();
	}
    
    private class MyEntry implements Entry<K,V> {
        K key;
        V value;
        
        public MyEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V arg0) {
            throw new UnsupportedOperationException("not implemented");
        }
        
    }
	
	public V put(K key, V value) {
		List<V> list;
		if (data.containsKey(key)) {
			list=data.get(key);
		} else { 
			list=new ArrayList<V>();
			data.put(key, list);
		}
		list.add(value);
		return null;
	}

    /**
     * Get all values registered for key K.
     * @param key the key
     * @return <code>null</code> or a list
     */
	public List<V>get(K key) {
		return data.get(key);
	}

	public boolean containsKey(K key) {
		return data.containsKey(key);
	}

	public String toString() {
		return data.toString();
	}

	public void remove(K key) {
		data.remove(key);
	}

    /**
     * Returns a Set copy of the mappings contained in this map.
     * @return a copy of the listmap as set of entries
     */
    public Set<Entry<K,V>> entrySet() {
        HashSet<Entry<K,V>> result  = new HashSet<Entry<K,V>>();
        for (Entry<K,List<V>> i : data.entrySet())
        {
            for (V v : i.getValue())
            {
                result.add(new MyEntry(i.getKey(), v));
            }
        }
        return result;
        
    }

	
	
}
