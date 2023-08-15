package com.cruat.automation.input;

public interface MouseInputStrategy {

	default void click(int x, int y) {
		click(x, y, 20);
	}

	void click(int x, int y, int delay);

	void hover(int x, int y);
}
