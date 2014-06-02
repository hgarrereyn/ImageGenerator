package core;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Creator {
	/**
	 * Static image to prevent repeatedly creating buffered images
	 */
	public static BufferedImage buff;
	
	private static ArrayList<BufferedImage> parts = new ArrayList<BufferedImage>();

	/**
	 * Used to count things
	 */
	private static long total = 0;
	private static long counter = 0;
	private static long lastCheck = System.currentTimeMillis();
	
	private static LargeSeed a;

	public static void main(String[] args) {
		buff = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		a = new LargeSeed(buff.getWidth() * buff.getHeight());

		Viewer.initWindow();
		Viewer.loop();
	}

	/**
	 * While animating, this should be called every time an image is created.
	 * @param time
	 */
	public static void syncSpeed(int time) {
		counter++;
		if (System.currentTimeMillis() - lastCheck > 1000) {
			Viewer.setIps(counter + " per second");
			counter = 0;
			lastCheck = System.currentTimeMillis();
		}
		
		while (System.currentTimeMillis() - lastCheck < time){
			//wait
			try {
				Thread.sleep(time - (System.currentTimeMillis() - lastCheck));
			} catch (Exception e){
				
			}
		}
	}
	
	/**
	 * Retuns the number of mode types
	 * Current types:
	 * <ul>
	 * <li>Random colorful</li>
	 * <li>Random binary</li>
	 * </ul>
	 * @return 2
	 */
	public static int getModes(){
		return 3;
	}
	
	public static String[] getModeStrings(){
		return new String[]{
				"Random colorful",
				"Random binary",
				"Pattern 1"
		};
	}

	/**
	 * This method creates a random colorful image from a seed
	 * @param buff
	 * @param seed
	 */
	public static void randomImage(BufferedImage buff, long seed) {
		Random r = new Random(seed);

		int[] pixels = new int[buff.getWidth() * buff.getHeight()];

		for (int i = 0; i < pixels.length; ++i){
			pixels[i] = new Color(((r.nextFloat()*255)/255F),((r.nextFloat()*255)/255F),((r.nextFloat()*255)/255F), 1F).getRGB();
		}
			
		buff.setRGB(0, 0, buff.getWidth(), buff.getHeight(), pixels, 0, buff.getWidth());
	}
	
	/**
	 * This method creates a random binary image from a seed
	 * @param buff
	 * @param seed
	 */
	public static void randomBinaryImage(BufferedImage buff, long seed){
		Random r = new Random(seed);
		
		int[] pixels = new int[buff.getWidth() * buff.getHeight()];

		for (int i = 0; i < pixels.length; ++i){
			pixels[i] = r.nextBoolean()?new Color(255,255,255).getRGB():new Color(0,0,0).getRGB();
		}
			
		buff.setRGB(0, 0, buff.getWidth(), buff.getHeight(), pixels, 0, buff.getWidth());
	}
	
	public static void pattern1(BufferedImage buff, long seed){
		a.increment();
		
		buff.setRGB(0, 0, buff.getWidth(), buff.getHeight(), a.getImageData(), 0, buff.getWidth());
	}
	
	public static Color vary(Color c, float v, float v2, float v3){
		int r = (int)Math.min(Math.max(c.getRed() + (((v*2)-1) * 10), 0), 100);
		int g = (int)Math.min(Math.max(c.getGreen() + (((v2*2)-1) * 10), 0), 100);
		int b = (int)Math.min(Math.max(c.getBlue() + (((v3*2)-1) * 10), 0), 100);
		return new Color(r,g,b);
	}
}
