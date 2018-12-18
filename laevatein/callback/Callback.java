package laevatein.callback;

import laevatein.game.model.player.*;
import laevatein.server.process_server.*;

/*
 * 設定角色能力值更新時觸發的更新包
 */
public class Callback {
	PcInstance pc;
	boolean enable = false;
	
	//建構回報目標 pc一定要有效
	public Callback (PcInstance pc) {
		this.pc = pc;
	}
	
	public void updateStr () {
		//check weight scale
	}
	
	public void updateCon () {
		//check weight scale
	}
	
	public void updateDex () {
		//check basic dex for AC
	}
	
	public void updateWis () {
		//check Mr
	}
	
	public void updateCha () {
	}
	
	public void updateIntel () {
		//check Sp
	}
	
	public void updateMaxHp () {
		if (enable) {
			pc.sendPacket (new UpdateHp (pc.hp, pc.getMaxHp ()).getPacket ());
		}
	}
	
	public void updateMaxMp () {
		if (enable) {
			pc.sendPacket (new UpdateMp (pc.mp, pc.getMaxMp ()).getPacket ());
		}
	}
	
	public void updateAc () {
		if (enable) {
			pc.sendPacket (new UpdateAc (pc.getAc ()).getPacket ());
		}
	}
	
	public void updateSpMr () {
		if (enable) {
			pc.sendPacket (new ReportSpMr (pc.getSp (), pc.getMr ()).getPacket ());
		}
	}
	
	public void updateWeightScale () {
		if (enable) {
			pc.sendPacket (new UpdateWeight (pc.getWeightScale30 ()).getPacket ());
		}
	}
	
	public boolean isEnable () {
		return enable;
	}
	
	public void setEnable (boolean enable) {
		this.enable = enable;
	}
			
}
