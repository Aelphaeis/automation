package com.cruat.automation.process;

import java.util.Objects;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

public class ProcessNameResolver implements ProcessResolutionStrategy {

	private final String processName;

	public ProcessNameResolver(String name) {
		this.processName = Objects.requireNonNull(name);
	}

	public HWND getHandle() {
		return User32.INSTANCE.FindWindow(null, this.processName);
	}
}
