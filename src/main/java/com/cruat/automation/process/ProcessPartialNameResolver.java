package com.cruat.automation.process;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.cruat.automation.AutomationException;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;

public class ProcessPartialNameResolver implements ProcessResolutionStrategy {

	private final String partialName;

	public ProcessPartialNameResolver(String partialName) {
		this.partialName = Objects.requireNonNull(partialName);
	}

	@Override
	public HWND getHandle() {
		ResolverCallback callback = new ResolverCallback();
		User32.INSTANCE.EnumWindows(callback, null);
		Map<String, HWND> filtered = callback.getHandleMap()
				.entrySet()
				.stream()
				.filter(p -> p.getKey().contains(partialName))
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

		if (filtered.isEmpty()) {
			return null;
		}

		if (filtered.size() > 1) {

			String processes = filtered.keySet()
					.stream()
					.collect(Collectors.joining(", ", "[", "]"));

			StringBuilder builder = new StringBuilder();
			builder.append("Found multiple handles for specified name [");
			builder.append(this.partialName);
			builder.append("]: ");
			builder.append(processes);

			throw new DuplicateHandleException(builder.toString());

		}

		return filtered.values().iterator().next();
	}

	public class DuplicateHandleException extends AutomationException {

		private static final long serialVersionUID = 1L;

		public DuplicateHandleException(String message) {
			super(message);
		}
	}

	private class ResolverCallback implements WNDENUMPROC {

		private final Map<String, HWND> handleMap;

		public ResolverCallback() {
			this.handleMap = new HashMap<>();
		}

		@Override
		public boolean callback(HWND hWnd, Pointer data) {
			String windowTitle = getWindowText(hWnd);
			handleMap.put(windowTitle, hWnd);
			return true;
		}

		private String getWindowText(HWND hWnd) {
			char[] windowTextChars = new char[512];
			User32.INSTANCE.GetWindowText(hWnd, windowTextChars, 512);
			return new String(windowTextChars).trim();
		}

		public Map<String, HWND> getHandleMap() {
			return Collections.unmodifiableMap(this.handleMap);
		}
	}
}
