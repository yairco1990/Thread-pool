import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author Yair this class will help to move the threadsUnits from working queue
 *         to resting queue
 */
public class ThreadHelper extends Thread {
	private volatile boolean isFinish;
	private Queue<ThreadUnit> workingThreads = new ConcurrentLinkedQueue<ThreadUnit>();
	private Queue<ThreadUnit> restingThreads = new ConcurrentLinkedQueue<ThreadUnit>();
	private ThreadManager tm;

	public ThreadHelper(Queue<ThreadUnit> workingThreads,
			Queue<ThreadUnit> restingThreads, ThreadManager tm) {
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
			// TODO not working always
			if (!workingThreads.isEmpty()
					&& workingThreads.peek().getState() == Thread.State.WAITING) {
				restingThreads.add(workingThreads.poll());
				// after moving thread to the working pool - wake up the thread
				// manager
				synchronized (tm) {
					tm.notify();
				}
				System.out
						.println("ThreadUnit finish to work, moved to restingThreads and wait for task");
			} else {
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	/* GETTERS AND SETTERS */
	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

}
