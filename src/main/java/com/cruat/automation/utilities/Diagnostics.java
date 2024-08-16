package com.cruat.automation.utilities;

import java.awt.image.RenderedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Diagnostics {

	public static final String DATETIME_FORMAT = "uuuu-MM-dd'T'HH-mm-ss-SSS";
	private static final DateTimeFormatter DTF = defaultDateTimeFormatter();
	private static final String BASE_DIR = "logs/";
	private static final String PREFIX = "file://";

	public static String logImage(RenderedImage ri) {
		return logImage(ri, LocalDateTime.now());
	}

	public static String logImage(RenderedImage ri, LocalDateTime ldt) {
		File imgFile = new File(new File(BASE_DIR), DTF.format(ldt) + ".png");
		Images.toFile(ri, imgFile);
		return PREFIX + imgFile.getAbsolutePath();
	}

	private static DateTimeFormatter defaultDateTimeFormatter() {
		return DateTimeFormatter.ofPattern(DATETIME_FORMAT);
	}

	private Diagnostics() {
		throw new UnsupportedOperationException();
	}
}
