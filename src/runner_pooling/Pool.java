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
		synchronized (freeRunners) {
			if (freeRunners.isEmpty())
				return false;
			synchronized (busyRunners) {
				Runner r = freeRunners.poll();
				r.yield(task);
				busyRunners.add(r);
			}
		}
		return true;
	}

}
