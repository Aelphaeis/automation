package com.cruat.automation.utilities;

import java.awt.image.RenderedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Diagnostics {

	private static final String DT_FORMAT = "uuuu-MM-dd'T'HH-mm-ss-SSS";
	private static final DateTimeFormatter DTF = DateTimeFormatter
			.ofPattern(DT_FORMAT);

	public static String logImage(RenderedImage image) {
		File file = new File("src/main/resources/logs/");
		String name = DTF.format(LocalDateTime.now());
		File imgFile = new File(file, name + ".png");
		Images.toFile(image, imgFile);
		return imgFile.getAbsolutePath();
	}

	private Diagnostics() {
		throw new UnsupportedOperationException();
	}
}
