package com.github.linyuzai.mapqueue.core.concurrent;

import com.github.linyuzai.mapqueue.core.concept.MapQueueElement;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;

public abstract class AbstractBlockingMapQueue<K, V> implements BlockingMapQueue<K, V> {

    /**
     * The capacity bound, or Integer.MAX_VALUE if none
     */
    private final int capacity;

    /**
     * Current number of elements
     */
    //size 方法没加锁，可以用 cas 优化
    //或者 size 加锁
    private int count;

    /**
     * Lock
     */
    private final ReentrantLock lock;

    /**
     * Wait queue for waiting takes
     */
    private final Condition notEmpty;

    /**
     * Wait queue for waiting puts
     */
    private final Condition notFull;

    private final Map<K, V> map;

    private final Map<K, V> readOnly;

    private final List<Synchronizer<K, V>> synchronizers = new CopyOnWriteArrayList<>();

    /*/**
     * Signals a waiting take. Called only from put/offer (which do not
     * otherwise ordinarily lock takeLock.)
     */
    /*private void signalNotEmpty() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }*/

    /*/**
     * Signals a waiting put. Called only from take/poll.
     */
    /*private void signalNotFull() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            notFull.signal();
        } finally {
            lock.unlock();
        }
    }*/

    private void invokeSynchronizersBeforeEnqueue(K key, V value) {
        synchronizers.forEach(it -> it.beforeEnqueue(key, value, readOnly));
    }

    private void invokeSynchronizersAfterEnqueue(K key, V value) {
        synchronizers.forEach(it -> it.afterEnqueue(key, value, readOnly));
    }

    private void invokeSynchronizersBeforeDequeue(K key, V value) {
        synchronizers.forEach(it -> it.beforeDequeue(key, value, readOnly));
    }

    private void invokeSynchronizersAfterDequeue(K key, V value) {
        synchronizers.forEach(it -> it.afterDequeue(key, value, readOnly));
    }

    /*
    /**
     * Links node at end of queue.
     *
     * @param node the node
     */
    /*private void enqueue(LinkedBlockingQueue.Node<E> node) {
        // assert putLock.isHeldByCurrentThread();
        // assert last.next == null;
        last = last.next = node;
    }*/
    private boolean nonBlockingEnqueue(K k, V v) {
        boolean x;
        if (map.containsKey(k)) {
            invokeSynchronizersBeforeEnqueue(k, v);
            map.put(k, v);
            x = true;
        } else {
            if (count < capacity) {
                invokeSynchronizersBeforeEnqueue(k, v);
                map.put(k, v);
                count++;
                x = true;
            } else {
                x = false;
            }
        }
        if (x) {
            invokeSynchronizersAfterEnqueue(k, v);
        }
        //发送未空信号
        notEmpty.signal();
        return x;
    }

    private V blockingEnqueue(K k, V v) throws InterruptedException {
        V x;
        if (map.containsKey(k)) {
            //已经存在
            //更新节点
            //count不变
            invokeSynchronizersBeforeEnqueue(k, v);
            x = map.put(k, v);
        } else {
            //不存在
            while (count == capacity) {
                //满了，等未满的信号
                notFull.await();
            }
            //添加节点
            //count+1
            invokeSynchronizersBeforeEnqueue(k, v);
            x = map.put(k, v);
            count++;
        }
        invokeSynchronizersAfterEnqueue(k, v);
        //发送未空信号
        notEmpty.signal();
        return x;
    }

    private boolean blockingEnqueue(K k, V v, long timeout, TimeUnit unit) throws InterruptedException {
        if (map.containsKey(k)) {
            //已经存在
            //更新节点
            //count不变
            invokeSynchronizersBeforeEnqueue(k, v);
            map.put(k, v);
        } else {
            //不存在
            long nanos = unit.toNanos(timeout);
            while (count == capacity) {
                //满了，等未满的信号
                if (nanos <= 0) {
                    return false;
                }
                nanos = notFull.awaitNanos(nanos);
            }
            //添加节点
            //count+1
            invokeSynchronizersBeforeEnqueue(k, v);
            map.put(k, v);
            count++;
        }
        invokeSynchronizersAfterEnqueue(k, v);
        //发送未空信号
        notEmpty.signal();
        return true;
    }

    private V nonBlockingDequeue(K k) {
        if (map.containsKey(k)) {
            //已经存在
            //移除节点
            //count-1
            invokeSynchronizersBeforeDequeue(k, map.get(k));
            V v = map.remove(k);
            count--;
            invokeSynchronizersAfterDequeue(k, v);
            //发送未满信号
            notFull.signal();
            return v;
        }
        return null;
    }

    private Map.Entry<K, V> nonBlockingDequeue() {
        if (count > 0) {
            return dequeue0();
        }
        return null;
    }

    /*/**
     * Removes a node from head of queue.
     *
     * @return the node
     */
    /*private E dequeue() {
        // assert takeLock.isHeldByCurrentThread();
        // assert head.item == null;
        LinkedBlockingQueue.Node<E> h = head;
        LinkedBlockingQueue.Node<E> first = h.next;
        h.next = h; // help GC
        head = first;
        E x = first.item;
        first.item = null;
        return x;
    }*/

    /**
     * 出队
     *
     * @return 下一个数据节点
     */
    private Map.Entry<K, V> blockingDequeue() throws InterruptedException {
        while (count == 0) {
            notEmpty.await();
        }
        return dequeue0();
    }

    private Map.Entry<K, V> blockingDequeue(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        while (count == 0) {
            if (nanos <= 0) {
                return null;
            }
            nanos = notEmpty.awaitNanos(nanos);
        }
        return dequeue0();
    }

