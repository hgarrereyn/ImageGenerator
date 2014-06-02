package core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class ViewerKeyListener implements KeyListener{

	/**
	 * Controls:
	 * 
	 * Space - toggle animation
	 * left - previous frame (when stopped)
	 * right - next frame (when stopped)
	 * cmd-s or ctrl-s - save the image(open dialog)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()){
		case (KeyEvent.VK_SPACE):{
			Viewer.toggleAnimated();
			break;
		}
		case (KeyEvent.VK_LEFT):{
			if (!Viewer.isAnimated())
				Viewer.previousFrame();
			break;
		}
		case (KeyEvent.VK_RIGHT):{
			if (!Viewer.isAnimated())
				Viewer.nextFrame();
			break;
		}
		case (KeyEvent.VK_S):{
			if (!Viewer.isAnimated()){
				if (e.isControlDown() || e.isMetaDown()){
					JFileChooser chooser = new JFileChooser();
					int result = chooser.showSaveDialog(null);
					if (result == JFileChooser.APPROVE_OPTION){
						File f = chooser.getSelectedFile();
						if (!f.getAbsolutePath().endsWith(".png"))
							f = new File(f.getAbsolutePath().concat(".png"));
						
						try {
							ImageIO.write(Creator.buff, "PNG", f);
							JOptionPane.showMessageDialog(null, "Saved image successfully");
						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "Could not save image");
						}
					}
				} else {
					String result = JOptionPane.showInputDialog(null, "Enter seed: (Integer between -2^63 and 2^63-1)", "0");
					try {
						long r = Long.parseLong(result);
						Viewer.setFrame(r);
					} catch (Exception ex){
						//Just ignore
					}
				}
			}
			break;
		}
		case (KeyEvent.VK_M):{
			Viewer.incrementMode();
			break;
		}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
