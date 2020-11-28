package F28DA_CW1;

import java.util.Iterator;
import java.util.LinkedList;

public class HashWordMap implements IWordMap, IHashMonitor {
	private class WordPos 
	{
		String word;
		LinkedList<IPosition> positions;
		boolean deleted;
		// the constructor
		public WordPos(String word,IPosition pos)
		{
			this.word = word;
			positions = new LinkedList<>();
			positions.add(pos);
			deleted = false;
		}
		
		public boolean equals(Object o)
		{
			if( o==null )
				return false;
			
			if( o instanceof WordPos )
			{
				WordPos other = (WordPos)o;
				return word.equalsIgnoreCase(other.word);
			}
			else
				return false;
		}
	}
	
	// the max load factor
	private float maxLoadFactor;
	// the array size
	private int N;
	// the table
	private WordPos table[];
	// the number of probes 
	private int numProbes;
	// the number of operations
	private int numOperations;
	
	// the constructor
	public HashWordMap()
	{
		// set the value of N to 13 to begin with
		this.N = 13;
		// set the max load factor 
		this.maxLoadFactor = 0.5F;
		// create the table of this size
		this.table = new WordPos[N];
		// set the number of probes and the number of operations
		numProbes = 0;
		numOperations = 0;
	}
	
	public HashWordMap(float maxLoadFactor)
	{
		// set the max load factor
		this.maxLoadFactor = maxLoadFactor;
		// set the value of N to 13 to begin with
		this.N = 13;
		// create the table of this size
		this.table = new WordPos[N];
		// set the number of probes and the number of operations
		numProbes = 0;
		numOperations = 0;
	}
	@Override
	public void addPos(String word, IPosition pos) {
		// get the hash value
		int hashValue = hashCode(word);
		// increment the number of probes by 1
		numProbes++;
		// if there is no word at that position
		if( table[hashValue] == null )
		{
			// create a new word at that position
			table[hashValue] = new WordPos(word, pos);
			updateLoadFactor();
		}
		// else add this value to this table
		else
		{
			// if this contains the word we are looking for, return true
			if(table[hashValue].deleted)
			{
				// set this location to not-deleted
				table[hashValue].deleted = false;
				// place the word and the pos
				table[hashValue].word = word;
				table[hashValue].positions.clear();
				table[hashValue].positions.add(pos);
				return;
			}
			
			int i = 1;
			// get the next hash value
			hashValue = (hashCode(word) + i*hashCode2(word)) % table.length;
			// increment the number of probes by 1
			numProbes++;
			
			// as long as this location is not null
			while(  table[hashValue] != null )
			{
				// if this contains the word we are looking for, return true
				if(table[hashValue].deleted)
				{
					// // set this location to not-deleted
					table[hashValue].deleted = false;
					// place the word and the pos
					table[hashValue].word = word;
					table[hashValue].positions.clear();
					table[hashValue].positions.add(pos);
					return;
				}
				// increment i
				i++;
				
				// get the next hash value
				hashValue = (hashCode(word) + i*hashCode2(word)) % table.length;
				// increment the number of probes by 1
				numProbes++;
			}
			
			// add the new word here
			table[hashValue] = new WordPos(word, pos);
			updateLoadFactor();
		}
	}
	
	// method to update load factor and rehash
	private void updateLoadFactor()
	{
		// get the current load factor
		float loadFactor = getLoadFactor();
		// if this is more than the load factor we need
		if( loadFactor > maxLoadFactor )
		{
			// rehash
			resize();
		}
	}

	@Override
	public void removeWord(String word) throws WordException {
		// get the first hash
		int hashValue = hashCode(word);
		// if this is null, return false
		if( table[hashValue] == null )
			throw new WordException("Word does not exist!");
		
		// increment the number of probes by 1
		numProbes++;
		// if this contains the word we are looking for, return true
		if(!table[hashValue].deleted && table[hashValue].word.equals(word))
		{
			// set this location to deleted
			table[hashValue].deleted = true;
			return;
		}
		
		int i = 1;
		// as long as this location is not null
		while(  table[hashValue] != null )
		{
			// get the next hash value
			hashValue = (hashCode(word) + i*hashCode2(word)) % table.length;
			
			// increment the number of probes by 1
			numProbes++;
			// if this contains the word we are looking for, return true
			if(!table[hashValue].deleted && table[hashValue].word.equals(word))
			{
				// set this to deleted
				table[hashValue].deleted = true;
				return;
			}
			// increment i
			i++;
		}
		
		throw new WordException("Word does not exist!");
	}

	@Override
	public void removePos(String word, IPosition pos) throws WordException {
		// get the first hash
		int hashValue = hashCode(word);
		// if this is null, return false
		if( table[hashValue] == null )
			throw new WordException("Word does not exists!");
		
		// increment the number of probes by 1
		numProbes++;
		// if this contains the word we are looking for, return true
		if(!table[hashValue].deleted && table[hashValue].word.equals(word))
		{
			// remove pos from here
			table[hashValue].positions.remove( table[hashValue].positions.indexOf(pos) );
			// if all positions deleted, mark this word as deleted
			if( table[hashValue].positions.size() == 0 )
				table[hashValue].deleted = true;
			return;
		}
		
		int i = 1;
		// as long as this location is not null
		while(  table[hashValue] != null )
		{
			// get the next hash value
			hashValue = (hashCode(word) + i*hashCode2(word)) % table.length;
			
			// increment the number of probes by 1
			numProbes++;
			// if this contains the word we are looking for, return true
			if(!table[hashValue].deleted && table[hashValue].word.equals(word))
			{
				// remove pos from here
				table[hashValue].positions.remove( table[hashValue].positions.indexOf(pos) );
				// if all positions deleted, mark this word as deleted
				if( table[hashValue].positions.size() == 0 )
					table[hashValue].deleted = true;
				return;
			}
			// increment i
			i++;
		}
		throw new WordException("Word does not exist!");
	}

