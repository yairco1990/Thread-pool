public class Result {
	private double res;
	private int[] resultsCounter;
	private double[] results;
	public int id = 0;
	Object lockUser;
	public static int[] iIterators11;
	public static int[] iIterators121;
	public static int[] iIterators122;

	public Result(int length, Object lockUser) {
		resultsCounter = new int[length];
		results = new double[length];
		for (int i = 0; i < results.length; i++) {
			results[i] = 1;
		}
		this.lockUser = lockUser;
	}

	public synchronized void setRes(double x, int index) {
		//System.out.println(x + "    " + index);
		// this.res = this.res + x;
		results[index] *= x;
	}

	public double getRes(int index) {
		return results[index];
	}

	public synchronized void report(int index, int numOfParts) {
		// System.err.println(resultsCounter[index]);
		resultsCounter[index]++;
		// check if finish one calc
		if (resultsCounter[index] == numOfParts) {
			id++;
		}
		// check if finish all the calcs of k
		if (id == results.length) {
			synchronized (lockUser) {
				lockUser.notify();
			}
		}
	}

	public static void initIterator(int length, int formula) {
		if (formula == 1) {
			iIterators11 = new int[length];
			for (int i = 0; i < iIterators11.length; i++) {
				iIterators11[i] = 1;
			}
		}
		if (formula == 2) {
			iIterators121 = new int[length];
			for (int i = 0; i < iIterators121.length; i++) {
				iIterators121[i] = 1;
			}
		} else {
			iIterators122 = new int[length];
			for (int i = 0; i < iIterators122.length; i++) {
				iIterators122[i] = 1;
			}
		}
	}
}
