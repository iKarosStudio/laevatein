package laevatein.game.routine_task;

import java.util.concurrent.ScheduledFuture;

import laevatein.server.*;
import laevatein.server.database.*;
import laevatein.server.threadpool.*;
import laevatein.server.utility.*;
import laevatein.server.process_server.*;
import laevatein.game.model.player.*;

//HS(HighSpeed) task
//hp monitor
//exp monitor
public class HsTask implements Runnable
{
	ScheduledFuture<?> schedulor;
	private PcInstance pc;
	private SessionHandler handle;
	
	public HsTask (PcInstance p) {
		pc = p;
		handle = pc.getHandle ();
	}
	
	public void run () { //0.5s interval
		expMonitor ();
		
	}
	
	private void expMonitor () {
		boolean toggleSave = false;
		
		try {
			while (pc.getExp () >= EXP_REQUEST[pc.getLevel ()]) {
				pc.setLevel (pc.getLevel () + 1); //level++
				
				//升級事項
				int hpIncrease = Utility.calcIncreaseHp (pc.getType (), pc.hp, pc.basicParameters.getHp (), pc.basicParameters.getCon ());
				int mpIncrease = Utility.calcIncreaseMp (pc.getType (), pc.mp, pc.basicParameters.getMp (), pc.basicParameters.getWis ());
				pc.basicParameters.setHp (pc.basicParameters.getHp () + hpIncrease);
				pc.basicParameters.setMp (pc.basicParameters.getMp () + mpIncrease);
				
				pc.basicParameters.setAc (Utility.calcAcBonusFromDex (pc.getLevel (), pc.basicParameters.getDex ()));
				
				//重新計算sp, mr
				pc.basicParameters.setSp (Utility.calcSp (pc.getType (), pc.getLevel (), pc.basicParameters.getInt ()));
				pc.basicParameters.setMr (Utility.calcMr (pc.getType (), pc.getLevel (), pc.basicParameters.getWis ()));
				
				toggleSave = true;
			}
			
			//升級事件處理
			if (toggleSave) {
				handle.sendPacket (new ModelStatus (pc).getPacketNoPadding ());
				handle.sendPacket (new UpdateExp(pc).getPacket ());
				DatabaseCmds.savePc (pc);
				toggleSave = false;
			}
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
	
	public void start () {
		schedulor = KernelThreadPool.getInstance ().ScheduleAtFixedRate (this, 200, 500);
	}
	
	public void stop () {
		schedulor.cancel (true);
	}
	
	/* 
	 * 0-99等級需求經驗值表 
	 * 經驗值的量為到當前等級所累積的總和, 並不是所需要的量
	 * */
	private static final int EXP_REQUEST[] = {
			0, 125, 300, 500, 750, //0-4
			1296, 2401, 4096, 6581, 10000, //5-9
			14661, 20756, 28581, 38436, 50645, 0x10014,
			0x14655, 0x19a24, 0x1fd25, 0x27114, 0x2f7c5, 0x39324, 0x44535,
			0x51010, 0x5f5f1, 0x6f920, 0x81c01, 0x96110, 0xacae1, 0xc5c20,
			0xe1791, 0x100010, 0x121891, 0x146420, 0x16e5e1, 0x19a110,
			0x1c9901, 0x1fd120, 0x234cf1, 0x271010, 0x2b1e31, 0x2f7b21,
			0x342ac2, 0x393111, 0x3e9222, 0x49b332, 0x60b772, 0x960cd1,
			0x12d4c4e, 0x3539b92, 0x579ead6, 0x7a03a1a, 0x9c6895e, 0xbecd8a2,
			0xe1327e6, 0x1039772a, 0x125fc66e, 0x148615b2, 0x16ac64f6,
			0x18d2b43a, 0x1af9037e, 0x1d1f52c2, 0x1f45a206, 0x216bf14a,
			0x2392408e, 0x25b88fd2, 0x27dedf16, 0x2a052e5a, 0x2c2b7d9e,
			0x2e51cce2, 0x30781c26, 0x329e6b6a, 0x34c4baae, 0x36eb09f2,
			0x39115936, 0x3b37a87a, 0x3d5df7be, 0x3f844702, 0x41aa9646,
			0x43d0e58a, 0x45f734ce, 0x481d8412, 0x4a43d356, 0x4c6a229a,
			0x4e9071de, 0x50b6c122, 0x52dd1066, 0x55035faa, 0x5729aeee,
			0x594ffe32, 0x5b764d76, 0x5d9c9cba, 0x5fc2ebfe, 0x61e93b42,
			0x640f8a86, 0x6635d9ca, 0x685c290e, 0x6a827852, 0x6ca8c796,
			0x6ecf16da};
}
