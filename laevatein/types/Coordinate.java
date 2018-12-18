package laevatein.types;

@Deprecated
public class Coordinate
{
	/* -32768 ~ 32767 */
	public int x = 0;
	public int y = 0;
	
	public Coordinate (int _x, int _y) {
		x = _x;
		y = _y;
	}
}
