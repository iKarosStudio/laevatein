package laevatein.game.routine_task;

import java.util.concurrent.ScheduledFuture;

import laevatein.server.threadpool.*;
import laevatein.game.model.player.*;

//LS(Low Speed) task
public class LsTask implements Runnable
{
	ScheduledFuture<?> schedulor;
	
	public LsTask (PcInstance p) {
	}
	
	public void run () {
		//
	}
	
	public void start () {
		schedulor = KernelThreadPool.getInstance ().ScheduleAtFixedRate (this, 200, 500);
	}
	
	public void stop () {
		schedulor.cancel (true);
	}
}
