package com.cruat.automation.input;

import java.awt.AWTException;
import java.awt.Robot;

import com.cruat.automation.AutomationException;
import com.cruat.automation.utilities.Threads;

public class RobotKeyboardInputStrategy implements KeyboardInputStrategy {

	private final Robot robot;

	public RobotKeyboardInputStrategy() {
		this.robot = createRobot();
	}

	@Override
	public void sendKey(int key, int delay) {
		this.robot.keyPress(key);
		Threads.pause(delay);
		this.robot.keyRelease(key);
	}

	private static Robot createRobot() {
		try {
			return new Robot();
		}
		catch (AWTException e) {
			throw new AutomationException(e);
		}
	}
}
