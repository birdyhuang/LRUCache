/**
 * Created by peng on 5/18/16.
 */

import junit.framework.*;
import org.junit.Test;
import org.junit.After;
//import static org.junit.Assert.*;

public class TestLruCache extends TestCase {
    public Object dInput;
    public Object dOutput;
    public LruCache cache;

    @After
    public void tearDown() {
        cache = null;
    }

    @Test
    public void testGeneral() {
        cache = new LruCache<Integer, String>(15);
        for (int i = 1; i <= 20; i++) {
            cache.set(i, i);
        }
        System.out.println();
//        LruCache.printCache(cache);
        String result = LruCache.getByString(cache);
        dOutput = "6; 7; 8; 9; 10; 11; 12; 13; 14; 15; 16; 17; 18; 19; 20; ";
        assertEquals(result, dOutput);
    }
}
