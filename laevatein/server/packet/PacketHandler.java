package laevatein.server.packet;

import laevatein.game.*;
import laevatein.game.skill.*;
import laevatein.server.*;
import laevatein.server.opcodes.*;
import laevatein.server.process_client.*;
import laevatein.server.process_server.*;

public class PacketHandler
{	
	private SessionHandler handle;
	
	public PacketHandler (SessionHandler session) {
		handle = session;
	}
	
	public void process (byte[] packet) {
		int opcode = packet[0] & 0x0FF;
		
		/*
		System.out.printf ("[C_OP:%3d] - [", opcode);
		for (byte b : packet) {
			System.out.printf ("0x%02X ", b);
		}
		System.out.println ("]");
		*/
		
		switch (opcode) {
		case ClientOpcodes.ITEM_USE: 
			new ItemUse (handle, packet);
			break;
			
		case ClientOpcodes.ATTACK:
			new NormalAttack (handle, packet);
			break;
			
		case ClientOpcodes.REMOTE_ATTACK:
			new NormalAttackLongRange (handle, packet);
			break;	
			
		case ClientOpcodes.MOVE:
			new Move (handle, packet);
			break;
		
		case ClientOpcodes.SKILL_USE:
			new SkillUse (handle, packet);
			break;
			
		case ClientOpcodes.UPDATE_HEADING: 
			new UpdateHeading (handle, packet);
			break;
			
		//case ClientOpcodes.ATTR : 
		//	break;
				
		case ClientOpcodes.ITEM_DROP:
			new ItemDrop (handle, packet);
			break;
		
		case ClientOpcodes.ITEM_PICK:
			new ItemPick (handle, packet);
			break;
		
		case ClientOpcodes.ITEM_DELETE :
			new ItemDelete (handle, packet);
			break;
			
		case ClientOpcodes.TALK:
			new Talk (handle, packet);
			break;
		
		case ClientOpcodes.GLOBAL_TALK:
			new GlobalTalk (handle, packet);
			break;
			
		case ClientOpcodes.ACCESS_NPC:
			new NpcAccess (handle, packet);
			break;
		
		case ClientOpcodes.ACTION_NPC:
			new NpcAction (handle, packet);
			break;
		
		case ClientOpcodes.REQUEST_NPC:
			new NpcRequest (handle, packet);
			break;
		
		case ClientOpcodes.SKILL_BUY:
			System.out.println ("要求技能商店清單");
			new SkillBuy (handle, packet);
			break;
		
		case ClientOpcodes.SKILL_BUY_ORDER:
			System.out.println ("要求買技能");
			new SkillBuyOrder (handle, packet);
			break;
		
		case ClientOpcodes.DOOR_TOUCH:
			//new DoorTouch(handle, packet);
			break;
			
		case ClientOpcodes.CLIENT_BEAT: //keep alive
		case ClientOpcodes.CLIENT_TICK:
			handle.sendPacket (new GameTime ().getPacket ());
			break;
		
		case ClientOpcodes.CLIENT_VERSION : //1.進入輸入帳號密碼畫面(客戶端回報版本)
			new ServerVersion (handle);
			break;
		
		case ClientOpcodes.CLIENT_CONFIG:
			//new ClientConfig (handle, packet) ;
			break;
		
		case ClientOpcodes.LOGIN_PACKET: //2.帳號密碼
			new AccountOperation().login (handle, packet);
			break;
			
		case ClientOpcodes.LIST_CHARACTER: //3.要求列出帳號的腳色清單 (l1j:CommonClick.java)
			new AccountOperation().getCharacterData (handle, packet); 
			break;
			
		case ClientOpcodes.CREATE_CHARACTER:
			new CharacterOperation ().create (handle, packet);
			break;
			
		case ClientOpcodes.LOGIN_TO_SERVER: //選定登入角色
			new CharacterOperation().login (handle, packet);
			break;
			
		case ClientOpcodes.DELETE_CHARACTER:
			new CharacterOperation ().delete (handle, packet);
			break;
			
		case ClientOpcodes.LOGIN_TO_SERVER_DONE: 
			//客戶端回報進入世界完成
			break;
			
		case ClientOpcodes.RESTART:
			handle.user.activePc.save ();
			break;
		
		case ClientOpcodes.WHO:
			String count = String.format ("%d", Laevatein.getInstance ().getOnlinePlayers ());
			handle.sendPacket (new GameMessage (81, count).getPacket ());
			break;
			
		case ClientOpcodes.TS: //神秘後門服務(Text Service)
			new TS (handle, packet);
			break;
			
		case ClientOpcodes.RST: //重新開始
			if (handle.user.activePc != null) {
				handle.user.activePc.save ();
			}
			break;
			
		case ClientOpcodes.EXIT_GAME: //離開遊戲
			new ExitGame (handle, packet);
			break;
			
		default:
			System.out.println ("UNKNOWN OPCODE : " + opcode + " Length : " + packet.length);
			for (byte b : packet) {
				System.out.printf ("0x%02X, ", b);
			}
			System.out.println ();
			/*
			try {
				Handle.Disconnect () ;
			} catch (Exception e) {
				//
			}
			*/
			break;
		}
	}
}
