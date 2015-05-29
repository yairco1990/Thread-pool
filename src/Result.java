public class Result {
	private double res;
	private int []resultsCounter;
	public static int id = 1;
	Object lockUser;

	public Result(int length, Object lockUser) {
		resultsCounter = new int[length];
		this.lockUser = lockUser;
	}

	public synchronized void setRes(double x) {
		//System.out.println("num of setRes " + id++);
		this.res = this.res + x;
	}

	public double getRes() {
		return this.res;
	}
	
	public synchronized void report(int index, int numOfParts){
		System.err.println(resultsCounter[index]);
		resultsCounter[index]++;
		if(resultsCounter[index] == numOfParts){
			synchronized (lockUser) {
				lockUser.notify();
			}
		}
	}
}
