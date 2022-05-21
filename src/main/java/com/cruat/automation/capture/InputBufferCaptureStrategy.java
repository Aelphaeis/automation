package com.cruat.automation.capture;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cruat.automation.process.ProcessNameResolver;
import com.sun.jna.Memory;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;

public class InputBufferCaptureStrategy implements CaptureStrategy {

	private static final Logger logger = LogManager.getLogger();
	private final HWND handle;

	public InputBufferCaptureStrategy(String processName) {
		this(new ProcessNameResolver(processName).getHandle());
	}

	public InputBufferCaptureStrategy(HWND handle) {
		this.handle = Objects.requireNonNull(handle);
	}

	@Override
	public BufferedImage execute(Rectangle region) {
		logger.info("Capturing Region: {}", region);

		int x = region.x;
		int y = region.y;
		int w = region.width;
		int h = region.height;

		// graphics device interface
		GDI32 gdi = GDI32.INSTANCE;

		HDC win = User32.INSTANCE.GetDC(handle);
		HDC hdcMemDC = gdi.CreateCompatibleDC(win);
		HBITMAP bMap = gdi.CreateCompatibleBitmap(win, w, h);
		HANDLE hOld = gdi.SelectObject(hdcMemDC, bMap);

		gdi.BitBlt(hdcMemDC, 0, 0, w, h, win, x, y, GDI32.SRCCOPY);
		gdi.SelectObject(hdcMemDC, hOld);
		gdi.DeleteDC(hdcMemDC);

		BITMAPINFO bmi = new BITMAPINFO();
		bmi.bmiHeader.biWidth = region.width;
		bmi.bmiHeader.biHeight = -region.height;
		bmi.bmiHeader.biPlanes = 1;
		bmi.bmiHeader.biBitCount = 32;
		bmi.bmiHeader.biCompression = WinGDI.BI_RGB;

		Memory buffer = new Memory(w * h * 4L);

		gdi.GetDIBits(win, bMap, 0, h, buffer, bmi, WinGDI.DIB_RGB_COLORS);
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		int[] intArr = buffer.getIntArray(0, w * h);
		img.setRGB(0, 0, w, h, intArr, 0, w);

		gdi.DeleteObject(bMap);
		User32.INSTANCE.ReleaseDC(handle, win);

		return img;
	}

	public Rectangle getRegion() {
		RECT winRect = new RECT();
		User32.INSTANCE.GetWindowRect(handle, winRect);
		Rectangle region = new Rectangle();

		// Normalize this for when the game is not on main monitor
		region.height = winRect.bottom - winRect.top;
		region.width = winRect.right - winRect.left;
		region.x = 0;
		region.y = 0;

		return region;
	}
}
