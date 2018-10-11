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
