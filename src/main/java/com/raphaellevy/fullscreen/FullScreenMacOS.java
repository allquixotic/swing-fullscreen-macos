//Copyright (C) 2017 Raphael Levy
package com.raphaellevy.fullscreen;

import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *	Utility methods for adding native mac full screen support to AWT/Swing applications.
 */
public class FullScreenMacOS 
{

	public static void main(String[] args) throws Throwable {
		final JFrame frame = new JFrame("JFrame Example");
		FullScreenMacOS.setFullScreenEnabled(frame, true);

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		JLabel label = new JLabel("This is a label!");

		JButton button = new JButton();
		button.setText("Click me to toggle fullscreen");
		button.addActionListener((evt) -> {
			try {
				FullScreenMacOS.toggleFullScreen(frame);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(frame, "failed");
			}
		});

		panel.add(label);
		panel.add(button);

		frame.add(panel);
		frame.setSize(300, 300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	/**
	 * Set whether this window can be made full screen.
	 * @param frame
	 * @throws FullScreenException
	 */
	public static void setFullScreenEnabled(Window frame, boolean b) throws FullScreenException {
		try {
			Class<? extends Object> fsu = Class.forName("com.apple.eawt.FullScreenUtilities");
			fsu.getMethod("setWindowCanFullScreen", Window.class, Boolean.TYPE).invoke(null, frame, b);
		} catch (ClassNotFoundException e) {
			throw new FullScreenException(FullScreenException.CLASS_DOES_NOT_EXIST);
		} catch (Exception e) {
			throw new FullScreenException(FullScreenException.REFLECTION_ERROR);
		}
	}
	
	/**
	 * Toggles whether this window is full screen. Full screen must be enabled with setFullScreenEnabled() first, and the window must be visible.
	 * @param frame
	 * @throws FullScreenException
	 */
    public static void toggleFullScreen(Window frame) throws FullScreenException {
    	try {
			Class<? extends Object> app = Class.forName("com.apple.eawt.Application");
			Object geta = app.getMethod("getApplication").invoke(null);
			geta.getClass().getMethod("requestToggleFullScreen", Window.class).invoke(geta, frame);
		} catch (ClassNotFoundException e) {
			throw new FullScreenException(FullScreenException.CLASS_DOES_NOT_EXIST);
		} catch (Exception e) {
			throw new FullScreenException(FullScreenException.REFLECTION_ERROR);
		}
    }
    
    /**
     * Gets whether full screen is available on this computer.
     */
    public static boolean fullScreenAvailable() {
    	try {
    		Class.forName("com.apple.eawt.FullScreenUtilities");
        	Class.forName("com.apple.eawt.Application");
    	} catch (ClassNotFoundException e) {
    		return false;
    	}
    	return true;
    	
    }
}
