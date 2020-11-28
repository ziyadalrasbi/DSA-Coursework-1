package F28DA_CW1;

import java.util.Iterator;
import java.util.LinkedList;

public class ListWordMap implements IWordMap
{
	// the class to store the position and the word, didn't make it an external class chose to make it in this class
	private class WordPos 
	{
		String word;
		LinkedList<IPosition> positions;
		// the constructor
		public WordPos(String word,IPosition pos) {
			this.word = word;
			positions = new LinkedList<>();
			positions.add(pos);
		}
		 
		public WordPos(String word) {
			this.word = word;
			positions = new LinkedList<>();
		}
		
		// add function that adds a position to the linked list
		public void add(IPosition pos) {
			positions.add(pos);
		}
		
		//  
		public boolean equals(Object o) { // checking the equality of the WordPos objects using a boolean function
			if( o==null )
				return false;
			
			if( o instanceof WordPos ) { // if the object is a WordPos instance then compare it with the other WordPos object
				WordPos other = (WordPos)o;
				return word.equalsIgnoreCase(other.word);
			}
			else
				return false; // if it isn't equal then return false
		}
	}
	
	// the linked list for the word map
	private LinkedList<WordPos> wordMap;
	
	// the constructor
	public ListWordMap() {
		wordMap = new LinkedList<>();
	}

	@Override
	// if statement to check if word isn't or is in the map
	public void addPos(String word, IPosition pos) {
		if( wordMap.contains(new WordPos(word))) { // if map contains the word then the position won't be added
			int index = wordMap.indexOf(new WordPos(word));
			wordMap.get(index).add(pos);
		}
		else
			wordMap.add(new WordPos(word, pos)); // otherwise both the word and the position will be added
	}

	@Override
	public void removeWord(String word) throws WordException {
		if( wordMap.contains(new WordPos(word))) { // if map contains the word then it will get removed
			int index = wordMap.indexOf(new WordPos(word)); // index variable for the index of the word
			wordMap.remove(index); // removing the index from the map
		}
		else
			throw new WordException("Word " + word + " does not exist in the map."); // exception if word isn't present
	}

	@Override
	public void removePos(String word, IPosition pos) throws WordException {
		if( wordMap.contains(new WordPos(word))) { // if map contains the word then it will get removed
			int index = wordMap.indexOf(new WordPos(word)); // index variable for the index of the word
			wordMap.get(index).positions.remove(pos); // removing the position from the map
		}
		else
			throw new WordException("Word " + word + " does not exist in the map."); // exception if word isn't present
	}

	@Override
	public Iterator<String> words() {
		return new Iterator<String>() { // return function which returns a type of Iterator<String>
			int index = 0; // creating index variable and setting it to 0
			
			@Override
			public boolean hasNext() { // initialising my iterator function hasNext which checks the map size with the index
				return index < wordMap.size();
			}

			@Override
			public String next() { // initialising the next function which returns the words
				String word = wordMap.get(index).word;
				index++;
				return word;
			}
		};
	}

	@Override
	public Iterator<IPosition> positions(String word) throws WordException {
		if( wordMap.contains(new WordPos(word))) { // checking if word is in the map
			int index = wordMap.indexOf(new WordPos(word)); // making the index the index of the word
			LinkedList<IPosition> p = wordMap.get(index).positions; // making a new LinkedList of type IPosition which stores the positions of the word map
			return p.iterator(); // returning all the positions of the map using the iterator
		}
		else
			throw new WordException("Word " + word + " does not exist in the map."); // throws exception if the word isnt present
	}

	@Override
	public int numberOfEntries() {
		return wordMap.size(); // returns the size of the word map
	}

}
