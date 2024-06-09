package com.github.linyuzai.plugin.jar.extension.file;

/**
 * Utilities for dealing with bytes from ZIP files.
 */
public class Bytes {

	private Bytes() {
	}

	public static long littleEndianValue(byte[] bytes, int offset, int length) {
		long value = 0;
		for (int i = length - 1; i >= 0; i--) {
			value = ((value << 8) | (bytes[offset + i] & 0xFF));
		}
		return value;
	}

}
