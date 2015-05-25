
public class MyQueueGen<T> {
	private int front, tail, size;
	private final int INIT_SIZE = 10;
	private T[] data;

	public MyQueueGen(){
		front = 0;
		tail = 0;
		size = 0;
		data = (T[])new Object[INIT_SIZE];
	}
	public MyQueueGen(int len){
		front = 0;
		tail = 0;
		size = 0;
		data = (T[])new Object[len];
	}
	// complexity O(1)
	public synchronized boolean add(T val){// add new element 
		boolean ans = true;
		if (size==data.length){
			ans = false;
			//System.out.println("queue is full");
		}
		else{
			data[tail] = val;
			tail = (tail + 1)%data.length;
			size++;		
		}
		return ans;
	}
	//complexity O(1)
	public synchronized T poll(){// delete the head of queue 
		T ans = null;
		if (size==0){
			//System.out.println("queue is empty");
		}
		else{
			ans = data[front];
			front = (front+1)%data.length;
			size--;
		}
		return ans;
	}
	
	public synchronized T peek(){// delete the head of queue 
		T ans = null;
		if (size==0){
			//System.out.println("queue is empty");
		}
		else{
			ans = data[front];
		}
		return ans;
	}
	
	// complexity O(n)
	public String toString(){
		String ans = "[";
		if (size == 0){
			ans = ans + "]";
		}
		else{
			for (int i=0; i<size-1; i++){
				ans = ans + data[(front + i)%data.length] + ", ";
			}
			ans = ans + data[(front+size-1)%data.length]  + "]";
		}
		return ans;
	}
	// complexity O(1)
	public int size(){
		return size;
	}
	// complexity O(1)
	public synchronized boolean isEmpty(){
		return size==0;
	}
}
