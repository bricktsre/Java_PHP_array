
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

	/**
	 * Deletes the node with the argument key. If key isn't found it does nothing.
	 * Rehashes any keys clustered after the deleted key.
	 * 
	 * @param s	the key of key,value pair to delete
	 */
	public void unset(String s) {
		if (get(s) == null)				//If key isn't in the table do nothing
			return;		
		int index = (s.hashCode()& 0x7fffffff) % hashnumber;		//start looking for the key at this index
		while (!hashtable[index].key.equals(s))						//Iterate through the table looking for the matching key
			index = (index + 1) % hashnumber;

		deleteNode(index);									//Delete the node holding the key,value pair
		hashtable[index]=null;								//Remove the reference to the node in the hashtable
		length--;											

		index = (index + 1) % hashnumber;
		while (hashtable[index] != null) {					//Goes through the cluster after the found key and rehash
			Node<V> n = hashtable[index];
			hashtable[index]=null;
			System.out.println("Key: "+n.key+" rehashed...");
			hashtable[getLocation(n,hashtable)]=n;
			index = (index + 1) % hashnumber;
		}
	}

	/**
	 * Same as above but the key is inputed as an int and must be converted to a string.
	 * 
	 * @param a key of the key,value pair to delete
	 */
	public void unset(int a) {
		unset(Integer.toString(a));
	}
	
	/**
	 * Deletes a node from the linked list stored in the hashtable. Changes the previous and next pointers
	 * of the nodes before and after it.
	 * 
	 * @param index the index of the node in the hashtable
	 */
	private void deleteNode(int index) {
		if(hashtable[index].previous==null) {					//Node is front of the list
			hashtable[index].next.previous=null;
			front=hashtable[index].next;
			return;
		}else
			hashtable[index].next.previous=hashtable[index].previous;
		if(hashtable[index].next==null) {						//Node is end of the list
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

	/**
	 * Returns a Pair object created from the current node and advances the node down the list.
	 * Very similar to an iterator
	 * 
	 * @return	Pair object created from the node or null if the current node is null
	 */
	public Pair<V> each() {
		if (eachNode == null)
			return null;
		Pair<V> p = new Pair<V>(eachNode.key, eachNode.value);
		eachNode = eachNode.next;
		return p;
	}

	/**
	 * Resets the iteration of each back to the head of the list
	 */
	public void reset() {
		eachNode = front;
	}

	/**
	 * Prints out the contents of the entire hashtable
	 */
	public void showTable() {
		System.out.println("Raw Hash Table Contents:");
		for (int i = 0; i < hashtable.length; i++) {
			if (hashtable[i] != null)
				System.out.println(i + ": Key: " + hashtable[i].key + " Value: " + hashtable[i].value);
			else
				System.out.println(i + ": null");
		}
	}

	/**
	 * Sorts the linkedlist using quicksort. Values of the keys are changed to 0-(length-1) and rehashed
	 */
	@SuppressWarnings("unchecked")
	public void sort() {
		_quickSort(front, last);					//Sorting of the list
		hashtable = (Node<V>[]) new Node<?>[hashnumber];
		Node<V> current = front;
		for (int i = 0; i < length; i++) {			//Reassignment of keys and rehashing
			current.key = Integer.toString(i);
			hashtable[getLocation(current,hashtable)] = current;
			current = current.next;
		}
	}

	/**
	 * Sort the linkedlist but does not change the keys
	 */
	public void asort() {
		_quickSort(front, last);
	}

	/**
	 * Partioning needed for quicksort
	 * 
	 * @param l	first node in the partition
	 * @param h pivot node being the last value in the partition
	 * @return pivot node after partitioning
	 */
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

	/**
	 * A recursive implementation of quicksort for linked list
	 * 
	 * @param l	head node
	 * @param h	tail node
	 */
	private void _quickSort(Node<V> l, Node<V> h) {
		if (h != null && l != h && l != h.next) {
			Node<V> temp = partition(l, h);
			_quickSort(l, temp.previous);
			_quickSort(temp.next, h);
		}
	}

	/**
	 * Returns an arraylist of all the keys
	 * 
	 * @return arraylist of keys
	 */
	public ArrayList<String> keys() {
		ArrayList<String> list = new ArrayList<String>(length);
		Node<V> current = front;
		for (int i = 0; i < length; i++) {
			list.add(current.key);
			current = current.next;
		}
		return list;
	}

	/**
	 * Returns an arraylist of the values
	 * 
	 * @return arraylist of values
	 */
	public ArrayList<V> values() {
		ArrayList<V> list = new ArrayList<V>(length);
		Node<V> current = front;
		for (int i = 0; i < length; i++) {
			list.add(current.value);
			current = current.next;
		}
		return list;
	}

	/**
	 * Flips the keys and values inside each node
	 * 
	 * @return new PHPArray of the flipped array
	 */
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
	
	/**
	 * Creates and returns an iterator
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Iterator<V> iterator() {
		return new ListIterator(front);
	}

	/**
	 * Node classes to make a linkedlist and hold the key,value pairs
	 * @author Greg
	 *
	 * @param <V>
	 */
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

	/**
	 * Pair class that goes along with each method
	 * @author Greg
	 *
	 * @param <V>
	 */
	public static class Pair<V> {
		String key;
		V value;

		public Pair(String s, V value) {
			key = s;
			this.value = value;
		}
	}

	/**
	 *Iterator for the linkedlist
	 * @author Greg
	 *
	 * @param <V>
	 */
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
=======
	private Node<V> front,last;
	private PHPArray.Node<V>[] hashtable;
	private int length;
	private int hashnumber;
	
	@SuppressWarnings("unchecked")
	public PHPArray(){
		hashtable = (Node<V>[]) new Node<?>[1];
		length =0;
		hashnumber =1;
	}
	
	public void put(String s, V data) {
		if(s.equals("")||data == null) return;
		Node<V> n = putNode(s,data);
		if(length/hashtable.length>=)
			resizeHashTable();
		
	}
	
	private Node<V> putNode(String s, V data){
		Node<V> n = new Node<V>(s,data);
		if(front==null) {
			front = n;
			last = n;
		}else {
			last.next =n;
			last =n;
		}
		length++;
		return n;
	}
	
	@Override
	public Iterator<V> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static class Node<V>{
		String key;
		V data;
		Node next;
		
		public Node(String k, V data) {
			key=k;
			this.data=data;
		}
	}
}
