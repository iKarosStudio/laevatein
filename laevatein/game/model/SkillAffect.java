package laevatein.game.model;

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
