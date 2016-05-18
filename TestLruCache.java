/**
 * Created by peng on 5/18/16.
 */

import junit.framework.*;
import org.junit.Test;
import org.junit.After;

public class TestLruCache extends TestCase {
    public Object dInput;
    public Object dOutput;
    public LruCache cache;

    @After
    public void tearDown() {
        cache = null;
    }

    @Test
    public void testSizeControl() {
        cache = new LruCache<Integer, Integer>(15);
        for (int i = 1; i <= 20; i++) {
            cache.set(i, i);
        }
        System.out.println();
        String result = cache.getByString();
        dOutput = "6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 ";
        assertEquals(result, dOutput);
    }

    @Test
    public void testRead() {
        cache = new LruCache<Integer, Integer>(10);
        for (int i = 1; i <= 10; i++) {
            cache.set(i, i);
        }
        for (int i = 10; i >= 1; i--) {
            cache.get(i);
        }
        String result = cache.getByString();
        dOutput = "10 9 8 7 6 5 4 3 2 1 ";
        assertEquals(result, dOutput);
    }

    @Test
    public void testRemoveNotExist() {
        cache = new LruCache<Integer, Integer>();
        cache.remove(1);
    }

    @Test
    public void testReadNotExist() {
        cache = new LruCache<Integer, Integer>();
        Integer result = (Integer) cache.get(1);
        dOutput = null;
        assertEquals(result, dOutput);
    }

    @Test
    public void testGeneral() {
        cache = new LruCache<Integer, Integer>();
        cache.set(2, 1);
        cache.set(2, 2);
        cache.set(1, 2);
        String result = cache.getByString();
        dOutput = "2 2 ";
        assertEquals(result, dOutput);
    }
}
