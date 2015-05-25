

public class UserThread {
	public static void main(String[] args) {
		final Result result1 = new Result();
		final Result result2 = new Result();
		final Result result3 = new Result();
		Object lock = new Object();
		Object lock2 = new Object();
		ThreadManager tm = new ThreadManager(1, 10, result1, result2, result3, lock);
		MyQueueGen<Task> tasks = new MyQueueGen<Task>();
		for (int i = 0; i < 10; i++) {
			tasks.add(new Task() {
				@Override
				public void doTask(int a, int b) {
					result1.setRes(a * b);
				}
			});
		}
		Feeder feeder = new Feeder(tasks, tm, lock);
		feeder.start();
		tm.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tm.setFinished(true);
		System.out.println(result1.getRes());
	}
}
