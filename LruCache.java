/**
 * Created by peng on 5/17/16.
 */

import java.util.*;
import java.util.concurrent.locks.*;

public class LruCache<K, V> {
    public static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> prev, next;
        public Entry(K k, V v) {
            key = k;
            value = v;
            prev = null;
            next = null;
        }
        public void setKey(K key) {
            this.key = key;
        }
        public void setValue(V value) {
            this.value = value;
        }
        public K getKey() {
            return key;
        }
        public V getValue() {
            return value;
        }
        public void setPrev(Entry<K, V> prev) {
            this.prev = prev;
        }
        public void setNext(Entry<K, V> next) {
            this.next = next;
        }
        public Entry<K, V> getPrev() {
            return prev;
        }
        public Entry<K, V> getNext() {
            return next;
        }
    }
    private Entry<K, V> head, tail;
    private Map<K, Entry<K, V>> map;
    private int capacity, size;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final int DEFAULT_SIZE = 10;

    public LruCache () {
        capacity = DEFAULT_SIZE;
        size = 0;
        head = new Entry<K, V>(null, null);
        tail = new Entry<K, V>(null, null);
        head.setNext(tail);
        tail.setPrev(head);
        map = new HashMap<K, Entry<K, V>>();
    }

    public LruCache (int capacity) {
        if (capacity <= 0) {
            System.out.println("Capacity must be larger than 0");
            System.exit(1);
        }
        this.capacity = capacity;
        size = 0;
        head = new Entry<K, V>(null, null);
        tail = new Entry<K, V>(null, null);
        head.setNext(tail);
        tail.setPrev(head);
        map = new HashMap<K, Entry<K, V>>();
    }

    public void printCache() {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            Entry<K, V> it = head.getNext();
            while (it != tail) {
                System.out.println(it.value);
                it = it.getNext();
            }
        } finally {
            readLock.unlock();
        }
    }

    public  String getByString() {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            Entry<K, V> it = head.getNext();
            StringBuilder sb = new StringBuilder();
            while (it != tail) {
                sb.append(it.value + " ");
                it = it.getNext();
            }
            return sb.toString();
        } finally {
            readLock.unlock();
        }
    }

    public V get(K key) {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            Entry<K, V> entry = map.get(key);
            if (entry != null) {
                moveToTail(entry);
                return entry.getValue();
            } else {
                return null;
            }
        } finally {
            readLock.unlock();
        }
    }

    public void set(K key, V value) {
        Lock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            Entry<K, V> entry = map.get(key);
            if (entry == null) {
                entry = new Entry<K, V>(key, value);
                size++;
            }
            entry.setValue(value);
            moveToTail(entry);
            map.put(key, entry);
            checkOverFlow();
        } finally {
            writeLock.unlock();
        }
    }

    public void remove(K key) {
        Lock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            Entry<K, V> entry = map.get(key);
            if (entry == null) {
                throw new NoSuchElementException();
            }
            entry.getPrev().setNext(entry.getNext());
            entry.getNext().setPrev(entry.getPrev());
            map.remove(key);
            size--;
        } catch (NoSuchElementException e) {
            System.err.println("No such element");
            // do something
        } finally {
            writeLock.unlock();
        }
    }

    private void checkOverFlow() {
        if (size > capacity) {
            remove(head.getNext().getKey());
        }
    }

    private void moveToTail(Entry<K, V> entry) {
        if (entry.next != null) {
            entry.getPrev().setNext(entry.getNext());
            entry.getNext().setPrev(entry.getPrev());
        }
        tail.getPrev().setNext(entry);
        entry.setPrev(tail.getPrev());
        entry.setNext(tail);
        tail.setPrev(entry);
    }
}
