package com.cruat.automation.utilities;

import java.awt.FlowLayout;
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
	
	static final String PNG = "png";

	public static void show(BufferedImage image) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	public static BufferedImage copy(BufferedImage img) {
		ColorModel cm = img.getColorModel();
		boolean isAlphaPremulti = cm.isAlphaPremultiplied();
		WritableRaster wr = img.getRaster().createCompatibleWritableRaster();
		return new BufferedImage(cm, img.copyData(wr), isAlphaPremulti, null);
	}
	

	public static void toFile(RenderedImage image, File output) {
		try {
			ImageIO.write(image, Images.PNG, output);
		}
		catch (IOException e) {
			throw new AutomationException(e);
		}
	}

	private Images() {
		throw new UnsupportedOperationException("utility class");
	}
}
