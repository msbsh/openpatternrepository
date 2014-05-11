/*
 * Copyright (c) 2005 - 2008 Aduna.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A simple Map implementation, taking roughly the same strategy as ArrayList, resulting in a memory-efficient
 * and (for small amounts of key-value pairs) fast implementation.
 * 
 * <p>
 * Please note that methods like containsKey, get, etc. test on object identity (i.e. key == keys[i]), rather
 * than using Object.equals.
 */
public class ArrayMap implements Map {

	private Object[] keys;

	private Object[] values;

	private int size;

	private int modCount;

	public ArrayMap() {
		this(0);
	}

	public ArrayMap(int initialCapacity) {
		keys = new Object[initialCapacity];
		values = new Object[initialCapacity];
		size = 0;
		modCount = 0;
	}

	public ArrayMap(Map map) {
		Map.Entry entry;

		size = map.size();
		keys = new Object[size];
		values = new Object[size];

		Iterator entries = map.entrySet().iterator();

		for (int i = 0; i < size; i++) {
			entry = (Map.Entry) entries.next();
			keys[i] = entry.getKey();
			values[i] = entry.getValue();
		}

		modCount = 0;
	}

	public void clear() {
		keys = new Object[0];
		values = new Object[0];
		size = 0;
		modCount++;
	}

	public boolean containsKey(Object key) {
		return indexOf(key, keys, size) != -1;
	}

	public boolean containsValue(Object value) {
		return indexOf(value, values, size) != -1;
	}

	public Set entrySet() {
		return new AbstractSet() {

			public Iterator iterator() {
				return new EntryIterator();
			}

			public int size() {
				return size;
			}

			// the following three methods are strictly speaking
			// not necessary, since AbstractCollection already
			// provides them, but are added for better performance

			public boolean contains(Object object) {
				if (object instanceof Map.Entry) {
					Map.Entry entry = (Map.Entry) object;
					int pos = indexOf(entry.getKey(), keys, size);
					return pos != -1 && values[pos] == entry.getValue();
				}
				else {
					return false;
				}
			}

			public boolean remove(Object object) {
				if (object instanceof Map.Entry) {
					Map.Entry entry = (Map.Entry) object;
					int pos = indexOf(entry.getKey(), keys, size);
					if (pos != -1 && values[pos] == entry.getValue()) {
						ArrayMap.this.remove(pos);
						return true;
					}
				}

				return false;
			}

			public void clear() {
				ArrayMap.this.clear();
			}
		};
	}

	public boolean equals(Object object) {
		if (object instanceof Map) {
			Set otherEntries = ((Map) object).entrySet();
			return entrySet().equals(otherEntries);
		}
		else {
			return false;
		}
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append('{');

		Iterator i = entrySet().iterator();
		boolean hasNext = i.hasNext();

		while (hasNext) {
			Entry e = (Entry) i.next();

			Object key = e.getKey();
			if (key == this) {
				buffer.append("(this Map)");
			}
			else {
				buffer.append(key);
			}

			buffer.append('=');

			Object value = e.getValue();
			if (value == this) {
				buffer.append("(this Map)");
			}
			else {
				buffer.append(value);
			}

			hasNext = i.hasNext();
			if (hasNext) {
				buffer.append(", ");
			}
		}

		buffer.append('}');
		return buffer.toString();
	}
	
	public Object get(Object key) {
		for (int i = 0; i < size; i++) {
			if (keys[i] == key) {
				return values[i];
			}
		}

		return null;
	}

	public int hashCode() {
		Object key, value;
		int keyCode, valueCode;
		int hashCode = 0;

		for (int i = 0; i < size; i++) {
			key = keys[i];
			value = values[i];

			keyCode = key == null ? 0 : key.hashCode();
			valueCode = value == null ? 0 : value.hashCode();

			hashCode += Math.pow(keyCode, valueCode);
		}

		return hashCode;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public Set keySet() {
		return new AbstractSet() {

			public Iterator iterator() {
				return new KeyIterator();
			}

			public int size() {
				return size;
			}

			// the following three methods are strictly speaking
			// not necessary, since AbstractCollection already
			// provides them, but are added for better performance

			public boolean contains(Object object) {
				return containsKey(object);
			}

			public boolean remove(Object object) {
				// done this way to allow null keys!
				int oldSize = size;
				ArrayMap.this.remove(object);
				return size != oldSize;
			}

			public void clear() {
				ArrayMap.this.clear();
			}
		};
	}

	public Object put(Object key, Object value) {
		Object result = null;

		int index = indexOf(key, keys, size);
		if (index == -1) {
			if (keys.length == size) {
				keys = toLargerArray(keys);
				values = toLargerArray(values);
			}

			keys[size] = key;
			values[size] = value;
			size++;
		}
		else {
			result = values[index];
			values[index] = value;
		}

		modCount++;
		return result;
	}

	public void putAll(Map map) {
		Map.Entry entry;

		Iterator entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			put(entry.getKey(), entry.getValue());
		}
	}

