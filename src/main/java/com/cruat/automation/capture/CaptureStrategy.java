package com.cruat.automation.capture;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public interface CaptureStrategy {

	public BufferedImage execute(Rectangle rectangle);
}
