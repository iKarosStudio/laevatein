package laevatein.game.model;

/*
	可以被技能影響的地圖物件必須實做這個介面
*/
public interface SkillAffect
{
	public boolean hasSkillEffect (int skillId);
	
	public void addSkillEffect (int skillId, int time);
	public void addSkillEffect (int skillId, int time, int polyGfx);
	public void removeSkillEffect (int skillId);
	
	public void updateSkillTime (); //routine task
	
	//public void receiveSkillAttack ();
	//public void receiveSkillDebuff ();
}
