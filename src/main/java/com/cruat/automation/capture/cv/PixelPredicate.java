package com.cruat.automation.capture.cv;

import java.awt.Color;
import java.util.function.Predicate;

public interface PixelPredicate extends Predicate<Integer> {

	default boolean test(Color color) {
		return test(color.getRGB());
	}
}
