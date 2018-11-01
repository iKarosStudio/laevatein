package laevatein.game.model;

//basic parameters, skill parameters, equipment parameters access interface
public interface ApAccessable
{	
	public int getStr ();
	public int getCon ();
	public int getDex ();
	public int getWis ();
	public int getCha ();
	public int getIntel ();
	
	public int getMaxHp ();
	public int getMaxMp ();
	
	public int getSp ();
	public int getMr ();
	
	public int getAc ();
}
