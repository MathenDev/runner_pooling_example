package test;

import runner_pooling.Pool;
import runner_pooling.Task;

public class Test {

	static int accept = 0, reject = 0;

	public static void main(String[] args) throws InterruptedException {
		Pool pool = new Pool(10);
		pool.init();
		for (int i = 0; i < 20; i++) {
			final int current = i;
			if (pool.assignTask(new Task() {

				@Override
				public Object perform() throws Exception {
					return current;
				}

				@Override
				public void onDone(Object t) {
					System.out.println(t);
				}

				@Override
				public void onException(Exception e) {
					e.printStackTrace();
				}

			}))
				accept++;
			else
				reject++;

		}
		System.out.println("Accept: " + accept);
		System.err.println("Reject: " + reject);
	}
}
