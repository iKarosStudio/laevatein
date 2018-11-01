package laevatein.gui;

import javax.swing.*;

import laevatein.game.*;

public class ManagementTab 
{
	private static ManagementTab instance;
	JPanel managementPane;
	JLabel onlineAmountLabel;
	
	public static ManagementTab getInstance () {
		if (instance == null) {
			instance = new ManagementTab ();
		}
		return instance;
	}
	
	public ManagementTab () {
		managementPane = new JPanel ();
		onlineAmountLabel = new JLabel ();
		onlineAmountLabel.setVisible (true);
		
		managementPane.add (onlineAmountLabel);
		managementPane.setVerifyInputWhenFocusTarget (true);
	}
	
	public JPanel getComponent () {
		return managementPane;
	}
	
	public void update () {
		if (FunctionTab.getInstance ().getComponent ().getSelectedIndex () == 1) {
			onlineAmountLabel.setText (String.format ("Online players : %d", Laevatein.getInstance ().getOnlinePlayers ()));
		}
	}
}
