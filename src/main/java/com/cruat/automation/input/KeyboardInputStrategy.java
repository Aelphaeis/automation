package com.cruat.automation.input;

public interface KeyboardInputStrategy {

	public default void sendKey(int key) {
		sendKey(key, 200);
	}

	public void sendKey(int key, int delay);
}
