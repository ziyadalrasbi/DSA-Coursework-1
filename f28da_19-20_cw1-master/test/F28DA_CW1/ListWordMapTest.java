package F28DA_CW1;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Test;



public class ListWordMapTest {
	
	@Test
	public void test1() {
		ListWordMap m = new ListWordMap();
		String word = "hello";
		String file = "file1";
		int line = 1;
		WordPosition pos = new WordPosition(word, line, file);
		m.addPos(word, pos);
		assertTrue(m.numberOfEntries() == 1);
	}
	
	@Test
	public void test2() {
		ListWordMap m = new ListWordMap();
		String word = "hello";
		String file = "file2";
		int line = 2;
		WordPosition pos = new WordPosition(file, line, word);
		m.addPos(word, pos);
		try {
			m.removeWord("other");
			fail();
		} catch (WordException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void test3() {
		
		ListWordMap m = new ListWordMap();
		String word = "hello";
		String file = "file3";
		int line = 3;
		WordPosition pos = new WordPosition(file, line, word);
		m.addPos(word, pos);
		Iterator<IPosition> possOut;
		try {
			possOut = m.positions(word);
			IPosition posOut = possOut.next();
			assertTrue(posOut.getFileName().equals(file) && posOut.getLine() == line);
		} catch (WordException e) {
			fail();
		}
	}
	
	@Test
	public void test4() throws WordException {
		ListWordMap m = new ListWordMap();
		String word;
		int line;
		String file;
		WordPosition pos;
		for (int k = 0; k < 100; k++) {
			word = "w" + k;
			line = k + 1;
			file = "f" + k;
			pos = new WordPosition(file, line, word);
			m.addPos(word, pos);
		}
		Iterator<String> it = m.words();
		int count = 0;

		while (it.hasNext()) {
			count++;
			it.next();
		}
		assertEquals(m.numberOfEntries(),100);
		assertEquals(count,100);
	}
	
	public void test5() {
		try	{
			
			ListWordMap m = new ListWordMap();
			String word;
			int line;
			String file;
			WordPosition pos;
			for (int k = 0; k < 200; ++k) {
				word = "w" + k;
				line = k + 1;
				file = "f" + k;
				pos = new WordPosition(file, line, word);
				m.addPos(word, pos);
			}
			assertEquals(m.numberOfEntries(), 200);
			for ( int k = 0; k < 200; ++k )
			{
				word = "w" + k;
				line = k + 1;
				file = "f" + k;
				pos = new WordPosition(file, line, word);
				m.removePos(word, pos);
			}
			assertEquals(m.numberOfEntries(), 0);
			for (int k = 0; k < 200; ++k) {
				word = "w" + k;
				try {
					m.positions(word);
					fail();
				} catch (WordException e) {
				}
			}
		} catch (WordException e) {
			fail();
		}
	}
	
	@Test
	public void signatureTest() {
        try {
            IWordMap map = new ListWordMap();
            String word1 = "test1";
            String word2 = "test2";
            IPosition pos1 = new WordPosition("test.txt", 4, word1);
            IPosition pos2 = new WordPosition("test.txt", 5, word2);      
            map.addPos(word1, pos1);
            map.addPos(word2, pos2);
            map.words();
            map.positions(word1);
            map.numberOfEntries();
            map.removePos(word1, pos1);
            map.removeWord(word2);
        } catch (Exception e) {
            fail("Signature of solution does not conform");
        }
	}

}
