package com.cruat.automation.utilities;

public class Threads {
	
	public static void sleep(long milliseconds) {
		pause(milliseconds);
	}

	public static void pause(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private Threads() {
		throw new UnsupportedOperationException("utility class");
	}
}
