package laevatein.gui;

import javax.swing.*;
import javax.swing.event.*;

public class FunctionTab implements ChangeListener
{
	private static FunctionTab instance;
	JTabbedPane tabs;
	
	
	public static FunctionTab getInstance () {
		if (instance == null) {
			instance = new FunctionTab ();
		}
		return instance;
	}
	
	public FunctionTab () {
		tabs = new JTabbedPane (JTabbedPane.LEFT);
		tabs.setSize (800, 500);
		
		//建構其他功能子頁面
		tabs.addTab ("System", SystemTab.getInstance ().getComponent ());
		tabs.addTab ("Management", ManagementTab.getInstance ().getComponent ());
		tabs.addChangeListener (this);
		tabs.setVisible (true);
	}
	
	public JTabbedPane getComponent () {
		return tabs;
	}
	
	@Override
	public void stateChanged (ChangeEvent e) {
		JTabbedPane p = (JTabbedPane) e.getSource ();
		System.out.printf ("換到第%d個標籤\n", p.getSelectedIndex ()) ;
	}
}
