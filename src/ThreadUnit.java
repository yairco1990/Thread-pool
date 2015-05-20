public class ThreadUnit extends Thread {
	private volatile String calcType;
	private volatile boolean finish;
	private volatile boolean freeToWork;
	private volatile Task task;
	public static int id = 1;
	public static int timeToSleep = 500;
	private ThreadHelper th;

	public ThreadUnit(ThreadHelper th) {
		freeToWork = true;
		finish = false;
		this.th = th;
	}

	@Override
	public void run() {
		try {
			// wait for the first task
			synchronized (this) {
				this.wait();
			}
			// thread will running as long as finish will be false
			while (!finish) {
				// check if there is task to do
				if (task != null) {
					task.doTask(1, 5);
				}
				// wait until get a new task
				synchronized (this) {
					this.wait();
				}
				// wake up thread helper after finish the task for moving him to
				// the resting pool
				synchronized (th) {
					th.notify();
				}
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	/* GETTERS AND SETTERS */
	public String getCalcType() {
		return calcType;
	}

	public void setCalcType(String calcType) {
		this.calcType = calcType;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public boolean isFreeToWork() {
		return freeToWork;
	}

	public void setFreeToWork(boolean isFreeToWork) {
		this.freeToWork = freeToWork;

	}

	public void setFreeToWorkAndTask(boolean freeToWork, Task t) {
		this.task = t;
		this.freeToWork = freeToWork;
	}
}
