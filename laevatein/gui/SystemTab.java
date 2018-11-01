package laevatein.gui;

import javax.swing.*;
import java.awt.*;

import laevatein.server.*;

public class SystemTab
{
	private static SystemTab instance;
	
	JPanel sysInfoPane;
	JProgressBar cpuUsage;
	JProgressBar memUsage;
	JLabel threadCountLabel, cpuNameLabel, osNameLabel, pidLabel;
	
	public static SystemTab getInstance () {
		if (instance == null) {
			instance = new SystemTab ();
		}
		return instance;
	}
	
	public SystemTab () {
		sysInfoPane = new JPanel ();
		sysInfoPane.setLayout(new GridLayout(0, 4, 0, 0));
		cpuNameLabel = new JLabel ("CPU : " + SystemMonitor.getInstance ().cpuName);
		cpuNameLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		sysInfoPane.add (cpuNameLabel);
		osNameLabel = new JLabel ("OS : " + SystemMonitor.getInstance ().osName);
		sysInfoPane.add (osNameLabel);
		
		cpuUsage = new JProgressBar ();
		cpuUsage.setMaximum (1000);
		cpuUsage.setMinimum (0);
		cpuUsage.setStringPainted (true);
		sysInfoPane.add (cpuUsage);
		
		memUsage = new JProgressBar ();
		memUsage.setMaximum (SystemMonitor.getInstance ().getMaxMemory ());
		memUsage.setMinimum (0);
		memUsage.setStringPainted (true);
		sysInfoPane.add (memUsage);
		
		
		threadCountLabel = new JLabel ();
		sysInfoPane.add (threadCountLabel);
		sysInfoPane.setVisible (true);

	}
	
	public void update () {
		if (FunctionTab.getInstance ().getComponent ().getSelectedIndex () == 0) {
			//update string
			int value = (int) SystemMonitor.getInstance ().cpuUsage * 10;
			cpuUsage.setValue (value);
			value = (int) SystemMonitor.getInstance ().memUsage;
			memUsage.setValue (value);
			
			threadCountLabel.setText (String.format ("Thread : %d", SystemMonitor.getInstance ().threadCount));
		}
	}
	
	public JPanel getComponent () {
		return sysInfoPane;
	}
}
