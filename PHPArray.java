import java.util.Iterator;

/**
 * 
 */

/**
 * @author gwood
 *
 */
public class PHPArray<V> implements Iterable<V> {
	private Node<V> front,last;
	private PHPArray.Node<V>[] hashtable;
	private int length;
	private int hashnumber;
	
	@SuppressWarnings("unchecked")
	public PHPArray(int capacity){
		hashtable = (Node<V>[]) new Node<?>[capacity];
		length =0;
		hashnumber =capacity;
	}
	
	/**Put the key and data into the hashtable and linkedlist
	 * 
	 * @param s		the key of the pair
	 * @param data  data of the pair
	 */
	public void put(String s, V data) {
		if(s.equals("")||data == null) return;			//Return if input is void
		Node<V> n = putNode(s,data);					//Create a node and put it into the linkedlist
		if(length/hashtable.length>=0.5)
			resizeHashTable();							//Resize the hashtable if over half full
		hashtable[getLocation(n,hashtable)]=n;			//Put into the hashtable
		length++;
		
	}
	
	/**Same as the other put method but key is input as a int
	 * and converted to a string
	 * 
	 * @param s
	 * @param data
	 */
	public void put(int s, V data) {
		if(data == null) return;
		Node<V> n = putNode(Integer.toString(s),data);		//Integer converted to a string
		if(length/hashtable.length>=0.5)
			resizeHashTable();
		hashtable[getLocation(n,hashtable)]=n;
		length++;
		
	}
	
	/**Resizes the hashtable by doubling its size
	 * Rehashes the nodes into the new table
	 * 
	 */
	private void resizeHashTable() {
		hashnumber*=2;								//Doubles the size of table
		@SuppressWarnings("unchecked")
		PHPArray.Node<V>[] temp = (Node<V>[]) new Node<?>[hashnumber];		//Temporary node array
		for(int i =0;i<hashtable.length;i++) {
			if(hashtable[i]!=null)
				temp[getLocation(hashtable[i],temp)]=hashtable[i];			//Rehashes the nodes
		}
		hashtable=temp;
	}
	
	/**Finds out where the node is supposed to go in the hashtable
	 * Utilizes linear probing hashing. Hashtable is an argument so both put
	 * and resizeHashTable
	 * 
	 * @param n			node that goes into the table
	 * @param table		table the node will be put in
	 * @return			index in the table to be placed
	 */
	private int getLocation(Node<V> n, Node<V>[] table) {
		int index = n.key.hashCode()% table.length;
		while(table[index]==null) {
			index++;
			if(index>=table.length)			//Wrap around to the front of the table
				index=0;
		}
		return index;
	}

	/**Create a node and put it into the linkedlist
	 * 
	 * @param s
	 * @param data
	 * @return  new node in the list
	 */
	private Node<V> putNode(String s, V data){
		Node<V> n = new Node<V>(s,data);
		if(front==null) {		//Special case of list being empty
			front = n;
			last = n;
		}else {					//Normal case of tail insertion
			last.next =n;
			n.previous=last;
			last =n;
		}
		return n;
	}
	
	/**Get value associated with argument key by linear probing
	 * 
	 * @param s key to search for
	 * @return	value of key, null if key does not exists
	 */
	public V get(String s){
		int index = s.hashCode()%hashnumber;
		while(hashtable[index]!=null) {
			if(hashtable[index].key.equals(s))		//The node at index has the same key as the argument
				return hashtable[index].data;
			else
				index++;
			if(index>=hashtable.length)
				index=0;							//Wrap around to the front
		}
		return null;
	}
	
	/**Same as above get method with key as an int converted to a string
	 * 
	 * @param s		int key
	 * @return		value of key, null if key does not exists
	 */
	public V get(int s){
		int index = Integer.toString(s).hashCode()%hashnumber;	//Integer converted to string
		while(hashtable[index]!=null) {
			if(hashtable[index].key.equals(s))
				return hashtable[index].data;
			else
				index++;
			if(index>=hashtable.length)
				index=0;
		}
		return null;
	}
	
	/**Returns the size of the hashtable
	 * 
	 * @return
	 */
	public int length() {
		return hashtable.length;
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
		Node previous;
		
		public Node(String k, V data) {
			key=k;
			this.data=data;
		}
	}
	
	public static class Pair<V>{
		
	}

}
