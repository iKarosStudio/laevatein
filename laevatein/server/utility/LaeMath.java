package laevatein.server.utility;

public class LaeMath
{
	public static int abs (int a) {
		return (a ^ (a >> 31)) - (a >> 31);
	}
	
	public static int sqrt (int s) {
		//TODO:使用JOHN CARMACK算法優化開根號算法
		//在java 1.8環境不須做優化已經有良好效能
		//java 1.5以前環境建置必須重寫這個method
		return (int) Math.sqrt ((double) s);
	}
	
	//
	//
	//
	
}
