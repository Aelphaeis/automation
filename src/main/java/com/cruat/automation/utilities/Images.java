package com.cruat.automation.utilities;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class Images {

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

	private Images() {
		throw new UnsupportedOperationException("utility class");
	}
}
