package com.cruat.automation.process;

import java.util.Objects;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

public class Win32ForegroundStrategy implements FocusStrategy {

	private final HWND handle;

	public Win32ForegroundStrategy(String processName) {
		this(new ProcessNameResolver(processName).getHandle());
	}

	public Win32ForegroundStrategy(HWND handle) {
		this.handle = Objects.requireNonNull(handle);
	}

	@Override
	public void execute() {
		User32.INSTANCE.SetForegroundWindow(handle);
	}
}
