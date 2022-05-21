package com.cruat.automation.input;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cruat.automation.AutomationException;
import com.cruat.automation.process.ProcessNameResolver;
import com.cruat.automation.process.Win32ForegroundStrategy;
import com.cruat.automation.utilities.Threads;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;

public class RobotMouseInputStrategy implements MouseInputStrategy {

	private static final Logger logger = LogManager.getLogger();
	private final String processName;
	private final Robot robot;

	public RobotMouseInputStrategy(String processName) {
		this.processName = Objects.requireNonNull(processName);
		this.robot = createRobot();
	}

	@Override
	public void click(int x, int y) {
		HWND handle = new ProcessNameResolver(processName).getHandle();

		RECT winRect = new RECT();
		User32.INSTANCE.GetWindowRect(handle, winRect);
		Rectangle region = winRect.toRectangle();

		logger.traceEntry(null, x, y);

		new Win32ForegroundStrategy(handle).execute();
		hover(region.x + x, region.y + y);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		Threads.pause(20);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	@Override
	public void hover(int x, int y) {
		robot.mouseMove(x, y);
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
