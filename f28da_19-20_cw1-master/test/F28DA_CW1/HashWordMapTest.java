package F28DA_CW1;

import static org.junit.Assert.*;

import org.junit.Test;

public class HashWordMapTest {

	// Add your own tests, for example to test the method hashCode from HashWordMap
	
	// ...
	@Test
	public void test1() {
		String a = "hello";
		String b = "there";
		
		assertFalse(a.hashCode() == b.hashCode());
	}
	
	@Test
	public void test2() {
		String a = "hello";
		String b = "hello";
		
		assertTrue(a.hashCode() == b.hashCode());
	}
	
	@Test
	public void signatureTest() {
        try {
            IWordMap map = new HashWordMap(0.5f);
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
