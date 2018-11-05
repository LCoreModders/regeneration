package me.fril.regeneration.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scheduler {
	
	private long ticks = 0; //this could theoretically overflow if you left the game running for >10 billion years
	private Map<Long, List<ScheduledTask>> schedule = new HashMap<>();
	
	public void tick() {
		ticks++;
		
		if (schedule.containsKey(ticks)) {
			schedule.get(ticks).forEach(r->r.run());
			schedule.remove(ticks);
		}
	}
	
	public ScheduledTask scheduleInSeconds(long inSeconds, Runnable callback) {
		return scheduleInTicks(inSeconds * 20, callback);
	}
	
	public ScheduledTask scheduleInTicks(long inTicks, Runnable callback) {
		long scheduledTick = ticks + inTicks;
		
		List<ScheduledTask> list = schedule.containsKey(scheduledTick) ? schedule.get(scheduledTick) : new ArrayList<>();
		ScheduledTask task = new ScheduledTask(callback, scheduledTick);
		list.add(task);
		schedule.put(scheduledTick, list);
		
		return task;
	}
	
	public ScheduledTask createBlankTask() {
		return new BlankScheduledTask();
	}
	
	public void reset() {
		ticks = 0;
		schedule.clear();
	}
	
	
	
	public class ScheduledTask implements Runnable {
		
		private final Runnable callback;
		private final long scheduledTick;
		private boolean canceled;
		
		private ScheduledTask(Runnable callback, long scheduledTick) {
			this.callback = callback;
			this.scheduledTick = scheduledTick;
		}
		
		@Override
		public void run() {
			callback.run();
		}
		
		public void cancel() {
			if (canceled)
				System.err.println("WARNING: Cancelling already canceled action");
			canceled = true;
			
			Scheduler.this.schedule.get(scheduledTick).remove(this);
		}
		
		public long ticksLeft() {
			return canceled ? -1 : Scheduler.this.ticks - scheduledTick;
		}
		
	}
	
	
	private class BlankScheduledTask extends ScheduledTask {
		
		public BlankScheduledTask() {
			super(()->{}, -1);
		}
		
		@Override
		public void run() {
			
		}
		
		@Override
		public void cancel() {
			
		}
		
		@Override
		public long ticksLeft() {
			return -1;
		}
		
	}

}
