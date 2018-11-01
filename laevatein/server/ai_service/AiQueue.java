package laevatein.server.ai_service;

import java.util.*;
import java.util.concurrent.*;

/* AI工作要求Queue */
public class AiQueue
{
	private static AiQueue instance;
	private static Queue<Runnable> taskQueue;
	
	public static AiQueue getInstance () {
		if (instance == null) {
			instance = new AiQueue ();
		}
		return instance;
	}
	
	public AiQueue () {
		System.out.printf ("A.I. queue initializing...") ;
		taskQueue = new ConcurrentLinkedQueue<Runnable> () ;
		System.out.printf ("success\n") ;
	}
	
	public int getQueueSize () {
		return taskQueue.size ();
	}
	
	public synchronized Queue<Runnable> getQueue () {
		return taskQueue;
	}
}
