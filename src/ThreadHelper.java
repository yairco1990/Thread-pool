
/**
 * 
 * this class will help to move the threadsUnits from working queue to resting
 * queue
 */
public class ThreadHelper extends Thread {
	private volatile boolean isFinish;
	private MyQueueGen<ThreadUnit> workingThreads = new MyQueueGen<ThreadUnit>();
	private MyQueueGen<ThreadUnit> restingThreads = new MyQueueGen<ThreadUnit>();
	private ThreadManager tm;

	public ThreadHelper(MyQueueGen<ThreadUnit> workingThreads,
			MyQueueGen<ThreadUnit> restingThreads, ThreadManager tm) {
		this.workingThreads = workingThreads;
		this.restingThreads = restingThreads;
		this.tm = tm;
		isFinish = false;
	}

	@Override
	public void run() {
		while (!isFinish) {
			System.out.println(isFinish);
			// check if the Thread unit is free to work
			// if it is - move it to the resting queue
			if (!workingThreads.isEmpty()
					&& workingThreads.peek().isFreeToWork()) {
				restingThreads.add(workingThreads.poll());
				// after moving thread to the working pool - wake up the thread
				// manager
				synchronized (tm) {
					System.out.println("RELEASE THREAD MANAGER!");
					tm.notify();
				}
				System.out
						.println("ThreadUnit finish to work, moved to restingThreads and wait for task");
			} else {
				synchronized (this) {
					try {
						System.out.println("THREAD HELPER WAIT!");
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.err.println("Thread Helper dead!");
	}

	/* GETTERS AND SETTERS */
	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

}
