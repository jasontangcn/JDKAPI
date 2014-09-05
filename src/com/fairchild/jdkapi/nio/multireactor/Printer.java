/*
 * Created on 2005-9-21
 *
 */
package com.fairchild.jdkapi.nio.multireactor;

/**
 * @author TomHornson@hotmail.com
 *
 */
public class Printer {
	private static final boolean debug = true;

	public static void debug(Object info) {
		if (debug)
			System.out.println(info);
	}

	/**
	 * for private types.
	 */
	public static void debug(boolean info) {
		debug(Boolean.toString(info));
	}

	public static void debug(byte info) {
		debug(Byte.toString(info));
	}

	public static void debug(char info) {
		debug(info);
	}

	public static void debug(short info) {
		debug(Short.toString(info));
	}

	public static void debug(int info) {
		debug(Integer.toString(info));
	}

	public static void debug(long info) {
		debug(Long.toString(info));
	}

	public static void debug(float info) {
		debug(Float.toString(info));
	}

	public static void debug(double info) {
		debug(Double.toString(info));
	}
	/*
	 * TODO: for private type array.
	 */

}
