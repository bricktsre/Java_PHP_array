import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 */

/**
 * @author gwood
 *
 */
public class PHPArray<V> implements Iterable<V> {
	private Node<V> front, last;
	private PHPArray.Node<V>[] hashtable;
	private int length;
	private int hashnumber;
	private Node<V> eachNode;

	@SuppressWarnings("unchecked")
	public PHPArray(int capacity) {
		hashtable = (Node<V>[]) new Node<?>[capacity];
		length = 0;
		hashnumber = capacity;
		eachNode = front;
	}

	/**
	 * Put the key and data into the hashtable and linkedlist. If the key exists the
	 * value associated with it is changed to the data argument
	 * 
	 * @param s    the key of the pair
	 * @param data data of the pair
	 */
	public void put(String s, V data) {
		if (s.equals("") || data == null)
			return; // Return if input is void
		Node<V> n = putNode(s, data); // Create a node and put it into the linkedlist
		
		if (length >= hashtable.length/2)
			resizeHashTable(2); // Resize the hashtable if over half full
		int index = getLocation(n, hashtable);
		if (hashtable[index] != null) { // Update value associated with key in the linked list
			hashtable[index].value = data;
			last = last.previous; // delete the last entry in the linkedlist since it is no longer needed
			last.next=null;
			length--;
		} else
			hashtable[index] = n; // Put into the hashtable
		length++;
	}

	/**
	 * Same as the other put method but key is input as a int and converted to a
	 * string
	 * 
	 * @param s
	 * @param data
	 */
	public void put(int s, V data) {
		put(Integer.toString(s),data);
	}

	/**
	 * Resizes the hashtable by doubling its size Rehashes the nodes into the new
	 * table
	 * 
	 */
	private void resizeHashTable(int n) {
		System.out.println("\tSize: "+length+" -- resizing array from "+hashnumber+" to "+hashnumber*n);
		hashnumber *= n; // Doubles the size of table
		@SuppressWarnings("unchecked")
		PHPArray.Node<V>[] temp = (Node<V>[]) new Node<?>[hashnumber]; // Temporary node array
		for (int i = 0; i < hashtable.length; i++) {
			if (hashtable[i] != null)
				temp[getLocation(hashtable[i], temp)] = hashtable[i]; // Rehashes the nodes
		}
		hashtable = temp;
	}

	/**
	 * Finds out where the node is supposed to go in the hashtable Utilizes linear
	 * probing hashing. Hashtable is an argument so both put and resizeHashTable
	 * 
	 * @param n     node that goes into the table
	 * @param table table the node will be put in
	 * @return index in the table to be placed
	 */
	private int getLocation(Node<V> n, Node<V>[] table) {
		int index = (n.key.hashCode() & 0x7fffffff)% table.length;
		while (table[index] != null) {
			if (table[index].key == n.key)
				return index;
			index=(index+1)%table.length;
		}
		return index;
	}

	/**
	 * Create a node and put it into the linkedlist
	 * 
	 * @param s
	 * @param data
	 * @return new node in the list
	 */
	private Node<V> putNode(String s, V data) {
		Node<V> n = new Node<V>(s, data);
		if (front == null) { // Special case of list being empty
			front = n;
			last = n;
			eachNode=front;
		} else { // Normal case of tail insertion
			last.next = n;
			n.previous = last;
			last = n;
		}
		return n;
	}

	/**
	 * Get value associated with argument key by linear probing
	 * 
	 * @param s key to search for
	 * @return value of key, null if key does not exists
	 */
	public V get(String s) {
		int index = (s.hashCode()& 0x7fffffff) % hashnumber;
		while (hashtable[index] != null) {
			if (hashtable[index].key.equals(s)) // The node at index has the same key as the argument
				return hashtable[index].value;
			else
				index++;
			if (index >= hashtable.length)
				index = 0; // Wrap around to the front
		}
		return null;
	}

	/**
	 * Same as above get method with key as an int converted to a string
	 * 
	 * @param s int key
	 * @return value of key, null if key does not exists
	 */
	public V get(int s) {
		return get(Integer.toString(s));
	}

	public void unset(String s) {
		if (get(s) == null)
			return;
		int index = (s.hashCode()& 0x7fffffff) % hashnumber;
		while (!hashtable[index].key.equals(s))
			index = (index + 1) % hashnumber;

		deleteNode(index);
		hashtable[index]=null;
		length--;

		index = (index + 1) % hashnumber;
		while (hashtable[index] != null) {
			Node<V> n = hashtable[index];
			hashtable[index]=null;
			System.out.println("Key: "+n.key+" rehashed...");
			hashtable[getLocation(n,hashtable)]=n;
			index = (index + 1) % hashnumber;
		}
	}

