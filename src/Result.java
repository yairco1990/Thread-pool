public class Result {
	private double res;
	private int []resultsCounter;
	private double []results;
	public int id = 0;
	Object lockUser;

	public Result(int length, Object lockUser) {
		resultsCounter = new int[length];
		results = new double[length];
		this.lockUser = lockUser;
	}

	public synchronized void setRes(double x, int index) {
		this.res = this.res + x;
		results[index] *= x;
	}

	public double getRes(int index) {
		return results[index];
	}
	
	public synchronized void report(int index, int numOfParts){
		//System.err.println(resultsCounter[index]);
		resultsCounter[index]++;
		//check if finish one calc
		if(resultsCounter[index] == numOfParts){
			id++;
		}
		//check if finish all the calcs of k
		if(id == results.length){
			synchronized (lockUser) {
				lockUser.notify();
			}
		}
	}
}
