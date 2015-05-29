
public class ThreadManager extends Thread {
	private int numberOfThreads;
	private int numberOfTasks;
	private volatile boolean isFinished;
	private MyQueueGen<Task> tasks = new MyQueueGen<>();
	private MyQueueGen<ThreadUnit> workingThreads = new MyQueueGen<ThreadUnit>();
	private MyQueueGen<ThreadUnit> restingThreads = new MyQueueGen<ThreadUnit>();
	private Result result1, result2, result3;
	private ThreadHelper th;
	private Object lock;
	public static int id = 1;

	public ThreadManager(int numberOfThreads, int numberOfTasks,
			Result result1, Result result2, Result result3, Object lock) {
		this.numberOfThreads = numberOfThreads;
		this.numberOfTasks = numberOfTasks;
		this.result1 = result1;
		this.result2 = result2;
		this.result3 = result3;
		this.isFinished = false;
		this.lock = lock;
	}

	@Override
	public void run() {
		// initialize the ThreadHelper
		th = new ThreadHelper(workingThreads, restingThreads, this);

		// initialize all the threads
		for (int i = 0; i < numberOfThreads; i++) {
			restingThreads.add(new ThreadUnit());
		}

		// wake up all the threads to work
		// checks that there are tasks to give to the thread units
		// remove them from the resting queue and move them to the working queue
		for (int i = 0; i < numberOfThreads; i++) {
			restingThreads.peek().start();
			workingThreads.add(restingThreads.poll());
		}

		// thread helper start to look for threads that free to work and move
		// them to the resting queue
		th.start();

		// start to working until finish all the calculates
		while (!this.isFinished) {
			// if all the threads working - wait.
			if (restingThreads.isEmpty()) {
				synchronized (this) {
					try {
						System.out.println("MANAGER WAIT!");
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			// if there are available threads in resting queue
			// send them tasks
			else {
				// check if there are available tasks to do
				if (!tasks.isEmpty()) {
					// System.out.println("There are tasks available [" +
					// tasks.peek() + "]");
					ThreadUnit tu = restingThreads.poll();
					workingThreads.add(tu);
					tu.setFreeToWorkAndTask(false, tasks.poll());
					// release feeder to get more tasks
					synchronized (lock) {
						lock.notify();
					}
					// wake up the thread unit to do the task
					synchronized (tu) {
						tu.notify();
					}
				}
			}
		}
		synchronized (th) {
			th.setFinish(true);
			th.notify();
		}

		// kill all the threads when finish all tasks
		while (!restingThreads.isEmpty()) {
			ThreadUnit tu = restingThreads.peek();
			tu.setFinish(true);
			// if the thread wait - wake up him
			synchronized (tu) {
				restingThreads.poll().notify();
			}
		}
		while (!workingThreads.isEmpty()) {
			ThreadUnit tu = workingThreads.peek();
			tu.setFinish(true);
			// if the thread wait - wake up him
			synchronized (tu) {
				workingThreads.poll().notify();
			}
		}
		System.out.println("MANAGER FINISHED");
	}

	/* GETTERS AND SETTERS */
	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	/**
	 * check if we can to push more tasks. if return false - the feeder need to
	 * wait
	 * 
	 * @param t
	 * @return
	 */
	public synchronized boolean setTaskFromFeeder(Task t) {
		//System.out.println(t);
		if (numberOfTasks > tasks.size()) {
			tasks.add(t);
			return true;
		}
		return false;
	}
}
