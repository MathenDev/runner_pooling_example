package test;

import java.util.Random;

import runner_pooling.Pool;
import runner_pooling.Task;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		Pool pool = new Pool(5000);
		pool.init();
		for (int i = 0; i < 1000000000; i++) {
			Task t = new Task() {

				@Override
				public Object perform() throws Exception {
					Random rd = new Random();
					int i = rd.nextInt(30000);
					synchronized (rd) {
						rd.wait(i);
					}
					return i;
				}

				@Override
				public void onException(Exception e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onDone(Object t) {
					System.out.println(t);
				}
			};

			while (!pool.assignTask(t)) {
				synchronized (t) {
					t.wait(300);
				}
			}

		}

//		Stack<Integer> stack = new Stack<>();
//		IntStream.range(0, 1000).forEach(stack::push);
//		while (!stack.isEmpty()) {
//			final int current = stack.peek();
//			if (pool.assignTask(new Task() {
//
//				@Override
//				public Object perform() throws Exception {
//					return current;
//				}
//
//				@Override
//				public void onDone(Object t) {
//					System.out.println(t);
//				}
//
//				@Override
//				public void onException(Exception e) {
//					e.printStackTrace();
//				}
//
//			}))
//				stack.pop();
//
//		}
	}

}
