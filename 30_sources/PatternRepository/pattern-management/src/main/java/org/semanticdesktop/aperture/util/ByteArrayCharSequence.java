package org.semanticdesktop.aperture.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.NullArgumentException;

public class ByteArrayCharSequence implements CharSequence {

	private byte [] buf;
	private int startIdx;
	private int endIdx;
	
	public ByteArrayCharSequence(byte[] bs) {
		this(bs,0,bs.length);
	}
	
	private ByteArrayCharSequence(byte [] bs, int startIdx, int endIdx) {
		if (bs == null) {
			throw new NullArgumentException("bs cannot be null");
		}
		if (startIdx < 0 || startIdx > endIdx || endIdx > bs.length) {
			throw new IllegalArgumentException("The indices should be : 0 <= startIdx <= endIdx < " + bs.length);
		}
		this.buf = bs;	
		this.startIdx = startIdx;
		this.endIdx = endIdx;
	}

	public char charAt(int index) {
		if (index < 0 || index >= endIdx - startIdx) {
			throw new IndexOutOfBoundsException("Index " + index + " not between 0 and " + (endIdx - startIdx));
		}
		return (char)(buf[startIdx + index] & 0xFF);
	}

	public int length() {
		return endIdx - startIdx;
	}

	public CharSequence subSequence(int start, int end) {
		
		if (start < 0 || start > end || end > endIdx - startIdx) {
			throw new IllegalArgumentException("The indices should be : 0 <= start <= end < " + length());
		}
		
		return new ByteArrayCharSequence(buf,startIdx + start,startIdx + end);
	}
	
	public String toString() {
		try {
			return new String(buf,"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e); // this will not happen
		}
	}
}
