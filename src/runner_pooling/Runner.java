package runner_pooling;

class Runner implements Runnable {

	private final Pool pool;

	private Task task;

	public Runner(Pool pool) {
		super();
		this.pool = pool;
	}

	void notify(Task task) {
		synchronized (this) {
			this.task = task;
			this.notify();
		}
	}

	@Override
	public void run() {

		while (true) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				if (task != null) {
					try {
						Object o = task.perform();
						task.onDone(o);
					} catch (Exception e) {
						task.onException(e);
					}
				}
				task = null;
				pool.setFree(this);
			}
		}

	}

}
