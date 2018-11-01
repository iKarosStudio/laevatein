package laevatein.game.model.npc;

public class NpcTalkData
{
	public final int npcId;
	public final String normalAction;
	public final String caoticAction;
	public final String teleportUrl;
	public final String teleportUrla;
	
	public NpcTalkData (int _npcId, String _normalAction, String _caoticAction, String _teleportUrl, String _teleportUrla) {
		npcId = _npcId;
		normalAction = _normalAction;
		caoticAction = _caoticAction;
		teleportUrl = _teleportUrl;
		teleportUrla = _teleportUrla;
	}
}
