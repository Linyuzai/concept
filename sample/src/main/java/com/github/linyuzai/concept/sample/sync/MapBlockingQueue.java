package com.github.linyuzai.concept.sample.sync;

import lombok.NonNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class MapBlockingQueue<E> extends AbstractQueue<E>
        implements BlockingQueue<E>, Serializable {

    private final int capacity;

    private final AtomicInteger count = new AtomicInteger();

    private final ReentrantLock takeLock = new ReentrantLock();

    private final Condition notEmpty = takeLock.newCondition();

    private final ReentrantLock putLock = new ReentrantLock();

    private final Condition notFull = putLock.newCondition();

    private Map<Object, E> map;

    public Map<Object, E> getMap() {
        return map;
    }

    private void signalNotEmpty() {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }

    private void signalNotFull() {
        final ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    private boolean enqueue(Object k, E v) {
        if (map.containsKey(k)) {
            map.put(k, v);
            //+0
            return false;
        } else {
            map.put(k, v);
            //+1
            return true;
        }
    }

    private E dequeue() {
        Iterator<E> iterator = map.values().iterator();
        E e = iterator.next();
        iterator.remove();
        return e;
    }

    void fullyLock() {
        putLock.lock();
        takeLock.lock();
    }

    void fullyUnlock() {
        takeLock.unlock();
        putLock.unlock();
    }

    public MapBlockingQueue() {
        this(Integer.MAX_VALUE);
    }

    public MapBlockingQueue(Map<?, ? extends E> map) {
        this(map, Integer.MAX_VALUE);
    }

    public MapBlockingQueue(int capacity) {
        this(new LinkedHashMap<>(), capacity);
    }

    @SuppressWarnings("unchecked")
    public MapBlockingQueue(Map<?, ? extends E> map, int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException();
        final ReentrantLock putLock = this.putLock;
        putLock.lock();
        this.map = (Map<Object, E>) map;
        this.capacity = capacity;
        try {
            int size = map.size();
            if (size >= capacity) {
                throw new IllegalStateException("Queue full");
            }
            count.set(size);
        } finally {
            putLock.unlock();
        }
    }

    public int size() {
        return count.get();
    }

    public int remainingCapacity() {
        return capacity - count.get();
    }

    public void put(E e) throws InterruptedException {
        put(e, e);
    }

    public void put(Object k, E v) throws InterruptedException {
        int c;
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        try {
            while (count.get() == capacity) {
                notFull.await();
            }
            if (enqueue(k, v)) {
                c = count.getAndIncrement();
            } else {
                c = count.get();
            }
            if (c + 1 < capacity)
                notFull.signal();
        } finally {
            putLock.unlock();
        }
        if (c == 0)
            signalNotEmpty();
    }

    public boolean offer(E e, long timeout, @NonNull TimeUnit unit)
            throws InterruptedException {
        return offer(e, e, timeout, unit);
    }

    public boolean offer(Object k, E v, long timeout, TimeUnit unit)
            throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        int c;
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        try {
            while (count.get() == capacity) {
                if (nanos <= 0)
                    return false;
                nanos = notFull.awaitNanos(nanos);
            }
            if (enqueue(k, v)) {
                c = count.getAndIncrement();
            } else {
                c = count.get();
            }
            if (c + 1 < capacity)
                notFull.signal();
        } finally {
            putLock.unlock();
        }
        if (c == 0)
            signalNotEmpty();
        return true;
    }

    public boolean offer(E e) {
        return offer(e, e);
    }

    public boolean offer(Object k, E v) {
        final AtomicInteger count = this.count;
        if (count.get() == capacity)
            return false;
        int c = -1;
        final ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            if (count.get() < capacity) {
                if (enqueue(k, v)) {
                    c = count.getAndIncrement();
                } else {
                    c = count.get();
                }
                if (c + 1 < capacity)
                    notFull.signal();
            }
        } finally {
            putLock.unlock();
        }
        if (c == 0)
            signalNotEmpty();
        return c >= 0;
    }

    public E take() throws InterruptedException {
        E x;
        int c;
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            while (count.get() == 0) {
                notEmpty.await();
            }
            x = dequeue();
            c = count.getAndDecrement();
            if (c > 1)
                notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
        if (c == capacity)
            signalNotFull();
        return x;
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E x;
        int c;
        long nanos = unit.toNanos(timeout);
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            while (count.get() == 0) {
                if (nanos <= 0)
                    return null;
                nanos = notEmpty.awaitNanos(nanos);
            }
            x = dequeue();
            c = count.getAndDecrement();
            if (c > 1)
                notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
        if (c == capacity)
            signalNotFull();
        return x;
    }

    public E poll() {
        final AtomicInteger count = this.count;
        if (count.get() == 0)
            return null;
        E x = null;
        int c = -1;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            if (count.get() > 0) {
                x = dequeue();
                c = count.getAndDecrement();
                if (c > 1)
                    notEmpty.signal();
            }
        } finally {
            takeLock.unlock();
        }
        if (c == capacity)
            signalNotFull();
        return x;
    }

    public E peek() {
        if (count.get() == 0)
            return null;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            Iterator<E> iterator = map.values().iterator();
            if (iterator.hasNext())
                return iterator.next();
            else
                return null;
        } finally {
            takeLock.unlock();
        }
    }

    public boolean remove(Object v) {
        fullyLock();
        try {
            boolean removed = false;
            Iterator<E> iterator = map.values().iterator();
            while (iterator.hasNext()) {
                E e = iterator.next();
                if (Objects.equals(v, e)) {
                    iterator.remove();
                    count.decrementAndGet();
                    removed = true;
                }
            }
            if (count.get() < capacity)
                notFull.signal();
            return removed;
        } finally {
            fullyUnlock();
        }
    }

    public boolean removeKey(Object k) {
        fullyLock();
        try {
            if (map.containsKey(k)) {
                map.remove(k);
                if (count.getAndDecrement() == capacity)
                    notFull.signal();
                return true;
            } else {
                return false;
            }
        } finally {
            fullyUnlock();
        }
    }

    public boolean contains(Object v) {
        fullyLock();
        try {
            return map.containsValue(v);
        } finally {
            fullyUnlock();
        }
    }

    public boolean containsKey(Object k) {
        fullyLock();
        try {
            return map.containsKey(k);
        } finally {
            fullyUnlock();
        }
    }

    public Object[] toArray() {
        fullyLock();
        try {
            int size = count.get();
            Object[] a = new Object[size];
            int k = 0;
            for (E value : map.values()) {
                a[k++] = value;
            }
            return a;
        } finally {
            fullyUnlock();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        fullyLock();
        try {
            int size = count.get();
            if (a.length < size)
                a = (T[]) java.lang.reflect.Array.newInstance
                        (a.getClass().getComponentType(), size);

            int k = 0;
            for (E value : map.values()) {
                a[k++] = (T) value;
            }
            if (a.length > k)
                a[k] = null;
            return a;
        } finally {
            fullyUnlock();
        }
    }

    public String toString() {
        fullyLock();
        try {
            return map.values().toString();
        } finally {
            fullyUnlock();
        }
    }

    public void clear() {
        fullyLock();
        try {
            map.clear();
            if (count.getAndSet(0) == capacity)
                notFull.signal();
        } finally {
            fullyUnlock();
        }
    }

    public int drainTo(Collection<? super E> c) {
        return drainTo(c, Integer.MAX_VALUE);
    }

    public int drainTo(Collection<? super E> c, int maxElements) {
        if (c == null)
            throw new NullPointerException();
        if (c == this)
            throw new IllegalArgumentException();
        if (maxElements <= 0)
            return 0;
        boolean signalNotFull = false;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            int n = Math.min(maxElements, count.get());
            int i = 0;
            try {
                Iterator<E> iterator = map.values().iterator();
                while (i < n && iterator.hasNext()) {
                    E next = iterator.next();
                    iterator.remove();
                    c.add(next);
                    ++i;
                }
                return n;
            } finally {
                if (i > 0) {
                    signalNotFull = (count.getAndAdd(-i) == capacity);
                }
            }
        } finally {
            takeLock.unlock();
            if (signalNotFull)
                signalNotFull();
        }
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {

        private final Iterator<E> iterator;

        Itr() {
            fullyLock();
            try {
                iterator = map.values().iterator();
            } finally {
                fullyUnlock();
            }
        }

        public boolean hasNext() {
            fullyLock();
            try {
                return iterator.hasNext();
            } finally {
                fullyUnlock();
            }
        }

        public E next() {
            fullyLock();
            try {
                return iterator.next();
            } finally {
                fullyUnlock();
            }
        }

        public void remove() {
            fullyLock();
            try {
                iterator.remove();
            } finally {
                fullyUnlock();
            }
        }
    }

    private class SItr implements Spliterator<E> {
        final Spliterator<E> spliterator;

        SItr(MapBlockingQueue<E> queue) {
            this.spliterator = queue.map.values().spliterator();
        }

        public long estimateSize() {
            return spliterator.estimateSize();
        }

        public Spliterator<E> trySplit() {
            fullyLock();
            try {
                return spliterator.trySplit();
            } finally {
                fullyUnlock();
            }
        }

        public void forEachRemaining(Consumer<? super E> action) {
            fullyLock();
            try {
                spliterator.forEachRemaining(action);
            } finally {
                fullyUnlock();
            }
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            fullyLock();
            try {
                return spliterator.tryAdvance(action);
            } finally {
                fullyUnlock();
            }
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.NONNULL |
                    Spliterator.CONCURRENT;
        }

    }

    public Spliterator<E> spliterator() {
        return new SItr(this);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        fullyLock();
        try {
            s.defaultWriteObject();
            s.writeObject(map);
            s.writeObject(null);
        } finally {
            fullyUnlock();
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        map = (Map<Object, E>) s.readObject();
        count.set(map.size());
    }
}
