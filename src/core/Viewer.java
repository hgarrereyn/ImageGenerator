package core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * This class is responsible for constructing a window to display images and for
 * saving images to the filesystem
 * 
 * @author Russian-Assassin
 * 
 */
public class Viewer extends Canvas {
	/**
	 * The image that will be displayed on the screen
	 */
	private static BufferedImage toDisplay;
	
	/**
	 * String that indicates the images per second
	 * ex: "59 per second\t457 total"
	 */
	private static String ips = "";
	private static int modeInc = 0;
	private static String mode = "Mode: Random colorful";
	
	/**
	 * Control variables
	 */
	private static boolean isAnimated = false;
	private static long currentFrame = 0;
	private static int syncTime = 0;

	/**
	 * The frame that will be displayed
	 */
	private static JFrame frame;
	private static Viewer viewer;

	/**
	 * The background image
	 */
	private static BufferedImage background;
	
	/**
	 * Creates a window and adds a new Viewer to it
	 */
	public static void initWindow() {
		toDisplay = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
		try {
			background = ImageIO.read(Viewer.class.getResourceAsStream("background.png"));
		} catch (Exception e){
			e.printStackTrace();
			background = new BufferedImage(500,300,BufferedImage.TYPE_INT_RGB);
		}

		viewer = new Viewer();
		
		frame = new JFrame("Russian-Assassin's Generator");
		frame.setSize(500, 800);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		viewer.addKeyListener(new ViewerKeyListener());
		frame.add(viewer);
		frame.setVisible(true);
	}

	/**
	 * Set the currently visible image. This method will resize the window to
	 * fit the image (not smaller than 100x100)
	 * 
	 * @param b
	 *            The image to display
	 */
	public static void draw(BufferedImage b) {
		toDisplay = b;
		//frame.setSize(Math.max(b.getWidth(), 100), Math.max(b.getHeight(), 100));
		viewer.repaint();
	}

	@Override
	public void paint(Graphics g) {

	}

	@Override
	public void update(Graphics g) {
		//Draw the image
		g.drawImage(toDisplay, 0, 0, 500, 500, null);
		
		//Draw the debug background
		g.drawImage(background, 0, 500, 500, viewer.getHeight()-500, null);
		
		int pre = 20;
		
		//Titles
		g.setColor(Color.cyan); g.drawString("Current:", pre, 535);
		g.setColor(Color.red); g.drawString("Speed:", pre, 550);
		g.setColor(Color.green); g.drawString("Mode:", pre, 565);
		//580
		g.setColor(Color.yellow); g.drawString("Controls:", pre, 595);
		//610-640
		//655
		g.setColor(Color.orange); g.drawString("Modes:", pre, 700);
		
		int indent = pre + 70;
		
		//Info
		g.setColor(Color.white);
		g.drawString("" + currentFrame, indent, 535);
		if (isAnimated){
			g.drawString(ips, indent, 550);
		}else{
			g.setColor(Color.gray);
			g.drawString("N/A", indent, 550);
			g.setColor(Color.white);
		}
		g.drawString(mode, indent, 565);
		
		//Controls
		g.drawString("SPACE - Toggle animation", indent, 595);
		g.drawString("LEFT - decrement current seed (when paused)", indent, 610);
		g.drawString("RIGHT - increment current seed (when paused)", indent, 625);
		g.drawString("S - set seed", indent, 640);
		g.drawString("M - change mode", indent, 655);
		g.drawString("CMD/CTRL-S - save current image", indent, 670);
		
		//Modes
		int start = 700;
		for (String m : Creator.getModeStrings()){
			g.drawString(m, indent, start);
			start += 15;
		}
	}

	/**
	 * Runs a loop to modify and display images
	 */
	public static void loop() {
		while (true){
			if (isAnimated){
				nextFrame();
				Creator.syncSpeed(syncTime);
			}
			
			updateBuff();
			Viewer.draw(Creator.buff);
		}
	}
	
	/*
	 * CONTROL METHODS:
	 */
	
	public static void toggleAnimated(){
		isAnimated = isAnimated == false;
	}
	
	public static boolean isAnimated(){
		return isAnimated;
	}
	
	public static void nextFrame(){
		currentFrame++;
	}
	
	public static void previousFrame(){
		currentFrame--;
	}
	
	public static void setFrame(long frame){
		currentFrame = frame;
	}
	
	public static void setIps(String ips){
		Viewer.ips = ips;
	}
	
	public static void incrementMode(){
		modeInc = (modeInc + 1) % Creator.getModes();
	}
	
	/**
	 * This method calls the chosen image generation method
	 */
	public static void updateBuff(){
		switch (modeInc){
		case 0:{
			Creator.randomImage(Creator.buff, currentFrame);
			mode = "Random Colorful";
			break;
		}
		
		case 1:{
			Creator.randomBinaryImage(Creator.buff, currentFrame);
			mode = "Random Binary";
			break;
		}
		
		case 2:{
			Creator.pattern1(Creator.buff, currentFrame);
			mode = "Pattern 1";
			break;
		}
		
		default:{
			mode = "Error";
			break;
		}
		}
	}

}
