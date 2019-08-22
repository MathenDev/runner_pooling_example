package runner_pooling;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Pool {

	public final int limit;

	private Queue<Runner> freeThreads;
	private List<Runner> busyThreads;

	public Pool(int limit) {
		super();
		this.limit = limit;
		freeThreads = new LinkedList<>();
		busyThreads = new LinkedList<>();
	}

	public void init() throws InterruptedException {
		for (int i = 0; i < limit; i++)
			freeThreads.offer(new Runner(this));
		for (Runnable runnable : freeThreads) {
			new Thread(runnable).start();
		}
		Thread.sleep(10);
	}

	synchronized void setFree(Runner runnableImpl) {
		boolean removed;
		synchronized (busyThreads) {
			removed = busyThreads.remove(runnableImpl);
		}
		if (removed)
			synchronized (freeThreads) {
				freeThreads.offer(runnableImpl);
			}
	}

	public boolean assignTask(Task task) {
		synchronized (freeThreads) {
			if (freeThreads.isEmpty())
				return false;
			synchronized (busyThreads) {
				Runner r = freeThreads.poll();
				r.notify(task);
				busyThreads.add(r);
			}
		}
		return true;
	}

}
