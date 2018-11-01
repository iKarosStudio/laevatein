package laevatein.game.model;

public interface Moveable
{
	public void move (int x, int y, int heading);
	public void moveToHeading (int heading);
}
