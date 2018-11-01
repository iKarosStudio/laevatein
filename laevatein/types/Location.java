package laevatein.types;

/* 表示一個物件在遊戲中的位置 */
public class Location
{
	/* 所在地圖編號 */
	public int mapId;
	
	/* 所在座標 */
	public Coordinate p;
	
	public Location () {
		mapId = 0;
		p = new Coordinate (0, 0);
	}
	
	public Location (int _mapId, int x, int y) {
		mapId = _mapId;
		p = new Coordinate (x, y);
	}
}
