public class UserThread {
	public static void main(String[] args) {
		int[] nArr = { 10, 40, 1111 };
		int[] lArr = { 7, 6, 5 };
		int[] mArr = { 8, 12, 6 };
		solution(3, 3, nArr, lArr, mArr, 3, 4, 3);
	}

	public static void solution(int k, int r, int[] nArr, int[] lArr,
			int[] mArr, int t, final int s, final int m) {
		Object lockUser = new Object();
		final Result result1 = new Result(nArr.length, lockUser);
		final Result result2 = new Result(lArr.length, lockUser);
		final Result result3 = new Result(mArr.length, lockUser);
		Object lock = new Object();
		
		ThreadManager tm = new ThreadManager(1, t, lock);
		MyQueueGen<Task> tasks = new MyQueueGen<Task>();
		// set tasks to 1.1
		for (int i = 0; i < nArr.length; i++) {
			// for report to the relevant part result
			final int indexToReport = i;
			// check how many tasks in one [n1,nk]
			int numOfParts;
			if (nArr[i] % m == 0)
				numOfParts = nArr[i] / m;
			else
				numOfParts = nArr[i] / m + 1;
			// check if need to divide one of the tasks
			final int restOfTasks = nArr[i] % m;
			final int numOfPartsToReport = numOfParts;
			for (int j = 0; j < numOfParts; j++) {
				final int power = j;
				if (numOfParts - 1 != j) {
					tasks.add(new Task() {
						@Override
						public void doTask() {
							for (int l = 0; l < m; l++) {
								result1.setRes((Math.pow(-1, power))
										/ (2 * power + 1), indexToReport);
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
										/ (2 * power + 1), indexToReport);
							}
							result1.report(indexToReport, numOfPartsToReport);
						}
					});
				}
			}
		}
		// set tasks to 1.2.1
		for (int i = 0; i < lArr.length; i++) {
			// for report to the relevant part result
			final int indexToReport = i;
			// check how many tasks in one [n1,nk]
			int numOfParts;
			if (lArr[i] % m == 0)
				numOfParts = lArr[i] / m;
			else
				numOfParts = lArr[i] / m + 1;
			// check if need to divide one of the tasks
			final int restOfTasks = lArr[i] % m;
			final int numOfPartsToReport = numOfParts;
			for (int j = 0; j < numOfParts; j++) {
				final int power = j;
				if (numOfParts - 1 != j) {
					tasks.add(new Task() {
						@Override
						public void doTask() {
							for (int l = 0; l < m; l++) {
								result2.setRes((Math.pow(-1, 3 * power))
										/ (2 * (power + 1) + 1), indexToReport);
							}
							result2.report(indexToReport, numOfPartsToReport);
						}
					});
				} else {
					tasks.add(new Task() {
						@Override
						public void doTask() {
							for (int l = 0; l < restOfTasks; l++) {
								result2.setRes((Math.pow(-1, power))
										/ (2 * power + 1), indexToReport);
							}
							result2.report(indexToReport, numOfPartsToReport);
						}
					});
				}
			}
		}
		// set tasks to 1.2.2
		for (int i = 0; i < mArr.length; i++) {
			// for report to the relevant part result
			final int indexToReport = i;
			// check how many tasks in one [n1,nk]
			int numOfParts;
			if (mArr[i] % s == 0)
				numOfParts = mArr[i] / s;
			else
				numOfParts = mArr[i] / s + 1;
			// check if need to divide one of the tasks
			final int restOfTasks = mArr[i] % s;
			final int numOfPartsToReport = numOfParts;
			for (int j = 0; j < numOfParts; j++) {
				final int power = j;
				if (numOfParts - 1 != j) {
					tasks.add(new Task() {
						@Override
						public void doTask() {
							for (int l = 0; l < s; l++) {
								result3.setRes((Math.pow(-1, 3 * power))
										/ (2 * (power + 1) + 1), indexToReport);
							}
							result3.report(indexToReport, numOfPartsToReport);
						}
					});
				} else {
					tasks.add(new Task() {
						@Override
						public void doTask() {
							for (int l = 0; l < restOfTasks; l++) {
								result3.setRes((Math.pow(-1, power))
										/ (2 * power + 1), indexToReport);
							}
							result3.report(indexToReport, numOfPartsToReport);
						}
					});
				}
			}
		}
		final Result result4 = new Result(r, lockUser);
		for (int i = 0; i < r; i++) {
			// for report to the relevant part result
			final int indexToReport = i;
			// check how many tasks in one [n1,nk]
			int numOfParts;
			if (mArr[i] % s == 0)
				numOfParts = mArr[i] / s;
			else
				numOfParts = mArr[i] / s + 1;
			// check if need to divide one of the tasks
			final int restOfTasks = mArr[i] % s;
			final int numOfPartsToReport = numOfParts;
			for (int j = 0; j < numOfParts; j++) {
				final int power = j;
				if (numOfParts - 1 != j) {
					tasks.add(new Task() {
						@Override
						public void doTask() {
							for (int l = 0; l < s; l++) {
								result4.setRes((Math.pow(-1, 3 * power))
										/ (2 * (power + 1) + 1), indexToReport);
							}
							result4.report(indexToReport, numOfPartsToReport);
						}
					});
				} else {
					tasks.add(new Task() {
						@Override
						public void doTask() {
							for (int l = 0; l < restOfTasks; l++) {
								result4.setRes((Math.pow(-1, power))
										/ (2 * power + 1), indexToReport);
							}
							result4.report(indexToReport, numOfPartsToReport);
						}
					});
				}
			}
		}
		Feeder feeder = new Feeder(tasks, tm, lock);
		int counter = 0;
		synchronized (lockUser) {
			try {
				tm.start();
				feeder.start();
				// System.err.println("USER WAIT NOW!!!");
				while (counter != 3) {
					lockUser.wait();
					counter++;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		tm.setFinished(true);
		for (int j = 0; j < nArr.length; j++) {
			System.out.println("Expr. type (1.1), n = " + nArr[j] + ": "
					+ result1.getRes(j));
		}
		System.out.println();
		for (int j = 0; j < lArr.length; j++) {
			System.out.println("Expr. type (1.2), m = " + lArr[j] + ": "
					+ result2.getRes(j));
		}
	}
}
