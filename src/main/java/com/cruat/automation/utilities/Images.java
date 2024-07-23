package com.cruat.automation.utilities;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import com.cruat.automation.AutomationException;

public class Images {

	public static final String PREFIX = "tempMatch";
	public static final String SUFFIX = ".png";
	public static final String PNG = "png";

	public static void show(BufferedImage image) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	public static BufferedImage grayScale(BufferedImage source) {
		BufferedImage destination = copy(source);
		for (int y = 0; y < source.getHeight(); y++) {
			for (int x = 0; x < source.getWidth(); x++) {
				int rbg = source.getRGB(x, y);

				byte alpha = (byte) ((rbg >> 24) & 0xFF);
				byte red = (byte) ((rbg >> 16) & 0xFF);
				byte green = (byte) ((rbg >> 8) & 0xFF);
				byte blue = (byte) ((rbg >> 0) & 0xFF);

				double rr = Math.pow(red / 255.0, 2.2);
				double gg = Math.pow(green / 255.0, 2.2);
				double bb = Math.pow(blue / 255.0, 2.2);

				// Calculate luminance:
				double lum = 0.2126 * rr + 0.7152 * gg + 0.0722 * bb;

				// Gamma compand and rescale to byte range:
				int val = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));
				int result = (alpha << 24) + (val << 16) + (val << 8) + val;

				destination.setRGB(x, y, result);
			}
		}
		return destination;
	}

	public static BufferedImage copy(BufferedImage img) {
		ColorModel cm = img.getColorModel();
		boolean isAlphaPremulti = cm.isAlphaPremultiplied();
		WritableRaster wr = img.getRaster().createCompatibleWritableRaster();
		return new BufferedImage(cm, img.copyData(wr), isAlphaPremulti, null);
	}

	public static BufferedImage sub(BufferedImage image, Rectangle r) {
		return image.getSubimage(r.x, r.y, r.width, r.height);
	}

	public static BufferedImage bullseye(BufferedImage src, Point p) {
		return bullseye(src, p.x, p.y);
	}

	public static BufferedImage bullseye(BufferedImage src, int x, int y) {
		BufferedImage dest = Images.copy(src);

		Graphics2D gfx = (Graphics2D) dest.getGraphics();
		gfx.setColor(Color.WHITE);

		gfx.drawOval(x - 5, y - 5, 10, 10);
		gfx.drawRect(x, y, 0, 0);

		gfx.dispose();

		return dest;
	}

	public static BufferedImage circle(BufferedImage src, int x, int y) {
		BufferedImage dest = Images.copy(src);

		Graphics2D gfx = (Graphics2D) dest.getGraphics();
		gfx.setColor(Color.WHITE);
		gfx.drawOval(x, y, 10, 10);
		gfx.dispose();

		return dest;
	}

	public static void toFile(RenderedImage image, File output) {
		try {
			Files.createDirectories(output.toPath());
			ImageIO.write(image, Images.PNG, output);
		}
		catch (IOException e) {
			throw new AutomationException(e);
		}
	}

	public static BufferedImage toBufferedImage(File file) {
		try {
			return ImageIO.read(file);
		}
		catch (IOException e) {
			throw new AutomationException(e);
		}
	}

	public static Point midpoint(Rectangle rectangle) {
		int x = rectangle.width / 2;
		int y = rectangle.height / 2;

		return new Point(x, y);
	}

	private Images() {
		throw new UnsupportedOperationException("utility class");
	}
}