	public void unset(int a) {
		unset(Integer.toString(a));
	}
	
	private void deleteNode(int index) {
		if(hashtable[index].previous==null) {
			hashtable[index].next.previous=null;
			front=hashtable[index].next;
			return;
		}else
			hashtable[index].next.previous=hashtable[index].previous;
		if(hashtable[index].next==null) {
			hashtable[index].previous.next=null;
			last=hashtable[index].previous;
		}else
			hashtable[index].previous.next = hashtable[index].next;
	}

	/**
	 * Returns the size of the hashtable
	 * 
	 * @return
	 */
	public int length() {
		return length;
	}

	public Pair<V> each() {
		if (eachNode == null)
			return null;
		Pair<V> p = new Pair<V>(eachNode.key, eachNode.value);
		eachNode = eachNode.next;
		return p;
	}

	public void reset() {
		eachNode = front;
	}

	public void showTable() {
		System.out.println("Raw Hash Table Contents:");
		for (int i = 0; i < hashtable.length; i++) {
			if (hashtable[i] != null)
				System.out.println(i + ": Key: " + hashtable[i].key + " Value: " + hashtable[i].value);
			else
				System.out.println(i + ": null");
		}
	}

	@SuppressWarnings("unchecked")
	public void sort() {
		_quickSort(front, last);
		hashtable = (Node<V>[]) new Node<?>[hashnumber];
		Node<V> current = front;
		for (int i = 0; i < length; i++) {
			current.key = Integer.toString(i);
			hashtable[getLocation(current,hashtable)] = current;
			current = current.next;
		}
	}

	public void asort() {
		_quickSort(front, last);
	}

	private Node<V> partition(Node<V> l, Node<V> h) {
		// set pivot as h element
		V x = h.value;

		// similar to i = l-1 for array implementation
		Node<V> i = l.previous;

		// Similar to "for (int j = l; j <= h- 1; j++)"
		for (Node<V> j = l; j != h; j = j.next) {
			if (j.compareTo(h) >= 0) {
				// Similar to i++ for array
				i = (i == null) ? l : i.next;
				V temp = i.value;
				String temp2 = i.key;
				i.value = j.value;
				i.key=j.key;
				j.value = temp;
				j.key=temp2;
			}
		}
		i = (i == null) ? l : i.next; // Similar to i++
		V temp = i.value;
		String temp2 = i.key;
		i.value = h.value;
		i.key=h.key;
		h.value = temp;
		h.key=temp2;
		return i;
	}

	/* A recursive implementation of quicksort for linked list */
	private void _quickSort(Node<V> l, Node<V> h) {
		if (h != null && l != h && l != h.next) {
			Node<V> temp = partition(l, h);
			_quickSort(l, temp.previous);
			_quickSort(temp.next, h);
		}
	}

	public ArrayList<String> keys() {
		ArrayList<String> list = new ArrayList<String>(length);
		Node<V> current = front;
		for (int i = 0; i < length; i++) {
			list.add(current.key);
			current = current.next;
		}
		return list;
	}

	public ArrayList<V> values() {
		ArrayList<V> list = new ArrayList<V>(length);
		Node<V> current = front;
		for (int i = 0; i < length; i++) {
			list.add(current.value);
			current = current.next;
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public PHPArray<String> array_flip() {
		if(!(front.value instanceof String))
			throw new ClassCastException();
		else {
			PHPArray<String> B = new PHPArray<String>(hashnumber);
			Node<V> current=front;
			while(current!=null) {
				B.put((String)current.value,current.key);
				current=current.next;
			}
			return B;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Iterator<V> iterator() {
		return new ListIterator(front);
	}

	private static class Node<V> implements Comparable<Node<V>> {
		String key;
		V value;
		Node<V> next;
		Node<V> previous;

		public Node(String k, V data) {
			key = k;
			value = data;
		}

		@Override
		public int compareTo(Node<V> n) {
			return ((Comparable) n.value).compareTo((Comparable) value);
		}
	}

	public static class Pair<V> {
		String key;
		V value;

		public Pair(String s, V value) {
			key = s;
			this.value = value;
		}
	}

	public class ListIterator<V> implements Iterator<V> {
		private Node<V> n;

		public ListIterator(Node<V> n) {
			this.n = n;
		}

		@Override
		public boolean hasNext() {
			return (n != null);
		}

		@Override
		public V next() {
			V temp = n.value;
			n = n.next;
			return temp;
		}

	}
}
