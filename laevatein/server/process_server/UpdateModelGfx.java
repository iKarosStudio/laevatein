package laevatein.server.process_server;

import laevatein.server.opcodes.*;

public class UpdateModelGfx extends _PacketFrame
{
	public UpdateModelGfx (int uuid, int gfx, boolean unsetWeapon) {
		packet.writeByte (ServerOpcodes.UPDATE_MODEL_GFX);
		packet.writeDoubleWord (uuid);
		packet.writeWord (gfx);
		//TODO:不知道為什麼29
		packet.writeByte (unsetWeapon ? 0 : 29);
	}
}