    private Map.Entry<K, V> dequeue0() {
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        Map.Entry<K, V> entry = iterator.next();
        K k = entry.getKey();
        V v = entry.getValue();
        invokeSynchronizersBeforeDequeue(k, v);
        iterator.remove();
        count--;
        invokeSynchronizersAfterDequeue(k, v);
        notFull.signal();
        return entry;
    }

    /*/**
     * Locks to prevent both puts and takes.
     */
    /*void fullyLock() {
        lock.lock();
    }*/

    /*/**
     * Unlocks to allow both puts and takes.
     */
    /*void fullyUnlock() {
        lock.unlock();
    }*/

    /*void fullyLockInterruptibly() throws InterruptedException {
        lock.lockInterruptibly();
    }*/

    protected abstract Map<K, V> createMap();

    /*/**
     * Creates a {@code v} with a capacity of
     * {@link Integer#MAX_VALUE}.
     */
    /*public LinkedBlockingQueue() {
        this(Integer.MAX_VALUE);
    }*/

    public AbstractBlockingMapQueue() {
        this(Integer.MAX_VALUE, false);
    }

    public AbstractBlockingMapQueue(int capacity) {
        this(capacity, false);
    }

    public AbstractBlockingMapQueue(boolean fair) {
        this(Integer.MAX_VALUE, fair);
    }

    public AbstractBlockingMapQueue(Map<K, V> map) {
        this(Integer.MAX_VALUE, false, map);
    }

    public AbstractBlockingMapQueue(int capacity, Map<K, V> map) {
        this(capacity, false, map);
    }

    public AbstractBlockingMapQueue(boolean fair, Map<K, V> map) {
        this(Integer.MAX_VALUE, fair, map);
    }

    /*/**
     * Creates a {@code LinkedBlockingQueue} with the given (fixed) capacity.
     *
     * @param capacity the capacity of this queue
     * @throws IllegalArgumentException if {@code capacity} is not greater
     *         than zero
     */
    /*public LinkedBlockingQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException();
        this.capacity = capacity;
        last = head = new LinkedBlockingQueue.Node<E>(null);
    }*/

    /*/**
     * Creates a {@code LinkedBlockingQueue} with a capacity of
     * {@link Integer#MAX_VALUE}, initially containing the elements of the
     * given collection,
     * added in traversal order of the collection's iterator.
     *
     * @param c the collection of elements to initially contain
     * @throws NullPointerException if the specified collection or any
     *         of its elements are null
     */
    /*public LinkedBlockingQueue(Collection<? extends E> c) {
        this(Integer.MAX_VALUE);
        final ReentrantLock putLock = this.putLock;
        putLock.lock(); // Never contended, but necessary for visibility
        try {
            int n = 0;
            for (E e : c) {
                if (e == null)
                    throw new NullPointerException();
                if (n == capacity)
                    throw new IllegalStateException("Queue full");
                enqueue(new LinkedBlockingQueue.Node<E>(e));
                ++n;
            }
            count.set(n);
        } finally {
            putLock.unlock();
        }
    }*/

