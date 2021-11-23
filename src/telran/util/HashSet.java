package telran.util;

import java.util.Iterator;
import java.util.LinkedList;

import org.w3c.dom.Node;

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
				size--;
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
		
		return new HashSetIterator<T>();
	}
	
	@SuppressWarnings("hiding")
	private class HashSetIterator<T> implements Iterator<T> {
		Iterator<T> backetIterators[];
		int currentBacketInd = -1;
		int prevBacketInd;
		
		@SuppressWarnings("unchecked")
		public HashSetIterator() {
			backetIterators = new Iterator[hashTable.length];
			for(int i = 0; i < hashTable.length; i++) {
				if(hashTable[i] != null) {
					backetIterators[i] = (Iterator<T>) hashTable[i].iterator();
					if(currentBacketInd < 0) {
						currentBacketInd = i;
					}
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			return currentBacketInd < backetIterators.length &&
					backetIterators[currentBacketInd].hasNext();
		}

		@Override
		public T next() {
			T res = backetIterators[currentBacketInd].next();
			prevBacketInd = currentBacketInd;
			currentBacketInd = getNextCurrentIndex(currentBacketInd);
			return res;
		}
		
		private int getNextCurrentIndex(int currentBacketInd2) {
			if(!backetIterators[currentBacketInd2].hasNext()) {
				while(++currentBacketInd2 < backetIterators.length &&
						(isLinkedListNotExist(currentBacketInd2) || isLinkedListPassed(currentBacketInd2))) {
					
				}
			}
			return currentBacketInd2;
		}

		private boolean isLinkedListPassed(int currentBacketInd2) {
			
			return !backetIterators[currentBacketInd2].hasNext();
		}

		private boolean isLinkedListNotExist(int currentBacketInd2) {
			
			return backetIterators[currentBacketInd2] == null;
		}

		@Override
		public void remove() {
			backetIterators[prevBacketInd].remove();
			size--;
		}
	}
}
