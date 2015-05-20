
class NodeCycle<T> {
	T data;
	NodeCycle<T> next;
	NodeCycle(T data, NodeCycle<T> next){
		this.data = data;
		this.next = next;
	} 
	public String toString(){
		return ""+this.data;
	}
}
public class LinkedListCycle<T> {

	private NodeCycle<T> head, tail;
	private int size;
	// Constructor,  constructs an empty list
	public LinkedListCycle(){
		head = tail = null;
		size = 0;
	} 
	// Appends the specified element to the end of this list. 
	public void add(T obj){
		if (head == null){
			head = new NodeCycle<T>(obj, null);
			tail = head;
			tail.next = head;
		}
		else{
			NodeCycle<T> n = new NodeCycle<T>(obj, head);
			tail.next = n;
			tail = n;
		}
		size++;
	} 
	
	// Removes the first occurrence of the specified element
	// from this list, if it is present. 
	public T remove(T obj){
		T ans = null;
		// empty list
		if (head == null){
			ans = null;
		}
		// remove the first element (head)
		else if (head.data.equals(obj)){
			ans = head.data;
			if (head.next == head){
				head = null;
			}
			else{
				head = head.next;
				tail.next = head;
			}
			size--;
		}
		// remove the mid or the last element 
		else{
			NodeCycle<T> prev = head, n = head.next;//find element to remove
			while(!n.data.equals(obj) && n != tail){
				prev = n;
				n = n.next;
			}
			if (n.data.equals(obj)){// the element is found
				ans = n.data;
				if (n != tail){
					prev.next = n.next;
				}
				else{
					tail = prev;
					tail.next = head;
				}
				size--;
			}
		}
		return ans;
	}
	// Returns true if this list contains the specified element
	public boolean contains(Object obj){
		// empty list
		boolean ans = false;
		if (head != null){// find the obj
			if (head.data.equals(obj)){
				ans = true;
			}
			else{
				NodeCycle<T> n = head.next;
				while (!ans && n!=head){
					if (n.data.equals(obj)) ans = true;
					else n = n.next;
				}
			}
		}
		return ans;
	}
	public String toString(){
		String s = "[";
		if (head != null){
			s = s + head.toString() + ", ";
			for (NodeCycle<T> n = head.next; n != head; n=n.next){
				s = s + n.toString() + ", ";
			}
			s = s.substring(0, s.length()-2);
		}
	return s+"]";
}
public static void main(String[] args) {
	LinkedListCycle<String> list = new LinkedListCycle<String>();
	list.add("a");
	list.add("b");
	list.add("c");
	list.add("d");
	System.out.println(list);
	System.out.println("remove x: "+list.remove("x"));
	System.out.println(list);
	System.out.println("contains a?: "+list.contains("a"));
	System.out.println("contains b?: "+list.contains("b"));
	System.out.println("contains d?: "+list.contains("d"));
	System.out.println("contains x?: "+list.contains("x"));
	System.out.println("remove c: "+list.remove("c"));
	System.out.println(list);
	System.out.println("remove a: "+list.remove("a"));
	System.out.println(list);
	System.out.println("remove d: "+list.remove("d"));
	System.out.println(list);
	System.out.println("remove d: "+list.remove("d"));
	System.out.println("contains d?: "+list.contains("d"));
	System.out.println(list);
	System.out.println("remove b: "+list.remove("b"));
	System.out.println(list);
	System.out.println("remove b: "+list.remove("b"));
	System.out.println(list);
}

}
