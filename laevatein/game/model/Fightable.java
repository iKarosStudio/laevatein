package laevatein.game.model;

public interface Fightable
{
	public void attack (int tid, int x, int y);
	public void attack (Objeto target);
	
	public void attackLongRange (int tid, int x, int y);
	public void attackLongRange (Objeto target);
}
