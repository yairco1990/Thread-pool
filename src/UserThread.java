public class UserThread {
	private static int i11 = 1;
	private static int i121 = 1;
	private static int i122 = 1;

	public static void main(String[] args) {
		int[] nArr = { 3, 4, 5 };
		int[] lArr = { 5, 3, 7 };
		int[] mArr = { 5, 2, 5 };
		solution(3, 3, nArr, lArr, mArr, 3, 4, 3);
	}

	public static void solution(int k, int r, int[] nArr, int[] lArr,
			int[] mArr, int t, final int s, final int m) {
		Object lockUser = new Object();
		final Result result1 = new Result(nArr.length, lockUser);

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
			// counter will represent i to n indexes
			// final int counter = 0;
			Result.initIterator(nArr.length, 1);
			for (int j = 0; j < numOfParts; j++) {
				if (numOfParts - 1 != j || restOfTasks == 0) {
					tasks.add(new Task() {
						// public int x = counter + 1;
						@Override
						public void doTask() {
							int num;
							for (int l = 0; l < m; l++) {
								num = Result.iIterators11[indexToReport]++;
								result1.setRes((Math.pow(-1, num))
										/ (2 * num + 1), indexToReport);
							}
							result1.report(indexToReport, numOfPartsToReport);
						}
					});
				} else {
					tasks.add(new Task() {
						@Override
						public void doTask() {
							int num;
							for (int l = 0; l < restOfTasks; l++) {
								num = Result.iIterators11[indexToReport]++;
								result1.setRes((Math.pow(-1, num))
										/ (2 * num + 1), indexToReport);
							}
							result1.report(indexToReport, numOfPartsToReport);
						}
					});
				}
			}
		}
		final Result result2 = new Result(lArr.length, lockUser);
		Result.initIterator(lArr.length, 2);
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
							int num;
							for (int l = 0; l < m; l++) {
								num = Result.iIterators121[indexToReport]++;
								result2.setRes((Math.pow(-1, 3 * num))
										/ (2 * (num + 1) + 1), indexToReport);
							}
							result2.report(indexToReport, numOfPartsToReport);
						}
					});
				} else {
					tasks.add(new Task() {
						@Override
						public void doTask() {
							int num;
							for (int l = 0; l < restOfTasks; l++) {
								num = Result.iIterators121[indexToReport]++;
								result2.setRes((Math.pow(-1, num))
										/ (2 * num + 1), indexToReport);
							}
							result2.report(indexToReport, numOfPartsToReport);
						}
					});
				}
			}
		}
		final Result result3 = new Result(mArr.length, lockUser);
		Result.initIterator(mArr.length, 3);
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
							int num;
							for (int l = 0; l < s; l++) {
								num = Result.iIterators122[indexToReport]++;
								result3.setRes((Math.pow(-1, 3 * num))
										/ (2 * (num + 1) + 1), indexToReport);
							}
							result3.report(indexToReport, numOfPartsToReport);
						}
					});
				} else {
					tasks.add(new Task() {
						@Override
						public void doTask() {
							int num;
							for (int l = 0; l < restOfTasks; l++) {
								num = Result.iIterators122[indexToReport]++;
								result3.setRes((Math.pow(-1, num))
										/ (2 * num + 1), indexToReport);
							}
							result3.report(indexToReport, numOfPartsToReport);
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
			System.out.println("Expr. type (1.2), m = " + mArr[j] + " l = " + lArr[j] + ": "
					+ (result2.getRes(j) + result3.getRes(j)));
		}
	}
}
