public class UserThread {
	public static void main(String[] args) {
		int[] nArr = { 10};
		int[] lArr = { 7, 6, 5 };
		int[] mArr = { 8, 12, 6 };
		solution(3, 3, nArr, lArr, mArr, 3, 4, 3);
	}

	public static void solution(int k, int r, int[] nArr, int[] lArr,
			int[] mArr, int t, int s, final int m) {
		Object lockUser = new Object();
		final Result result1 = new Result(nArr.length, lockUser);
		final Result result2 = new Result(lArr.length, lockUser);
		final Result result3 = new Result(mArr.length, lockUser);
		Object lock = new Object();

		ThreadManager tm = new ThreadManager(1, t, result1, result2, result3,
				lock);
		MyQueueGen<Task> tasks = new MyQueueGen<Task>();
		for (int i = 0; i < nArr.length; i++) {
			//for report to the relevant part result
			final int indexToReport = i;
			//check how many tasks in one [n1,nk]
			int numOfParts;
			if (nArr[i] % m == 0)
				numOfParts = nArr[i] / m;
			else
				numOfParts = nArr[i] / m + 1;
			//check if need to divide one of the tasks
			final int restOfTasks = nArr[i]%m;
			final int numOfPartsToReport = numOfParts;
			for (int j = 0; j < numOfParts; j++) {
				final int power = j;
				if (numOfParts - 1 != j) {
					tasks.add(new Task() {
						@Override
						public void doTask() {
							for (int l = 0; l < m; l++) {
								result1.setRes((Math.pow(-1, power))
										/ (2 * power + 1));
							}
							result1.report(indexToReport, numOfPartsToReport);
						}
					});
				} else {
					tasks.add(new Task() {
						@Override
						public void doTask() {
							for (int l = 0; l < restOfTasks; l++) {
								result1.setRes((Math.pow(-1, power))
										/ (2 * power + 1));
							}
							result1.report(indexToReport, numOfPartsToReport);
						}
					});
				}
			}
		}
		Feeder feeder = new Feeder(tasks, tm, lock);
		synchronized (lockUser) {
			try {
				tm.start();
				feeder.start();
				System.err.println("USER WAIT NOW!!!");
				lockUser.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		tm.setFinished(true);
		System.out.println(result1.getRes());
	}
}
