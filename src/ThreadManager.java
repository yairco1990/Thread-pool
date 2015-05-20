import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadManager extends Thread {
	private int numberOfThreads;
	private int numberOfTasks;
	private volatile boolean isFinished;
	private Queue<Task> tasks = new LinkedList<>();
	private Queue<ThreadUnit> workingThreads = new ConcurrentLinkedQueue<ThreadUnit>();
	private Queue<ThreadUnit> restingThreads = new ConcurrentLinkedQueue<ThreadUnit>();
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
		// init the ThreadHelper
		th = new ThreadHelper(workingThreads, restingThreads, this);

		// init all the threads
		for (int i = 0; i < numberOfThreads; i++) {
			restingThreads.add(new ThreadUnit(th));
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

		// start to working in a loop until finish all the calculates
		while (!this.isFinished) {
			// if all the threads working - wait.
			if (restingThreads.isEmpty()) {
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			// check if there are available threads in resting queue
			// and send them tasks
			if (!restingThreads.isEmpty()) {
				// check if there are available tasks to do
				if (!tasks.isEmpty()) {
					// System.out.println("There are tasks available [" +
					// tasks.peek() + "]");
					ThreadUnit tu = restingThreads.poll();
					workingThreads.add(tu);
					tu.setFreeToWorkAndTask(false, tasks.poll());
					//wake up feeder to get more tasks
					synchronized (lock) {
						lock.notify();
					}
					//wake up the thread unit to do the task
					synchronized (tu) {
						tu.notify();
					}
				}
			}
		}
		th.setFinish(true);

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
		System.out.println("MANAGER FINISH");
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
	public boolean setTaskFromFeeder(Task t) {
		if (numberOfTasks > tasks.size()) {
			System.out.println(t);
			tasks.add(t);
			return true;
		}
		return false;
	}
}