    public AbstractBlockingMapQueue(int capacity, boolean fair) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        this.map = createMap();
        this.readOnly = Collections.unmodifiableMap(this.map);
        this.lock = new ReentrantLock(fair);
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
    }

    public AbstractBlockingMapQueue(int capacity, boolean fair, Map<? extends K, ? extends V> map) {
        this(capacity, fair);
        this.lock.lock();
        try {
            this.map.putAll(map);
            int size = map.size();
            if (size >= capacity) {
                throw new IllegalStateException("Queue full");
            }
            count = size;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addSynchronizer(Synchronizer<K, V> synchronizer) {
        synchronizers.add(synchronizer);
    }

    @Override
    public void removeSynchronizer(Synchronizer<K, V> synchronizer) {
        synchronizers.remove(synchronizer);
    }

    // this doc comment is overridden to remove the reference to collections
    // greater in size than Integer.MAX_VALUE

    /**
     * Returns the number of elements in this queue.
     *
     * @return the number of elements in this queue
     */
    public int size() {
        return count;
    }

    // this doc comment is a modified copy of the inherited doc comment,
    // without the reference to unlimited queues.

    /**
     * Returns the number of additional elements that this queue can ideally
     * (in the absence of memory or resource constraints) accept without
     * blocking. This is always equal to the initial capacity of this queue
     * less the current {@code size} of this queue.
     *
     * <p>Note that you <em>cannot</em> always tell if an attempt to insert
     * an element will succeed by inspecting {@code remainingCapacity}
     * because it may be the case that another thread is about to
     * insert or remove an element.
     */
    public int remainingCapacity() {
        return capacity - count;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Inserts the specified element at the tail of this queue, waiting if
     * necessary for space to become available.
     *
     * @throws InterruptedException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    /*public void put(E e) throws InterruptedException {
        if (e == null) throw new NullPointerException();
        // Note: convention in all put/take/etc is to preset local var
        // holding count negative to indicate failure unless set.
        int c = -1;
        LinkedBlockingQueue.Node<E> node = new LinkedBlockingQueue.Node<E>(e);
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        try {
            *//*
     * Note that count is used in wait guard even though it is
     * not protected by lock. This works because count can
     * only decrease at this point (all other puts are shut
     * out by lock), and we (or some other waiting put) are
     * signalled if it ever changes from capacity. Similarly
     * for all other uses of count in other wait guards.
     *//*
            while (count.get() == capacity) {
                notFull.await();
            }
            enqueue(node);
            c = count.getAndIncrement();
            if (c + 1 < capacity)
                notFull.signal();
        } finally {
            putLock.unlock();
        }
        if (c == 0)
            signalNotEmpty();
    }*/
    public V put(K key, V value) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            return blockingEnqueue(key, value);
        } finally {
            lock.unlock();
        }
    }

    public void putAll(Map<? extends K, ? extends V> m) throws InterruptedException {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public V putIfAbsent(K key, V value) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            V oldValue = map.get(key);
            //如果不存在或值为null则需要添加
            if (oldValue == null) {
                return blockingEnqueue(key, value);
            } else {
                return oldValue;
            }
        } finally {
            lock.unlock();
        }
    }

    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) throws InterruptedException {
        Objects.requireNonNull(mappingFunction);
        lock.lockInterruptibly();
        try {
            V oldValue = map.get(key);
            if (oldValue == null) {
                V newValue = mappingFunction.apply(key);
                if (newValue == null) {
                    return null;
                } else {
                    blockingEnqueue(key, newValue);
                    return newValue;
                }
            } else {
                return oldValue;
            }
        } finally {
            lock.unlock();
        }
    }

    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) throws InterruptedException {
        Objects.requireNonNull(remappingFunction);
        lock.lockInterruptibly();
        try {
            V oldValue = map.get(key);
            if (oldValue != null) {
                V newValue = remappingFunction.apply(key, oldValue);
                if (newValue == null) {
                    nonBlockingDequeue(key);
                    return null;
                } else {
                    blockingEnqueue(key, newValue);
                    return newValue;
                }
            } else {
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) throws InterruptedException {
        Objects.requireNonNull(remappingFunction);
        lock.lockInterruptibly();
        try {
            Objects.requireNonNull(remappingFunction);
            V oldValue = map.get(key);
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue == null) {
                // delete mapping
                if (oldValue != null || map.containsKey(key)) {
                    // something to remove
                    nonBlockingDequeue(key);
                } else {
                    // nothing to do. Leave things as they were.
                }
                return null;
            } else {
                // add or replace old mapping
                blockingEnqueue(key, newValue);
                return newValue;
            }
        } finally {
            lock.unlock();
        }
    }

    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) throws InterruptedException {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);
        lock.lockInterruptibly();
        try {
            V oldValue = get(key);
            V newValue = (oldValue == null) ? value :
                    remappingFunction.apply(oldValue, value);
            if (newValue == null) {
                nonBlockingDequeue(key);
            } else {
                blockingEnqueue(key, newValue);
            }
            return newValue;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Inserts the specified element at the tail of this queue, waiting if
     * necessary up to the specified wait time for space to become available.
     *
     * @return {@code true} if successful, or {@code false} if
     * the specified waiting time elapses before space is available
     * @throws InterruptedException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    /*public boolean offer(E e, long timeout, TimeUnit unit)
            throws InterruptedException {

        if (e == null) throw new NullPointerException();
        long nanos = unit.toNanos(timeout);
        int c = -1;
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        try {
            while (count.get() == capacity) {
                if (nanos <= 0)
                    return false;
                nanos = notFull.awaitNanos(nanos);
            }
            enqueue(new LinkedBlockingQueue.Node<E>(e));
            c = count.getAndIncrement();
            if (c + 1 < capacity)
                notFull.signal();
        } finally {
            putLock.unlock();
        }
        if (c == 0)
            signalNotEmpty();
        return true;
    }*/
    public boolean offer(K key, V value, long timeout, TimeUnit unit) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            return blockingEnqueue(key, value, timeout, unit);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Inserts the specified element at the tail of this queue if it is
     * possible to do so immediately without exceeding the queue's capacity,
     * returning {@code true} upon success and {@code false} if this queue
     * is full.
     * When using a capacity-restricted queue, this method is generally
     * preferable to method {@link BlockingQueue#add add}, which can fail to
     * insert an element only by throwing an exception.
     *
     * @throws NullPointerException if the specified element is null
     */
    /*public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        final AtomicInteger count = this.count;
        if (count.get() == capacity)
            return false;
        int c = -1;
        LinkedBlockingQueue.Node<E> node = new LinkedBlockingQueue.Node<E>(e);
        final ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            if (count.get() < capacity) {
                enqueue(node);
                c = count.getAndIncrement();
                if (c + 1 < capacity)
                    notFull.signal();
            }
        } finally {
            putLock.unlock();
        }
        if (c == 0)
            signalNotEmpty();
        return c >= 0;
    }*/
    public boolean offer(K key, V value) {
        lock.lock();
        try {
            return nonBlockingEnqueue(key, value);
        } finally {
            lock.unlock();
        }
    }

    /*public E take() throws InterruptedException {
        E x;
        int c = -1;
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
    }*/

    public Map.Entry<K, V> take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            return blockingDequeue();
        } finally {
            lock.unlock();
        }
    }

    public V takeValue() throws InterruptedException {
        return take().getValue();
    }

    /*public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E x = null;
        int c = -1;
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
    }*/

    public Map.Entry<K, V> poll(long timeout, TimeUnit unit) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            return blockingDequeue(timeout, unit);
        } finally {
            lock.unlock();
        }
    }

    public V pollValue(long timeout, TimeUnit unit) throws InterruptedException {
        return poll(timeout, unit).getValue();
    }

    /*public E poll() {
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
    }*/

    public Map.Entry<K, V> poll() {
        lock.lock();
        try {
            return nonBlockingDequeue();
        } finally {
            lock.unlock();
        }
    }

    public V pollValue() {
        return poll().getValue();
    }

    /*public E peek() {
        if (count.get() == 0)
            return null;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            LinkedBlockingQueue.Node<E> first = head.next;
            if (first == null)
                return null;
            else
                return first.item;
        } finally {
            takeLock.unlock();
        }
    }*/

    public Map.Entry<K, V> peek() {
        lock.lock();
        try {
            if (count == 0) {
                return null;
            }
            Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            } else {
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    public V peekValue() {
        return peek().getValue();
    }

    public V get(K key) {
        lock.lock();
        try {
            return map.get(key);
        } finally {
            lock.unlock();
        }
    }

    public V getOrDefault(K key, V defaultValue) {
        lock.lock();
        try {
            return map.getOrDefault(key, defaultValue);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Unlinks interior Node p with predecessor trail.
     */
    /*void unlink(LinkedBlockingQueue.Node<E> p, LinkedBlockingQueue.Node<E> trail) {
        // assert isFullyLocked();
        // p.next is not changed, to allow iterators that are
        // traversing p to maintain their weak-consistency guarantee.
        p.item = null;
        trail.next = p.next;
        if (last == p)
            last = trail;
        if (count.getAndDecrement() == capacity)
            notFull.signal();
    }*/

    /*/**
     * Removes a single instance of the specified element from this queue,
     * if it is present.  More formally, removes an element {@code e} such
     * that {@code o.equals(e)}, if this queue contains one or more such
     * elements.
     * Returns {@code true} if this queue contained the specified element
     * (or equivalently, if this queue changed as a result of the call).
     *
     * @param o element to be removed from this queue, if present
     * @return {@code true} if this queue changed as a result of the call
     */
    /*public boolean remove(Object o) {
        if (o == null) return false;
        fullyLock();
        try {
            for (LinkedBlockingQueue.Node<E> trail = head, p = trail.next;
                 p != null;
                 trail = p, p = p.next) {
                if (o.equals(p.item)) {
                    unlink(p, trail);
                    return true;
                }
            }
            return false;
        } finally {
            fullyUnlock();
        }
    }*/
    public V remove(K key) {
        lock.lock();
        try {
            return nonBlockingDequeue(key);
        } finally {
            lock.unlock();
        }
    }

    public boolean remove(K key, V value) {
        lock.lock();
        try {
            if (Objects.equals(map.get(key), value)) {
                invokeSynchronizersBeforeDequeue(key, value);
            }
            boolean remove = map.remove(key, value);
            if (remove) {
                count--;
                invokeSynchronizersAfterDequeue(key, value);
                //发送未满信号
                notFull.signal();
            }
            return remove;
        } finally {
            lock.unlock();
        }
    }

    public boolean removeValue(V value) {
        lock.lock();
        try {
            boolean removed = false;
            Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<K, V> entry = iterator.next();
                K k = entry.getKey();
                V v = entry.getValue();
                if (Objects.equals(value, v)) {
                    invokeSynchronizersBeforeDequeue(k, v);
                    iterator.remove();
                    count--;
                    removed = true;
                    invokeSynchronizersAfterDequeue(k, v);
                }
            }
            if (removed) {
                notFull.signal();
            }
            return removed;
        } finally {
            lock.unlock();
        }
    }

    public boolean replace(K key, V oldValue, V newValue) {
        lock.lock();
        try {
            return map.replace(key, oldValue, newValue);
        } finally {
            lock.unlock();
        }
    }

    public V replace(K key, V value) {
        lock.lock();
        try {
            return map.replace(key, value);
        } finally {
            lock.unlock();
        }
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        lock.lock();
        try {
            map.replaceAll(function);
        } finally {
            lock.unlock();
        }
    }

    /*/**
     * Returns {@code true} if this queue contains the specified element.
     * More formally, returns {@code true} if and only if this queue contains
     * at least one element {@code e} such that {@code o.equals(e)}.
     *
     * @param o object to be checked for containment in this queue
     * @return {@code true} if this queue contains the specified element
     */
    /*public boolean contains(Object o) {
        if (o == null) return false;
        fullyLock();
        try {
            for (LinkedBlockingQueue.Node<E> p = head.next; p != null; p = p.next)
                if (o.equals(p.item))
                    return true;
            return false;
        } finally {
            fullyUnlock();
        }
    }*/
    public boolean containsKey(K key) {
        lock.lock();
        try {
            return map.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    public boolean containsValue(V v) {
        lock.lock();
        try {
            return map.containsValue(v);
        } finally {
            lock.unlock();
        }
    }

    public Set<K> keySet() {
        lock.lock();
        try {
            return map.keySet();
        } finally {
            lock.unlock();
        }
    }

    public Collection<V> values() {
        lock.lock();
        try {
            return map.values();
        } finally {
            lock.unlock();
        }
    }

    public Set<Map.Entry<K, V>> entrySet() {
        lock.lock();
        try {
            return map.entrySet();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns an array containing all of the elements in this queue, in
     * proper sequence.
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this queue.  (In other words, this method must allocate
     * a new array).  The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this queue
     */
    /*public Object[] toArray() {
        fullyLock();
        try {
            int size = count.get();
            Object[] a = new Object[size];
            int k = 0;
            for (LinkedBlockingQueue.Node<E> p = head.next; p != null; p = p.next)
                a[k++] = p.item;
            return a;
        } finally {
            fullyUnlock();
        }
    }*/
    public Object[] toArray() {
        lock.lock();
        try {
            int size = count;
            Object[] a = new Object[size];
            int k = 0;
            for (Map.Entry<K, V> value : map.entrySet()) {
                a[k++] = value;
            }
            return a;
        } finally {
            lock.unlock();
        }
    }

    /*/**
     * Returns an array containing all of the elements in this queue, in
     * proper sequence; the runtime type of the returned array is that of
     * the specified array.  If the queue fits in the specified array, it
     * is returned therein.  Otherwise, a new array is allocated with the
     * runtime type of the specified array and the size of this queue.
     *
     * <p>If this queue fits in the specified array with room to spare
     * (i.e., the array has more elements than this queue), the element in
     * the array immediately following the end of the queue is set to
     * {@code null}.
     *
     * <p>Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs.
     *
     * <p>Suppose {@code x} is a queue known to contain only strings.
     * The following code can be used to dump the queue into a newly
     * allocated array of {@code String}:
     *
     *  <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
     *
     * Note that {@code toArray(new Object[0])} is identical in function to
     * {@code toArray()}.
     *
     * @param a the array into which the elements of the queue are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose
     * @return an array containing all of the elements in this queue
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this queue
     * @throws NullPointerException if the specified array is null
     */
    /*@SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        fullyLock();
        try {
            int size = count.get();
            if (a.length < size)
                a = (T[])java.lang.reflect.Array.newInstance
                        (a.getClass().getComponentType(), size);

            int k = 0;
            for (LinkedBlockingQueue.Node<E> p = head.next; p != null; p = p.next)
                a[k++] = (T)p.item;
            if (a.length > k)
                a[k] = null;
            return a;
        } finally {
            fullyUnlock();
        }
    }*/

    public Object[] toValueArray() {
        lock.lock();
        try {
            int size = count;
            Object[] a = new Object[size];
            int k = 0;
            for (V value : map.values()) {
                a[k++] = value;
            }
            return a;
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toValueArray(T[] a) {
        lock.lock();
        try {
            int size = count;
            if (a.length < size) {
                a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
            }
            int k = 0;
            for (V value : map.values()) {
                a[k++] = (T) value;
            }
            if (a.length > k) a[k] = null;
            return a;
        } finally {
            lock.unlock();
        }
    }

    /*public String toString() {
        fullyLock();
        try {
            LinkedBlockingQueue.Node<E> p = head.next;
            if (p == null)
                return "[]";

            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for (;;) {
                E e = p.item;
                sb.append(e == this ? "(this Collection)" : e);
                p = p.next;
                if (p == null)
                    return sb.append(']').toString();
                sb.append(',').append(' ');
            }
        } finally {
            fullyUnlock();
        }
    }*/

    @Override
    public int hashCode() {
        lock.lock();
        try {
            return map.hashCode();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            return map.toString();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean equals(Object obj) {
        lock.lock();
        try {
            return map.equals(obj);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Atomically removes all of the elements from this queue.
     * The queue will be empty after this call returns.
     */
    /*public void clear() {
        fullyLock();
        try {
            for (LinkedBlockingQueue.Node<E> p, h = head; (p = h.next) != null; h = p) {
                h.next = h;
                p.item = null;
            }
            head = last;
            // assert head.item == null && head.next == null;
            if (count.getAndSet(0) == capacity)
                notFull.signal();
        } finally {
            fullyUnlock();
        }
    }*/
    public void clear() {
        lock.lock();
        try {
            map.clear();
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     */
    /*public int drainTo(Collection<? super E> c) {
        return drainTo(c, Integer.MAX_VALUE);
    }*/
    public int drainValueTo(Collection<? super V> c) {
        return drainValueTo(c, Integer.MAX_VALUE);
    }

    /**
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     */
    /*public int drainTo(Collection<? super E> c, int maxElements) {
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
            // count.get provides visibility to first n Nodes
            LinkedBlockingQueue.Node<E> h = head;
            int i = 0;
            try {
                while (i < n) {
                    LinkedBlockingQueue.Node<E> p = h.next;
                    c.add(p.item);
                    p.item = null;
                    h.next = h;
                    h = p;
                    ++i;
                }
                return n;
            } finally {
                // Restore invariants even if c.add() threw
                if (i > 0) {
                    // assert h.item == null;
                    head = h;
                    signalNotFull = (count.getAndAdd(-i) == capacity);
                }
            }
        } finally {
            takeLock.unlock();
            if (signalNotFull)
                signalNotFull();
        }
    }*/
    public int drainValueTo(Collection<? super V> c, int maxElements) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (maxElements <= 0) {
            return 0;
        }
        lock.lock();
        try {
            int n = Math.min(maxElements, count);
            int i = 0;
            try {
                Iterator<V> iterator = map.values().iterator();
                while (i < n && iterator.hasNext()) {
                    V next = iterator.next();
                    iterator.remove();
                    c.add(next);
                    ++i;
                }
                return n;
            } finally {
                if (i > 0) {
                    boolean signalNotFull = (count == capacity);
                    count -= i;
                    if (signalNotFull) {
                        notFull.signal();
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        lock.lock();
        try {
            map.forEach(action);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns an iterator over the elements in this queue in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * <p>The returned iterator is
     * <a href="package-summary.html#Weakly"><i>weakly consistent</i></a>.
     *
     * @return an iterator over the elements in this queue in proper sequence
     */
    /*public Iterator<E> iterator() {
        return new LinkedBlockingQueue.Itr();
    }*/
    public Iterator<Map.Entry<K, V>> iterator() {
        return new Itr();
    }

    /*private class Itr implements Iterator<E> {
     *//*
     * Basic weakly-consistent iterator.  At all times hold the next
     * item to hand out so that if hasNext() reports true, we will
     * still have it to return even if lost race with a take etc.
     *//*

        private LinkedBlockingQueue.Node<E> current;
        private LinkedBlockingQueue.Node<E> lastRet;
        private E currentElement;

        Itr() {
            fullyLock();
            try {
                current = head.next;
                if (current != null)
                    currentElement = current.item;
            } finally {
                fullyUnlock();
            }
        }

        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next live successor of p, or null if no such.
         *
         * Unlike other traversal methods, iterators need to handle both:
         * - dequeued nodes (p.next == p)
         * - (possibly multiple) interior removed nodes (p.item == null)
         *//*
        private LinkedBlockingQueue.Node<E> nextNode(LinkedBlockingQueue.Node<E> p) {
            for (;;) {
                LinkedBlockingQueue.Node<E> s = p.next;
                if (s == p)
                    return head.next;
                if (s == null || s.item != null)
                    return s;
                p = s;
            }
        }

        public E next() {
            fullyLock();
            try {
                if (current == null)
                    throw new NoSuchElementException();
                E x = currentElement;
                lastRet = current;
                current = nextNode(current);
                currentElement = (current == null) ? null : current.item;
                return x;
            } finally {
                fullyUnlock();
            }
        }

        public void remove() {
            if (lastRet == null)
                throw new IllegalStateException();
            fullyLock();
            try {
                LinkedBlockingQueue.Node<E> node = lastRet;
                lastRet = null;
                for (LinkedBlockingQueue.Node<E> trail = head, p = trail.next;
                     p != null;
                     trail = p, p = p.next) {
                    if (p == node) {
                        unlink(p, trail);
                        break;
                    }
                }
            } finally {
                fullyUnlock();
            }
        }
    }*/

    private class Itr implements Iterator<Map.Entry<K, V>> {

        private final Iterator<Map.Entry<K, V>> iterator;

        Itr() {
            lock.lock();
            try {
                iterator = map.entrySet().iterator();
            } finally {
                lock.unlock();
            }
        }

        public boolean hasNext() {
            lock.lock();
            try {
                return iterator.hasNext();
            } finally {
                lock.unlock();
            }
        }

        public Map.Entry<K, V> next() {
            lock.lock();
            try {
                return iterator.next();
            } finally {
                lock.unlock();
            }
        }

        public void remove() {
            lock.lock();
            try {
                iterator.remove();
            } finally {
                lock.unlock();
            }
        }
    }

    public Iterator<V> valueIterator() {
        return new ValueItr();
    }

    private class ValueItr implements Iterator<V> {

        private final Iterator<V> iterator;

        ValueItr() {
            lock.lock();
            try {
                iterator = map.values().iterator();
            } finally {
                lock.unlock();
            }
        }

        public boolean hasNext() {
            lock.lock();
            try {
                return iterator.hasNext();
            } finally {
                lock.unlock();
            }
        }

        public V next() {
            lock.lock();
            try {
                return iterator.next();
            } finally {
                lock.unlock();
            }
        }

        public void remove() {
            lock.lock();
            try {
                iterator.remove();
            } finally {
                lock.unlock();
            }
        }
    }

    /*/**
     * A customized variant of Spliterators.IteratorSpliterator
     */
    /*static final class LBQSpliterator<E> implements Spliterator<E> {
        static final int MAX_BATCH = 1 << 25;  // max batch array size;
        final LinkedBlockingQueue<E> queue;
        LinkedBlockingQueue.Node<E> current;    // current node; null until initialized
        int batch;          // batch size for splits
        boolean exhausted;  // true when no more nodes
        long est;           // size estimate
        LBQSpliterator(LinkedBlockingQueue<E> queue) {
            this.queue = queue;
            this.est = queue.size();
        }

        public long estimateSize() { return est; }

        public Spliterator<E> trySplit() {
            LinkedBlockingQueue.Node<E> h;
            final LinkedBlockingQueue<E> q = this.queue;
            int b = batch;
            int n = (b <= 0) ? 1 : (b >= MAX_BATCH) ? MAX_BATCH : b + 1;
            if (!exhausted &&
                    ((h = current) != null || (h = q.head.next) != null) &&
                    h.next != null) {
                Object[] a = new Object[n];
                int i = 0;
                LinkedBlockingQueue.Node<E> p = current;
                q.fullyLock();
                try {
                    if (p != null || (p = q.head.next) != null) {
                        do {
                            if ((a[i] = p.item) != null)
                                ++i;
                        } while ((p = p.next) != null && i < n);
                    }
                } finally {
                    q.fullyUnlock();
                }
                if ((current = p) == null) {
                    est = 0L;
                    exhausted = true;
                }
                else if ((est -= i) < 0L)
                    est = 0L;
                if (i > 0) {
                    batch = i;
                    return Spliterators.spliterator
                            (a, 0, i, Spliterator.ORDERED | Spliterator.NONNULL |
                                    Spliterator.CONCURRENT);
                }
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            if (action == null) throw new NullPointerException();
            final LinkedBlockingQueue<E> q = this.queue;
            if (!exhausted) {
                exhausted = true;
                LinkedBlockingQueue.Node<E> p = current;
                do {
                    E e = null;
                    q.fullyLock();
                    try {
                        if (p == null)
                            p = q.head.next;
                        while (p != null) {
                            e = p.item;
                            p = p.next;
                            if (e != null)
                                break;
                        }
                    } finally {
                        q.fullyUnlock();
                    }
                    if (e != null)
                        action.accept(e);
                } while (p != null);
            }
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            if (action == null) throw new NullPointerException();
            final LinkedBlockingQueue<E> q = this.queue;
            if (!exhausted) {
                E e = null;
                q.fullyLock();
                try {
                    if (current == null)
                        current = q.head.next;
                    while (current != null) {
                        e = current.item;
                        current = current.next;
                        if (e != null)
                            break;
                    }
                } finally {
                    q.fullyUnlock();
                }
                if (current == null)
                    exhausted = true;
                if (e != null) {
                    action.accept(e);
                    return true;
                }
            }
            return false;
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.NONNULL |
                    Spliterator.CONCURRENT;
        }
    }*/

    /**
     * Returns a {@link Spliterator} over the elements in this queue.
     *
     * <p>The returned spliterator is
     * <a href="package-summary.html#Weakly"><i>weakly consistent</i></a>.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#CONCURRENT},
     * {@link Spliterator#ORDERED}, and {@link Spliterator#NONNULL}.
     *
     * @return a {@code Spliterator} over the elements in this queue
     * @implNote The {@code Spliterator} implements {@code trySplit} to permit limited
     * parallelism.
     * @since 1.8
     */
    /*public Spliterator<E> spliterator() {
        return new LinkedBlockingQueue.LBQSpliterator<E>(this);
    }*/
    public Spliterator<Map.Entry<K, V>> spliterator() {
        return new LBQSpliterator<>(this);
    }

    private class LBQSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {

        final Spliterator<Map.Entry<K, V>> spliterator;

        LBQSpliterator(AbstractBlockingMapQueue<K, V> queue) {
            this.spliterator = queue.map.entrySet().spliterator();
        }

        public long estimateSize() {
            return spliterator.estimateSize();
        }

        public Spliterator<Map.Entry<K, V>> trySplit() {
            lock.lock();
            try {
                return spliterator.trySplit();
            } finally {
                lock.unlock();
            }
        }

        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> action) {
            lock.lock();
            try {
                spliterator.forEachRemaining(action);
            } finally {
                lock.unlock();
            }
        }

        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> action) {
            lock.lock();
            try {
                return spliterator.tryAdvance(action);
            } finally {
                lock.unlock();
            }
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.CONCURRENT;
        }

    }

    public Spliterator<V> valueSpliterator() {
        return new ValueLBQSpliterator<>(this);
    }

    private class ValueLBQSpliterator<V> implements Spliterator<V> {

        final Spliterator<V> spliterator;

        ValueLBQSpliterator(AbstractBlockingMapQueue<K, V> queue) {
            this.spliterator = queue.map.values().spliterator();
        }

        public long estimateSize() {
            return spliterator.estimateSize();
        }

        public Spliterator<V> trySplit() {
            lock.lock();
            try {
                return spliterator.trySplit();
            } finally {
                lock.unlock();
            }
        }

        public void forEachRemaining(Consumer<? super V> action) {
            lock.lock();
            try {
                spliterator.forEachRemaining(action);
            } finally {
                lock.unlock();
            }
        }

        public boolean tryAdvance(Consumer<? super V> action) {
            lock.lock();
            try {
                return spliterator.tryAdvance(action);
            } finally {
                lock.unlock();
            }
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.CONCURRENT;
        }

    }

    /**
     * Saves this queue to a stream (that is, serializes it).
     *
     * @param s the stream
     * @throws java.io.IOException if an I/O error occurs
     * @serialData The capacity is emitted (int), followed by all of
     * its elements (each an {@code Object}) in the proper order,
     * followed by a null
     */
    /*private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {

        fullyLock();
        try {
            // Write out any hidden stuff, plus capacity
            s.defaultWriteObject();

            // Write out all elements in the proper order.
            for (LinkedBlockingQueue.Node<E> p = head.next; p != null; p = p.next)
                s.writeObject(p.item);

            // Use trailing null as sentinel
            s.writeObject(null);
        } finally {
            fullyUnlock();
        }
    }*/
    private void writeObject(ObjectOutputStream s) throws IOException {
        lock.lock();
        try {
            s.defaultWriteObject();
            s.writeObject(map);
            //s.writeObject(null);
        } finally {
            lock.unlock();
        }
    }

    /*/**
     * Reconstitutes this queue from a stream (that is, deserializes it).
     * @param s the stream
     * @throws ClassNotFoundException if the class of a serialized object
     *         could not be found
     * @throws java.io.IOException if an I/O error occurs
     */
    /*private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        // Read in capacity, and any hidden stuff
        s.defaultReadObject();

        count.set(0);
        last = head = new LinkedBlockingQueue.Node<E>(null);

        // Read in all elements and place in queue
        for (;;) {
            @SuppressWarnings("unchecked")
            E item = (E)s.readObject();
            if (item == null)
                break;
            add(item);
        }
    }*/

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        Map<K, V> map = (Map<K, V>) s.readObject();
        this.map.putAll(map);
        count = map.size();
    }

    @Override
    public ConcurrentMap<K, V> map() {
        return new MapImpl();
    }

    @Override
    public BlockingQueue<V> queue() {
        return new QueueImpl();
    }

    @SuppressWarnings("unchecked")
    private class MapImpl implements ConcurrentMap<K, V> {

        @Override
        public int size() {
            return AbstractBlockingMapQueue.this.size();
        }

        @Override
        public boolean isEmpty() {
            return AbstractBlockingMapQueue.this.isEmpty();
        }

        @Override
        public V get(Object key) {
            return AbstractBlockingMapQueue.this.get((K) key);
        }

        @Override
        public V getOrDefault(Object key, V defaultValue) {
            return AbstractBlockingMapQueue.this.getOrDefault((K) key, defaultValue);
        }

        @Override
        public boolean containsKey(Object key) {
            return AbstractBlockingMapQueue.this.containsKey((K) key);
        }

        @Override
        public boolean containsValue(Object value) {
            return AbstractBlockingMapQueue.this.containsValue((V) value);
        }

        @SneakyThrows
        @Override
        public V put(K key, V value) {
            return AbstractBlockingMapQueue.this.put(key, value);
        }

        @SneakyThrows
        @Override
        public void putAll(Map<? extends K, ? extends V> m) {
            AbstractBlockingMapQueue.this.putAll(m);
        }

        @SneakyThrows
        @Override
        public V putIfAbsent(@NonNull K key, V value) {
            return AbstractBlockingMapQueue.this.putIfAbsent(key, value);
        }

        @Override
        public V remove(Object key) {
            return AbstractBlockingMapQueue.this.remove((K) key);
        }

        @Override
        public void clear() {
            AbstractBlockingMapQueue.this.clear();
        }

        @NonNull
        @Override
        public Set<K> keySet() {
            return AbstractBlockingMapQueue.this.keySet();
        }

        @NonNull
        @Override
        public Collection<V> values() {
            return AbstractBlockingMapQueue.this.values();
        }

        @NonNull
        @Override
        public Set<Entry<K, V>> entrySet() {
            return AbstractBlockingMapQueue.this.entrySet();
        }

        @Override
        public int hashCode() {
            return AbstractBlockingMapQueue.this.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return AbstractBlockingMapQueue.this.equals(o);
        }

        @Override
        public boolean remove(@NonNull Object key, Object value) {
            return AbstractBlockingMapQueue.this.remove((K) key, (V) value);
        }

        @Override
        public boolean replace(@NonNull K key, @NonNull V oldValue, @NonNull V newValue) {
            return AbstractBlockingMapQueue.this.replace(key, oldValue, newValue);
        }

        @Override
        public V replace(@NonNull K key, @NonNull V value) {
            return AbstractBlockingMapQueue.this.replace(key, value);
        }

        @Override
        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
            AbstractBlockingMapQueue.this.replaceAll(function);
        }

        @Override
        public void forEach(BiConsumer<? super K, ? super V> action) {
            AbstractBlockingMapQueue.this.forEach(action);
        }

        @SneakyThrows
        @Override
        public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
            return AbstractBlockingMapQueue.this.computeIfAbsent(key, mappingFunction);
        }

        @SneakyThrows
        @Override
        public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            return AbstractBlockingMapQueue.this.computeIfPresent(key, remappingFunction);
        }

        @SneakyThrows
        @Override
        public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            return AbstractBlockingMapQueue.this.compute(key, remappingFunction);
        }

        @SneakyThrows
        @Override
        public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            return AbstractBlockingMapQueue.this.merge(key, value, remappingFunction);
        }

        @Override
        public String toString() {
            return AbstractBlockingMapQueue.this.toString();
        }
    }

    @SuppressWarnings("unchecked")
    private class QueueImpl extends AbstractQueue<V> implements BlockingQueue<V> {

        @Override
        public int size() {
            return AbstractBlockingMapQueue.this.size();
        }

        @Override
        public int remainingCapacity() {
            return AbstractBlockingMapQueue.this.remainingCapacity();
        }

        @Override
        public void put(@NonNull V v) throws InterruptedException {
            if (v instanceof MapQueueElement) {
                K key = (K) ((MapQueueElement<?>) v).getKey();
                AbstractBlockingMapQueue.this.put(key, v);
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean offer(V v) {
            if (v instanceof MapQueueElement) {
                K key = (K) ((MapQueueElement<?>) v).getKey();
                return AbstractBlockingMapQueue.this.offer(key, v);
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean offer(V v, long timeout, @NonNull TimeUnit unit) throws InterruptedException {
            if (v instanceof MapQueueElement) {
                K key = (K) ((MapQueueElement<?>) v).getKey();
                return AbstractBlockingMapQueue.this.offer(key, v, timeout, unit);
            }
            throw new UnsupportedOperationException();
        }

        @NonNull
        @Override
        public V take() throws InterruptedException {
            return AbstractBlockingMapQueue.this.takeValue();
        }

        @Override
        public V poll() {
            return AbstractBlockingMapQueue.this.pollValue();
        }

        @Override
        public V poll(long timeout, @NonNull TimeUnit unit) throws InterruptedException {
            return AbstractBlockingMapQueue.this.pollValue(timeout, unit);
        }

        @Override
        public V peek() {
            return AbstractBlockingMapQueue.this.peekValue();
        }

        @Override
        public boolean remove(Object o) {
            return AbstractBlockingMapQueue.this.removeValue((V) o);
        }

        @Override
        public boolean contains(Object o) {
            return AbstractBlockingMapQueue.this.containsValue((V) o);
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return AbstractBlockingMapQueue.this.toValueArray();
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] a) {
            return AbstractBlockingMapQueue.this.toValueArray(a);
        }

        @Override
        public String toString() {
            return AbstractBlockingMapQueue.this.toString();
        }

        @Override
        public void clear() {
            AbstractBlockingMapQueue.this.clear();
        }

        @Override
        public int drainTo(@NonNull Collection<? super V> c) {
            return AbstractBlockingMapQueue.this.drainValueTo(c);
        }

        @Override
        public int drainTo(@NonNull Collection<? super V> c, int maxElements) {
            return AbstractBlockingMapQueue.this.drainValueTo(c, maxElements);
        }

        @Override
        public Iterator<V> iterator() {
            return AbstractBlockingMapQueue.this.valueIterator();
        }

        @Override
        public Spliterator<V> spliterator() {
            return AbstractBlockingMapQueue.this.valueSpliterator();
        }
    }
}
