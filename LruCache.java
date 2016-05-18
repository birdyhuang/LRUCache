/**
 * Created by peng on 5/17/16.
 */

import java.util.*;

public class LruCache<K, V> {
    public static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> prev, next;
        public Entry(K k, V v) {
            key = k;
            value = v;
            prev = null;
            next = null;
        }
    }
    private Entry<K, V> head, tail;
    private Map<K, Entry<K, V>> map;
    private int capacity, size;
    private static final int DEFAULT_SIZE = 10;

    public LruCache () {
        capacity = DEFAULT_SIZE;
        size = 0;
        head = new Entry<K, V>(null, null);
        tail = new Entry<K, V>(null, null);
        head.next = tail;
        tail.next = head;
        map = new HashMap<K, Entry<K, V>>();
    }

    public LruCache (int capacity) {
        this.capacity = capacity;
        size = 0;
        head = new Entry<K, V>(null, null);
        tail = new Entry<K, V>(null, null);
        head.next = tail;
        tail.prev = head;
        map = new HashMap<K, Entry<K, V>>();
    }

    public static <K, V> void printCache(LruCache<K, V> cache) {
        Entry<K, V> it = cache.head.next;
        while (it != cache.tail) {
            System.out.println(it.value);
            it = it.next;
        }
    }

    public static <K, V> String getByString(LruCache<K, V> cache) {
        Entry<K, V> it = cache.head.next;
        StringBuilder sb = new StringBuilder();
        while (it != cache.tail) {
            sb.append(it.value + "; ");
            it = it.next;
        }
        return sb.toString();
    }

    public V get(K key) {
        Entry<K, V> entry = map.get(key);
        if (entry != null) {
            moveToTail(entry);
            return entry.value;
        } else {
            return null;
        }
    }

    public void set(K key, V value) {
        Entry<K, V> entry = map.get(key);
        if (entry == null) {
            entry = new Entry<K, V>(key, value);
            size++;
        }
        moveToTail(entry);
        map.put(key, entry);
        checkOverFlow();
    }

    public void remove(K key) {
        Entry<K, V> entry = map.get(key);
        if (entry == null) { return; }
        entry.prev.next = entry.next;
        entry.next.prev = entry.prev;
        map.remove(key);
        size--;
    }

    private void checkOverFlow() {
        if (size > capacity) {
            remove(head.next.key);
        }
    }

    private void moveToTail(Entry<K, V> entry) {
        if (entry.next != null) {
            entry.prev.next = entry.next;
            entry.next.prev = entry.prev;
        }
        tail.prev.next = entry;
        entry.prev = tail.prev;
        entry.next = tail;
        tail.prev = entry;
    }

//    /* Used to add a new entry */
//    private void appendToTail(Entry<K, V> entry) {
//        tail.prev.next = entry;
//        entry.prev = tail.prev;
//        entry.next = tail;
//        tail.prev = entry;
//    }

    public static void main(String[] args) {
    }
}
