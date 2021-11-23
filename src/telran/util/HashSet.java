package telran.util;

import java.util.Iterator;
import java.util.LinkedList;

public class HashSet<T> extends AbstractSet<T> {
private static final int DEFAULT_ARRAY_LENGTH = 16;
private static final float FACTOR = 0.75f;
LinkedList<T> hashTable[];
@SuppressWarnings("unchecked")
public HashSet (int arrayLength) {
	hashTable = new LinkedList[arrayLength];
}
public HashSet() {
	this(DEFAULT_ARRAY_LENGTH);
}
	@Override
	public boolean add(T obj) {
		boolean res = false;
		if(!contains(obj)) {
			res = true;
			size++;
			if(size > FACTOR * hashTable.length) {
				recreateHashTable();
			}
			int index = getHashTableIndex(obj);
			if(hashTable[index] == null) {
				hashTable[index] = new LinkedList<>();
			}
			hashTable[index].add(obj);
		}
		return res;
	}

	private int getHashTableIndex(T obj) {
		int hashCode = obj.hashCode();
		int res = Math.abs(hashCode) % hashTable.length;
		return res;
	}
	
	private void recreateHashTable() {
		HashSet<T> tmpSet = new HashSet<>(hashTable.length * 2);
		for (LinkedList<T> backet: hashTable) {
			if (backet != null) {
				for (T obj: backet) {
					tmpSet.add(obj);
				}
			}
		}
		hashTable = tmpSet.hashTable;
		tmpSet = null;
	}
	
	@Override
	public T remove(T pattern) {
		int index = getHashTableIndex(pattern);
		T res = null;
		if(hashTable[index] != null) {
			int indObj = hashTable[index].indexOf(pattern);
			if(indObj >= 0) {
				res = hashTable[index].remove(indObj);
			}
		}
		return res;
	}
	
	@Override
	public boolean contains(T pattern) {
		boolean res = false;
		int htIndex = getHashTableIndex(pattern);
		if (hashTable[htIndex] != null) {
			res = hashTable[htIndex].contains(pattern);
		}
		return res;
	}

	@Override
	public Iterator<T> iterator() {	
		return new HashSetIterator();
	}
	
	private class HashSetIterator implements Iterator<T> {

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public T next() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void remove() {
			//TODO
		}
		
	}

}
