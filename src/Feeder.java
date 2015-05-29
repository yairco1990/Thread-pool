

public class Feeder extends Thread {
	// queue of the tasks that sent from UserThread
	private MyQueueGen<Task> tasks = new MyQueueGen<Task>();
	private ThreadManager tm;
	private Object lock;
	private volatile boolean finish;

	public Feeder(MyQueueGen<Task> tasks, ThreadManager tm, Object lock) {
		this.tm = tm;
		this.finish = false;
		this.tasks = tasks;
		this.lock = lock;
		System.err.println(tasks.size());
	}

	@Override
	public void run() {
		while (!finish) {
			// check if there are tasks to send to the manager
			if (!tasks.isEmpty()) {
				// try to send task to the manager.
				// if return true - keep to send.
				// else wait until the manager tasks will be available
				if (!tm.setTaskFromFeeder(tasks.peek())) {
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					//if success to send - remove the sent task
					tasks.poll();
				}
			} else {
				// finish himself after send all the tasks to the manager
				setFinish(true);
				System.out.println("NO TASKS IN FEEDER!");
			}
		}

		System.err.println("FEEDER RELEASED");
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public void setTasks(Task t) {
		tasks.add(t);
	}

}