	public Object remove(Object key) {
		int index = indexOf(key, keys, size);
		if (index == -1) {
			return null;
		}

		Object oldValue = values[index];
		remove(index);

		return oldValue;
	}

	public int size() {
		return size;
	}

	public Collection values() {
		return new AbstractCollection() {

			public Iterator iterator() {
				return new ValueIterator();
			}

			public int size() {
				return size;
			}

			// the following two methods are strictly speaking
			// not necessary, since AbstractCollection already
			// provides them, but are added for better performance

			public boolean contains(Object object) {
				for (int i = 0; i < size; i++) {
					if (values[i] == object) {
						return true;
					}
				}
				return false;
			}

			public boolean remove(Object object) {
				int index = indexOf(object, values, size);
				if (index == -1) {
					return false;
				}
				else {
					ArrayMap.this.remove(index);
					return true;
				}
			}

			public void clear() {
				ArrayMap.this.clear();
			}
		};
	}

	public Object getKey(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("invalid index: " + index + ", size=" + size);
		}
		return keys[index];
	}

	public Object getValue(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("invalid index: " + index + ", size=" + size);
		}
		return values[index];
	}

	private Object[] toLargerArray(Object[] array) {
		int oldSize = array.length;
		Object[] result = new Object[oldSize + 1];
		System.arraycopy(array, 0, result, 0, oldSize);
		return result;
	}

	private int indexOf(Object object, Object[] array, int length) {
		for (int i = 0; i < length; i++) {
			if (array[i] == object) {
				return i;
			}
		}

		return -1;
	}

	private void remove(int index) {
		// move all elements one position to the left, and set the last to null
		int nextIndex = index + 1;
		int remainingLength = size - nextIndex;

		System.arraycopy(keys, nextIndex, keys, index, remainingLength);
		System.arraycopy(values, nextIndex, values, index, remainingLength);

		size--;
		keys[size] = null;
		values[size] = null;

		modCount++;
	}

	public class Entry implements Map.Entry {

		private int index;

		public Entry(int index) {
			this.index = index;
		}

		public Object getKey() {
			return keys[index];
		}

		public Object getValue() {
			return values[index];
		}

		public Object setValue(Object object) {
			Object oldValue = values[index];
			values[index] = object;
			return oldValue;
		}

		public boolean equals(Object object) {
			if (object instanceof Map.Entry) {
				Map.Entry entry = (Map.Entry) object;
				boolean keyOK = keys[index] == null ? entry.getKey() == null : keys[index] == entry.getKey();
				boolean valueOK = values[index] == null ? entry.getValue() == null : values[index] == entry
						.getValue();
				return keyOK && valueOK;
			}
			else {
				return false;
			}
		}

		public int hashCode() {
			Object key = keys[index];
			Object value = values[index];

			int keyCode = key == null ? 0 : key.hashCode();
			int valueCode = value == null ? 0 : value.hashCode();

			return (int) Math.pow(keyCode, valueCode);
		}
	}

	private class EntryIterator implements Iterator {

		private int index;

		private int expectedModCount;

		public EntryIterator() {
			index = 0;
			expectedModCount = modCount;
		}

		public boolean hasNext() {
			return index < size;
		}

		public Object next() {
			if (index >= size) {
				throw new NoSuchElementException();
			}

			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}

			Entry result = new Entry(index);
			index++;
			return result;
		}

		public void remove() {
			if (index == 0) {
				// 'next' has not been invoked yet
				throw new IllegalStateException();
			}

			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}

			ArrayMap.this.remove(index - 1);
			expectedModCount++;
		}
	}

	private class ArrayIterator implements Iterator {

		// fixme: class implementation looks very similar to
		// EntryIterator: share some code?

		private Object[] array;

		private int length;

		private int index;

		private int expectedModCount;

		public ArrayIterator(Object[] array, int length) {
			this.array = array;
			this.length = length;
			this.index = 0;
			this.expectedModCount = modCount;
		}

		public boolean hasNext() {
			return index < length;
		}

		public Object next() {
			if (index >= length) {
				throw new NoSuchElementException();
			}

			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}

			Object result = array[index];
			index++;
			return result;
		}

		public void remove() {
			if (index == 0) {
				// 'next' has not been invoked yet
				throw new IllegalStateException();
			}

			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}

			ArrayMap.this.remove(index - 1);
			length--;
			expectedModCount++;
		}
	}

	private class KeyIterator extends ArrayIterator {

		public KeyIterator() {
			super(keys, size);
		}
	}

	private class ValueIterator extends ArrayIterator {

		public ValueIterator() {
			super(values, size);
		}
	}
}
