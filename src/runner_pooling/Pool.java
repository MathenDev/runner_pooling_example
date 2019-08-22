package runner_pooling;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Pool {

	public final int size;

	private Queue<Runner> freeRunners;
	private List<Runner> busyRunners;

	public Pool(int size) {
		super();
		this.size = size;
		freeRunners = new LinkedList<>();
		busyRunners = new LinkedList<>();
	}

	public void init() {
		for (int i = 0; i < size; i++)
			freeRunners.offer(new Runner(this));
		for (Runnable runnable : freeRunners) {
			new Thread(runnable).start();
		}
	}

	synchronized void setFree(Runner runner) {
		boolean removed;
		synchronized (busyRunners) {
			removed = busyRunners.remove(runner);
		}
		if (removed)
			synchronized (freeRunners) {
				freeRunners.offer(runner);
			}
	}

	public boolean assignTask(Task task) {
		Runner r;
		synchronized (freeRunners) {
			r = freeRunners.poll();
		}
		if (r == null)
			return false;
		synchronized (busyRunners) {
			busyRunners.add(r);
		}
		r.yield(task);
		return true;
	}

}
