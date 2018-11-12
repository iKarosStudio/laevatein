package laevatein.gui;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ConsolePane
{
	private static ConsolePane instance;
	JTextArea console;
	JScrollPane scroll;
	
	public static ConsolePane getInstance () {
		if (instance == null) {
			instance = new ConsolePane ();
		}
		return instance;
	}
	
	public ConsolePane () {
		console = new JTextArea (10, 30);
		scroll = new JScrollPane (console);
		
		console.setEditable (false);
		console.setSize (800, 100);
		console.setLineWrap (true);
		console.setForeground (new Color (0, 180, 0));
		console.setBackground (Color.BLACK);
		DefaultCaret c = (DefaultCaret) console.getCaret ();
		c.setUpdatePolicy (DefaultCaret.ALWAYS_UPDATE);
		scroll.setVerticalScrollBarPolicy (JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//系統導向
		OutputStream textAreaStream = new OutputStream() {
			//StringBuffer buffer = new StringBuffer ();
			public void write(int b) throws IOException {
				console.append(String.valueOf ((char) b));
			}
		};
		
		try {
			System.setOut (new PrintStream (textAreaStream, true, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
	
	public JScrollPane getComponent () {
		return scroll;
	}
}
