package laevatein.gui;

import javax.swing.*;
import java.awt.*;

import laevatein.game.*;

public class GuiMain
{
	public static GuiMain instance;
	
	public static GuiMain getInstance () {
		if (instance == null) {
			instance = new GuiMain ();
		}
		return instance;
	}
	
	public GuiMain () {
		JFrame mainWindow = new JFrame ("Vidar GUI");
		FunctionTab functionPane = FunctionTab.getInstance ();
		ConsolePane console = ConsolePane.getInstance ();
		
		
		mainWindow.add (functionPane.getComponent (), BorderLayout.CENTER);
		mainWindow.add (console.getComponent (), BorderLayout.SOUTH);
		
		mainWindow.setSize (800, 600);
		mainWindow.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible (true);
	}
	
	public static void setVidar (Laevatein v) {
		//
	}
}
