package utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;

public class BufferedImageUtils {
	public static BufferedImage loadImage(URL url) {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bimg;
	}
	
	public static BufferedImage loadImage(String url) {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bimg;
	}
	
	
	/**
	 * @param img: A BufferedImage that will be split
	 * @param cols: Amount of columns
	 * @param rows: Amount of rows
	 * @return An array with the images
	 */
	public static BufferedImage[] splitImage(BufferedImage img, int cols, int rows) {
		int w = img.getWidth()/cols;
		int h = img.getHeight()/rows;
		int num = 0;
		BufferedImage imgs[] = new BufferedImage[w*h];
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < cols; x++) {
				imgs[num] = new BufferedImage(w, h, img.getType());
				// Tell the graphics to draw only one block of the image
				Graphics2D g = imgs[num].createGraphics();
				g.drawImage(img, 0, 0, w, h, w*x, h*y, w*x+w, h*y+h, null);
				g.dispose();
				num++;
			}
		}
		return imgs;
	}
}
