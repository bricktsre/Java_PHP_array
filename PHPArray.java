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
	
	public void put(String s, V data) {
		if(s.equals("")||data == null) return;
		Node<V> n = putNode(s,data);
		if(length/hashtable.length>=0.5)
			resizeHashTable();
		hashtable[getLocation(n,hashtable)]=n;
		length++;
		
	}
	
	public void put(int s, V data) {
		if(data == null) return;
		Node<V> n = putNode(Integer.toString(s),data);
		if(length/hashtable.length>=0.5)
			resizeHashTable();
		hashtable[getLocation(n,hashtable)]=n;
		length++;
		
	}

	private void resizeHashTable() {
		hashnumber*=2;
		@SuppressWarnings("unchecked")
		PHPArray.Node<V>[] temp = (Node<V>[]) new Node<?>[hashnumber];
		for(int i =0;i<hashtable.length;i++) {
			if(hashtable[i]!=null)
				temp[getLocation(hashtable[i],temp)]=hashtable[i];
		}
		hashtable=temp;
	}
	
	private int getLocation(Node<V> n, Node<V>[] table) {
		int index = n.key.hashCode()& table.length;
		while(table[index]==null) {
			index++;
			if(index>=table.length)
				index=0;
		}
		return index;
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
