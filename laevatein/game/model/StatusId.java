package laevatein.game.model;

public abstract class StatusId
{
	public static final int STATUS_POISON = 0x01; //中毒
	public static final int STATUS_INVISIBLE = 0x02;//隱身
	public static final int STATUS_PC = 0x04; //一般玩家
	public static final int STATUS_FROZEN = 0x08; //冷凍
	public static final int STATUS_BRAVE = 0x10; //勇水
	public static final int STATUS_ELF_BRAVE = 0x20; //精餅
	public static final int STATUS_FASTMOVE = 0x40; //高速移動用
	public static final int STATUS_GHOST = 0x80; //幽靈模式
}
