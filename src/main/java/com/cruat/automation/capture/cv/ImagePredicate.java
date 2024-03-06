package com.cruat.automation.capture.cv;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Predicate;

public interface ImagePredicate extends Predicate<BufferedImage> {

	default Future<Boolean> toFuture(BufferedImage image) {
		return CompletableFuture.supplyAsync(() -> this.test(image));
	}
}
