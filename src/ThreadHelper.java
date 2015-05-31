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
			// check if the Thread unit is free to work
			// if it is - move it to the resting queue
			if (!workingThreads.isEmpty()) {
				if (workingThreads.peek().isFreeToWork()) {
					synchronized (tm) {
						restingThreads.add(workingThreads.poll());
						// after moving thread to the resting pool - wake up the
						// thread manager
//						System.out.println("RELEASE THREAD MANAGER!");
						tm.notify();
					}
//					System.out
//							.println("ThreadUnit finish to work, moved to restingThreads and wait for task");
				} else {
					// look for other free thread
					workingThreads.add(workingThreads.poll());
				}
			} else {
				// if there are resting - release thread manager to prevent
				// dead lock
				synchronized (tm) {
					tm.notify();
				}
			}
		}
//		System.err.println("Thread Helper dead!");
	}

	/* GETTERS AND SETTERS */
	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

}
