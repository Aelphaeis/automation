package com.cruat.automation.capture.cv;

import java.awt.image.BufferedImage;
import java.util.function.IntPredicate;

import com.cruat.automation.utilities.Images;

public interface ImagePixelFilter extends IntPredicate {

	public default BufferedImage filter(BufferedImage img) {
		BufferedImage clone = Images.copy(img);
		for (int r = 0; r < clone.getWidth(); r++) {
			for (int c = 0; c < clone.getHeight(); c++) {
				if (!test(clone.getRGB(r, c))) {
					clone.setRGB(r, c, 0);
				}
			}
		}
		return clone;
	}
}
