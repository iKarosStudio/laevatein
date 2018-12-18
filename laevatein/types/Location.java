package laevatein.types;

/* 表示一個物件在遊戲中的位置 */
public class Location
{
	/* 所在地圖編號 */
	public int mapId;
	
	/* 所在座標 */
	public int x, y;
	
	public Location () {
		mapId = 0;
		x = 0;
		y = 0;
	}
	
	public Location (int mapId, int x, int y) {
		this.mapId = mapId;
		this.x = x;
		this.y = y;
	}
}
