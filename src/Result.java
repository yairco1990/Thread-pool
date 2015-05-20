public class Result {
	private double res;
	public static int id = 1;

	public Result() {

	}

	public synchronized void setRes(double x) {
		//System.out.println("num of setRes " + id++);
		this.res = this.res + x;
	}

	public double getRes() {
		return this.res;
	}
}
