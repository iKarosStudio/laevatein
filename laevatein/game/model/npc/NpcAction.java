package laevatein.game.model.npc;

/*
import org.xml.*;
import java.io.*;
import javax.xml.*;
import javax.xml.parsers.*;
*/

public class NpcAction
{
	private final String actionName;
	private final int npcId;
	
	public NpcAction (String _actionName, int _npcId) {
		actionName = _actionName;
		npcId = _npcId;
		
		System.out.printf ("NPC ACTION name=%s, id=%d\n", actionName, npcId) ;
	}
}