	@Override
	public Iterator<String> words() {
		return new WordPosIterator();
	}
	
	// the String iterator
	private class WordPosIterator implements Iterator<String>
	{
		// the current index
		private int index;
		// the constructor
		public WordPosIterator()
		{
			// set the index to where the first non-null entry of the 
			index = 0;
			// as long as as index is not null
			// index must be pointing to the first non null entry
			index = getNextNonNullEntryIndex(index);
		}
		@Override
		public boolean hasNext() {
			// if there is another entry
			if( getNextNonNullEntryIndex(index) < table.length )
				return true;
			return false;
		}

		@Override
		public String next() {
			// the word to return
			String word = table[index].word;
			// set the index to next word
			index = getNextNonNullEntryIndex(index + 1);
			// return the word
			return word;
		}
		
		// method to get the next not null entry index
		private int getNextNonNullEntryIndex(int i)
		{
			// as long as i is not null
			while( i<table.length && (table[i] == null || table[i].deleted) )
			{
				i++;
			}
			return i;
		}
	}

	@Override
	public Iterator<IPosition> positions(String word) throws WordException {
		//  get the first hash
		int hashValue = hashCode(word);
		// if this is null, return false
		if( table[hashValue] == null )
			throw new WordException("Word does not exist!");
		
		// increment the number of probes by 1
		numProbes++;
		// if this contains the word we are looking for, return true
		if(!table[hashValue].deleted && table[hashValue].word.equals(word))
		{
			// return the iterator for this
			return table[hashValue].positions.iterator();
		}
		
		int i = 1;
		// get the next hash value
		hashValue = (hashCode(word) + i*hashCode2(word)) % table.length;
		// increment the number of probes by 1
		numProbes++;
		// as long as this location is not null
		while(  table[hashValue] != null && hashValue != hashCode(word) )
		{
			// if this contains the word we are looking for, return true
			if(!table[hashValue].deleted && table[hashValue].word.equals(word))
			{
				// return the iterator for this
				return table[hashValue].positions.iterator();
			}
			// increment i
			i++;
			
			// get the next hash value
			hashValue = (hashCode(word) + i*hashCode2(word)) % table.length;
			// increment the number of probes by 1
			numProbes++;
		}
		throw new WordException("Word does not exist!");
	}

	@Override
	public int numberOfEntries() {
		// calculate the number of entries
		int numEntries = 0;
		for( int i=0; i<table.length; i++)
		{
			// if this is not null, increment the count by 1
			if( table[i] != null && !table[i].deleted)
				numEntries++;
		}
		return numEntries;
	}

	@Override
	public float getMaxLoadFactor() {
		return maxLoadFactor; // returns the max load factor
	}

	@Override
	public float getLoadFactor() {
		return ((float)(numberOfEntries()))/table.length; // getting the load factor
	}

	@Override
	public float averNumProbes() {
		// if number of operations 0, return 0, otherwise do average calculation
		if( numOperations == 0 ) 
			return 0;
		return ((float)(numProbes))/numProbes;
	}

	@Override
	public int hashCode(String s) {
		int hash=0;
		int x = 31;
		// loop to get the hash of each string in a word
		for(int i=0;i<s.length();i++)
		  hash = (x*hash + s.charAt(i)) % table.length;
		return hash % table.length;
	}
	
	// method to check if this number is a prime number
	private boolean isPrime(int n)
	{
		if( n==0 )
			return false;
		if( n==2 )
			return true;
		if( n%2 == 0 )
			return false;
		
		for( int i=3; i<=Math.sqrt(n); i+=2 )
			if( n%i == 0 )
				return false;
		return true;
	}
	
	// method to get the next prime number
	private int getNextPrime(int n)
	{
		// get the next prime number at least twice this 
		int next = 2*n;
		
		// find the next prime number
		while( !isPrime(next))
			next++;
		return next;
	}

	// private method to resize the array, used in updateLoadFactor
	private void resize()
	{
		// create a new array of size
		WordPos[] newArray = new WordPos[ table.length ];
		
		// copy all the values from table to this new array
		for( int i=0; i<table.length; i++)
			newArray[i] = table[i];
		
		// create array with new size
		table = new WordPos[ getNextPrime(newArray.length )];
		
		// rehash the values into the table
		for( int i=0; i<newArray.length; i++ )
		{
			// if this is not null, add the word and all the positions in the word
			if( newArray[i] != null )
			{
				for( int j=0; j<newArray[i].positions.size(); j++ )
					addPos( newArray[i].word , newArray[i].positions.get(j));
			}
		}
	}
	
	// the secondary hash function
	private int hashCode2(String s)
	{
		int hash=0;
		int x = 41;
		for(int i=0;i<s.length();i++)
		  hash = (x*hash + s.charAt(i)) % table.length;
		return hash % table.length;
	}
}
